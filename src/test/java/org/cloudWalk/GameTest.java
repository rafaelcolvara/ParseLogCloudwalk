package org.cloudWalk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        Game.getInstance().resetScore();
        game = Game.getInstance();
    }

    @Test
    void singletonGameInstance() {
        Game anotherInstance = Game.getInstance();
        assertSame(game, anotherInstance, "Both instances should be the same object");
    }

    @Test
    void addPlayer() {
        game.addPlayer("player1");
        assertTrue(game.getPlayers().contains("player1"), "Player1 should be added to the players set");
    }

    @Test
    void ignoreWorldAsPlayer() {
        game.addPlayer("<world>");
        assertFalse(game.getPlayers().contains("<world>"), "<world> should not be added to the players set");
    }

    @Test
    void addKill() {
        game.addPlayer("killer");
        game.addPlayer("victim");
        game.addKill("killer", "victim", "MOD_SHOTGUN");
        assertEquals(1, game.getKills().get("killer"), "Killer should have 1 kill");
        assertEquals(1, game.getKillsByMeans().get("MOD_SHOTGUN"), "There should be 1 kill by MOD_SHOTGUN");
    }
    @Test
    void subKillByWorld() {
        game.addPlayer("<world>");
        game.addPlayer("victim");
        game.addKill("<world>", "victim", "MOD_TRIGGER_HURT");
        assertEquals(-1, game.getKills().get("victim"), "Victim should have -1 kills due to <world> kill");
    }


    @Test
    void totalKillsCount() {
        game.addKill("player1", "player2", "MOD_RAILGUN");
        game.addKill("<world>", "player2", "MOD_FALLING");
        assertEquals(2, game.getTotalKills(), "Total kills should count all kill events");
    }


}