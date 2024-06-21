package objMeat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class GlobPInfo {
	public int wons=0;
	public int games=0;
	public String kitC=ChatColor.BLUE+"Защитник";
	public String kitW=ChatColor.RED+"Варвар";
	public List<String> kitsC=new ArrayList<>();
	public List<String> kitsW=new ArrayList<>();
	public List<String> achs=new ArrayList<>();
	public int achPoints=0;
	String name;
	
	public GlobPInfo(){
		
	}
	public GlobPInfo(String name){
		this.name=name;
		/*if(main.con.GetText(name, "KitC")==null){
			Bukkit.getPlayer(name).sendMessage(ChatColor.RED+"Ошибка при загрузке информации...");
			return;
		}
		wons=main.con.GetInt(name, "Wons");
		games=main.con.GetInt(name, "Games");
		kitC=main.con.GetText(name, "KitC");
		kitW=main.con.GetText(name, "KitW");
		kitsC=GepUtil.stringToArrayList(main.con.GetText(name, "KitsC"));
		kitsW=GepUtil.stringToArrayList(main.con.GetText(name, "KitsW"));
		achs=GepUtil.stringToArrayList(main.con.GetText(name, "Achs"));
		achPoints=main.con.GetInt(name, "achPoints");*/
	}
	public void saveSQL(){
		/*main.con.SetInt(name, "Wons", wons);
		main.con.SetInt(name, "Games", games);
		main.con.SetText(name, "KitC", kitC);
		main.con.SetText(name, "KitW", kitW);
		main.con.SetText(name, "KitsC", GepUtil.ArrayListToString((ArrayList<String>) kitsC));
		main.con.SetText(name, "KitsW", GepUtil.ArrayListToString((ArrayList<String>) kitsW));
		main.con.SetText(name, "Achs", GepUtil.ArrayListToString((ArrayList<String>) achs));
		main.con.SetInt(name, "achPoints", achPoints);*/
	}
}
