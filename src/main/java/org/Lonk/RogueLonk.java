package org.Lonk;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.Lonk.Commands.MainCommand;
import org.Lonk.Items.BeamListener;
import org.Lonk.Items.CustomSkullCreator;
import org.Lonk.Items.ItemManager;
import org.Lonk.Items.ItemUpdater;
import org.Lonk.Mobs.Bosses.BossManager;
import org.Lonk.Mobs.MobManager;
import org.Lonk.Stats.Stats;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@SuppressWarnings({"all"})
public final class RogueLonk extends JavaPlugin {

    public static HashMap<UUID, HashMap<String, Double>> playerStats = new HashMap<>();
    private File itemsFile;
    private FileConfiguration itemsConfig;
    private File mobFile;
    private FileConfiguration mobConfig;
    private static RogueLonk instance;
    private File npcFile;
    private FileConfiguration npcConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        createItemsConfig();
        createMobConfig();
        getServer().getPluginManager().registerEvents(new BeamListener(), this);
        getServer().getPluginManager().registerEvents(new Stats(), this);
        getServer().getPluginManager().registerEvents(new ItemUpdater(), this);
        ItemManager.init();
        MobManager.innit();
        BossManager.init();


        getCommand("RLP").setExecutor(new MainCommand());
        Bukkit.getServer().getConsoleSender().sendMessage("§eRogueLonk has been enabled!");
        statUpdate(true);
    }

    public static RogueLonk getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void createNpcConfig() {
        npcFile = new File(getDataFolder(), "npcs.yml");
        if (!npcFile.exists()) {
            npcFile.getParentFile().mkdirs();
            saveResource("npcs.yml", false);
        }

        npcConfig = new YamlConfiguration();
        try {
            npcConfig.load(npcFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        npcConfig = YamlConfiguration.loadConfiguration(npcFile);
    }

    public FileConfiguration getNpcConfig() {
        return this.npcConfig;
    }

    public static void saveNPC(String ID, LivingEntity entity) {
        RogueLonk plugin = JavaPlugin.getPlugin(RogueLonk.class);
        FileConfiguration npcConfig = plugin.getNpcConfig();
        if (npcConfig != null) {

        }
    }


    public static void UpdateDisguises() {
        for (Entity entity : Bukkit.getServer().getWorlds().get(0).getEntities()) {
            if (entity instanceof LivingEntity) {
                disguise((LivingEntity) entity, entity.getCustomName());
            }
        }

    }

    public static void setDisguise(LivingEntity entity, String skinName) {
        NamespacedKey key = new NamespacedKey("roguelonk", "disguise");
        entity.getPersistentDataContainer().set(key, PersistentDataType.STRING, skinName);
    }

    public static void disguise(LivingEntity entity, String skinName ) {

        PlayerDisguise disg = new PlayerDisguise(skinName);

        DisguiseAPI.disguiseToAll(entity, disg);

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
        mobConfig.set(mobID, mobID);
        mobConfig.set(mobID + ".EntityType", mob.getType().toString());
        mobConfig.set(mobID + ".Name", mob.getCustomName());
        mobConfig.set(mobID + ".HP", mob.getHealth());
        mobConfig.set(mobID + ".Dmg", mob.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue());
        mobConfig.set(mobID + ".Spd", mob.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());

        saveEquipment(mobConfig, mobID, mob.getEquipment().getHelmet(), "Helmet");
        saveEquipment(mobConfig, mobID, mob.getEquipment().getChestplate(), "Chestplate");
        saveEquipment(mobConfig, mobID, mob.getEquipment().getLeggings(), "Leggings");
        saveEquipment(mobConfig, mobID, mob.getEquipment().getBoots(), "Boots");
        saveEquipment(mobConfig, mobID, mob.getEquipment().getItemInMainHand(), "MainHand");
        saveEquipment(mobConfig, mobID, mob.getEquipment().getItemInOffHand(), "OffHand");


        PersistentDataContainer pdc = mob.getPersistentDataContainer();
        for (NamespacedKey key : pdc.getKeys()) {
            if (pdc.has(key, PersistentDataType.DOUBLE)) {
                mobConfig.set(mobID + ".Data" + "." + key.getKey(), pdc.get(key, PersistentDataType.DOUBLE));
            } else if (pdc.has(key, PersistentDataType.STRING)) {
                mobConfig.set(mobID + ".Data" + "." + key.getKey(), pdc.get(key, PersistentDataType.STRING));
            } else if (pdc.has(key, PersistentDataType.BOOLEAN)) {
                mobConfig.set(mobID + ".Data" + "." + key.getKey(), pdc.get(key, PersistentDataType.BOOLEAN));
            } else if (pdc.has(key, PersistentDataType.INTEGER)) {
                mobConfig.set(mobID + ".Data" + "." + key.getKey(), pdc.get(key, PersistentDataType.INTEGER));
            }
        }



        try {
            mobConfig.save(mobFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LivingEntity loadMobById(String mobID, Location spawnLocation) {
        if (mobConfig.contains(mobID)) {
            // Create a new LivingEntity instance at the specified location
            LivingEntity mob = (LivingEntity) Bukkit.getServer().getWorlds().get(0).spawnEntity(spawnLocation, EntityType.valueOf(mobConfig.getString(mobID + ".EntityType")));
            //set Stuff
            mob.setCustomName(mobConfig.getString(mobID + ".Name"));
            mob.setHealth(mobConfig.getDouble(mobID + ".HP"));
            mob.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(mobConfig.getDouble(mobID + ".Dmg"));
            mob.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(mobConfig.getDouble(mobID + ".Spd"));

            // Set the age of the mob if it's Ageable
            if (mob instanceof Ageable) {
                int age = mobConfig.getInt(mobID + ".age");
                ((Ageable) mob).setAge(age);
                if (age < 0) {
                    ((Ageable) mob).setBaby();
                } else {
                    ((Ageable) mob).setAdult();
                }
            }


            //Helm
            ItemStack helmet = new ItemStack(Material.valueOf(mobConfig.getString(mobID + ".Equipment.Helmet.Type")));
            ItemMeta helmetMeta = helmet.getItemMeta();
            if (helmetMeta != null) {
                Map<NamespacedKey, Object> helmetData = convertConfigSectionToMap(mobConfig.getConfigurationSection(mobID + ".Equipment.Helmet.Data"));
                helmet = rebuildItem(Material.valueOf(mobConfig.getString(mobID + ".Equipment.Helmet.Type")), mobConfig.getString(mobID + ".Equipment.Helmet.Name"), mobConfig.getStringList(mobID + ".Equipment.Helmet.Lore"), helmetData);
            }

            //Chest
            ItemStack chestplate = new ItemStack(Material.valueOf(mobConfig.getString(mobID + ".Equipment.Chestplate.Type")));
            ItemMeta chestplateMeta = chestplate.getItemMeta();
            if (chestplateMeta != null) {
                Map<NamespacedKey, Object> chestplateData = convertConfigSectionToMap(mobConfig.getConfigurationSection(mobID + ".Equipment.Chestplate.Data"));
                chestplate = rebuildItem(Material.valueOf(mobConfig.getString(mobID + ".Equipment.Chestplate.Type")), mobConfig.getString(mobID + ".Equipment.Chestplate.Name"), mobConfig.getStringList(mobID + ".Equipment.Chestplate.Lore"), chestplateData);
            }

            //Legs
            ItemStack leggings = new ItemStack(Material.valueOf(mobConfig.getString(mobID + ".Equipment.Leggings.Type")));
            ItemMeta leggingsMeta = leggings.getItemMeta();
            if (leggingsMeta != null) {
                Map<NamespacedKey, Object> leggingsData = convertConfigSectionToMap(mobConfig.getConfigurationSection(mobID + ".Equipment.Leggings.Data"));
                leggings = rebuildItem(Material.valueOf(mobConfig.getString(mobID + ".Equipment.Leggings.Type")), mobConfig.getString(mobID + ".Equipment.Leggings.Name"), mobConfig.getStringList(mobID + ".Equipment.Leggings.Lore"), leggingsData);
            }

            //Boots
            ItemStack boots = new ItemStack(Material.valueOf(mobConfig.getString(mobID + ".Equipment.Boots.Type")));
            ItemMeta bootsMeta = boots.getItemMeta();
            if (bootsMeta != null) {
                Map<NamespacedKey, Object> helmetData = convertConfigSectionToMap(mobConfig.getConfigurationSection(mobID + ".Equipment.Boots.Data"));
                boots = rebuildItem(Material.valueOf(mobConfig.getString(mobID + ".Equipment.Boots.Type")), mobConfig.getString(mobID + ".Equipment.Boots.Name"), mobConfig.getStringList(mobID + ".Equipment.Boots.Lore"), helmetData);
            }

            //MainHand
            ItemStack mainHand = new ItemStack(Material.valueOf(mobConfig.getString(mobID + ".Equipment.MainHand.Type")));
            ItemMeta mainHandMeta = mainHand.getItemMeta();
            if (mainHandMeta != null) {
                Map<NamespacedKey, Object> mainHandData = convertConfigSectionToMap(mobConfig.getConfigurationSection(mobID + ".Equipment.MainHand.Data"));
                mainHand = rebuildItem(Material.valueOf(mobConfig.getString(mobID + ".Equipment.MainHand.Type")), mobConfig.getString(mobID + ".Equipment.MainHand.Name"), mobConfig.getStringList(mobID + ".Equipment.MainHand.Lore"), mainHandData);
            }

            //OffHand

            ItemStack offHand = new ItemStack(Material.valueOf(mobConfig.getString(mobID + ".Equipment.OffHand.Type")));
            ItemMeta offHandMeta = offHand.getItemMeta();
            if (offHandMeta != null) {
                Map<NamespacedKey, Object> offHandData = convertConfigSectionToMap(mobConfig.getConfigurationSection(mobID + ".Equipment.OffHand.Data"));
                offHand = rebuildItem(Material.valueOf(mobConfig.getString(mobID + ".Equipment.OffHand.Type")), mobConfig.getString(mobID + ".Equipment.OffHand.Name"), mobConfig.getStringList(mobID + ".Equipment.OffHand.Lore"), offHandData);
            }

            //equip
            mob.getEquipment().setHelmet(helmet);
            mob.getEquipment().setChestplate(chestplate);
            mob.getEquipment().setLeggings(leggings);
            mob.getEquipment().setBoots(boots);
            mob.getEquipment().setItemInMainHand(mainHand);
            mob.getEquipment().setItemInOffHand(offHand);

            //data
            PersistentDataContainer pdc = mob.getPersistentDataContainer();
            ConfigurationSection mobData = mobConfig.getConfigurationSection(mobID + ".Data");
            if (mobData != null) {
                for (String key : mobData.getKeys(false)) {
                    NamespacedKey namespacedKey = new NamespacedKey("roguelonk", key);
                    if (mobData.isDouble(key)) {
                        double value = mobData.getDouble(key);
                        pdc.set(namespacedKey, PersistentDataType.DOUBLE, value);
                    } else if (mobData.isString(key)) {
                        String value = mobData.getString(key);
                        pdc.set(namespacedKey, PersistentDataType.STRING, value);
                    } else if (mobData.isBoolean(key)) {
                        boolean value = mobData.getBoolean(key);
                        pdc.set(namespacedKey, PersistentDataType.BOOLEAN, value);
                    } else if (mobData.isInt(key)) {
                        int value = mobData.getInt(key);
                        pdc.set(namespacedKey, PersistentDataType.INTEGER, value);
                    }


                }
            }

            return mob;





        }
        return null;
    }


    public Map<NamespacedKey, Object> convertConfigSectionToMap(ConfigurationSection section) {
        Map<NamespacedKey, Object> map = new HashMap<>();
        if (section != null) {
            for (String key : section.getKeys(false)) {
                NamespacedKey namespacedKey = new NamespacedKey("roguelonk", key);
                Object value = section.get(key);
                map.put(namespacedKey, value);
            }
        }
        return map;
    }



    private void saveEquipment(FileConfiguration mobConfig, String mobID, ItemStack equipment, String equipmentName) {
        if (equipment != null) {
            mobConfig.set(mobID + ".Equipment." + equipmentName + ".Type", equipment.getType().toString());
            if (equipment.getItemMeta() != null) {
                mobConfig.set(mobID + ".Equipment." + equipmentName + ".Name", equipment.getItemMeta().getDisplayName());
                mobConfig.set(mobID + ".Equipment." + equipmentName + ".Lore", equipment.getItemMeta().getLore());

                PersistentDataContainer pdc = equipment.getItemMeta().getPersistentDataContainer();
                for (NamespacedKey key : pdc.getKeys()) {
                    if (pdc.has(key, PersistentDataType.DOUBLE)) {
                        mobConfig.set(mobID + ".Equipment." + equipmentName + ".Data" + "." + key.getKey(), pdc.get(key, PersistentDataType.DOUBLE));
                    } else if (pdc.has(key, PersistentDataType.STRING)) {
                        mobConfig.set(mobID + ".Equipment." + equipmentName + ".Data" + "." + key.getKey(), pdc.get(key, PersistentDataType.STRING));
                    } else if (pdc.has(key, PersistentDataType.BOOLEAN)) {
                        mobConfig.set(mobID + ".Equipment." + equipmentName + ".Data" + "." + key.getKey(), pdc.get(key, PersistentDataType.BOOLEAN));
                    }
                }

            } else {
                mobConfig.set(mobID + ".Equipment." + equipmentName + ".Name", " ");
                mobConfig.set(mobID + ".Equipment." + equipmentName + ".Lore", " ");

                mobConfig.set(mobID + ".Equipment." + equipmentName + ".Data", " ");
                mobConfig.set(mobID + ".Equipment." + equipmentName + ".Data", " ");

            }


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
            return;
        }
        Bukkit.getServer().getConsoleSender().sendMessage("§cSaving item with ID " + itemID);
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
            } else if (pdc2.has(key, PersistentDataType.INTEGER)) {
                itemsConfig.set(itemID + ".PDCs" + "." + key.getKey(), pdc2.get(key, PersistentDataType.INTEGER));
            }
        }


        try {
            itemsConfig.save(itemsFile);
            Bukkit.getServer().getConsoleSender().sendMessage("§aItem with ID " + itemID + " has been saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public ItemStack loadItemsByID(String ID) {
        if (itemsConfig.contains(ID)) {
            ItemStack item = new ItemStack(Material.valueOf(itemsConfig.getString(ID + ".Material")));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(itemsConfig.getString(ID + ".Name"));
            meta.setLore(itemsConfig.getStringList(ID + ".Lore"));
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
                    } else if (pdcSection.isInt(key)) {
                        int value = pdcSection.getInt(key);
                        meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, value);
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

            // IF ITEM IS A CUSTOM SKULL
            if (meta.getPersistentDataContainer().has(new NamespacedKey("roguelonk","texture"), PersistentDataType.STRING)) {
                ItemStack skull = CustomSkullCreator.createSkull(meta.getPersistentDataContainer().get(new NamespacedKey("roguelonk","texture"), PersistentDataType.STRING));
                ItemMeta skullMeta = skull.getItemMeta();

                // Copy properties from 'meta' to 'skullMeta'
                skullMeta.setDisplayName(meta.getDisplayName());
                skullMeta.setLore(meta.getLore());
                skullMeta.setUnbreakable(meta.isUnbreakable());

                // Copy the PersistentDataContainer from 'meta' to 'skullMeta'
                if (pdcSection != null) {
                    for (String key : pdcSection.getKeys(false)) {
                        NamespacedKey namespacedKey = new NamespacedKey("roguelonk", key);
                        if (pdcSection.isDouble(key)) {
                            double value = pdcSection.getDouble(key);
                            skullMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.DOUBLE, value);
                        } else if (pdcSection.isString(key)) {
                            String value = pdcSection.getString(key);
                            skullMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value);
                        } else if (pdcSection.isBoolean(key)) {
                            boolean value = pdcSection.getBoolean(key);
                            skullMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.BOOLEAN, value);
                        } else if (pdcSection.isInt(key)) {
                            int value = pdcSection.getInt(key);
                            skullMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, value);
                        }
                    }
                }

                skullMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

                // Set the merged ItemMeta back to the skull
                skull.setItemMeta(skullMeta);

                // Use the skull ItemStack
                item = skull;
                return item;
            }


            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
            return item;
        }
        ItemStack item = new ItemStack(Material.STONE);
        item.getItemMeta().setDisplayName("§cItem not found!");
        return item;
    }

    public ItemStack rebuildItem(Material mat, String name, List<String> lore, Map<NamespacedKey, Object> pdcData) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (pdcData != null) {

            for (Map.Entry<NamespacedKey, Object> entry : pdcData.entrySet()) {
                NamespacedKey key = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof String) {
                    pdc.set(key, PersistentDataType.STRING, (String) value);
                } else if (value instanceof Double) {
                    pdc.set(key, PersistentDataType.DOUBLE, (Double) value);
                } else if (value instanceof Integer) {
                    pdc.set(key, PersistentDataType.INTEGER, (Integer) value);
                } else if (value instanceof Boolean) {
                    pdc.set(key, PersistentDataType.BOOLEAN, (Boolean) value);
                }
            }
        }

        if(meta.getPersistentDataContainer().has(new NamespacedKey("roguelonk","unbreakable"), PersistentDataType.BOOLEAN)) {
            meta.setUnbreakable(meta.getPersistentDataContainer().get(new NamespacedKey("roguelonk","unbreakable"), PersistentDataType.BOOLEAN));
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        if(meta.getPersistentDataContainer().has(new NamespacedKey("roguelonk","enchglint"), PersistentDataType.BOOLEAN)) {
            if(meta.getPersistentDataContainer().get(new NamespacedKey("roguelonk","enchglint"), PersistentDataType.BOOLEAN)) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
        }

        item.setItemMeta(meta);
        return item;

    }


    public PlayerProfile createPlayerProfile(String texture) {
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.setProperty(new ProfileProperty("textures", texture));
        return profile;
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
                            if (MainHand.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE) != null) {
                                if (MainHand.getItemMeta().getPersistentDataContainer().has(hpKey, PersistentDataType.DOUBLE)) {
                                    MainHandHp = MainHand.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE);
                                }
                            }
                        }
                    }
                    if (OffHand != null) {
                        NamespacedKey hpKey = new NamespacedKey("roguelonk", "hp");
                        if (OffHand.getItemMeta() != null) {
                            if (OffHand.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE) != null) {
                                OffHandHp = OffHand.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (Chestplate != null) {
                        NamespacedKey hpKey = new NamespacedKey("roguelonk", "hp");
                        if (Chestplate.getItemMeta() != null) {
                            if (Chestplate.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE) != null) {
                                ChestplateHp = Chestplate.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (Leggings != null) {
                        NamespacedKey hpKey = new NamespacedKey("roguelonk", "hp");
                        if (Leggings.getItemMeta() != null) {
                            if (Leggings.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE) != null) {
                                LeggingsHp = Leggings.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (Boots != null) {
                        NamespacedKey hpKey = new NamespacedKey("roguelonk", "hp");
                        if(Boots.getItemMeta() != null) {
                            if (Boots.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE) != null) {
                                BootsDmgHp = Boots.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (Helmet != null) {
                        NamespacedKey hpKey = new NamespacedKey("roguelonk", "hp");
                        if (Helmet.getItemMeta() != null) {
                            if (Helmet.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE) != null) {
                                HelmetDmgHp = Helmet.getItemMeta().getPersistentDataContainer().get(hpKey, PersistentDataType.DOUBLE);
                            }
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
                            if (OffHand.getItemMeta().getPersistentDataContainer().has(spdKey, PersistentDataType.DOUBLE)){
                                OffHandSpd = OffHand.getItemMeta().getPersistentDataContainer().get(spdKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (Chestplate != null) {
                        NamespacedKey spdKey = new NamespacedKey("roguelonk", "spd");
                        if (Chestplate.getItemMeta() != null) {
                            if (Chestplate.getItemMeta().getPersistentDataContainer().has(spdKey, PersistentDataType.DOUBLE)) {
                                ChestplateSpd = Chestplate.getItemMeta().getPersistentDataContainer().get(spdKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (Leggings != null) {
                        NamespacedKey spdKey = new NamespacedKey("roguelonk", "spd");
                        if (Leggings.getItemMeta() != null) {
                            if (Leggings.getItemMeta().getPersistentDataContainer().has(spdKey, PersistentDataType.DOUBLE)) {
                                LeggingsSpd = Leggings.getItemMeta().getPersistentDataContainer().get(spdKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (Boots != null) {
                        NamespacedKey spdKey = new NamespacedKey("roguelonk", "spd");
                        if (Boots.getItemMeta() != null) {
                            if (Boots.getItemMeta().getPersistentDataContainer().has(spdKey, PersistentDataType.DOUBLE)) {
                                BootsDmgSpd = Boots.getItemMeta().getPersistentDataContainer().get(spdKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (Helmet != null) {
                        NamespacedKey spdKey = new NamespacedKey("roguelonk", "spd");
                        if (Helmet.getItemMeta() != null) {
                            if (Helmet.getItemMeta().getPersistentDataContainer().has(spdKey, PersistentDataType.DOUBLE)) {
                                HelmetDmgSpd = Helmet.getItemMeta().getPersistentDataContainer().get(spdKey, PersistentDataType.DOUBLE);
                            }
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
                            if (OffHand.getItemMeta().getPersistentDataContainer().has(dmgKey, PersistentDataType.DOUBLE))
                            {
                                OffHandDmg = OffHand.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (Chestplate != null) {
                        NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
                        if (Chestplate.getItemMeta() != null) {
                            if (Chestplate.getItemMeta().getPersistentDataContainer().has(dmgKey, PersistentDataType.DOUBLE)) {
                                ChestplateDmg = Chestplate.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (Leggings != null) {
                        NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
                        if (Leggings.getItemMeta() != null) {
                            if (Leggings.getItemMeta().getPersistentDataContainer().has(dmgKey, PersistentDataType.DOUBLE)) {
                                LeggingsDmg = Leggings.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (Boots != null) {
                        NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
                        if (Boots.getItemMeta() != null) {
                            if (Boots.getItemMeta().getPersistentDataContainer().has(dmgKey, PersistentDataType.DOUBLE)) {
                                BootsDmg = Boots.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (Helmet != null) {
                        NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
                        if (Helmet.getItemMeta() != null) {
                            if (Helmet.getItemMeta().getPersistentDataContainer().has(dmgKey, PersistentDataType.DOUBLE)) {
                                HelmetDmg = Helmet.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                            }
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
                            if (OffHand.getItemMeta().getPersistentDataContainer().has(armorKey, PersistentDataType.DOUBLE)) {
                                OffHandArmor = OffHand.getItemMeta().getPersistentDataContainer().get(armorKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (Chestplate != null) {
                        NamespacedKey armorKey = new NamespacedKey("roguelonk", "armor");
                        if (Chestplate.getItemMeta() != null) {
                            if (Chestplate.getItemMeta().getPersistentDataContainer().has(armorKey, PersistentDataType.DOUBLE)) {
                                ChestplateArmor = Chestplate.getItemMeta().getPersistentDataContainer().get(armorKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (Leggings != null) {
                        NamespacedKey armorKey = new NamespacedKey("roguelonk", "armor");
                        if (Leggings.getItemMeta() != null) {
                            if (Leggings.getItemMeta().getPersistentDataContainer().has(armorKey, PersistentDataType.DOUBLE)) {
                                LeggingsArmor = Leggings.getItemMeta().getPersistentDataContainer().get(armorKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (Boots != null) {
                        NamespacedKey armorKey = new NamespacedKey("roguelonk", "armor");
                        if (Boots.getItemMeta() != null) {
                            if (Boots.getItemMeta().getPersistentDataContainer().has(armorKey, PersistentDataType.DOUBLE)) {
                                BootsArmor = Boots.getItemMeta().getPersistentDataContainer().get(armorKey, PersistentDataType.DOUBLE);
                            }
                        }
                    }
                    if (Helmet != null) {
                        NamespacedKey armorKey = new NamespacedKey("roguelonk", "armor");
                        if (Helmet.getItemMeta() != null) {
                            if (Helmet.getItemMeta().getPersistentDataContainer().has(armorKey, PersistentDataType.DOUBLE)) {
                                HelmetArmor = Helmet.getItemMeta().getPersistentDataContainer().get(armorKey, PersistentDataType.DOUBLE);
                            }
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

