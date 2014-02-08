/*
 * Copyright (C) 2014 Joshua M Hertlein
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.jmhertlein.mobprotect.command;

import java.util.Set;
import java.util.UUID;
import net.jmhertlein.core.ebcf.CommandLeaf;
import net.jmhertlein.core.ebcf.InsufficientPermissionException;
import net.jmhertlein.core.ebcf.UnsupportedCommandSenderException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author joshua
 */
public class UnprotectEntityCommand extends CommandLeaf {
    private final Set<Player> pendingUnprotectors;
    
    public UnprotectEntityCommand(final Set<Player> pendingUnprotectors) {
        super("unprotect");
        this.pendingUnprotectors = pendingUnprotectors;
    }
    

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) throws InsufficientPermissionException, UnsupportedCommandSenderException {
        if(!(sender instanceof Player))
            throw new UnsupportedCommandSenderException(sender);
        if(!sender.hasPermission("mobprotect.use"))
            throw new InsufficientPermissionException("Error: insufficient permission.");
        
        pendingUnprotectors.add((Player) sender);
        sender.sendMessage(ChatColor.GOLD + "Punch an entity to unprotect it.");
        
    }

    @Override
    public String getMissingRequiredArgsHelpMessage() {
        return "";
    }
}
