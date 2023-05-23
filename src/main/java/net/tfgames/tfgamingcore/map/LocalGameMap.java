package net.tfgames.tfgamingcore.map;

import net.tfgames.tfgamingcore.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;

public class LocalGameMap implements GameMap {

    private final File sourceWorldFolder;
    private File activeWorldFolder;

    private World bukkitWorld;

    public LocalGameMap(File worldFolder, String worldName, boolean loadonInit){
        this.sourceWorldFolder = new File(
                worldFolder,
                worldName
        );

        if(loadonInit) load();
    }

    public boolean load(){
        if(isLoaded()) return true;

        this.activeWorldFolder = new File(
                Bukkit.getWorldContainer().getParentFile(),
                sourceWorldFolder.getName()
        );

        try{
            FileUtil.copy(sourceWorldFolder, activeWorldFolder);
        }catch (IOException e){
            Bukkit.getLogger().severe("Falha ao carregar mapa de jogo do arquivo parente " + sourceWorldFolder.getName());
            e.printStackTrace();
            return false;
        }

        this.bukkitWorld = Bukkit.createWorld(
                new WorldCreator(activeWorldFolder.getName())
        );

        if(bukkitWorld != null) this.bukkitWorld.setAutoSave(false);
        return isLoaded();
    }

    public void unload(){
        if(bukkitWorld != null) Bukkit.unloadWorld(bukkitWorld, false);
        if(activeWorldFolder != null) FileUtil.delete(activeWorldFolder);

        bukkitWorld = null;
        activeWorldFolder = null;
    }


    public void delete(String name) {
        World world = Bukkit.getWorld(name);
        if (world == null) {
            return;
        }
        File activeWorldFolder = world.getWorldFolder();
        if (activeWorldFolder.exists()){
            Bukkit.unloadWorld(world, false);
            FileUtil.delete(activeWorldFolder);
            Bukkit.getConsoleSender().sendMessage("Successfully deleted the world and the world file");
        }
    }

    public boolean restoreFromSource(){
        unload();
        return load();
    }

    @Override
    public boolean isLoaded() {
        return getWorld() != null;
    }

    @Override
    public World getWorld() {
        return bukkitWorld;
    }

}