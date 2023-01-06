package just_a_09.battlefieldscorer.commands;

import just_a_09.battlefieldscorer.BattlefieldScorer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class clean implements ICommand{
    Plugin plugin;
    CommandSender commandSender;
    Command command;
    String s;
    String[] args;
    String type;

    public clean(String type,Plugin plugin, CommandSender commandSender, Command command, String s, String[] args){
        this.plugin = plugin;
        this.commandSender=commandSender;
        this.command = command;
        this.s = s;
        this.args = args;
        this.type = type;
        if (commandSender instanceof Player) {
            if (args.length == 2) {
                if (this.type.equalsIgnoreCase("cleanactive")){

                    this.execute(args[1],"active");

                }else if(this.type.equalsIgnoreCase("cleanlifetime")){

                    this.execute(args[1],"lifetime");

                }else{
                       return;
                }



            }else if(args.length == 1){
                commandSender.sendMessage(
                        BattlefieldScorer.getInstance()._l("INVALID_COMMAND")
                );
                commandSender.sendMessage("/battlefieldscorer <cleanActive/cleanLifetime> <playername>");
            }
            else {
                commandSender.sendMessage(
                        BattlefieldScorer.getInstance()._l("INVALID_COMMAND")
                );
                commandSender.sendMessage("/battlefieldscorer <cleanActive/cleanLifetime> <playername>");
            }
        }else {
            commandSender.sendMessage(BattlefieldScorer.getInstance()._l("INVALID_COMMAND"));
        }
    }




    public boolean execute(String name,String type) {
        if (type.equals("lifetime")) {
            commandSender.sendMessage(BattlefieldScorer.getInstance()._l("CHAT_PREFIX")+BattlefieldScorer.getInstance()._l("COMMAND_CLEAN_LIFETIME").replaceAll("<player>",args[1]));
            BattlefieldScorer.getInstance().dataManager.LifetimeClear(name);
        }else if (type.equals("active")){
            commandSender.sendMessage(BattlefieldScorer.getInstance()._l("CHAT_PREFIX")+BattlefieldScorer.getInstance()._l("COMMAND_CLEAN_ACTIVE").replaceAll("<player>",args[1]));
            BattlefieldScorer.getInstance().dataManager.ActiveClear(name);
        }else{
            return false;
        }

        return true;
    }
}
