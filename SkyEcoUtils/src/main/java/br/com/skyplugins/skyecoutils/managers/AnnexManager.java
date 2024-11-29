package br.com.skyplugins.skyecoutils.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnnexManager {
    private final Map<UUID, AnnexData> annexedPlayers = new HashMap<>();

    public void addAnnex(Player source, Player target, double percentage, double limit) {
        annexedPlayers.put(source.getUniqueId(), new AnnexData(target, percentage, limit));
    }

    public boolean removeAnnex(Player source) {
        return annexedPlayers.remove(source.getUniqueId()) != null;
    }

    public boolean isAnnexed(Player source) {
        return annexedPlayers.containsKey(source.getUniqueId());
    }

    public AnnexData getAnnexData(Player source) {
        return annexedPlayers.get(source.getUniqueId());
    }

    public static class AnnexData {
        private final Player target;
        private final double percentage;
        private final double limit;
        private double redirectedAmount = 0;

        public AnnexData(Player target, double percentage, double limit) {
            this.target = target;
            this.percentage = percentage;
            this.limit = limit;
        }

        public Player getTarget() {
            return target;
        }

        public double getPercentage() {
            return percentage;
        }

        public double getLimit() {
            return limit;
        }

        public double getRedirectedAmount() {
            return redirectedAmount;
        }

        public void addRedirectedAmount(double amount) {
            this.redirectedAmount += amount;
        }

        public boolean hasReachedLimit() {
            return limit > 0 && redirectedAmount >= limit;
        }
    }
}
