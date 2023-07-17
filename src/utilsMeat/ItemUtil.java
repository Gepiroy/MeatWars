package utilsMeat;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemUtil {
	public static ItemStack create(Material material, int amount, int j, String DisplayName, String[] lore1, Enchantment ench, int lvl) {
		ItemStack item = new ItemStack(material, amount, (short) j);
		ItemMeta meta = item.getItemMeta();
		
		if(DisplayName != null){
			meta.setDisplayName(DisplayName);
		}
		ArrayList<String> lore = new ArrayList<String>();
		if(lore1 != null){
			for(int i=0;i<lore1.length;i++){
				lore.add(lore1[i]);
			}
		}
		meta.setLore(lore);
		if(ench != null)meta.addEnchant(ench, lvl, true);
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack create(Material material, String DisplayName){
		return create(material, 1, (byte)0, DisplayName, null, null, 0);
	}
	public static ItemStack create(Material material, int amount, int data){
		return create(material, amount, (byte) data, null, null, null, 0);
	}
	public static ItemStack create(Material material, int amount, String DisplayName, String[] lore1){
		return create(material, amount, (byte) 0, DisplayName, lore1, null, 0);
	}
	public static ItemStack create(Material material, int amount, String DisplayName, String[] lore1, Enchantment ench, int lvl){
		return create(material, amount, (byte) 0, DisplayName, lore1, ench, lvl);
	}
	public static ItemStack createTool(Material material, String DisplayName, String[] lore1, Enchantment ench, int lvl) {
		ItemStack item = new ItemStack(material, 1, (short)0);
		ItemMeta meta = item.getItemMeta();
		if(DisplayName != null){
			meta.setDisplayName(DisplayName);
		}
		ArrayList<String> lore = new ArrayList<String>();
		if(lore1 != null){
			for(int i=0;i<lore1.length;i++){
				lore.add(lore1[i]);
			}
		}
		meta.setLore(lore);
		meta.setUnbreakable(true);
		if(ench != null)meta.addEnchant(ench, lvl, true);
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack ZItem(Material mat){
		return createTool(mat, null, null, null, 0);
	}
	public static ItemStack createArmorColored(Material mat, String DisplayName, String[] lore, int r, int g, int b) {
		ItemStack item = createTool(mat,DisplayName,lore,null,0);
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(r, g, b));
		meta.setUnbreakable(true);
		item.setItemMeta(meta);
		return item;
	}
}
