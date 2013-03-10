package taco.mineopoly.cmds.mineopoly;

import org.bukkit.entity.Player;

import taco.tacoapi.api.TacoCommand;
import taco.mineopoly.Mineopoly;
import taco.mineopoly.Permissions;

public class MineopolyJoinCommand extends TacoCommand {

	public MineopolyJoinCommand() {
		super("join", new String[]{"j"}, "", "Join the game queue", Permissions.JOIN_GAME);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPlayerCommand(Player player, String[] args) {
		if(Mineopoly.plugin.getQueue().playerIsInQueue(player)){
			Mineopoly.plugin.chat.sendPlayerMessage(player, "&cYou are already queued to join the next Mineopoly game");
		}else if(Mineopoly.plugin.getGame().isRunning() && Mineopoly.plugin.getGame().hasPlayer(player)){
			Mineopoly.plugin.chat.sendPlayerMessage(player, "&cYou are already playing Mineopoly");
		}else{
			Mineopoly.plugin.getQueue().addPlayer(player);
			Mineopoly.plugin.chat.sendPlayerMessage(player, "You've been added to the game queue, please wait until the next game is over or until more players join");
		}
		
	}

	@Override
	public boolean onConsoleCommand(String[] args) {
		return false;
	}

}
