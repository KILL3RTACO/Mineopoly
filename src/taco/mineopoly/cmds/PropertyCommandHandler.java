package taco.mineopoly.cmds;

import org.bukkit.entity.Player;

import taco.tacoapi.TacoAPI;
import taco.tacoapi.api.TacoCommandHandler;
import taco.mineopoly.Mineopoly;
import taco.mineopoly.cmds.property.PropertyAddHotelCommand;
import taco.mineopoly.cmds.property.PropertyAddHouseCommand;
import taco.mineopoly.cmds.property.PropertyBuyCommand;
import taco.mineopoly.cmds.property.PropertyInfoCommand;
import taco.mineopoly.cmds.property.PropertyRemoveHotelCommand;
import taco.mineopoly.cmds.property.PropertyRemoveHouseCommand;

public class PropertyCommandHandler extends TacoCommandHandler {

	public PropertyCommandHandler() {
		super("property", "Mineopoly Property commands", "");
	}

	@Override
	protected void registerCommands() {
		registerCommand(new PropertyAddHouseCommand());
		registerCommand(new PropertyAddHotelCommand());
		registerCommand(new PropertyBuyCommand());
		registerCommand(new PropertyInfoCommand());
		registerCommand(new PropertyRemoveHouseCommand());
		registerCommand(new PropertyRemoveHotelCommand());
	}

	@Override
	protected boolean onConsoleCommand() {
		return false;
	}

	@Override
	protected void onPlayerCommand(Player player) {
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, TacoAPI.getChatUtils().createHeader("&c/property"));
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&3Aliases&7: &b/property&7, &b/prop&7, &b/mprop&7, &b/p");
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&3Commands&7: &b/property ? [page]");
	}

}
