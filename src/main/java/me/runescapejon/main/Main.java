package me.runescapejon.main;

import java.io.File;
import java.io.IOException;

import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.google.inject.Inject;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "keepinventorysponge", name = "KeepInventorySponge", authors = {
		"runescapejon" }, description = "Let players keep their Inventory if they have permission - Spongepowered", version = "1.1")
public class Main {
	private ConfigurationNode Config;
	private File DConfig;
	private ConfigurationLoader<CommentedConfigurationNode> ConfigNode;
	public static Main instance;

	public ConfigurationLoader<CommentedConfigurationNode> getConfig() {
		return this.ConfigNode;
	}

	@Inject
	public Main(@DefaultConfig(sharedRoot = false) File DConfig,
			@DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> Configs) {
		this.DConfig = DConfig;
		this.ConfigNode = Configs;
		instance = this;
	}

	@Listener
	public void onPreInit(GamePreInitializationEvent event) throws IOException {
		try {
			if (!this.DConfig.exists()) {
				this.DConfig.createNewFile();

				this.Config = getConfig().load();
				this.Config.getNode("Languages", "KeepInventoryMessage")
						.setValue("&6&lYour whole inventory was saved from your recent Death.");
				getConfig().save(this.Config);
			}
			this.Config = getConfig().load();
			Language.SetLang(TextSerializers.FORMATTING_CODE
					.deserialize(this.Config.getNode("Languages", "KeepInventoryMessage").getString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Listener(order = Order.PRE)
	public void onPlayerDeath(DestructEntityEvent.Death e, @First Player p) {
		if (e.getTargetEntity() instanceof Player && p.hasPermission("inventory.keep")) {
			e.setKeepInventory(true);
			p.sendMessage(
					Text.of(TextColors.GOLD, p.getName(), " ", Text.builder().append(Language.getLang()).build()));
		}
	}
}
