package just_a_09.battlefieldscorer.localer;

import just_a_09.battlefieldscorer.BattlefieldScorer;
import just_a_09.battlefieldscorer.debug.debugger;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {
    BattlefieldScorer plugin;
    Map PresentLanguage = new HashMap();
    public LanguageManager(BattlefieldScorer plugin) throws IOException {
        this.plugin = plugin;
        init();
    }
    public void init() throws IOException {
        String locale_type = plugin.config.getString("language");
        String local_path = plugin.getDataFolder().getPath() + "/languages/" + locale_type + ".yml";
        if (!(new File(local_path).exists())) {

            debugger.print("[BattlefieldScorer]Can't find your locale language:" + locale_type + ",using default language.");
            local_path = plugin.getDataFolder().getPath() + "/languages/" + locale_type + ".yml";
        }
        InputStream origin_file_inputstream = null;
        if (plugin.getResource("languages/" + locale_type + ".yml") != null) {
            origin_file_inputstream = plugin.getResource("languages/" + locale_type + ".yml");
        } else {
            debugger.print("[BattlefieldScorer]Can't find language yml in resourse,will use zh_CN instead.");
            local_path = plugin.getDataFolder().getPath() + "/languages/zh_CN.yml";
        }
        if (origin_file_inputstream != null) {

            if (!new File(local_path).exists()) {
                //If doesn't exit,copy from jar to folder.
                debugger.print("[BattlefieldScorer]" + "Didn't find local language.yml ,will create by config");
                new File(plugin.getDataFolder().getPath() + "/languages").mkdir();
                File localfile = new File(local_path);
                localfile.createNewFile();
                FileOutputStream fos = new FileOutputStream(localfile);
                fos.write(origin_file_inputstream.readAllBytes());
                fos.close();
                origin_file_inputstream.close();


            }
            File Local_Languageyml = new File(local_path);
            if (Local_Languageyml.exists()) {
                Yaml yaml = new Yaml();
                InputStream is = new FileInputStream(Local_Languageyml);
                this.PresentLanguage = yaml.load(is);
                is.close();
            } else {
                debugger.print("[BattlefieldScorer]Failed to copy language yml");
            }
        } else {
            debugger.print("[BattlefieldScorer]Cannot find default language yml");
        }
    }

    public Object get(Object key){
        return ((String)this.PresentLanguage.get(key)).replaceAll("&","§");
    }


}
