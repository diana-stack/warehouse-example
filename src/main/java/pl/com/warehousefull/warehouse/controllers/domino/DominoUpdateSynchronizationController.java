package pl.com.warehousefull.warehouse.controllers.domino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.com.warehousefull.warehouse.helpers.domino.DateTimeHelper;
import pl.com.warehousefull.warehouse.services.domino.DominoUpdateSynchronizationService;

@RestController
@RequestMapping("/domino")
public class DominoUpdateSynchronizationController {

    @Autowired
    private DominoUpdateSynchronizationService dominoUpdateSynchronizationService;
    @Autowired
    private DateTimeHelper dateTimeHelper;

    @GetMapping("/update/all-db")
    public ResponseEntity<String> autoUpdate(@RequestParam(name = "id") int configOriginDbId) throws Exception {
        System.out.println("Start create: " + dateTimeHelper.getActualDateTime());
        dominoUpdateSynchronizationService.autoUpdateSync(configOriginDbId);
        System.out.println("End create: " + dateTimeHelper.getActualDateTime());
        return ResponseEntity.ok("Update documents end successfully");
    }

    @GetMapping("/update/table")
    public ResponseEntity<String> manualUpdate(@RequestParam(name = "id") int configOriginTableDbId) throws Exception {
        System.out.println("Start create: " + dateTimeHelper.getActualDateTime());
        dominoUpdateSynchronizationService.manualUpdateSync(configOriginTableDbId);
        System.out.println("End create: " + dateTimeHelper.getActualDateTime());
        return ResponseEntity.ok("Update documents end successfully");
    }

}
