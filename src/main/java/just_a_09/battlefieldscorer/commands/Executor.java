package just_a_09.battlefieldscorer.commands;

import just_a_09.battlefieldscorer.BattlefieldScorer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Executor implements CommandExecutor {
    BattlefieldScorer plugin;
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
        if(command.getName().equalsIgnoreCase("battlefieldscorer")){  //your command name
            List<String> l = new ArrayList<String>(); //makes a ArrayList


            //define the possible possibility's for argument 1
            if ((args.length==1)){

                l.add("reload");
                l.add("cleanActive");
                l.add("cleanLifetime");
                l.add("active");
                l.add("lifetime");
                l.add("help");

            }
            return l; //returns the possibility's to the client

        }
        return null; //this is a little confusing but just put it there
        //it just returns NULL if absolutely nothing has happened
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 0){
            new help(this.plugin, commandSender, command, s, args);
            return true;
        }else if(args.length >= 1) {
            if (args[0].equals("reload")) {
                new reload(this.plugin, commandSender, command, s, args);
                return true;
            } else if (
                    args[0].equals("active")
            ) {
                new showstates("Active", this.plugin, commandSender, command, s, args);
                return true;
            } else if (
                    args[0].equals("lifetime")
            ) {
                new showstates("Lifetime", this.plugin, commandSender, command, s, args);
                return true;
            }else if(
                    (args[0].equalsIgnoreCase("cleanlifetime"))||(args[0].equalsIgnoreCase("cleanactive"))
            ) {
                new clean(args[0],this.plugin,commandSender,command,s,args);
                return  true;
            }
            else {
                new help(this.plugin, commandSender, command, s, args);
                return true;
            }
        }
        return  false;
    }
    public Executor(BattlefieldScorer plugin){
        this.plugin = plugin;
        this.plugin.getCommand("battlefieldscorer").setTabCompleter(this::onTabComplete);
    }
}
