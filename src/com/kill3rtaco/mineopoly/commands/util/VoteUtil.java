package com.kill3rtaco.mineopoly.commands.util;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.MineopolyGame;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.tacoapi.api.ncommands.CommandContext;

public class VoteUtil {
	
	public static void vote(CommandContext context, boolean end) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(MineopolyConstants.L_GAME_NOT_RUNNING, true));
			return;
		}
		MineopolyPlayer mp = game.getBoard().getPlayer(context.getPlayer());
		if(mp == null) {
			context.sendMessageToSender(Mineopoly.lang.message(MineopolyConstants.L_NOT_PLAYING, true));
			return;
		}
		
		if(!game.pollsAreOpen()) {
			if(!end) {
				mp.sendMessage("&cThe polls for ending the game are not open");
				return;
			}
			mp.sendMessage("&aYour vote has been added");
			game.openPolls();
		} else {
			if(game.hasVoted(mp.getName())) {
				mp.sendMessage("&cYou have already voted");
				return;
			}
			mp.sendMessage("&aYour vote has been added");
		}
		game.addVote(end);
	}
	
}
