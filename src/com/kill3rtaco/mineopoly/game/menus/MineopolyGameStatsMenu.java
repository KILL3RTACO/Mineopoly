package com.kill3rtaco.mineopoly.game.menus;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyGame;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.tacoapi.TacoAPI;

public class MineopolyGameStatsMenu extends MineopolyMenu {

	private MineopolyMenu parent;
	
	public MineopolyGameStatsMenu(MineopolyMenu parent){
		super();
		this.parent = parent;
	}
	
	@Override
	protected Inventory createInventory() {
		Inventory inv = Mineopoly.plugin.getServer().createInventory(this, 9 * 3, "Mineopoly Game Stats");
		
		MineopolyGame game = Mineopoly.plugin.getGame();
		int count = 1;
		
		ItemStack timeRunning = makeItem(Material.WATCH, "Time Running", "The game has been running for " + game.getTimeRunningString());
		inv.setItem(count++, timeRunning);
		ItemStack timeLeft = makeItem(Material.WATCH, "Time Left", "There is " + game.getTimeLeftString() + " left in the game");
		if(Mineopoly.houseRules.timeLimit() > 0) inv.setItem(count++, timeLeft);
		ItemStack currentTurn = makeItem(Material.SKULL_ITEM, "Current Turn", "It is currently " + game.getPlayerWithCurrentTurn().getName() + "'s turn");
		inv.setItem(count++, currentTurn);
		String players = TacoAPI.getChatUtils().join(game.getBoard().getPlayerList(), ", ");
		ItemStack allPlayers = makeItem(Material.SKULL_ITEM, 3, "Players", players);
		inv.setItem(count++, allPlayers);//347
		int ownedProps = game.getBoard().getTotalOwnedProperties();
		int ownedRR = game.getBoard().getTotalOwnedRailroads();
		int ownedUtil = game.getBoard().getTotalOwnedUtilities();
		ItemStack totalProps = makeItem(Material.WOOL, 3, "Total Owned Properties", "There are currently " + ownedProps + " owned propert" + (ownedProps == 1 ? "y" : "ies"));
		ItemStack totalRR = makeItem(Material.WOOL, 4, "Total Owned Railroads", "There are currently " + ownedUtil + " owned utilit" + (ownedUtil == 1 ? "y" : "ies"));
		ItemStack totalUtil = makeItem(Material.WOOL, 7, "Total Owned Utilities", "There are currently " + ownedRR + " owned railroad" + (ownedRR == 1 ? "" : "s"));
		inv.setItem(count++, totalProps);
		inv.setItem(count++, totalRR);
		inv.setItem(count++, totalUtil);
		
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
