package nz.fox.geek.hostnamecheck;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

public final class HostNameCheckListener implements Listener {

	/**
	 * Tell {@code player} that their host in {@code hostinfo} is bad
	 * 
	 * @param player
	 *          The player to message
	 * @param hostinfo
	 *          The players server info
	 */
	public static void tellPlayerBadHostName( final Player player, final HostNameInfo hostinfo ) {
		player.sendMessage( "Warning: The server address you have connected to ( " + hostinfo.getDomainName()
				+ " ) is not recommended, and maybe slower than necessary" );
		HostNameCheckListener.tellPlayerPickHost( player );
	}

	/**
	 * Tell {@code player} that they are using a direct IP, and that this is bad
	 * 
	 * @param player
	 *          The player to message
	 * @param hostinfo
	 *          The players server info
	 */
	public static void tellPlayerBadIP( final Player player, final HostNameInfo hostinfo ) {
		player.sendMessage( "Warning: You are connecting to noirland by direct IP. Please don't." );
		HostNameCheckListener.tellPlayerPickHost( player );
	}

	/**
	 * Tell the {@code player} the list of recommended hosts
	 * 
	 * @param player
	 *          The player to message
	 */
	public static void tellPlayerPickHost( final Player player ) {
		player.sendMessage( "Please switch to one of: " + HostNameInfo.getWhitelistString() );
	}

	private final Plugin plugin;

	/**
	 * Create an instance of a HostNameCheck Listener, owned by {@code plugin}
	 * 
	 * @param plugin
	 *          The plugin that owns this listener
	 */
	public HostNameCheckListener( final Plugin plugin ) {
		this.plugin = plugin;
	}

	/**
	 * When Minecraft fires a "Join" event, start dispatching warnings to users.
	 * 
	 * @param event
	 *          The Join event.
	 */
	@EventHandler( priority = EventPriority.LOWEST )
	public void onJoin( final PlayerJoinEvent event ) {
		// This, fetches the data obtained during login
		// and dispatches it on join,
		// so that hopefully, its visible to the user by then.
		final Player p = event.getPlayer();

		if ( !p.isOnline() ) {
			// Can't tell player they dun goofed if they're not online
			return;
		}

		if ( !HostNameInfo.canExtractFromPlayer( p, this.plugin ) ) {
			// This returning false --> something else died
			return;
		}

		final HostNameInfo hi = HostNameInfo.extractFromPlayer( p, this.plugin );

		if ( hi.getIsBadHostName() ) {
			HostNameCheckListener.tellPlayerBadHostName( p, hi );
		}
		if ( hi.getIsIpAddress() ) {
			HostNameCheckListener.tellPlayerBadIP( p, hi );

		}
	}

	/**
	 * When a user logs on, record information about that users login details and
	 * attach it to their metadata. Also, tell console about weird things.
	 * 
	 * @param event
	 *          The Login event
	 */
	@EventHandler
	public void onLogin( final PlayerLoginEvent event ) {
		// This simply creates metadata
		// on the player pertaining to the details
		// that transpired during login
		// so that it can be used elsewhere
		final HostNameInfo hi = new HostNameInfo( event.getHostname() );
		if ( !hi.getIsGoodHostName() || hi.getIsIpAddress() ) {
			this.tellConsoleWeirdHost( event.getPlayer(), hi );
		}
		hi.attachToPlayer( event.getPlayer(), this.plugin );

	}

	/**
	 * Report via the console logger that a {@code player} is using a bad host
	 * name {@code hostinfo}
	 * 
	 * @param player
	 *          The player to report information about to the error log
	 * @param hostinfo
	 *          The data about a users host
	 */
	public void tellConsoleWeirdHost( final Player player, final HostNameInfo hostinfo ) {
		this.plugin.getLogger().info( "weird login of player " + player.getName() + " via " + hostinfo.getHostName() );
	}
}
