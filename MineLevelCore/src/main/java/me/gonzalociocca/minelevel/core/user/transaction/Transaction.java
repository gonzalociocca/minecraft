package me.gonzalociocca.minelevel.core.user.transaction;

/**
 *
 * @author cuack
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;
import org.bukkit.Material;

public class Transaction
{
private String aItem;
private Long aStartdate;
private Long aStopdate;
  
public Transaction(String item, long startdate, long stopdate) {
aItem = item;
aStartdate = startdate;
aStopdate = stopdate;
}

public boolean isExpired(){
    return aStartdate > aStopdate;
}

//short command ej: kitvip3

public String getItem(){
    return aItem;
}

public int daysLeft(){
    if(!isExpired()){
        Long left = aStopdate - System.currentTimeMillis();
        int days = (int)(left / 86400000);
        return days;
    }else{
        return 0;
    }
}


}