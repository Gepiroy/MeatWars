package utilsMeat;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class GepUtil {
	public static boolean HashMapReplacer(HashMap<String,Integer> hm, String key, int val, boolean zero, boolean set){
		if(hm.containsKey(key)){
			if(set)hm.replace(key, val);
			else hm.replace(key, hm.get(key)+val);
		}
		else{
			hm.put(key, val);
		}
		if(zero){
			if(hm.get(key)<=0){
				hm.remove(key);
				return true;
			}
		}
		return false;
	}
	public static boolean HashMapReplacer(HashMap<Location,Integer> hm, Location key, int val, boolean zero, boolean set){
		if(hm.containsKey(key)){
			if(set)hm.replace(key, val);
			else hm.replace(key, hm.get(key)+val);
		}
		else{
			hm.put(key, val);
		}
		if(zero){
			if(hm.get(key)<=0){
				hm.remove(key);
				return true;
			}
		}
		return false;
	}
	public static boolean HashMapCounter(HashMap<String,Integer> hm, String key, int detect, boolean reset){
		HashMapReplacer(hm, key, 1, false, false);
		if(hm.get(key)>=detect){
			if(reset)hm.remove(key);
			return true;
		}
		return false;
	}
	public static void globMessage(String mes, Sound sound, float vol, float speed, String title, String subtitle, int spawn, int hold, int remove){
		for(Player p:Bukkit.getOnlinePlayers()){
			if(mes!=null)p.sendMessage(mes);
			if(sound!=null){
				p.playSound(p.getLocation(), sound, vol, speed);
			}
			if(title!=null&&subtitle!=null) {
				p.sendTitle(title, subtitle, spawn, hold, remove);
			}
		}
	}
	public static void globMessage(String mes){
		globMessage(mes, null, 0, 0, null, null, 0, 0, 0);
	}
	public static ChatColor boolCol(boolean arg){
		if(arg)return ChatColor.GREEN;
		else return ChatColor.RED;
	}
	public static ChatColor boolCol(ChatColor Tcolor, ChatColor Fcolor, boolean arg){
		if(arg)return Tcolor;
		else return Fcolor;
	}
	public static ChatColor numCol(ChatColor[] colors, int[] ints, int in){
		for(int i=0;i<colors.length;i++){
			if(in<=ints[i])return colors[i];
		}
		return ChatColor.RED;
	}
	public static String boolString(String True, String False, boolean arg){
		if(arg)return True;
		else return False;
	}
	public static void debug(String message, String whoCaused, String type){
		String prefix = ChatColor.GRAY+"[DEBUG";
		if(whoCaused!=null)prefix+="(from "+ChatColor.YELLOW+whoCaused+ChatColor.GRAY+")";
		prefix+="]";
		if(type.equals("error"))prefix+=ChatColor.RED;
		if(type.equals("info"))prefix+=ChatColor.AQUA;
		if(Bukkit.getPlayer("Gepiroy")!=null){
			Bukkit.getPlayer("Gepiroy").sendMessage(prefix+message);
		}
		Bukkit.getConsoleSender().sendMessage(prefix+message);
	}
	public static boolean chance(int ch){
		return new Random().nextInt(100)+1<=ch;
	}
	public static boolean chance(double ch){
		return new Random().nextDouble()<=ch;
	}
	public static String chances(String[] sts, double[] chs){
		double r = new Random().nextInt(100)+new Random().nextDouble();
		double ch = 0.000;
		for(int i=0;i<sts.length;i++){
			if(r>ch&&r<=ch+chs[i]){
				return sts[i];
			}
			ch+=chs[i];
		}
		return "";
	}
	public static String chancesByCoef(String[] sts, int[] coefs){
		int coef=0;
		for(int d:coefs){
			coef+=d;
		}
		int r = new Random().nextInt(coef);
		int ch = 0;
		for(int i=0;i<sts.length;i++){
			if(r>=ch&&r<=ch+coefs[i]){
				return sts[i];
			}
			ch+=coefs[i];
		}
		return ""+r;
	}
	public static boolean itemName(ItemStack item, String name) {
		if(!item.hasItemMeta())return false;
		if(!item.getItemMeta().hasDisplayName())return false;
		if(item.getItemMeta().getDisplayName().equals(name))return true;
		return false;
	}
	public static boolean isItem(ItemStack item, String name, String lore, Material mat){
		if(item==null||!item.hasItemMeta()){
			return false;
		}
		if(mat!=null&&!item.getType().equals(mat)){
			return false;
		}
		if(!item.getItemMeta().hasDisplayName()&&name!=null){
			return false;
		}
		if(!item.getItemMeta().hasLore()&&lore!=null){
			return false;
		}
		if(name!=null&&!item.getItemMeta().getDisplayName().contains(name)){
			return false;
		}
		if(lore!=null){
			for(String st:item.getItemMeta().getLore()){
				if(st.contains(lore)){
					return true;
				}
			}
			return false;
		}
		return true;
	}
	public static boolean isFullyItem(ItemStack item, String name, String lore, Material mat){
		if(item==null){
			return false;
		}
		if(mat!=null&&!item.getType().equals(mat)){
			return false;
		}
		if(!item.hasItemMeta()){
			return false;
		}
		if(!item.getItemMeta().hasDisplayName()){
			return false;
		}
		if(!item.getItemMeta().hasLore()){
			return false;
		}
		if(name!=null&&!item.getItemMeta().getDisplayName().equals(name)){
			return false;
		}
		if(lore!=null){
			for(String st:item.getItemMeta().getLore()){
				if(st.contains(lore)){
					return true;
				}
			}
			return false;
		}
		return true;
	}
	public static boolean isFullyItem(ItemStack item, ItemStack otherItem){
		if(item==null||otherItem==null){
			//globMessage("item null", null, 0, 0, null, null, 0, 0, 0);
			return false;
		}
		if(!item.getType().equals(otherItem.getType())){
			//globMessage("mat not eq, mat1="+item.getType()+",mat2="+otherItem.getType(), null, 0, 0, null, null, 0, 0, 0);
			return false;
		}
		if(!item.hasItemMeta()||!otherItem.hasItemMeta()){
			//globMessage("item no have meta", null, 0, 0, null, null, 0, 0, 0);
			return false;
		}
		if(!item.getItemMeta().hasDisplayName()){
			//globMessage("item no have displayname", null, 0, 0, null, null, 0, 0, 0);
			return false;
		}
		if(!item.getItemMeta().hasLore()){
			//globMessage("item no have lore", null, 0, 0, null, null, 0, 0, 0);
			return false;
		}
		if(otherItem.getItemMeta().hasDisplayName()&&!item.getItemMeta().getDisplayName().equals(otherItem.getItemMeta().getDisplayName())){
			//globMessage("displaynames not equal", null, 0, 0, null, null, 0, 0, 0);
			return false;
		}
		if(otherItem.getItemMeta().hasLore()&&!item.getItemMeta().getLore().equals(otherItem.getItemMeta().getLore())){
			//globMessage("lores not eq", null, 0, 0, null, null, 0, 0, 0);
			return false;
		}
		//globMessage("ÓÐÀ ÁËßÒÜ!", null, 0, 0, null, null, 0, 0, 0);
		return true;
	}
	public static ArrayList<String> stringToArrayList(String st){
		ArrayList<String> ret = new ArrayList<>();
		String toadd = "";
		for(int i=0;i<st.length();i++){
			String c = st.charAt(i)+"";
			if(!c.equals(";")){
				toadd=toadd+c;
			}
			else{
				ret.add(toadd);
				toadd="";
			}
		}
		return ret;
	}
	public static String ArrayListToString(ArrayList<String> ara){
		String ret = "";
		for(String st:ara){
			ret = ret+st+";";
		}
		return ret;
	}
	public static String leader(HashMap<String,Integer> leaders, String retType){
		String maxp = "";
		int max = 0;
		for(Entry<String, Integer> ES:leaders.entrySet()){
			if(max<=ES.getValue()){
				max=ES.getValue();
				maxp=ES.getKey();
			}
		}
		if(maxp.equals(""))return ChatColor.RED+"...";
		else if(retType.equals("name"))return maxp;
		else if(retType.equals("score"))return ""+max;
		else return maxp+ChatColor.YELLOW+" ("+max+")";
	}
	public static void upscor(Player p, List<String> strings, String borders){
		Scoreboard s = p.getScoreboard();
		for(String e:s.getEntries()){
			s.resetScores(e);
		}
		Objective o = s.getObjective("stats");
		int i=strings.size();
		o.getScore(ChatColor.RED+borders).setScore(i+1);
		for(String st:strings){
			o.getScore(st).setScore(i);
			i--;
		}
		o.getScore(ChatColor.BLUE+borders).setScore(i);
	}
	public static HashMap<String,Integer> drops(int points, String[] canDrop, int[] ams, double[] ch, int coef){
		HashMap<String,Integer> drops = new HashMap<>();
		while(new Random().nextInt(coef)<=points){
			points-=coef;
			double r = new Random().nextDouble();
			double nowch = 0.000;
			for(int i=0;i<canDrop.length;i++){
				if(r>nowch&&r<nowch+ch[i]){
					HashMapReplacer(drops, canDrop[i], ams[i], false, false);
				}
				else nowch+=ch[i];
			}
		}
		return drops;
	}
	public static boolean isNumeric(String str)
	{
	  NumberFormat formatter = NumberFormat.getInstance();
	  ParsePosition pos = new ParsePosition(0);
	  formatter.parse(str, pos);
	  return str.length() == pos.getIndex();
	}
	public static int intFromString(String st){
		String rets = "";
		int ret = 0;
		boolean negative=false;
		boolean ignore=false;
		for(int i=0;i<st.length();i++){
			if(!ignore&&isNumeric(st.charAt(i)+"")){
				rets = rets+st.charAt(i)+"";
			}
			if((st.charAt(i)+"").equals("-"))negative=true;
			if((st.charAt(i)+"").equals("§")||(st.charAt(i)+"").equals("&"))ignore=true;
			else ignore=false;
		}
		ret = Integer.parseInt(rets);
		if(negative)ret=-ret;
		return ret;
	}
	public static int intFromLore(ItemStack item,String str){
		String rets = "";
		int ret = 0;
		boolean negative=false;
		if(!item.hasItemMeta())return 0;
		if(!item.getItemMeta().hasLore())return 0;
		for(String st:item.getItemMeta().getLore()){
			if(st.contains(str)){
				boolean ignore=false;
				for(int i=0;i<st.length();i++){
					if(!ignore&&isNumeric(st.charAt(i)+"")){
						rets = rets+st.charAt(i)+"";
					}
					if((st.charAt(i)+"").equals("-"))negative=true;
					if((st.charAt(i)+"").equals("§")||(st.charAt(i)+"").equals("&"))ignore=true;
					else ignore=false;
				}
			}
		}
		if(!rets.equals(""))ret = Integer.parseInt(rets);
		if(negative)ret=-ret;
		return ret;
	}
	public static boolean LoreContains(ItemStack item,String[] lores){
		if(!item.hasItemMeta())return false;
		if(!item.getItemMeta().hasLore())return false;
		for(String lor:lores){
			boolean yes = false;
			for(String st:item.getItemMeta().getLore()){
				if(st.contains(lor)){
					yes=true;
					break;
				}
			}
			if(!yes)return false;
		}
		return true;
	}
	public static double sellAll(Player p,Material mat, double price){
		double total=0;
		for(ItemStack item:p.getInventory()){
			if(item!=null&&item.getType().equals(mat)){
				total+=price*item.getAmount();
				item.setAmount(0);
			}
		}
		return total;
	}
	public static void sellItems(Player p, Material mat, String name, int am){
		for(ItemStack item:p.getInventory()){
			if(item!=null&&item.getType().equals(mat)){
				if(name!=null&&!item.hasItemMeta())continue;
				if(!item.getItemMeta().getDisplayName().contains(name))continue;
				if(item.getAmount()<=am){
					am-=item.getAmount();
					item.setAmount(0);
				}
				else{
					item.setAmount(item.getAmount()-am);
					return;
				}
			}
		}
	}
	public static void saveCfg(FileConfiguration conf, File file) {
	    try {
	        conf.save(file);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public static void saveLocToConf(FileConfiguration conf, String st, Location loc){
		if(loc==null)return;
		conf.set(st+".world",loc.getWorld().getName());
		conf.set(st+".x",loc.getX());
		conf.set(st+".y",loc.getY());
		conf.set(st+".z",loc.getZ());
	}
	public static Location getLocFromConf(FileConfiguration conf, String st){
		if(!conf.contains(st)){
			debug("no loc "+st+"in config!",null,"error");
			return null;
		}
		return new Location(Bukkit.getWorld(conf.getString(st+".world")),conf.getDouble(st+".x"),conf.getDouble(st+".y"),conf.getDouble(st+".z"));
	}
	public static String locInfo(Location loc){
		return "("+loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ()+")";
	}
	public static String CylDouble(double d, String cyl){
		return new DecimalFormat(cyl).format(d).replaceAll(",", ".");
	}
	public static Location nearest(Location loc, List<Location> locs){
		double dist = 10000;
		Location ret = null;
		for(Location l:locs){
			if(loc.distance(l)<dist){
				dist=loc.distance(l);
				ret=l;
			}
		}
		return ret;
	}
	public static boolean haveItem(Player p, Material mat, int am){
		if(countItem(p, mat)>=am)return true;
		return false;
	}
	public static int countItem(Player p, Material mat){
		int am=0;
		for(ItemStack item:p.getInventory()){
			if(item!=null&&item.getType().equals(mat)){
				am+=item.getAmount();
			}
		}
		return am;
	}
	public static boolean insqr(Location l, double r, Location p){
		if(l.getX()-r<=p.getX()&&l.getX()+r>=p.getX()&&l.getZ()-r<=p.getZ()&&l.getZ()+r>=p.getZ()){
			return true;
		}
		return false;
	}
}