package com.kill3rtaco.mineopoly.commands;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.commands.util.VoteUtil;
import com.kill3rtaco.tacoapi.api.ncommands.Command;
import com.kill3rtaco.tacoapi.api.ncommands.CommandContext;
import com.kill3rtaco.tacoapi.api.ncommands.ParentCommand;

public class VoteCommands {
	
	@ParentCommand("mvote")
	@Command(name = "", desc = "MineopolyVote Commands")
	public static void noArgs(CommandContext context) {
		context.invokeCommand(Mineopoly.getVAlias() + " ?");
	}
	
	@ParentCommand("mvote")
	@Command(name = "yes", desc = "Vote to continue the game")
	public static void yes(CommandContext context) {
		VoteUtil.vote(context, true);
	}
	
	@ParentCommand("mvote")
	@Command(name = "no", desc = "Vote to end the game")
	public static void no(CommandContext context) {
		VoteUtil.vote(context, false);
	}
	
}
