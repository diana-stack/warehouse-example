
package pl.com.warehousefull.warehouse.parser.domino;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class DateParser {

    public static String formatDateTime(String dateTime) {
        try {
            if(dateTime.length()==10){
                if(formatDate(dateTime) != null && formatDate(dateTime) != ""){
                    return formatDate(dateTime) + " 00:00:00";
                } else {
                    return null;
                }
            } else if(dateTime != null && !dateTime.isEmpty() && dateTime.length() >= 19){
                dateTime = dateTime.substring(0, 19);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd/MM/yyyy HH:mm:ss][dd.MM.yyyy HH:mm:ss][yyyy.MM.dd HH:mm:ss][yyyy/MM/dd HH:mm:ss][yyyy.dd.MM HH:mm:ss][dd-MM-yyyy HH:mm:ss][yyyy-MM-dd HH:mm:ss][yyyy-dd-MM HH:mm:ss]");
                LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
                return localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            }
        } catch (DateTimeParseException e) {
            return null;
        }
        return null;
    }

    public static String formatDate(String date) {
        try{
            if(date.length() > 10){
                date = date.substring(0, 10);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd/MM/yyyy][dd.MM.yyyy][yyyy.MM.dd][yyyy/MM/dd][yyyy.dd.MM][dd-MM-yyyy][yyyy-MM-dd][yyyy-dd-MM]");
            LocalDate localDate = LocalDate.parse(date, formatter);
            return localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (Exception e) {
            return null;
        }
    }
}
