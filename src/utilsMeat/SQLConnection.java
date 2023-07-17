package utilsMeat;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import MeatWars.main;


public class SQLConnection {
	private Connection c;
	String host,port,login,pass,database,table;
	public void connect()
	{
		File file = new File(main.instance.getDataFolder() + File.separator + "mysql.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		if(!conf.contains("Host")){
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"No MySQL cfg.");
			conf.set("Host", "localhost");
			conf.set("User", "mysql");
			conf.set("Pass", "mysql");
			conf.set("BaseName", "gepcraft");
			GepUtil.saveCfg(conf, file);
		}
		
		host = conf.getString("Host");port = "3306";login = conf.getString("User");pass = conf.getString("Pass");database = conf.getString("BaseName");
		table = "CRPlayers";
			if (c != null)
			{
			    close();
			}
			try
			{
			    c = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database + "?useUnicode=true&characterEncoding=utf8&autoReconnect=true" , login, pass);
			}
			catch (SQLException ex)
			{
			    ex.printStackTrace();
			    GepUtil.debug("ВКЛЮЧИ БД! ENABLE BD!!!!!!!", null, "error");
			}
			
			TryToCreateTable();
	}
	public void close()
	{
	        try {
	            c.close();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	}
	public void TryToCreateTable()
	{
		Statement state;
		
		try {
			state = c.createStatement();
			state.executeUpdate("CREATE TABLE IF NOT EXISTS " + database + "." + table +
					" (PlayerName TEXT(20),KitsC TEXT,KitC TEXT,KitsW TEXT,KitW TEXT, Wons INT NOT NULL DEFAULT 0, Games INT NOT NULL DEFAULT 0, Achs TEXT, achPoints INT NOT NULL DEFAULT 0);"
					);
		} catch (SQLException e) {
			e.printStackTrace();
			GepUtil.debug("ENABLE BD! Error from method TryToCreateTable", null, "error");
		}
	}
	public void NewStats(String PlayerName)
	{
		Statement state;
		ResultSet res;
		
		try {
			state = c.createStatement(); //SELECT * FROM database.table WHERE PlayerName = 'PlayerName';
			res = state.executeQuery("SELECT * FROM " + database + "." + table + " WHERE PlayerName = '" + PlayerName + "';");
												//INSERT INTO database.table (PlayerName,KitC,KitW) VALUES('PlayerName','Защитник','Варвар');
			if(!res.next()) state.executeUpdate("INSERT INTO " + database + "." + table + " (PlayerName,KitC,KitW,KitsC,KitsW,Achs) VALUES('" + PlayerName + "','§9Защитник','§cВарвар','','','');");
			//state.executeUpdate("UPDATE " + database + "." + table + " SET Perks = '" + "" + "',maxPerks = '" + 1 + "',MinedStats = '" + "" + "',Other = '" + "FAMILY:100;CRIME:0;" + "' WHERE PlayerName = '" + PlayerName + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void SetText(String PlayerName, String col, String set)
	{
		Statement state;
		ResultSet res;
		
		try {
			state = c.createStatement();
			res = state.executeQuery("SELECT * FROM " + database + "." + table + " WHERE PlayerName = '" + PlayerName + "';");
			
			if(!res.next()) state.executeUpdate("INSERT INTO " + database + "." + table + " (PlayerName,"+col+") VALUES('" + PlayerName + "','" + set + "');");
			else state.executeUpdate("UPDATE " + database + "." + table + " SET "+col+" = '" + set + "' WHERE PlayerName = '" + PlayerName + "';");
		} catch (SQLException e) {
			e.printStackTrace();
			GepUtil.debug("ENABLE BD! Error from method SetText", null, "error");
		}
	}
	public String GetText(String name, String col)
	{
		Statement state;
		ResultSet res;
		String ret=null;
		
		try {
			if (!c.isValid(1)){
				c.close();
				connect();
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		try {
			state = c.createStatement();
			res = state.executeQuery("SELECT * FROM " + database + "." + table + " WHERE PlayerName = '" + name + "';");
			if(!res.next())ret=null;
			else ret = res.getString(col);
		} catch (SQLException e) {
			e.printStackTrace();
			GepUtil.debug("ENABLE BD! Error from method GetText", null, "error");
		}
		return ret;
	}
	public void SetInt(String PlayerName, String col, int set)
	{
		Statement state;
		ResultSet res;
		
		try {
			state = c.createStatement();
			res = state.executeQuery("SELECT * FROM " + database + "." + table + " WHERE PlayerName = '" + PlayerName + "';");
			
			if(!res.next()) state.executeUpdate("INSERT INTO " + database + "." + table + " (PlayerName,"+col+") VALUES('" + PlayerName + "','" + set + "');");
			else state.executeUpdate("UPDATE " + database + "." + table + " SET "+col+" = '" + set + "' WHERE PlayerName = '" + PlayerName + "';");
		} catch (SQLException e) {
			e.printStackTrace();
			GepUtil.debug("ENABLE BD! Error from method SetInt", null, "error");
		}
	}
	public int GetInt(String name, String col)
	{
		Statement state;
		ResultSet res;
		int ret=-1;
		
		try {
			state = c.createStatement();
			res = state.executeQuery("SELECT * FROM " + database + "." + table + " WHERE PlayerName = '" + name + "';");
			if(!res.next())ret=-2;
			else ret = res.getInt(col);
		} catch (SQLException e) {
			e.printStackTrace();
			GepUtil.debug("ENABLE BD! Error from method GetText", null, "error");
		}
		return ret;
	}
}
