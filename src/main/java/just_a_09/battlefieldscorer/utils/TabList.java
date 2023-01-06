package just_a_09.battlefieldscorer.utils;

import io.github.bedwarsrel.BedwarsRel;
import just_a_09.battlefieldscorer.BattlefieldScorer;
import just_a_09.battlefieldscorer.BattlefieldScorer;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import just_a_09.battlefieldscorer.debug.debugger;
public class TabList {
    Map<UUID,Boolean> PL = new HashMap<>();

    public void enable(UUID name){
        PL.put(name,true);
        debugger.print("[BWBS]tab enabled:"+
                        BattlefieldScorer.getInstance().getServer().getPlayer(name).getName()
                );
        this.Refresh();
    }

    public void disable(UUID name){
        PL.put(name,false);
        Player player = BattlefieldScorer.getInstance().getServer().getPlayer(name);
        player.setPlayerListName(player.getDisplayName());

    }
    public TabList(){

    }

    public void Refresh() {
        if (BattlefieldScorer.getInstance().config.getBoolean("enable_tablist")) {
            for (UUID p : PL.keySet()) {
                if (PL.get(p)) {

                    Player player = BattlefieldScorer.getInstance().getServer().getPlayer(p);

                    if (player != null) {
                        //debugger.print("[BWBS]Tablist刷新"+player.getName());
                        player.setPlayerListName(

                                "§f[§e" +
                                        String.valueOf(BattlefieldScorer.getInstance().dataManager.getPlayer(player.getName()).getActivePoints()
                                                + "§f] " + "§" +
                                                BedwarsRel.getInstance().getGameManager().getGameOfPlayer(player).getPlayerTeam(player).getColor().getChatColor().getChar()
                                                + player.getName()
                                        ));
                    } else {
                        debugger.print("[BWBS]tab null player getted");
                    }
                }
            }
        }
    }
}
