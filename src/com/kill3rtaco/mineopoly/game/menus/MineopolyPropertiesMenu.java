package com.kill3rtaco.mineopoly.game.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyBoard;
import com.kill3rtaco.mineopoly.game.MineopolyColor;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.sections.OwnableSection;
import com.kill3rtaco.mineopoly.game.sections.Property;
import com.kill3rtaco.mineopoly.game.sections.Railroad;
import com.kill3rtaco.mineopoly.game.sections.Utility;

public class MineopolyPropertiesMenu extends MineopolyMenu {

	private int count = 0;
	private OwnableSection[] sections;
	private OwnableSection trading;
	private MineopolyMenu parent;
	
	//player = the properties to view
	
	public MineopolyPropertiesMenu(MineopolyPlayer player, Action action, String title, MineopolyMenu parent) {
		super(player, action, title);
		this.parent = parent;
	}
	
	public MineopolyPropertiesMenu(MineopolyPlayer player, OwnableSection trading, Action action, String title, MineopolyMenu parent) {
		super(player, action, title);
		this.trading = trading;
		this.parent = parent;
	}

	@Override
	protected Inventory createInventory() {
		sections = new OwnableSection[33];
		MineopolyBoard board = Mineopoly.plugin.getGame().getBoard();
		Inventory inv = Mineopoly.plugin.getServer().createInventory(this, 6 * 9, title);
		
		addItem(board.getProperty(MineopolyColor.PURPLE, 0), inv);
		addItem(board.getProperty(MineopolyColor.PURPLE, 1), inv);
		addItem(board.getRailroad(0), inv);
		addItem(board.getProperty(MineopolyColor.LIGHT_BLUE, 0), inv);
		addItem(board.getProperty(MineopolyColor.LIGHT_BLUE, 1), inv);
		addItem(board.getProperty(MineopolyColor.LIGHT_BLUE, 2), inv);
		count = 9;
		addItem(board.getProperty(MineopolyColor.MAGENTA, 0), inv);
		addItem(board.getUtility(0), inv);
		addItem(board.getProperty(MineopolyColor.MAGENTA, 1), inv);
		addItem(board.getProperty(MineopolyColor.MAGENTA, 2), inv);
		addItem(board.getRailroad(1), inv);
		addItem(board.getProperty(MineopolyColor.ORANGE, 0), inv);
		addItem(board.getProperty(MineopolyColor.ORANGE, 1), inv);
		addItem(board.getProperty(MineopolyColor.ORANGE, 2), inv);
		count = 18;
		addItem(board.getProperty(MineopolyColor.RED, 0), inv);
		addItem(board.getProperty(MineopolyColor.RED, 1), inv);
		addItem(board.getProperty(MineopolyColor.RED, 2), inv);
		addItem(board.getRailroad(2), inv);
		addItem(board.getProperty(MineopolyColor.YELLOW, 0), inv);
		addItem(board.getUtility(1), inv);
		addItem(board.getProperty(MineopolyColor.YELLOW, 1), inv);
		addItem(board.getProperty(MineopolyColor.YELLOW, 2), inv);
		count = 27;
		addItem(board.getProperty(MineopolyColor.GREEN, 0), inv);
		addItem(board.getProperty(MineopolyColor.GREEN, 1), inv);
		addItem(board.getProperty(MineopolyColor.GREEN, 2), inv);
		addItem(board.getRailroad(3), inv);
		addItem(board.getProperty(MineopolyColor.BLUE, 0), inv);
		addItem(board.getProperty(MineopolyColor.BLUE, 1), inv);
		
		addBackButton(inv);
		
		return inv;
	}
	
	private void addItem(Property property, Inventory inv){
		int color = property.getColor().getWoolColor();
		String name = property.getName();
		String rent = "Rent: " + property.getRent();
		String line2 = property.hasHotel() ? "This property has a hotel" : "This property has " + property.getHouses() + " houses";
		ItemStack item =  makeItem(Material.WOOL, (short) color, name, rent, line2);
		if(player.ownsSection(property)){
			boolean add = true;
			if(action == Action.ADD_HOUSE){
				add = property.canAddHouse();
			}else if(action == Action.ADD_HOTEL){
				add = property.canAddHotel();
			}
			if(add){
				inv.setItem(count, item);
				sections[count] = property;
			}
		}
		count++;
	}
	
	private void addItem(Railroad rr, Inventory inv){
		if(action != Action.ADD_HOUSE && action != Action.ADD_HOTEL){
			String name = rr.getName();
			String rent = "Rent: " + rr.getRent();
			ItemStack item = makeItem(Material.WOOL, 15, name, rent);
			if(player.ownsSection(rr)){
				inv.setItem(count, item);
				sections[count] = rr;
			}
			count++;
		}
	}
	
	private void addItem(Utility util, Inventory inv){
		if(action != Action.ADD_HOUSE && action != Action.ADD_HOTEL){
			Material mat;
			if(util.getId() == 12){
				mat = Material.GOLD_BLOCK;
			}else{
				mat = Material.LAPIS_BLOCK;
			}
			String name = util.getName();
			String rent = "Rent: " + util.getRent();
			ItemStack item = makeItem(mat, name, rent);
			if(player.ownsSection(util)){
				inv.setItem(count, item);
				sections[count] = util;
			}
			count++;
		}
	}
	
	@Override
	public void action(MineopolyPlayer player, int cell) {
		if(cell == backIndex){
			player.showMenu(parent);
			return;
		}
		OwnableSection section = sections[cell];
		Player p = player.getPlayer();
		if(action == Action.ADD_HOTEL){
			player.showMenu(null);
			p.chat("/" + Mineopoly.getPAlias() + " add-hotel " + section.getId());
		}else if(action == Action.ADD_HOUSE){
			player.showMenu(null);
			p.chat("/" + Mineopoly.getPAlias() + " add-house " + section.getId());
		}else if(action == Action.SELL_PROPERTY){
			player.showMenu(new MineopolyPlayersMenu(player, section, action, "Select who you want to sell to"));
		}else if(action == Action.TRADE_PROPERTY){
			if(trading == null){ //the property the player is offering was just selected
				player.showMenu(new MineopolyPlayersMenu(player, section, action, "Select who to trade with"));
			}else{
				//with = who 'player' wants to trade with 
				//player = player iniating trade
				player.showMenu(null);
				player.getPlayer().chat("/" + Mineopoly.getPAlias() + " trade " + trading.getId() + " " + this.player.getName() + " " + section.getId());
			}
		}else if(action == Action.VIEW_INFO){
			player.showMenu(null);
			p.chat("/" + Mineopoly.getPAlias() + " info " + section.getId());
		}else if(action != Action.VIEW_PROPERTIES){
			throw new IllegalArgumentException("Action type " + action.name() + " not allowed in MineopolyPropertiesMenu");
		}
	}

}
