package dev.encode42.wgetp;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.EntityType;
import org.bukkit.event.*;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class WGETp extends JavaPlugin implements Listener {
	public static StateFlag ENDERMAN_TELEPORT;

	// Register the teleport event
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	// Inject the flag
	@Override
	public void onLoad() {
		try {
			ENDERMAN_TELEPORT = new StateFlag("enderman-teleport", true);
			WorldGuard.getInstance().getFlagRegistry().register(ENDERMAN_TELEPORT);
		} catch (Exception e) {
			getLogger().warning("The \"enderman-teleport\" flag is taken by another plugin!");
			setEnabled(false);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTeleport(EntityTeleportEvent event) {
		// Exit if not an Enderman
		if (event.getEntityType() != EntityType.ENDERMAN) return;

		// Exit if region doesn't deny
		com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(event.getEntity().getLocation());
		RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
		if (query.getApplicableRegions(location).testState(null, ENDERMAN_TELEPORT)) return;

		// Cancel the teleportation
		event.setCancelled(true);
	}
}