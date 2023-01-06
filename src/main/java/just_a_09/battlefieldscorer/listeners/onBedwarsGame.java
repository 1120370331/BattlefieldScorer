package just_a_09.battlefieldscorer.listeners;

import io.github.bedwarsrel.events.*;
import just_a_09.battlefieldscorer.BattlefieldScorer;
import just_a_09.battlefieldscorer.data.DataManager;
import just_a_09.battlefieldscorer.debug.debugger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class onBedwarsGame implements Listener {
    @EventHandler
    public void  BedwarsGameEnd(BedwarsGameEndEvent event){
        debugger.print("[BattlefieldScorer]Game Ended.");
        Map players = new HashMap();
        event.getGame().getPlayers();
        Iterator var1 = event.getGame().getPlayers().iterator();
        while (var1.hasNext()){

            Player player = (Player) var1.next();
            if (player.getName()!=null) {

                BattlefieldScorer.getInstance().tabList.disable(player.getUniqueId());
                BattlefieldScorer.getInstance().dataManager.ShowStates(player.getName(),player.getName(),"Lifetime");
                BattlefieldScorer.getInstance().dataManager.ActiveClear(player.getName());
            }else{
                debugger.print("[BattlefieldScorer]end null getted");
            }
        }

    }
    public static ArrayList<Player> StartedPlayer = new ArrayList();
    @EventHandler
    public void onBedwarsGameStart(BedwarsGameStartedEvent event){
        Map players = new HashMap();
        event.getGame().getPlayers();
        Iterator var1 = event.getGame().getPlayers().iterator();
        while (var1.hasNext()){

            Player player = (Player) var1.next();
            if (player.getName()!=null) {
                BattlefieldScorer.getInstance().dataManager.newPlayer(player.getName(),true);
                BattlefieldScorer.getInstance().dataManager.ActiveClear(player.getName());
                BattlefieldScorer.getInstance().tabList.enable(player.getUniqueId());
                BattlefieldScorer.getInstance().dataManager.addLifetime(player.getName(), DataManager.PlayerDataKeys.BedwarsPlayed,1);
                StartedPlayer.add(player);
            }
        }
    }
    @EventHandler
    public void onBedwarsPlayerLeave(BedwarsPlayerLeaveEvent event){
        Player player = event.getPlayer();
        BattlefieldScorer.getInstance().tabList.disable(player.getUniqueId());
        BattlefieldScorer.getInstance().dataManager.save();
        BattlefieldScorer.getInstance().dataManager.ActiveClear(player.getName());

    }

    @EventHandler
    public void onBedwarsOver(BedwarsGameOverEvent bedwarsGameOverEvent){

        List<Player> players = bedwarsGameOverEvent.getGame().getPlayers();
        for (Player player:players){
            BattlefieldScorer.getInstance().dataManager.ShowStates(player.getName(),player.getName(),"Active");
        }

    }
}
