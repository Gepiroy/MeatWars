package objMeat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import MeatWars.Events;
import MeatWars.main;
import utilsMeat.GepUtil;

public class Castle extends Team{
	
	public int wall=0;
	public Location arch;
	public Location wallBreak;
	public int weaponHP=100;
	public Location weaponRush;
	public boolean baseRush=false;
	public int magicHP=100;
	public Location magicRush;
	public int medHP=100;
	public Location medRush;
	public int goldHP=100;
	public double baseHP=100.0;
	public Location MainBase;
	public Location goldRush;
	public Castle(){}
	public Location afterBreak;
	public List<String> canBreak = new ArrayList<>();
	public Location mine;
	public List<Location> mineBlocks = new ArrayList<>();
	public String power="";
	public HashMap<String,Integer> waits=new HashMap<>();
	public Castle(FileConfiguration conf){
		super(conf, "Castle");
		wallBreak=GepUtil.getLocFromConf(conf, "Castle.wall");
		weaponRush=GepUtil.getLocFromConf(conf, "Castle.weaponRush");
		magicRush=GepUtil.getLocFromConf(conf, "Castle.magicRush");
		goldRush=GepUtil.getLocFromConf(conf, "Castle.goldRush");
		medRush=GepUtil.getLocFromConf(conf, "Castle.medRush");
		afterBreak=GepUtil.getLocFromConf(conf, "Castle.afterBreak");
		MainBase=GepUtil.getLocFromConf(conf, "Castle.Mbase");
		mine=GepUtil.getLocFromConf(conf, "Castle.mine");
		arch=GepUtil.getLocFromConf(conf, "Castle.arch");
		if(conf.contains("Castle.MineBlocks"))for(String st:conf.getConfigurationSection("Castle.MineBlocks").getKeys(false)){
			mineBlocks.add(GepUtil.getLocFromConf(conf, "Castle.MineBlocks."+st));
		}
	}
	
	public void save(FileConfiguration conf){
		save(conf,"Castle");
		GepUtil.saveLocToConf(conf, "Castle.wall", wallBreak);
		GepUtil.saveLocToConf(conf, "Castle.weaponRush", weaponRush);
		GepUtil.saveLocToConf(conf, "Castle.magicRush", magicRush);
		GepUtil.saveLocToConf(conf, "Castle.goldRush", goldRush);
		GepUtil.saveLocToConf(conf, "Castle.afterBreak", afterBreak);
		GepUtil.saveLocToConf(conf, "Castle.Mbase", MainBase);
		GepUtil.saveLocToConf(conf, "Castle.medRush", medRush);
		GepUtil.saveLocToConf(conf, "Castle.mine", mine);
		GepUtil.saveLocToConf(conf, "Castle.arch", arch);
		int i=0;
		for(Location loc:mineBlocks){
			GepUtil.saveLocToConf(conf, "Castle.MineBlocks."+i,loc);
			i++;
		}
	}
	
	public void breakWall(){
		int x=wallBreak.getBlockX()-5;
		int y=wallBreak.getBlockY()-1;
		int z=wallBreak.getBlockZ();
		for(int dx=0;dx<11;dx++){
			for(int dy=0;dy<7;dy++){
				Location loc = new Location(wallBreak.getWorld(),x+dx,y+dy,z);
				if(loc.getBlock().getType().equals(Material.LOG)){
					loc.getBlock().breakNaturally();
				}
			}
		}
		GepUtil.globMessage(null, Sound.ENTITY_GENERIC_EXPLODE, 10, 0, null, null, 0, 0, 0);
		GepUtil.globMessage(null, Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 10, 0, ChatColor.RED+"ÂÐÀÒÀ ÏÐÎËÎÌËÅÍÛ!", ChatColor.GOLD+"Âðåìÿ áîðîòüñÿ çà äåíüãè!", 10, 30, 10);
		wallBreak.getWorld().spawnParticle(Particle.CLOUD, wallBreak.clone().add(0, 1, 0), 400, 2, 1, 2, 0.7);
		for(String st:players){
			Player p=Bukkit.getPlayer(st);
			PlayerInfo pi=Events.plist.get(st);
			main.tp(p, afterBreak.clone().add(new Random().nextDouble()*2-1, new Random().nextDouble()+0.5, new Random().nextDouble()*2-1), 180);
			GepUtil.HashMapReplacer(pi.timers, "respawn", 0, true, true);
			p.setGameMode(GameMode.ADVENTURE);
		}
		for(String st:main.wars.players){
			Player p=Bukkit.getPlayer(st);
			PlayerInfo pi=Events.plist.get(st);
			p.teleport(wallBreak.clone().add(new Random().nextDouble()*2-1, new Random().nextDouble()+0.5, new Random().nextDouble()*2-3));
			Vector v=new Vector(0, 0.25, -1);
			if(main.wars.power.equals("break")){
				v=new Vector(0,0.25,0.5);
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 0));
				p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 0));
			}
			p.setVelocity(v);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 1));
			GepUtil.HashMapReplacer(pi.timers, "respawn", 0, true, true);
			p.setGameMode(GameMode.ADVENTURE);
		}
		main.MeatStage="destroy";
	}
	@SuppressWarnings("deprecation")
	public void repairWall(){
		int wars = main.wars.players.size();
		GepUtil.globMessage("wars="+wars);
		weaponHP=wars*20;
		magicHP=wars*20;
		goldHP=wars*20;
		medHP=wars*20;
		canBreak.clear();
		canBreak.add("weapon");
		canBreak.add("magic");
		canBreak.add("gold");
		canBreak.add("med");
		baseRush=false;
		wall=main.wars.players.size()*10;
		int x=wallBreak.getBlockX()-5;
		int y=wallBreak.getBlockY()-1;
		int z=wallBreak.getBlockZ();
		for(int dx=0;dx<11;dx++){
			for(int dy=0;dy<7;dy++){
				Location loc = new Location(wallBreak.getWorld(),x+dx,y+dy,z);
				Block b=loc.getBlock();
				if(b.getType().equals(Material.AIR)){
					loc.getBlock().setType(Material.LOG);
					loc.getBlock().setData((byte) 1);
				}
			}
		}
	}
	public double getDamage(){//100/(250.0*2/(100+100+50+0))=100/(500/250)=100/(0)
		//(250.0)/2.5
		//return (magicHP+weaponHP+medHP+goldHP*2)/(250.0*main.wars.players.size());
		//return (250.0*main.wars.players.size())*(magicHP+weaponHP+medHP+goldHP*2)/2.5*main.wars.players.size()
		return 100/(100.0*main.wars.players.size())*(magicHP+weaponHP+medHP+goldHP*2);
	}
	public String baseHp(){
		return GepUtil.CylDouble(baseHP, "#0.00");
	}
	public void launch(){
		GepUtil.globMessage(null, Sound.ENTITY_BLAZE_SHOOT, 2, 0, ChatColor.GOLD+"ÇÀËÏ!", null, 5, 20, 5);
		for(int i=0;i<50;i++){
			Location l=arch.clone();
			l.add(new Random().nextDouble()*30-15, -2, 10);
			Arrow a=l.getWorld().spawnArrow(l, new Vector(new Random().nextDouble()*0.3-0.15,new Random().nextDouble()*1+0.75,new Random().nextDouble()*-2), new Random().nextFloat()*2+1, 0);
			a.addScoreboardTag("castler");
		}
		for(int i=0;i<2;i++){
			fireBall(arch.clone().add(new Random().nextDouble()*30-15, -2, 10), new Random().nextDouble()*0.3-0.15,new Random().nextDouble()*2+1,new Random().nextDouble()*-4);
		}
	}
	public void fireBall(Location l, double dx, double dy, double dz){
		new BukkitRunnable() {
			double x=dx;
			double y=dy;
			double z=dz;
			double fy=dy;
			int timer=0;
			@Override
			public void run() {
				for(int i=0;i<10;i++){
					l.getWorld().spawnParticle(Particle.FLAME, l.clone().add(new Random().nextDouble()-0.5, new Random().nextDouble()-0.5, new Random().nextDouble()-0.5), 0, x*0.75, y*0.75, z*0.75);
					l.getWorld().spawnParticle(Particle.SMOKE_LARGE, l.clone().add(new Random().nextDouble()-0.5, new Random().nextDouble()-0.5, new Random().nextDouble()-0.5), 0, -x*0.5, -y*0.5, -z*0.5);
				}
				l.add(x, y, z);
				if(!l.getBlock().getType().equals(Material.AIR)){
					for(int i=0;i<100;i++){
						l.add(0, 0.1, 0);
						if(l.getBlock().getType().equals(Material.AIR)){
							break;
						}
					}
					for(int i=0;i<15;i++){
						flameBall(l.clone(), new Random().nextDouble()*0.75-0.375,new Random().nextDouble()*0.5+0.125,new Random().nextDouble()*0.75-0.375);
					}
					this.cancel();
				}
				x*=0.95;
				y-=fy*0.05;
				//if(y<-1)GepUtil.globMessage("y="+y);
				z*=0.95;
				timer++;
				if(timer>=200)this.cancel();
			}
		}.runTaskTimer(main.instance, 0, 1);
	}
	public void flameBall(Location l, double dx, double dy, double dz){
		new BukkitRunnable() {
			double x=dx;
			double y=dy;
			double z=dz;
			double fy=dy;
			int timer=0;
			@Override
			public void run() {
				l.getWorld().spawnParticle(Particle.FLAME, l, 0, x*1.5, y*1.5, z*1.5);
				l.getWorld().spawnParticle(Particle.SMOKE_NORMAL, l, 0, -x*0.5, -y*0.5, -z*0.5);
				l.add(x, y, z);
				if(!l.getBlock().getType().equals(Material.AIR)&&timer>5){
					l.getWorld().spawnParticle(Particle.CLOUD, l, 0, 0, 0.1, 0);
					//GepUtil.globMessage("timer="+timer);
					this.cancel();
				}
				for(String n:main.wars.players){
					Player p=Bukkit.getPlayer(n);
					if(p==null)continue;
					if(l.distance(p.getLocation().add(0, 1, 0))<=1){
						p.setFireTicks(100);
						p.damage(7.5);
						p.setNoDamageTicks(0);
						//GepUtil.globMessage("PL timer="+timer);
						this.cancel();
					}
				}
				x*=0.95;
				y-=fy*0.05;
				z*=0.95;
				timer++;
				if(timer>=100){
					this.cancel();
					//GepUtil.globMessage("timer="+timer);
				}
			}
		}.runTaskTimer(main.instance, 0, 1);
	}
}
