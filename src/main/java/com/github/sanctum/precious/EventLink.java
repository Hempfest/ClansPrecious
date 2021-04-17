package com.github.sanctum.precious;

import com.github.sanctum.labyrinth.data.FileManager;
import com.youtube.hempfest.clans.construct.api.ClansAPI;
import com.youtube.hempfest.link.EventCycle;
import java.util.Collection;
import java.util.Collections;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class EventLink extends EventCycle {

	@Override
	public boolean persist() {
		return Bukkit.getPluginManager().isPluginEnabled("PreciousStones");
	}

	@Override
	public EventCycle getInstance() {
		return this;
	}

	@Override
	public String getAddonName() {
		return "PreciousHook";
	}

	@Override
	public String getAddonDescription() {
		return "Disallows non rivaled clans from messing with your fields.";
	}

	@Override
	public Collection<Listener> getAdditions() {
		return Collections.singletonList(new EventListener());
	}

	public static FileManager getSettings() {
		return ClansAPI.getInstance().getFileList().find("Precious", "Configuration/Addon");
	}

	@Override
	public void setAdditions() {
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
}
