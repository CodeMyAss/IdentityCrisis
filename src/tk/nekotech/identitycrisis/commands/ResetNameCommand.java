package tk.nekotech.identitycrisis.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.nekotech.identitycrisis.IdentityCrisis;

public class ResetNameCommand implements CommandExecutor {
    private IdentityCrisis plugin;

    public ResetNameCommand(IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }
        if (args[0].length() > 16) {
            sender.sendMessage(ChatColor.RED + "That username is too large to be a players!");
        } else {
            plugin.removeNameChange(args[0]);
            sender.sendMessage(ChatColor.GREEN + "Reset username for " + args[0] + "!");
        }
        return true;
    }
}
