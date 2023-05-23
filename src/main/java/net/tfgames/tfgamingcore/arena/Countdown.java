package net.tfgames.tfgamingcore.arena;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tfgames.tfgamingcore.TFGamingCore;
import net.tfgames.tfgamingcore.games.GameState;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {

    private final TFGamingCore plugin;
    private final Arena arena;
    private int seconds;

    public Countdown (TFGamingCore plugin, Arena arena){
        this.plugin = plugin;
        this.arena = arena;
        this.seconds = plugin.getArenaConfig().getCountdownSeconds(arena.getID());
    }

    public void start(){
        arena.setState(GameState.INICIANDO);
        runTaskTimer(plugin, 0, 20);
    }

    @Override
    public void run() {
        MiniMessage mm = MiniMessage.miniMessage();

        Component insPlayers = mm.deserialize("<gold>[⚡] <red>Jogadores insuficientes! Início cancelado.");
        Component startingMessage = mm.deserialize("<gold>[⌚] <green>O jogo vai iniciar em <gold>" + seconds + " <green>segundos.");
        Component starting1sMessage = mm.deserialize("<gold>[⌚] <green>O jogo vai iniciar em <gold>" + seconds + " <green>segundo.");
        Component startingTitle = mm.deserialize("<gold>" + seconds);
        Component startingSubtitle = mm.deserialize("<gold>Prepare-se para jogar!");

        if(arena.getPlayers().size() < plugin.getArenaConfig().getRequiredPlayers(arena.getID())){
            arena.setState(GameState.RECRUTANDO);
            arena.sendMessage(insPlayers);
            arena.softReset();
            cancel();
            return;
        }
        if(seconds == 0){
            cancel();
            arena.start();
            return;
        }
        if(seconds <= 10 || seconds % 10 == 0){
            if(seconds <= 5 && seconds > 1){
                arena.sendMessage(startingMessage);
                arena.sendPingTitle(startingTitle, startingSubtitle);
            }else if (seconds == 1){
                arena.sendMessage(starting1sMessage);
                arena.sendPingTitle(startingTitle, startingSubtitle);
            }
        }
        seconds--;
    }

    public int getSeconds(){
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

}
