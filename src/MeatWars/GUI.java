package MeatWars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ShopSystem.Shop;
import ShopSystem.Shop2;
import ShopSystem.ShopEnch;
import objMeat.Ach;
import objMeat.GlobPInfo;
import objMeat.Mclass;
import objMeat.PlayerInfo;
import utilsMeat.GepUtil;
import utilsMeat.ItemUtil;

public class GUI implements Listener{
	static HashMap<Integer, ShopEnch> enchs = new HashMap<>();
	static HashMap<Integer, ShopEnch> bowenchs = new HashMap<>();
	static HashMap<Integer, ShopEnch> armenchs = new HashMap<>();
	public static ArrayList<Mclass> Cclasses = new ArrayList<>();
	public static ArrayList<Mclass> Wclasses = new ArrayList<>();
	public static void teamSel(Player p){
		Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.DARK_GREEN+"Выбор команды");
		inv.setItem(0, teamItem(true, p.getName()));
		inv.setItem(4, teamItem(false, p.getName()));
		ItemStack item = ItemUtil.create(Material.CAKE, ChatColor.LIGHT_PURPLE+"Рандом");
		ItemMeta meta =item.getItemMeta();
		PlayerInfo pi = Events.plist.get(p.getName());
		if(!pi.bools.contains("redSelected")&&!pi.bools.contains("blueSelected")){
			meta.addEnchant(Enchantment.ARROW_DAMAGE, 10, true);
		}
		item.setItemMeta(meta);
		inv.setItem(2, item);
		p.openInventory(inv);
	}
	public static void classesC(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD+"Классы защитников");
		int i=0;
		for(Mclass c:Cclasses){
			inv.setItem(i, c.item(p, true));
			i++;
		}
		p.openInventory(inv);
	}
	public static void classesW(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD+"Классы варваров");
		int i=0;
		for(Mclass c:Wclasses){
			inv.setItem(i, c.item(p, false));
			i++;
		}
		p.openInventory(inv);
	}
	public static void achs(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.AQUA+"Достижения");
		int i=0;
		for(Ach a:main.achs){
			inv.setItem(i, a.genItem(p.getName()));
			i++;
		}
		p.openInventory(inv);
	}
	static ItemStack teamItem(boolean cas,String pname){
		ItemStack item = ItemUtil.create(Material.IRON_SWORD, ChatColor.AQUA+"Защитники");
		if(!cas)item=ItemUtil.create(Material.RAW_BEEF, ChatColor.RED+"Варвары");
		ItemMeta meta =item.getItemMeta();
		PlayerInfo pi = Events.plist.get(pname);
		if(pi.bools.contains("redSelected")&&!cas)meta.addEnchant(Enchantment.ARROW_DAMAGE, 10, true);
		if(pi.bools.contains("blueSelected")&&cas)meta.addEnchant(Enchantment.ARROW_DAMAGE, 10, true);
		item.setItemMeta(meta);
		return item;
	}
	
	@EventHandler
	public void click(InventoryClickEvent e){
		if(e.getClickedInventory() != null&&e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			PlayerInfo pi = Events.plist.get(p.getName());
			ItemStack item = e.getCurrentItem();
			if(item!=null){
				for(Shop s:main.shops.values()){
					if(e.getInventory().getName().equals(s.name)){
						e.setCancelled(true);
						if(e.getClickedInventory().getName().equals(s.name)){
							if(e.getCurrentItem().getType().equals(Material.GOLD_AXE)){
								if(GepUtil.haveItem(p, Material.GOLD_AXE, 1)){
									p.sendMessage(ChatColor.GOLD+"Вы не можете использовать больше 1 топора разрухи.");
									return;
								}
							}
							if(e.getCurrentItem().getType().equals(Material.FLINT)){
								if(GepUtil.haveItem(p, Material.FLINT, 1)){
									p.sendMessage(ChatColor.GOLD+"Вы не можете использовать больше 1 наручников.");
									return;
								}
							}
							s.sitems.get(e.getSlot()).buy(p);
							s.openGUI(p);
						}
						break;
					}
				}
				for(Shop2 s:main.shopsUps.values()){
					if(e.getInventory().getName().equals(s.name)){
						e.setCancelled(true);
						if(e.getClickedInventory().getName().equals(s.name)){
							s.sitems.get(e.getSlot()).buy(p, s.name);
							s.openGUI(p);
						}
						break;
					}
				}
				if(e.getInventory().getName().equals(ChatColor.DARK_GREEN+"Выбор команды")){
					e.setCancelled(true);
					if(e.getClickedInventory().getName().equals(ChatColor.DARK_GREEN+"Выбор команды")){
						if(e.getCurrentItem().getType().equals(Material.IRON_SWORD)){
							pi.toggleBool("redSelected", false);
							pi.toggleBool("blueSelected", true);
						}
						else if(e.getCurrentItem().getType().equals(Material.RAW_BEEF)){
							pi.toggleBool("redSelected", true);
							pi.toggleBool("blueSelected", false);
						}
						else{
							pi.toggleBool("redSelected", false);
							pi.toggleBool("blueSelected", false);
						}
						for(Player pl:Bukkit.getOnlinePlayers()){
							if(pl.getOpenInventory()!=null&&pl.getOpenInventory().getTitle().equals(ChatColor.DARK_GREEN+"Выбор команды"))teamSel(pl);
						}
					}
				}
				if(e.getInventory().getName().equals(ChatColor.GOLD+"Классы защитников")){
					e.setCancelled(true);
					if(e.getClickedInventory().getName().equals(ChatColor.GOLD+"Классы защитников")){
						GlobPInfo gpi=Events.gplist.get(p.getName());
						Mclass c = getClass(e.getCurrentItem().getItemMeta().getDisplayName(), true);
						if(gpi.kitsC.contains(c.name)){
							gpi.kitC=c.name;
						}
						else{
							c.buy(p, true);
						}
						classesC(p);
					}
				}
				if(e.getInventory().getName().equals(ChatColor.GOLD+"Классы варваров")){
					e.setCancelled(true);
					if(e.getClickedInventory().getName().equals(ChatColor.GOLD+"Классы варваров")){
						GlobPInfo gpi=Events.gplist.get(p.getName());
						Mclass c = getClass(e.getCurrentItem().getItemMeta().getDisplayName(), false);
						if(gpi.kitsW.contains(c.name)){
							gpi.kitW=c.name;
						}
						else{
							c.buy(p, false);
						}
						classesW(p);
					}
				}
				if(e.getInventory().getName().equals(ChatColor.LIGHT_PURPLE+"Зачарования")){
					e.setCancelled(true);
					if(e.getClickedInventory().getName().equals(ChatColor.LIGHT_PURPLE+"Зачарования")){
						List<Material> mats = new ArrayList<>();
						mats.add(Material.WOOD_SWORD);
						mats.add(Material.STONE_SWORD);
						mats.add(Material.IRON_SWORD);
						mats.add(Material.STONE_AXE);
						mats.add(Material.IRON_AXE);
						if(mats.contains(p.getInventory().getItemInMainHand().getType())&&enchs.get(e.getSlot()).buyEnch(p))p.closeInventory();
						else if(!p.getInventory().getItemInMainHand().getType().equals(Material.BOW)&&armenchs.get(e.getSlot()).buyEnch(p))p.closeInventory();
						else if(p.getInventory().getItemInMainHand().getType().equals(Material.BOW)&&bowenchs.get(e.getSlot()).buyEnch(p))p.closeInventory();
						
					}
				}
				if(e.getInventory().getName().equals(ChatColor.AQUA+"Достижения")){
					e.setCancelled(true);
				}
			}
		}
	}
	public static Mclass getClass(String name, boolean isCastle){
		if(!main.full){
			if(isCastle)name=ChatColor.BLUE+"Защитник";
			else name=ChatColor.RED+"Варвар";
		}
		if(isCastle){
			for(Mclass c:Cclasses){
				if(c.name.equals(name))return c;
			}
		}
		else{
			for(Mclass c:Wclasses){
				if(c.name.equals(name))return c;
			}
		}
		return null;
	}
}
