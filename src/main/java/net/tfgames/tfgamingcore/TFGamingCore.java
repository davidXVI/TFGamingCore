package net.tfgames.tfgamingcore;

import net.kyori.adventure.text.format.NamedTextColor;
import net.tfgames.tfarenagame.TFArenaGame;
import net.tfgames.tfgamingcore.arena.*;
import net.tfgames.tfgamingcore.command.commands.ArenaCommand;
import net.tfgames.tfgamingcore.command.commands.EntrarCommand;
import net.tfgames.tfgamingcore.command.commands.LobbyCommand;
import net.tfgames.tfgamingcore.command.commands.RestrictGameCommand;
import net.tfgames.tfgamingcore.map.GameMap;
import net.tfgames.tfgamingcore.util.MessageUtil;
import net.tfgames.tfservercore.TFServerCore;
import net.tfgames.tfservercore.api.PlayerManager;
import net.tfgames.tfservercore.tags.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;

public final class TFGamingCore extends JavaPlugin {

    private static TFGamingCore instance;
    private TFServerCore serverCore;
    public TFArenaGame arenaGame;
    private ArenaConfig arenaConfig;
    private ArenaManager arenaManager;
    private MessageUtil messageUtil;
    private static QueueManager queueManager;
    private GameMap map;
    private final File gameMapsFolder = new File(getDataFolder(), "gameMaps");

    @Override
    public void onEnable() {
        serverCore = (TFServerCore) Bukkit.getPluginManager().getPlugin("TFServerCore");
        arenaConfig = new ArenaConfig();
        arenaConfig.setupConfig(this);
        arenaManager = new ArenaManager(this);
        messageUtil = new MessageUtil();
        queueManager = new QueueManager();

        //WORLD RESET MANAGER
        if(!gameMapsFolder.exists()){
            gameMapsFolder.mkdirs();
        }

        arenaGame = (TFArenaGame) Bukkit.getPluginManager().getPlugin("TFArenaGame");

        //COMANDO REGISTRO
        new LobbyCommand(this);
        new ArenaCommand(this);
        new EntrarCommand(this);
        new RestrictGameCommand(this);

        Bukkit.getPluginManager().registerEvents(new PreGameListener(this), this);

        //INGAME TEAM CREATOR


    }

    @Override
    public void onDisable() {
        if(map != null){
            map.unload();
        }
        Arena.deleteUHCWorld();
    }

    public TFServerCore getServerCore(){return serverCore;}
    public ArenaConfig getArenaConfig(){
        return arenaConfig;
    }
    public File getGameMapsFolder(){return gameMapsFolder; }
    public ArenaManager getArenaManager(){ return arenaManager; }
    public MessageUtil getMessageUtil() {return messageUtil;}
    public static QueueManager getQueueManager() {return queueManager;}

    public static TFGamingCore getInstance() {return instance;}

    //CRIA TIMES
    public void createScoreboardNametags(){

        Scoreboard board = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
        if(board.getTeam("ingamePlayers") == null){
            Team team = board.registerNewTeam("ingamePlayers");
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
            team.color(NamedTextColor.YELLOW);
        }
    }

}
