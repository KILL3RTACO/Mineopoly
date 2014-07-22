package com.kill3rtaco.mineopoly.game.tasks;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyGame;

public class MineopolyVotingTask implements Runnable {
	
	@Override
	public void run() {
		long now = System.currentTimeMillis();
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.pollsAreOpen()) return;
		long timeLimit = Mineopoly.options.votingTimeLimit() * 1000;
		long end = game.getVoteStart() + timeLimit;
		if(now >= end){
			game.closePolls();
		}
	}

}
