package taco.mineopoly.cmds.mineopoly;

import org.bukkit.entity.Player;

import taco.mineopoly.Mineopoly;
import taco.mineopoly.Permissions;
import taco.tacoapi.api.TacoCommand;

public class MineopolyStartCommand extends TacoCommand {

	public MineopolyStartCommand() {
		super("start", new String[]{}, "", "Start the game", Permissions.START_GAME);
	}

	@Override
	public void onPlayerCommand(Player player, String[] args) {
		if(!Mineopoly.plugin.getGame().isRunning()){
			int queued = Mineopoly.plugin.getQueue().getSize();
			int min = Mineopoly.config.getMinPlayers();
			if(queued >= min){
				Mineopoly.plugin.restartGame();
			}else{
				Mineopoly.chat.sendPlayerMessage(player, "&e" + queued + " &cplayers in queue with minimum of &e" + min + " &crequired");
				Mineopoly.chat.sendPlayerMessage(player, "&eView players in the queue by using &a/mineopoly queue");
			}
		}else{
			Mineopoly.chat.sendPlayerMessage(player, "&cThere is already a game in progress");
		}
	}

	@Override
	public boolean onConsoleCommand(String[] args) {
		if(!Mineopoly.plugin.getGame().isRunning()){
			int queued = Mineopoly.plugin.getQueue().getSize();
			int min = Mineopoly.config.getMinPlayers();
			if(queued >= min){
				Mineopoly.plugin.restartGame();
			}else{
				Mineopoly.chat.out(queued + " players in queue with minimum of " + min + " required");
			}
		}else{
			Mineopoly.chat.out("There is already a game in progress");
		}
		return true;
	}

}