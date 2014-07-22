package com.kill3rtaco.mineopoly.game.cards.actions;

import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.CardResult;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCardAction;

public class TakeAction extends MineopolyCardAction {
	
	public TakeAction() {
		super("take", "i");
	}
	
	@Override
	public CardResult doAction(MineopolyPlayer player, Object... params) {
		player.takeMoney((Integer) params[0]);
		return CardResult.MONEY_RELATED;
	}
	
}
