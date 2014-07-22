package com.kill3rtaco.mineopoly.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.InventoryHolder;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyGame;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.MineopolySection;
import com.kill3rtaco.mineopoly.game.menus.MineopolyMenu;
import com.kill3rtaco.mineopoly.game.sections.OwnableSection;
import com.kill3rtaco.mineopoly.game.tasks.managers.PlayerSessionManager;
import com.kill3rtaco.mineopoly.messages.OutsideSectionMessage;

public class MineopolyListener implements Listener {
	
	public static boolean	cancelBlockBreaks	= true;
	public static boolean	lockToSpace			= true;
	public static boolean	pvp					= false;
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(PlayerSessionManager.isInList(player.getName())) {
			PlayerSessionManager.remove(player.getName());
			if(Mineopoly.plugin.getGame().isRunning()) {
				MineopolyPlayer mp = Mineopoly.plugin.getGame().getBoard().getPlayer(player.getName());
				if(mp != null)
					mp.setPlayer(player);
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(Mineopoly.plugin.getGame().isRunning()) {
			if(Mineopoly.plugin.getGame().hasPlayer(player)) {
				MineopolyPlayer mp = Mineopoly.plugin.getGame().getBoard().getPlayer(player);
				PlayerSessionManager.addToList(mp.getName());
			} else {
				if(Mineopoly.plugin.getQueue().playerIsInQueue(player)) {
					Mineopoly.plugin.getQueue().removePlayer(player);
				}
			}
		} else {
			if(Mineopoly.plugin.getQueue().playerIsInQueue(player)) {
				Mineopoly.plugin.getQueue().removePlayer(player);
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(!lockToSpace || event.getFrom().getBlock() == event.getTo().getBlock())
			return;
		Player player = event.getPlayer();
//		lastLocs.put(player, event.getFrom());
		if(Mineopoly.plugin.getGame().isRunning()) {
			if(Mineopoly.plugin.getGame().hasPlayer(player)) {
				MineopolyPlayer mp = Mineopoly.plugin.getGame().getBoard().getPlayer(player);
				Location loc = null;
				double[] diffs = mp.getCurrentSection().outside(mp);
				if(diffs == null) {
					return;
				}
//				mp.sendMessage("xDiff: " + diffs[0]);
//				mp.sendMessage("zDiff: " + diffs[1]);
				double delta = .3;
				boolean close = (Math.abs(diffs[0]) < delta || Math.abs(diffs[1]) < delta);
//				mp.sendMessage(close + "");
				if(mp.isJailed()) {
					loc = Mineopoly.plugin.getGame().getBoard().getJailLocation();
					String s = "jailed";
					if(Mineopoly.config.ahEdition()) {
						s = "in Edgar's Hole";
					}
					mp.sendMessage("&cYou are &1" + s + "&c, do not try to escape. You can use a &6Get out of Jail Free &ccard if you need to." +
							" (&6/" + Mineopoly.getJAlias() + " card&c)");
				} else if(mp.getCurrentSection().getId() == 10) { //not jailed, but on just visiting
					if(!close)
						loc = Mineopoly.plugin.getGame().getBoard().getJustVisitingLocation();
					else
						loc = event.getFrom();
					mp.sendMessage(new OutsideSectionMessage());
				} else {
					if(!close)
						loc = mp.getCurrentSection().getLocation();
					else
						loc = event.getFrom();
					mp.sendMessage(new OutsideSectionMessage());
				}
				player.teleport(loc);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if(pvp)
			return;
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(Mineopoly.plugin.getGame().isRunning()) {
				if(Mineopoly.plugin.getGame().hasPlayer(player)) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onHungerChange(FoodLevelChangeEvent event) {
		if(Mineopoly.plugin.getGame().hasPlayer((Player) event.getEntity())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(game.isRunning() && game.hasPlayer(event.getPlayer())) {
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Block block = event.getClickedBlock();
				if(block.getType() == Material.WORKBENCH) {
					Location origin = game.getBoard().getOrigin();
					int boardLength = Mineopoly.boardConfig.size();
					if(block.getX() <= origin.getBlockX() + boardLength - 1
							&& block.getZ() <= origin.getBlockZ() + boardLength - 1
							&& block.getY() <= origin.getY() + 25) {
						event.setCancelled(true);
						event.getPlayer().chat("/mineopoly menu");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		InventoryHolder holder = event.getInventory().getHolder();
		if(holder instanceof MineopolyMenu) {
			MineopolyMenu menu = (MineopolyMenu) holder;
			if(menu.clickIsValid(event.getSlot())) {
				MineopolyPlayer mp = Mineopoly.plugin.getGame().getBoard().getPlayer((Player) event.getWhoClicked());
				if(mp != null) {
					menu.action(mp, event.getSlot());
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) { //cancel if player breaks ownership piece
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning())
			return;
		MineopolyPlayer player = game.getBoard().getPlayer(event.getPlayer());
		if(player == null)
			return;
		MineopolySection section = player.getCurrentSection();
		if(section instanceof OwnableSection) {
			Location opLocation = ((OwnableSection) section).getOwnershipPieceLocation();
			if(event.getBlock().getLocation().equals(opLocation)) {
				event.setCancelled(true);
				return;
			}
		}
		event.setCancelled(cancelBlockBreaks);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRespawn(PlayerRespawnEvent event) { //for pvp events?
		if(!pvp)
			return;
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			return;
		}
		MineopolyPlayer player = game.getBoard().getPlayer(event.getPlayer());
		if(player == null) {
			return;
		}
		event.setRespawnLocation(player.getCurrentSection().getLocation());
	}
}
