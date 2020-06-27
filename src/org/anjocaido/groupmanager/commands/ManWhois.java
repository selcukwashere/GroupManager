/*
 *  GroupManager - A plug-in for Spigot/Bukkit based Minecraft servers.
 *  Copyright (C) 2020  ElgarL
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.anjocaido.groupmanager.commands;

import java.util.ArrayList;
import java.util.List;

import org.anjocaido.groupmanager.data.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * @author ElgarL
 *
 */
public class ManWhois extends BaseCommand implements TabCompleter {

	/**
	 * 
	 */
	public ManWhois() {}

	@Override
	protected boolean parseCommand(@NotNull String[] args) {

		// Validating state of sender
		if (dataHolder == null || permissionHandler == null) {
			if (!setDefaultWorldHandler(sender))
				return true;
		}
		// Validating arguments
		if (args.length != 1) {
			sender.sendMessage(ChatColor.RED + "Review your arguments count! (/manwhois <player>)");
			return true;
		}
		if ((plugin.isValidateOnlinePlayer()) && ((match = validatePlayer(args[0], sender)) == null)) {
			return false;
		}
		if (match != null) {
			auxUser = dataHolder.getUser(match.toString());
		} else {
			auxUser = dataHolder.getUser(args[0]);
		}
		// Seems OK
		sender.sendMessage(ChatColor.YELLOW + "Name: " + ChatColor.GREEN + auxUser.getLastName());
		sender.sendMessage(ChatColor.YELLOW + "Group: " + ChatColor.GREEN + auxUser.getGroup().getName());
		// Compile a list of subgroups
		auxString = "";
		for (String subGroup : auxUser.subGroupListStringCopy()) {
			auxString += subGroup + ", ";
		}
		if (auxString.lastIndexOf(",") > 0) {
			auxString = auxString.substring(0, auxString.lastIndexOf(","));
			sender.sendMessage(ChatColor.YELLOW + "subgroups: " + auxString);
		}

		sender.sendMessage(ChatColor.YELLOW + "Overloaded: " + ChatColor.GREEN + dataHolder.isOverloaded(auxUser.getUUID()));
		auxGroup = dataHolder.surpassOverload(auxUser.getUUID()).getGroup();
		if (!auxGroup.equals(auxUser.getGroup())) {
			sender.sendMessage(ChatColor.YELLOW + "Original Group: " + ChatColor.GREEN + auxGroup.getName());
		}
		// victim.permissions.add(args[1]);
		return true;
	}
	
	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

		parseSender(sender, alias);
		
		List<String> result = new ArrayList<String>();
		
		/*
		 * Return a TabComplete for users.
		 */
		if (args.length == 1) {

			for (User user : dataHolder.getUserList()) {
				result.add(user.getLastName());
			}
			return result;
		}
		
		return null;
	}

}
