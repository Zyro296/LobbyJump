package gg.irindium.lobbyJump;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

public class PlayerJumpListener implements Listener {

    private final LobbyJump plugin;

    public PlayerJumpListener(LobbyJump plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        if (plugin.getConfig().getBoolean("enabled")) {
            player.setAllowFlight(true);
        }
    }

    @EventHandler
    public void onPlayertoggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        FileConfiguration config = plugin.getConfig();

        if (!config.getBoolean("enabled")) {
            return;
        }

        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {

            event.setCancelled(true);
            player.setAllowFlight(false);

            double horizontalMultiplier = config.getDouble("jump.horizontal-power");
            double verticalPower = config.getDouble("jump.vertical-power");

            Vector direction = player.getLocation().getDirection();

            Vector jumpVector = direction.multiply(horizontalMultiplier).setY(verticalPower);
            player.setVelocity(jumpVector);

            String soundName = config.getString("sound.name");
            if (soundName != null && !soundName.equalsIgnoreCase("NONE")) {

                try {
                    NamespacedKey soundKey = NamespacedKey.minecraft(soundName.toLowerCase());
                    Sound jumpSound = Registry.SOUNDS.get(soundKey);

                    if (jumpSound != null) {
                        float volume = (float) config.getDouble("sound.volume");
                        float pitch = (float) config.getDouble("sound.pitch");
                        player.playSound(player.getLocation(), jumpSound, volume, pitch);
                    }
                } catch (IllegalArgumentException e) {

                    plugin.getLogger().warning("Invalid sound name in config: " + soundName);

                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (plugin.getConfig().getBoolean("enabled") && !player.getAllowFlight() && player.isOnGround()) {
            if (player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
                player.setAllowFlight(true);
            }
        }
    }

}
