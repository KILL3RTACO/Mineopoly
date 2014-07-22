package com.kill3rtaco.mineopoly.game.cards.actions.events.ah;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.MineopolyGame;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.actions.events.MineopolyGameEvent;

/*
 * Players must be the first to erect a Tower of Pimps. A Tower of Pimps
 * is an obsidian block with four blocks of gold on top.
 */
public class AHMineopolyErectTowerEvent extends MineopolyGameEvent {
	
	public AHMineopolyErectTowerEvent(MineopolyPlayer starter, int prize) {
		super(starter, prize);
	}
	
	@Override
	public void start() {
		final MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning())
			return;
		
		final List<Player> players = new ArrayList<Player>();
		for(MineopolyPlayer p : game.getBoard().getPlayers()) {
			Player player = p.getPlayer();
			PlayerInventory inv = player.getInventory();
			inv.setHeldItemSlot(1);
			inv.setItemInHand(new ItemStack(Material.OBSIDIAN));
			inv.setHeldItemSlot(2);
			inv.setItemInHand(new ItemStack(Material.GOLD_BLOCK, 4));
			inv.setHeldItemSlot(1);
			players.add(player);
		}
		
		game.getChannel().sendMessage("&3First player to erect a &0Tow&eer of pimps &3wins &2" + MineopolyConstants.money(_prize) + "&3!");
		
		final Map<Player, List<Location>> placed = new HashMap<Player, List<Location>>();
		Mineopoly.plugin.registerEvents(new Listener() {
			
			@EventHandler
			public void onBlockPlace(BlockPlaceEvent event) {
				Material mat = event.getBlock().getType();
				MineopolyPlayer p = game.getBoard().getPlayer(event.getPlayer());
				if(p == null)
					return;
				if(mat == Material.OBSIDIAN) {
					placed.put(p.getPlayer(), new ArrayList<Location>());
				} else if(mat == Material.GOLD_BLOCK) { //don't let them place a gold block unless its on the obsidian/gold they placed
					Location last = getLastBlock(event.getPlayer());
					if(last == null || !validPlace(last) || !isAbove(last, event.getBlock().getLocation())) {
						p.sendMessage("&cNope! :p");
						event.setCancelled(true);
						return;
					}
				} else {
					p.sendMessage("&cWhere'd that come from? .-.");
					event.setCancelled(true);
					return;
				}
				placed.get(p.getPlayer()).add(event.getBlock().getLocation());
				if(placed.get(event.getPlayer()).size() == 5) { //all blocks placed
					for(Player player : players) { //clear inventories and reset placed blocks
						player.getInventory().clear();
						List<Location> blocks = placed.get(player);
						if(blocks != null && !blocks.isEmpty()) {
							for(Location l : blocks) {
								event.getBlock().getWorld().getBlockAt(l).setType(Material.AIR);
							}
						}
					}
					game.getChannel().sendMessage("&3" + p.getName() + " &ais the winner!", p);
					p.sendMessage("&aYou win! Here's &2" + MineopolyConstants.money(_prize) + "&a!");
					p.addMoney(_prize);
					HandlerList.unregisterAll(this);
				}
				
			}
			
			private Location getLastBlock(Player player) {
				List<Location> blocks = (placed.get(player) != null ? placed.get(player) : new ArrayList<Location>());
				if(blocks.isEmpty())
					return null;
				return blocks.get(blocks.size() - 1);
			}
			
			private boolean isAbove(Location last, Location next) {
				return next.getBlockX() == last.getBlockX() && next.getBlockZ() == last.getBlockZ() && next.getBlockY() - last.getBlockY() == 1;
			}
			
			private boolean validPlace(Location last) {
				Material m = last.getWorld().getBlockAt(last).getType();
				return m == Material.OBSIDIAN || m == Material.GOLD_BLOCK;
			}
			
		});
		
	}
}
