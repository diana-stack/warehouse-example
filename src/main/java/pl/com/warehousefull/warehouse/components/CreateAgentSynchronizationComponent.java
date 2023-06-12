package pl.com.warehousefull.warehouse.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import pl.com.warehousefull.warehouse.controllers.domino.DominoAutoCreateSynchronizationController;
import pl.com.warehousefull.warehouse.controllers.enova.EnovaAutoSynchronizationController;
import pl.com.warehousefull.warehouse.controllers.pivotal.PivotalAutoSynchronizationController;
import pl.com.warehousefull.warehouse.models.config.agent.ConfigCreateAgent;
import pl.com.warehousefull.warehouse.repositories.config.ConfigCreateAgentDbRepository;

import java.time.LocalTime;
import java.util.List;

@Component
public class CreateAgentSynchronizationComponent {
    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private ConfigCreateAgentDbRepository configCreateAgentDbRepository;
    @Autowired
    private DominoAutoCreateSynchronizationController dominoAutoCreateSynchronizationController;
    @Autowired
    private EnovaAutoSynchronizationController enovaAutoSynchronizationController;
    @Autowired
    private PivotalAutoSynchronizationController pivotalAutoSynchronizationController;

    @Scheduled(cron = "0 0 10 * * FRI")
    public void scheduleDailyCreateTasks(){
        List<ConfigCreateAgent> configCreateAgentList = configCreateAgentDbRepository.findAll();
        for (ConfigCreateAgent configCreateAgent : configCreateAgentList) {
            LocalTime scheduledTime = LocalTime.parse(configCreateAgent.getStartTime());
            String dbName = configCreateAgent.getWarehouseDbName();
            CronTrigger cronTrigger = new CronTrigger(getCronExpression(scheduledTime));
            if(dbName.equals("WH_ENOVA")){
                Runnable task = () -> {
                    try {
                        enovaAutoSynchronizationController.autoCreate();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                };
                taskScheduler.schedule(task, cronTrigger);
            } else if(dbName.equals("WH_PIVOTAL")){
                Runnable task = () -> {
                    try {
                        pivotalAutoSynchronizationController.autoCreate();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                };
                taskScheduler.schedule(task, cronTrigger);
            } else if(dbName.equals("WH_DOMINO")){
                Runnable task = () -> {
                    try {
                        dominoAutoCreateSynchronizationController.autoCreate();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                };
                taskScheduler.schedule(task, cronTrigger);
            }
        }
    }

    private String getCronExpression(LocalTime time) {
        return "0 " + time.getMinute() + " " + time.getHour() + " ? * SAT,SUN";
    }
}

