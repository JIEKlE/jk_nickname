package jiekie.nickname.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtil {
    /* error */
    public static final String NICKNAME_NOT_SET = getXPrefix() + "설정된 닉네임이 없습니다.";
    public static final String PLAYER_NOT_FOUND = getXPrefix() + "플레이어를 찾을 수 없습니다.";
    public static final String NICKNAME_DUPLICATED = getXPrefix() + "이미 사용 중인 닉네임입니다.";
    public static String INVENTORY_FULL = getXPrefix() + "인벤토리가 가득 찼습니다. 인벤토리를 1칸 이상 비워주시기 바랍니다.";

    /* feedback */
    public static final String SET_NICKNAME = getCheckPrefix() + "닉네임이 설정되었습니다.";
    public static final String RESET_NICKNAME = getCheckPrefix() + "닉네임이 해제되었습니다.";
    public static final String GET_NICKNAME_TICKET = getCheckPrefix() + "입주신고서를 받았습니다.";

    /* prefix */
    public static String getCheckPrefix() {
        return "\uA001 ";
    }

    public static String getXPrefix() {
        return "\uA002 ";
    }

    public static String getWarnPrefix() {
        return "\uA003 ";
    }

    public static String getLeftArrowPrefix() {
        return "\uA005 ";
    }

    public static String getRightArrowPrefix() {
        return "\uA006 ";
    }

    public static void showMessage(Player player, String message) {
        player.sendMessage(message);
    }

    /* validate */
    public static void notPlayer(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "플레이어가 아닙니다.");
    }

    public static void notOp(Player player) {
        player.sendMessage(getWarnPrefix() + "권한이 없습니다.");
    }

    public static String wrongCommand() {
        return getWarnPrefix() + "명령어 사용법이 잘못되었습니다.";
    }

    /* feedback */
    public static void showNameNandNickname(Player player, String name, String nickname) {
        player.sendMessage(getWarnPrefix() + "설정한 닉네임 : " + nickname + " (" + name + ")");
    }

    /* command */
    public static void commandHelper(Player player) {
        player.sendMessage(getWarnPrefix() + "/닉네임 도움말" + ChatColor.GRAY + " : 사용 가능한 명령어를 확인할 수 있습니다.");
    }

    public static void commandList(Player player) {
        player.sendMessage("");
        player.sendMessage(getWarnPrefix() + "닉네임 명령어 목록");

        if(player.isOp()) {
            player.sendMessage("　　　① /닉네임 설정 닉네임");
            player.sendMessage(ChatColor.GRAY + "　　　　　: 닉네임을 설정합니다.");
            player.sendMessage("　　　② /닉네임 해제");
            player.sendMessage(ChatColor.GRAY + "　　　　　: 닉네임 설정을 해제합니다.");
            player.sendMessage("　　　③ /닉네임 확인");
            player.sendMessage(ChatColor.GRAY + "　　　　　: 본인의 닉네임을 확인합니다.");
            player.sendMessage("　　　④ /닉네임 확인 플레이어ID|닉네임");
            player.sendMessage(ChatColor.GRAY + "　　　　　: 플레이어의 닉네임과 ID를 확인합니다.");
            player.sendMessage("　　　⑤ /닉네임 변경 플레이어ID 닉네임");
            player.sendMessage(ChatColor.GRAY + "　　　　　: 플레이어의 닉네임을 변경시킵니다. (닉네임 공백 입력 시 해제)");
            player.sendMessage("　　　⑥ /닉네임 도움말");
            player.sendMessage(ChatColor.GRAY + "　　　　　: 사용 가능한 명령어를 확인할 수 있습니다.");

        } else {
            player.sendMessage("　　　① /닉네임 확인");
            player.sendMessage(ChatColor.GRAY + "　　　　　: 본인의 닉네임을 확인합니다.");
            player.sendMessage("　　　② /닉네임 확인 플레이어ID|닉네임");
            player.sendMessage(ChatColor.GRAY + "　　　　　: 플레이어의 닉네임과 ID를 확인합니다.");
            player.sendMessage("　　　③ /닉네임 도움말");
            player.sendMessage(ChatColor.GRAY + "　　　　　: 사용 가능한 명령어를 확인할 수 있습니다.");
        }

        player.sendMessage("");
    }
}
