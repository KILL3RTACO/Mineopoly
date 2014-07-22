package com.kill3rtaco.mineopoly.game.sections;

import org.bukkit.entity.Player;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCard;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCardSet;

public class CardSection extends CardinalSection implements ActionProvoker {
	
	private int				side;
	private MineopolyCard	lastCard;
	
	public CardSection(int id, String name, char color, SectionType type, int side) {
		super(id, name, color, type);
		this.side = side;
	}
	
	@Override
	public void getInfo(Player player) {
		player.sendMessage("&6---[" + getColorfulName() + "&b(&3" + getId() + "&b)&6]---");
		player.sendMessage("&3Landing on this space will draw a card from the " + getColorfulName() + " &3card pile");
	}
	
	@Override
	public void provokeAction(MineopolyPlayer player) {
		MineopolyCardSet cards;
		if(getType() == SectionType.CHANCE) {
			cards = Mineopoly.plugin.getGame().getBoard().getChanceCards();
		} else {
			cards = Mineopoly.plugin.getGame().getBoard().getCommunityChestCards();
		}
		MineopolyCard card = cards.drawCard(player);
		lastCard = card;
//		if(card.getResult() == CardResult.MONEY_RELATED){
//			player.setCanEndTurnAutomatically(true);
//		}
	}
	
	@Override
	public int getSide() {
		return side;
	}
	
	public MineopolyCard getLastCard() {
		return lastCard;
	}
	
}
