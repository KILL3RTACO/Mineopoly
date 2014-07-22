package com.kill3rtaco.mineopoly.game.cards.actions;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.CardResult;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCardAction;

public class PayPlayerAction extends MineopolyCardAction {
	
	public PayPlayerAction() {
		super("payplayer", "i");
	}
	
	@Override
	public CardResult doAction(MineopolyPlayer player, Object... params) {
		int param = (Integer) params[0];
		int size = Mineopoly.plugin.getGame().getBoard().getPlayers().size();
		for(MineopolyPlayer p : Mineopoly.plugin.getGame().getBoard().getPlayers()) {
			if(!p.getName().equalsIgnoreCase(player.getName())) {
				p.sendMessage("&3You paid &b" + player.getName() + " &2" + param);
				p.payPlayer(player, param, false, true);
			}
		}
		player.sendMessage("&3You've been paid a total of &2" + (size * param));
		player.sendBalanceMessage();
		return CardResult.MONEY_RELATED;
	}
	
}
