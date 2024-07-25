package org.Lonk.Stats;

import org.Lonk.RogueLonk;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

public class DamageIndecator implements Listener {
    RogueLonk plugin = JavaPlugin.getPlugin(RogueLonk.class);

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {

        if (event.isCancelled()) return;

        //get damage
        long damage = Math.round(event.getFinalDamage());
        String damageString = String.valueOf(damage);

        //get location of damaged entity
        Location loc = event.getEntity().getLocation().add(0, 1, 0);
        //get random loc
        double x = loc.getX() + ThreadLocalRandom.current().nextDouble(-1, 1);
        double y = loc.getY() + ThreadLocalRandom.current().nextDouble(-1, 1);
        double z = loc.getZ() + ThreadLocalRandom.current().nextDouble(-1, 1);
        Location randomLoc = new Location(loc.getWorld(), x, y, z);
        //spawn damage indecator
        TextDisplay display = loc.getWorld().spawn(randomLoc, TextDisplay.class);
        display.setText("§c" + damageString);
        display.setBillboard(Display.Billboard.CENTER);
        new BukkitRunnable() {
            @Override
            public void run() {
                display.remove();
            }
        }.runTaskLater(plugin, 20);




    }

}
