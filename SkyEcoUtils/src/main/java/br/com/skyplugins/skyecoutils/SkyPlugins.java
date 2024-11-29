package br.com.skyplugins.skyecoutils;

import br.com.skyplugins.skyecoutils.commands.AnnexCommand;
import br.com.skyplugins.skyecoutils.listeners.PlayerMoneyListener;
import br.com.skyplugins.skyecoutils.managers.AnnexManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyPlugins extends JavaPlugin {
    private static Economy economy;
    private AnnexManager annexManager;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe("Vault não encontrado! O plugin será desativado.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        annexManager = new AnnexManager(this);

        getCommand("anexar").setExecutor(new AnnexCommand(this, annexManager));
        getCommand("desanexar").setExecutor(new AnnexCommand(this, annexManager));

        Bukkit.getPluginManager().registerEvents(new PlayerMoneyListener(this, annexManager), this);

        getLogger().info("SkyEcoUtils ativado com sucesso!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SkyEcoUtils desativado.");
    }

    public static Economy getEconomy() {
        return economy;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        return economy != null;
    }
}
