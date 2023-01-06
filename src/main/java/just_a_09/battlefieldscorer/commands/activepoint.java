package just_a_09.battlefieldscorer.commands;

import just_a_09.battlefieldscorer.BattlefieldScorer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class activepoint implements CommandExecutor{
    private String name = "activepoint";


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            BattlefieldScorer.getInstance().getServer().getPlayer(sender.getName()).sendMessage(
                    String.valueOf(
                            BattlefieldScorer.getInstance().dataManager.getPlayer(sender.getName()).getActivePoints()));
            return true;
}}
