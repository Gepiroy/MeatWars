package MeatWars;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import objMeat.Castle;
import objMeat.PlayerInfo;
import objMeat.Team;
import utilsMeat.GepUtil;

public class edit implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if(!p.isOp())return true;
		Team team=null;
		ChatColor bcol = ChatColor.WHITE;
		if(args.length==0){
			main.castle.launch();
			return true;
		}
		if(args.length==2){
			if(args[0].equals("castle")){
				team=main.castle;
				Castle c = main.castle;
				bcol=ChatColor.BLUE;
				if(args[1].equals("wall")){
					c.wallBreak=p.getLocation().getBlock().getLocation().add(0.5, 1, 0.5);
					p.sendMessage(bcol+"wall set");
				}
				if(args[1].equals("weaponRush")){
					c.weaponRush=p.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
					p.sendMessage(bcol+"W.R. set");
				}
				if(args[1].equals("magicRush")){
					c.magicRush=p.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
					p.sendMessage(bcol+"M.R. set");
				}
				if(args[1].equals("goldRush")){
					c.goldRush=p.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
					p.sendMessage(bcol+"G.R. set");
				}
				if(args[1].equals("afterBreak")){
					c.afterBreak=p.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
					p.sendMessage(bcol+"A.Br. set");
				}
				if(args[1].equals("Mbase")){
					c.MainBase=p.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
					p.sendMessage(bcol+"Mbase set");
				}
				if(args[1].equals("medRush")){
					c.medRush=p.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
					p.sendMessage(bcol+"M.R. set");
				}
				if(args[1].equals("arch")){
					c.arch=p.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
					p.sendMessage(bcol+"arch set");
				}
				if(args[1].equals("mine")){
					PlayerInfo pi=Events.plist.get(p.getName());
					c.mine=p.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
					pi.toggleBool("mine");
					p.sendMessage(bcol+"Mine set, can edit? "+pi.bools.contains("mine"));
				}
			}
			else{
				team=main.wars;
				bcol = ChatColor.RED;
			}
			if(args[1].equals("base")){
				team.base=p.getLocation().getBlock().getLocation().add(0.5,0,0.5);
				p.sendMessage(bcol+"base set");
			}
			if(args[1].equals("meat")){
				team.meat=p.getLocation().getBlock().getLocation().add(0.5,0,0.5);
				p.sendMessage(bcol+"meat set");
			}
			if(args[1].equals("weapons")){
				Villager vil=(Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
				vil.setInvulnerable(true);
				vil.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);
				team.weapons=vil.getUniqueId();
				p.sendMessage(bcol+"weapons set");
			}
			if(args[1].equals("gold")){
				Villager vil=(Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
				vil.setInvulnerable(true);
				vil.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);
				team.gold=vil.getUniqueId();
				p.sendMessage(bcol+"gold set");
			}
			if(args[1].equals("med")){
				Villager vil=(Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
				vil.setInvulnerable(true);
				vil.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);
				team.med=vil.getUniqueId();
				p.sendMessage(bcol+"med set");
			}
			if(args[0].equals("resp")){
				main.resps.add(p.getLocation().getBlock().getLocation().add(0.5, 0.1, 0.5));
				File file = new File(main.instance.getDataFolder()+File.separator+"global.yml");
				FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
				conf.set("resps", null);
				int i=0;
				for(Location l:main.resps){
					GepUtil.saveLocToConf(conf, "resps."+i, l);
					i++;
				}
				GepUtil.saveCfg(conf, file);
				p.sendMessage("saved "+i+" resp");
			}
		}
		save.saveTeams();
		return true;
	}
	
}
