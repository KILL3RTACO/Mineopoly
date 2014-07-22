package com.kill3rtaco.mineopoly.game.menus;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.sections.OwnableSection;

public class MineopolySellPropertyMenu extends MineopolyMenu {

	private ArrayList<Integer> prices;
	private int count = 0;
	private MineopolyPlayer with;
	private OwnableSection selling;
	private MineopolyMenu parent;
	
	public MineopolySellPropertyMenu(MineopolyPlayer seller, MineopolyPlayer with, OwnableSection selling, MineopolyMenu parent) {
		super(seller);
		this.with = with;
		this.selling = selling;
		this.parent = parent;
	}

	@Override
	protected Inventory createInventory() {
		prices = new ArrayList<Integer>();
		
		Inventory inv = Mineopoly.plugin.getServer().createInventory(this, 5 * 9, "Select a price");
		
		makeItem(0, inv); //who knows maybe someone wants to be nice
		makeItem(1, inv);
		makeItem(5, inv);
		makeItem(10, inv);
		makeItem(20, inv);
		makeItem(50, inv);
		makeItem(100, inv);
		makeItem(200, inv);
		makeItem(250, inv);
		makeItem(500, inv);
		makeItem(750, inv);
		makeItem(1000, inv);
		makeItem(1250, inv);
		makeItem(1500, inv);
		makeItem(2000, inv);
		makeItem(3000, inv);
		makeItem(4000, inv);
		makeItem(5000, inv);
		makeItem(10000, inv);
		makeItem(12500, inv);
		makeItem(15000, inv);
		makeItem(20000, inv);
		makeItem(25000, inv);
		makeItem(50000, inv);
		
		return inv;
	}
	
	private void makeItem(int price, Inventory inv){
		ItemStack items = super.makeItem(Material.DIAMOND, (price == 0 ? "Free" : price + ""));
		inv.setItem(count++, items);
		prices.add(price);
	}

	@Override
	public void action(MineopolyPlayer player, int cell) {
		if(cell == backIndex){
			player.showMenu(parent);
			return;
		}
		player.showMenu(null);
		player.getPlayer().chat("/" + Mineopoly.getPAlias() + " sell " + selling.getId() + " " + with.getName() + " " + prices.get(cell));
	}
	

}
