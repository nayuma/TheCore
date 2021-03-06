package me.esshd.hcf.eventgame.conquest;

import me.esshd.api.utils.JavaUtils;
import me.esshd.api.utils.cmds.CommandArgument;
import me.esshd.hcf.ConfigurationService;
import me.esshd.hcf.HCF;
import me.esshd.hcf.eventgame.EventType;
import me.esshd.hcf.eventgame.tracker.ConquestTracker;
import me.esshd.hcf.faction.type.Faction;
import me.esshd.hcf.faction.type.PlayerFaction;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ConquestSetpointsArgument extends CommandArgument {

    private final HCF plugin;

    public ConquestSetpointsArgument(HCF plugin) {
        super("setpoints", "Sets the points of a faction in the Conquest event", "hcf.command.conquest.argument.setpoints");
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <factionName> <amount>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	
    	if (!sender.hasPermission("Core.staff.advanced")) {
			sender.sendMessage(ChatColor.RED + "No permission.");
		}
    	
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return true;
        }

        Faction faction = plugin.getFactionManager().getFaction(args[1]);

        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage(ChatColor.RED + "Faction " + args[1] + " is either not found or is not a player faction.");
            return true;
        }

        Integer amount = JavaUtils.tryParseInt(args[2]);

        if (amount == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a number.");
            return true;
        }

        if (amount > ConfigurationService.CONQUEST_REQUIRED_WIN_POINTS) {
            sender.sendMessage(ChatColor.RED + "Maximum points for Conquest is " + ConfigurationService.CONQUEST_REQUIRED_WIN_POINTS + '.');
            return true;
        }

        PlayerFaction playerFaction = (PlayerFaction) faction;
        ((ConquestTracker) EventType.CONQUEST.getEventTracker()).setPoints(playerFaction, amount);

        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Set the points of faction " + playerFaction.getName() + " to " + amount + '.');
        return true;
    }
}
