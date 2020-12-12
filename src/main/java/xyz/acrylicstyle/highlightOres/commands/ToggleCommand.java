package xyz.acrylicstyle.highlightOres.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.acrylicstyle.highlightOres.HighlightOres;
import xyz.acrylicstyle.tomeito_api.subcommand.PlayerSubCommandExecutor;
import xyz.acrylicstyle.tomeito_api.subcommand.SubCommand;

import java.util.Collections;

@SubCommand(name = "toggle", usage = "/highlight toggle", description = "Toggles ore visibility.")
public class ToggleCommand extends PlayerSubCommandExecutor {
    @Override
    public void onCommand(Player player, String[] args) {
        if (HighlightOres.highlight.contains(player.getUniqueId())) {
            HighlightOres.highlight.remove(player.getUniqueId());
            HighlightOres.clearEntities(player.getUniqueId());
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            player.sendMessage(ChatColor.GREEN + "You are no longer highlighting the ores.");
        } else {
            HighlightOres.highlight.add(player.getUniqueId());
            int size = HighlightOres.highlight.size() * 25;
            HighlightOres.writePool.setMaximumPoolSize(size);
            HighlightOres.writePool.setCorePoolSize(size);
            player.addPotionEffects(Collections.singletonList(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 1, false, false)));
            player.sendMessage(ChatColor.GREEN + "Now you are highlighting the ores within 25 blocks range.");
        }
    }
}
