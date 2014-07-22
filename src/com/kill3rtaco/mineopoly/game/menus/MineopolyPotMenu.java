package com.kill3rtaco.mineopoly.game.menus;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.MineopolyPot;

public class MineopolyPotMenu extends MineopolyMenu {
	
	@Override
	protected Inventory createInventory() {
		MineopolyPot pot = Mineopoly.plugin.getGame().getBoard().getPot();
		Inventory inv = Mineopoly.plugin.getServer().createInventory(this, 3 * 9, "Mineopoly Pot");
		
		ItemStack money = makeItem(Material.DIAMOND, "Money", "There is " + MineopolyConstants.money(pot.getMoney(), false) + " in the pot");
		inv.setItem(12, money);
		
		ItemStack chanceJailCard = makeItem(Material.WOOL, 1, "Get out of Jail Free Card", "There is a Get Out of Jail Free Card in the pot");
		if(pot.hasChanceJailCard())
			inv.setItem(13, chanceJailCard);
		
		ItemStack ccJailCard = makeItem(Material.WOOL, 4, "Get out of Jail Free Card", "There is a Get Out of Jail Free Card in the pot");
		if(pot.hasCommunityChestJailCard())
			inv.setItem(14, ccJailCard);
		
		addBackButton(inv);
		
		return inv;
	}
	
	@Override
	public void action(MineopolyPlayer player, int cell) {
		if(cell == backIndex) {
			player.showMenu(new MineopolyGameMenu(player));
		}
	}
	
}
