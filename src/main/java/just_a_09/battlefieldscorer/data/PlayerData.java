package just_a_09.battlefieldscorer.data;

import java.util.HashMap;
import java.util.Map;

public class PlayerData {
    String name;
    Map <String,Integer> ActiveData = new HashMap();
    Map <String,Integer> LifetimeData = new HashMap();

    public PlayerData(String name){
        this.name = name;
        this.ActiveData.put("Points",0);
    }
    public int getLifetimePoints(){
        return LifetimeData.get("Points");
    }
    public int getActivePoints(){
        return ActiveData.get("Points");
    }
    public Map <String,Integer>getActiveData(){
        return this.ActiveData;
    }
    public Map <String,Integer>getLifetimeData(){
        return this.LifetimeData;
    }
    public void ActiveAddPoints(int value){
        this.ActiveData.put("Points",this.ActiveData.get("Points") + value);
        this.LifetimeAddPoints(value);

    }

    public void ActiveAdd(String key,Integer value){
        Integer history = ActiveData.get(key);
        if (history!=null) {
            this.ActiveData.put(key, history + value);
        }else{
            this.ActiveData.put(key,value);
        }
    }
    public void LifetimeAdd(String key,Integer value){
        Integer history = LifetimeData.get(key);
        if (history!=null) {
            this.LifetimeData.put(key, history + value);
        }else{
            this.LifetimeData.put(key,value);
        }
    }
    public void ActiveSet(String key,int value){
        this.ActiveData.put(key,value);
    }

    public void ActiveSetPoints(int value){
        this.ActiveData.put("Points",value);
    }
    public void LifetimeSetPoints(int value){
        this.LifetimeData.put("Points",value);
    }
    public void LifetimeAddPoints(int value){
        this.LifetimeData.put("Points", LifetimeData.get("Points") + value);
    }
    public void LifetimeSet(String key,int value){
        this.LifetimeData.put(key,value);
    }
    public String getName(){return  this.name;}


}
