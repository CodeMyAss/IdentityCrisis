package ch.jamiete.identitycrisis.commands;

import java.util.UUID;
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

        if (args[0].length() != 32) {
            sender.sendMessage(ChatColor.RED + "Invalid UUID specified.");
        } else {
            UUID uuid;

            try {
                uuid = UUID.fromString(args[0]);
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + "Invalid UUID specified.");
                return true;
            }

            this.plugin.getManager().removeNameChange(uuid);
            sender.sendMessage(ChatColor.GREEN + "Reset username for " + uuid + "!");
        }
        return true;
    }
}
