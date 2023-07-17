package MeatWars;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import utilsMeat.GepUtil;

public class save implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.isOp()){
			saveLocs();
			saveTeams();
			sender.sendMessage(ChatColor.GREEN+"saved.");
		}
		return true;
	}
	void saveLocs(){
		File file = new File(main.instance.getDataFolder()+File.separator+"locs.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		GepUtil.saveLocToConf(conf, "wait", main.wait);
		GepUtil.saveCfg(conf, file);
	}
	public static void saveTeams(){
		File file = new File(main.instance.getDataFolder()+File.separator+"teams.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		main.wars.save(conf);
		main.castle.save(conf);
		GepUtil.saveCfg(conf, file);
	}
}
