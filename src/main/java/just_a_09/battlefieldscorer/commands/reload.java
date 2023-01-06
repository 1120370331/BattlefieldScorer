package just_a_09.battlefieldscorer.commands;

import just_a_09.battlefieldscorer.BattlefieldScorer;
import just_a_09.battlefieldscorer.localer.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Bat;
import org.bukkit.plugin.Plugin;

public class reload implements ICommand{
    Plugin plugin;
    CommandSender commandSender;
    Command command;
    String s;
    String[] args;

    public reload(Plugin plugin, CommandSender commandSender, Command command, String s, String[] args){
        this.plugin = plugin;
        this.commandSender=commandSender;
        this.command = command;
        this.s = s;
        this.args = args;
        this.execute();

    }


    public boolean execute() {
        commandSender.sendMessage(ChatUtils.TextBuilder(BattlefieldScorer.getInstance()._l("COMMAND_RELOAD")));

        BattlefieldScorer.getInstance().localreloadConfigs();
        BattlefieldScorer.getInstance().loadLanguageManager();
        return true;
    }
}
