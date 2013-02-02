package ch.jamiete.identitycrisis.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ch.jamiete.identitycrisis.IdentityCrisis;

public class ResetNameCommand implements CommandExecutor {
    private final IdentityCrisis plugin;

    public ResetNameCommand(final IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length != 1) {
            return false;
        }
        if (args[0].length() > 16) {
            sender.sendMessage(ChatColor.RED + "That username is too large to be a players!");
        } else {
            this.plugin.removeNameChange(args[0]);
            sender.sendMessage(ChatColor.GREEN + "Reset username for " + args[0] + "!");
        }
        return true;
    }
}
