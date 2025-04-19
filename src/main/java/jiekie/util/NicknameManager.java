package jiekie.util;

import jiekie.NicknamePlugin;
import jiekie.exception.ApplyNicknameException;
import jiekie.exception.ResetNicknameException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.Collectors;

public class NicknameManager {
    private final NicknamePlugin plugin;
    private final Map<UUID, PlayerNameData> playerNameDataMap = new HashMap<>();
    private final Map<String, UUID> nicknameMap = new HashMap<>();
    private final String PLAYERS_PREFIX = "players";

    public NicknameManager(NicknamePlugin plugin) {
        this.plugin = plugin;
    }

    public void loadPlayerNameData() {
        playerNameDataMap.clear();
        nicknameMap.clear();

        FileConfiguration config = plugin.getConfig();
        ConfigurationSection players = config.getConfigurationSection(PLAYERS_PREFIX);

        if(players == null) return;
        for(String key : players.getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            String name = players.getString(key + ".name");
            String nickname = players.getString(key + ".nickname");
            PlayerNameData playerNameData = new PlayerNameData(uuid, name, nickname);
            playerNameData.setOnline(false);

            Player player = Bukkit.getPlayer(uuid);
            if(player != null && player.isOnline())
                playerNameData.setOnline(true);

            playerNameDataMap.put(uuid, playerNameData);
            nicknameMap.put(nickname, uuid);
        }
    }

    private boolean isNicknameTaken(Player player, String nickname) {
        UUID uuid = player.getUniqueId();
        if(!nicknameMap.containsKey(nickname))
            return false;

        UUID nicknameUUID = nicknameMap.get(nickname);
        return !uuid.equals(nicknameUUID);
    }

    public void applyNickname(Player player, String nickname) throws ApplyNicknameException {
        if(isNicknameTaken(player, nickname))
            throw new ApplyNicknameException(ChatUtil.NICKNAME_DUPLICATED);

        if(playerNameDataMap.containsKey(player.getUniqueId()))
            changeNickname(player, nickname);
        else
            createNickname(player, nickname);

        player.setDisplayName(nickname);
        player.setPlayerListName(nickname);

        addPlayerToTeam(player, nickname);
    }

    public void resetNickname(Player player) throws ResetNicknameException {
        UUID uuid = player.getUniqueId();
        if(!playerNameDataMap.containsKey(uuid))
            throw new ResetNicknameException(ChatUtil.NICKNAME_NOT_SET);

        PlayerNameData playerNameData = getPlayerNameDataByUuid(uuid);
        String nickname = playerNameData.getNickname();

        playerNameDataMap.remove(player.getUniqueId());
        nicknameMap.remove(nickname);

        String name = playerNameData.getName();
        player.setDisplayName(name);
        player.setPlayerListName(name);

        removePlayerFromTeam(player);
    }

    public PlayerNameData getPlayerNameDataByUuid(UUID uuid) {
        return playerNameDataMap.get(uuid);
    }

    public Player getPlayerByNameOrNickname(String name) {
        if(name == null) return null;

        if(nicknameMap.containsKey(name))
            return Bukkit.getPlayer(nicknameMap.get(name));

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.getName().equals(name))
                return player;
        }

        return null;
    }

    private void createNickname(Player player, String nickname) {
        PlayerNameData playerNameData = new PlayerNameData(player.getUniqueId(), player.getName(), nickname);
        playerNameDataMap.put(player.getUniqueId(), playerNameData);
        nicknameMap.put(nickname, player.getUniqueId());
    }

    private void changeNickname(Player player, String nickname) {
        PlayerNameData playerNameData = getPlayerNameDataByUuid(player.getUniqueId());
        String oldNickname = playerNameData.getNickname();

        nicknameMap.remove(oldNickname);
        nicknameMap.put(nickname, player.getUniqueId());

        playerNameData.setNickname(nickname);
    }

    private void addPlayerToTeam(Player player, String nickname) {
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

    private void removePlayerFromTeam(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        String teamName = "nickname_" + player.getName();

        Team team = scoreboard.getTeam(teamName);
        if(team != null)
            team.unregister();

        player.setScoreboard(scoreboard);
    }

    public List<String> getPlayerNameAndNicknameList() {
        List<String> nicknameList = getPlayerNicknameList();
        nicknameList.addAll(getPlayerNameList());
        return nicknameList;
    }

    public List<String> getPlayerNicknameList() {
        List<String> nicknameList = new ArrayList<>();
        for(PlayerNameData data : playerNameDataMap.values()) {
            if(!data.isOnline()) continue;
            nicknameList.add(data.getNickname().replaceAll("§", "&"));
        }

        return nicknameList;
    }

    public List<String> getPlayerNameList() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    public void savePlayerNameData() {
        FileConfiguration config = plugin.getConfig();
        config.set(PLAYERS_PREFIX, null);

        for(Map.Entry<UUID, PlayerNameData> entry : playerNameDataMap.entrySet()) {
            String key = entry.getKey().toString();
            PlayerNameData value = entry.getValue();

            String path = PLAYERS_PREFIX + "." + key;
            config.set(path + ".name", value.getName());
            config.set(path + ".nickname", value.getNickname());
            config.set(path + ".is_online", false);
        }

        plugin.saveConfig();
    }
}
