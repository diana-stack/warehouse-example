package pl.com.warehousefull.warehouse.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import pl.com.warehousefull.warehouse.controllers.domino.DominoAutoUpdateSynchronizationController;
import pl.com.warehousefull.warehouse.controllers.enova.EnovaAutoSynchronizationController;
import pl.com.warehousefull.warehouse.controllers.pivotal.PivotalAutoSynchronizationController;
import pl.com.warehousefull.warehouse.models.config.agent.ConfigUpdateAgent;
import pl.com.warehousefull.warehouse.repositories.config.ConfigUpdateAgentDbRepository;

import java.time.LocalTime;
import java.util.List;

@Component
public class UpdateAgentSynchronizationComponent {
    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private ConfigUpdateAgentDbRepository configUpdateAgentDbRepository;
    @Autowired
    private DominoAutoUpdateSynchronizationController dominoAutoUpdateSynchronizationController;
    @Autowired
    private EnovaAutoSynchronizationController enovaAutoSynchronizationController;
    @Autowired
    private PivotalAutoSynchronizationController pivotalAutoSynchronizationController;

    @Scheduled(cron = "0 10 15 * * *")
    public void scheduleDailyCreateTasks(){
        List<ConfigUpdateAgent> configUpdateAgentList = configUpdateAgentDbRepository.findAll();
        for (ConfigUpdateAgent configUpdateAgent : configUpdateAgentList) {
            LocalTime scheduledTime = LocalTime.parse(configUpdateAgent.getStartTime());
            String dbName = configUpdateAgent.getWarehouseDbName();
            CronTrigger cronTrigger = new CronTrigger(getCronExpression(scheduledTime));
            if(dbName.equals("WH_ENOVA")){
                Runnable task = () -> {
                    try {
                        enovaAutoSynchronizationController.autoUpdate();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                };
                taskScheduler.schedule(task, cronTrigger);
            } else if(dbName.equals("WH_PIVOTAL")){
                Runnable task = () -> {
                    try {
                        pivotalAutoSynchronizationController.autoUpdate();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                };
                taskScheduler.schedule(task, cronTrigger);
            } else if(dbName.equals("WH_DOMINO")){
                Runnable task = () -> {
                    try {
                        dominoAutoUpdateSynchronizationController.autoUpdate();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                };
                taskScheduler.schedule(task, cronTrigger);
            }
        }
    }

    private String getCronExpression(LocalTime time) {
        return "0 " + time.getMinute() + " " + time.getHour() + " * * MON-FRI";
    }
}

