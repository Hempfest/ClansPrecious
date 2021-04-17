package com.github.sanctum.precious;

import com.youtube.hempfest.clans.construct.api.ClansAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class PreciousHook extends JavaPlugin {

	@Override
	public void onEnable() {
		ClansAPI.getInstance().importAddon(EventLink.class);
	}

	@Override
	public void onDisable() {
	}
}
