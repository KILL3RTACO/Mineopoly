package com.kill3rtaco.mineopoly.game.cards.actions;

import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.CardResult;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCardAction;

public class RepairsAction extends MineopolyCardAction {
	
	public RepairsAction() {
		super("repairs", "ii");
	}
	
	@Override
	public CardResult doAction(MineopolyPlayer player, Object... params) {
		int houses = (Integer) params[0];
		int hotels = (Integer) params[1];
		player.payPot((houses * player.getHouses()) + (hotels * player.getHotels()));
		return CardResult.MONEY_RELATED;
	}
	
}
