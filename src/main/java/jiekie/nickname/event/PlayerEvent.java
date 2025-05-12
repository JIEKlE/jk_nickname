package jiekie.nickname.event;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import jiekie.nickname.NicknamePlugin;
import jiekie.nickname.exception.ApplyNicknameException;
import jiekie.nickname.util.ChatUtil;
import jiekie.nickname.model.PlayerNameData;
import jiekie.nickname.util.PacketNames;
import jiekie.nickname.util.SoundUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class PlayerEvent implements Listener {
    private final NicknamePlugin plugin;

    public PlayerEvent(NicknamePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        applyNickname(e);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        setQuitMessage(e);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        useNicknameTicket(e);
    }

    private void applyNickname(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerNameData playerNameData = plugin.getNicknameManager().getPlayerNameDataByUuid(uuid);

        if(playerNameData == null) {
            e.setJoinMessage(ChatUtil.getRightArrowPrefix() + player.getName());
            return;
        }

        String nickname = playerNameData.getNickname();
        e.setJoinMessage(ChatUtil.getRightArrowPrefix() + nickname);
        playerNameData.setOnline(true);

        try {
            plugin.getNicknameManager().applyNickname(player, nickname);
        } catch (ApplyNicknameException ex) {
            plugin.getLogger().info(ex.getMessage());
        }
    }

    private void setQuitMessage(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerNameData playerNameData = plugin.getNicknameManager().getPlayerNameDataByUuid(uuid);

        if(playerNameData == null) {
            e.setQuitMessage(ChatUtil.getLeftArrowPrefix() + player.getName());
            return;
        }

        e.setQuitMessage(ChatUtil.getLeftArrowPrefix() + playerNameData.getNickname());
        playerNameData.setOnline(false);
    }

    private void useNicknameTicket(PlayerInteractEvent e) {
        if(e.getHand() == null) return;
        if(!e.getHand().equals(EquipmentSlot.HAND)) return;
        if(e.getAction() != Action.RIGHT_CLICK_AIR  && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = e.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        ItemStack nicknameTicket = plugin.getNicknameManager().getNicknameTicket();
        if(!item.isSimilar(nicknameTicket)) return;

        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
        player.sendPluginMessage(plugin, PacketNames.SET_NICKNAME, byteArrayDataOutput.toByteArray());

        SoundUtil.playNoteBlockBell(player);
    }
}
