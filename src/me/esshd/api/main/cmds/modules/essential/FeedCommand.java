/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffectType
 */
package me.esshd.api.main.cmds.modules.essential;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import me.esshd.api.main.BaseConstants;
import me.esshd.api.main.cmds.BaseCommand;
import me.esshd.api.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class FeedCommand
extends BaseCommand {
    private static final int MAX_HUNGER = 20;

    public FeedCommand() {
        super("feed", "Feeds a player.");
        this.setUsage("/(command) <player>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ImmutableSet<Player> targets;
        Player onlyTarget = null;
        if (args.length > 0 && sender.hasPermission(String.valueOf(command.getPermission()) + ".others")) {
            if (args[0].equalsIgnoreCase("all") && sender.hasPermission(String.valueOf(command.getPermission()) + ".all")) {
                targets = ImmutableSet.copyOf((Collection)Bukkit.getOnlinePlayers());
            } else {
                onlyTarget = BukkitUtils.playerWithNameOrUUID(args[0]);
                if (onlyTarget == null || !BaseCommand.canSee(sender, onlyTarget)) {
                    sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
                    return true;
                }
                targets = ImmutableSet.of(onlyTarget);
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(this.getUsage(label));
                return true;
            }
            onlyTarget = (Player)sender;
            targets = ImmutableSet.of(onlyTarget);
        }
        if (onlyTarget != null && onlyTarget.getFoodLevel() == 20) {
            sender.sendMessage((Object)ChatColor.RED + onlyTarget.getName() + " already has full hunger.");
            return true;
        }
        for (Player target : targets) {
            target.removePotionEffect(PotionEffectType.HUNGER);
            target.setFoodLevel(20);
        }
        sender.sendMessage((Object)ChatColor.YELLOW + "Fed " + (onlyTarget == null ? "all online players" : onlyTarget.getName()) + '.');
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}

