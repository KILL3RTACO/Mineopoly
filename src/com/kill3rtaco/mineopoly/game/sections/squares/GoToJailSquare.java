package com.kill3rtaco.mineopoly.game.sections.squares;

import org.bukkit.entity.Player;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.sections.SpecialSquare;

public class GoToJailSquare extends SpecialSquare {
	
	public GoToJailSquare() {
		super(30, Mineopoly.names.getString(MineopolyConstants.N_GO_TO_JAIL), '1');
	}
	
	@Override
	public void provokeAction(MineopolyPlayer player) {
		Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + player.getName() + " &3was &1jailed&3!", player);
		player.sendMessage("&3You were &1jailed&3!");
		player.setJailed(true, true);
	}
	
	@Override
	public void getInfo(Player player) {
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&6---[" + getColorfulName() + "&b(&3" + getId() + "&b)&6]---");
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&bLand on this space and you will be sent to jail immediatley.");
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "");
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&bYou can use a &1Get out of Jail Free &bcard on your next turn if you were jailed.");
	}
	
}
