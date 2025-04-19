package jiekie.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ChatUtil {
    /* 에러 메시지 */
    public static final String NICKNAME_NOT_SET = "설정된 닉네임이 없습니다.";
    public static final String PLAYER_NOT_FOUND = "플레이어를 찾을 수 없습니다.";
    public static final String NICKNAME_DUPLICATED = "이미 사용 중인 닉네임입니다.";

    public static String getWarnPrefix() {
        return "[ " + ChatColor.YELLOW + "❗" + ChatColor.WHITE + " ] ";
    }

    public static String getAddPrefix() {
        return "[ " + ChatColor.GREEN + "➕" + ChatColor.WHITE + " ] ";
    }

    public static String getSubtractPrefix() {
        return "[ " + ChatColor.RED + "➖" + ChatColor.WHITE + " ] ";
    }

    /* 유효성 검사 */
    public static void notPlayer(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "플레이어가 아닙니다.");
    }

    public static void notOp(Player player) {
        player.sendMessage(getWarnPrefix() + "권한이 없습니다.");
    }

    public static String wrongCommand() {
        return getWarnPrefix() + "명령어 사용법이 잘못되었습니다.";
    }

    /* 피드백 */
    public static void showErrorMessage(Player player, String message) {
        player.sendMessage(getWarnPrefix() + message);
    }

    public static void setNickname(Player player) {
        player.sendMessage(getWarnPrefix() + "닉네임이 설정되었습니다.");
    }

    public static void resetNickname(Player player) {
        player.sendMessage(getWarnPrefix() + "닉네임이 해제되었습니다.");
    }

    public static void showNameNandNickname(Player player, String name, String nickname) {
        player.sendMessage(getWarnPrefix() + "설정한 닉네임 : " + nickname + " (" + name + ")");
    }

    /* 명령어 설명 */
    public static void commandHelper(Player player) {
        player.sendMessage(getWarnPrefix() + "/닉네임 도움말" + ChatColor.GRAY + " : 사용 가능한 명령어를 확인할 수 있습니다.");
    }

    public static void commandList(Player player) {
        player.sendMessage(getWarnPrefix() + "닉네임 명령어 목록");
        player.sendMessage("　　　① /닉네임 설정 닉네임");
        player.sendMessage(ChatColor.GRAY + "　　　　　: 닉네임을 설정합니다.");
        player.sendMessage("　　　② /닉네임 해제");
        player.sendMessage(ChatColor.GRAY + "　　　　　: 닉네임 설정을 해제합니다.");
        player.sendMessage("　　　③ /닉네임 확인");
        player.sendMessage(ChatColor.GRAY + "　　　　　: 본인의 닉네임을 확인합니다.");
        player.sendMessage("　　　④ /닉네임 확인 플레이어ID|닉네임");
        player.sendMessage(ChatColor.GRAY + "　　　　　: 플레이어의 닉네임과 ID를 확인합니다.");

        if(player.isOp()) {
            player.sendMessage("　　　⑤ /닉네임 변경 플레이어ID 닉네임");
            player.sendMessage(ChatColor.GRAY + "　　　　　: 플레이어의 닉네임을 변경시킵니다. (닉네임 공백 입력 시 해제)");
            player.sendMessage("　　　⑥ /닉네임 도움말");
            player.sendMessage(ChatColor.GRAY + "　　　　　: 사용 가능한 명령어를 확인할 수 있습니다.");

        } else {
            player.sendMessage("　　　⑤ /닉네임 도움말");
            player.sendMessage(ChatColor.GRAY + "　　　　　: 사용 가능한 명령어를 확인할 수 있습니다.");
        }

    }
}
