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
		if(items.size()>0)lore.add(ChatColor.YELLOW+"��������� ����:");
		else lore.add(ChatColor.RED+"��������� ����� ���.");
		for(ItemStack it:items){
			String st="�! ������ ����!";
			if(it.hasItemMeta()&&it.getItemMeta().hasDisplayName())st=it.getItemMeta().getDisplayName();
			else st=it.getType().name();
			lore.add(ChatColor.GRAY+"- "+ChatColor.GOLD+st+ChatColor.WHITE+" x"+ChatColor.AQUA+it.getAmount());
		}
		ItemMeta meta=item.getItemMeta();
		GlobPInfo gpi = Events.gplist.get(p.getName());
		if(isCastle){
			if(gpi.kitC.equals(name)){
				meta.addEnchant(Enchantment.ARROW_DAMAGE, 10, true);
				lore.add(ChatColor.AQUA+""+ChatColor.UNDERLINE+"������������!");
			}
			else if(gpi.kitsC.contains(name)){
				lore.add(ChatColor.GREEN+"��������������!");
			}
			else{
				lore.add(ChatColor.YELLOW+"����: "+GepUtil.boolCol(Donate.main.connection.GetG(p.getName())>=price)+price+ChatColor.BLUE+" G$"+ChatColor.AQUA+" (� ��� "+Donate.main.connection.GetG(p.getName())+")");
			}
		}
		else{
			if(gpi.kitW.equals(name)){
				meta.addEnchant(Enchantment.ARROW_DAMAGE, 10, true);
				lore.add(ChatColor.AQUA+""+ChatColor.UNDERLINE+"������������!");
			}
			else if(gpi.kitsW.contains(name)){
				lore.add(ChatColor.GREEN+"��������������!");
			}
			else{
				lore.add(ChatColor.YELLOW+"����: "+GepUtil.boolCol(Donate.main.connection.GetG(p.getName())>=price)+price+ChatColor.BLUE+" G$"+ChatColor.AQUA+" (� ��� "+Donate.main.connection.GetG(p.getName())+")");
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
				p.sendMessage(ChatColor.GOLD+"� ��� ��� ����� ����������. "+ChatColor.AQUA+"��������� ����� ������, ���� ������ �������� ���� �����!");
				p.sendMessage(ChatColor.AQUA+"�� ��� �� ������ ������� ������������� �������, �� ��� �������� ������ "+ChatColor.BLUE+"Unlocker"+ChatColor.AQUA+". ��� ������ ������� ������ �����������, � ��� ����� � ����������� �� �������. "+ChatColor.GRAY+"(��������� /donate)");
				return false;
			}
			if(isCastle)gpi.kitsC.add(name);
			else gpi.kitsW.add(name);
			Donate.main.connection.AddG(p.getName(), -price);
			p.sendMessage(ChatColor.GREEN+"����� �������������! ������ �������� ���, ����� ������������.");
			gpi.saveSQL();
			return true;
		}
		else{
			p.sendMessage(ChatColor.GOLD+"�� ������� ��������. "+ChatColor.AQUA+"�� ������ ���������� ��, ����� �� �������!");
			return false;
		}
	}
}
