package tk.nekotech.identitycrisis.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.nekotech.identitycrisis.IdentityCrisis;
import tk.nekotech.identitycrisis.exceptions.TooBigException;

public class ChangeNameCommand implements CommandExecutor {
    private final IdentityCrisis plugin;

    public ChangeNameCommand(final IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length != 2) {
            return false;
        }
        if (args[0].length() > 16) {
            sender.sendMessage(ChatColor.RED + "That username is too large to be a players!");
        } else if (args[1].length() > 16) {
            sender.sendMessage(ChatColor.RED + "The new username is too long. 16 characters maximum.");
        } else {
            try {
                this.plugin.addNameChange(args[0], args[1]);
            } catch (final TooBigException e) {
                e.printStackTrace();
            }
            sender.sendMessage(ChatColor.GREEN + "Changed username for " + args[0] + "!");
        }
        return true;
    }
}
