package com.kill3rtaco.mineopoly.game;

import org.bukkit.entity.Player;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.tacoapi.TacoAPI;

public class MineopolyPot {
	
	private int		money;
	private boolean	chanceJailCard;
	private boolean	ccJailCard;
	
	public MineopolyPot() {
		money = 0;
		chanceJailCard = false;
		ccJailCard = false;
	}
	
	public int getMoney() {
		return money;
	}
	
	public void setMoney(int amount) {
		money = amount;
	}
	
	public void give(MineopolyPlayer player) {
		if(money > 0)
			player.addMoney(money);
		if(hasChanceJailCard())
			player.giveChanceJailCard();
		if(hasCommunityChestJailCard())
			player.giveCommunityChestJailCard();
		money = 0;
		chanceJailCard = false;
		ccJailCard = false;
	}
	
	public boolean hasChanceJailCard() {
		return chanceJailCard;
	}
	
	public boolean hasCommunityChestJailCard() {
		return ccJailCard;
	}
	
	public void addMoney(int amount) {
		money += amount;
	}
	
	public void addChanceJailCard() {
		chanceJailCard = true;
	}
	
	public void addCommunityChestJailCard() {
		ccJailCard = true;
	}
	
	public void getInfo(Player player) {
		int cards = 0;
		if(chanceJailCard)
			cards++;
		if(ccJailCard)
			cards++;
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, TacoAPI.getChatUtils().createHeader("&3Mineopoly Pot"));
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&2Money&7: &2" + getMoney());
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&2Get Out of Jail Free cards&7: &2" + (cards > 0 ? cards : "none"));
//		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&2");
	}
	
	public boolean isEmpty() {
		return money == 0 && !chanceJailCard && !ccJailCard;
	}
	
}
