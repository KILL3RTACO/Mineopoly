package com.kill3rtaco.mineopoly.commands;

import static com.kill3rtaco.mineopoly.MineopolyConstants.*;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.MineopolyGame;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.MineopolySection;
import com.kill3rtaco.mineopoly.game.sections.OwnableSection;
import com.kill3rtaco.mineopoly.game.sections.Property;
import com.kill3rtaco.mineopoly.game.sections.Railroad;
import com.kill3rtaco.mineopoly.game.sections.SectionType;
import com.kill3rtaco.mineopoly.game.sections.Utility;
import com.kill3rtaco.mineopoly.game.trading.TradeRequest;
import com.kill3rtaco.mineopoly.game.trading.TradeType;
import com.kill3rtaco.mineopoly.messages.CannotPerformActionMessage;
import com.kill3rtaco.mineopoly.messages.GameNotInProgressMessage;
import com.kill3rtaco.mineopoly.messages.InsufficientFundsMessage;
import com.kill3rtaco.mineopoly.messages.InvalidTurnMessage;
import com.kill3rtaco.mineopoly.messages.MustRollFirstMessage;
import com.kill3rtaco.mineopoly.messages.NotPlayingGameMessage;
import com.kill3rtaco.mineopoly.messages.SectionAlreadyOwnedMessage;
import com.kill3rtaco.mineopoly.messages.SectionNotFoundMessage;
import com.kill3rtaco.mineopoly.messages.SectionNotOwnableMessage;
import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.tacoapi.api.ncommands.Command;
import com.kill3rtaco.tacoapi.api.ncommands.CommandContext;
import com.kill3rtaco.tacoapi.api.ncommands.ParentCommand;

public class PropertyCommands {
	
	@ParentCommand("mproperty")
	@Command(name = "", desc = "MineopolyProperty Commands")
	public static void noArgs(CommandContext context) {
		context.invokeCommand(Mineopoly.getPAlias() + " ?");
	}
	
	@ParentCommand("mproperty")
	@Command(name = "add-hotel", args = "[property]", desc = "Add a hotel to a property")
	public static void addHotel(CommandContext context) {
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
			mp.sendMessage(new InvalidTurnMessage());
			return;
		}
		if(mp.hasRolled()) {
			mp.sendMessage("&cYou can only add hotels before you roll");
			return;
		}
		if(mp.isJailed() && !Mineopoly.houseRules.improveWhileJailed()) {
			mp.sendMessage("&cYou cannot do that while in jail");
			return;
		}
		Property p = null;
		if(context.eq(0)) {
			MineopolySection section = mp.getCurrentSection();
			if(section.getType() != SectionType.PROPERTY) {
				mp.sendMessage(new CannotPerformActionMessage() + "add a hotel to that");
			} else {
				p = (Property) section;
			}
		} else {
			if(TacoAPI.getChatUtils().isNum(context.getString(0))) {
				int id = context.getInteger(0);
				MineopolySection section = Mineopoly.plugin.getGame().getBoard().getSection(id);
				if(section.getType() != SectionType.PROPERTY) {
					mp.sendMessage(new CannotPerformActionMessage() + "add a hotel to that");
					return;
				} else {
					p = (Property) section;
				}
			} else {
				MineopolySection section = Mineopoly.plugin.getGame().getBoard().getSection(context.getString(0));
				if(section == null) {
					mp.sendMessage(new SectionNotFoundMessage());
					return;
				}
				if(section.getType() != SectionType.PROPERTY) {
					mp.sendMessage(new CannotPerformActionMessage() + "add a hotel to that");
					return;
				} else {
					p = (Property) section;
				}
			}
		}
		
		if(Mineopoly.houseRules.improvementRequiresLocation() && p.getId() != mp.getCurrentSection().getId()) {
			mp.sendMessage("&cYou can only improve the property you are on");
			return;
		}
		
		if(p.getOwner() != mp) {
			mp.sendMessage("&cYou do not own " + p.getColorfulName());
			return;
		}
		if(!mp.hasMoney(p.getHotelPrice())) {
			int amount = p.getHotelPrice() - mp.getBalance();
			mp.sendMessage("&cYou need an additional &2" + MineopolyConstants.money(amount) + " &cbefore you can do that");
			return;
		}
		if(!p.canAddHotel()) {
			if(Mineopoly.houseRules.improvementRequiresMonopoly() && mp.hasMonopoly(p.getColor())) {
				mp.sendMessage("&cYou do not have a monopoly for the color " + p.getColor().getName());
				return;
			}
			ArrayList<Property> props = mp.getPropertiesLessHousing(p.getColor(), 4);
			if(props.size() > 0) {
				String message = "";
				for(int i = 0; i < props.size(); i++) {
					if(i == props.size() - 1) {
						message += (message.isEmpty() ? "" : "and ") + props.get(i).getColorfulName();
					} else {
						message += props.get(i).getColorfulName() + " &7, &3";
					}
				}
				message += " &3 " + (props.size() == 1 ? "does" : "do") + " not have four houses";
				mp.sendMessage(message);
			}
		} else {
			Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3 added a hotel to " + p.getColorfulName(), mp);
			mp.sendMessage("&3You added a &chotel &3to " + p.getColorfulName());
			p.addHotel();
		}
	}
	
	@ParentCommand("mproperty")
	@Command(name = "add-house", args = "[property]", desc = "Add a house to a property")
	public static void addHouse(CommandContext context) {
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
			mp.sendMessage(new InvalidTurnMessage());
			return;
		}
		if(mp.hasRolled()) {
			mp.sendMessage("&cYou can only add houses before you roll");
			return;
		}
		if(mp.isJailed() && !Mineopoly.houseRules.improveWhileJailed()) {
			mp.sendMessage("&cYou cannot do that while in jail");
			return;
		}
		Property p = null;
		if(context.eq(0)) {
			MineopolySection section = mp.getCurrentSection();
			if(section.getType() != SectionType.PROPERTY) {
				mp.sendMessage(new CannotPerformActionMessage() + "add a house to that");
			} else {
				p = (Property) section;
			}
		} else {
			if(TacoAPI.getChatUtils().isNum(context.getString(0))) {
				int id = context.getInteger(0);
				MineopolySection section = Mineopoly.plugin.getGame().getBoard().getSection(id);
				if(section.getType() != SectionType.PROPERTY) {
					mp.sendMessage(new CannotPerformActionMessage() + "add a house to that");
					return;
				} else {
					p = (Property) section;
				}
			} else {
				MineopolySection section = Mineopoly.plugin.getGame().getBoard().getSection(context.getString(0));
				if(section == null) {
					mp.sendMessage(new SectionNotFoundMessage());
					return;
				}
				if(section.getType() != SectionType.PROPERTY) {
					mp.sendMessage(new CannotPerformActionMessage() + "add a house to that");
					return;
				} else {
					p = (Property) section;
				}
			}
		}
		
		if(Mineopoly.houseRules.improvementRequiresLocation() && p.getId() != mp.getCurrentSection().getId()) {
			mp.sendMessage("&cYou can only improve the property you are on");
			return;
		}
		
		if(p.getOwner().getName().equalsIgnoreCase(mp.getName())) {
			if(mp.hasMoney(p.getHousePrice())) {
				if(!p.canAddHouse()) {
					if(Mineopoly.houseRules.improvementRequiresMonopoly()) {
						if(!mp.hasMonopoly(p.getColor())) {
							mp.sendMessage("&cYou do not have a Monopoly for the color " + p.getColor().getName());
							return;
						}
						if(p.getHouses() >= 4) {
							mp.sendMessage(p.getColorfulName() + " &3already has &b4 &3houses");
							return;
						}
						ArrayList<Property> props = mp.getPropertiesNeedingHousing(p);
						if(props.size() > 0) {
							String message = "";
							for(int i = 0; i < props.size(); i++) {
								if(i == props.size() - 1) {
									message += (message.isEmpty() ? "" : "and ") + props.get(i).getColorfulName();
								} else {
									message += props.get(i).getColorfulName() + " &7, &3";
								}
							}
							message += " &3 " + (props.size() == 1 ? "needs" : "need") + "more houses";
							mp.sendMessage(message);
							return;
						}
					}
				} else {
					Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3 added a house to " + p.getColorfulName(), mp);
					mp.sendMessage("&3You added a &2house &3to " + p.getColorfulName());
					p.addHouse();
				}
			} else {
				int amount = p.getHousePrice() - mp.getBalance();
				mp.sendMessage("&cYou need an additional &2" + MineopolyConstants.money(amount) + " &cbefore you can do that");
			}
		} else {
			context.sendMessageToSender("&cYou do not own that");
		}
	}
	
	@ParentCommand("mproperty")
	@Command(name = "buy", desc = "Buy the space you're on")
	public static void buy(CommandContext context) {
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
		MineopolySection section = mp.getCurrentSection();
		if(!mp.hasTurn()) {
			mp.sendMessage(Mineopoly.lang.message(L_INVALID_TURN, false));
			return;
		}
		if(!mp.hasRolled()) {
			mp.sendMessage(new MustRollFirstMessage("buying property"));
			return;
		}
		if(Mineopoly.houseRules.purchaseAfterGoPasses() > 0) {
			int neededPasses = Mineopoly.houseRules.purchaseAfterGoPasses();
			if(mp.getGoPasses() < neededPasses) {
				mp.sendMessage("&cYou need to pass &6Go &e" + (neededPasses - mp.getGoPasses()) + " &c more times to buy property");
				return;
			}
		}
		if(!(section instanceof OwnableSection)) {
			mp.sendMessage(new SectionNotOwnableMessage(section, "buy"));
		}
		OwnableSection oSection = (OwnableSection) section;
		if(oSection.isOwned()) {
			mp.sendMessage(new SectionAlreadyOwnedMessage(section));
			return;
		}
		int neededPasses = Mineopoly.houseRules.purchaseAfterGoPasses();
		if(neededPasses > 0 && mp.getGoPasses() < neededPasses) {
			mp.sendMessage("&cYou need to pass &6Go &e" + (neededPasses - mp.getGoPasses()) + " &cmore times before you can buy property");
			return;
		}
		if(!mp.canBuy(oSection)) {
			mp.sendMessage(new InsufficientFundsMessage());
			return;
		}
		oSection.setOwner(mp);
		Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3bought " + section.getColorfulName() + "&3 for &2" + MineopolyConstants.money(oSection.getPrice()), mp);
		mp.sendMessage("&3You bought " + section.getColorfulName() + "&3 for &2" + MineopolyConstants.money(oSection.getPrice()));
		mp.takeMoney(oSection.getPrice());
		if(oSection instanceof Property) {
			Property prop = (Property) oSection;
			if(mp.hasMonopoly(prop.getColor())) {
				Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3now has a monopoly for the color " + prop.getColor().getName(), mp);
				mp.sendMessage("&3You now have a monopoly for the color " + prop.getColor().getName());
				if(Mineopoly.houseRules.improvementRequiresMonopoly())
					mp.sendMessage("&3You can now add houses by typing &b/" + Mineopoly.getPAlias() + " add-house [property]");
			}
		} else if(oSection instanceof Railroad && mp.ownedRailRoads() == 4) {
			Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3now owns all Railroad spaces", mp);
			mp.sendMessage("&3You now now own all Railroad spaces");
		} else if(oSection instanceof Utility && mp.ownedUtilities() == 2) {
			Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3now owns both Utility spaces", mp);
			mp.sendMessage("&3You now own both Utility spaces");
		}
		mp.endTurnAutomatically();
	}
	
	@ParentCommand("mproperty")
	@Command(name = "info", args = "[property]", desc = "View information about a space")
	public static void info(CommandContext context) {
		Player player = context.getPlayer();
		if(Mineopoly.plugin.getGame().isRunning()) {
			if(player.hasPermission(MineopolyConstants.P_VIEW_GAME_STATS) || Mineopoly.plugin.getGame().hasPlayer(player)) {
				if(context.eq(0)) {
					if(Mineopoly.plugin.getGame().hasPlayer(player)) {
						MineopolyPlayer mp = Mineopoly.plugin.getGame().getBoard().getPlayer(player);
						mp.getCurrentSection().getInfo(player);
					} else {
						Mineopoly.plugin.chat.sendPlayerMessage(player, new NotPlayingGameMessage());
					}
				} else {
					MineopolySection section;
					if(TacoAPI.getChatUtils().isNum(context.getString(0))) {
						int id = context.getInteger(0);
						section = Mineopoly.plugin.getGame().getBoard().getSection(id);
						if(section == null) {
							Mineopoly.plugin.chat.sendPlayerMessage(player, "&cID cannot be lower than &60");
						}
					} else {
						section = Mineopoly.plugin.getGame().getBoard().getSection(context.getString(0));
						if(section == null) {
							Mineopoly.plugin.chat.sendPlayerMessage(player, "&cSpace on board with name of &6" + context.getString(0) + " &cnot found");
						}
					}
					section.getInfo(player);
				}
			} else if(!player.hasPermission(MineopolyConstants.P_VIEW_GAME_STATS)) {
				Mineopoly.plugin.chat.sendInvalidPermissionsMessage(player);
			} else {
				Mineopoly.plugin.chat.sendPlayerMessage(player, new NotPlayingGameMessage());
			}
		} else {
			Mineopoly.plugin.chat.sendPlayerMessage(player, new GameNotInProgressMessage());
		}
	}
	
	@ParentCommand("mproperty")
	@Command(name = "remove-hotel", aliases = {"rm-hotel"}, args = "[property]", desc = "Remove the hotel from a property")
	public static void removeHotel(CommandContext context) {
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
		if(mp.hasTurn()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_INVALID_TURN, false));
			return;
		}
		if(context.eq(0)) { //add house to current property
			if(mp.getCurrentSection() instanceof Property) {
				Property prop = (Property) mp.getCurrentSection();
				if(prop.hasHotel()) {
					prop.removeHotel();
					Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3removed the hotel from " + prop.getColorfulName(), mp);
					mp.sendMessage("&3You removed the hotel from " + prop.getColorfulName());
				} else {
					mp.sendMessage("&cThat property doesn't have a hotel");
				}
			} else {
				mp.sendMessage("&cThat is not a valid space to remove a hotel");
			}
		} else {
			MineopolySection section;
			if(TacoAPI.getChatUtils().isNum(context.getString(0)))
				section = Mineopoly.plugin.getGame().getBoard().getSection(context.getInteger(0));
			else
				section = Mineopoly.plugin.getGame().getBoard().getSection(context.getString(0));
			if(section == null) {
				mp.sendMessage(new SectionNotFoundMessage());
			} else {
				if(section instanceof Property) {
					Property prop = (Property) section;
					if(prop.hasHotel()) {
						prop.removeHotel();
						Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3removed the hotel from " + prop.getColorfulName(), mp);
						mp.sendMessage("&3You removed the hotel from " + prop.getColorfulName());
					} else {
						mp.sendMessage("&cThat property doesn't have a hotel");
					}
				} else {
					mp.sendMessage("&cThat is not a valid space to remove a hotel");
				}
			}
		}
	}
	
	@ParentCommand("mproperty")
	@Command(name = "remove-house", aliases = {"rm-house"}, args = "[property]", desc = "Remove a house from a property")
	public static void removeHouse(CommandContext context) {
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
		if(mp.hasTurn()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_INVALID_TURN, false));
			return;
		}
		if(context.eq(0)) { //remove house to current property
			if(mp.getCurrentSection() instanceof Property) {
				Property prop = (Property) mp.getCurrentSection();
				if(prop.getHouses() > 0) {
					prop.removeHouse();
					Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3removed a house from " + prop.getColorfulName(), mp);
					mp.sendMessage("&3You removed a house from " + prop.getColorfulName());
				} else {
					mp.sendMessage("&cThat property has no houses");
				}
			} else {
				mp.sendMessage("&cThat is not a valid space to remove houses");
			}
		} else { //get specified property and remove house from it
			MineopolySection section;
			if(TacoAPI.getChatUtils().isNum(context.getString(0)))
				section = Mineopoly.plugin.getGame().getBoard().getSection(context.getInteger(0));
			else
				section = Mineopoly.plugin.getGame().getBoard().getSection(context.getString(0));
			if(section == null) {
				mp.sendMessage(new SectionNotFoundMessage());
			} else {
				if(section instanceof Property) {
					Property prop = (Property) section;
					if(prop.getHouses() > 0) {
						prop.removeHouse();
						Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3removed a house from " + prop.getColorfulName(), mp);
						mp.sendMessage("&3You removed a house from " + prop.getColorfulName());
					} else {
						mp.sendMessage("&cThat property has no houses");
					}
				} else {
					mp.sendMessage("&cThat is not a valid space to remove houses");
				}
			}
		}
	}
	
	@ParentCommand("mproperty")
	@Command(name = "sell", args = "[property] <player> <price>", desc = "Sell a property to another player")
	public static void sell(CommandContext context) {
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
		if(!Mineopoly.houseRules.tradeAnytime() && !mp.hasTurn()) {
			mp.sendMessage("&cYou can only iniate a trade on your turn");
			return;
		}
		if(mp.isJailed() && !Mineopoly.houseRules.tradeWhileJailed()) {
			mp.sendMessage("&cYou cannot trade while in jail");
			return;
		}
		OwnableSection owned;
		MineopolyPlayer trader;
		String[] newArgs = context.getArgs();
		if(context.lt(2)) {
			context.sendMessageToSender("&cAt least 2 arguments required");
			return;
		} else if(context.gt(2)) { //determine what property the player wants to trade
			MineopolySection section;
			if(TacoAPI.getChatUtils().isNum(context.getString(0))) {
				int id = context.getInteger(0);
				section = Mineopoly.plugin.getGame().getBoard().getSection(id);
			} else {
				section = Mineopoly.plugin.getGame().getBoard().getSection(context.getString(0));
			}
			
			if(section == null) {
				mp.sendMessage("&e" + context.getString(0).replace("_", " ") + " &cis not a space on the board");
				return;
			}
			if(section.getType() != SectionType.PROPERTY && section.getType() != SectionType.RAILROAD && section.getType() != SectionType.UTILITY) {
				mp.sendMessage(section.getColorfulName() + " &cis not a property, railroad, or utility");
				return;
			}
			owned = (OwnableSection) section;
			if(owned.getOwner() != null && !owned.getOwner().getName().equalsIgnoreCase(mp.getName())) {
				mp.sendMessage("&cYou do not own " + section.getColorfulName());
				return;
			}
			newArgs = context.getArgs(1);
		} else {
			MineopolySection section = mp.getCurrentSection();
			if(section.getType() != SectionType.PROPERTY && section.getType() != SectionType.RAILROAD && section.getType() != SectionType.UTILITY) {
				mp.sendMessage(section.getColorfulName() + " &cis not a property, railroad, or utility");
				return;
			}
			owned = (OwnableSection) section;
			if(owned.getOwner() != null && !owned.getOwner().getName().equalsIgnoreCase(mp.getName())) {
				mp.sendMessage("&cYou do not own " + section.getColorfulName());
				return;
			}
		}
		trader = Mineopoly.plugin.getGame().getBoard().getPlayer(newArgs[0]);
		if(trader == null) {
			mp.sendMessage("&e" + newArgs[0] + " &cis not playing Mineopoly");
			return;
		}
		if(trader.isJailed() && !Mineopoly.houseRules.tradeWhileJailed()) {
			mp.sendMessage("&e" + trader.getName() + " &ccannot trade because they are in jail");
			return;
		}
		if(TacoAPI.getChatUtils().isNum(newArgs[1])) {
			int price = Integer.parseInt(newArgs[1]);
			TradeRequest request = new TradeRequest(mp, trader, TradeType.SELL);
			request.setOfferedSection(owned);
			request.setRequestMoney(price);
			mp.addRequest(request); //sent to 'trader'
			trader.sendRequest(request); //received from 'mp'
			mp.sendMessage("&aYou requested to trade with &e" + trader.getName() + "&7: " + owned.getColorfulName() + " &7-> &2" + price);
			trader.sendMessage("&e" + mp.getName() + " &ahas requested to trade&7: &2" + price + " &7-> " + owned.getColorfulName());
			Mineopoly.plugin.getGame().getChannel()
					.sendMessage("&b" + mp.getName() + " &3has requested to trade with &b" + trader.getName() + "&7: " +
							owned.getColorfulName() + " &7-> &2" + price, mp, trader);
		} else {
			mp.sendMessage("&e" + newArgs[1] + " &cis not an integer");
		}
	}
	
	@ParentCommand("mproperty")
	@Command(name = "trade", args = "[your-property] <player> <their-property>", desc = "Trade with another player")
	public static void trade(CommandContext context) {
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
		if(!Mineopoly.houseRules.tradeAnytime() && !mp.hasTurn()) {
			mp.sendMessage("&cYou can only iniate a trade on your turn");
			return;
		}
		if(mp.isJailed() && !Mineopoly.houseRules.tradeWhileJailed()) {
			mp.sendMessage("&cYou cannot trade while in jail");
			return;
		}
		OwnableSection owned, wanted;
		MineopolyPlayer trader;
		String[] args = context.getArgs();
		if(context.lt(2)) {
			context.sendMessageToSender("&cAt least 2 arguments required");
			return;
		} else if(context.gt(2)) { //determine what property the player wants to trade
			MineopolySection section;
			if(TacoAPI.getChatUtils().isNum(context.getString(0))) {
				int id = context.getInteger(0);
				section = Mineopoly.plugin.getGame().getBoard().getSection(id);
			} else {
				section = Mineopoly.plugin.getGame().getBoard().getSection(context.getString(0));
			}
			
			if(section == null) {
				mp.sendMessage("&e" + context.getString(0).replace("_", " ") + " &cis not a space on the board");
				return;
			}
			if(section.getType() != SectionType.PROPERTY && section.getType() != SectionType.RAILROAD && section.getType() != SectionType.UTILITY) {
				mp.sendMessage(section.getColorfulName() + " &cis not a property, railroad, or utility");
				return;
			}
			owned = (OwnableSection) section;
			if(owned.getOwner() != null && !owned.getOwner().getName().equalsIgnoreCase(mp.getName())) {
				mp.sendMessage("&cYou do not own " + section.getColorfulName());
				return;
			}
			args = TacoAPI.getChatUtils().removeFirstArg(args);
		} else { //trade current section
			MineopolySection section = mp.getCurrentSection();
			if(section.getType() != SectionType.PROPERTY && section.getType() != SectionType.RAILROAD && section.getType() != SectionType.UTILITY) {
				mp.sendMessage(section.getColorfulName() + " &cis not a property, railroad, or utility");
				return;
			}
			owned = (OwnableSection) section;
			if(owned.getOwner() != null && !owned.getOwner().getName().equalsIgnoreCase(mp.getName())) {
				mp.sendMessage("&cYou do not own " + section.getColorfulName());
				return;
			}
		}
		trader = Mineopoly.plugin.getGame().getBoard().getPlayer(args[0]);
		if(trader == null) {
			mp.sendMessage("&e" + args[0] + " &cis not playing Mineopoly");
			return;
		}
		if(trader.isJailed() && !Mineopoly.houseRules.tradeWhileJailed()) {
			mp.sendMessage("&e" + trader.getName() + " &ccannot trade because they are in jail");
			return;
		}
		MineopolySection section;
		if(TacoAPI.getChatUtils().isNum(args[1])) {
			int id = Integer.parseInt(args[1]);
			section = Mineopoly.plugin.getGame().getBoard().getSection(id);
		} else {
			section = Mineopoly.plugin.getGame().getBoard().getSection(args[0]);
		}
		
		if(section == null) {
			mp.sendMessage("&e" + args[0].replace("_", " ") + " &cis not a space on the board");
			return;
		}
		if(section.getType() != SectionType.PROPERTY && section.getType() != SectionType.RAILROAD && section.getType() != SectionType.UTILITY) {
			mp.sendMessage(section.getColorfulName() + " &cis not a property, railroad, or utility");
			return;
		}
		wanted = (OwnableSection) section;
		if(wanted.getOwner() != null && !wanted.getOwner().getName().equalsIgnoreCase(trader.getName())) {
			mp.sendMessage("&e" + trader.getName() + "does not own " + section.getColorfulName());
			return;
		}
		TradeRequest request = new TradeRequest(mp, trader, TradeType.TRADE);
		request.setOfferedSection(owned);
		request.setRequestedSection(wanted);
		mp.addRequest(request);
		trader.sendRequest(request);
		mp.sendMessage("&aYou requested to trade with &e" + trader.getName() + "&7: " + owned.getColorfulName() + " &7-> " + wanted.getColorfulName());
		trader.sendMessage("&e" + mp.getName() + " &ahas requested to trade&7: " + wanted.getColorfulName() + " &7-> " + owned.getColorfulName());
		Mineopoly.plugin.getGame().getChannel()
				.sendMessage("&b" + mp.getName() + " &3has requested to trade with &b" + trader.getName() + "&7: " +
						owned.getColorfulName() + " &7-> " + wanted.getColorfulName(), mp, trader);
	}
	
}
