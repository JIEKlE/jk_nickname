package jiekie.event;

import jiekie.NicknamePlugin;
import jiekie.exception.ApplyNicknameException;
import jiekie.util.ChatUtil;
import jiekie.model.PlayerNameData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerEvent implements Listener {
    private final NicknamePlugin plugin;

    public PlayerEvent(NicknamePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerNameData playerNameData = plugin.getNicknameManager().getPlayerNameDataByUuid(uuid);

        if(playerNameData == null) {
            e.setJoinMessage(ChatUtil.getAddPrefix() + player.getName());
            return;
        }

        String nickname = playerNameData.getNickname();
        e.setJoinMessage(ChatUtil.getAddPrefix() + nickname);
        playerNameData.setOnline(true);

        try {
            plugin.getNicknameManager().applyNickname(player, nickname);
        } catch (ApplyNicknameException ex) {
            plugin.getLogger().info(ex.getMessage());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerNameData playerNameData = plugin.getNicknameManager().getPlayerNameDataByUuid(uuid);

        if(playerNameData == null) {
            e.setQuitMessage(ChatUtil.getSubtractPrefix() + player.getName());
            return;
        }

        e.setQuitMessage(ChatUtil.getSubtractPrefix() + playerNameData.getNickname());
        playerNameData.setOnline(false);
    }
}
