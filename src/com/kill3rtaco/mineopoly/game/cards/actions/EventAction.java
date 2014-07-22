package com.kill3rtaco.mineopoly.game.cards.actions;

import java.util.Arrays;
import java.util.List;

import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.CardResult;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCard;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCardAction;
import com.kill3rtaco.mineopoly.game.cards.actions.events.ah.AHMineopolyErectTowerEvent;
import com.kill3rtaco.mineopoly.game.cards.actions.events.ah.AHMineopolyKillEdgarEvent;

public class EventAction extends MineopolyCardAction {
	
	public static List<String> validEvents() {
		return Arrays.asList("ah_killEdgar", "ah_erectTower");
	}
	
	public EventAction() {
		super("event", "si");
	}
	
	@Override
	public CardResult doAction(MineopolyPlayer player, Object... params) {
		String eventType = (String) params[0];
		int prize = (Integer) params[1];
		MineopolyCard.eventInProgess = true;
		if(eventType.equalsIgnoreCase("ah_killEdgar")) {
			new AHMineopolyKillEdgarEvent(player, prize).start();
		} else if(eventType.equalsIgnoreCase("ah_erectTower")) {
			new AHMineopolyErectTowerEvent(player, prize).start();
		}
		return CardResult.EVENT_RELATED;
	}
	
}