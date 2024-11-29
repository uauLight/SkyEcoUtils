package br.com.skyplugins.skyecoutils.commands;

import br.com.skyplugins.skyecoutils.managers.AnnexManager;
import br.com.skyplugins.skyecoutils.SkyPlugins;
import br.com.skyplugins.skyecoutils.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AnnexCommand implements CommandExecutor {
    private final SkyPlugins plugin;
    private final AnnexManager annexManager;
    private final ConfigUtils configUtils;

    public AnnexCommand(SkyPlugins plugin, AnnexManager annexManager) {
        this.plugin = plugin;
        this.annexManager = annexManager;
        this.configUtils = new ConfigUtils(plugin.getConfig());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("anexar")) {
            if (args.length < 2) {
                configUtils.sendMessage(player, "annex_fail");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                configUtils.sendMessage(player, "annex_fail");
                return true;
            }

            try {
                double percentage = Double.parseDouble(args[1]);
                double maxPercentage = plugin.getConfig().getDouble("settings.max_percentage", 100.0);
                double minPercentage = plugin.getConfig().getDouble("settings.min_percentage", 1.0);

                if (percentage < minPercentage || percentage > maxPercentage) {
                    configUtils.sendMessage(player, "annex_fail");
                    return true;
                }

                double limit = (args.length > 2) ? Double.parseDouble(args[2]) : -1; // Limite opcional (-1 significa sem limite)

                annexManager.addAnnex(player, target, percentage, limit);
                configUtils.sendMessage(player, "annex_success",
                        "{player}", target.getName(),
                        "{percent}", String.valueOf(percentage));
            } catch (NumberFormatException e) {
                configUtils.sendMessage(player, "annex_fail");
            }
        }

        if (command.getName().equalsIgnoreCase("desanexar")) {
            if (annexManager.removeAnnex(player)) {
                configUtils.sendMessage(player, "unannex_success");
            } else {
                configUtils.sendMessage(player, "annex_fail");
            }
        }

        return true;
    }
}
