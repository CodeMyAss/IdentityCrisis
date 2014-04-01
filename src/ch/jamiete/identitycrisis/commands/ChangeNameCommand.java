package ch.jamiete.identitycrisis.commands;

import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ch.jamiete.identitycrisis.IdentityCrisis;

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

        UUID uuid = null;

        if (args[0].length() <= 16) {
            Player player = this.plugin.getServer().getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Could not find that player online.");
                return true;
            }

            uuid = player.getUniqueId();
        } else {
            if (args[0].length() != 32) {
                sender.sendMessage(ChatColor.RED + "Invalid UUID specified.");
                return false;
            }

            try {
                uuid = UUID.fromString(args[0]);
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + "Invalid UUID specified.");
                return true;
            }
        }

        if (args[1].length() > 16) {
            sender.sendMessage(ChatColor.RED + "The new username is too long. 16 characters maximum.");
            return true;
        }

        this.plugin.getManager().addNameChange(uuid, args[1]);
        sender.sendMessage(ChatColor.GREEN + "Changed username for " + uuid + "!");

        return true;
    }
}
