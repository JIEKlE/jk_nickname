package jiekie.event;

import jiekie.NicknamePlugin;
import jiekie.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
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
        String nickname = plugin.getNicknameManager().getPlayerNickname(uuid);

        if(nickname == null)
            nickname = player.getName();
        else
            plugin.getNicknameManager().applyNickname(player, nickname);

        e.setJoinMessage(ChatUtil.getAddPrefix() + nickname);
    }

    @EventHandler
    public void onPlayerJoin(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String nickname = plugin.getNicknameManager().existsPlayerNameData(uuid) ? plugin.getNicknameManager().getPlayerNickname(uuid) : player.getName();

        e.setQuitMessage(ChatUtil.getSubtractPrefix() + nickname);
    }
}
