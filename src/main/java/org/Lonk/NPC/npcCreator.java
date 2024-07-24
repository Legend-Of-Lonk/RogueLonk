package org.Lonk.NPC;

import org.Lonk.RogueLonk;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;



public class npcCreator {


    public static List<String> getAllNPCIDs() {
        FileConfiguration npcConfig = RogueLonk.getPlugin(RogueLonk.class).getNpcConfig();
        if (npcConfig != null) {
            Set<String> keys = npcConfig.getKeys(false); // Get all keys at the root level
            return new ArrayList<>(keys); // Convert the Set to a List and return it
        }
        return new ArrayList<>(); // Return an empty list if the NPC config is null
    }
    public static LivingEntity npcCreator(String name, Entity npc, String skin, String ID){
        // Code here
        RogueLonk plugin = RogueLonk.getPlugin(RogueLonk.class);

        if (getAllNPCIDs().contains(ID)) {
            LivingEntity existingNPC = plugin.loadNPCbyID(ID, new Location(Bukkit.getServer().getWorlds().get(0),0,0,0));
            existingNPC.remove();
            if (existingNPC != null) {
                return existingNPC;
            }
        }

        if (npc instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) npc;
            livingEntity.setCustomName(name);
            livingEntity.setCustomNameVisible(true);
            livingEntity.setPersistent(true);
            livingEntity.setRemoveWhenFarAway(false);
            livingEntity.setAI(false);
            livingEntity.setInvulnerable(true);
            livingEntity.setCollidable(false);
            livingEntity.setCanPickupItems(false);
            livingEntity.setSilent(true);
            livingEntity.setGlowing(false);
            livingEntity.setGravity(false);
            livingEntity.setSwimming(false);
            livingEntity.setFireTicks(0);
            livingEntity.setHealth(20);
            livingEntity.getPersistentDataContainer().set(new NamespacedKey("roguelonk","npcid"), PersistentDataType.STRING, ID);
            livingEntity.getPersistentDataContainer().set(new NamespacedKey("roguelonk","skin"), PersistentDataType.STRING, skin);

            plugin.saveNPC(ID,(Creature) livingEntity);

            return livingEntity;
        }

        return null;
    }

    public void makeNpcsLookAtNearestPlayer() {
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                if (isNpc(entity)) {
                    Player nearestPlayer = getNearestPlayer(entity);
                    if (nearestPlayer != null) {
                        faceEntity(entity,nearestPlayer);
                    }
                }
            }
        }
    }

    private void faceEntity(Entity entity, Entity target) {
        Location location = entity.getLocation();
        double dx = target.getLocation().getX() - location.getX();
        double dy = target.getLocation().getY() - location.getY();
        double dz = target.getLocation().getZ() - location.getZ();

        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        double distanceY = Math.sqrt(distanceXZ * distanceXZ + dy * dy);

        double yaw = Math.acos(dx / distanceXZ) * 180 / Math.PI;
        double pitch = Math.acos(dy / distanceY) * 180 / Math.PI - 90;
        if (dz < 0.0) {
            yaw += Math.abs(180 - yaw) * 2;
        }

        entity.teleport(new Location(entity.getWorld(), location.getX(), location.getY(), location.getZ(), (float) yaw - 90, (float) pitch));
    }

    private boolean isNpc(LivingEntity entity) {
        // Replace this with your own method of determining whether an entity is an NPC
        return entity.getPersistentDataContainer().has(new NamespacedKey("roguelonk", "npcid"), PersistentDataType.STRING);
    }

    private Player getNearestPlayer(LivingEntity entity) {
        double closestDistance = Double.MAX_VALUE;
        Player closestPlayer = null;

        for (Entity nearbyEntity : entity.getNearbyEntities(100, 100, 100)) {
            if (nearbyEntity instanceof Player) {
                double distance = nearbyEntity.getLocation().distance(entity.getLocation());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestPlayer = (Player) nearbyEntity;
                }
            }
        }

        return closestPlayer;
    }

}
