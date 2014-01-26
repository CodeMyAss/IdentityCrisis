package ch.jamiete.identitycrisis;

import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;
import ch.jamiete.identitycrisis.exceptions.TooBigException;

public class PeopleManager {
    private final IdentityCrisis plugin;
    private final ConcurrentHashMap<String, String> nameChanges = new ConcurrentHashMap<String, String>();

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
     * @param oldName
     * @param newName
     * @throws TooBigException
     */
    public void addNameChange(final String oldName, final String newName) throws TooBigException {
        if (newName.length() > 16) {
            throw new TooBigException("Couldn't change " + oldName + " to " + newName + " as the new name is too long!");
        }

        this.nameChanges.put(oldName, newName);
        this.plugin.getConfig().set("names." + oldName, newName);
        this.plugin.saveConfig();

        final Player player = this.plugin.getServer().getPlayerExact(oldName);
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
     * If not set, returns normal name.
     * @param oldName
     * @return
     */
    public String getDefinedName(final String oldName) {
        final String newName = this.plugin.getConfig().getString("names." + oldName);
        return newName == null ? oldName : newName;
    }

    /**
     * Returns the changed name for a user, null if not changed.
     * @param name
     * @return
     */
    public String getName(final String name) {
        return this.nameChanges.get(name);
    }

    /**
     * Returns whether or not there is an active name change for specified user.
     * @param name
     * @return
     */
    public boolean hasChanged(final String name) {
        return this.nameChanges.containsKey(name);
    }

    /**
     * Removes a name change for defined user.
     * <b>Saved to configuration immediately.</b>
     * @param oldName
     */
    public void removeNameChange(final String oldName) {
        this.nameChanges.remove(oldName);
        this.plugin.getConfig().set("names." + oldName, null);
        this.plugin.saveConfig();

        final Player player = this.plugin.getServer().getPlayerExact(oldName);
        if (player != null) {
            TagAPI.refreshPlayer(player);

            if (this.plugin.changeTab) {
                player.setPlayerListName(oldName);
            }

            if (this.plugin.changeChat) {
                player.setDisplayName(oldName);
            }
        }
    }

}
