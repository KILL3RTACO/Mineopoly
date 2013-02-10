package taco.mineopoly.sections.squares;

import org.bukkit.entity.Player;

import taco.mineopoly.Mineopoly;
import taco.mineopoly.MineopolyPlayer;
import taco.mineopoly.sections.SpecialSquare;

public class GoToJailSquare extends SpecialSquare {
	
	public GoToJailSquare() {
		super(30, "Go to Jail", '1');
	}

	@Override
	public void provokeAction(MineopolyPlayer player) {
		Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + player.getName() + " &3was &1jailed&3!", player);
		player.sendMessage("&3You were &1jailed&3!");
		player.setJailed(true, true);
	}

	@Override
	public void getInfo(Player player) {
		Mineopoly.chat.sendPlayerMessageNoHeader(player, "&6---[" + getColorfulName() + "&6]---");
		Mineopoly.chat.sendPlayerMessageNoHeader(player, "&bLand on this space and you will be sent to jail immediatley.");
		Mineopoly.chat.sendPlayerMessageNoHeader(player, "");
		Mineopoly.chat.sendPlayerMessageNoHeader(player, "&bYou can use a &1Get out of Jail Free &bcard on your next turn if you were jailed.");
	}

}
