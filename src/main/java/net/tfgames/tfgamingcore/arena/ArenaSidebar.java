package net.tfgames.tfgamingcore.arena;

import me.catcoder.sidebar.ProtocolSidebar;
import me.catcoder.sidebar.Sidebar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.tfgames.tfgamingcore.TFGamingCore;
import net.tfgames.tfservercore.TFServerCore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ArenaSidebar {

    private final TFGamingCore plugin;
    private final TFServerCore serverCore;
    private final ArenaManager arenaManager;
    private final Arena arena;
    private final ArenaConfig arenaConfig;

    public ArenaSidebar(TFGamingCore plugin, Arena arena){
        this.plugin = plugin;
        this.serverCore = plugin.getServerCore();
        this.arena = arena;
        this.arenaManager = plugin.getArenaManager();
        this.arenaConfig = plugin.getArenaConfig();
    }

    public void createSoloScoreboard(Player sidebarPlayer){

        //SKYWARS
        //
        //Mapa: Teste
        //Jogadors: 1/12
        //
        //Aguardando...
        //
        //Server: skwr01
        //
        //play.tfgames.com.br

        Component title = Component.text(arena.getGame(arena.getID()).getDisplay().toUpperCase()).color(arena.getGame(arena.getID()).getColor()).decorate(TextDecoration.BOLD);
        Sidebar<Component> soloSidebar = ProtocolSidebar.newAdventureSidebar(title, plugin);

        soloSidebar.addBlankLine();
        soloSidebar.addUpdatableLine(player -> Component.text("⛏ Mapa: ")
                .append(Component.text(arenaConfig.getArenaWorld(arena.getID())).color(NamedTextColor.GREEN)));
        soloSidebar.addUpdatableLine(player -> Component.text("⛨ Jogadores: ")
                .append(Component.text(arena.getPlayers().size() + "/" + arenaConfig.getMaxPlayers(arena.getID())).color(NamedTextColor.GREEN)));
        soloSidebar.addBlankLine();
        soloSidebar.addUpdatableLine(player -> Component.text("⌚ Iniciando em ").append(Component.text(String.format("%02d:%02d", arena.getCountdown().getSeconds() / 60, arena.getCountdown().getSeconds() % 60)).color(NamedTextColor.GREEN)));
        soloSidebar.addBlankLine();
        soloSidebar.addLine(Component.text(
                ChatColor.BOLD + "⚡ " + ChatColor.RESET + "Server: ").append(Component.text(arena.getGame(arena.getID()).getKey() + arena.getID()).color(NamedTextColor.GREEN)));
        soloSidebar.addBlankLine();
        soloSidebar.addLine(Component.text("play.tfgames.com.br").color(NamedTextColor.YELLOW));

        soloSidebar.updateLinesPeriodically(0, 10);
        soloSidebar.addViewer(sidebarPlayer);

    }

    public void createTeamScoreboard(Player sidebarPlayer){

        //SKYWARS
        //
        //Mapa: Teste
        //Jogadors: 1/12
        //
        //Aguardando...
        //
        //Server: skwr01
        //
        //play.tfgames.com.br

        Component title = Component.text(arena.getGame(arena.getID()).getDisplay().toUpperCase()).color(arena.getGame(arena.getID()).getColor()).decorate(TextDecoration.BOLD);
        Sidebar<Component> teamSidebar = ProtocolSidebar.newAdventureSidebar(title, plugin);

        teamSidebar.addBlankLine();
        teamSidebar.addUpdatableLine(player -> Component.text("⛏ Mapa: ")
                .append(Component.text(arenaConfig.getArenaWorld(arena.getID())).color(NamedTextColor.GREEN)));
        teamSidebar.addUpdatableLine(player -> Component.text("⛨ Jogadores: ")
                .append(Component.text(arena.getPlayers().size() + "/" + arenaConfig.getMaxPlayers(arena.getID())).color(NamedTextColor.GREEN)));
        teamSidebar.addBlankLine();
        teamSidebar.addUpdatableLine(player -> Component.text("⌚ Iniciando em ").append(Component.text(String.format("%02d:%02d", arena.getCountdown().getSeconds() / 60, arena.getCountdown().getSeconds() % 60)).color(NamedTextColor.GREEN)));
        teamSidebar.addBlankLine();
        teamSidebar.addLine(Component.text("⚑ Time: ").append(Component.text(arena.getTeam(sidebarPlayer).getDisplay())));
        teamSidebar.addLine(Component.text(
                ChatColor.BOLD + "⚡ " + ChatColor.RESET + "Server: ").append(Component.text(arena.getGame(arena.getID()).getKey() + arena.getID()).color(NamedTextColor.GREEN)));
        teamSidebar.addBlankLine();
        teamSidebar.addLine(Component.text("play.tfgames.com.br").color(NamedTextColor.YELLOW));

        teamSidebar.updateLinesPeriodically(0, 10);
        teamSidebar.addViewer(sidebarPlayer);

    }

}
