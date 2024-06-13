package org.Lonk.Items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemManager {

    public static ItemStack Test;
    public static ItemStack LonksMurderSword;

    public static void init() {
        createTest();
        createLonksMurderSword();
    }

    private static void createTest() {
        Test = ItemCreator.createItem("Test", new ItemStack(Material.DEAD_BUSH).getType(), 1.0, 1.0, 1.0, 1.0, "Test", "Test", false, false);
    }
    private static void createLonksMurderSword() {
        LonksMurderSword = ItemCreator.createItem("Lonk_Murder_Sword", new ItemStack(Material.NETHERITE_SWORD).getType(), 999.0, -5.0, 50.0, -50.0, "&cLonk's Murder Sword", "Lonk Murdered Somone With This", true, true);
    }

}
