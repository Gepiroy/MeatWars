package objMeat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import utilsMeat.GepUtil;

public class Team {
	public List<String> players = new ArrayList<>();
	public Location base;
	public Location meat;
	public UUID weapons;
	public UUID gold;
	public UUID med;
	
	public Team(){}
	public Team(FileConfiguration conf, String st){
		if(conf.contains(st+".base"))base=GepUtil.getLocFromConf(conf, st+".base");
		if(conf.contains(st+".meat"))meat=GepUtil.getLocFromConf(conf, st+".meat");
		if(conf.contains(st+".weapons"))weapons=UUID.fromString(conf.getString(st+".weapons"));
		if(conf.contains(st+".gold"))gold=UUID.fromString(conf.getString(st+".gold"));
		if(conf.contains(st+".med"))med=UUID.fromString(conf.getString(st+".med"));
	}
	public void save(FileConfiguration conf, String st){
		GepUtil.saveLocToConf(conf, st+".base", base);
		GepUtil.saveLocToConf(conf, st+".meat", meat);
		if(weapons!=null)conf.set(st+".weapons", weapons+"");
		if(gold!=null)conf.set(st+".gold", gold+"");
		if(med!=null)conf.set(st+".med", med+"");
	}
}
