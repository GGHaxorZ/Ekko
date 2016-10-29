package com.alexandeh.ekko.factions;

import com.alexandeh.ekko.Ekko;
import com.alexandeh.ekko.factions.claims.Claim;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.files.ConfigFile;
import com.alexandeh.ekko.utils.ItemBuilder;
import com.alexandeh.ekko.utils.player.SimpleOfflinePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/*
 * Copyright (c) 2016, Alexander Maxwell. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - The name of Alexander Maxwell may not be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
@Getter
public class Faction {

    private static Set<Faction> factions = new HashSet<>();
    private Ekko main = Ekko.getInstance();

    public ConfigFile mainConfig = main.getMainConfig();

    @Setter
    private String name, home;

    @Getter @Setter
    private ChatColor color;
    
    private UUID uuid;
    private Set<Claim> claims;

    public Faction(String name, UUID uuid, ChatColor color) {
        this.name = name;
        this.uuid = uuid;
        this.color = color;

        claims = new HashSet<>();

        if (uuid == null) {
            this.uuid = UUID.randomUUID();
        /*
        "Only after generating 1 billion UUIDs every second for the next 100 years, the probability of creating just one duplicate would be about 50%." - Wikipedia
          while (getByUuid(uuid) != null) {
              uuid = UUID.randomUUID();
          }*/
        }
        factions.add(this);
    }


    public static Faction getByName(String name) {
        for (Faction faction : getFactions()) {
            if (faction.getName().equalsIgnoreCase(name)) {
                return faction;
            }
        }
        return null;
    }

    public static Faction getByUuid(UUID uuid) {
        for (Faction faction : getFactions()) {
            if (faction.getUuid().equals(uuid)) {
                return faction;
            }
        }
        return null;
    }

    public static Set<Faction> getAllByString(String string) {
        Set<Faction> toReturn = new HashSet<>();
        for (Faction faction : factions) {
            if (!(toReturn.contains(faction))) {

                if (faction.getName().equalsIgnoreCase(string)) {
                    toReturn.add(faction);
                }

                if (faction instanceof PlayerFaction) {
                    PlayerFaction playerFaction = (PlayerFaction) faction;

                    for (UUID uuid : playerFaction.getAllPlayerUuids()) {
                        SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByUuid(uuid);
                        if (offlinePlayer != null && offlinePlayer.getName().equalsIgnoreCase(string)) {
                            toReturn.add(faction);
                        }
                    }
                }

            }
        }
        return toReturn;
    }

    public static ItemStack getWand(Ekko main) {
        return new ItemBuilder(Material.valueOf(main.getMainConfig().getString("FACTION_CLAIMING.WAND.TYPE")))
                .lore(main.getMainConfig().getStringList("FACTION_CLAIMING.WAND.LORE"))
                .name(main.getMainConfig().getString("FACTION_CLAIMING.WAND.NAME"))
                .data(main.getMainConfig().getInt("FACTION_CLAIMING.WAND.DATA")).build();
    }

    public static Set<Faction> getFactions() {
        return factions;
    }
}
