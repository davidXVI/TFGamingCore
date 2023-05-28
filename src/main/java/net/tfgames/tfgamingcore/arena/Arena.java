package net.tfgames.tfgamingcore.arena;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.tfgames.tfarenagame.TFArenaGame;
import net.tfgames.tfarenagame.game.GameArena;
import net.tfgames.tfgamingcore.TFGamingCore;
import net.tfgames.tfgamingcore.games.GameState;
import net.tfgames.tfgamingcore.games.Games;
import net.tfgames.tfgamingcore.games.Teams;
import net.tfgames.tfgamingcore.map.GameMap;
import net.tfgames.tfgamingcore.map.LocalGameMap;
import net.tfgames.tfservercore.TFServerCore;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Arena {

    //CORES
    private final TFGamingCore plugin;
    private final TFServerCore serverCore;
    private final ArenaConfig arenaConfig;
    private final ArenaSidebar arenaSidebar;
    private final GameMap gameMap;
    //INFO ARENA
    private final int id;
    private Location waitingSpawn;
    private final ArrayList<Player> players;
    private final ArrayList<Player> spectators;
    private GameState state;
    private Countdown countdown;
    private HashMap<Player, Teams> teams;
    private GameArena gameArena;

    //BOOLEANS
    private boolean canJoin;
    private boolean canSpectate;
    private boolean isPrivate;

    //ARENA INICIALIZADOR
    public Arena(TFGamingCore plugin, int id) {

        this.plugin = plugin;
        this.serverCore = plugin.getServerCore();
        this.arenaConfig = plugin.getArenaConfig();
        this.arenaSidebar = new ArenaSidebar(plugin, this);

        //REGISTRO DE JOGOS
        if(getGame(id).equals(Games.ARENA)){
            gameArena = new GameArena(this);
        }

        //REGISTRO DE MAPA
        //arenaConfig.getArenaWorldMap(id).setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        //arenaConfig.getArenaWorldMap(id).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        //arenaConfig.getArenaWorldMap(id).setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        //arenaConfig.getArenaWorldMap(id).setGameRule(GameRule.DO_ENTITY_DROPS, false);
        //arenaConfig.getArenaWorldMap(id).setGameRule(GameRule.DO_FIRE_TICK, false);

        this.id = id;
        this.waitingSpawn = arenaConfig.getArenaSpawn(id);
        this.state = GameState.RECRUTANDO;
        this.teams = new HashMap<>();
        this.countdown = new Countdown(plugin, this);
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.teams = new HashMap<>();

        canJoin = true;
        canSpectate = true;
        isPrivate = false;

        //REGISTRO DE MAPA
        if(getGame(id).needsMapRestore()){
            gameMap = new LocalGameMap(plugin.getGameMapsFolder(), arenaConfig.getArenaWorld(id), true);
            arenaConfig.getArenaWorldMap(id).setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            arenaConfig.getArenaWorldMap(id).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            arenaConfig.getArenaWorldMap(id).setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            arenaConfig.getArenaWorldMap(id).setGameRule(GameRule.DO_ENTITY_DROPS, false);
            arenaConfig.getArenaWorldMap(id).setGameRule(GameRule.DO_FIRE_TICK, false);
        }else{
            gameMap = new LocalGameMap(plugin.getGameMapsFolder(), "", false);
        }

    }

    public void start(){
        if(getGame(id).equals(Games.ARENA)){
            gameArena.start();
        }
    }

    //ADICIONAR JOGADOR
    public void addPlayer(Player player){

        players.add(player);

        //RESETANDO JOGADOR E TELEPORTANDO O JOGADOR
        if(player.getAllowFlight()){ player.setFlying(false); }
        player.setWalkSpeed(0.2f);
        player.setLevel(0);
        player.setExp(0);
        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.teleport(waitingSpawn);

        if(getGame(id).isTeamGame()){arenaSidebar.createTeamScoreboard(player);}
        else{arenaSidebar.createSoloScoreboard(player);}

        MiniMessage mm = MiniMessage.miniMessage();
        Component joinMessage = mm.deserialize("<gray>[<green>+<gray>] ")
                .append(Component.text(player.getName()).color(serverCore.getPlayerManager().getRank(player).getTextColor())
                .append(Component.text(" entrou na partida.").color(NamedTextColor.YELLOW)));
        sendMessage(joinMessage);

        //FASE INICIAR
        if (state.equals(GameState.RECRUTANDO) && players.size() >= arenaConfig.getRequiredPlayers(getID())) {
            countdown.start();
        }
    }

    //REMOVER JOGADOR
    public void removePlayer(Player player){

        if(player.isOnline()){
            serverCore.getLobby().addPlayer(player);
        }
        players.remove(player);
        removeTeam(player);
        removeSpectator(player);

    }

    public int getID(){
        return id;
    }

    //PLAYER GETTERS
    public ArrayList<Player> getPlayers(){ return players; }
    public ArrayList<Player> getSpectators(){ return spectators; }

    //GAMESTATES
    public void setState(GameState state){
        this.state = state;
    }
    public GameState getState(){ return state; }

    //MESSAGE UTIL
    public void sendMessage(Component message){
        for(Player player : players){
            player.sendMessage(message);
        }
    }

    public void sendMessage(String message){
        for(Player player : players){
            player.sendMessage(message);
        }
    }

    public void sendPingMessage(Component message){
        for(Player player : players){
            player.sendMessage(message);
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 2.0F, 2.0F);
        }
    }

    public void sendActionBar(Component message){
        for(Player player : players){
            player.sendActionBar(message);
        }
    }

    public void sendPingActionBar(Component message){
        for(Player player : players){
            player.sendActionBar(message);
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 2.0F, 2.0F);
        }
    }

    public void sendPingTitle(Component title, Component subtitle){
        for(Player player : players){
            Title mainTitle = Title.title(title, subtitle);
            player.showTitle(mainTitle);
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 2.0F, 2.0F);
        }
    }

    public void sendTitle(Component title, Component subtitle){
        for(Player player : players){
            Title mainTitle = Title.title(title, subtitle);
            player.showTitle(mainTitle);
        }
    }

    public void reset() {
        for (Player player : players) {
            serverCore.getLobby().addPlayer(player);
            teams.clear();
            spectators.clear();
        }

        if(getGame(id).needsMapRestore()){gameMap.restoreFromSource();}

        state = GameState.RECRUTANDO;
        players.clear();
        spectators.clear();
        teams.clear();
        countdown = new Countdown(plugin,this);
        canJoin = true;
        canSpectate = true;
        isPrivate = false;

    }

    public void softReset(){
        state = GameState.RECRUTANDO;
        new Countdown(plugin, this);
        waitingSpawn = arenaConfig.getArenaSpawn(id);
        canJoin = true;
        canSpectate = true;
        isPrivate = false;
    }

    //GAME MANAGER
    public Games getGame(int id){
        String arenaKey = arenaConfig.getGameType(id);
        for(Games game : Games.values()){
            if(game.getKey().equals(arenaKey)){
                return game;
            }
        }
        return null;
    }

    public Countdown getCountdown(){
        return countdown;
    }

    public void setCanJoin(boolean set){
        this.canJoin = set;
    }
    public void setCanSpectate(boolean set){
        this.canSpectate = set;
    }
    public void setPrivate(boolean set){
        this.isPrivate = set;
    }

    //TEAM MANAGER
    public void setTeam(Player player, Teams team){
        removeTeam(player);
        teams.put(player, team);
    }

    public void removeTeam(Player player){
        teams.remove(player);
    }
    public Teams getTeam(Player player){
        return teams.get(player);
    }

    //SPECTATOR MANAGER
    public void setSpectator(Player player){
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        spectators.add(player);
        for (Player player2 : players) {
            player2.hidePlayer(plugin, player);
        }
        player.setAllowFlight(true);
        player.addPotionEffect(PotionEffectType.SPEED.createEffect(999999999, 3));
        player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(99999999, 3));
    }

    public void removeSpectator(Player player){
        spectators.remove(player);
        player.setAllowFlight(false);
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    //LOCATION MANAGER
    public Location getWaitingSpawn(){
        return waitingSpawn;
    }

}
