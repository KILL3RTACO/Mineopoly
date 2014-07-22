package com.kill3rtaco.mineopoly.game.sections.squares;

import org.bukkit.entity.Player;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.sections.SpecialSquare;

public class GoSquare extends SpecialSquare {
	
	public GoSquare() {
		super(0, Mineopoly.names.getString(MineopolyConstants.N_GO), '6');
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void provokeAction(MineopolyPlayer player) {
		///player.setNeedsGoMoney(true);
		//if this were to be called the player would get an extra 200 on start and an extra 200 if they land on go
		if(player.getTotalRolls() > 0) {
			player.setCanEndTurnAutomatically(true);
		}
	}
	
	@Override
	public void getInfo(Player player) {
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&6---[" + getColorfulName() + "&b(&3" + getId() + "&b)&6]---");
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&bThe inital starting point. Pass this square and you receive &2200");
	}
	
}
