package nz.fox.geek.hostnamecheck;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public final class HostNameCheckListener implements Listener {

	private static final HostNameMatcher matcher = new HostNameMatcher();

	private static final String metakey = "connected_server";

	private final Plugin plugin;

	public HostNameCheckListener(final Plugin plugin) {
		this.plugin = plugin;
	}

	private HostNameInfo getHostInfo(final Player player) {
		final List<MetadataValue> values = player
				.getMetadata(HostNameCheckListener.metakey);
		for (final MetadataValue value : values) {
			if (value.getOwningPlugin().getDescription().getName()
					.equals(plugin.getDescription().getName())) {
				return (HostNameInfo) value.value();
			}
		}
		return new HostNameInfo("", false, false, false);
	}

	private boolean hasHostInfo(final Player p) {
		return p.hasMetadata(HostNameCheckListener.metakey);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(final PlayerJoinEvent event) {
		// This, fetches the data obtained during login
		// and dispatches it on join,
		// so that hopefully, its visible to the user by then.
		final Player p = event.getPlayer();
		if (!hasHostInfo(p)) {
			return;
		}

		final HostNameInfo hi = getHostInfo(p);

		if (hi.isBadHostName()) {
			p.sendMessage("Warning: The server address you have connected to ( "
					+ hi.getHostDomainName()
					+ " ) is not recommended, and maybe slower than necessary");
			p.sendMessage("Please switch to one of: "
					+ HostNameCheckListener.matcher.goodServers());
		}
		if (hi.isByIP()) {
			p.sendMessage("Warning: You are connecting to noirland by direct IP. Please don't.");
			p.sendMessage("Please switch to one of: "
					+ HostNameCheckListener.matcher.goodServers());
		}
	}

	@EventHandler
	public void onLogin(final PlayerLoginEvent event) {
		// This simply creates metadata
		// on the player pertaining to the details
		// that transpired during login
		// so that it can be used elsewhere
		final HostNameInfo info = HostNameCheckListener.matcher.match(event
				.getHostname());
		if (!info.isGoodHostName() || info.isByIP()) {
			plugin.getLogger().info(
					"weird login of player " + event.getPlayer().getName()
							+ " via " + info.getHostDomainName());
		}

		event.getPlayer().setMetadata(HostNameCheckListener.metakey,
				new FixedMetadataValue(plugin, info));

	}
}
