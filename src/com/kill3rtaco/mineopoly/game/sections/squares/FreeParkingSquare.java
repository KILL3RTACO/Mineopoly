package com.kill3rtaco.mineopoly.game.sections.squares;

import org.bukkit.entity.Player;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.MineopolyPot;
import com.kill3rtaco.mineopoly.game.chat.MineopolyChatChannel;
import com.kill3rtaco.mineopoly.game.sections.SpecialSquare;

public class FreeParkingSquare extends SpecialSquare {
	
	public FreeParkingSquare() {
		super(20, Mineopoly.names.getString(MineopolyConstants.N_FREE_PARKING), '4');
	}
	
	@Override
	public void provokeAction(MineopolyPlayer player) {
		MineopolyChatChannel channel = Mineopoly.plugin.getGame().getChannel();
		MineopolyPot pot = Mineopoly.plugin.getGame().getBoard().getPot();
		int cards = 0;
		if(pot.hasChanceJailCard())
			cards++;
		if(pot.hasCommunityChestJailCard())
			cards++;
		String money = MineopolyConstants.money(pot.getMoney());
		String isCards = (cards == 0 ? "" : (cards == 1 ? "&3 and a Get Out Of Jail Free card" : "&3 and two Get Out Of Jail Free cards"));
		String message = "&b" + player + " &3landed on &4Free Parking and was awarded &2" + money + isCards;
		String message2 = "&3You were awarded &2" + money + isCards + " &3for landing on &4Free Parking";
		channel.sendMessage(message, player);
		player.sendMessage(message2);
		pot.give(player);
		player.setCanEndTurnAutomatically(true);
	}
	
	@Override
	public void getInfo(Player player) {
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&6---[" + getColorfulName() + "&b(&3" + getId() + "&b)&6]---");
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&bLand on this square and you shall recieve all money in the pot");
	}
	
}
