package net.tfgames.tfgamingcore.arena;

import net.tfgames.tfgamingcore.TFGamingCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PreGameListener implements Listener {

    private final TFGamingCore plugin;

    public PreGameListener(TFGamingCore plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        Arena arena = ArenaManager.getArena(player);
        if(arena != null) {
            if (ArenaManager.isPreGame(arena.getID())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            Player player = (Player) e.getEntity();
            Arena arena = ArenaManager.getArena(player);
            if(arena != null) {
                if (ArenaManager.isPreGame(arena.getID())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDamagePlayer(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player){
            Player player = (Player) e.getEntity();
            Arena arena = ArenaManager.getArena(player);
            if(arena != null) {
                if (ArenaManager.isPreGame(arena.getID())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
            Player player = e.getPlayer();
            Arena arena = ArenaManager.getArena(player);
            if(arena != null) {
                if (ArenaManager.isPreGame(arena.getID())) {
                    e.setCancelled(true);
                }
            }
        }
    }
