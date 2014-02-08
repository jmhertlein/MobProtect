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

package net.jmhertlein.mobprotect;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.jmhertlein.core.ebcf.TreeCommandExecutor;
import net.jmhertlein.mobprotect.command.ProtectEntityCommand;
import net.jmhertlein.mobprotect.command.UnprotectEntityCommand;
import net.jmhertlein.mobprotect.listener.EntityDamageListener;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author joshua
 */
public class MobProtectPlugin extends JavaPlugin {
    private static final String PROTECTED_ENTITIES_PATH = "protectedEntities";
    private Set<UUID> protectedEntities;
    private transient Set<Player> pendingProtectors, pendingUnprotectors;
    
    @Override
    public void onEnable() {
        saveDefaultConfig();
        protectedEntities = new HashSet<>();
        pendingProtectors = new HashSet<>();
        pendingUnprotectors = new HashSet<>();
        
        for(String s : getConfig().getStringList(PROTECTED_ENTITIES_PATH))
            protectedEntities.add(UUID.fromString(s));
        
        getServer().getPluginManager().addPermission(new Permission("mobprotect.use"));
        getServer().getPluginManager().registerEvents(new EntityDamageListener(protectedEntities, pendingProtectors, pendingUnprotectors), this);
        
        TreeCommandExecutor e = new TreeCommandExecutor();
        e.add(new ProtectEntityCommand(pendingProtectors));
        TreeCommandExecutor e2 = new TreeCommandExecutor();
        e2.add(new UnprotectEntityCommand(pendingUnprotectors));
        getCommand("protect").setExecutor(e);
        getCommand("unprotect").setExecutor(e2);
    }

    @Override
    public void onDisable() {
        List<String> l = new LinkedList<>();
        for(UUID id : protectedEntities)
            l.add(id.toString());
        getConfig().set(PROTECTED_ENTITIES_PATH, l);
        saveConfig();
    }
    
    
    
}
