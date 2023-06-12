package pl.com.warehousefull.warehouse.helpers.domino;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeHelper {
    public String getActualDateTime(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime startSync = LocalDateTime.now(ZoneId.of("Europe/Warsaw"));
        return startSync.format(dateTimeFormatter);
    }
}
