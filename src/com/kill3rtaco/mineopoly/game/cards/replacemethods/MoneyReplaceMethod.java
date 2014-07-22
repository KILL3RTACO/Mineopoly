package com.kill3rtaco.mineopoly.game.cards.replacemethods;

import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCard;
import com.kill3rtaco.mineopoly.game.cards.VariableReplaceMethod;

public class MoneyReplaceMethod implements VariableReplaceMethod {
	
	@Override
	public String replace(String s, MineopolyCard card) {
		String amount = s.replaceAll("%money([0-9]+)", "$1");
		return MineopolyConstants.money(Integer.parseInt(amount));
	}
	
}
