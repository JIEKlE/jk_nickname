package jiekie.api;

import jiekie.util.NicknameManager;
import org.bukkit.entity.Player;

import java.util.List;

public class NicknameAPI {
    private static NicknameAPI instance;
    private final NicknameManager nicknameManager;

    private NicknameAPI(NicknameManager nicknameManager) {
        this.nicknameManager = nicknameManager;
    }

    public static void initialize(NicknameManager nicknameManager) {
        if(instance != null) return;
        instance = new NicknameAPI(nicknameManager);
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    public static NicknameAPI getInstance() {
        return instance;
    }

    public List<String> getPlayerNameAndNicknameList() {
        return nicknameManager.getPlayerNameAndNicknameList();
    }

    public List<String> getPlayerNicknameList() {
        return nicknameManager.getPlayerNicknameList();
    }

    public List<String> getPlayerNameList() {
        return nicknameManager.getPlayerNameList();
    }

    public Player getPlayerByNameOrNickname(String name) {
        return nicknameManager.getPlayerByNameOrNickname(name);
    }
}
