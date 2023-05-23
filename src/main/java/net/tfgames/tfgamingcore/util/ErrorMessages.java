package net.tfgames.tfgamingcore.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum ErrorMessages {

    NO_PERMISSION(Component.text("[!] Permissão Insuficiente!")){
        @Override
        public Component getMessage(){
            return super.getMessage();
        }
    },
    INVALID_ARGUMENTS(Component.text("[!] Argumentos Inválidos!")){
        @Override
        public Component getMessage(){
            return super.getMessage();
        }
    },
    INVALID_USAGE(Component.text("[!] Uso Inválido!")){
        @Override
        public Component getMessage(){
            return super.getMessage();
        }
    },
    RUNTIME_EXCEPTION(Component.text("[!] Houve um problema ao executar esse comando!")){
        @Override
        public Component getMessage(){
            return super.getMessage();
        }
    },
    DEV_COMMAND(Component.text("[!] Esse comando está em desenvolvimento!")){
        @Override
        public Component getMessage(){
            return super.getMessage();
        }
    },
    ONLY_ARENA(Component.text("[!] Você precisa estar em uma arena para executar esse comando!")){
        @Override
        public Component getMessage(){
            return super.getMessage();
        }
    },
    ONLY_LOBBY(Component.text("[!] Você não pode executar esse comando aqui!")){
        @Override
        public Component getMessage(){
            return super.getMessage();
        }
    },
    ARENA_UNAVAILABLE(Component.text("[!] Essa arena não está disponível!")){
        @Override
        public Component getMessage(){
            return super.getMessage();
        }
    },
    ALREADY_PLAYING(Component.text("[!] Você já está em um jogo!")){
        @Override
        public Component getMessage(){
            return super.getMessage();
        }
    },
    NEEDS_PLAYING(Component.text("[!] Você precisa estar jogando para executar esse comando!")){
        @Override
        public Component getMessage(){
            return super.getMessage();
        }
    },
    NO_PLAYING(Component.text("[!] Você não pode executar esse comando durante o jogo!")){
        @Override
        public Component getMessage(){
            return super.getMessage();
        }
    };

    private final Component message;

    ErrorMessages(Component message){
        this.message = message.color(NamedTextColor.RED);
    }

    public Component getMessage(){
        return message;
    }

}