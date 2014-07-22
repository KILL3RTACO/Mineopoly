package com.kill3rtaco.mineopoly.game.cards.actions;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.CardResult;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCardAction;

public class PayAllAction extends MineopolyCardAction {
	
	public PayAllAction() {
		super("payall", "i");
	}
	
	@Override
	public CardResult doAction(MineopolyPlayer player, Object... params) {
		int param = (Integer) params[0];
		for(MineopolyPlayer p : Mineopoly.plugin.getGame().getBoard().getPlayers()) {
			if(!p.getName().equalsIgnoreCase(player.getName())) {
				p.sendMessage("&3You've been given &2" + param + " &3by &b" + player.getName());
				player.payPlayer(p, param, false, true);
			}
			player.sendBalanceMessage();
		}
		return CardResult.MONEY_RELATED;
	}
	
}
