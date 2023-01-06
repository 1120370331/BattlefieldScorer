package just_a_09.battlefieldscorer.data;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import org.yaml.snakeyaml.Yaml;

import java.sql.*;
import java.util.Map;
import java.util.TimeZone;
import just_a_09.battlefieldscorer.debug.debugger;

import java.sql.Connection;
import java.sql.SQLException;


public class DatabaseManager {
    private String database = null;
    private String host = null;
    private String password = null;
    private Connection connection = null;
    private Statement statement = null;
    private int port = 3306;
    private String user = null;

    Driver driver = new com.mysql.jdbc.Driver();

    public DatabaseManager(String host, int port, String user, String password, String database ) throws SQLException {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
        debugger.print(this.host+"|"+this.port+"|"+this.user+"|"+this.password+"|"+this.database);
    }
    public Map getData(){
        Yaml yaml = new Yaml();
        try {
            ResultSet datainfo = statement.executeQuery("SELECT data FROM battlefieldscorer WHERE position='1';");
            String str = null;
            while (datainfo.next()) {
                str = datainfo.getString("data");
                return yaml.load(str);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void saveData(String text){
        try {
            statement.execute("DELETE FROM battlefieldscorer WHERE position='1';");
            statement.execute("INSERT INTO battlefieldscorer (position,data) VALUES ('1','"+text+"');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            //init database
            String url = "jdbc:mysql://"+this.host+":"+this.port+"?useUnicode=true&characterEncoding=utf8";
            Connection conn = DriverManager.getConnection(url,this.user,this.password);
            Statement stm = conn.createStatement();
            stm.execute("CREATE DATABASE IF NOT EXISTS "+this.database+";");
            stm.execute("USE "+this.database+";");
            stm.execute("CREATE TABLE IF NOT EXISTS battlefieldscorer(position TEXT,data TEXT)"+";");
            stm.close();
            conn.close();
            debugger.print("[BattlefieldScorer]Connected to the database");



            //connect to database
            String url2 = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true&serverTimezone=" + TimeZone.getDefault().getID()+"&useUnicode=true&characterEncoding=utf8";
            this.connection = DriverManager.getConnection(url2,this.user,this.password);
            this.statement = this.connection.createStatement();
            debugger.print("[BattlefieldScorer]Successfully create databasemanager.");




        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    public Connection getConnection() {
        return this.connection;
    }

}
