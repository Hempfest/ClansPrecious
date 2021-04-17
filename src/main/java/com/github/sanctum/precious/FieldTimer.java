package com.github.sanctum.precious;

import com.github.sanctum.labyrinth.library.Cooldown;
import com.github.sanctum.labyrinth.library.HUID;
import com.youtube.hempfest.clans.construct.Clan;

public class FieldTimer extends Cooldown {

	private final HUID id;
	private final long time;

	public FieldTimer(Clan c) {
		this.id = c.getClanId();
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
