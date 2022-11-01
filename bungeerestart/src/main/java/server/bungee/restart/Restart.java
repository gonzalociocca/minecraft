package server.bungee.restart;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

public class Restart extends Plugin implements Listener {

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, this);

        UpdateTick();

    }

    boolean shuttingdown = false;
    public void UpdateTick() {
        getProxy().getScheduler().schedule(this, new Runnable() {
        @Override
        public void run() {
            ZoneId zoneId = ZoneId.of("America/Argentina/Buenos_Aires");
            ZonedDateTime zdt = ZonedDateTime.now(zoneId);
            java.util.Date date = java.util.Date.from(zdt.toInstant());
            if (date.getHours() == 5) {
                if (date.getMinutes() == 59) {
                    if (!shuttingdown) {
                        shuttingdown = true;
                        getProxy().stop("Reiniciando..");
                    }
                }
                if (date.getMinutes() == 57) {
                    getProxy().broadcast("Reinicio del server en 1 Minuto");
                }
            }
        }
    }, 30L, 30L, TimeUnit.SECONDS);
    }


}
  

   
    
    
    
