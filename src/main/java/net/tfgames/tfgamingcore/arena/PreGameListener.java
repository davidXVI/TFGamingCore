package net.tfgames.tfgamingcore.arena;

import net.tfgames.tfgamingcore.TFGamingCore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;


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
            if (ArenaManager.isPreGame(arena.getID()) || arena.getSpectators().contains(player)) {
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
                if (ArenaManager.isPreGame(arena.getID()) || arena.getSpectators().contains(player)) {
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
                if (ArenaManager.isPreGame(arena.getID()) || arena.getSpectators().contains(player)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();
        Arena arena = ArenaManager.getArena(player);
        if(arena != null){
            if (ArenaManager.isPreGame(arena.getID())) {
                arena.removePlayer(player);
            }else if (ArenaManager.isPlaying(player)){
                arena.removePlayer(player);

            }
        }
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        Arena arena = ArenaManager.getArena(player);
        if(arena != null) {
            if (ArenaManager.isPreGame(arena.getID()) || arena.getSpectators().contains(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        Player player = e.getPlayer();
        Arena arena = ArenaManager.getArena(player);
        if(arena != null) {
            if (ArenaManager.isPreGame(arena.getID()) || arena.getSpectators().contains(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEditInventory(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        Arena arena = ArenaManager.getArena(player);
        if(arena != null) {
            if (ArenaManager.isPreGame(arena.getID()) || arena.getSpectators().contains(player)) {
                if(Objects.requireNonNull(e.getClickedInventory()).getType().equals(InventoryType.PLAYER)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        Player player = e.getPlayer();
        Arena arena = ArenaManager.getArena(player);
        if(arena != null) {
            if (ArenaManager.isPreGame(arena.getID()) || arena.getSpectators().contains(player)) {
                if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                    if(player.getInventory().getItemInMainHand().getType().equals(Material.RED_BED)){
                        arena.removePlayer(player);
                        player.sendRichMessage("<green>[✔] <gray>Enviando você para o Lobby...");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e){
        Player player = (Player) e.getEntity();
        Arena arena = ArenaManager.getArena(player);
        if(arena != null) {
            if (ArenaManager.isPreGame(arena.getID())) {
                e.setCancelled(true);
                player.setFoodLevel(20);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Player player = e.getPlayer();
        Arena arena = ArenaManager.getArena(player);
        if(arena != null) {
            if (ArenaManager.isPreGame(arena.getID()) || arena.getSpectators().contains(player)) {
                e.setCancelled(true);
            }
        }
    }

}

