package org.Lonk.Items;

import org.Lonk.RogueLonk;
import org.bukkit.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import static org.Lonk.Items.ItemCreator.createItem;

public class ItemManager {

    public static ItemStack Test;
    public static ItemStack LonksMurderSword;

    public static ItemStack helmet;
    public static ItemStack chestplate;
    public static ItemStack leggings;
    public static ItemStack boots;
    public static ItemStack mainHand;

    public static ItemStack PlayerHeadItem;
    public static ItemStack Playerhead;



    public static void init() {
        createTest();
        createLonksMurderSword();
        createFullSet();
        createPlayerHeadItem();

    }

    private static void createFullSet() {
        helmet = createItem("helmet_id", Material.DIAMOND_HELMET, 0.0, 0.0, 0.0, 10.0, "Helmet_of_Power", "The_legendary_helmet_of_power", false, true, " ", EquipmentSlot.HEAD, Boolean.TRUE);
        chestplate = createItem("chestplate_id", Material.DIAMOND_CHESTPLATE, 0.0, 0.0, 0.0, 20.0, "Chestplate_of_Defense", "The_legendary_chestplate_of_defense", false, true,  " ", EquipmentSlot.CHEST, Boolean.TRUE);
        leggings = createItem("leggings_id", Material.DIAMOND_LEGGINGS, 0.0, 0.0, 0.0, 15.0, "Leggings_of_Agility", "The_legendary_leggings_of_agility", false, true, " ", EquipmentSlot.LEGS, Boolean.TRUE);
        boots = createItem("boots_id", Material.DIAMOND_BOOTS, 0.0, 0.0, 0.0, 10.0, "Boots_of_Speed", "The_legendary_boots_of_speed", false, true, " ", EquipmentSlot.FEET, Boolean.TRUE);
        mainHand = createItem("sword_id", Material.DIAMOND_SWORD, 10.0, 0.0, 0.0, 5.0, "Sword_of_Power", "The_legendary_sword_of_power", true, true, " ", EquipmentSlot.HAND, Boolean.TRUE);
    }
    private static void createTest() {
        Test = createItem("Test", new ItemStack(Material.DEAD_BUSH).getType(), 1.0, 1.0, 1.0, 1.0, "Test", "Test", false, false," ", EquipmentSlot.HAND, Boolean.TRUE);
    }
    private static void createLonksMurderSword() {
        LonksMurderSword = createItem("Lonk_Murder_Sword", new ItemStack(Material.NETHERITE_SWORD).getType(), 999.0, -5.0, 50.0, -50.0, "&cLonk's Murder Sword", "Lonk Murdered Somone With This", true, true, " ", EquipmentSlot.HAND, Boolean.TRUE);
    }

    private static void createPlayerHeadItem(){
        Playerhead = CustomSkullCreator.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2MwOTQ3NjUyMDliOGUyNTlhODFkYmU5ZjA3ZDlhNDRhNDZhMjAyMWMyZWRjMjg1M2VjNGMzZDFjODJlNWE4In19fQ");
        PlayerHeadItem = createItem("playerHead", CustomSkullCreator.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2MwOTQ3NjUyMDliOGUyNTlhODFkYmU5ZjA3ZDlhNDRhNDZhMjAyMWMyZWRjMjg1M2VjNGMzZDFjODJlNWE4In19fQ==").getType(), 10D, 0D,10D ,10D, "head", "head", true, true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2MwOTQ3NjUyMDliOGUyNTlhODFkYmU5ZjA3ZDlhNDRhNDZhMjAyMWMyZWRjMjg1M2VjNGMzZDFjODJlNWE4In19fQ==", EquipmentSlot.HAND, Boolean.FALSE);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.AQUA,3);
        ItemMeta meta = ParticleBeam.setBeam(PlayerHeadItem, 10D, dustOptions, Particle.REDSTONE, 10D, 1D, true, 20);
        PlayerHeadItem.setItemMeta(meta);
        RogueLonk plugin = JavaPlugin.getPlugin(RogueLonk.class);
        plugin.saveItem("playerHead", PlayerHeadItem);
    }

}
