package objMeat;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import utilsMeat.GepUtil;

public class Warriors extends Team{
	public Location hunt;
	public String power="";
	
	public Warriors(){}
	public Warriors(FileConfiguration conf){
		super(conf, "Wars");
		hunt=GepUtil.getLocFromConf(conf, "Wars.hunt");
	}
	
	public void save(FileConfiguration conf){
		save(conf, "Wars");
		GepUtil.saveLocToConf(conf, "Wars.hunt", hunt);
	}
	public void gruz(Player p){
		Material[] mats = {Material.IRON_INGOT,Material.GOLD_INGOT,Material.COAL,Material.DIAMOND};
		p.getInventory().addItem(new ItemStack(mats[new Random().nextInt(mats.length)]));
	}
}
