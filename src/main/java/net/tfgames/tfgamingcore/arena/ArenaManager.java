package net.tfgames.tfgamingcore.arena;

import net.tfgames.tfgamingcore.TFGamingCore;
import net.tfgames.tfgamingcore.games.GameState;
import net.tfgames.tfgamingcore.games.Games;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArenaManager {

    protected final TFGamingCore plugin;
    protected final ArenaConfig arenaConfig;

    private static List<Arena> arenas = new ArrayList<>();

    public ArenaManager(TFGamingCore plugin){
        this.plugin = plugin;
        this.arenaConfig = plugin.getArenaConfig();

        arenas = new ArrayList<>();

        for(int i = 0; i <= arenaConfig.getArenaAmount() - 1; i++){
            arenas.add(new Arena(plugin, i));
        }
    }

    //GETTERS

    public static List<Arena> getArenas(){ return arenas; }

    public Games getGame(Arena arena){
        String arenaKey = arenaConfig.getGameType(arena.getID());
        for (Games game : Games.values()){
            if(game.getKey().equals(arenaKey)){
                return game;
            }
        }
        return null;
    }

    public static Arena getArena(Player player){
        for(Arena arena : arenas){
            if(arena.getPlayers().contains(player)){
                return arena;
            }
        }
        return null;
    }

    public static Arena getArena(int id){
        for(Arena arena : arenas){
            if(arena.getID() == id){
                return arena;
            }
        }
        return null;
    }

    public static Arena getArena(World world){
        for(Arena arena : arenas){
            if(arena.getWaitingSpawn().getWorld().getName().equals(world.getName())){
                return arena;
            }
        }
        return null;
    }

    //CHECKERS
    public static boolean hasArena(Games game){
        for(Arena arena : arenas){
            if(arena.getGame(arena.getID()).equals(game)){
                return true;
            }
        }
        return false;
    }

    public static boolean isPlaying(Player player){
        for(Arena arena : arenas){
            if(arena.getPlayers().contains(player)){
                return true;
            }
        }
        return false;
    }

    public static boolean isSpectating(Player player){
        for(Arena arena : arenas){
            if(arena.getSpectators().contains(player)){
                return true;
            }
        }
        return false;
    }

    public static boolean isRecruiting(int id){
        return Objects.requireNonNull(getArena(id)).getState() == GameState.RECRUTANDO;
    }

    public static boolean isPreGame(int id){
        return Objects.requireNonNull(getArena(id)).getState().equals(GameState.INICIANDO) || Objects.requireNonNull(getArena(id)).getState().equals(GameState.RECRUTANDO);
    }

    public static boolean isStatePlaying(int id){
        return Objects.requireNonNull(getArena(id)).getState().equals(GameState.JOGANDO);
    }

    public static boolean isArenaWorld(World world){
        for(Arena arena : arenas){
            if(arena.getWaitingSpawn().getWorld().getName().equals(world.getName())){
                return true;
            }
        }
        return false;
    }

}
