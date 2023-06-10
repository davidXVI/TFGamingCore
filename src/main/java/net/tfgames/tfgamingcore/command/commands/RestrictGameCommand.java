package net.tfgames.tfgamingcore.command.commands;

import net.tfgames.tfgamingcore.TFGamingCore;
import net.tfgames.tfgamingcore.command.Command;
import net.tfgames.tfgamingcore.games.Games;
import net.tfgames.tfgamingcore.util.ErrorMessages;
import net.tfgames.tfservercore.tags.Ranks;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RestrictGameCommand extends Command {
    public RestrictGameCommand(TFGamingCore plugin) {
        super(
                plugin,
                "gamerestrict",
                new String[]{"gr"},
                "",
                "",
                true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(serverCore.getPlayerManager().getRank(player).getPermissionLevel() >= 9){
            if(args.length == 2){
                for(Games game : Games.values()) {
                    if (game.name().equals(args[0])) {
                        boolean set = Boolean.parseBoolean(args[1]);
                        game.setRestricted(set);
                    }
                }
            }
        }else{
            messageUtil.sendErrorMessage(player, ErrorMessages.NO_PERMISSION);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> results = new ArrayList<>();
        List<String> options = new ArrayList<>();
        for(Games games : Games.values()){ results.add(String.valueOf(games)); }
        options.add("true");
        options.add("false");
        if(args.length == 1){return results;}
        if(args.length == 2){return options;}
        return new ArrayList<String>();
    }
}