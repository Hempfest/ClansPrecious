package com.github.sanctum.precious;

import com.github.sanctum.clans.construct.api.ClansAPI;
import com.github.sanctum.labyrinth.data.FileManager;
import com.github.sanctum.link.EventCycle;
import org.bukkit.Bukkit;

public class EventLink extends EventCycle {

	@Override
	public boolean persist() {
		return Bukkit.getPluginManager().isPluginEnabled("PreciousStones");
	}

	@Override
	public String getName() {
		return "PreciousHook";
	}

	@Override
	public String getDescription() {
		return "Disallows non rivaled clans from messing with your fields.";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String[] getAuthors() {
		return new String[]{"Hempfest"};
	}

	public static FileManager getSettings() {
		return ClansAPI.getInstance().getFileList().find("Precious", "Addons/ClansPrecious");
	}

	@Override
	public void onLoad() {
		register(new EventListener());
		FileManager precious = getSettings();
		if (!precious.exists()) {
			precious.getConfig().set("Options.field-timer", 60);
			precious.getConfig().set("Options.field-requirement", 1);
			precious.getConfig().set("Messages.cooldown-set", "&4{CLAN}'s Protection Field disabled. On cooldown for &6&l1 &4minute, they never saw it coming...");
			precious.getConfig().set("Messages.cooldown-done", "&2Our stone breaking cooldown is finished.");
			precious.getConfig().set("Messages.invalid-request", "&cThis clan doesn't have enough member's online");
			precious.saveConfig();
		}
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}
}
