package ch.jamiete.identitycrisis.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import ch.jamiete.identitycrisis.IdentityCrisis;
import ch.jamiete.identitycrisis.Namer;

/**
 * Stupid implementation that doesn't work.
 * I'm angry.
 */
public class ScoreboardNamer implements Namer {
    private final IdentityCrisis plugin;
    private Scoreboard scoreboard;

    public ScoreboardNamer(IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canEnable() {
        return true;
    }

    @Override
    public void enable() {
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    @Override
    public void rename(Player whom, String to) {
        //scoreboard.
    }

}
