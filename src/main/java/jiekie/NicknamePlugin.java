package jiekie;

import jiekie.api.NicknameAPI;
import jiekie.command.NicknameCommand;
import jiekie.completer.NicknameTabCompleter;
import jiekie.event.PlayerEvent;
import jiekie.manager.NicknameManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class NicknamePlugin extends JavaPlugin {
    private NicknameManager nicknameManager;

    @Override
    public void onEnable() {
        // config
        saveDefaultConfig();
        reloadConfig();

        nicknameManager = new NicknameManager(this);
        nicknameManager.loadPlayerNameData();

        //  이벤트 등록
        getServer().getPluginManager().registerEvents(new PlayerEvent(this), this);

        // 명령어 등록
        getCommand("닉네임").setExecutor(new NicknameCommand(this));

        // 자동완성 등록
        getCommand("닉네임").setTabCompleter(new NicknameTabCompleter(this));

        // API 등록
        NicknameAPI.initialize(nicknameManager);

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
