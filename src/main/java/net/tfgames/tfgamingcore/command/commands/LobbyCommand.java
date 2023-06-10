package net.tfgames.tfgamingcore.command.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tfgames.tfgamingcore.TFGamingCore;
import net.tfgames.tfgamingcore.arena.Arena;
import net.tfgames.tfgamingcore.arena.ArenaManager;
import net.tfgames.tfgamingcore.command.Command;
import net.tfgames.tfgamingcore.util.ErrorMessages;
import net.tfgames.tfservercore.TFServerCore;
import net.tfgames.tfservercore.lobby.Lobby;
import net.tfgames.tfuhcgame.game.GameUHC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class LobbyCommand extends Command {
    public LobbyCommand(TFGamingCore plugin) {
        super(
                plugin,
                "lobby",
                new String[]{"sair"},
                "",
                "",
                true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(ArenaManager.isPlaying(player)){
            Arena arena = ArenaManager.getArena(player);
            assert arena != null;
            Objects.requireNonNull(ArenaManager.getArena(player)).removePlayer(player);
            Lobby.addPlayer(player);
        }else{
            messageUtil.sendErrorMessage(player, ErrorMessages.ONLY_ARENA);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
