package com.kill3rtaco.mineopoly.game.cards.actions;

import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.CardResult;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCardAction;

public class GiveAction extends MineopolyCardAction {
	
	public GiveAction() {
		super("give", "i");
	}
	
	@Override
	public CardResult doAction(MineopolyPlayer player, Object... params) {
		player.addMoney((Integer) params[0]);
		return CardResult.MONEY_RELATED;
	}
	
}
