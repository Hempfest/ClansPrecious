package com.github.sanctum.precious;

import com.github.sanctum.labyrinth.library.Cooldown;
import com.github.sanctum.labyrinth.task.Schedule;
import com.youtube.hempfest.clans.construct.Claim;
import com.youtube.hempfest.clans.construct.Clan;
import com.youtube.hempfest.clans.construct.ClanAssociate;
import com.youtube.hempfest.clans.construct.api.ClansAPI;
import com.youtube.hempfest.clans.util.events.clans.LandPreClaimEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;
import net.sacredlabyrinth.Phaed.PreciousStones.api.IApi;
import net.sacredlabyrinth.Phaed.PreciousStones.field.Field;
import net.sacredlabyrinth.Phaed.PreciousStones.field.FieldFlag;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class EventListener implements Listener {

	IApi precious = PreciousStones.API();

	private static final Map<Clan, Field> FIELD_MAP = new HashMap<>();

	static {
		Schedule.sync(() -> {
			for (Clan c : ClansAPI.getData().clans) {
				Cooldown test = Cooldown.getById(FieldTimerKey.CLAN.invoke(c.getClanId().toString()));
				if (test != null) {
					if (test.isComplete() || (test.getMinutesLeft() == 0 && test.getSecondsLeft() == 0)) {
						Cooldown.remove(test);
						Field field = FIELD_MAP.get(c);
						List<Field> related = field.getChildren();
						for (Field r : related) {
							PreciousStones.getInstance().getForceFieldManager().deleteField(r);
						}
						PreciousStones.getInstance().getForceFieldManager().deleteField(field);
						FIELD_MAP.remove(c);
						c.broadcast(EventLink.getSettings().getConfig().getString("Messages.cooldown-done"));
					}
				}
			}
		}).repeatReal(2, 4);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		ClanAssociate associate = ClansAPI.getInstance().getAssociate(e.getPlayer()).orElse(null);
		if (associate != null && associate.isValid()) {
			if (precious.isPStone(e.getBlock().getLocation())) {
				if (precious.isFieldProtectingArea(FieldFlag.ALL, e.getBlock().getLocation())) {
					if (!precious.getFieldsProtectingArea(FieldFlag.ALL, e.getBlock().getLocation()).get(0).getOwner().equals(e.getPlayer().getName())) {
						if (!precious.getFieldsProtectingArea(FieldFlag.ALL, e.getBlock().getLocation()).get(0).getAllowed().contains(e.getPlayer().getName())) {
							Field field = PreciousStones.getInstance().getForceFieldManager().getEnabledSourceField(e.getBlock().getLocation(), FieldFlag.ALL);
							UUID owner = Clan.action.getUserID(field.getOwner());
							ClanAssociate associate2 = ClansAPI.getInstance().getAssociate(owner).orElse(null);
							if (!e.getPlayer().isSneaking())
								return;
							if (associate2 != null && associate2.isValid()) {
								if (associate.getClan().getEnemies().contains(associate2.getClanID().toString())) {
									Cooldown test = Cooldown.getById(FieldTimerKey.CLAN.invoke(associate.getClan().getClanId().toString()));
									if (test != null && !test.isComplete()) {
										return;
									}
									int count = 0;
									for (String id : associate2.getClan().getMembers()) {
										UUID uuid = UUID.fromString(id);
										OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
										if (target.isOnline()) {
											count++;
										}
									}
									if (count < EventLink.getSettings().getConfig().getInt("Options.field-requirement")) {
										Clan.action.sendMessage(e.getPlayer(), EventLink.getSettings().getConfig().getString("Messages.invalid-request"));
										return;
									}
									field.getFlagsModule().addFlag(FieldFlag.BREAKABLE_ON_DISABLED);
									field.setDisabled(true, e.getPlayer());
									FieldTimer timer = new FieldTimer(associate.getClan());
									timer.save();
									associate.getClan().broadcast(EventLink.getSettings().getConfig().getString("Messages.cooldown-set").replace("{CLAN}", associate2.getClan().getName()));
									FIELD_MAP.put(associate.getClan(), field);
									return;
								}
							}
							Claim.action.sendMessage(e.getPlayer(), Claim.action.notClaimOwner("&2" + precious.getFieldsProtectingArea(FieldFlag.ALL, e.getPlayer().getLocation()).get(0).getOwner()));
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onClaim(LandPreClaimEvent e) {
		ClanAssociate associate = ClansAPI.getInstance().getAssociate(e.getClaimer()).orElse(null);
		if (associate != null && associate.isValid()) {
			if (!precious.canBreak(e.getClaimer(), e.getClaimer().getLocation()) || !precious.canPlace(e.getClaimer(), e.getClaimer().getLocation())) {
				if (precious.isFieldProtectingArea(FieldFlag.ALL, e.getClaimer().getLocation())) {
					if (!precious.getFieldsProtectingArea(FieldFlag.ALL, e.getClaimer().getLocation()).get(0).getOwner().equals(e.getClaimer().getName())) {
						if (!precious.getFieldsProtectingArea(FieldFlag.ALL, e.getClaimer().getLocation()).get(0).getAllowed().contains(e.getClaimer().getName())) {
							e.getUtil().sendMessage(e.getClaimer(), e.getUtil().notClaimOwner("&2" + precious.getFieldsProtectingArea(FieldFlag.ALL, e.getClaimer().getLocation()).get(0).getOwner()));
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}


}
