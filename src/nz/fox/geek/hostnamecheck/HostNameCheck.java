package nz.fox.geek.hostnamecheck;

import org.bukkit.plugin.java.JavaPlugin;

public final class HostNameCheck extends JavaPlugin {

	@Override
	public void onEnable() {
		final HostNameCheckListener l = new HostNameCheckListener(this);
		getServer().getPluginManager().registerEvents(l, this);
	}

}
