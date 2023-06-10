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
import net.tfgames.tfgamingcore.util.FileUtil;
import net.tfgames.tfsandbox.game.GameSandbox;
import net.tfgames.tfservercore.TFServerCore;
import net.tfgames.tfservercore.lobby.Lobby;
import net.tfgames.tfuhcgame.game.GameUHC;
import net.tfgames.tfuhcgame.game.world.MapLoader;
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
    private GameUHC gameUHC;
    private GameSandbox gameSandbox;

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
        }else if(getGame(id).equals(Games.UHC)){
            gameUHC = new GameUHC(this);
        }else if(getGame(id).equals(Games.SANDBOX)){
            gameSandbox = new GameSandbox(this);
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
        if(getGame(id).equals(Games.UHC)){
            gameUHC.start();
        }
        if(getGame(id).equals(Games.SANDBOX)){
            gameSandbox.start();
        }
    }

    //ADICIONAR JOGADOR
    public void addPlayer(Player player){

        Lobby.removePlayer(player);
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
            if(getGame(id).equals(Games.UHC)){
                gameUHC.generateArena();
            }
        }
    }

    //REMOVER JOGADOR
    public void removePlayer(Player player){
        if(Objects.requireNonNull(ArenaManager.getArena(player)).getGame(id).equals(Games.UHC)){
            GameUHC.alivePlayers.remove(player);
        }else if(Objects.requireNonNull(ArenaManager.getArena(player)).getGame(id).equals(Games.ARENA)){
            GameArena.ingamePlayers.remove(player);
            GameArena.kills.remove(player);
        }

        if(player.isOnline()){Lobby.addPlayer(player);}
        if(spectators.contains(player)){removeSpectator(player);}

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

    public void sendSound(Sound sound){
        for(Player player : players){
            player.playSound(player, sound, 1.0F, 1.0F);
        }
    }

    public void reset() {
        for (Player player : players) {
            Lobby.addPlayer(player);
            teams.clear();
            spectators.clear();
        }

        state = GameState.RECRUTANDO;
        players.clear();
        spectators.clear();
        teams.clear();
        countdown = new Countdown(plugin,this);
        canJoin = true;
        canSpectate = true;
        isPrivate = false;

        if(getGame(id).needsMapRestore()){gameMap.restoreFromSource();}
        if(getGame(id).equals(Games.UHC)){deleteUHCWorld();}

    }

    public void softReset(){
        state = GameState.RECRUTANDO;
        waitingSpawn = arenaConfig.getArenaSpawn(id);
        canJoin = true;
        canSpectate = true;
        isPrivate = false;
        new Countdown(plugin, this);
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
        player.setFlying(true);
        player.addPotionEffect(PotionEffectType.SPEED.createEffect(999999999, 3));
        player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(99999999, 3));
    }

    public void removeSpectator(Player player){
        spectators.remove(player);
        player.setAllowFlight(false);
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        for (Player player2 : Bukkit.getOnlinePlayers()) {player2.showPlayer(plugin, player);}
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    //LOCATION MANAGER
    public Location getWaitingSpawn(){
        return waitingSpawn;
    }

    public static void deleteUHCWorld(){
        World world = Bukkit.getWorld("Vanilla");
        if(world != null){
            FileUtil.delete(world.getWorldFolder());
        }
    }

}
