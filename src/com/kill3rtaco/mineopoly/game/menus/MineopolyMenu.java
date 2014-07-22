package com.kill3rtaco.mineopoly.game.menus;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.kill3rtaco.mineopoly.game.MineopolyPlayer;

public abstract class MineopolyMenu implements InventoryHolder {

	protected MineopolyPlayer player;
	protected Inventory inventory;
	protected Action action;
	protected String title;
	protected int backIndex;
	
	public MineopolyMenu() {
		this.player = null;
		this.inventory = createInventory();
	}
	
	public MineopolyMenu(String title){
		this.player = null;
		this.action = null;
		this.title = title;
		this.inventory = createInventory();
	}
	
	public MineopolyMenu(Action action){
		this.player = null;
		this.action = action;
		this.title = null;
		this.inventory = createInventory();
	}
	
	public MineopolyMenu(Action action, String title){
		this.player = null;
		this.action = action;
		this.title = title;
		this.inventory = createInventory();
	}
	
	public MineopolyMenu(MineopolyPlayer player){
		this.player = player;
		this.action = null;
		this.title = null;
		this.inventory = createInventory();
	}
	
	public MineopolyMenu(MineopolyPlayer player, String title){
		this.player = player;
		this.action = null;
		this.title = title;
		this.inventory = createInventory();
	}
	
	public MineopolyMenu(MineopolyPlayer player, Action action){
		this.player = player;
		this.action = action;
		this.title = null;
		this.inventory = createInventory();
	}
	
	public MineopolyMenu(MineopolyPlayer player, Action action, String title){
		this.player = player;
		this.action = action;
		this.title = title;
		this.inventory = createInventory();
	}
	
	protected abstract Inventory createInventory();
	
	public abstract void action(MineopolyPlayer player, int cell);
	
	protected void addBackButton(Inventory inv){
		ItemStack backButton = makeItem(Material.BEDROCK, "Back", "Go back to previous menu");
		int back = inv.getSize() - 9;
		inv.setItem(back, backButton);
		backIndex = back;
	}
	
	protected ItemStack makeItem(Material mat, String display, String... lore){
		return makeItem(mat, 0, display, lore);
	}
	
	protected ItemStack makeItem(Material mat, int damage, String display, String... lore){
		ItemStack items = new ItemStack(mat, 1, (short) damage);
		ItemMeta meta = items.getItemMeta();
		meta.setDisplayName(display);
		ArrayList<String> l = new ArrayList<String>();
		for(String s : lore){
			l.add(s);
		}
		meta.setLore(l);
		items.setItemMeta(meta);
		return items;
	}
	
	protected ArrayList<String> makeLore(String lore){
		ArrayList<String> l = new ArrayList<String>();
		l.add(lore);
		return l;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
	
	public boolean clickIsValid(int cell){
		if(cell >= inventory.getContents().length || cell < 0) return false;
		return inventory.getItem(cell) != null;
	}

}
