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
			p.sendMessage(ChatColor.YELLOW+"����� �������� ������� � ���� ������, ��������� /gift <���>");
			return true;
		}
		if(p.getName().equals(args[0])){
			p.sendMessage(ChatColor.YELLOW+"�� ���� �� ���, ����.");
			return true;
		}
		if(!main.stage.equals("shop")){
			p.sendMessage(ChatColor.RED+"���������� �������� ����� ������ �� ������ ����������.");
			return true;
		}
		Player p2 = Bukkit.getPlayer(args[0]);
		if(p2==null){
			p.sendMessage(ChatColor.RED+"����� �� ������.");
			return true;
		}
		if(main.castle.players.contains(p.getName())!=main.castle.players.contains(p2.getName())){
			p.sendMessage(ChatColor.RED+"��� ����.");
			return true;
		}
		if(p.getInventory().getItemInMainHand()==null){
			p.sendMessage(ChatColor.RED+"�� ��� ���, ���� �������� ��������?");
			return true;
		}
		if(p2.getInventory().addItem(p.getInventory().getItemInMainHand())==null){
			p.sendMessage(ChatColor.RED+"���� ���� ������ ��������, ����� �������� ���������, � �� ������ ��� ��������� ���.");
			return true;
		}
		ItemStack item=p.getInventory().getItemInMainHand().clone();
		p.getInventory().getItemInMainHand().setAmount(0);
		p.sendMessage(ChatColor.GREEN+"�� ������� ������ "+ChatColor.DARK_GREEN+p2.getName()+ChatColor.GREEN+" ���� �������.");
		String what = "���-��";
		if(item.hasItemMeta()&&item.getItemMeta().hasDisplayName()){
			what=item.getItemMeta().getDisplayName();
			if(item.getAmount()>1)what+=ChatColor.AQUA+"x"+item.getAmount();
			what+=ChatColor.GREEN+"";
		}
		p2.sendMessage(ChatColor.GREEN+"����� "+ChatColor.DARK_GREEN+p.getName()+ChatColor.GREEN+" ������� ���� "+what+", ����� ���������.");
		return true;
	}
	
}