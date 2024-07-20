package org.Lonk.Mobs;

import org.Lonk.RogueLonk;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

public class MobManager {
    public static Entity zomb;

    public static void innit(){
        CreateZomb();
    }

    private static void CreateZomb(){
        LivingEntity mob = (Zombie) Bukkit.getServer().getWorlds().get(0).spawnEntity(new Location(Bukkit.getServer().getWorlds().get(0), 0, 0, 0), EntityType.ZOMBIE);
        mob.remove(); // Remove the mob immediately after creation

        // Define the mob's equipment
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemStack mainHand = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack offHand = new ItemStack(Material.SHIELD);

        // Set the mob's attributes and equipment
        zomb = MobCreator.MobCreator("custom_zombie", mob, 40.0, 10.0, 0.25, chestplate, leggings, boots, helmet, mainHand, offHand, 1);

    }


}
