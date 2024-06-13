package org.Lonk;

import org.Lonk.Commands.MainCommand;
import org.Lonk.Items.ItemCreator;
import org.Lonk.Items.ItemManager;
import org.Lonk.Stats.Stats;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@SuppressWarnings({"all"})
public final class RogueLonk extends JavaPlugin {

    public static HashMap<UUID, HashMap<String, Double>> playerStats = new HashMap<>();
    private File itemsFile;
    private FileConfiguration itemsConfig;
    private File mobFile;
    private FileConfiguration mobConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic
        createItemsConfig();
        createMobConfig();
        getServer().getPluginManager().registerEvents(new Stats(), this);
        ItemManager.init();


        getCommand("RLP").setExecutor(new MainCommand());
        Bukkit.getServer().getConsoleSender().sendMessage("§eRogueLonk has been enabled!");
        statUpdate(true);



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void createMobConfig() {
        mobFile = new File(getDataFolder(), "mobs.yml");
        if (!mobFile.exists()) {
            mobFile.getParentFile().mkdirs();
            saveResource("mobs.yml", false);
        }

        mobConfig = new YamlConfiguration();
        try {
            mobConfig.load(mobFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        mobConfig = YamlConfiguration.loadConfiguration(mobFile);
    }
    public FileConfiguration getMobConfig() {
        return this.mobConfig;
    }

    public void saveMob(String mobID, Creature mob){
        if(mobConfig.contains(mobID)){
            Bukkit.getServer().getConsoleSender().sendMessage("§cMob with ID " + mobID + " already exists!");
            mobConfig.set(mobID, mobID);
            mobConfig.set(mobID + ".EntityType", mob.getType().toString());
            mobConfig.set(mobID + ".Name", mob.getCustomName());
            mobConfig.set(mobID + ".HP", mob.getHealth());
            mobConfig.set(mobID + ".Dmg", mob.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue());
            mobConfig.set(mobID + ".Spd", mob.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
            mobConfig.set(mobID + ".Helmet", mob.getEquipment().getHelmet());
            mobConfig.set(mobID + ".Chestplate", mob.getEquipment().getChestplate());
            mobConfig.set(mobID + ".Leggings", mob.getEquipment().getLeggings());
            mobConfig.set(mobID + ".Boots", mob.getEquipment().getBoots());
            mobConfig.set(mobID + ".MainHand", mob.getEquipment().getItemInMainHand());
            mobConfig.set(mobID + ".OffHand", mob.getEquipment().getItemInOffHand());
            PersistentDataContainer pdc = mob.getPersistentDataContainer();
            for (NamespacedKey key : pdc.getKeys()) {
                if (pdc.has(key, PersistentDataType.DOUBLE)) {
                    mobConfig.set(mobID + ".PDCs" + "." + key.getKey(), pdc.get(key, PersistentDataType.DOUBLE));
                } else if (pdc.has(key, PersistentDataType.STRING)) {
                    mobConfig.set(mobID + ".PDCs" + "." + key.getKey(), pdc.get(key, PersistentDataType.STRING));
                } else if (pdc.has(key, PersistentDataType.BOOLEAN)) {
                    mobConfig.set(mobID + ".PDCs" + "." + key.getKey(), pdc.get(key, PersistentDataType.BOOLEAN));
                }
            }
            return;
        }
        mobConfig.set(mobID, mobID);
        mobConfig.set(mobID + ".EntityType", mob.getType().toString());
        mobConfig.set(mobID + ".Name", mob.getCustomName());
        mobConfig.set(mobID + ".HP", mob.getHealth());
        mobConfig.set(mobID + ".Dmg", mob.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue());
        mobConfig.set(mobID + ".Spd", mob.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
        mobConfig.set(mobID + ".Helmet", mob.getEquipment().getHelmet());
        mobConfig.set(mobID + ".Chestplate", mob.getEquipment().getChestplate());
        mobConfig.set(mobID + ".Leggings", mob.getEquipment().getLeggings());
        mobConfig.set(mobID + ".Boots", mob.getEquipment().getBoots());
        mobConfig.set(mobID + ".MainHand", mob.getEquipment().getItemInMainHand());
        mobConfig.set(mobID + ".OffHand", mob.getEquipment().getItemInOffHand());
        PersistentDataContainer pdc = mob.getPersistentDataContainer();
        for (NamespacedKey key : pdc.getKeys()) {
            if (pdc.has(key, PersistentDataType.DOUBLE)) {
                mobConfig.set(mobID + ".PDCs" + "." + key.getKey(), pdc.get(key, PersistentDataType.DOUBLE));
            } else if (pdc.has(key, PersistentDataType.STRING)) {
                mobConfig.set(mobID + ".PDCs" + "." + key.getKey(), pdc.get(key, PersistentDataType.STRING));
            } else if (pdc.has(key, PersistentDataType.BOOLEAN)) {
                mobConfig.set(mobID + ".PDCs" + "." + key.getKey(), pdc.get(key, PersistentDataType.BOOLEAN));
            }
        }



        try {
            mobConfig.save(mobFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void createItemsConfig() {
        itemsFile = new File(getDataFolder(), "items.yml");
        if (!itemsFile.exists()) {
            itemsFile.getParentFile().mkdirs();
            saveResource("items.yml", false);
        }

        itemsConfig = new YamlConfiguration();
        try {
            itemsConfig.load(itemsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        itemsConfig = YamlConfiguration.loadConfiguration(itemsFile);
    }

    public FileConfiguration getItemsConfig() {
        return this.itemsConfig;
    }
    public void saveItem(String itemID, ItemStack item) {
        if (itemsConfig.contains(itemID)) {
            Bukkit.getServer().getConsoleSender().sendMessage("§cItem with ID " + itemID + " already exists!");
            itemsConfig.set(itemID + ".Name", item.getItemMeta().getDisplayName());
            itemsConfig.set(itemID + ".Lore", item.getLore());
            itemsConfig.set(itemID + ".Material", item.getType().toString());
            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
            for (NamespacedKey key : pdc.getKeys()) {
                if (pdc.has(key, PersistentDataType.DOUBLE)) {
                    itemsConfig.set(itemID + ".PDCs" + "." + key.getKey(), pdc.get(key, PersistentDataType.DOUBLE));
                } else if (pdc.has(key, PersistentDataType.STRING)) {
                    itemsConfig.set(itemID + ".PDCs" + "." + key.getKey(), pdc.get(key, PersistentDataType.STRING));
                } else if (pdc.has(key, PersistentDataType.BOOLEAN)) {
                    itemsConfig.set(itemID + ".PDCs" + "." + key.getKey(), pdc.get(key, PersistentDataType.BOOLEAN));
                }
            }

                try {
                    itemsConfig.save(itemsFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            itemsConfig.set(itemID, itemID);
            itemsConfig.set(itemID + ".Material", item.getType().toString());
            itemsConfig.set(itemID + ".Name", item.getItemMeta().getDisplayName());
            itemsConfig.set(itemID + ".Lore", item.getLore());
            PersistentDataContainer pdc2 = item.getItemMeta().getPersistentDataContainer();
            for (NamespacedKey key : pdc2.getKeys()) {
                if (pdc2.has(key, PersistentDataType.DOUBLE)) {
                    itemsConfig.set(itemID + ".PDCs" + "." + key.getKey(), pdc2.get(key, PersistentDataType.DOUBLE));
                }else if (pdc2.has(key, PersistentDataType.STRING)) {
                    itemsConfig.set(itemID + ".PDCs" + "." + key.getKey(), pdc2.get(key, PersistentDataType.STRING));
                }else if (pdc2.has(key, PersistentDataType.BOOLEAN)) {
                    itemsConfig.set(itemID + ".PDCs" + "." + key.getKey(), pdc2.get(key, PersistentDataType.BOOLEAN));
                }
            }


            try {
                itemsConfig.save(itemsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    public ItemStack loadItemsByID(String ID) {
        if (itemsConfig.contains(ID)) {
            ItemStack item = new ItemStack(Material.valueOf(itemsConfig.getString(ID + ".Material")));
            ItemMeta meta = item.getItemMeta();
            ConfigurationSection pdcSection = itemsConfig.getConfigurationSection(ID + ".PDCs");
            if (pdcSection != null) {
                for (String key : pdcSection.getKeys(false)) {
                    NamespacedKey namespacedKey = new NamespacedKey("roguelonk", key);
                    if (pdcSection.isDouble(key)) {
                        double value = pdcSection.getDouble(key);
                        meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.DOUBLE, value);
                    } else if (pdcSection.isString(key)) {
                        String value = pdcSection.getString(key);
                        meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value);
                    } else if (pdcSection.isBoolean(key)) {
                        boolean value = pdcSection.getBoolean(key);
                        meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.BOOLEAN, value);
                    }
                }
            }
            if (meta.getPersistentDataContainer().has(new NamespacedKey("roguelonk","unbreakable"), PersistentDataType.BOOLEAN)) {
                meta.setUnbreakable(itemsConfig.getBoolean(ID + ".PDCs.unbreakable"));
            }
            if (meta.getPersistentDataContainer().has(new NamespacedKey("roguelonk","enchglint"), PersistentDataType.BOOLEAN)) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.setDisplayName(itemsConfig.getString(ID + ".Name"));
            meta.setLore(itemsConfig.getStringList(ID + ".Lore"));
            item.setItemMeta(meta);
            return item;
        }
        ItemStack item = new ItemStack(Material.STONE);
        item.getItemMeta().setDisplayName("§cItem not found!");
        return item;
    }







    public static HashMap<UUID, HashMap<String, Double>> getPlayerStats() {
        return playerStats;
    }





    public void statUpdate(Boolean sendBar){
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    HashMap<String, Double> stats = new HashMap<>();
                    ItemStack Helmet = player.getInventory().getHelmet();
                    ItemStack Chestplate = player.getInventory().getChestplate();
                    ItemStack Leggings = player.getInventory().getLeggings();
                    ItemStack Boots = player.getInventory().getBoots();
                    ItemStack MainHand = player.getInventory().getItemInMainHand();
                    ItemStack OffHand = player.getInventory().getItemInOffHand();

                    //HP
                    Double MainHandHp = 0.0;
                    Double OffHandHp = 0.0;
                    Double ChestplateHp = 0.0;
                    Double LeggingsHp = 0.0;
                    Double BootsDmgHp = 0.0;
                    Double HelmetDmgHp = 0.0;

                    if (MainHand != null) {
                        NamespacedKey hpKey = new NamespacedKey("roguelonk", "hp");
                        if (MainHand.getItemMeta() != null) {
                            if (MainHand.getItemMeta().getPersistentDataContainer().has(hpKey, PersistentDataType.DOUBLE)) {
                                MainHandHp = MainHand.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (OffHand != null) {
                        NamespacedKey hpKey = new NamespacedKey("roguelonk", "hp");
                        if (OffHand.getItemMeta() != null) {
                            OffHandHp = OffHand.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE);
                        }
                    }
                    if (Chestplate != null) {
                        NamespacedKey hpKey = new NamespacedKey("roguelonk", "hp");
                        if (Chestplate.getItemMeta() != null) {
                            ChestplateHp = Chestplate.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE);
                        }
                    }
                    if (Leggings != null) {
                        NamespacedKey hpKey = new NamespacedKey("roguelonk", "hp");
                        if (Leggings.getItemMeta() != null) {
                            LeggingsHp = Leggings.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE);
                        }
                    }
                    if (Boots != null) {
                        NamespacedKey hpKey = new NamespacedKey("roguelonk", "hp");
                        if (Boots.getItemMeta() != null) {
                            BootsDmgHp = Boots.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE);
                        }
                    }
                    if (Helmet != null) {
                        NamespacedKey hpKey = new NamespacedKey("roguelonk", "hp");
                        if (Helmet.getItemMeta() != null) {
                            HelmetDmgHp = Helmet.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE);
                        }
                    }

                    Double totalHp = MainHandHp + OffHandHp + ChestplateHp + LeggingsHp + BootsDmgHp + HelmetDmgHp + 20.0;

                    if (totalHp < 0) {
                        totalHp = 0.1;
                    }

                    player.setMaxHealth(totalHp);
                    player.setHealthScale(20);



                    //Speed
                    Double MainHandSpd = 0.0;
                    Double OffHandSpd = 0.0;
                    Double ChestplateSpd = 0.0;
                    Double LeggingsSpd = 0.0;
                    Double BootsDmgSpd = 0.0;
                    Double HelmetDmgSpd = 0.0;

                    if (MainHand != null) {
                        NamespacedKey spdKey = new NamespacedKey("roguelonk", "spd");
                        if (MainHand.getItemMeta() != null) {
                            if (MainHand.getItemMeta().getPersistentDataContainer().has(spdKey, PersistentDataType.DOUBLE)) {
                                MainHandSpd = MainHand.getItemMeta().getPersistentDataContainer().get(spdKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (OffHand != null) {
                        NamespacedKey spdKey = new NamespacedKey("roguelonk", "spd");
                        if (OffHand.getItemMeta() != null) {
                            OffHandSpd = OffHand.getItemMeta().getPersistentDataContainer().get(spdKey, PersistentDataType.DOUBLE);
                        }
                    }
                    if (Chestplate != null) {
                        NamespacedKey spdKey = new NamespacedKey("roguelonk", "spd");
                        if (Chestplate.getItemMeta() != null) {
                            ChestplateSpd = Chestplate.getItemMeta().getPersistentDataContainer().get(spdKey, PersistentDataType.DOUBLE);
                        }
                    }
                    if (Leggings != null) {
                        NamespacedKey spdKey = new NamespacedKey("roguelonk", "spd");
                        if (Leggings.getItemMeta() != null) {
                            LeggingsSpd = Leggings.getItemMeta().getPersistentDataContainer().get(spdKey, PersistentDataType.DOUBLE);
                        }
                    }
                    if (Boots != null) {
                        NamespacedKey spdKey = new NamespacedKey("roguelonk", "spd");
                        if (Boots.getItemMeta() != null) {
                            BootsDmgSpd = Boots.getItemMeta().getPersistentDataContainer().get(spdKey, PersistentDataType.DOUBLE);
                        }
                    }
                    if (Helmet != null) {
                        NamespacedKey spdKey = new NamespacedKey("roguelonk", "spd");
                        if (Helmet.getItemMeta() != null) {
                            HelmetDmgSpd = Helmet.getItemMeta().getPersistentDataContainer().get(spdKey, PersistentDataType.DOUBLE);
                        }
                    }

                    Double totalSpd = MainHandSpd + OffHandSpd + ChestplateSpd + LeggingsSpd + BootsDmgSpd + HelmetDmgSpd ;

                    stats.put("stat-spd", totalSpd);

                    totalSpd = totalSpd + 0.02;

                    totalSpd = totalSpd * 0.01;

                    if (totalSpd > 1) {
                        totalSpd = 1.0;
                    }

                    else if (totalSpd < -1) {
                        totalSpd = -1.0;
                    }
                    else{
                        totalSpd = totalSpd + 0.2;
                    }

                    player.setWalkSpeed(totalSpd.floatValue());


                    // DMG
                    Double MainHandDmg = 0.0;
                    Double OffHandDmg = 0.0;
                    Double ChestplateDmg = 0.0;
                    Double LeggingsDmg = 0.0;
                    Double BootsDmg = 0.0;
                    Double HelmetDmg = 0.0;

                    if (MainHand != null) {
                        NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
                        if (MainHand.getItemMeta() != null) {
                            if (MainHand.getItemMeta().getPersistentDataContainer().has(dmgKey, PersistentDataType.DOUBLE)) {
                                MainHandDmg = MainHand.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (OffHand != null) {
                        NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
                        if (OffHand.getItemMeta() != null) {
                            OffHandDmg = OffHand.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                        }
                    }
                    if (Chestplate != null) {
                        NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
                        if (Chestplate.getItemMeta() != null) {
                            ChestplateDmg = Chestplate.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                        }
                    }
                    if (Leggings != null) {
                        NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
                        if (Leggings.getItemMeta() != null) {
                            LeggingsDmg = Leggings.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                        }
                    }
                    if (Boots != null) {
                        NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
                        if (Boots.getItemMeta() != null) {
                            BootsDmg = Boots.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                        }
                    }
                    if (Helmet != null) {
                        NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
                        if (Helmet.getItemMeta() != null) {
                            HelmetDmg = Helmet.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                        }
                    }

                    Double FinalDmg = MainHandDmg + OffHandDmg + ChestplateDmg + LeggingsDmg + BootsDmg + HelmetDmg;

                    //defense
                    Double MainHandArmor = 0.0;
                    Double OffHandArmor = 0.0;
                    Double ChestplateArmor = 0.0;
                    Double LeggingsArmor = 0.0;
                    Double BootsArmor = 0.0;
                    Double HelmetArmor = 0.0;



                    if (MainHand != null) {
                        NamespacedKey armorKey = new NamespacedKey("roguelonk", "armor");
                        if (MainHand.getItemMeta() != null) {
                            if (MainHand.getItemMeta().getPersistentDataContainer().has(armorKey, PersistentDataType.DOUBLE)) {
                                MainHandArmor = MainHand.getItemMeta().getPersistentDataContainer().get(armorKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (OffHand != null) {
                        NamespacedKey armorKey = new NamespacedKey("roguelonk", "armor");
                        if (OffHand.getItemMeta() != null) {
                            OffHandArmor = OffHand.getItemMeta().getPersistentDataContainer().get(armorKey, PersistentDataType.DOUBLE);
                        }
                    }
                    if (Chestplate != null) {
                        NamespacedKey armorKey = new NamespacedKey("roguelonk", "armor");
                        if (Chestplate.getItemMeta() != null) {
                            ChestplateArmor = Chestplate.getItemMeta().getPersistentDataContainer().get(armorKey, PersistentDataType.DOUBLE);
                        }
                    }
                    if (Leggings != null) {
                        NamespacedKey armorKey = new NamespacedKey("roguelonk", "armor");
                        if (Leggings.getItemMeta() != null) {
                            LeggingsArmor = Leggings.getItemMeta().getPersistentDataContainer().get(armorKey, PersistentDataType.DOUBLE);
                        }
                    }
                    if (Boots != null) {
                        NamespacedKey armorKey = new NamespacedKey("roguelonk", "armor");
                        if (Boots.getItemMeta() != null) {
                            BootsArmor = Boots.getItemMeta().getPersistentDataContainer().get(armorKey, PersistentDataType.DOUBLE);
                        }
                    }
                    if (Helmet != null) {
                        NamespacedKey armorKey = new NamespacedKey("roguelonk", "armor");
                        if (Helmet.getItemMeta() != null) {
                            HelmetArmor = Helmet.getItemMeta().getPersistentDataContainer().get(armorKey, PersistentDataType.DOUBLE);
                        }
                    }

                    Double FinalArmor = MainHandArmor + OffHandArmor + ChestplateArmor + LeggingsArmor + BootsArmor + HelmetArmor;


                    stats.put("armor", FinalArmor);
                    stats.put("dmg", FinalDmg);
                    stats.put("hp", totalHp);
                    stats.put("real-spd", totalSpd);




                    if (sendBar) {
                        player.sendActionBar("§8⏩" + "§c🗡 " + stats.get("dmg") + "§8↔" + "§a❤ " + (int) player.getHealth() + "§7/§c" + (int) player.getMaxHealth() + "§8↔" + "§b🛡 " + FinalArmor + "§8⏪");
                    }

                    playerStats.put(player.getUniqueId(), stats);


                }
            }
        }.runTaskTimer(this, 0, 1);
    }


}


