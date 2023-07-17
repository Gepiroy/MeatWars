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
			p.sendMessage(ChatColor.GREEN+"/pay"+ChatColor.RED+" <Ник> <Сумма> ");
		}
		else if(args.length==1){
			p.sendMessage(ChatColor.GREEN+"/pay <Ник>"+ChatColor.RED+" <Сумма> ");
		}
		else if(args.length>=2){
			if(Bukkit.getPlayer(args[0])==null){
				p.sendMessage(ChatColor.RED+"Игрока с таким ником нет, либо вы неправильно указали его ник.");
			}
			if(main.castle.players.contains(p.getName())!=main.castle.players.contains(Bukkit.getPlayer(args[0]).getName())){
				p.sendMessage(ChatColor.RED+"Это враг.");
				return true;
			}
			else if(args[0].equals(p.getName())){
				p.sendMessage(ChatColor.RED+"Самому себе отправлять деньги... Обратитесь к психиатру.");
			}
			else if(!isNumeric((args[1]))){
				p.sendMessage(ChatColor.RED+"Можно указывать только целое число.");
			}
			else if(Integer.parseInt(args[1])<=0){
				p.sendMessage(ChatColor.RED+"Давай, пробегись везде, проверь, может быть мы действительно где-то забыли тебя потроллить за такое сомнение...");
			}
			else if(Integer.parseInt(args[1])>pi.money){
				p.sendMessage(ChatColor.RED+"У вас столько нет. Надеюсь, вы не из тех, кто решил попробовать так дюпнуть...");
			}
			else{
				int m=Integer.parseInt(args[1]);
				pi.money-=m;
				p.sendMessage(ChatColor.BLUE+"Прощайте, "+ChatColor.YELLOW+args[1]+" золота"+ChatColor.BLUE+"... Теперь ваш хозяин - "+ChatColor.YELLOW+args[0]);
				Player p2 = Bukkit.getPlayer(args[0]);
				PlayerInfo pi2 = Events.plist.get(p2.getName());
				pi2.money+=m;
				p2.sendMessage(ChatColor.GOLD+args[1]+" золота"+ChatColor.GREEN+" игрока "+ChatColor.YELLOW+p.getName()+ChatColor.GREEN+" теперь Ваши!");
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
