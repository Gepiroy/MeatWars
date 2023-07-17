package ShopSystem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import MeatWars.Events;
import objMeat.PlayerInfo;

public class Shop {
	public String name="";
	public List<ShopItem> sitems = new ArrayList<>();
	
	public Shop(String Name, List<ShopItem> Sitems){
		name=Name;
		sitems=Sitems;
	}
	
	public void openGUI(Player p){
		PlayerInfo pi=Events.plist.get(p.getName());
		Inventory inv = Bukkit.createInventory(null, (sitems.size()-1)/9*9+9, name);
		int i=0;
		for(ShopItem sitem:sitems){
			inv.setItem(i, sitem.GUIItem(pi));
			i++;
		}
		p.openInventory(inv);
	}
}
