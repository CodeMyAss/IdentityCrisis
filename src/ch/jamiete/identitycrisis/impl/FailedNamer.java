package ch.jamiete.identitycrisis.impl;

import org.bukkit.entity.Player;
import ch.jamiete.identitycrisis.Namer;

public class FailedNamer implements Namer {

    @Override
    public boolean canEnable() {
        return false;
    }

    @Override
    public void enable() {
        return;
    }

    @Override
    public void rename(Player whom, String to) {
        return;
    }

}
