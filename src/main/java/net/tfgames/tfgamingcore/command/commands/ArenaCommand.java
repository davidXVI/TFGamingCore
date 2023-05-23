package net.tfgames.tfgamingcore.command.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tfgames.tfgamingcore.TFGamingCore;
import net.tfgames.tfgamingcore.arena.ArenaManager;
import net.tfgames.tfgamingcore.command.Command;
import net.tfgames.tfgamingcore.games.GameState;
import net.tfgames.tfgamingcore.util.ErrorMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArenaCommand extends Command {
    public ArenaCommand(TFGamingCore plugin) {
        super(
                plugin,
                "arena",
                new String[]{},
                "",
                "",
                true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        MiniMessage mm = MiniMessage.miniMessage();
        if(serverCore.getPlayerManager().getRank(player).getPermissionLevel() >= 9){
            if(args.length == 1){
                switch (args[0]) {
                    case "shorttime":
                        if (ArenaManager.isPlaying(player)) {
                            if (!Objects.requireNonNull(ArenaManager.getArena(player)).getState().equals(GameState.JOGANDO)) {
                                Objects.requireNonNull(ArenaManager.getArena(player)).getCountdown().setSeconds(5);
                                Component shortTime = mm.deserialize("<gold>[⌚] <gray>Um administrador diminuiu o tempo de inicio da partida!");
                                Objects.requireNonNull(ArenaManager.getArena(player)).sendMessage(shortTime);
                            } else {
                                messageUtil.sendErrorMessage(player, ErrorMessages.NEEDS_PLAYING);
                            }
                        } else {
                            messageUtil.sendErrorMessage(player, ErrorMessages.ONLY_ARENA);
                        }
                        break;
                    case "parar":
                        if (ArenaManager.isPlaying(player)) {
                            if (Objects.requireNonNull(ArenaManager.getArena(player)).getState().equals(GameState.JOGANDO)) {
                                Objects.requireNonNull(ArenaManager.getArena(player)).reset();
                                Component stopGame = mm.deserialize("<gold>[⚡] <gray>Um administrador cancelou a partida que você estava jogando!\nEnviando você para o lobby...");
                                Objects.requireNonNull(ArenaManager.getArena(player)).sendMessage(stopGame);

                            } else {
                                messageUtil.sendErrorMessage(player, ErrorMessages.NEEDS_PLAYING);
                            }
                        } else {
                            messageUtil.sendErrorMessage(player, ErrorMessages.ONLY_ARENA);
                        }
                        break;
                    case "iniciar":
                        if (ArenaManager.isPlaying(player)) {
                            if (!Objects.requireNonNull(ArenaManager.getArena(player)).getState().equals(GameState.JOGANDO)) {
                                Objects.requireNonNull(ArenaManager.getArena(player)).start();
                                Component startGame = mm.deserialize("<gold>[⚡] <gray>Um administrador forçou o inicio da partida!");
                                Objects.requireNonNull(ArenaManager.getArena(player)).sendMessage(startGame);
                            } else {
                                messageUtil.sendErrorMessage(player, ErrorMessages.NEEDS_PLAYING);
                            }
                        } else {
                            messageUtil.sendErrorMessage(player, ErrorMessages.ONLY_ARENA);
                        }
                        break;
                }
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> options = new ArrayList<>();
        if(args.length == 1){
            options.add("iniciar");
            options.add("parar");
            options.add("shorttime");

            return options;
        }
        return null;
    }
}
