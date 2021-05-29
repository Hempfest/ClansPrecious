package com.github.sanctum.precious;

import com.github.sanctum.clans.construct.api.Clan;
import com.github.sanctum.labyrinth.library.Cooldown;
import com.github.sanctum.labyrinth.library.HUID;

public class FieldTimer extends Cooldown {

	private final HUID id;
	private final long time;

	public FieldTimer(Clan c) {
		this.id = c.getId();
		this.time = abv(60);
	}

	@Override
	public String getId() {
		return FieldTimerKey.CLAN.get() + id.toString();
	}

	@Override
	public long getCooldown() {
		return time;
	}
}
