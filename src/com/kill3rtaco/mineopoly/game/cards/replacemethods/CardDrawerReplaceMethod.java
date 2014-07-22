package com.kill3rtaco.mineopoly.game.cards.replacemethods;

import com.kill3rtaco.mineopoly.game.cards.MineopolyCard;
import com.kill3rtaco.mineopoly.game.cards.VariableReplaceMethod;

public class CardDrawerReplaceMethod implements VariableReplaceMethod {
	
	@Override
	public String replace(String s, MineopolyCard card) {
		return s.replaceAll("%cardDrawer", card.getDrawer().getName());
	}
	
}
