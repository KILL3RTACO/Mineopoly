package com.kill3rtaco.mineopoly.game.cards.actions.events.ah;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.MineopolyGame;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.actions.events.MineopolyGameEvent;
import com.kill3rtaco.mineopoly.listener.MineopolyListener;

/*
 * Players must be the first to kill a cow (Edgar) that appears on there space
 */
public class AHMineopolyKillEdgarEvent extends MineopolyGameEvent {
	
	public AHMineopolyKillEdgarEvent(MineopolyPlayer starter, int prize) {
		super(starter, prize);
	}
	
	@Override
	public void start() {
		final MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning())
			return;
		
		MineopolyListener.lockToSpace = false;
		final Map<Entity, MineopolyPlayer> edgars = new HashMap<Entity, MineopolyPlayer>();
		for(MineopolyPlayer p : game.getBoard().getPlayers()) {
			World w = game.getBoard().getOrigin().getWorld();
			edgars.put(w.spawnEntity(p.getCurrentSection().getLocation(), EntityType.COW), p);
		}
		
		final List<Player> players = new ArrayList<Player>();
		for(MineopolyPlayer p : edgars.values()) {
			Player player = p.getPlayer();
			players.add(player);
		}
		
		game.getChannel().sendMessage("&3First player to kill &bEdgar &3wins &2" + MineopolyConstants.money(_prize) + "&3!");
		
		Mineopoly.plugin.registerEvents(new Listener() {
			
			@EventHandler
			public void onEdgarDeath(EntityDeathEvent event) {
				Entity e = event.getEntity();
				if(e.getType() != EntityType.COW)
					return;
				if(!edgars.containsKey(e))
					return;
				event.getDrops().clear();
				MineopolyPlayer winner = edgars.get(e);
				for(Entity entity : edgars.keySet()) {
					entity.remove();
				}
				
				game.getChannel().sendMessage("&b" + winner.getName() + " &ais the winner!", winner);
				winner.sendMessage("&aYou win! Here's &2" + MineopolyConstants.money(_prize) + "&a!");
				winner.addMoney(_prize);
				
				HandlerList.unregisterAll(this);
				MineopolyListener.lockToSpace = true;
			}
			
		});
	}
	
}
