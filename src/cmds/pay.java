package cmds;

import java.text.NumberFormat;
import java.text.ParsePosition;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import MeatWars.Events;
import MeatWars.main;
import objMeat.PlayerInfo;

public class pay implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player))return true;
		Player p = (Player) sender;
		PlayerInfo pi = Events.plist.get(p.getName());
		if(args.length==0){
			p.sendMessage(ChatColor.GREEN+"/pay"+ChatColor.RED+" <���> <�����> ");
		}
		else if(args.length==1){
			p.sendMessage(ChatColor.GREEN+"/pay <���>"+ChatColor.RED+" <�����> ");
		}
		else if(args.length>=2){
			if(Bukkit.getPlayer(args[0])==null){
				p.sendMessage(ChatColor.RED+"������ � ����� ����� ���, ���� �� ����������� ������� ��� ���.");
			}
			if(main.castle.players.contains(p.getName())!=main.castle.players.contains(Bukkit.getPlayer(args[0]).getName())){
				p.sendMessage(ChatColor.RED+"��� ����.");
				return true;
			}
			else if(args[0].equals(p.getName())){
				p.sendMessage(ChatColor.RED+"������ ���� ���������� ������... ���������� � ���������.");
			}
			else if(!isNumeric((args[1]))){
				p.sendMessage(ChatColor.RED+"����� ��������� ������ ����� �����.");
			}
			else if(Integer.parseInt(args[1])<=0){
				p.sendMessage(ChatColor.RED+"�����, ��������� �����, �������, ����� ���� �� ������������� ���-�� ������ ���� ���������� �� ����� ��������...");
			}
			else if(Integer.parseInt(args[1])>pi.money){
				p.sendMessage(ChatColor.RED+"� ��� ������� ���. �������, �� �� �� ���, ��� ����� ����������� ��� �������...");
			}
			else{
				int m=Integer.parseInt(args[1]);
				pi.money-=m;
				p.sendMessage(ChatColor.BLUE+"��������, "+ChatColor.YELLOW+args[1]+" ������"+ChatColor.BLUE+"... ������ ��� ������ - "+ChatColor.YELLOW+args[0]);
				Player p2 = Bukkit.getPlayer(args[0]);
				PlayerInfo pi2 = Events.plist.get(p2.getName());
				pi2.money+=m;
				p2.sendMessage(ChatColor.GOLD+args[1]+" ������"+ChatColor.GREEN+" ������ "+ChatColor.YELLOW+p.getName()+ChatColor.GREEN+" ������ ����!");
			}
			return true;
		}
		return true;
	}
	public static boolean isNumeric(String str)
	{
	  NumberFormat formatter = NumberFormat.getInstance();
	  ParsePosition pos = new ParsePosition(0);
	  formatter.parse(str, pos);
	  return str.length() == pos.getIndex();
	}
}
