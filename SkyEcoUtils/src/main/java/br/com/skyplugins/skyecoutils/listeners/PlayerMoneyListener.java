package br.com.skyplugins.skyecoutils.listeners;

import br.com.skyplugins.skyecoutils.SkyPlugins;
import br.com.skyplugins.skyecoutils.managers.AnnexManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerMoneyListener implements Listener {
    private final SkyPlugins plugin;
    private final AnnexManager annexManager;
    private final Economy economy;

    public PlayerMoneyListener(SkyPlugins plugin, AnnexManager annexManager) {
        this.plugin = plugin;
        this.annexManager = annexManager;
        this.economy = SkyPlugins.getEconomy();
    }

    @EventHandler
    public void onPlayerEarnMoney(PlayerJoinEvent event) {
        // Simulação de evento de ganho de dinheiro
        double earnedAmount = 100.0; // Exemplo

        if (annexManager.isAnnexed(event.getPlayer())) {
            AnnexManager.AnnexData annexData = annexManager.getAnnexData(event.getPlayer());
            double percentage = annexData.getPercentage() / 100;
            double redirectedAmount = earnedAmount * percentage;

            // Verificar limite, se existir
            if (annexData.hasReachedLimit()) {
                annexManager.removeAnnex(event.getPlayer());
                event.getPlayer().sendMessage("§aO limite foi atingido. Você foi desanexado.");
                return;
            }

            EconomyResponse withdrawResponse = economy.withdrawPlayer(event.getPlayer(), earnedAmount);
            if (withdrawResponse.transactionSuccess()) {
                economy.depositPlayer(annexData.getTarget(), redirectedAmount);
                annexData.addRedirectedAmount(redirectedAmount);

                event.getPlayer().sendMessage("§aVocê enviou " + redirectedAmount + " para " + annexData.getTarget().getName());
            }
        }
    }
}
