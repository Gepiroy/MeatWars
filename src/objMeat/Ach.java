package objMeat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import MeatWars.Events;
import utilsMeat.GepUtil;
import utilsMeat.ItemUtil;

public class Ach {
	public String name;
	public String[] lore;
	public Ach(String Name, String[] Lore){
		name=Name;
		lore=Lore;
	}
	public ItemStack genItem(String plname){
		ItemStack ret=ItemUtil.create(Material.DIAMOND, 1, 0, name, lore, null, 0);
		GlobPInfo gpi=Events.gplist.get(plname);
		if(gpi.achs.contains(name)){
			ItemMeta meta = ret.getItemMeta();
			meta.addEnchant(Enchantment.ARROW_DAMAGE, 10, true);
			ret.setItemMeta(meta);
		}
		return ret;
	}
	public void compl(Player p){
		GlobPInfo gpi=Events.gplist.get(p.getName());
		if(gpi.achs.contains(name)){
			return;
		}
		gpi.achs.add(name);
		gpi.saveSQL();
		p.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 10, 1);
		p.sendMessage(ChatColor.BLUE+"-------ѕќ«ƒ–ј¬Ћя≈ћ!-------");
		p.sendMessage(ChatColor.AQUA+"¬ы получили достижение "+ChatColor.DARK_PURPLE+"["+name+ChatColor.DARK_PURPLE+"]"+ChatColor.AQUA+"!");
		for(String st:lore){
			p.sendMessage(ChatColor.GRAY+"| "+st);
		}
		p.sendMessage(ChatColor.BLUE+"-------ѕќ«ƒ–ј¬Ћя≈ћ!-------");
		GepUtil.globMessage(ChatColor.GOLD+p.getName()+ChatColor.GREEN+" получил достижение "+ChatColor.DARK_PURPLE+"["+name+ChatColor.DARK_PURPLE+"]"+ChatColor.AQUA+"!", Sound.ENTITY_WITHER_SHOOT, 1, 2, null, null, 0, 0, 0);
		gpi.achPoints++;
	}
}
