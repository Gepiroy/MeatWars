package ShopSystem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Shop2 {
	public String name="";
	public List<ShopUpgrade> sitems = new ArrayList<>();
	
	public Shop2(String Name, List<ShopUpgrade> Sitems){
		name=Name;
		sitems=Sitems;
	}
	
	public void openGUI(Player p){
		Inventory inv = Bukkit.createInventory(null, (sitems.size()-1)/9*9+9, name);
		int i=0;
		for(ShopUpgrade sitem:sitems){
			inv.setItem(i, sitem.GUIItem(p, name));
			i++;
		}
		p.openInventory(inv);
	}
}
