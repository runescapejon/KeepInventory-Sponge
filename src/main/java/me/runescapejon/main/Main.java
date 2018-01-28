package me.runescapejon.main;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

@Plugin(id = "keepinventorysponge", name = "KeepInventorySponge", authors = {
		"runescapejon" }, description = "Let players keep their Inventory if they have permission - Spongepowered", version = "1.0")
public class Main {
	
	@Listener(order = Order.PRE)
	public void onPlayerDeath(DestructEntityEvent.Death e, @First Player p) {
		if (e.getTargetEntity() instanceof Player && p.hasPermission("inventory.keep")) {
			e.setKeepInventory(true);
			p.sendMessage(Text.of(TextColors.GOLD, TextStyles.BOLD,
					"Your whole inventory was saved from your recent Death."));
		}
	}
}
