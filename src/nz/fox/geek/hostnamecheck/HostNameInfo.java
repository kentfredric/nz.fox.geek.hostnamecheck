package nz.fox.geek.hostnamecheck;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

/**
 * @author Kent Fredric
 * 
 */
public class HostNameInfo {

	private static final String			 metakey	 = "connected_server";

	private static final List<String> blacklist = Arrays.asList( "www.noirland.co.nz", "noirland.co.nz" );

	private static final String			 ipRegexp	= "^\\d+[.]\\d+[.]\\d+[.]\\d+:\\d+$";

	private static final List<String> whitelist = Arrays.asList( "nz.noirland.co.nz", "au.noirland.co.nz",
																									"us.noirland.co.nz" );

	public static boolean canExtractFromPlayer( final Player p, final Plugin plugin ) {
		if ( !p.hasMetadata( HostNameInfo.metakey ) ) {
			return false;
		}
		final List<MetadataValue> values = p.getMetadata( HostNameInfo.metakey );
		for ( final MetadataValue value : values ) {
			if ( value.getOwningPlugin().getDescription().getName().equals( plugin.getDescription().getName() ) ) {
				return true;
			}
		}
		return false;
	}

	public static HostNameInfo extractFromPlayer( final Player p, final Plugin plugin ) {
		final List<MetadataValue> values = p.getMetadata( HostNameInfo.metakey );
		for ( final MetadataValue value : values ) {
			if ( value.getOwningPlugin().getDescription().getName().equals( plugin.getDescription().getName() ) ) {
				return (HostNameInfo) value.value();
			}
		}
		return new HostNameInfo( "" );
	}

	public static String getWhitelistString() {
		return StringUtils.join( HostNameInfo.whitelist, ", " );
	}

	private final String hostName;

	public HostNameInfo( final String hostName ) {
		this.hostName = hostName;
	}

	public void attachToPlayer( final Player p, final Plugin plugin ) {
		p.setMetadata( HostNameInfo.metakey, new FixedMetadataValue( plugin, this ) );
	}

	public String getDomainName() {
		return this.getHostName().substring( 0, this.getHostName().indexOf( ":" ) );
	}

	public String getHostName() {
		return this.hostName;
	}

	public boolean getIsBadHostName() {
		for ( final String i : HostNameInfo.blacklist ) {
			if ( this.getDomainName().contentEquals( i ) ) {
				return true;
			}
		}
		return false;
	}

	public boolean getIsGoodHostName() {
		for ( final String i : HostNameInfo.whitelist ) {
			if ( this.getDomainName().contentEquals( i ) ) {
				return true;
			}
		}
		return false;
	}

	public boolean getIsIpAddress() {
		return this.getHostName().matches( HostNameInfo.ipRegexp );
	}

}
