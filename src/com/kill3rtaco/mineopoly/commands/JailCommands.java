package com.kill3rtaco.mineopoly.commands;

import static com.kill3rtaco.mineopoly.MineopolyConstants.*;

import java.util.Random;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.MineopolyGame;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.messages.NotInJailMessage;
import com.kill3rtaco.tacoapi.api.ncommands.Command;
import com.kill3rtaco.tacoapi.api.ncommands.CommandContext;
import com.kill3rtaco.tacoapi.api.ncommands.ParentCommand;

public class JailCommands {
	
	@ParentCommand("mjail")
	@Command(name = "", desc = "MineopolyJail Commands")
	public static void noArgs(CommandContext context) {
		context.invokeCommand(Mineopoly.getJAlias() + " ?");
	}
	
	@ParentCommand("mjail")
	@Command(name = "bail", aliases = {"b"}, desc = "Pay bail")
	public static void bail(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		MineopolyPlayer mp = game.getBoard().getPlayer(context.getPlayer());
		if(mp == null) {
			context.sendMessageToSender(Mineopoly.lang.message(L_NOT_PLAYING, true));
			return;
		}
		if(!mp.hasTurn()) {
			mp.sendMessage(Mineopoly.lang.message(L_INVALID_TURN, false));
			return;
		}
		if(!mp.isJailed()) {
			mp.sendMessage(new NotInJailMessage());
			return;
		}
		int bail = Mineopoly.houseRules.bailPrice();
		if(!mp.hasMoney(bail)) {
			mp.sendMessage(Mineopoly.lang.message(L_INSUFFICIENT_FUNDS, false));
			return;
		}
		mp.payPot(bail);
		game.getChannel().sendMessage("&b" + mp.getName() + " &3paid bail &b(&2" + MineopolyConstants.money(bail) + "&b) and was let out of jail", mp);
		mp.sendMessage("&3You paid bail &b(&2" + MineopolyConstants.money(bail) + "&b) and were let out of jail");
		mp.sendMessage("&3You are out of jail. You can now use &b/" + Mineopoly.getMAlias() + " roll on your next turn");
		mp.setJailed(false, true);
	}
	
	@ParentCommand("mjail")
	@Command(name = "card", aliases = {"c"}, desc = "Use a Get Out of Jail Free Card")
	public static void card(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		MineopolyPlayer mp = game.getBoard().getPlayer(context.getPlayer());
		if(mp == null) {
			context.sendMessageToSender(Mineopoly.lang.message(L_NOT_PLAYING, true));
			return;
		}
		if(!mp.hasTurn()) {
			mp.sendMessage(Mineopoly.lang.message(L_INVALID_TURN, false));
			return;
		}
		if(!mp.isJailed()) {
			mp.sendMessage(Mineopoly.lang.message(L_NOT_JAILED, false));
			return;
		}
		if(!mp.hasChanceJailCard() && !mp.hasCommunityChestJailCard()) {
			mp.sendMessage("&cYou do not have a &6Get out of Jail Free &ccard to use");
			return;
		}
		if(mp.hasChanceJailCard()) {
			mp.takeChanceJailCard();
			Mineopoly.plugin.getGame().getBoard().getPot().addChanceJailCard();
		} else if(mp.hasCommunityChestJailCard()) {
			mp.takeCommunityChestJailCard();
			Mineopoly.plugin.getGame().getBoard().getPot().addCommunityChestJailCard();
		}
		Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3has used a &bGet out of Jail Free &3card", mp);
		mp.sendMessage("&3You are out of jail. You can now use &b/" + Mineopoly.getMAlias() + " roll on your next turn");
		mp.setJailed(false, true);
	}
	
	@ParentCommand("mjail")
	@Command(name = "roll", aliases = {"r"}, desc = "Try to roll doubles")
	public static void roll(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		
		MineopolyPlayer mp = game.getBoard().getPlayer(context.getPlayer());
		if(mp == null) {
			context.sendMessageToSender(Mineopoly.lang.message(L_NOT_PLAYING, true));
			return;
		}
		if(!mp.hasTurn()) {
			mp.sendMessage(Mineopoly.lang.message(L_INVALID_TURN, false));
			return;
		}
		if(!mp.isJailed()) {
			mp.sendMessage(Mineopoly.lang.message(L_NOT_JAILED, false));
			return;
		}
		if(!mp.canRoll()) {
			mp.sendMessage(Mineopoly.lang.message(L_INVALID_TURN, true));
			return;
		}
		int bail = Mineopoly.houseRules.bailPrice();
		Random random = new Random();
		int roll1 = random.nextInt(6) + 1;
		int roll2 = random.nextInt(6) + 1;
		if(roll1 == roll2) {
			game.getChannel().sendMessage("&b" + mp.getName() + " &3rolled doubles and was let out of jail", mp);
			mp.sendMessage("&3You rolled doubles and were let out of jail");
			mp.sendMessage("&3You are out of jail. You can now use &b/" + Mineopoly.getMAlias() + " roll on your next turn");
			mp.setJailed(false, true);
		} else {
			if(mp.getJailRolls() == 2 && mp.getBalance() >= bail) {
				game.getChannel().sendMessage("&b" + mp.getName() + " &3rolled three times without rolling doubles and was let out of jail", mp);
				mp.sendMessage("&3You were let out of jail because you rolled 3 times without rolling doubles");
				mp.sendMessage("&3You are out of jail. You can now use &b/" + Mineopoly.getMAlias() + " roll on your next turn");
				mp.takeMoney(50, true);
				mp.setJailed(false, true);
			} else if(mp.getBalance() < bail) {
				game.getChannel().sendMessage("&b" + mp.getName() + " &3rolled three times, but cannot make bail (&2" + MineopolyConstants.money(bail) + "&3) and was not let out of jail", mp);
				mp.sendMessage("&3You were not let out of jail because you cannot make bail (&2" + MineopolyConstants.money(bail) + "&3)");
				mp.sendMessage("&3You must stay until you roll doubles, use a &bGet out of Jail Free &3card, or have &2" + MineopolyConstants.money(bail));
				mp.setTurn(false, false);
			} else {
				game.getChannel().sendMessage("&b" + mp.getName() + " &3rolled and was not let out of jail", mp);
				mp.sendMessage("&3You were not let of jail");
				mp.setJailRolls(mp.getJailRolls() + 1);
				mp.setTurn(false, false);
			}
		}
	}
	
}
