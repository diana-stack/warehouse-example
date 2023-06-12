package pl.com.warehousefull.warehouse.services.agents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.com.warehousefull.warehouse.models.config.agent.ConfigUpdateAgent;
import pl.com.warehousefull.warehouse.repositories.config.ConfigUpdateAgentDbRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigUpdateAgentService {
    @Autowired
    private ConfigUpdateAgentDbRepository configUpdateAgentDbRepository;

    public void save(@RequestBody ConfigUpdateAgent configUpdateAgent){
        configUpdateAgentDbRepository.save(configUpdateAgent);
    }

    public void delete(@RequestBody ConfigUpdateAgent configUpdateAgent){
        configUpdateAgentDbRepository.delete(configUpdateAgent);
    }

    public void update(ConfigUpdateAgent configUpdateAgentNew){
        ConfigUpdateAgent configUpdateAgentOld = configUpdateAgentDbRepository.findById(configUpdateAgentNew.getStartTime()).get();
        configUpdateAgentOld.setStartTime(configUpdateAgentNew.getStartTime());
        configUpdateAgentOld.setWarehouseDbName(configUpdateAgentNew.getWarehouseDbName());
        configUpdateAgentOld.setConfigOriginDbIds(configUpdateAgentNew.getConfigOriginDbIds());
        configUpdateAgentDbRepository.save(configUpdateAgentOld);
    }


    public List<ConfigUpdateAgent> getAll(){
        return configUpdateAgentDbRepository.findAll();
    }

    public Optional<ConfigUpdateAgent> getById(String startTime) {
        return configUpdateAgentDbRepository.findById(startTime);
    }

    public Optional<ConfigUpdateAgent> createNewEntry(){
        Optional<ConfigUpdateAgent> configUpdateAgent = Optional.of(new ConfigUpdateAgent());
        return configUpdateAgent;
    }
}
