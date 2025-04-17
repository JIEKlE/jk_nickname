package jiekie;

import jiekie.command.NicknameCommand;
import jiekie.completer.NicknameTabCompleter;
import jiekie.event.PlayerEvent;
import jiekie.util.NicknameManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class NicknamePlugin extends JavaPlugin {
    private NicknameManager nicknameManager;

    @Override
    public void onEnable() {
        // config
        FileConfiguration config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();

        nicknameManager = new NicknameManager(this);
        nicknameManager.loadPlayerNameData();

        //  이벤트 등록
        getServer().getPluginManager().registerEvents(new PlayerEvent(this), this);

        // 명령어 등록
        getCommand("닉네임").setExecutor(new NicknameCommand(this));

        // 자동완성 등록
        getCommand("닉네임").setTabCompleter(new NicknameTabCompleter(this));

        getLogger().info("닉네임 설정 플러그인 by Jiekie");
        getLogger().info("Copyright © 2025 Jiekie. All rights reserved.");
    }

    public NicknameManager getNicknameManager() {
        return nicknameManager;
    }

    @Override
    public void onDisable() {
        // config
        nicknameManager.savePlayerNameData();
    }
}
