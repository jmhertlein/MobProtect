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

package net.jmhertlein.mobprotect.listener;

import java.util.Set;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 *
 * @author joshua
 */
public class EntityDamageListener implements Listener {
    private final Set<UUID> protectedEntities;
    private final Set<Player> pendingProtectors, pendingUnprotectors;

    public EntityDamageListener(final Set<UUID> protectedEntities, final Set<Player> pendingProtectors, final Set<Player> pendingUnprotectors) {
        this.protectedEntities = protectedEntities;
        this.pendingProtectors = pendingProtectors;
        this.pendingUnprotectors = pendingUnprotectors;
    }

    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if(e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Player) {
           Player puncher = (Player) ((EntityDamageByEntityEvent) e).getDamager();
           if(pendingProtectors.contains(puncher)) {
               protectedEntities.add(e.getEntity().getUniqueId());
               pendingProtectors.remove(puncher);
               puncher.sendMessage(ChatColor.GREEN + "Entity protected.");
           } else if(pendingUnprotectors.contains(puncher)) {
               protectedEntities.remove(e.getEntity().getUniqueId());
               pendingUnprotectors.remove(puncher);
               puncher.sendMessage(ChatColor.GREEN + "Entity unprotected.");
           }
        }
        
        if(protectedEntities.contains(e.getEntity().getUniqueId()))
            e.setCancelled(true);
    }
}
