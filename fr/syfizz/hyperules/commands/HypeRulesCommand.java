package fr.syfizz.hyperules.commands;

import fr.syfizz.hyperules.HypeRules;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HypeRulesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length==1){
            if(args[0].equalsIgnoreCase("reload")){
                if (sender.hasPermission(HypeRules.getInstance().getConfig().getString("reloadPermission"))){
                    HypeRules.getInstance().reloadConfig();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HypeRules.getInstance().getConfig().getString("successfullyReloaded")));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HypeRules.getInstance().getConfig().getString("noPermissionMessage")));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HypeRules.getInstance().getConfig().getString("wrongArgument")));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HypeRules.getInstance().getConfig().getString("tooMuchOrFewArguments")));
        }
        return false;
    }
}
