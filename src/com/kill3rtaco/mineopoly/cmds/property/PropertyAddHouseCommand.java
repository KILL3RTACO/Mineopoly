package com.kill3rtaco.mineopoly.cmds.property;

import org.bukkit.entity.Player;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyPlayer;
import com.kill3rtaco.mineopoly.messages.CannotPerformActionMessage;
import com.kill3rtaco.mineopoly.messages.GameNotInProgressMessage;
import com.kill3rtaco.mineopoly.messages.InsufficientFundsMessage;
import com.kill3rtaco.mineopoly.messages.InvalidTurnMessage;
import com.kill3rtaco.mineopoly.messages.NotPlayingGameMessage;
import com.kill3rtaco.mineopoly.messages.SectionNotFoundMessage;
import com.kill3rtaco.mineopoly.sections.MineopolySection;
import com.kill3rtaco.mineopoly.sections.Property;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.tacoapi.api.TacoCommand;

public class PropertyAddHouseCommand extends TacoCommand {

	public PropertyAddHouseCommand() {
		super("add-house", new String[]{}, "[property]", "Add a House to a property", "");
	}

	@Override
	public void onPlayerCommand(Player player, String[] args) {
		if(Mineopoly.plugin.getGame().isRunning()){
			if(Mineopoly.plugin.getGame().hasPlayer(player)){
				MineopolyPlayer mp = Mineopoly.plugin.getGame().getBoard().getPlayer(player);
				if(mp.hasTurn()){
					if(args.length == 0){ //add house to current property
						if(mp.getCurrentSection() instanceof Property){
							Property prop = (Property) mp.getCurrentSection();
							if(prop.getHouses() < 4){
								if(mp.hasMonopoly(prop.getColor())){
									if(mp.canAddHouse(prop)){
										prop.addHouse();
										Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3added a house to " + prop.getColorfulName(), mp);
										mp.sendMessage("&3You added a &ahouse &3to " + prop.getColorfulName());
										mp.sendBalanceMessage();
										if(prop.getHouses() == 4){
											mp.sendMessage("&3You can now add a hotel to " + prop.getColorfulName() + " &3by typing &b/property add-hotel " + prop.getId());
										}
									}else{
										mp.sendMessage(new InsufficientFundsMessage());
									}
								}else{
									mp.sendMessage("You do not have a monopoly for the color " + prop.getColor().getName());
								}
							}else{
								mp.sendMessage(new CannotPerformActionMessage("add a house to that. There are already 4 houses"));
							}
						}else{
							mp.sendMessage(new CannotPerformActionMessage("add a house to that"));
						}
					}else{ //get specified property and add house to it if not null (id or name, name being the config name)
						MineopolySection section;
						if(TacoAPI.getChatUtils().isNum(args[0]))
							section = Mineopoly.plugin.getGame().getBoard().getSection(Integer.parseInt(args[0]));
						else
							section = Mineopoly.plugin.getGame().getBoard().getSection(args[0]);
						if(section == null){
							mp.sendMessage(new SectionNotFoundMessage());
						}else{
							if(section instanceof Property){
								Property prop = (Property) section;
								if(prop.getHouses() < 4){
									if(mp.hasMonopoly(prop.getColor())){
										if(mp.canAddHouse(prop)){
											prop.addHouse();
											Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3added a house to " + prop.getColorfulName(), mp);
											mp.sendMessage("&3You added a &ahouse &3to " + prop.getColorfulName());
											mp.sendBalanceMessage();
											if(prop.getHouses() == 4){
												mp.sendMessage("&3You can now add a hotel to " + prop.getColorfulName() + " &3by typing &b/property add-hotel " + prop.getId());
											}
										}else{
											mp.sendMessage(new InsufficientFundsMessage());
										}
									}else{
										mp.sendMessage("You do not have a monopoly for the color " + prop.getColor().getName());
									}
								}else{
									mp.sendMessage(new CannotPerformActionMessage("add a house to that. There are already 4 houses"));
								}
							}else{
								mp.sendMessage(new CannotPerformActionMessage("add a house to that"));
							}
						}
					}
				}else{
					Mineopoly.plugin.chat.sendPlayerMessage(player, new InvalidTurnMessage());
				}
			}else{
				Mineopoly.plugin.chat.sendPlayerMessage(player, new NotPlayingGameMessage());
			}
		}else{
			Mineopoly.plugin.chat.sendPlayerMessage(player, new GameNotInProgressMessage());
		}
	}

	@Override
	public boolean onConsoleCommand(String[] args) {
		return false;
	}

}