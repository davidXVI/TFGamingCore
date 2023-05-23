package net.tfgames.tfgamingcore.games;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;

public enum Teams {

    //TIMES NORMAIS
    VERMELHO(TextColor.color(0xFF6964) + "Vermelho", TextColor.color(0xFF6964), "[V]"),
    AZUL(TextColor.color(0x5D56FA) + "Azul", TextColor.color(0x5D56FA), "[A]"),
    AMARELO(TextColor.color(0xEED768) + "Amarelo", TextColor.color(0xEED768), "A]"),
    VERDE(TextColor.color(0x00B123) + "Verde", TextColor.color(0x00B123), "[V]"),
    LIMA(TextColor.color(0x6CED11) + "Lima", TextColor.color(0x6CED11), "[L]"),
    ROSA(TextColor.color(0xF329DE) + "Rosa", TextColor.color(0xF329DE), "[R]"),
    CIANO(TextColor.color(0x00B8A5) + "Ciano", TextColor.color(0x00B8A5), "[C]"),
    AQUA(TextColor.color(0x00CCEF) + "Aqua", TextColor.color(0x00CCEF), "[A]"),
    LARANJA(TextColor.color(0xFEA758) + "Laranja", TextColor.color(0xFEA758), "[L]"),
    ROXO(TextColor.color(0x9C1EE9) + "Roxo", TextColor.color(0x9C1EE9), "[R]");

    private final String display;
    private final TextColor color;
    private final String tag;

    Teams(String display, TextColor color, String tag){
        this.display = display;
        this.color = color;
        this.tag = tag;
    }

    public String getDisplay() {
        return display;
    }

    public TextColor getColor() {
        return color;
    }

    public String getTag() {
        return tag;
    }

}
