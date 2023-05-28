package net.tfgames.tfgamingcore.command.commands;

import net.tfgames.tfgamingcore.TFGamingCore;
import net.tfgames.tfgamingcore.arena.Arena;
import net.tfgames.tfgamingcore.arena.ArenaManager;
import net.tfgames.tfgamingcore.command.Command;
import net.tfgames.tfgamingcore.games.GameState;
import net.tfgames.tfgamingcore.util.ErrorMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntrarCommand extends Command {
    public EntrarCommand(TFGamingCore plugin) {
        super(
                plugin,
                "entrar",
                new String[]{"join"},
                "",
                "",
                true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 1){
            if(!ArenaManager.isPlaying(player)){
                try{
                    int id = Integer.parseInt(args[0]);
                    if(id >= 0 && id <= arenaConfig.getArenaAmount() - 1){
                        if(ArenaManager.isRecruiting(id) || Objects.requireNonNull(ArenaManager.getArena(id)).getState().equals(GameState.INICIANDO) && Objects.requireNonNull(ArenaManager.getArena(id)).getPlayers().size() <= arenaConfig.getMaxPlayers(id)){
                            if(!Objects.requireNonNull(ArenaManager.getArena(id)).getState().equals(GameState.JOGANDO)){
                                Objects.requireNonNull(ArenaManager.getArena(id)).addPlayer(player);
                                player.sendMessage("<green>[✔] <gray>Arena encontrada! Enviando você para a arena.");
                            }else{
                                messageUtil.sendErrorMessage(player, ErrorMessages.ARENA_UNAVAILABLE);
                            }
                        }
                    }
                }catch(NumberFormatException e){
                    messageUtil.sendErrorMessage(player, ErrorMessages.ARENA_UNAVAILABLE);
                }
            }else{
                messageUtil.sendErrorMessage(player, ErrorMessages.ALREADY_PLAYING);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> arenas = new ArrayList<>();
        for(Arena arena : ArenaManager.getArenas()){arenas.add(String.valueOf(arena.getID()));}
        if(args.length == 1){return arenas;}
        return null;
    }
}
