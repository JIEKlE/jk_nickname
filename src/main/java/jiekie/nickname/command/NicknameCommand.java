package jiekie.nickname.command;

import jiekie.nickname.NicknamePlugin;
import jiekie.nickname.exception.ApplyNicknameException;
import jiekie.nickname.exception.ResetNicknameException;
import jiekie.nickname.util.ChatUtil;
import jiekie.nickname.model.PlayerNameData;
import jiekie.nickname.util.SoundUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NicknameCommand implements CommandExecutor {
    private final NicknamePlugin plugin;

    public NicknameCommand(NicknamePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            ChatUtil.notPlayer(sender);
            return true;
        }

        if(args == null || args.length == 0) {
            ChatUtil.commandHelper(player);
            return true;
        }

        switch (args[0]) {
            case "설정":
                setNickname(player, args);
                break;

            case "해제":
                resetNickname(player);
                break;

            case "변경":
                changePlayerNickname(player, args);
                break;

            case "확인":
                checkNickname(player, args);
                break;

            case "도움말":
                ChatUtil.commandList(player);
                break;

            default:
                ChatUtil.commandHelper(player);
                break;
        }

        return true;
    }

    public void setNickname(Player player, String[] args) {
        if(args.length < 2) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/닉네임 설정 이름)");
            return;
        }

        String nickname = getContents(args, 1);
        try {
            plugin.getNicknameManager().applyNickname(player, nickname);
            plugin.getNicknameManager().removeNicknameTicketFromPlayer(player);
            ChatUtil.showMessage(player, ChatUtil.SET_NICKNAME);
            SoundUtil.playNoteBlockBell(player);

        } catch (ApplyNicknameException e) {
            ChatUtil.showMessage(player, e.getMessage());
        }
    }

    public void resetNickname(Player player) {
        try {
            plugin.getNicknameManager().resetNickname(player);
            ChatUtil.showMessage(player, ChatUtil.RESET_NICKNAME);
            SoundUtil.playNoteBlockBell(player);

        } catch (ResetNicknameException e) {
            ChatUtil.showMessage(player, e.getMessage());
        }
    }

    public void checkNickname(Player player, String[] args) {
        PlayerNameData playerNameData;

        // 본인 닉네임 확인
        if(args.length == 1) {
            playerNameData = plugin.getNicknameManager().getPlayerNameDataByUuid(player.getUniqueId());

        // 플레이어 닉네임 확인
        } else {
            String searchNickname = getContents(args, 1);
            Player searchedPlayer = plugin.getNicknameManager().getPlayerByNameOrNickname(searchNickname);
            if(searchedPlayer == null) {
                ChatUtil.showMessage(player, ChatUtil.PLAYER_NOT_FOUND);
                return;
            }
            playerNameData = plugin.getNicknameManager().getPlayerNameDataByUuid(searchedPlayer.getUniqueId());
        }

        if(playerNameData == null) {
            ChatUtil.showMessage(player, ChatUtil.NICKNAME_NOT_SET);
            return;
        }

        ChatUtil.showNameNandNickname(player, playerNameData.getName(), playerNameData.getNickname());
        SoundUtil.playNoteBlockBell(player);
    }

    public void changePlayerNickname(Player player, String[] args) {
        if(!player.isOp()) {
            ChatUtil.notOp(player);
            return;
        }

        if(args.length < 2) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/닉네임 변경 플레이어ID 닉네임)");
            return;
        }

        // 플레이어 닉네임 해제
        Player searchedPlayer = Bukkit.getPlayerExact(args[1]);
        if(searchedPlayer == null) {
            ChatUtil.showMessage(player, ChatUtil.PLAYER_NOT_FOUND);
            return;
        }

        if(args.length == 2) {
            try {
                plugin.getNicknameManager().resetNickname(searchedPlayer);
                ChatUtil.showMessage(player, ChatUtil.RESET_NICKNAME);
                SoundUtil.playNoteBlockBell(player);

            } catch (ResetNicknameException e) {
                ChatUtil.showMessage(player, e.getMessage());
            }
            return;
        }

        String nickname = getContents(args, 2);
        try {
            plugin.getNicknameManager().applyNickname(searchedPlayer, nickname);
            ChatUtil.showMessage(player, ChatUtil.SET_NICKNAME);
            SoundUtil.playNoteBlockBell(player);

        } catch (ApplyNicknameException e) {
            ChatUtil.showMessage(player, e.getMessage());
        }
    }

    private String getContents(String[] args, int startIndex) {
        StringBuilder sb = new StringBuilder();
        for(int i = startIndex ; i < args.length ; i++) {
            if(i != startIndex)
                sb.append(" ");
            sb.append(args[i]);
        }

        String contents = sb.toString();
        return ChatColor.translateAlternateColorCodes('&', contents);
    }
}
