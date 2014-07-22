package com.kill3rtaco.mineopoly.game.menus;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.MineopolySection;

public class MineopolyPlayerStatsMenu extends MineopolyMenu {

	private MineopolyMenu parent;
	
	public MineopolyPlayerStatsMenu(MineopolyPlayer player, MineopolyMenu parent){
		super(player);
		this.parent = parent;
	}
	
	@Override
	protected Inventory createInventory() {
		Inventory inv = Mineopoly.plugin.getServer().createInventory(this, 3 * 9, player.getName() + "'s stats");
		
		ItemStack bal = makeItem(Material.DIAMOND, "Balance", "This player's balance is " + player.getBalance());
		inv.setItem(1, bal);
		int total = player.getTotalRolls();
		ItemStack rolls = makeItem(Material.WOOL, "Rolls", "This player has rolled the dice " + total + " time" + (total == 1 ? "" : "s"));
		inv.setItem(2, rolls);
		MineopolySection section = player.getCurrentSection();
		String spaceName = section.getName();
		String inJail = null;
		if(section.getId() == 10){ //Jail/Just Visiting
			if(player.isJailed()) inJail = "(Jail)";
			else inJail = "(Just Visiting)";
		}
		ItemStack space = makeItem(Material.WOOL, 3, "Current Space", "This player is currently on " + spaceName + (inJail != null ? inJail : ""));
		inv.setItem(3, space);
		int properties = player.ownedSections().size();
		ItemStack props = makeItem(Material.BOOK, "Properties", "This player owns " + properties + " propert" + (properties == 1 ? "y" : "ies"));
		inv.setItem(4, props);
		int monopolies = player.getMonopolies().size();
		ItemStack monops = makeItem(Material.BOOKSHELF, "Monopolies", "This player has " + monopolies + " monopol" + (monopolies == 1 ? "y" : "ies"));
		inv.setItem(5, monops);
		int cards = (player.hasChanceJailCard() ? 1 : 0) + (player.hasCommunityChestJailCard() ? 1 : 0);
		ItemStack goojf = makeItem(Material.ITEM_FRAME, "Get Out of Jail Free Cards", "This player has " + cards + " card" + (cards == 1 ? "" : "s"));
		inv.setItem(6, goojf);
		
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
