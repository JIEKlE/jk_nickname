package jiekie.command;

import jiekie.NicknamePlugin;
import jiekie.util.ChatUtil;
import jiekie.util.SoundUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class NicknameTicketCommand implements CommandExecutor {
    private final NicknamePlugin plugin;

    public NicknameTicketCommand(NicknamePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            ChatUtil.notPlayer(sender);
            return true;
        }

        if(!player.isOp()) {
            ChatUtil.notOp(player);
            return true;
        }

        PlayerInventory inventory = player.getInventory();
        if(inventory.firstEmpty() == -1) {
            ChatUtil.showMessage(player, ChatUtil.INVENTORY_FULL);
            return true;
        }

        ItemStack nicknameTicket = plugin.getNicknameManager().getNicknameTicket();
        inventory.addItem(nicknameTicket);

        ChatUtil.showMessage(player, ChatUtil.GET_NICKNAME_TICKET);
        SoundUtil.playNoteBlockBell(player);

        return true;
    }
}
