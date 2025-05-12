package jiekie.nickname;

import jiekie.nickname.api.NicknameAPI;
import jiekie.nickname.command.NicknameCommand;
import jiekie.nickname.command.NicknameTicketCommand;
import jiekie.nickname.completer.NicknameTabCompleter;
import jiekie.nickname.event.PlayerEvent;
import jiekie.nickname.manager.NicknameManager;
import jiekie.nickname.util.PacketNames;
import org.bukkit.Bukkit;
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
        getCommand("입주신고서").setExecutor(new NicknameTicketCommand(this));

        // 자동완성 등록
        getCommand("닉네임").setTabCompleter(new NicknameTabCompleter(this));

        // 패킷 등록
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, PacketNames.SET_NICKNAME);

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
