package jiekie.util;

public class PlayerNameData {
    private final String name;
    private String nickname;

    public PlayerNameData(String name, String nickname) {
        this.name = name;
        this.nickname = nickname;
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
}
