package com.kill3rtaco.mineopoly.game.menus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.sections.OwnableSection;

public class MineopolyPlayersMenu extends MineopolyMenu {

	private ArrayList<MineopolyPlayer> players;
	private OwnableSection trading;
	private MineopolyMenu parent;
	
	public MineopolyPlayersMenu(Action action, MineopolyMenu parent){
		super(action);
		this.parent = parent;
	}
	
	public MineopolyPlayersMenu(MineopolyPlayer player, Action action, String title) {
		super(player, action, title);
	}
	
	public MineopolyPlayersMenu(MineopolyPlayer player, OwnableSection trading, Action action, String title) {
		super(player, action, title);
		this.trading = trading;
	}

	@Override
	protected Inventory createInventory() {
		players = new ArrayList<MineopolyPlayer>();
		ArrayList<MineopolyPlayer> ps = Mineopoly.plugin.getGame().getBoard().getPlayers();
		if(player != null){
			for(MineopolyPlayer p : ps){
				if(!p.getName().equalsIgnoreCase(player.getName())) players.add(p);
			}
		}else{
			players = ps;
		}
		
		ArrayList<MineopolyPlayer> list = new ArrayList<MineopolyPlayer>();
		if(action == Action.VIEW_PROPERTIES){
			for(MineopolyPlayer mp : players){
				if(mp.ownedSections().size() > 0){
					list.add(mp);
				}
			}
			players = list;
		}else if(action == Action.VIEW_MONOPOLIES){
			for(MineopolyPlayer mp : players){
				if(mp.getMonopolies().size() > 0){
					list.add(mp);
				}
			}
			players = list;
		}else if(action == Action.ACCEPT_TRADE || action == Action.DECLINE_TRADE){
			for(MineopolyPlayer mp : players){
				if(player.getReceivedRequest(mp.getName()) != null){
					list.add(mp);
				}
			}
			players = list;
		}else if(action == Action.CANCEL_TRADE){	
			for(MineopolyPlayer mp : players){
				if(player.getSentRequest(mp.getName()) != null){
					list.add(mp);
				}
			}
			players = list;
		}else if(action != Action.VIEW_INFO && action != Action.TRADE_PROPERTY && action != Action.SELL_PROPERTY){
			throw new IllegalArgumentException("Action type " + action.name() + " not allowed in MineopolyPlayersMenu");
		}
		
		if(players.size() > 1) Collections.sort(players, new Comparator<MineopolyPlayer>(){

			@Override
			public int compare(MineopolyPlayer arg0, MineopolyPlayer arg1) {
				return arg0.getName().compareToIgnoreCase(arg1.getName());
			}
			
		});
		int size = players.size() / 9;
		if(players.size() % 9 > 0) size++;
		Inventory inv = Mineopoly.plugin.getServer().createInventory(this, (size + 2) * 9, title);
		
		int index = 0;
		for(MineopolyPlayer mp : players){
			ItemStack head = makeItem(Material.SKULL_ITEM, 3, mp.getName());
			inv.setItem(index++, head);
		}
		
		addBackButton(inv);
		
		return inv;
	}

	@Override
	public void action(MineopolyPlayer player, int cell) {
		if(cell == backIndex){
			player.showMenu(parent);
			return;
		}
		if(cell >= 0 && cell < players.size()){
			MineopolyPlayer p = players.get(cell);
			if(action == Action.VIEW_INFO){
				player.showMenu(new MineopolyPlayerStatsMenu(p, this));
			}else if(action == Action.VIEW_MONOPOLIES){
				player.showMenu(new MineopolyMonopoliesMenu(p, this));
			}else if(action == Action.VIEW_PROPERTIES){
				player.showMenu(new MineopolyPropertiesMenu(p, action, p.getName() + "'s properties", this));
			}else if(action == Action.SELL_PROPERTY){
				player.showMenu(new MineopolySellPropertyMenu(player, p, trading, this));
			}else if(action == Action.TRADE_PROPERTY){
				//player = player initiating trade
				//p = player who 'player' wants to trade with
				player.showMenu(new MineopolyPropertiesMenu(p, trading, action, "Select the property you want", this));
			}else if(action == Action.ACCEPT_TRADE){
				player.showMenu(null);
				player.getPlayer().chat("/" + Mineopoly.getTAlias() + " accept " + p.getName());
			}else if(action == Action.DECLINE_TRADE){
				player.showMenu(null);
				player.getPlayer().chat("/" + Mineopoly.getTAlias() + " decline " + p.getName());
			}else if(action == Action.CANCEL_TRADE){
				player.showMenu(null);
				player.getPlayer().chat("/" + Mineopoly.getTAlias() + " cancel " + p.getName());
			}
		}else{
			Mineopoly.plugin.chat.out("OutOfBounds");
		}
	}

}
