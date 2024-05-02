package org.cloudWalk;
import com.google.gson.Gson;

import java.util.*;

public class Game {
    private int totalKills;
    private Set<String> players;
    private Map<String, Integer> kills;
    private Map<String, Integer> killsByMeans;

    private static Game instance;  // Singleton instance

    private int current_game=1;
    public Game() {
        resetScore();
    }

    public static synchronized Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    void resetScore(){

        this.players = new HashSet<>();
        this.kills = new HashMap<>();
        this.killsByMeans = new HashMap<>();
        totalKills=0;

    }
    public void addPlayer(String player) {
        if (!player.equals("<world>")) {
            this.players.add(player);
            this.kills.putIfAbsent(player, 0);
        }
    }

    public void addKill(String killer, String victim, String method) {
        this.totalKills++;
        if (!victim.equals("<world>")) {
            this.addPlayer(victim);
        }

        if (killer.equals("<world>")) {
            if (this.kills.containsKey(victim)) {
                this.kills.put(victim, this.kills.get(victim) - 1);
            }
        } else {

            this.addPlayer(killer);
            if (!killer.equals(victim) && this.kills.containsKey(killer)) {
                this.kills.put(killer, this.kills.get(killer) + 1);
            }
        }

        this.killsByMeans.put(method, this.killsByMeans.getOrDefault(method, 0) + 1);
    }

    public Map<String, Integer> getKillsByMeans() {
        return killsByMeans;
    }

    public Set<String> getPlayers() {
        return players;
    }

    public Map<String, Integer> getKills(){
        return kills;
    }

    public Integer getTotalKills(){
        return totalKills;
    }
    public void printSummary() {

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("total_kills", this.totalKills );
        summary.put("players", this.players);
        summary.put("kills",  this.kills);
        summary.put("kills_by_means", this.getKillsByMeans());

        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("game_" + current_game++ , summary);


        Gson result = new Gson();
        String jsonOutput = result.toJson(wrapper);
        System.out.println(jsonOutput);
        resetScore();
    }
}
