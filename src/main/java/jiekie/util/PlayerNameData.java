package jiekie.util;

import java.util.UUID;

public class PlayerNameData {
    private final UUID uuid;
    private final String name;
    private String nickname;
    private boolean isOnline;

    public PlayerNameData(UUID uuid, String name, String nickname) {
        this.uuid = uuid;
        this.name = name;
        this.nickname = nickname;
        this.isOnline = true;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
