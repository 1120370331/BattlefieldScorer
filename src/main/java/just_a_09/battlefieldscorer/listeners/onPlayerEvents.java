package just_a_09.battlefieldscorer.listeners;
import io.github.bedwarsrel.com.v1_8_r3.ActionBar;
import io.github.bedwarsrel.events.BedwarsTargetBlockDestroyedEvent;
import just_a_09.battlefieldscorer.BattlefieldScorer;
import just_a_09.battlefieldscorer.data.DataManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import just_a_09.battlefieldscorer.debug.debugger;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class onPlayerEvents implements Listener{

    Map<UUID, Map> KillAssistants = new HashMap();

    public onPlayerEvents(){

    }
    public boolean IsPlayerBehavior(EntityDamageByEntityEvent event){
        if ((event.getDamager().getType().equals(EntityType.PLAYER))&&(event.getEntity().getType().equals(EntityType.PLAYER))){
            return true;
        }
        if ((event.getDamager() instanceof Projectile)){
            Projectile projectile = (Projectile) event.getDamager();
            if ((projectile.getShooter() instanceof Player) &&((event.getEntity() instanceof Player))) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerHitEvent(EntityDamageEvent event){
        double final_damage = event.getFinalDamage();
    }
    @EventHandler
    public void onPlayerKillEvent(EntityDamageByEntityEvent event){
        int everydamage =  BattlefieldScorer.getInstance().config.getInt("onplayerhit_damage");
        int everykill = BattlefieldScorer.getInstance().config.getInt("onplayerhit_kill");
        int everyassist = BattlefieldScorer.getInstance().config.getInt("onplayerhit_killassist");
        int assist_time = BattlefieldScorer.getInstance().config.getInt("assist_time");
        boolean using_projecticle = false;

        if (IsPlayerBehavior(event)){
            Player damager;
            if (event.getDamager() instanceof Player) {
                damager = (Player) event.getDamager();
            }else{
                damager = (Player) ((Projectile) event.getDamager()).getShooter();
                using_projecticle = true;
            }

            Player victim = (Player) event.getEntity();
            double final_damage = event.getFinalDamage();
            double rounded_damage = Math.round(final_damage);


            if (final_damage>=(victim.getHealth())){
                //It actually dead.
                //check effective assistants
                Map EffectiveAssistants = new HashMap();
                Map <UUID,Number> VictimRelationships = KillAssistants.get(victim.getUniqueId());
                if (VictimRelationships != null) {
                    for (Object i : VictimRelationships.keySet()) {
                        UUID damager_uuid = (UUID) i;
                        if (damager_uuid != damager.getUniqueId()){

                            long nowtime = System.currentTimeMillis();
                            long historytime = (long) VictimRelationships.get(damager_uuid);
                            if (nowtime-historytime <= assist_time*1000){
                                if (everyassist>0){
                                    BattlefieldScorer.getInstance().dataManager.addActive(
                                            BattlefieldScorer.getInstance().getServer().getPlayer(damager_uuid).getName(), DataManager.PlayerDataKeys.Points
                                            ,everyassist);
                                    BattlefieldScorer.getInstance().dataManager.addActive(BattlefieldScorer.getInstance().getServer().getPlayer(damager_uuid).getName(),DataManager.PlayerDataKeys.KillAssistments,1);
                                    ActionBar.sendActionBar(BattlefieldScorer.getInstance().getServer().getPlayer(damager_uuid)
                                            ,BattlefieldScorer.getInstance()._l("ONPLAYERHIT_ASSIST")+" +"+String.valueOf(everyassist));
                                }
                            }
                        }
                    }
                    VictimRelationships = null;
                    KillAssistants.remove(victim.getUniqueId());
                }
                //killer's part


                if (everykill>0) {
                    BattlefieldScorer.getInstance().dataManager.addActive(damager.getName(), DataManager.PlayerDataKeys.Points, everykill);
                    ActionBar.sendActionBar(damager, BattlefieldScorer.getInstance()._l("ONPLAYERHIT_KILL") + " +" + String.valueOf(everykill));
                }
                BattlefieldScorer.getInstance().dataManager.addActive(damager.getName(), DataManager.PlayerDataKeys.KilledPlayers, 1);
                BattlefieldScorer.getInstance().dataManager.addActive(damager.getName(), DataManager.PlayerDataKeys.TotalHits, 1);
                if(everydamage>0) {

                    BattlefieldScorer.getInstance().dataManager.addActive(damager.getName(), DataManager.PlayerDataKeys.Points, (int) (everydamage * victim.getHealth()));
                }
                //ActionBar.sendActionBar(damager,BattlefieldScorer._l("ONPLAYERHIT_DAMAGE")+" "+"+"+String.valueOf((everydamage*victim.getHealth())));


            }else{   //Not dead.
                //assistant's part
                Map <String,Object> assistinfo = new HashMap();
                Long time = System.currentTimeMillis();
                assistinfo.put("time",time);
                assistinfo.put("damager",damager.getUniqueId());
                Map victim_relateship = new HashMap();
                if (KillAssistants.containsKey(victim.getUniqueId())) {
                    victim_relateship = KillAssistants.get(victim.getUniqueId());
                }

                victim_relateship.put(damager.getUniqueId(),time);
                KillAssistants.put(victim.getUniqueId(),victim_relateship);
                debugger.print(damager.getName());
                //damager's part
                BattlefieldScorer.getInstance().dataManager.addActive(damager.getName(), DataManager.PlayerDataKeys.TotalHits, 1);
                if(everydamage>0) {
                    BattlefieldScorer.getInstance().dataManager.addActive(damager.getName(), DataManager.PlayerDataKeys.Points, (int) (everydamage * rounded_damage));

                    //ActonBar.send(damager,BattlefieldScorer._l("ONPLAYERHIT_DAMAGE")+" "+"+"+String.valueOf((everydamage*final_damage)));
                    ActionBar.sendActionBar(damager, BattlefieldScorer.getInstance()._l("ONPLAYERHIT_DAMAGE") + " " + "+" + String.valueOf((everydamage * rounded_damage)));
                }

            }
            if (using_projecticle){
                BattlefieldScorer.getInstance().dataManager.addActive(damager.getName(),DataManager.PlayerDataKeys.ArrowHits,1);
            }
        }else{
            debugger.print("[BWBS]Listener 认为不是Player");
        }

    }
    @EventHandler(priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onPlayerBreakBlock(BlockBreakEvent blockBreakEvent){
        Block block = blockBreakEvent.getBlock();
        if ((!blockBreakEvent.isCancelled())) {
            Player player = blockBreakEvent.getPlayer();

            String broken_block_name = null;

            MemorySection ms = (MemorySection) BattlefieldScorer.getInstance().config.get("onblockbreaks");
            Set<String> enabled_blocks = ms.getKeys(false);

            Boolean contains = false;
            for (String block_name : enabled_blocks) {
                if (block_name.equals(block.getType().name())) {
                    broken_block_name = block_name;
                    contains = true;
                }
            }

            for (String block_name : enabled_blocks) {
                Material block_matiral = Material.getMaterial(block_name);
                for (ItemStack itemStack : block.getDrops()) {
                    if (itemStack.getType().equals(block_matiral)) {
                        broken_block_name = block_name;
                        contains = true;
                    }
                }
            }

            if (contains) {
                int breakrewards = ms.getInt(broken_block_name);
                if (breakrewards > 0) {
                    BattlefieldScorer.getInstance().dataManager.addActive(player.getName(),
                            DataManager.PlayerDataKeys.Points, breakrewards);
                    BattlefieldScorer.getInstance().dataManager.addActive(player.getName(), DataManager.PlayerDataKeys.BrokeBlocks, 1);

                    ActionBar.sendActionBar(player, BattlefieldScorer.getInstance()._l("ONPLAYEREVENTS_BROKEBLOCKS") + " +" + breakrewards);
                }
            }
        }else{
            debugger.print("[BattlefieldScorer]BreakBlock Event is cancelled.");
        }

    }

    @EventHandler
    public void onBedwarsBedBroke(BedwarsTargetBlockDestroyedEvent bedwarsTargetBlockDestroyedEvent){
        Player player = bedwarsTargetBlockDestroyedEvent.getPlayer();
        int rewards = BattlefieldScorer.getInstance().config.getInt("onbedbroke");
        if (rewards>0){
            BattlefieldScorer.getInstance().dataManager.addActive(player.getName(),DataManager.PlayerDataKeys.Points,rewards);

            BattlefieldScorer.getInstance().dataManager.addActive(player.getName(),DataManager.PlayerDataKeys.BedwarsBedDestroyed,1);
            ActionBar.sendActionBar(player,BattlefieldScorer.getInstance()._l("ONPLAYEREVENTS_BEDWARSDESTROYEDBED")+" +"+rewards);

        }

    }

    @EventHandler
    public void onArrowShootsEvent(ProjectileLaunchEvent projectileLaunchEvent){
        if (projectileLaunchEvent.getEntity().getShooter() instanceof Player){
            Player player = (Player) projectileLaunchEvent.getEntity().getShooter();
            BattlefieldScorer.getInstance().dataManager.addActive(player.getName(),DataManager.PlayerDataKeys.ArrowShoots,1);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onBlockPlaces(BlockPlaceEvent blockPlaceEvent){
        if (!blockPlaceEvent.isCancelled()){
            Player player = blockPlaceEvent.getPlayer();
            if (player!=null){
                Block placedblock = blockPlaceEvent.getBlock();
                Material block_material = placedblock.getType();
                MemorySection ms = (MemorySection) BattlefieldScorer.getInstance().config.get("onblockplace");
                Set <String> enabled_blocks = ms.getKeys(false);
                String block_name = null;
                boolean contains = false;
                for (String blockname:enabled_blocks){
                    if (block_material.name().equals(blockname)){
                        block_name = blockname;
                        contains = true;
                    }
                }
                if (contains) {
                    int placerewards = ms.getInt(block_name);
                    if (placerewards > 0) {
                        BattlefieldScorer.getInstance().dataManager.addActive(player.getName(),
                                DataManager.PlayerDataKeys.Points, placerewards);
                        BattlefieldScorer.getInstance().dataManager.addActive(player.getName(), DataManager.PlayerDataKeys.PlaceBlocks, 1);

                        ActionBar.sendActionBar(player, BattlefieldScorer.getInstance()._l("ONPLAYEREVENTS_PLACEBLOCKS") + " +" + placerewards);
                    }
                }

            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onFoodEaten(PlayerItemConsumeEvent playerItemConsumeEvent){
        Player player = playerItemConsumeEvent.getPlayer();
        if (!playerItemConsumeEvent.isCancelled()){
            ItemStack consumed = playerItemConsumeEvent.getItem();
            MemorySection ms = (MemorySection) BattlefieldScorer.getInstance().config.get("oneat");
            Set<String> enabled_consum = ms.getKeys(false);
            boolean contains = false;
            String consumed_name = null;
            for (String consumname:enabled_consum){
                if (consumname.equals(consumed.getType().name())){
                    consumed_name = consumname;
                    contains = true;
                }
            }
            if (contains){
                int consumrewards = ms.getInt(consumed_name);
                if (consumrewards > 0) {
                    BattlefieldScorer.getInstance().dataManager.addActive(player.getName(),
                            DataManager.PlayerDataKeys.Points, consumrewards);
                    BattlefieldScorer.getInstance().dataManager.addActive(player.getName(), DataManager.PlayerDataKeys.EatenFoods, 1);

                    ActionBar.sendActionBar(player, BattlefieldScorer.getInstance()._l("ONPLAYEREVENTS_EATENFOODS") + " +" + consumrewards);
                }
            }

        }
    }
    @EventHandler
    public void onPlayerDead(PlayerDeathEvent playerDeathEvent){
        Player player = (Player) playerDeathEvent.getEntity();
        BattlefieldScorer.getInstance().dataManager.addActive(player.getName(), DataManager.PlayerDataKeys.DeathTimes, 1);
    }
}
