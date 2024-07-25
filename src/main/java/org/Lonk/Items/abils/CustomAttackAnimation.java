package org.Lonk.Items.abils;

import org.Lonk.RogueLonk;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class CustomAttackAnimation implements Listener {
    RogueLonk plugin = RogueLonk.getPlugin(RogueLonk.class);

    @EventHandler
    public void onPlayerInterract( PlayerInteractEvent event ) {

        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            NamespacedKey IDKey = new NamespacedKey("roguelonk", "id");
            Player player = event.getPlayer();
            if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(IDKey, PersistentDataType.STRING)) {

                event.setCancelled(true);
                event.getPlayer().setMetadata("antiBezierDamageStack", new FixedMetadataValue(plugin, true));
                drawCurveAroundPlayer(event.getPlayer(), 2);
                player.removeMetadata("antiBezierDamageStack", plugin);


            }
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {




        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            NamespacedKey IDKey = new NamespacedKey("roguelonk", "id");
            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(IDKey, PersistentDataType.STRING)) {
                if (! player.hasMetadata("antiBezierDamageStack")) {
                    if (!player.hasMetadata("BezierCurveAttack")) {
                        // set the metadata
                        player.setMetadata("BezierCurveAttack", new FixedMetadataValue(plugin, true));

                    } else {
                        // Cancel the regular attack
                        event.setCancelled(true);
                        // Trigger your Bezier curve attack
                        player.removeMetadata("BezierCurveAttack", plugin);
                        drawCurveAroundPlayer(player, 2);
                    }
                }
            }
        }
    }

    public Location calculateBezierPoint(double t, Location p1, Location p2, Location p3) {
        double x = Math.pow(1 - t, 2) * p1.getX() + 2 * (1 - t) * t * p2.getX() + Math.pow(t, 2) * p3.getX();
        double y = Math.pow(1 - t, 2) * p1.getY() + 2 * (1 - t) * t * p2.getY() + Math.pow(t, 2) * p3.getY();
        double z = Math.pow(1 - t, 2) * p1.getZ() + 2 * (1 - t) * t * p2.getZ() + Math.pow(t, 2) * p3.getZ();
        return new Location(p1.getWorld(), x, y, z);
    }

    public Location lerp(Location point1, Location point2, double t) {
        double x = (1 - t) * point1.getX() + t * point2.getX();
        double y = (1 - t) * point1.getY() + t * point2.getY();
        double z = (1 - t) * point1.getZ() + t * point2.getZ();
        return new Location(point1.getWorld(), x, y, z);
    }

    private static final Random RANDOM = new Random();

    public void drawCurveAroundPlayer(Player player, double radius) {


        Location playerLocation = player.getLocation().add(0D,1.5D,0);

        double angle = 2 * Math.PI * RANDOM.nextDouble();

        // Add 90 degrees to the angle
        angle += Math.PI / 2;

        Vector offset = new Vector(0, radius * Math.sin(angle), radius * Math.cos(angle));

        // Rotate the offset vector by the player's pitch
        offset.rotateAroundX(Math.toRadians(-playerLocation.getPitch()));


        Location p0 = playerLocation.clone().add(offset);
        p0.add(RANDOM.nextDouble() - 0.5, RANDOM.nextDouble() - 0.5, RANDOM.nextDouble() - 0.5); // Add random offset to p0

        offset.rotateAroundX(Math.PI);
        Location p2 = playerLocation.clone().add(offset);
        p2.add(RANDOM.nextDouble() - 0.5, RANDOM.nextDouble() - 0.5, RANDOM.nextDouble() - 0.5); // Add random offset to p2

        // Calculate the control point p1 as a point that is in the direction of the player's view and a certain distance away from the player's location
        Location p1 = playerLocation.clone().add(playerLocation.getDirection().multiply(radius));
        p1.add(RANDOM.nextDouble() - 0.5, RANDOM.nextDouble() - 0.5, RANDOM.nextDouble() - 0.5);

        HashMap<UUID, HashMap<String,Double>> playerData = plugin.getPlayerStats();

        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1);

        Bukkit.getServer().getWorlds().get(0).spawnParticle(Particle.REDSTONE, p0, 1, dustOptions);
        Bukkit.getServer().getWorlds().get(0).spawnParticle(Particle.REDSTONE, p1, 1, dustOptions);
        Bukkit.getServer().getWorlds().get(0).spawnParticle(Particle.REDSTONE, p2, 1, dustOptions);

        BezierCurveParticleAndDamage(player.getWorld(), p0, p1, p2, player,playerData.get(player.getUniqueId()).get("dmg"));
    }

    public void BezierCurveParticleAndDamage(World world, Location p0, Location p1, Location p2,Player player, Double Damage) {
        HashSet<Entity> damagedEntities = new HashSet<>();
        for (double t = 0; t <= 1; t += 0.01) {
            Location particleLocation = calculateBezierPoint(t, p0, p1, p2);
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1);
            Particle.DustTransition dustTransition = new Particle.DustTransition(Color.GRAY, Color.BLACK, 1);
            world.spawnParticle(Particle.DUST_COLOR_TRANSITION, particleLocation, 1, dustTransition);

            for (Entity entity : world.getNearbyEntities(particleLocation, 0.5, 0.5, 0.5)) {
                if (entity instanceof LivingEntity && !(entity == player) && !damagedEntities.contains(entity)) {

                    ((LivingEntity) entity).damage(Damage, player);
                    damagedEntities.add(entity);
                }
            }
        }
    }

    public void moveParticleAlongCurve(World world, Location p0, Location p1, Location p2) {
        new BukkitRunnable() {
            double t = 0;
            Location particleLocation = calculateBezierPoint(t, p0, p1, p2);

            @Override
            public void run() {
                if (t > 1) {
                    this.cancel();
                } else {
                    world.spawnParticle(Particle.FLAME, particleLocation, 1);
                    t += 0.01;
                    particleLocation = calculateBezierPoint(t, p0, p1, p2);
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

}
