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

public class BoostCon {
	private Connection c;
	String host,port,login,pass,database,table;
	public String server;
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
		table = "Boosts";
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
			    GepUtil.debug("������ ��! ENABLE BD!!!!!!!", null, "error");
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
					" (Name TEXT,Server TEXT,Owner TEXT,Date BIGINT,End BIGINT);"
					);
		} catch (SQLException e) {
			e.printStackTrace();
			GepUtil.debug("ENABLE BD! Error from method TryToCreateTable", null, "error");
		}
	}
	public void NewStats(String name, String server, String owner, Long date, Long end)
	{
		Statement state;
		
		try {
			state = c.createStatement();
			state.executeUpdate("INSERT INTO " + database + "." + table + " (Name,Server,Owner,Date,End) VALUES('"+name+"','"+server+"','"+owner+"','"+date+"','"+end+"');");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void SetText(String col, String set)
	{
		Statement state;
		ResultSet res;
		
		try {
			state = c.createStatement();
			res = state.executeQuery("SELECT * FROM " + database + "." + table + " WHERE BungeeName = '" + server + "';");
			
			if(!res.next()) state.executeUpdate("INSERT INTO " + database + "." + table + " (BungeeName,"+col+") VALUES('" + server + "','" + set + "');");
			else state.executeUpdate("UPDATE " + database + "." + table + " SET "+col+" = '" + set + "' WHERE BungeeName = '" + server + "';");
		} catch (SQLException e) {
			e.printStackTrace();
			GepUtil.debug("ENABLE BD! Error from method SetText", null, "error");
		}
	}
	public String GetText(String col)
	{
		Statement state;
		ResultSet res;
		String ret=null;
		
		try {
			state = c.createStatement();
			res = state.executeQuery("SELECT * FROM " + database + "." + table + " WHERE BungeeName = '" + server + "';");
			if(!res.next())ret=null;
			else ret = res.getString(col);
		} catch (SQLException e) {
			e.printStackTrace();
			GepUtil.debug("ENABLE BD! Error from method GetText", null, "error");
		}
		return ret;
	}
	public void SetInt(String col, int set)
	{
		Statement state;
		ResultSet res;
		
		try {
			state = c.createStatement();
			res = state.executeQuery("SELECT * FROM " + database + "." + table + " WHERE BungeeName = '" + server + "';");
			
			if(!res.next()) state.executeUpdate("INSERT INTO " + database + "." + table + " (BungeeName,"+col+") VALUES('" + server + "','" + set + "');");
			else state.executeUpdate("UPDATE " + database + "." + table + " SET "+col+" = '" + set + "' WHERE BungeeName = '" + server + "';");
		} catch (SQLException e) {
			e.printStackTrace();
			GepUtil.debug("ENABLE BD! Error from method SetInt", null, "error");
		}
	}
	public int GetInt(String col)
	{
		Statement state;
		ResultSet res;
		int ret=-1;
		
		try {
			state = c.createStatement();
			res = state.executeQuery("SELECT * FROM " + database + "." + table + " WHERE BungeeName = '" + server + "';");
			if(!res.next())ret=-2;
			else ret = res.getInt(col);
		} catch (SQLException e) {
			e.printStackTrace();
			GepUtil.debug("ENABLE BD! Error from method GetText", null, "error");
		}
		return ret;
	}
}
