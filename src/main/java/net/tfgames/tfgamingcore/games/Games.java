package net.tfgames.tfgamingcore.games;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;

public enum Games {

    SKYWARS("Sky Wars", "skwr", NamedTextColor.YELLOW, 150, 50, 0, false, true, false, true, false),
    NEXUSWARS("Nexus Wars", "nxwr", NamedTextColor.LIGHT_PURPLE, 500, 100, 4, true, false, false, true, false),
    ESCADONA("Escadona", "escd", NamedTextColor.GOLD, 350, 80, 8, true, false, false, true, false),
    ESCONDEESCONDE( "Esconde-Esconde", "escn", NamedTextColor.YELLOW, 350, 80, 2, true, false, false, false, false),
    ARENA("Arena", "arna", NamedTextColor.YELLOW, 500, 100, 0, false, false, false, false, false),
    UHC("UHC", "uhc", NamedTextColor.YELLOW, 1000, 200, 0, false, false, false, false, false),
    SANDBOX("Caixa de Areia", "dev", NamedTextColor.YELLOW, 100, 100, 0, false, false, false, false, false);

    private final String display;
    private final String key;
    private final TextColor color;
    private final int winCredits;
    private final int playedCredits;
    private final int teamCount;
    private final boolean isTeamGame;
    private final boolean isKitGame;
    private final boolean isPersistent;
    private final boolean mapRestore;
    private boolean isRestricted;

    Games(String display, String key, TextColor color, int winCredits, int playedCredits, int teamCount, boolean isTeamGame, boolean isKitGame, boolean isPersistent, boolean mapRestore, boolean isRestricted){
        this.display = display;
        this.key = key;
        this.color = color;
        this.winCredits = winCredits;
        this.playedCredits = playedCredits;
        this.teamCount = teamCount;
        this.isTeamGame = isTeamGame;
        this.isKitGame = isKitGame;
        this.isPersistent = isPersistent;
        this.mapRestore = mapRestore;
        this.isRestricted = isRestricted;
    }

    public String getDisplay(){return display;}

    public String getKey(){return key;}

    public TextColor getColor(){return color;}

    public int getWinCredits(){return winCredits;}

    public int getPlayedCredits(){return playedCredits;}

    public int getTeamCount(){return teamCount;}

    public boolean isTeamGame(){return isTeamGame;}

    public boolean isKitGame(){return isKitGame;}

    public boolean isPersistent(){return isPersistent;}

    public boolean needsMapRestore() {return mapRestore;}

    public boolean isRestricted(){ return isRestricted; }

    public void setRestricted(boolean set){isRestricted = set;}

}
