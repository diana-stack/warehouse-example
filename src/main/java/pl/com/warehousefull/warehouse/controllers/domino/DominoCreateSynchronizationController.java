package pl.com.warehousefull.warehouse.controllers.domino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.warehousefull.warehouse.helpers.domino.DateTimeHelper;
import pl.com.warehousefull.warehouse.services.domino.DominoCreateSynchronizationService;

@RestController
@RequestMapping("/domino")
public class DominoCreateSynchronizationController {
    @Autowired
    private DominoCreateSynchronizationService dominoCreateSynchronizationService;
    @Autowired
    private DateTimeHelper dateTimeHelper;

    @GetMapping("/create/table")
    public ResponseEntity<String> manualCreate(@RequestParam(name = "id") int configOriginDbId) throws Exception {
        System.out.println("Start create: " + dateTimeHelper.getActualDateTime());
        dominoCreateSynchronizationService.manualCreateSync(configOriginDbId);
        System.out.println("End create: " + dateTimeHelper.getActualDateTime());
        return ResponseEntity.ok("Create documents end successfully");
    }

    @GetMapping("/create/all-db")
    public ResponseEntity<String> autoCreate(@RequestParam(name = "id") int configOriginTableDbId) throws Exception {
        System.out.println("Start create: " + dateTimeHelper.getActualDateTime());
        dominoCreateSynchronizationService.autoCreateSync(configOriginTableDbId);
        System.out.println("End create: " + dateTimeHelper.getActualDateTime());
        return ResponseEntity.ok("Create documents end successfully");
    }

}
