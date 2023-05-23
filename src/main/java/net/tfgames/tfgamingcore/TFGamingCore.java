package net.tfgames.tfgamingcore;

import net.tfgames.tfarenagame.TFArenaGame;
import net.tfgames.tfgamingcore.arena.ArenaConfig;
import net.tfgames.tfgamingcore.arena.ArenaManager;
import net.tfgames.tfgamingcore.arena.PreGameListener;
import net.tfgames.tfgamingcore.command.commands.ArenaCommand;
import net.tfgames.tfgamingcore.command.commands.EntrarCommand;
import net.tfgames.tfgamingcore.command.commands.LobbyCommand;
import net.tfgames.tfgamingcore.map.GameMap;
import net.tfgames.tfgamingcore.util.MessageUtil;
import net.tfgames.tfservercore.TFServerCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class TFGamingCore extends JavaPlugin {

    private static TFGamingCore instance;
    private TFServerCore serverCore;
    public TFArenaGame arenaGame;
    private ArenaConfig arenaConfig;
    private ArenaManager arenaManager;
    private MessageUtil messageUtil;
    private GameMap map;
    private final File gameMapsFolder = new File(getDataFolder(), "gameMaps");

    @Override
    public void onEnable() {
        serverCore = (TFServerCore) Bukkit.getPluginManager().getPlugin("TFServerCore");
        arenaConfig = new ArenaConfig();
        arenaConfig.setupConfig(this);
        arenaManager = new ArenaManager(this);
        messageUtil = new MessageUtil();

        //WORLD RESET MANAGER
        if(!gameMapsFolder.exists()){
            gameMapsFolder.mkdirs();
        }

        arenaGame = (TFArenaGame) Bukkit.getPluginManager().getPlugin("TFArenaGame");

        //COMANDO REGISTRO
        new LobbyCommand(this);
        new ArenaCommand(this);
        new EntrarCommand(this);

        Bukkit.getPluginManager().registerEvents(new PreGameListener(this), this);
    }

    @Override
    public void onDisable() {
        if(map != null){
            map.unload();
        }
    }

    public TFServerCore getServerCore(){return serverCore;}
    public ArenaConfig getArenaConfig(){
        return arenaConfig;
    }
    public File getGameMapsFolder(){return gameMapsFolder; }
    public ArenaManager getArenaManager(){ return arenaManager; }
    public MessageUtil getMessageUtil() {return messageUtil;}
    public static TFGamingCore getInstance() {return instance;}
}
