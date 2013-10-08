package nz.fox.geek.hostnamecheck;

import org.bukkit.entity.Player;

public class MessageAfterLogin implements Runnable {

	private final Player			 player;

	private final HostNameInfo hostInfo;

	public MessageAfterLogin( final Player player, final HostNameInfo hostinfo ) {
		this.player = player;
		this.hostInfo = hostinfo;

	}

	public HostNameInfo getHostInfo() {
		return this.hostInfo;
	}

	public Player getPlayer() {
		return this.player;
	}

	@Override
	public void run() {
		if ( !this.getPlayer().isOnline() ) {
			return;
		}
		if ( this.getHostInfo() == null ) {
			return;
		}
		if ( this.getHostInfo().getIsBadHostName() ) {
			HostNameCheckListener.tellPlayerBadHostName( this.getPlayer(), this.getHostInfo() );
		}
		if ( this.getHostInfo().getIsIpAddress() ) {
			HostNameCheckListener.tellPlayerBadIP( this.getPlayer(), this.getHostInfo() );
		}
	}
}
