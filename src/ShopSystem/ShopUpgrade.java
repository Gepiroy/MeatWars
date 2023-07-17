package ShopSystem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import MeatWars.Events;
import objMeat.GlobPInfo;
import objMeat.PlayerInfo;
import utilsMeat.GepUtil;

public class ShopUpgrade{
	public int price;
	public ItemStack displItem;
	public String upKey;
	public int max;
	double priceUp=1.0;
	int start=0;
	
	public ShopUpgrade(){}
	public ShopUpgrade(ItemStack item, int Price, String UpKey, int Max, double PriceUp, int Start){
		price=Price;
		displItem=item;
		upKey=UpKey;
		max=Max;
		priceUp=PriceUp-1;
		start=Start;
	}
	
	public ItemStack GUIItem(Player p, String shop){
		PlayerInfo pi=Events.plist.get(p.getName());
		ItemStack item = displItem.clone();
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		if(lore==null)lore=new ArrayList<>();
		int pr=(int) (price*(1+(pi.ups.get(upKey)-start)*priceUp));
		GlobPInfo gpi=Events.gplist.get(p.getName());
		if(pi.ups.get(upKey)<max){
			if(gpi.kitC.equals(ChatColor.YELLOW+"Экономист")&&shop.equals("castleGold")){
				pr*=0.9;
				lore.add(ChatColor.GOLD+"Цена: "+GepUtil.boolCol(pi.money>=pr)+pr+ChatColor.GOLD+ChatColor.BOLD+" (-10%!)");
			}
			else lore.add(ChatColor.GOLD+"Цена: "+GepUtil.boolCol(pi.money>=pr)+pr);
			lore.add(ChatColor.GREEN+""+pi.ups.get(upKey)+ChatColor.YELLOW+"/"+max);
		}
		else lore.add(ChatColor.AQUA+"МАКСИМУМ!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	public void buy(Player p, String shop){
		PlayerInfo pi=Events.plist.get(p.getName());
		GlobPInfo gpi=Events.gplist.get(p.getName());
		if(pi.ups.get(upKey)>=max){
			p.sendMessage(ChatColor.AQUA+"Это МА-КСИ-МУМ, понимаешь?)");
			return;
		}
		int pr=(int) (price*(1+(pi.ups.get(upKey)-start)*priceUp));
		if(gpi.kitC.equals(ChatColor.YELLOW+"Экономист")&&shop.equals("castleGold")){
			pr*=0.9;
		}
		if(pi.money>=pr){
			pi.money-=pr;
			p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2, 1);
			GepUtil.HashMapReplacer(pi.ups, upKey, 1, false, false);
		}
		else{
			p.sendMessage(ChatColor.RED+"Денег нет. Ну вы держитесь)");
		}
	}
}
