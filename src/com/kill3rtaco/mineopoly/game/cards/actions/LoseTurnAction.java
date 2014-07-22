package com.kill3rtaco.mineopoly.game.cards.actions;

import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.CardResult;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCardAction;

public class LoseTurnAction extends MineopolyCardAction {
	
	public LoseTurnAction() {
		super("lose-turn", "i");
	}
	
	@Override
	public CardResult doAction(MineopolyPlayer player, Object... params) {
		player.setTurnSkips((Integer) params[0]);
		return CardResult.LOSE_TURN;
	}
	
}
