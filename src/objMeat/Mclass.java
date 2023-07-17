package objMeat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import MeatWars.Events;
import utilsMeat.GepUtil;
import utilsMeat.ItemUtil;

public class Mclass {
	Material mat;
	public String name;
	public List<ItemStack> items=new ArrayList<>();
	List<String> lore = new ArrayList<>();
	int price;
	public Mclass(List<ItemStack> Items, Material Mat, String Name, List<String> Lore, int Price){
		items=Items;
		mat=Mat;
		name=Name;
		lore=Lore;
		price=Price;
	}
	public ItemStack item(Player p, boolean isCastle){
		ItemStack item=ItemUtil.create(mat, name);
		List<String> lore = new ArrayList<>(this.lore);
		if(items.size()>0)lore.add(ChatColor.YELLOW+"Стартовые вещи:");
		else lore.add(ChatColor.RED+"Стартовых вещей нет.");
		for(ItemStack it:items){
			String st="А! ОШИБКА СТОП!";
			if(it.hasItemMeta()&&it.getItemMeta().hasDisplayName())st=it.getItemMeta().getDisplayName();
			else st=it.getType().name();
			lore.add(ChatColor.GRAY+"- "+ChatColor.GOLD+st+ChatColor.WHITE+" x"+ChatColor.AQUA+it.getAmount());
		}
		ItemMeta meta=item.getItemMeta();
		GlobPInfo gpi = Events.gplist.get(p.getName());
		if(isCastle){
			if(gpi.kitC.equals(name)){
				meta.addEnchant(Enchantment.ARROW_DAMAGE, 10, true);
				lore.add(ChatColor.AQUA+""+ChatColor.UNDERLINE+"ИСПОЛЬЗУЕТСЯ!");
			}
			else if(gpi.kitsC.contains(name)){
				lore.add(ChatColor.GREEN+"Разблокировано!");
			}
			else{
				lore.add(ChatColor.YELLOW+"Цена: "+GepUtil.boolCol(Donate.main.connection.GetG(p.getName())>=price)+price+ChatColor.BLUE+" G$"+ChatColor.AQUA+" (у вас "+Donate.main.connection.GetG(p.getName())+")");
			}
		}
		else{
			if(gpi.kitW.equals(name)){
				meta.addEnchant(Enchantment.ARROW_DAMAGE, 10, true);
				lore.add(ChatColor.AQUA+""+ChatColor.UNDERLINE+"ИСПОЛЬЗУЕТСЯ!");
			}
			else if(gpi.kitsW.contains(name)){
				lore.add(ChatColor.GREEN+"Разблокировано!");
			}
			else{
				lore.add(ChatColor.YELLOW+"Цена: "+GepUtil.boolCol(Donate.main.connection.GetG(p.getName())>=price)+price+ChatColor.BLUE+" G$"+ChatColor.AQUA+" (у вас "+Donate.main.connection.GetG(p.getName())+")");
			}
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	public boolean buy(Player p, boolean isCastle){
		GlobPInfo gpi=Events.gplist.get(p.getName());
		if(Donate.main.connection.GetG(p.getName())>=price){
			if(gpi.achPoints<=0&&!p.hasPermission("Unl")){
				p.sendMessage(ChatColor.GOLD+"У вас нет очков достижений. "+ChatColor.AQUA+"Выполните любую ачивку, если хотите получить этот набор!");
				p.sendMessage(ChatColor.AQUA+"Вы так же можете сделать пожертвование проекту, за что получите группу "+ChatColor.BLUE+"Unlocker"+ChatColor.AQUA+". Эта группа убирает многие ограничения, в том числе и ограничение по ачивкам. "+ChatColor.GRAY+"(Подробнее /donate)");
				return false;
			}
			if(isCastle)gpi.kitsC.add(name);
			else gpi.kitsW.add(name);
			Donate.main.connection.AddG(p.getName(), -price);
			p.sendMessage(ChatColor.GREEN+"Набор разблокирован! Теперь выберите его, чтобы использовать.");
			gpi.saveSQL();
			return true;
		}
		else{
			p.sendMessage(ChatColor.GOLD+"Не хватает гепчиков. "+ChatColor.AQUA+"Вы можете заработать их, играя на проекте!");
			return false;
		}
	}
}
