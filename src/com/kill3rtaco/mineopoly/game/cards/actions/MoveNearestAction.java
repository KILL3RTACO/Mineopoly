package com.kill3rtaco.mineopoly.game.cards.actions;

import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.CardResult;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCardAction;

public class MoveNearestAction extends MineopolyCardAction {
	
	public MoveNearestAction() {
		super("movenearest", "s");
	}
	
	@Override
	public CardResult doAction(MineopolyPlayer player, Object... params) {
		String param = (String) params[0];
		if(param.equalsIgnoreCase("railroad")) {
			player.moveToNearestRailroad();
		} else if(param.equalsIgnoreCase("utility")) {
			player.moveToNearestUtility();
		}
		return CardResult.MOVE_RELATED;
	}
	
}
