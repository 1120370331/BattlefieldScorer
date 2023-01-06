package just_a_09.battlefieldscorer.commands;

import just_a_09.battlefieldscorer.BattlefieldScorer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class help implements ICommand{
    Plugin plugin;
    CommandSender commandSender;
    Command command;
    String s;
    String[] args;

    public help(Plugin plugin, CommandSender commandSender, Command command, String s, String[] args){
        this.plugin = plugin;
        this.commandSender=commandSender;
        this.command = command;
        this.s = s;
        this.args = args;
        this.execute();

    }


    public boolean execute() {
        ArrayList<String> str = new ArrayList();
        str.add("--------"+BattlefieldScorer.getInstance()._l("CHAT_PREFIX")+BattlefieldScorer.getInstance()._l("COMMANDINFO")+"--------");
        if (commandSender.hasPermission("battlefieldscorer.base")) {
            str.add(BattlefieldScorer.getInstance()._l("COMMANDINFO_HELP"));
            str.add(BattlefieldScorer.getInstance()._l("COMMANDINFO_STATES"));
        }
        if (commandSender.hasPermission("battlefieldscorer.admin")){
            str.add(BattlefieldScorer.getInstance()._l("COMMANDINFO_CLEAN"));
            str.add(BattlefieldScorer.getInstance()._l("COMMANDINFO_RELOAD"));
        }
        str.add("--------------------------------");
        commandSender.sendMessage(str.toArray(new String[0]));
        return true;
    }
}
