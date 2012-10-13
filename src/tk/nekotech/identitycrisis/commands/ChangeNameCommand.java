package tk.nekotech.identitycrisis.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.nekotech.identitycrisis.IdentityCrisis;
import tk.nekotech.identitycrisis.exceptions.TooBigException;

public class ChangeNameCommand implements CommandExecutor {
    private IdentityCrisis plugin;

    public ChangeNameCommand(IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 2) {
            return false;
        }
        if (args[0].length() > 16) {
            sender.sendMessage(ChatColor.RED + "That username is too large to be a players!");
        } else if (args[1].length() > 16) {
            sender.sendMessage(ChatColor.RED + "The new username is too long. 16 characters maximum.");
        } else {
            try {
                plugin.addNameChange(args[0], args[1]);
            } catch (TooBigException e) {
                e.printStackTrace();
            }
            sender.sendMessage(ChatColor.GREEN + "Changed username for " + args[0] + "!");
        }
        return true;
    }
}
