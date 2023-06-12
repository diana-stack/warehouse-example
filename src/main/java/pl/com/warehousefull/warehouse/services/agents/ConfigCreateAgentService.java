package pl.com.warehousefull.warehouse.services.agents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.com.warehousefull.warehouse.models.config.agent.ConfigCreateAgent;
import pl.com.warehousefull.warehouse.repositories.config.ConfigCreateAgentDbRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigCreateAgentService {
    @Autowired
    private ConfigCreateAgentDbRepository configCreateAgentDbRepository;

    public void save(@RequestBody ConfigCreateAgent configCreateAgent){
        configCreateAgentDbRepository.save(configCreateAgent);
    }

    public void delete(@RequestBody ConfigCreateAgent configCreateAgent){
        configCreateAgentDbRepository.delete(configCreateAgent);
    }

    public void update(ConfigCreateAgent configCreateAgentNew){
        ConfigCreateAgent configCreateAgentOld = configCreateAgentDbRepository.findById(configCreateAgentNew.getStartTime()).get();
        configCreateAgentOld.setStartTime(configCreateAgentNew.getStartTime());
        configCreateAgentOld.setConfigOriginDbIds(configCreateAgentNew.getConfigOriginDbIds());
        configCreateAgentOld.setWarehouseDbName(configCreateAgentNew.getWarehouseDbName());
        configCreateAgentDbRepository.save(configCreateAgentOld);
    }


    public List<ConfigCreateAgent> getAll(){
        return configCreateAgentDbRepository.findAll();
    }

    public Optional<ConfigCreateAgent> getById(String startTime) {
        return configCreateAgentDbRepository.findById(startTime);
    }

    public Optional<ConfigCreateAgent> createNewEntry(){
        Optional<ConfigCreateAgent> configCreateAgent = Optional.of(new ConfigCreateAgent());
        return configCreateAgent;
    }
}
