package jiekie.command;

import jiekie.NicknamePlugin;
import jiekie.util.ChatUtil;
import jiekie.util.SoundUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class NicknameCommand implements CommandExecutor {
    private final NicknamePlugin plugin;

    public NicknameCommand(NicknamePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            ChatUtil.notPlayer(sender);
            return true;
        }

        Player player = (Player) sender;
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

    /* 설정 */
    public void setNickname(Player player, String[] args) {
        if(args.length < 2) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/닉네임 설정 이름)");
            return;
        }

        String nickname = getContents(args, 1);
        plugin.getNicknameManager().applyNickname(player, nickname);

        ChatUtil.setNickname(player);
        SoundUtil.playNoteBlockBell(player);
    }

    /* 해제 */
    public void resetNickname(Player player) {
        UUID uuid = player.getUniqueId();
        if(!plugin.getNicknameManager().existsPlayerNameData(uuid)) {
            ChatUtil.nicknameDoesNotExist(player);
            return;
        }

        plugin.getNicknameManager().resetNickname(player);

        ChatUtil.resetNickname(player);
        SoundUtil.playNoteBlockBell(player);
    }

    /* 변경 */
    public void changePlayerNickname(Player player, String[] args) {
        if(!player.isOp()) {
            ChatUtil.notOp(player);
            return;
        }

        if(args.length < 3) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/닉네임 변경 ID 닉네임)");
            return;
        }

        String name = args[1];
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(onlinePlayer.getName().equals(name)) {
                String nickname = getContents(args, 2);
                plugin.getNicknameManager().applyNickname(onlinePlayer, nickname);

                ChatUtil.changePlayerNickname(player, name, nickname);
                SoundUtil.playNoteBlockBell(player);

                return;
            }
        }

        ChatUtil.canNotFindPlayer(player, name);
        SoundUtil.playNoteBlockBell(player);
    }

    /* 확인 */
    public void checkNickname(Player player, String[] args) {
        // 본인 닉네임 확인
        if(args.length == 1) {
            UUID uuid = player.getUniqueId();
            String nickname = plugin.getNicknameManager().getPlayerNickname(uuid);

            if(nickname == null)
                ChatUtil.nicknameDoesNotExist(player);
            else
                ChatUtil.showOwnNickname(player, nickname);

            SoundUtil.playNoteBlockBell(player);
            return;
        }

        // 플레이어 닉네임 확인
        String searchNickname = getContents(args, 1);
        List<String> names = plugin.getNicknameManager().findPlayerNameByNickname(searchNickname);

        ChatUtil.showPlayerName(player, names, searchNickname);
        SoundUtil.playNoteBlockBell(player);
    }

    /*  내용 조합 */
    private String getContents(String[] args, int startIndex) {
        StringBuffer sb = new StringBuffer();
        for(int i = startIndex ; i < args.length ; i++) {
            if(i != startIndex)
                sb.append(" ");
            sb.append(args[i]);
        }

        String contents = sb.toString();
        return ChatColor.translateAlternateColorCodes('&', contents);
    }
}
