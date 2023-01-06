package just_a_09.battlefieldscorer.commands;

import just_a_09.battlefieldscorer.BattlefieldScorer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class showstates implements ICommand{
        Plugin plugin;
        CommandSender commandSender;
        Command command;
        String s;
        String[] args;
        String type;

    public showstates(String type,Plugin plugin, CommandSender commandSender, Command command, String s, String[] args){
        this.plugin = plugin;
        this.commandSender=commandSender;
        this.command = command;
        this.s = s;
        this.args = args;
        this.type = type;
        if (commandSender instanceof Player) {
            if (args.length == 2) {
                this.execute(args[1]);
            }else if(args.length == 1){
                this.execute(commandSender.getName());
            }
            else {
                commandSender.sendMessage(
                        BattlefieldScorer.getInstance()._l("INVALID_COMMAND")
                );
                commandSender.sendMessage("/battlefieldscorer <active/lifetime> <playername>");
            }
        }else {
                commandSender.sendMessage(BattlefieldScorer.getInstance()._l("INVALID_COMMAND"));
            }
        }




    public boolean execute(String name) {
        BattlefieldScorer.getInstance().dataManager.ShowStates(
                name,commandSender.getName(),type
        );

        return true;
    }
}
