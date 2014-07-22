package com.kill3rtaco.mineopoly.game.menus;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyColor;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;

public class MineopolyMonopoliesMenu extends MineopolyMenu {

	private MineopolyMenu parent;
	
	public MineopolyMonopoliesMenu(MineopolyPlayer player, MineopolyMenu parent){
		super(player);
		this.parent = parent;
	}
	
	@Override
	protected Inventory createInventory() {
		Inventory inv = Mineopoly.plugin.getServer().createInventory(this, 9 * 3, player.getName() + "'s monopolies");
		
		int count = 0;
		for(MineopolyColor c : MineopolyColor.values()){
			ItemStack color = makeItem(Material.WOOL, c.getWoolColor(), c.name());
			if(player.hasMonopoly(c)){
				inv.setItem(count, color);
			}
			count++;
		}
		
		addBackButton(inv);
		
		return inv;
	}

	@Override
	public void action(MineopolyPlayer player, int cell) {
		if(cell == backIndex){
			player.showMenu(parent);
		}
	}

}
