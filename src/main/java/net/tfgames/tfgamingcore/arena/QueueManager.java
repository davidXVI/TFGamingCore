package net.tfgames.tfgamingcore.arena;

import net.tfgames.tfgamingcore.games.GameState;
import net.tfgames.tfgamingcore.games.Games;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Objects;

public class QueueManager {

    public void entrarFila(Player player, Games game){
        int tentativas = 0;
        for(Arena arena : ArenaManager.getArenas()){
            if(tentativas == 0){
                player.sendRichMessage("<gold>[⚡] <gray>Procurando uma arena de <gold>" + game.getDisplay());
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 2f, 2f);
            }
            if(game.isRestricted()){
                player.sendRichMessage("<red>[❌] <gray>O minigame <gold>" + game.getDisplay() + "<gray> está desativado por enquanto!");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                break;
            }
            if(arena.getState() == GameState.RECRUTANDO || arena.getState() == GameState.PERSISTENTE || arena.getState() == GameState.INICIANDO && arena.getGame(arena.getID()).equals(game)){
                if(arena.getPlayers().size() < ArenaConfig.getMaxPlayers(arena.getID())){
                    if(!arena.getState().equals(GameState.JOGANDO) && arena.getGame(arena.getID()).equals(game)){
                        Objects.requireNonNull(ArenaManager.getArena(arena.getID())).addPlayer(player);
                        player.sendRichMessage("<green>[✔] <gray>Jogo encontrado! Enviando você ao jogo <gold>" + game.getKey() + arena.getID());
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 2f, 2f);
                        break;
                    }
                }
            }
            tentativas++;
            if(tentativas == ArenaManager.getArenas().size()){
                player.sendRichMessage("<red>[❌] <gray>Nenhuma arena encontrada!\n" +
                        "<gold>[⚡] <gray>Se você acredita que isso seja um erro, contate um staff com o erro: <gold>2");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                break;
            }
        }
    }

}
