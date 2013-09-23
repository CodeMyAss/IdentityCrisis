package ch.jamiete.identitycrisis;

import org.bukkit.entity.Player;

public interface Namer {

    /**
     * Can this interface be enabled?
     * @return
     */
    public boolean canEnable();

    /**
     * Enable this interface.
     */
    public void enable();

    /**
     * Renames a player.
     * @param whom
     * @param to
     */
    public void rename(Player whom, String to);

}
