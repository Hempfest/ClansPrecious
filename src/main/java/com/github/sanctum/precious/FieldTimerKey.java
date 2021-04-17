package com.github.sanctum.precious;

public enum FieldTimerKey {

	CLAN("ClansPro-Field-timer-");

	private final String id;

	FieldTimerKey(String id) {
		this.id = id;
	}

	public String get() {
		return id;
	}

	public String invoke(String clan) {
		return id + clan;
	}

}
