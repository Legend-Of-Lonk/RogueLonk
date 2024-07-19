package org.Lonk.Mobs.Bosses;

import org.Lonk.Items.ItemManager;
import org.Lonk.Mobs.MobCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

public class BossManager {
    public static Entity ZombKing;

    public static void init(){
        CreateZombKing();
    }

    private static void CreateZombKing(){
        // Code here
        LivingEntity mob = (Zombie) Bukkit.getServer().getWorlds().get(0).spawnEntity(new Location(Bukkit.getServer().getWorlds().get(0), 0, 0, 0), EntityType.ZOMBIE);
        mob.remove();

        //Equipment
        ItemStack helmet = ItemManager.helmet;
        ItemStack chestplate = ItemManager.chestplate;
        ItemStack leggings = ItemManager.leggings;
        ItemStack boots = ItemManager.boots;
        ItemStack mainHand = ItemManager.LonksMurderSword;

        ZombKing = MobCreator.MobCreator("ZombKing", mob, 100.0, 10.0, 0.25, chestplate, leggings, boots, helmet, mainHand,new ItemStack(Material.AIR) ,1);
    }

}
