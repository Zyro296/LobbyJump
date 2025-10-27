package gg.irindium.lobbyJump;

import org.bukkit.plugin.java.JavaPlugin;

public final class LobbyJump extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("LobbyJump starting ...");

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new PlayerJumpListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("LobbyJump stopping ...");

    }
}
