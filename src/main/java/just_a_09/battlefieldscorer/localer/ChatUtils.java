package just_a_09.battlefieldscorer.localer;

import just_a_09.battlefieldscorer.BattlefieldScorer;

public class ChatUtils {
    public static String TextBuilder(String text){

        text = BattlefieldScorer.getInstance()._l("CHAT_PREFIX")+text;
        return text;
    }
}
