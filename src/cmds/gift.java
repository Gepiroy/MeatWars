package cmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import MeatWars.main;

public class gift implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if(args.length<1){
			p.sendMessage(ChatColor.YELLOW+"Чтобы передать предмет в руке игроку, используй /gift <НИК>");
			return true;
		}
		if(p.getName().equals(args[0])){
			p.sendMessage(ChatColor.YELLOW+"Да учли мы это, учли.");
			return true;
		}
		if(!main.stage.equals("shop")){
			p.sendMessage(ChatColor.RED+"Передавать предметы можно только на стадии подготовки.");
			return true;
		}
		Player p2 = Bukkit.getPlayer(args[0]);
		if(p2==null){
			p.sendMessage(ChatColor.RED+"Игрок не найден.");
			return true;
		}
		if(main.castle.players.contains(p.getName())!=main.castle.players.contains(p2.getName())){
			p.sendMessage(ChatColor.RED+"Это враг.");
			return true;
		}
		if(p.getInventory().getItemInMainHand()==null){
			p.sendMessage(ChatColor.RED+"Ты ему что, РУКУ подарить собрался?");
			return true;
		}
		if(p2.getInventory().addItem(p.getInventory().getItemInMainHand())==null){
			p.sendMessage(ChatColor.RED+"Твой друг слегка зажрался, пусть почистит инвентарь, а то некуда уже впихивать это.");
			return true;
		}
		ItemStack item=p.getInventory().getItemInMainHand().clone();
		p.getInventory().getItemInMainHand().setAmount(0);
		p.sendMessage(ChatColor.GREEN+"Ты передал игроку "+ChatColor.DARK_GREEN+p2.getName()+ChatColor.GREEN+" этот предмет.");
		String what = "что-то";
		if(item.hasItemMeta()&&item.getItemMeta().hasDisplayName()){
			what=item.getItemMeta().getDisplayName();
			if(item.getAmount()>1)what+=ChatColor.AQUA+"x"+item.getAmount();
			what+=ChatColor.GREEN+"";
		}
		p2.sendMessage(ChatColor.GREEN+"Игрок "+ChatColor.DARK_GREEN+p.getName()+ChatColor.GREEN+" передал тебе "+what+", чекай инвентарь.");
		return true;
	}
	
}