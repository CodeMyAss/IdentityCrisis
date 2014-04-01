package ch.jamiete.identitycrisis;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.tag.TagAPI;
import ch.jamiete.identitycrisis.commands.ChangeNameCommand;
import ch.jamiete.identitycrisis.commands.ResetNameCommand;
import ch.jamiete.identitycrisis.exceptions.TooBigException;
import ch.jamiete.identitycrisis.listeners.AsyncPlayerReceiveNameTag;
import ch.jamiete.identitycrisis.listeners.BorderControl;

public class IdentityCrisis extends JavaPlugin {
    protected boolean changeTab = true, changeChat = false;
    private PeopleManager manager;

    @Override
    public void onDisable() {
        this.getLogger().info("Changed " + this.getManager().getNumberIssuedChanges() + " players names this session!");
    }

    /**
     * Can I speak to your manager?
     * @return
     */
    public PeopleManager getManager() {
        return this.manager;
    }

    @Override
    public void onEnable() {
        this.manager = new PeopleManager(this);

        final PluginManager pm = this.getServer().getPluginManager();

        if (pm.getPlugin("TagAPI") == null) {
            this.getLogger().severe("Oops. I couldn't manage to find TagAPI.");
            this.getLogger().severe("It is required to use this plugin.");
            this.getLogger().severe("Download it at http://dev.bukkit.org/server-mods/tag");
            pm.disablePlugin(this);
            return;
        }

        if (!this.getConfig().getBoolean("updated", false)) {
            this.getServer().getScheduler().runTaskAsynchronously(this, new UUIDUpdater(this));
        }

        pm.registerEvents(new AsyncPlayerReceiveNameTag(this), this);

        if (this.getConfig().getBoolean("bordercontrol", false)) {
            pm.registerEvents(new BorderControl(this), this);
        }

        this.changeChat = this.getConfig().getBoolean("changechat", false);
        this.changeTab = this.getConfig().getBoolean("changeplayerlist", true);

        this.getCommand("changename").setExecutor(new ChangeNameCommand(this));
        this.getCommand("resetname").setExecutor(new ResetNameCommand(this));

        for (final Player player : this.getServer().getOnlinePlayers()) {
            final UUID uuid = player.getUniqueId();
            final String newName = this.getManager().getDefinedName(uuid);
            if (!newName.equals(player.getName())) {
                try {
                    this.getManager().addNameChange(uuid, newName);
                } catch (final TooBigException e) {
                    this.getLogger().severe("Error while changing name from memory:");
                    this.getLogger().severe(e.getMessage());
                }
                TagAPI.refreshPlayer(player);
            }
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
