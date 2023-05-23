package net.tfgames.tfgamingcore.command.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tfgames.tfgamingcore.TFGamingCore;
import net.tfgames.tfgamingcore.arena.ArenaManager;
import net.tfgames.tfgamingcore.command.Command;
import net.tfgames.tfgamingcore.util.ErrorMessages;
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
            if(!Objects.requireNonNull(ArenaManager.getArena(player)).getSpectators().contains(player)){
                MiniMessage mm = MiniMessage.miniMessage();
                Component message = mm.deserialize("<gray>[<red>-<gray>] ")
                        .append(Component.text(player.getName()).color(serverCore.getPlayerManager().getRank(player).getTextColor())
                                .append(Component.text(" saiu!").color(NamedTextColor.YELLOW)));
                Objects.requireNonNull(ArenaManager.getArena(player)).sendMessage(message);
            }
            Objects.requireNonNull(ArenaManager.getArena(player)).removePlayer(player);
        }else{
            messageUtil.sendErrorMessage(player, ErrorMessages.ONLY_ARENA);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
