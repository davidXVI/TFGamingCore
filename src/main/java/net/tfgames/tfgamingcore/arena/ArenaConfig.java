package net.tfgames.tfgamingcore.arena;

import net.tfgames.tfgamingcore.TFGamingCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class ArenaConfig {

    private static FileConfiguration config;

    public void setupConfig(TFGamingCore plugin){
        plugin.saveDefaultConfig();
        ArenaConfig.config = plugin.getConfig();
    }

    public String getGameType(int id){ return config.getString("arenas." + id + ".game-type"); }
    public int getRequiredPlayers(int id){
        return config.getInt("arenas." + id + ".required-players");
    }
    public int getMaxPlayers(int id){
        return config.getInt("arenas." + id + ".max-players");
    }
    public int getCountdownSeconds(int id){
        return config.getInt("countdown-seconds");
    }
    public int getArenaAmount(){
        return config.getConfigurationSection("arenas.").getKeys(false).size();
    }

    public Location getArenaSpawn (int id){
        return new Location(
                Bukkit.getWorld(Objects.requireNonNull(config.getString("arenas." + id + ".world"))),
                config.getDouble("arenas." + id + ".x"),
                config.getDouble("arenas." + id + ".y"),
                config.getDouble("arenas." + id + ".z"));
    }

    public String getArenaWorld(int id){
        return config.getString("arenas." + id + ".world");
    }

    public World getArenaWorldMap(int id){
        return Bukkit.getWorld(Objects.requireNonNull(config.getString("arenas." + id + ".world")));
    }

}
