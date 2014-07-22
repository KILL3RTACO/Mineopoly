package com.kill3rtaco.mineopoly.game.cards.actions;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.CardResult;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCardAction;

public class TakeRestAction extends MineopolyCardAction {
	
	public TakeRestAction() {
		super("take-rest", "i");
	}
	
	@Override
	public CardResult doAction(MineopolyPlayer player, Object... params) {
		int amount = (Integer) params[0];
		for(MineopolyPlayer p : Mineopoly.plugin.getGame().getBoard().getPlayers()) {
			if(p == player)
				continue;
			p.takeMoney(amount);
		}
		return CardResult.MONEY_RELATED;
	}
	
}
