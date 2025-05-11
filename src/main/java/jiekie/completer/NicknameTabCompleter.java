package jiekie.completer;

import jiekie.NicknamePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NicknameTabCompleter implements TabCompleter {
    private final NicknamePlugin plugin;

    public NicknameTabCompleter(NicknamePlugin plugin) {
        this.plugin = plugin;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) return Collections.emptyList();
        Player player = (Player) sender;
        
        if(args.length == 1) {
            if(player.isOp())
                return Arrays.asList("설정", "해제", "확인", "변경", "도움말");
            else
                return Arrays.asList("확인", "도움말");
        }

        if(args.length == 2 && args[0].equals("확인"))
            return plugin.getNicknameManager().getPlayerNameAndNicknameList();

        if(args.length == 2 && args[0].equals("변경"))
            return plugin.getNicknameManager().getPlayerNameList();

        return Collections.emptyList();
    }
}
