package org.Lonk.Commands;

import org.Lonk.Items.ItemCreator;
import org.Lonk.RogueLonk;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.Lonk.Items.ItemCreator.createItem;

public class MainCommand implements CommandExecutor, TabCompleter {
    RogueLonk plugin = JavaPlugin.getPlugin(RogueLonk.class);
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("RLP")){
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cYou must be a player to use this command!");
                return false;
            }

            Player player = (Player) sender;

            if (args.length == 0) {
                sender.sendMessage("§cPlease specify a subcommand!");
                return false;
            }

            if (args[0].equalsIgnoreCase("createitem")) {
                if (!player.isOp()) {
                    player.sendMessage("§cYou do not have permission to use this command!");
                    return false;
                }
                if (args.length == 10) {
                    player.sendMessage("§aCreating item...");
                    Material mat = player.getInventory().getItemInMainHand().getType();
                    if (mat == Material.AIR) {
                        player.sendMessage("§cYou must be holding an item to create an item!");
                        return false;
                    }
                    ItemStack item = new ItemStack(mat);

                    double dmg = Double.parseDouble(args[2]);
                    double hp = Double.parseDouble(args[3]);
                    double spd = Double.parseDouble(args[4]);
                    double armor = Double.parseDouble(args[5]);
                    String name = args[1];
                    String lore = args[6];
                    Boolean enchGlint = Boolean.parseBoolean(args[7]);
                    Boolean unbreakable = Boolean.parseBoolean(args[8]);
                    String ID = args[9];

                    ItemStack newItem = createItem(ID ,item.getType(), dmg, hp, spd, armor, name, lore, enchGlint, unbreakable);


                    player.sendMessage("§aItem created!");

                    player.getInventory().setItemInMainHand(newItem);
                } else {
                    sender.sendMessage("§cInvalid arguments! Usage: /RogueLonk createitem <name> <dmg> <hp> <spd> <armor> <lore> <enchGlint> <unbreakable>");
                }



            } else if (args[0].equalsIgnoreCase("SpawnMob")) {
                // Handle another subcommand
                if (!player.isOp()) {
                    player.sendMessage("§cYou do not have permission to use this command!");
                    return false;
                }
                if (args.length == 2) {
                    String ID = args[1];



                    player.sendMessage("§aMob created!");
                }
            }else if (args[0].equalsIgnoreCase("GiveItem")) {
                    if (args.length == 2) {
                        String ID = args[1];
                        ItemStack item = plugin.loadItemsByID(ID);
                        player.getInventory().addItem(item);


                    } else {
                        sender.sendMessage("§cInvalid arguments! Usage: /RogueLonk GiveItem <ID>");
                    }
                } else {
                    sender.sendMessage("§cInvalid arguments! Usage: /RogueLonk CreateMob <mobType> <hp> <dmg> <spd> <helm> <Cp> <Leg> <Boot> <MainHand>");
                }


            } else {
                sender.sendMessage("§cInvalid subcommand!");
            }


        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("createitem", "createmob", "giveitem");
        } else if (args.length > 1 && args[0].equalsIgnoreCase("createitem")) {
            if (args.length == 2) {
                return List.of("<Name>"); // default name
            } else if (args.length == 3) {
                return List.of("<Damage>"); // default values for dmg, hp, spd, armor
            } else if (args.length == 4) {
                return List.of("<Hp>");
            } else if (args.length == 5) {
                return List.of("<Speed>");
            } else if (args.length == 6) {
                return List.of("<Armor>");
            } else if (args.length == 7) {
                return List.of("<Lore>"); // default lore
            } else if (args.length == 8) {
                return List.of("true", "false"); // enchGlint
            } else if (args.length == 9) {
                return List.of("true", "false"); // unbreakable
            }
        } else if (args.length > 1 && args[0].equalsIgnoreCase("createmob")) {
            if (args.length == 2) {
                return Arrays.stream(EntityType.values())
                        .filter(EntityType::isAlive)
                        .map(EntityType::name)
                        .collect(Collectors.toList());
            } else if (args.length >= 5 && args.length <= 9) {
                return Arrays.stream(Material.values())
                        .map(Material::name)
                        .collect(Collectors.toList());
            }
            
        }else if (args.length > 1 && args[0].equalsIgnoreCase("giveitem")) {
            if (args.length == 2) {
                return ItemCreator.getAllItemIDs();
            }
        }

        return null;
    }
}
