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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Entity zombkingBeta = MobCreator.MobCreator("ZombKing", mob, 100.0, 10.0, 0.25, chestplate, leggings, boots, helmet, mainHand,new ItemStack(Material.AIR) ,1);


        Map<ItemStack, Double> ZombKingDrops = new HashMap<>();
        ZombKingDrops.put(ItemManager.chestplate, 0.5);
        ZombKingDrops.put(ItemManager.leggings, 0.5);
        ZombKingDrops.put(ItemManager.boots, 0.5);
        ZombKingDrops.put(ItemManager.helmet, 0.5);
        ZombKingDrops.put(ItemManager.LonksMurderSword, 0.5);

        ZombKing = MobCreator.MobDrops("ZombKing",ZombKingDrops, (LivingEntity) zombkingBeta);
    }

}
