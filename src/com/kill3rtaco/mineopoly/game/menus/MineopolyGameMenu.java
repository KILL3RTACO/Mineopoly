package com.kill3rtaco.mineopoly.game.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.MineopolySection;
import com.kill3rtaco.mineopoly.game.sections.OwnableSection;

public class MineopolyGameMenu extends MineopolyMenu {
	
	public MineopolyGameMenu(MineopolyPlayer player) {
		super(player);
	}
	
	@Override
	protected Inventory createInventory() {
		Inventory inv = Mineopoly.plugin.getServer().createInventory(this, 6 * 9, "Mineopoly Game Menu");
		
		//action 'buttons'
		//------------------------------
		ItemStack roll = makeItem(Material.WOOL, "Roll", "Roll the dice"); //roll
		if(!player.isJailed() && player.hasTurn() && !player.hasRolled())
			inv.setItem(0, roll);
		ItemStack endTurn = makeItem(Material.WOOL, 15, "End Turn", "End your turn"); //end turn
		if(!player.isJailed() && player.hasTurn() && player.hasRolled())
			inv.setItem(1, endTurn);
		ItemStack buyProperty = makeItem(Material.DIAMOND_BLOCK, "Buy Property", "Buy the deed to " + player.getCurrentSection().getName()); //buy property
		if(player.hasRolled() && player.getCurrentSection() instanceof OwnableSection && !((OwnableSection) player.getCurrentSection()).isOwned())
			inv.setItem(2, buyProperty);
		ItemStack deeds = makeItem(Material.BOOK, "View your deeds", "See which properties you own"); //deeds
		if(player.ownedSections().size() > 0)
			inv.setItem(9, deeds);
		ItemStack monopolies = makeItem(Material.BOOKSHELF, "View your monopolies", "See what monopolies you have"); //monopolies
		if(player.getMonopolies().size() > 0)
			inv.setItem(10, monopolies);
		ItemStack stats = makeItem(Material.PAPER, "View your stats", "See various stats about yourself");
		inv.setItem(11, stats);
		
		//view pot
		ItemStack pot = makeItem(Material.CAULDRON_ITEM, "View the pot", "See what is in the Mineopoly pot");
		if(!Mineopoly.plugin.getGame().getBoard().getPot().isEmpty())
			inv.setItem(5, pot);
		
		//others 'buttons'
		//------------------------------
		ItemStack otherDeeds = makeItem(Material.BOOK, "View others' deeds", "See which properties another player owns"); //deeds
		if(Mineopoly.plugin.getGame().getBoard().getPlayers().size() > 1) {
			for(MineopolyPlayer mp : Mineopoly.plugin.getGame().getBoard().getPlayers()) {
				if(player != mp && mp.ownedSections().size() > 0) {
					inv.setItem(6, otherDeeds);
					break;
				}
			}
		}
		ItemStack otherMonopolies = makeItem(Material.BOOKSHELF, "View others' monopolies", "See what monopolies another player has"); //monopolies
		if(Mineopoly.plugin.getGame().getBoard().getPlayers().size() > 1) {
			for(MineopolyPlayer mp : Mineopoly.plugin.getGame().getBoard().getPlayers()) {
				if(player != mp && mp.getMonopolies().size() > 0) {
					inv.setItem(7, otherMonopolies);
					break;
				}
			}
		}
		ItemStack otherStats = makeItem(Material.PAPER, "View others' stats", "See various stats about another player");
		if(Mineopoly.plugin.getGame().getBoard().getPlayers().size() > 1) {
			inv.setItem(8, otherStats);
		}
		
		//leave game
		ItemStack leaveGame = makeItem(Material.SIGN, "Leave the game");
		inv.setItem(27, leaveGame);
		
		//jail 'buttons'
		//------------------------------
		int bail = Mineopoly.houseRules.bailPrice();
		ItemStack jBail = makeItem(Material.IRON_FENCE, "Jail bail", "Pay bail (" + bail + ")");
		if(player.isJailed() && player.hasTurn() && player.hasMoney(bail))
			inv.setItem(30, jBail);
		ItemStack jCard = makeItem(Material.IRON_FENCE, "Jail card", "Use a Get out of Jail Free card");
		if(player.isJailed() && player.hasTurn() && (player.hasChanceJailCard() || player.hasCommunityChestJailCard()))
			inv.setItem(31, jCard);
		ItemStack jRoll = makeItem(Material.IRON_FENCE, "Jail roll", "Roll the dice while in jail");
		if(player.isJailed() && player.hasTurn())
			inv.setItem(32, jRoll);
		
		//property management 'buttons'
		//------------------------------
		ItemStack addHouse = makeItem(Material.WOOL, 13, "Add house", "Add a house to one of your properties");
		if(!player.hasRolled() && player.ownedPropertiesSize() > 0 && player.canAddHouse())
			inv.setItem(36, addHouse);
		ItemStack addHotel = makeItem(Material.WOOL, 14, "Add hotel", "Add a hotel to one of your properties");
		if(player.ownedPropertiesSize() > 0 && player.canAddHotel())
			inv.setItem(37, addHotel);
		ItemStack sellProp = makeItem(Material.DIAMOND, "Sell", "Sell one of your properties to another player");
		if(player.ownedPropertiesSize() > 0) {
			for(MineopolyPlayer mp : Mineopoly.plugin.getGame().getBoard().getPlayers()) {
				if(!mp.getName().equalsIgnoreCase(player.getName())) {
					if(!mp.isJailed() || Mineopoly.houseRules.tradeWhileJailed()) {
						inv.setItem(45, sellProp);
						break;
					}
				}
			}
		}
		ItemStack tradeProp = makeItem(Material.PAINTING, "Trade", "Trade one of your properties to another player");
		if(player.ownedPropertiesSize() > 0) {
			for(MineopolyPlayer mp : Mineopoly.plugin.getGame().getBoard().getPlayers()) {
				if(!player.getName().equalsIgnoreCase(mp.getName()) && mp.ownedSections().size() > 0) {
					if(!mp.isJailed() || Mineopoly.houseRules.tradeWhileJailed()) {
						inv.setItem(46, tradeProp);
						break;
					}
				}
			}
		}
		
		//traveling railroads
		ItemStack travel = makeItem(Material.WOOL, 7, "Travel", "Travel to the Railroad across from this one");
		MineopolySection curr = player.getCurrentSection();
		if(player.hasTurn() && player.hasRolled() && Mineopoly.houseRules.travelingRailroads() && player.ownsCurrentSection() && player.ownsSection(curr.getId() + 20))
			inv.setItem(35, travel);
		
		//game stats 'button'
		//------------------------------
		ItemStack gameStats = makeItem(Material.ENCHANTMENT_TABLE, "Game Stats", "View the game stats");
		inv.setItem(49, gameStats);
		
		//trading management 'buttons'
		//------------------------------
		ItemStack accept = makeItem(Material.SLIME_BALL, "Accept", "Accept a trading offer from another player");
		if(player.hasRequest())
			inv.setItem(52, accept);
		ItemStack decline = makeItem(Material.MAGMA_CREAM, "Decline", "Decline a trading offer from another player");
		if(player.hasRequest())
			inv.setItem(53, decline);
		ItemStack cancel = makeItem(Material.SNOW_BALL, "Cancel", "Cancel a trading request that you sent");
		if(player.hasSentRequest())
			inv.setItem(44, cancel);
		
		return inv;
	}
	
	@Override
	public void action(MineopolyPlayer player, int cell) {
		Player p = player.getPlayer();
		if(cell == 0) { //roll
			p.closeInventory();
			p.chat("/mineopoly roll");
		} else if(cell == 1) { //end turn
			p.closeInventory();
			p.chat("/mineopoly end-turn");
		} else if(cell == 2) { //buy
			p.closeInventory();
			p.chat("/" + Mineopoly.getPAlias() + " buy");
		} else if(cell == 5) {
			player.showMenu(new MineopolyPotMenu());
		} else if(cell == 6) { //others deeds
			player.showMenu(new MineopolyPlayersMenu(player, Action.VIEW_PROPERTIES, "Select a player"));
		} else if(cell == 7) { //others monopolies
			player.showMenu(new MineopolyPlayersMenu(player, Action.VIEW_MONOPOLIES, "Select a player"));
		} else if(cell == 8) { //others stats
			player.showMenu(new MineopolyPlayersMenu(player, Action.VIEW_INFO, "Select a player"));
		} else if(cell == 9) { //deeds
			player.showMenu(new MineopolyPropertiesMenu(player, Action.VIEW_PROPERTIES, "Your Properties", this));
		} else if(cell == 10) { //monopolies
			player.showMenu(new MineopolyMonopoliesMenu(player, this));
		} else if(cell == 11) { //stats
			player.showMenu(new MineopolyPlayerStatsMenu(player, this));
		} else if(cell == 27) {
			p.chat("/mineopoly quit");
		} else if(cell == 30) { //j bail
			p.closeInventory();
			p.chat("/" + Mineopoly.getJAlias() + " bail");
		} else if(cell == 31) { //j card
			p.closeInventory();
			p.chat("/" + Mineopoly.getJAlias() + " card");
		} else if(cell == 32) { //j roll
			p.closeInventory();
			p.chat("/" + Mineopoly.getJAlias() + " roll");
		} else if(cell == 35) {
			p.chat("/mineopoly travel");
		} else if(cell == 36) { //add house
			player.showMenu(new MineopolyPropertiesMenu(player, Action.ADD_HOUSE, "Select a property", this));
		} else if(cell == 37) { //add hoteling to do
			player.showMenu(new MineopolyPropertiesMenu(player, Action.ADD_HOTEL, "Select a property", this));
		} else if(cell == 44) { //cancel trade
			player.showMenu(new MineopolyPlayersMenu(player, Action.CANCEL_TRADE, "Select a player"));
		} else if(cell == 45) { //sell
			player.showMenu(new MineopolyPropertiesMenu(player, Action.SELL_PROPERTY, "Select a property to sell", this));
		} else if(cell == 46) { //trade
			player.showMenu(new MineopolyPropertiesMenu(player, Action.TRADE_PROPERTY, "Select a property to trade", this));
		} else if(cell == 49) { //game stats
			player.showMenu(new MineopolyGameStatsMenu(this));
		} else if(cell == 52) { //accept trade
			player.showMenu(new MineopolyPlayersMenu(player, Action.ACCEPT_TRADE, "Select a player trading with you"));
		} else if(cell == 53) { //decline trade
			player.showMenu(new MineopolyPlayersMenu(player, Action.DECLINE_TRADE, "Select a player trading with you"));
		}
	}
	
}
