package ch.jamiete.identitycrisis;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;
import ch.jamiete.identitycrisis.exceptions.TooBigException;

public class PeopleManager {
    private final IdentityCrisis plugin;
    private final ConcurrentHashMap<UUID, String> nameChanges = new ConcurrentHashMap<UUID, String>();

    public PeopleManager(IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    /**
     * Returns the number of name changes issued this session.
     * @return
     */
    public int getNumberIssuedChanges() {
        return this.nameChanges.size();
    }

    /**
     * Adds a name change for defined user.
     * <b>Saved to configuration immediately.</b>
     * @param uuid
     * @param newName
     * @throws TooBigException
     */
    public void addNameChange(final UUID uuid, final String newName) throws TooBigException {
        if (newName.length() > 16) {
            throw new TooBigException("Couldn't change " + uuid + " to " + newName + " as the new name is too long!");
        }

        this.nameChanges.put(uuid, newName);
        this.plugin.getConfig().set("names." + uuid, newName);
        this.plugin.saveConfig();

        final Player player = this.plugin.getServer().getPlayer(uuid);
        if (player != null) {
            TagAPI.refreshPlayer(player);

            if (this.plugin.changeTab) {
                player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', newName));
            }

            if (this.plugin.changeChat) {
                player.setDisplayName(ChatColor.translateAlternateColorCodes('&', newName) + ChatColor.WHITE);
            }
        }
    }

    /**
     * Returns a username that is 16 characters or less.
     * @param name
     * @return
     */
    public String chopString(final String name) {
        if (name.length() <= 16) {
            return name;
        }
        return name.substring(0, 16);
    }

    /**
     * Returns the defined name of the user.
     * If not set, returns null.
     * @param oldName
     * @return
     */
    public String getDefinedName(final UUID uuid) {
        ;
        return this.plugin.getConfig().getString("names." + uuid);
    }

    /**
     * Returns the changed name for a user, null if not changed.
     * @param uuid
     * @return
     */
    public String getName(final UUID uuid) {
        return this.nameChanges.get(uuid);
    }

    /**
     * Returns whether or not there is an active name change for specified user.
     * @param name
     * @return
     */
    public boolean hasChanged(final UUID uuid) {
        return this.nameChanges.containsKey(uuid);
    }

    /**
     * Removes a name change for defined user.
     * <b>Saved to configuration immediately.</b>
     * @param oldName
     */
    public void removeNameChange(final UUID uuid) {
        this.nameChanges.remove(uuid);
        this.plugin.getConfig().set("names." + uuid, null);
        this.plugin.saveConfig();

        final Player player = this.plugin.getServer().getPlayer(uuid);
        if (player != null) {
            TagAPI.refreshPlayer(player);

            if (this.plugin.changeTab) {
                player.setPlayerListName(player.getName());
            }

            if (this.plugin.changeChat) {
                player.setDisplayName(player.getName());
            }
        }
    }

}
