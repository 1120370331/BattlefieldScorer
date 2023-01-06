package just_a_09.battlefieldscorer;
import just_a_09.battlefieldscorer.commands.Executor;
import just_a_09.battlefieldscorer.listeners.*;
import just_a_09.battlefieldscorer.data.DataManager;
import just_a_09.battlefieldscorer.data.DatabaseManager;
import just_a_09.battlefieldscorer.localer.LanguageManager;
import just_a_09.battlefieldscorer.debug.debugger;
import just_a_09.battlefieldscorer.utils.TabList;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.sql.SQLException;

public class BattlefieldScorer extends JavaPlugin {

    public FileConfiguration config = null;

    public LanguageManager languageManager = null;

    public static BattlefieldScorer instance = null;

    public static BattlefieldScorer getInstance(){
        return instance;
    }
    public TabList tabList = null;
    public DataManager dataManager = null;
    public DatabaseManager databaseManager = null;





    public String getFallbackLocale() {
        return config.getString("languages");
    }

    public String _l(String key) {
        if (this.languageManager.get((Object) key)!=null){
            String str =  (String) this.languageManager.get((Object) key);
            return str;
        }else{
            return "LOCALE_NOT_FOUND";
        }
    }
    public void loadLanguageManager() {
        try {
            this.languageManager = new LanguageManager(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadDataManager(){

        this.dataManager = new DataManager(getInstance().getDataFolder().getPath()+"/PlayerData.yml");

    }

    public void loadListeners(){

        getServer().getPluginManager().registerEvents(new onPlayerEvents(),this);
        getServer().getPluginManager().registerEvents(new onBedwarsGame(),this);
        getServer().getPluginManager().registerEvents(new onServerEvents(),this);
    }

    public void loadDatabaseManager() throws SQLException {

        String host = config.getString("database.host");
        int port = config.getInt("database.port");
        String user = config.getString("database.user");
        String password = config.getString("database.password");
        String database = config.getString("database.database");

        this.databaseManager = new DatabaseManager(
                host,port,user,password,database
        );
        databaseManager.initialize();

    }

    public void loadCommands(){
        this.getCommand("battlefieldscorer").setExecutor(new Executor(this));



        //list all possible tab arguments




    }
    public void localreloadConfigs(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(this.getDataFolder(),"config.yml")),"UTF-8"));
            FileConfiguration conf = YamlConfiguration.loadConfiguration(reader);
            this.config = conf;
            reloadConfig();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onEnable() {
        instance = this;
        this.config = getConfig();
        this.saveDefaultConfig();
        debugger.print("[BWBS]插件被加载。");
        if (config.getBoolean("using_database")) {
            try {
                this.loadDatabaseManager();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.loadLanguageManager();
        this.loadDataManager();
        this.loadListeners();
        this.loadCommands();
        this.tabList = new TabList();




        //every 0.02s run
        new BukkitRunnable(){

            @Override
            public void run() {

                tabList.Refresh();

            }
        }.runTaskTimer(this,0,1);
        // Plugin startup logic
        debugger.print("[BWBS]插件加载完毕。");
        debugger.print(_l("CHAT_PREFIX"));


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
