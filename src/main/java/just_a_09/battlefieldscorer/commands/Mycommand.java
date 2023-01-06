package just_a_09.battlefieldscorer.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Mycommand {
    private String name = null;
    private Player sender = null;
    boolean haspermission(CommandSender player){
        player = (Player)player;
        if (player.hasPermission("bedwarsbattlefieldscorer."+this.name)){
            return true;
        }else{
            player.sendMessage("ee");
            return false;
        }

    }
}
