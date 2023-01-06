package just_a_09.battlefieldscorer.data;

import just_a_09.battlefieldscorer.BattlefieldScorer;
import just_a_09.battlefieldscorer.debug.debugger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public class DataManager {
    //data path of the plugin
    public String path ;
    public enum PlayerDataKeys{
        Points("Points"),
        KilledPlayers("KilledPlayers"),
        BrokeBlocks("BrokeBlocks"),
        KillAssistments("KillAssistments"),
        DeathTimes("DeathTimes"),
        ArrowHits("ArrowHits"),
        ArrowShoots("ArrowShoots"),
        EatenFoods("EatenFoods"),
        TotalHits("TotalHits"),
        PlaceBlocks("PlaceBlocks"),
        BedwarsPlayed("BedwarsPlayed"),
        BedwarsBedDestroyed("BedwarsBedDestroyed"),
        ;
        private String name;
        PlayerDataKeys(String name){
            this.name=name;
        }
        public String getName(){
            return this.name;
        }
        public boolean hasName(String name){
            if (this.name.equals(name)){
                return true;
            }else{
                return false;
            }
        }
    }

    public PlayerDataKeys[] getDataKeys(){
        return PlayerDataKeys.values();
    }


    public Map<String,PlayerData> players = new HashMap<String,PlayerData>();
    public boolean UseDatabase = BattlefieldScorer.getInstance().config.getBoolean("using_database");
    //save lifetime information
    public void save(PlayerData player){

        Yaml yaml = new Yaml();
        Map data = get();
        if (data.keySet().contains(player.name)) {
            data.replace(player.name, player.getLifetimePoints());
        }else{
            data.put(player.name,player.getLifetimePoints());
        }



        String str = yaml.dump(data);
        try {

            BufferedWriter fl = new BufferedWriter( new FileWriter(this.path));
            fl.write(str);
            fl.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void save(Map map){
        Yaml yaml = new Yaml();
        String str = yaml.dump(map);
        try {

            BufferedWriter fl = new BufferedWriter( new FileWriter(this.path));
            fl.write(str);
            fl.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean useSQL ;
    public void save(){
        Yaml yaml = new Yaml();
        Map playerset = new HashMap();//completed information
        Map playerdata = new HashMap();




        if (!useSQL){

            try {
                for (String i:this.players.keySet()){

                    playerdata.put("LifetimeData",this.players.get(i).getLifetimeData());
                    debugger.print("[bwbs]playerdetails:"+yaml.dump(this.players.get(i).getLifetimeData()));
                    playerset.put(i,playerdata);
                    debugger.print("[bwbs]attemp to put in set:"+yaml.dump(playerdata));
                    debugger.print("[bwbs]playerset:"+yaml.dump(playerset));
                    playerdata = new HashMap();

                }
                String str = yaml.dump(playerset);
                BufferedWriter fl = new BufferedWriter( new FileWriter(this.path));
                fl.write(str);
                fl.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            //if getted a player doesn't exist,it won't be deleted
            Map temp = get();
            Map unexist_player = new HashMap();
            for (Object name:temp.keySet()){
                if (!players.containsKey((String)name)){
                    PlayerData ExPlayer = new PlayerData((String)name);
                    ExPlayer.LifetimeData = (Map)temp.get(name);
                    players.put((String)name,ExPlayer);

                }
                }
            for (String i:this.players.keySet()){

                    playerdata.put("LifetimeData",((PlayerData)this.players.get(i)).LifetimeData);
                    debugger.print("[bwbs]playerdetails:"+yaml.dump(((PlayerData)this.players.get(i)).LifetimeData));
                    playerset.put("O"+i,playerdata);
                    debugger.print("[bwbs]attemp to put in set:"+yaml.dump(playerdata));
                    debugger.print("[bwbs]playerset:"+yaml.dump(playerset));
                    playerdata = new HashMap();

                }
            String str = yaml.dump(playerset);
            BattlefieldScorer.getInstance().databaseManager.saveData(str);
            }


        }

    public DataManager(String path){

        this.path = path;
        File file = new File(path);
        if (!file.exists()){
            try {
                BufferedWriter bf = new BufferedWriter(new FileWriter(path));
                bf.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        this.useSQL = BattlefieldScorer.getInstance().config.getBoolean("using_database");
        Map data = get();
        if (data!=null){
            for (Object name:data.keySet()){
                newPlayer((String) name,false);
            }
        }


    }

    public Object readFile(){
        return null;
    };

    public static String deleteCharString0(String sourceString, char chElemData) {
        String deleteString = "";
        for (int i = 0; i < sourceString.length(); i++) {
            if (sourceString.charAt(i) != chElemData ) {
                deleteString += sourceString.charAt(i);
            }
        }
        return deleteString;
    }


    public Map get(){
        Map map = new HashMap();
        Yaml yaml = new Yaml();
        Map data = new HashMap();



        if (!useSQL){
            try {
                InputStream inputstream = new FileInputStream(this.path);

                data = yaml.load(inputstream);
                if (data != null) {
                    inputstream.close();
                    return data;
                }else {
                    inputstream.close();
                    return null;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }else{
            Map temp = BattlefieldScorer.getInstance().databaseManager.getData();
            if (temp!=null) {
                for (Object i : temp.keySet()) {
                    data.put(((String)i).substring(1) , temp.get(i));//delete '@' in the string i
                }
            }else{
                debugger.print("[BWSR]Database got null.");
            }
        }

        return data;
    }
    /*return by PlayerData.yml*///public int get(String key){Map <String, Integer> data = get();if (data != null) {if (data.containsKey(key)) {if (data.get(key) != null)return (int) data.get(key);else return 0;}else return 0;}else return 0;}
    public Map getLifetimeFromDatabase(String name){
        Map data = get();
        if (data!=null) {
            Map playerdata = (Map) data.get(name);
            if (playerdata!=null){
                return (Map)playerdata.get("LifetimeData");
            }else{
                debugger.print("[BWBS]getLifeFromDatabase return null");
                return null;
            }
        }else {
            return null;
        }


    }

    public void setPlayer(PlayerData player){
        if (this.players.containsKey(player.name)){
            this.players.replace(player.name,player);
        }else{
            this.players.put(player.name,player);
        }
    }

    public void newPlayer(String name,boolean Save){
        //if exists that would do nothing.
        if (!players.containsKey(name)){
            PlayerData player = new PlayerData(name);


            Map LifetimeInfo = getLifetimeFromDatabase(name);
            //If has data in PlayerData.yml,use it.


            this.players.put(name,player);
            ActiveClear(name);
            if (LifetimeInfo!=null){
                player = players.get(name);
                for (Object i : LifetimeInfo.keySet()){
                    player.LifetimeData.put((String)i, (Integer) LifetimeInfo.get(i));
                }
                //player.LifetimeData = LifetimeInfo;
                players.put(name , player);
            }
            else{
                LifetimeClear(name);

            }
            ;
            if (Save) {
                save();
            }
        }
        CompleteData(name);
    }
    public PlayerData getPlayer(String name){
        return this.players.get(name);
    }

    public void ClearList(){
        players = new HashMap<String,PlayerData>();

    }
    //it actually for cleaning active infos.
    public void removePlayer(String name){
        players.remove(name);
    }


    public void addLifetimePoints(String name, int value){
        newPlayer(name,true);
        PlayerData player = players.get(name);
        player.LifetimeAddPoints(value);
        players.replace(name,player);
        save();
    }
    public void setLifetimePoints(String name, int value){
        newPlayer(name,true);
        PlayerData player = players.get(name);
        player.LifetimeSetPoints(value);
        players.replace(name,player);
        save();
    }
    public void addActivePoints(String name, int value){
        newPlayer(name,true);
        PlayerData player = players.get(name);
        player.ActiveAddPoints(value);
        players.replace(name,player);
    }
    public void addActive(String name,PlayerDataKeys pdk,int value){
        newPlayer(name,false);
        String key = pdk.getName();
        PlayerData player = players.get(name);
        player.ActiveData.put(key,player.ActiveData.get(key)+value);

        players.put(name,player);
        addLifetime(name,pdk,value);
    }
    public void addLifetime(String name,PlayerDataKeys pdk,int value){
        String key=pdk.getName();
        newPlayer(name,false);
        PlayerData player = players.get(name);
        player.LifetimeData.put(key,player.LifetimeData.get(key)+value);
        players.put(name,player);
        save();
    }
    public void setActivePoints(String name, int value){
        newPlayer(name,true);
        PlayerData player = players.get(name);
        player.ActiveSetPoints(value);
        players.replace(name,player);
    }

    public Map setMapIfNull(Map map,Object key,Object obj){
        if (map.get(key)==null){
            map.put(key,obj);
            return map;
        }else {
            return map;
        }
    }
    public void CompleteData(String name) {
        PlayerData player = players.get(name);
        PlayerDataKeys[] dks = PlayerDataKeys.values();
        List list = Arrays.asList(dks);
        Map <String,Map>datas = new HashMap() {{
            put("LifetimeData", player.LifetimeData);
            put("ActiveData", player.ActiveData);
        }};

        for (String i : datas.keySet()) {

            Map data = (Map) datas.get(i);

            //initlize not searched datakeys
            for (PlayerDataKeys dk : dks) {
                String key = dk.getName();
                if (!data.containsKey(key)) {
                    data.put(key, 0);
                }
            }


            //check if the data has invalid value;
            boolean inlist = false;
            Set keyset = data.keySet();
            Map data2 = new HashMap();
            data2.putAll(data);

            for (Object key : keyset) {
                for (PlayerDataKeys dk : dks) {
                    if (dk.hasName((String)key)) {
                        inlist = true;
                    }
                }
                if (inlist) {//if in the list,don't do anything to it.

                } else {//else delete the data
                    data2.remove(key);
                }

                inlist = false;
            }
            datas.put(i,data2);

        }
        player.ActiveData=(Map)datas.get("ActiveData");
        player.LifetimeData=(Map)datas.get("LifetimeData");
        players.put(name,player);
    }


    

    public void ActiveClear(String name){
        PlayerData player = players.get(name);
        player.ActiveData = new HashMap<String,Integer>();
        for (PlayerDataKeys dt:PlayerDataKeys.values()){

            player.ActiveData.put(dt.getName(),0);
        }

        players.put(name,player);
        save();
    }
    public void LifetimeClear(String name){
        PlayerData player = players.get(name);
        player.LifetimeData = new HashMap<String,Integer>();
        for (PlayerDataKeys dt:PlayerDataKeys.values()){

            player.LifetimeData.put(dt.getName(),0);
        }

        players.put(name,player);
        save();
    }
    //type = Lifetime or Active
    public void ShowStates(String name,String targetplayer,String type){
        PlayerData playerdata = this.players.get(name);
        if (playerdata!=null) {
            String[] text = null;
            ArrayList<String> array;
            if (type.equals("Lifetime")) {
                array = new ArrayList<String>();

                array.add("-------------" + name + "(" +
                        BattlefieldScorer.getInstance()._l("LIFETIME")
                        + ")-------------");

                for (String key : playerdata.LifetimeData.keySet()) {

                        String dkey = "DATA_" + key;
                        array.add(
                                BattlefieldScorer.getInstance()._l(dkey.toUpperCase())
                                        + ": "
                                        + playerdata.LifetimeData.get(key)
                        );


                }
                array.add("-----------------------------");
                text = (String[]) array.toArray(new String[0]);
                BattlefieldScorer.getInstance().getServer().getPlayer(targetplayer).sendMessage(text);

            } else if (type.equals("Active")) {
                array = new ArrayList<String>();
                array.add("-------------" + name + "(" +
                        BattlefieldScorer.getInstance()._l("ACTIVE")
                        + ")-------------");

                for (String key : playerdata.ActiveData.keySet()) {
                    if (playerdata.ActiveData.get(key)>0) {
                        String dkey = "DATA_" + key;
                        debugger.print(dkey.toUpperCase());
                        array.add(
                                BattlefieldScorer.getInstance()._l(dkey.toUpperCase())
                                        + ": "
                                        + playerdata.ActiveData.get(key)
                        );
                    }

                }
                array.add("-----------------------------");
                text = (String[]) array.toArray(new String[0]);
                BattlefieldScorer.getInstance().getServer().getPlayer(targetplayer).sendMessage(text);
            }
        }else {
            BattlefieldScorer.getInstance().getServer().getPlayer(targetplayer).sendMessage(BattlefieldScorer.getInstance()._l("STATES_NOTFOUND"));
        }


    }

}
