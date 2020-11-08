package xyz.rusin.mvinvimporter;

import java.io.File;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class Bsh extends Plugin {

    @Override
    public void onEnable() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        Commands commands = new Commands(dataFolder, runnable -> getProxy().getScheduler().runAsync(this, runnable));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, commands);
    }
}
