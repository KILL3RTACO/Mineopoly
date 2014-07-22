package com.kill3rtaco.mineopoly.game.cards.replacemethods;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCard;
import com.kill3rtaco.mineopoly.game.cards.VariableReplaceMethod;

public class IdReplaceMethod implements VariableReplaceMethod {
	
	@Override
	public String replace(String s, MineopolyCard card) {
		for(int i = 39; i > -1; i--) {
			if(s.contains("%" + i))
				s = s.replaceAll("%" + i, Mineopoly.plugin.getGame().getBoard().getSection(i).getColorfulName());
		}
		return s;
	}
	
}
