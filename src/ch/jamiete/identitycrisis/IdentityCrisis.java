package ch.jamiete.identitycrisis;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ch.jamiete.identitycrisis.commands.ChangeNameCommand;
import ch.jamiete.identitycrisis.commands.ResetNameCommand;
import ch.jamiete.identitycrisis.impl.FailedNamer;
import ch.jamiete.identitycrisis.impl.ScoreboardNamer;
import ch.jamiete.identitycrisis.impl.TagAPINamer;
import ch.jamiete.identitycrisis.listeners.BorderControl;
import ch.jamiete.identitycrisis.listeners.PlayerJoin;

public class IdentityCrisis extends JavaPlugin {
    protected boolean changeTab = true, changeChat = false;
    protected Namer namer;
    protected NameManager manager;

    @Override
    public void onDisable() {
        manager.onDisable();
    }

    public NameManager getNameManager() {
        return this.manager;
    }

    @Override
    public void onEnable() {
        this.manager = new NameManager(this);
        final PluginManager pm = this.getServer().getPluginManager();

        String namertype = this.getConfig().getString("type", "tagapi");
        if (namertype.equalsIgnoreCase("tagapi")) {
            namer = new TagAPINamer(this);
        } else if (namertype.equalsIgnoreCase("scoreboard")) {
            namer = new ScoreboardNamer(this);
        } else {
            namer = new FailedNamer();
        }

        if (namer.canEnable()) {
            try {
                namer.enable();
            } catch (Exception e) {
                e.printStackTrace();
                pm.disablePlugin(this);
                return;
            }
        } else {
            this.getLogger().severe("Cannot startup with current configuration.");
            this.getLogger().severe("If using TagAPI ensure it is installed and enabled.");
            pm.disablePlugin(this);
            return;
        }

        pm.registerEvents(new PlayerJoin(this), this);

        if (this.getConfig().getBoolean("bordercontrol", false)) {
            pm.registerEvents(new BorderControl(this), this);
        }

        this.changeChat = this.getConfig().getBoolean("changechat", false);
        this.changeTab = this.getConfig().getBoolean("changeplayerlist", true);

        this.getCommand("changename").setExecutor(new ChangeNameCommand(this));
        this.getCommand("resetname").setExecutor(new ResetNameCommand(this));

        for (final Player player : this.getServer().getOnlinePlayers()) {
            manager.process(player);
        }
    }

    @Override
    public void onLoad() {
        final String version = this.getConfig().getString("version");

        if (version == null || version != null && !version.equals("1.1")) {
            this.getConfig().options().copyDefaults(true);
        }
    }

}
