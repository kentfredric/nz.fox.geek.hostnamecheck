package nz.fox.geek.hostnamecheck;

public class HostNameInfo {

	private String hostName;
	private boolean isBadHostName;
	private boolean isByIP;
	private boolean isGoodHostName;

	public HostNameInfo(final String hostName, final boolean isBadHostName,
			final boolean isGoodHostName, final boolean isByIP) {
		this.setHostName(hostName);
		this.setBadHostName(isBadHostName);
		this.setGoodHostName(isGoodHostName);
		this.setByIP(isByIP);
	}

	public String getHostDomainName() {
		return this.getHostName().substring(0, this.getHostName().indexOf(":"));
	}

	public String getHostName() {
		return hostName;
	}

	public boolean isBadHostName() {
		return isBadHostName;
	}

	public boolean isByIP() {
		return isByIP;
	}

	public boolean isGoodHostName() {
		return isGoodHostName;
	}

	public void setBadHostName(final boolean isBadHostName) {
		this.isBadHostName = isBadHostName;
	}

	public void setByIP(final boolean isByIP) {
		this.isByIP = isByIP;
	}

	public void setGoodHostName(final boolean isGoodHostName) {
		this.isGoodHostName = isGoodHostName;
	}

	public void setHostName(final String hostName) {
		this.hostName = hostName;
	}

}
