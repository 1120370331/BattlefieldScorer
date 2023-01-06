package just_a_09.battlefieldscorer.debug;

import just_a_09.battlefieldscorer.BattlefieldScorer;

public class debugger {
    public static boolean debugging = BattlefieldScorer.getInstance().config.getBoolean("debug");
    public static void print(String text){
        if (debugging == true){
            BattlefieldScorer.getInstance().getServer().getConsoleSender().sendMessage(text);
        }

    }
}
