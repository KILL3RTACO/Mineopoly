package com.kill3rtaco.mineopoly.game.cards;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.actions.EventAction;
import com.kill3rtaco.tacoapi.TacoAPI;

public class MineopolyCard {
	
	public static boolean		eventInProgess	= false;
	
	private String				description, name, action, invalidReason = "";
	private MineopolyCardAction	cardAction;
	private MineopolyPlayer		drawer			= null;
	private CardResult			result			= null;
	private CardType			type;
	protected boolean			valid			= true;
	private Object[]			params;
	
	public MineopolyCard(String name, CardType type, String description, String action) {
		this.name = name;
		this.type = type;
		this.description = description;
		this.action = action;
		validate();
	}
	
	public void action(MineopolyPlayer player) {
//		System.out.println(cardAction == null ? "null" : cardAction.name);
		result = cardAction.doAction(player, params);
	}
	
	public String getDescription() {
		return description;
	}
	
	public MineopolyCardAction getAction() {
		return cardAction;
	}
	
	public String getFormattedAction() {
		if(valid) {
			String[] parts = action.split("\\s+");
			String formattedAction = parts[0] + "(";
			for(int i = 1; i < parts.length; i++) {
				formattedAction += parts[i];
				if(i < parts.length - 1) {
					formattedAction += ", ";
				}
			}
			return formattedAction + ")";
		} else {
			return "InvalidAction";
		}
	}
	
	public String getFormattedDescription() {
		String message = "&b";
		for(String s : description.split("\\s+")) {
			boolean found = false;
			for(MineopolyCardVariable v : MineopolyCardVariable.values()) {
				if(s.matches(v.getRegex())) {
					message += v.getReplaceMethod().replace(s, this) + " ";
					found = true;
				}
			}
			if(!found)
				message += s + " ";
		}
		return message.replace("&r", "&r&b");
	}
	
	public MineopolyPlayer getDrawer() {
		return drawer;
	}
	
	public String getName() {
		return name;
	}
	
	public CardResult getResult() {
		return result;
	}
	
	public CardType getType() {
		return type;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public void readDescription(MineopolyPlayer player) {
		drawer = player;
		Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + player.getName() + " &3drew a " + type.getName() + " &3card, it says:");
		Mineopoly.plugin.getGame().getChannel().sendMessage(getFormattedDescription());
	}
	
	private void validate() {
		if(description == null || description.isEmpty() || action == null || action.isEmpty()) {
			valid = false;
			return;
		}
		String name = "";
		String[] params = action.split("\\s+");
		if(params.length == 0) {
			invalidReason = "";
			valid = false;
			return;
		} else {
			name = params[0];
			params = TacoAPI.getChatUtils().removeFirstArg(params);
		}
		boolean found = false;
		for(MineopolyCardAction a : MineopolyCardActionManager.getActions()) {
			if(!a.getName().equalsIgnoreCase(name)) {
				continue;
			}
			found = true;
			
			if(a.getRequiredParamLength() != params.length) {
				valid = false;
				invalidReason = a.getRequiredParamLength() + " params required, " + params.length + " given";
				break;
			}
			
			this.params = new Object[a.getRequiredParamLength()];
			String types = a.getParamTypes();
			for(int i = 0; i < types.length(); i++) {
				if(types.charAt(i) == 'b') {
					this.params[i] = Boolean.valueOf(params[i]);
				} else if(types.charAt(i) == 'i') {
					if(TacoAPI.getChatUtils().isNum(params[i])) {
						this.params[i] = Integer.parseInt(params[i]);
					} else {
						valid = false;
						invalidReason = "param " + i + " is not an integer";
						break;
					}
				} else if(types.charAt(i) == 's') {
					this.params[i] = params[i];
				}
			}
			if(valid && a.getName().equalsIgnoreCase("event")) {
				if(!EventAction.validEvents().contains(params[0])) {
					valid = false;
					invalidReason = "event '" + params[0] + "' does not exist";
				}
			}
		}
		if(!found) {
			valid = false;
			invalidReason = "action '" + name + "' not found";
		}
		if(found && valid) {
			cardAction = MineopolyCardActionManager.getAction(name);
		}
	}
	
	public String getInvalidReason() {
		return invalidReason;
	}
	
}
