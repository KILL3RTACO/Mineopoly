package com.kill3rtaco.mineopoly.game.cards.actions;

import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.CardResult;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCardAction;

public class PayPotAction extends MineopolyCardAction {
	
	public PayPotAction() {
		super("paypot", "i");
	}
	
	@Override
	public CardResult doAction(MineopolyPlayer player, Object... params) {
		player.payPot((Integer) params[0]);
		return CardResult.MONEY_RELATED;
	}
	
}
