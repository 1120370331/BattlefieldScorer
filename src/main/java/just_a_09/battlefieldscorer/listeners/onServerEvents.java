package just_a_09.battlefieldscorer.listeners;

import just_a_09.battlefieldscorer.BattlefieldScorer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class onServerEvents implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){
        Player player = playerJoinEvent.getPlayer();
        BattlefieldScorer.getInstance().dataManager.newPlayer(player.getName(),true);

    }
    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent playerQuitEvent){
        Player player = playerQuitEvent.getPlayer();
        BattlefieldScorer.getInstance().dataManager.ActiveClear(player.getName());

    }
}
