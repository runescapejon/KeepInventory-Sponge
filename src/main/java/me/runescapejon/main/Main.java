package me.runescapejon.main;

import java.io.File;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;

import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.GuiceObjectMapperFactory;

@Plugin(id = "keepinventorysponge", name = "KeepInventorySponge", authors = {
		"runescapejon" }, description = "Let players keep their Inventory if they have permission.", version = "1.1")
public class Main {
	public static Main instance;
	private Main plugin;
	private Logger logger;
	private Configuration configmsg;
	GuiceObjectMapperFactory factory;
	private final File configDirectory;

	public Logger getLogger() {
		return logger;
	}

	@Inject
	public Main(Logger logger, @ConfigDir(sharedRoot = false) File configDir, GuiceObjectMapperFactory factory) {
		this.logger = logger;

		this.configDirectory = configDir;
		this.factory = factory;
		instance = this;
	}

	@Listener
	public void onReload(GameReloadEvent event) {
		loadConfig();
	}

	public GuiceObjectMapperFactory getFactory() {
		return factory;
	}

	public boolean loadConfig() {
		if (!plugin.getConfigDirectory().exists()) {
			plugin.getConfigDirectory().mkdirs();
		}
		try {
			File configFile = new File(getConfigDirectory(), "configuration.conf");
			if (!configFile.exists()) {
				configFile.createNewFile();
				logger.info("Creating Config for BorderProtector");
			}
			ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder()
					.setFile(configFile).build();
			CommentedConfigurationNode config = loader.load(ConfigurationOptions.defaults()
					.setObjectMapperFactory(plugin.getFactory()).setShouldCopyDefaults(true));
			configmsg = config.getValue(TypeToken.of(Configuration.class), new Configuration());
			loader.save(config);
			return true;
		} catch (Exception error) {
			getLogger().error("coudnt make the config", error);

			return false;
		}
	}

	public File getConfigDirectory() {
		return configDirectory;
	}

	@Listener(order = Order.PRE)
	public void onPlayerDeath(DestructEntityEvent.Death e, @First Player p) {
		if (e.getTargetEntity() instanceof Player) {
			if (p.hasPermission("inventory.keep")) {
				e.setKeepInventory(true);
				p.sendMessage(Text.of(TextSerializers.FORMATTING_CODE
						.deserialize(Configuration.Message.replace("%player%", p.getName()))));
			}
		}
	}
}
