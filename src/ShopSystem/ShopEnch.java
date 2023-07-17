package ShopSystem;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import MeatWars.Events;
import objMeat.PlayerInfo;

public class ShopEnch extends ShopItem{
	public Enchantment ench=Enchantment.KNOCKBACK;
	public int lvl=1;
	
	public ShopEnch(ItemStack item, Enchantment Ench, int Lvl, int price){
		super(item, price);
		ench=Ench;
		lvl=Lvl;
	}
	
	public boolean buyEnch(Player p){
		PlayerInfo pi=Events.plist.get(p.getName());
		if(pi.money>=price){
			ItemStack hitem=p.getInventory().getItemInMainHand();
			if(hitem.getEnchantments().containsKey(ench)){
				p.sendMessage(ChatColor.RED+"У вас уже есть это зачарование.");
				return false;
			}
			ItemMeta hmeta = hitem.getItemMeta();
			hmeta.addEnchant(ench, lvl, true);
			hitem.setItemMeta(hmeta);
			pi.money-=price;
			p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 2, 2);
			return true;
		}
		else{
			p.sendMessage(ChatColor.RED+"Денег нет. Ну вы держитесь)");
		}
		return false;
	}
}
