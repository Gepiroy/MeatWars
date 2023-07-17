package ShopSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import MeatWars.Events;
import MeatWars.main;
import objMeat.GlobPInfo;
import objMeat.PlayerInfo;
import utilsMeat.GepUtil;

public class ShopItem{
	public int price;
	public ItemStack displItem;
	
	public ShopItem(){}
	public ShopItem(ItemStack item, int Price){
		price=Price;
		displItem=item;
	}
	
	public ItemStack GUIItem(PlayerInfo pi){
		ItemStack item = displItem.clone();
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		if(lore==null)lore=new ArrayList<>();
		lore.add(ChatColor.GOLD+"Цена: "+GepUtil.boolCol(pi.money>=price)+price);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	public void buy(Player p){
		PlayerInfo pi=Events.plist.get(p.getName());
		if(pi.money>=price){
			ItemStack hitem=displItem.clone();
			GlobPInfo gpi=Events.gplist.get(p.getName());
			if(hitem!=null&&main.castle.players.contains(p.getName())&&gpi.kitC.equals(ChatColor.RED+"Кузнец")&&new Random().nextDouble()<=0.75){
				List<Material> mats = new ArrayList<>();
				mats.add(Material.WOOD_SWORD);
				mats.add(Material.STONE_SWORD);
				mats.add(Material.IRON_SWORD);
				mats.add(Material.STONE_AXE);
				mats.add(Material.IRON_AXE);
				List<Material> arms = new ArrayList<>();
				arms.add(Material.LEATHER_HELMET);
				arms.add(Material.LEATHER_CHESTPLATE);
				arms.add(Material.LEATHER_LEGGINGS);
				arms.add(Material.LEATHER_BOOTS);
				arms.add(Material.CHAINMAIL_HELMET);
				arms.add(Material.CHAINMAIL_CHESTPLATE);
				arms.add(Material.CHAINMAIL_LEGGINGS);
				arms.add(Material.CHAINMAIL_BOOTS);
				arms.add(Material.IRON_HELMET);
				arms.add(Material.IRON_CHESTPLATE);
				arms.add(Material.IRON_LEGGINGS);
				arms.add(Material.IRON_BOOTS);
				ItemMeta meta = hitem.getItemMeta();
				if(hitem.getType().equals(Material.BOW)){
					meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
					hitem.setItemMeta(meta);
					p.sendTitle(ChatColor.LIGHT_PURPLE+"Усилено!", ChatColor.RED+"Сила "+ChatColor.GOLD+1, 10, 20, 10);
					p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 2, 1);
					
				}
				else if(arms.contains(hitem.getType())){
					meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
					hitem.setItemMeta(meta);
					p.sendTitle(ChatColor.LIGHT_PURPLE+"Укреплено!", ChatColor.RED+"Защита "+ChatColor.GOLD+1, 10, 20, 10);
					p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 2, 1);
				}
				else if(mats.contains(hitem.getType())){
					meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
					hitem.setItemMeta(meta);
					p.sendTitle(ChatColor.LIGHT_PURPLE+"Заточено!", ChatColor.RED+"Острота "+ChatColor.GOLD+1, 10, 20, 10);
					p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 2, 1);
				}
			}
			if(p.getInventory().addItem(hitem)==null){
				p.sendMessage(ChatColor.RED+"Места нет. Ну вы держитесь)");
			}
			else{
				pi.money-=price;
				p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2, 1);
			}
		}
		else{
			p.sendMessage(ChatColor.RED+"Денег нет. Ну вы держитесь)");
		}
	}
}
