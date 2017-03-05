/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.org.apache.commons.lang3.RandomStringUtils
 *  org.bukkit.scoreboard.DisplaySlot
 *  org.bukkit.scoreboard.Objective
 *  org.bukkit.scoreboard.Score
 *  org.bukkit.scoreboard.Scoreboard
 *  org.bukkit.scoreboard.Team
 */
package me.esshd.hcf.scoreboard;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TIntObjectProcedure;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import me.esshd.hcf.scoreboard.SidebarEntry;
import net.minecraft.util.org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class BufferedObjective {
    private static final int MAX_SIDEBAR_ENTRIES = 32;
    private static final int MAX_NAME_LENGTH = 32;
    private static final int MAX_PREFIX_LENGTH = 32;
    private static final int MAX_SUFFIX_LENGTH = 32;
    private final Scoreboard scoreboard;
    private Set<String> previousLines = new HashSet<String>();
    private TIntObjectHashMap<SidebarEntry> contents = new TIntObjectHashMap();
    private boolean requiresUpdate = false;
    private String title;
    private Objective current;
    private DisplaySlot displaySlot;

    public BufferedObjective(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
        this.title = RandomStringUtils.randomAlphabetic((int)4);
        this.current = scoreboard.registerNewObjective("buffered", "dummy");
    }

    public void setTitle(String title) {
        if (this.title == null || !this.title.equals(title)) {
            this.title = title;
            this.requiresUpdate = true;
        }
    }

    public void setDisplaySlot(DisplaySlot slot) {
        this.displaySlot = slot;
        this.current.setDisplaySlot(slot);
    }

    public void setAllLines(List<SidebarEntry> lines) {
        if (lines.size() != this.contents.size()) {
            this.contents.clear();
            if (lines.isEmpty()) {
                this.requiresUpdate = true;
                return;
            }
        }
        int size = Math.min(32, lines.size());
        int count = 0;
        for (SidebarEntry sidebarEntry : lines) {
            this.setLine(size - count++, sidebarEntry);
        }
    }

    public void setLine(int lineNumber, SidebarEntry sidebarEntry) {
        SidebarEntry value = this.contents.get(lineNumber);
        if (value == null || value != sidebarEntry) {
            this.contents.put(lineNumber, sidebarEntry);
            this.requiresUpdate = true;
        }
    }

    public void flip() {
        if (!this.requiresUpdate) {
            return;
        }
        final HashSet<String> adding = new HashSet<String>();
        this.contents.forEachEntry(new TIntObjectProcedure<SidebarEntry>(){

            @Override
            public boolean execute(int i, SidebarEntry sidebarEntry) {
                Team team;
                String name = sidebarEntry.name;
                if (name.length() > 32) {
                    name = name.substring(0, 32);
                }
                if ((team = BufferedObjective.this.scoreboard.getTeam(name)) == null) {
                    team = BufferedObjective.this.scoreboard.registerNewTeam(name);
                }
                if (sidebarEntry.prefix != null) {
                    team.setPrefix(sidebarEntry.prefix.length() > 32 ? sidebarEntry.prefix.substring(0, 32) : sidebarEntry.prefix);
                }
                if (sidebarEntry.suffix != null) {
                    team.setSuffix(sidebarEntry.suffix.length() > 32 ? sidebarEntry.suffix.substring(0, 32) : sidebarEntry.suffix);
                }
                adding.add(name);
                if (!team.hasEntry(name)) {
                    team.addEntry(name);
                }
                BufferedObjective.this.current.getScore(name).setScore(i);
                return true;
            }
        });
        this.previousLines.removeAll(adding);
        Iterator<String> iterator = this.previousLines.iterator();
        while (iterator.hasNext()) {
            String last = iterator.next();
            Team team = this.scoreboard.getTeam(last);
            if (team != null) {
                team.removeEntry(last);
            }
            this.scoreboard.resetScores(last);
            iterator.remove();
        }
        this.previousLines = adding;
        this.current.setDisplayName(this.title);
        this.requiresUpdate = false;
    }

    public void setVisible(boolean value) {
        if (this.displaySlot != null && !value) {
            this.scoreboard.clearSlot(this.displaySlot);
            this.displaySlot = null;
        } else if (this.displaySlot == null && value) {
            this.displaySlot = DisplaySlot.SIDEBAR;
            this.current.setDisplaySlot(this.displaySlot);
        }
    }

}

