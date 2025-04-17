package jiekie.util;

import jiekie.NicknamePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class NicknameManager {
    private final NicknamePlugin plugin;
    private final Map<UUID, PlayerNameData> nicknameMap = new HashMap<>();

    public NicknameManager(NicknamePlugin plugin) {
        this.plugin = plugin;
    }

    public void loadPlayerNameData() {
        nicknameMap.clear();
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection players = config.getConfigurationSection("players");

        if(players == null) return;

        for(String key : players.getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            String name = players.getString(key + ".name");
            String nickname = players.getString(key + ".nickname");

            nicknameMap.put(uuid, new PlayerNameData(name, nickname));
        }
    }

    public PlayerNameData getPlayerNameData(UUID uuid) {
        return nicknameMap.get(uuid);
    }

    public boolean existsPlayerNameData(UUID uuid) {
        return nicknameMap.containsKey(uuid);
    }

    public void setPlayerNameData(UUID uuid, PlayerNameData playerNameData) {
        nicknameMap.put(uuid, playerNameData);
    }

    public void removePlayerNameData(UUID uuid) {
        nicknameMap.remove(uuid);
    }

    public void savePlayerNameData() {
        FileConfiguration config = plugin.getConfig();
        config.set("players", null);

        for(Map.Entry<UUID, PlayerNameData> entry : nicknameMap.entrySet()) {
            String key = entry.getKey().toString();
            PlayerNameData value = entry.getValue();

            String path = "players." + key;
            config.set(path + ".name", value.getName());
            config.set(path + ".nickname", value.getNickname());
        }

        plugin.saveConfig();
    }

    public String getPlayerNickname(UUID uuid) {
        String nickname = null;
        if(existsPlayerNameData(uuid)) {
            nickname = getPlayerNameData(uuid).getNickname();
        }

        return nickname;
    }

    public List<String> getPlayerNameList() {
        List<String> names = new ArrayList<>();
        for(PlayerNameData data : nicknameMap.values()) {
            names.add(data.getName());
        }

        return names;
    }

    public List<String> getPlayerNicknameList() {
        List<String> nicknames = new ArrayList<>();
        for(PlayerNameData data : nicknameMap.values()) {
            nicknames.add(data.getNickname().replaceAll("§", "&"));
        }

        return nicknames;
    }

    public List<String> findPlayerNameByNickname(String nickname) {
        List<String> names = new ArrayList<>();
        for(PlayerNameData data : nicknameMap.values()) {
            if(data.getNickname().equals(nickname))
                names.add(data.getName());
        }

        return names;
    }

    public List<String> getCombinedNameAndNicknameList() {
        List<String> names = getPlayerNameList();
        List<String> nicknames = getPlayerNicknameList();
        nicknames.addAll(names);

        return nicknames;
    }

    public void applyNickname(Player player, String nickname) {
        UUID uuid = player.getUniqueId();
        String name = player.getName();

        PlayerNameData playerNameData;
        if(existsPlayerNameData(uuid)) {
            playerNameData = getPlayerNameData(uuid);
            playerNameData.setNickname(nickname);

        } else {
            playerNameData = new PlayerNameData(name, nickname);
        }
        setPlayerNameData(uuid, playerNameData);

        player.setDisplayName(nickname);
        player.setPlayerListName(nickname);

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        String teamName = "nickname_" + player.getName();

        Team team = scoreboard.getTeam(teamName);
        if(team == null)
            team = scoreboard.registerNewTeam(teamName);

        team.getEntries().forEach(team::removeEntry);
        team.addEntry(player.getName());

        team.setPrefix(ChatColor.RESET.toString());
        team.setSuffix(ChatColor.RESET.toString());
        team.setDisplayName(nickname);

        player.setScoreboard(scoreboard);
    }

    public void resetNickname(Player player) {
        UUID uuid = player.getUniqueId();
        String name = player.getName();

        removePlayerNameData(uuid);

        player.setDisplayName(name);
        player.setPlayerListName(name);

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        String teamName = "nickname_" + player.getName();

        Team team = scoreboard.getTeam(teamName);
        if(team != null)
            team.unregister();

        player.setScoreboard(scoreboard);
    }
}
