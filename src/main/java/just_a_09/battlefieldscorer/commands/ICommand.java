package just_a_09.battlefieldscorer.commands;

import just_a_09.battlefieldscorer.BattlefieldScorer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public interface ICommand {
    Plugin plugin = null;
    CommandSender commandSender = null;
    Command command = null;
    String s = null;
    String[] args = null;


    public default boolean execute() {
        BattlefieldScorer.getInstance().reloadConfig();
        return true;
    }
}
