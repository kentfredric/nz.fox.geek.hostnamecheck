package nz.fox.geek.hostnamecheck;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public final class HostNameMatcher {

	private static final List<String> blacklist = Arrays.asList(
			"www.noirland.co.nz", "noirland.co.nz");

	private static final String ipRegexp = "^\\d+[.]\\d+[.]\\d+[.]\\d+:\\d+$";

	private static final List<String> whitelist = Arrays.asList(
			"nz.noirland.co.nz", "au.noirland.co.nz", "us.noirland.co.nz");

	public HostNameMatcher() {
	}

	public final String goodServers() {
		return StringUtils.join(HostNameMatcher.whitelist, ", ");
	}

	private boolean isBadServer(final String hostname) {
		for (final String i : HostNameMatcher.blacklist) {
			if (hostname.startsWith(i + ":")) {
				return true;
			}
		}
		return false;
	}

	private boolean isGoodServer(final String hostname) {
		for (final String i : HostNameMatcher.whitelist) {
			if (hostname.startsWith(i + ":")) {
				return true;
			}
		}
		return false;
	}

	private boolean isIpAddress(final String hostname) {
		return hostname.matches(HostNameMatcher.ipRegexp);
	}

	public final HostNameInfo match(final String hostname) {
		return new HostNameInfo(hostname, this.isBadServer(hostname),
				this.isGoodServer(hostname), this.isIpAddress(hostname));
	}

}
