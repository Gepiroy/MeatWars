package objMeat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import MeatWars.main;
import utilsMeat.GepUtil;

public class PlayerInfo {
	public int money=0;
	public int sMoney=0;
	public int keys=10;
	public HashMap<String, Integer> timers = new HashMap<>();
	public HashMap<String, Integer> waits = new HashMap<>();
	public int kills=0;
	public int deaths=0;
	public ArrayList<String> bools = new ArrayList<>();
	public Location lastClickedBlock;
	public String lastHurted="";
	public HashMap<String, Integer> ups = new HashMap<>();
	public int regen=0;
	public int attack=0;
	public Location steal=null;
	public String name;
	
	public PlayerInfo(Player p){
		if(p!=null){
			name=p.getName();
			p.sendMessage("PI loaded.");
		}
		ups.put("steal", 1);
		ups.put("waveGold", 0);
		ups.put("regen", 0);
		ups.put("speedTime", 0);
		ups.put("axeRush", 0);
		ups.put("axeReload", 0);
		ups.put("jailTime", 0);
		ups.put("killGold", 0);
		ups.put("torch", 0);
	}
	
	public boolean toggleBool(String st){
		return toggleBool(st, !bools.contains(st));
	}
	public boolean toggleBool(String st, boolean set){
		if(bools.contains(st)&&!set){
			bools.remove(st);
		}
		else if(!bools.contains(st)&&set){
			bools.add(st);
		}
		return bools.contains(st);
	}
	public void sTrans(Player p, int get){
		boolean isCastle=main.castle.players.contains(p.getName());
		Team t=null;
		Team et=null;
		if(isCastle){t=main.castle;et=main.wars;}
		else{t=main.wars;et=main.castle;}
		if(t.players.size()<et.players.size()){
			double coef=1.0*et.players.size()/t.players.size();
			p.sendMessage(ChatColor.GREEN+"Вы получили x"+GepUtil.CylDouble(coef, "#0.00")+" золота "+ChatColor.GRAY+"("+ChatColor.BLUE+get*coef+ChatColor.GRAY+" вместо "+get+") "+ChatColor.GOLD+"(ваша команда меньше)");
			money+=get*coef;
		}
		else money+=get;
		if(main.getKit(p.getName(), true).equals(ChatColor.GOLD+"Экономист")&&main.castle.players.contains(p.getName())){
			p.sendMessage(ChatColor.GREEN+"Вы получили "+(int)(get*0.1)+" бонусного золота "+ChatColor.GOLD+"(Экономист)");
			money+=get*0.1;
		}
	}
	public boolean attack(int points){
		attack+=points;
		if(timers.containsKey("axeRush")){
			attack+=points;
		}
		if(attack>=20){
			attack-=20;
			return true;
		}
		else return false;
	}
	public boolean attack(){
		attack+=1;
		if(attack>=20){
			attack-=20;
			return true;
		}
		else return false;
	}
	public double respTime(Player p){
		double ret = 3;
		boolean isCastle=main.castle.players.contains(p.getName());
		Team t=null;
		Team et=null;
		if(isCastle){t=main.castle;et=main.wars;}
		else{t=main.wars;et=main.castle;}
		if(t.players.size()>et.players.size()){
			ret*=(1.0*t.players.size()/et.players.size());
		}
		return ret;
	}
	public boolean steal(Player p){
		if(steal==null)return false;
		Location lc=p.getEyeLocation();
		Vector v=lc.toVector();
		boolean magic=true;
		boolean axe=timers.containsKey("axeRush");
		for(int i=0;i<13;i++){
			Vector v1 = lc.getDirection().normalize().multiply(0.25);
			v.add(v1);
			Location l=v.toLocation(lc.getWorld());
			boolean skip=false;
			String m=l.getBlock().getType().toString();
			for(String st:main.ghosts){
				if(m.contains(st)){
					skip=true;
					break;
				}
			}
			if(!skip){
				v.subtract(v1);
				if(i<=3)magic=false;
				break;
			}
			double dist=0.35;
			if(axe)dist*=2;
			if(steal.clone().add(0, 0.35, 0).distance(l)<=dist){
				magic=false;
				if(p.isSneaking()){
					int add=2;
					if(axe)add=3;
					GepUtil.HashMapReplacer(timers, "stealing", add, false, false);
					if(timers.get("stealing")>=11){
						timers.remove("stealing");
						p.sendTitle(ChatColor.GOLD+"Грабим...",ChatColor.RED+""+ChatColor.BOLD+"Разграблено!", 0, 5, 5);
						if(axe)p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 2, 2);
						p.spawnParticle(Particle.FIREWORKS_SPARK, steal, 5, 0.1, 0, 0.1, 0.05);
						return true;
					}
					p.sendTitle(ChatColor.GOLD+"Грабим...",ChatColor.RED+""+(timers.get("stealing")-1)*10+"%", 0, 5, 5);
					p.spawnParticle(Particle.FIREWORKS_SPARK, steal.clone().add(new Random().nextDouble()*0.7-0.35,new Random().nextDouble()*0.7-0.35,new Random().nextDouble()*0.7-0.35), 0, 0, 1, 0, 0.1);
				}else{
					ChatColor color=ChatColor.WHITE;
					if(main.colorFactor/2%6==0)color=ChatColor.GREEN;
					else if(main.colorFactor/2%4==0)color=ChatColor.YELLOW;
					p.sendTitle(color+"Shift",ChatColor.RED+"чтобы разграбить!", 0, 5, 5);
					if(main.colorFactor%2==0)p.spawnParticle(Particle.FIREWORKS_SPARK, steal.clone().add(0,0.35,0), 1, 0.15, 0.15, 0.15, 0);
				}
				break;
			}else if(main.colorFactor%2==0){
				p.spawnParticle(Particle.CRIT, steal.clone().add(0,0.35,0), 1, 0.075, 0.075, 0.075, 0);
			}
			if(!magic)break;
		}
		if(magic){
			lc.getWorld().spawnParticle(Particle.CRIT_MAGIC, v.toLocation(lc.getWorld()), 1, 0, 0, 0, 0);
			if(axe)lc.getWorld().spawnParticle(Particle.TOTEM, v.toLocation(lc.getWorld()), 0, 0, 0.1, 0, 0.1);
		}
		return false;
	}
	public void addSteal(Location l){
		//Player p=getPlayer();
		//boolean axe=timers.containsKey("axeRush");
		int i=0;
		while(true){
			i++;
			if(i>=100){
				GepUtil.globMessage("error");
				return;
			}
			Location loc=l.clone().add(new Random().nextInt(20)-10, 0, new Random().nextInt(20)-10);
			if(loc.getBlock().getType().equals(Material.AIR)&&loc.clone().add(0, -loc.getY()+0.5, 0).getBlock().getType().equals(Material.EMERALD_BLOCK)){
				steal=loc;
				/*if(axe){
					Location point1 = p.getLocation();
					Location point2 = steal.clone();
					point1.setY(point1.getY()+1);
				    Vector p1 = point1.toVector();
				    Vector p2 = point2.toVector();
				    Vector v = p2.clone().subtract(p1).normalize();
				    Location vl=p.getLocation();
				    vl.setYaw(GeomUtil.getLookAtYaw(v));
				    //vl.setPitch(GeomUtil.getLookAtPitch(v));
				    p.teleport(vl);
				}*/
				return;
			}
		}
	}
	public Player getPlayer(){
		return Bukkit.getPlayer(name);
	}
}
