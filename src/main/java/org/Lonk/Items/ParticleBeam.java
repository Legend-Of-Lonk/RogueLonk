package org.Lonk.Items;
import org.Lonk.RogueLonk;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ParticleBeam {

    public static ItemMeta setBeam(ItemStack item, Double Beam_dmg, Particle.DustOptions dustOptions, Particle BEAM_PARTICLE, Double BEAM_LENGTH, Double BEAM_RADIUS, Boolean useDustOptions, Integer cooldown) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey beamdmg = new NamespacedKey("roguelonk", "beamdmg");
        meta.getPersistentDataContainer().set(beamdmg, PersistentDataType.DOUBLE, Beam_dmg);

        NamespacedKey dustOptionsKey = new NamespacedKey("roguelonk", "dustoptions");
        meta.getPersistentDataContainer().set(dustOptionsKey, PersistentDataType.STRING, dustOptionsToString(dustOptions));

        NamespacedKey beamParticleKey = new NamespacedKey("roguelonk", "beamparticle");
        meta.getPersistentDataContainer().set(beamParticleKey, PersistentDataType.STRING, BEAM_PARTICLE.toString());

        NamespacedKey beamLengthKey = new NamespacedKey("roguelonk", "beamlength");
        meta.getPersistentDataContainer().set(beamLengthKey, PersistentDataType.DOUBLE, BEAM_LENGTH);

        NamespacedKey beamRadiusKey = new NamespacedKey("roguelonk", "beamradius");
        meta.getPersistentDataContainer().set(beamRadiusKey, PersistentDataType.DOUBLE, BEAM_RADIUS);

        NamespacedKey useDustOptionsKey = new NamespacedKey("roguelonk", "usedustoptions");
        meta.getPersistentDataContainer().set(useDustOptionsKey, PersistentDataType.BOOLEAN, useDustOptions);

        NamespacedKey cooldownKey = new NamespacedKey("roguelonk", "cooldown");
        meta.getPersistentDataContainer().set(cooldownKey, PersistentDataType.INTEGER, cooldown);

        NamespacedKey UseKey = new NamespacedKey("roguelonk", "usebeam");
        meta.getPersistentDataContainer().set(UseKey, PersistentDataType.BOOLEAN, true);
        return meta;
    }

    public static String dustOptionsToString(Particle.DustOptions dustOptions) {
        Color color = dustOptions.getColor();
        float size = dustOptions.getSize();
        return color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "," + size;
    }



    public static void shootBeam(Player player, Double BEAM_DAMAGE, Particle.DustOptions dustOptions, Particle BEAM_PARTICLE, Double BEAM_LENGTH, Double BEAM_RADIUS, Boolean useDustOptions, Integer cooldown) {



        World world = player.getWorld();
        Location start = player.getEyeLocation(); // Start at the player's eye level for accuracy

        Vector direction = player.getLocation().getDirection();

        if(player.hasCooldown(player.getInventory().getItemInMainHand().getType())){
            return;
        }



        new BukkitRunnable() {
            double distanceTravelled = 0;

            @Override
            public void run() {
                if (distanceTravelled > BEAM_LENGTH) {
                    this.cancel(); // Stop the beam when it has reached its maximum length
                }

                Location point = start.clone().add(direction.clone().multiply(distanceTravelled));
                if (useDustOptions) {
                    world.spawnParticle(BEAM_PARTICLE, point, 1, dustOptions); // Spawn the beam particle
                } else {
                    world.spawnParticle(BEAM_PARTICLE, point, 1); // Spawn the beam particle
                }
                Material blockType = point.getBlock().getType();


                // Check the block type
                if (blockType != Material.WATER  && blockType != Material.AIR && blockType != Material.TALL_GRASS && blockType != Material.SHORT_GRASS && blockType != Material.DEAD_BUSH && blockType != Material.BRAIN_CORAL && blockType != Material.BUBBLE_CORAL) {
                    this.cancel(); // Stop the beam if the block type is not water, grass, leaves, or air
                    return;
                }

                // Check for entities near this particle and damage them
                for (Entity entity : world.getNearbyEntities(point, BEAM_RADIUS, BEAM_RADIUS, BEAM_RADIUS)) {
                    if (entity instanceof LivingEntity && !(entity == player)) {
                        ((LivingEntity) entity).damage(BEAM_DAMAGE, player);
                    }
                }

                distanceTravelled += 0.5; // Advance the beam by 0.5 blocks
            }
        }.runTaskTimer(RogueLonk.getInstance(), 0L, 1L); // Schedule the runnable to run every tick (20 ticks = 1 second)
        player.setCooldown(player.getInventory().getItemInMainHand().getType(), cooldown);
    }
}

