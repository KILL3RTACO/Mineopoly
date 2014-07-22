package com.kill3rtaco.mineopoly.game.cards.actions;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.CardResult;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCardAction;

//:D so nice!
public class GiveAllAction extends MineopolyCardAction {
	
	public GiveAllAction() {
		super("give-all", "i");
	}
	
	@Override
	public CardResult doAction(MineopolyPlayer player, Object... params) {
		int amount = (Integer) params[0];
		for(MineopolyPlayer p : Mineopoly.plugin.getGame().getBoard().getPlayers()) {
			p.addMoney(amount);
		}
		return CardResult.MONEY_RELATED;
	}
	
}
