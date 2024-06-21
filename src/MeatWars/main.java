package MeatWars;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import ShopSystem.Shop;
import ShopSystem.Shop2;
import ShopSystem.ShopEnch;
import ShopSystem.ShopItem;
import ShopSystem.ShopUpgrade;
import cmds.gift;
import cmds.pay;
import objMeat.Ach;
import objMeat.Castle;
import objMeat.GlobPInfo;
import objMeat.Mclass;
import objMeat.PlayerInfo;
import objMeat.Warriors;
import utilsMeat.ActionBar;
import utilsMeat.GepUtil;
import utilsMeat.ItemUtil;

public class main extends JavaPlugin {
	public static String stage = "wait";
	public static String MeatStage = "wall";
	public static int timer = 120;
	public static main instance;
	public static Castle castle = new Castle();
	public static Warriors wars = new Warriors();
	public static Location wait;
	public static HashMap<String, Shop> shops = new HashMap<>();
	public static HashMap<String, Shop2> shopsUps = new HashMap<>();
	public static HashMap<String, Integer> money = new HashMap<>();
	public static ArrayList<Location> resps = new ArrayList<>();
	public static ArrayList<Ach> achs = new ArrayList<>();
	public static int wave = 1;
	public static int castleBoost = 1;
	public static int warBoost = 1;
	public static boolean full = true;
	// public static SQLConnection con;
	// public static SRVConnection scon;
	public static List<String> ghosts = new ArrayList<>();
	public static int colorFactor = 0;

	public void onEnable() {
		instance = this;
		/*
		 * con = new SQLConnection(); con.connect(); scon = new SRVConnection();
		 * scon.connect();
		 */
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		//File gfile = new File(main.instance.getDataFolder() + File.separator + "global.yml");
		//FileConfiguration gconf = YamlConfiguration.loadConfiguration(gfile);
		/*
		 * if(gconf.contains("ServerName")){ scon.server=gconf.getString("ServerName");
		 * } else{ gconf.set("ServerName", "allahakbar"); scon.server=("ServerName");
		 * GepUtil.saveCfg(gconf, gfile); } scon.SetText("Ip",
		 * Bukkit.getServer().getIp()); scon.SetInt("Port",
		 * Bukkit.getServer().getPort()); scon.SetInt("CanPlay", 1); scon.SetInt("Type",
		 * 3);
		 */
		new PlayerInfo(null);
		new ActionBar("");
		new GlobPInfo();

		ghosts.add("AIR");
		ghosts.add("STEP");
		ghosts.add("ROSE");
		ghosts.add("FLOWER");
		ghosts.add("LONG_GRASS");

		achs.add(new Ach(ChatColor.LIGHT_PURPLE + "� �������!",
				new String[] { ChatColor.YELLOW + "����� � CastleRush �������." }));
		achs.add(new Ach(ChatColor.DARK_RED + "FATALITY",
				new String[] { ChatColor.YELLOW + "�������� ������ �� ��������,",
						ChatColor.YELLOW + "����� ����� ���������� 100%." }));
		achs.add(new Ach(ChatColor.AQUA + "�����-���", new String[] { ChatColor.YELLOW + "������� ��� �� ���� 5." }));
		achs.add(new Ach(ChatColor.GOLD + "������� ��������",
				new String[] { ChatColor.YELLOW + "����� �� ���������, ������� ������",
						ChatColor.YELLOW + "���� � �������������� 1000 ������." }));
		achs.add(new Ach(ChatColor.RED + "����� � ���",
				new String[] { ChatColor.YELLOW + "��������� 1000 ������ �� �����." }));
		achs.add(new Ach(ChatColor.RED + "����!", new String[] { ChatColor.YELLOW + "������� 50 ��� �� ����,",
				ChatColor.YELLOW + "������� ������ 5 ����." }));
		achs.add(new Ach(ChatColor.GOLD + "���� �� ����", new String[] {
				ChatColor.YELLOW + "��������, �� ��������� ���", ChatColor.YELLOW + "�� ���� ������� ����." }));
		achs.add(new Ach(ChatColor.AQUA + "������� ������� ���������.",
				new String[] { ChatColor.YELLOW + "�������� ����� ��������� � ������� ������",
						ChatColor.YELLOW + "(��� ���������� ��� ���� ���. ����������",
						ChatColor.YELLOW + "���� 80%, ��� �������� - ���� 20%.)" }));
		achs.add(new Ach(ChatColor.GOLD + "����� ������!",
				new String[] { ChatColor.YELLOW + "��������, ���� ������ � ��������",
						ChatColor.YELLOW + "����� �� ���� ������� ����." }));
		achs.add(new Ach(ChatColor.AQUA + "����������", new String[] { ChatColor.YELLOW + "�������� �� 1 �����.", }));

		List<ItemStack> itemsCdef = new ArrayList<>();
		itemsCdef.add(ItemUtil.createTool(Material.WOOD_SWORD, ChatColor.GOLD + "������������� ���", null, null, 0));
		List<String> loreCdef = new ArrayList<>();
		loreCdef.add(ChatColor.GREEN + "����� � ����� ����� ��");
		loreCdef.add(ChatColor.GREEN + "������ ���� 50%!");
		loreCdef.add(ChatColor.GREEN + "������ ��� ���������� ������");
		loreCdef.add(ChatColor.GREEN + "35% (������ 50%)!");
		GUI.Cclasses.add(new Mclass(itemsCdef, Material.WOOD_SWORD, ChatColor.BLUE + "��������", loreCdef, 0));

		List<ItemStack> itemsCarch = new ArrayList<>();
		itemsCarch.add(ItemUtil.createTool(Material.BOW, ChatColor.GOLD + "���", null, null, 0));
		itemsCarch.add(ItemUtil.create(Material.ARROW, 10, 0, null, null, null, 0));
		List<String> loreCarch = new ArrayList<>();
		loreCarch.add(ChatColor.GREEN + "�� ����� �������� 16 �����.");
		loreCarch.add(ChatColor.GREEN + "�� �������� �������� 2 ������.");
		loreCarch.add(ChatColor.GREEN + "����� ����� ���� �������� 75%.");
		GUI.Cclasses.add(new Mclass(itemsCarch, Material.BOW, ChatColor.DARK_GREEN + "������", loreCarch, 125));

		List<ItemStack> itemsCkuz = new ArrayList<>();
		List<String> loreCkuz = new ArrayList<>();
		loreCkuz.add(ChatColor.GREEN + "��� ������� ��������, 75%");
		loreCkuz.add(ChatColor.GREEN + "���� ��� ��������/�������/��������.");
		GUI.Cclasses.add(new Mclass(itemsCkuz, Material.ANVIL, ChatColor.RED + "������", loreCkuz, 125));

		List<ItemStack> itemsCeco = new ArrayList<>();
		List<String> loreCeco = new ArrayList<>();
		loreCeco.add(ChatColor.GREEN + "+50 ������ �� ������.");
		loreCeco.add(ChatColor.GREEN + "��������� � ����� �� 10% �������");
		GUI.Cclasses.add(new Mclass(itemsCeco, Material.GOLD_INGOT, ChatColor.GOLD + "���������", loreCeco, 125));

		List<ItemStack> itemsWdef = new ArrayList<>();
		itemsWdef.add(ItemUtil.createTool(Material.WOOD_SWORD, ChatColor.GOLD + "������", null, null, 0));
		List<String> loreWdef = new ArrayList<>();
		loreWdef.add(ChatColor.GREEN + "��� ����������� �������� ��������");
		loreWdef.add(ChatColor.GREEN + "������ ���� � ��������.");
		loreWdef.add(ChatColor.GREEN + "��� ������ ������ �����������");
		loreWdef.add(ChatColor.GREEN + "� ������ 2% (������ 5%).");
		GUI.Wclasses.add(new Mclass(itemsWdef, Material.WOOD_SWORD, ChatColor.RED + "������", loreWdef, 0));

		List<ItemStack> itemsWrush = new ArrayList<>();
		itemsWrush.add(ItemUtil.createTool(Material.GOLD_AXE, ChatColor.GOLD + "����� �������", null, null, 0));
		List<String> loreWrush = new ArrayList<>();
		GUI.Wclasses.add(new Mclass(itemsWrush, Material.GOLD_AXE, ChatColor.GOLD + "�����", loreWrush, 125));

		List<ItemStack> itemsWrob = new ArrayList<>();
		List<String> loreWrob = new ArrayList<>();
		loreWrob.add(ChatColor.GREEN + "��� �������� ���������, ����� 5%");
		loreWrob.add(ChatColor.GREEN + "������ ����� ��������� (� ����� ������).");
		loreWrob.add(ChatColor.GREEN + "� ����� ���� �������� �� 15% ������");
		loreWrob.add(ChatColor.GREEN + "������������� ������.");
		GUI.Wclasses.add(new Mclass(itemsWrob, Material.IRON_PICKAXE, ChatColor.RED + "���", loreWrob, 125));

		List<ItemStack> itemsWwall = new ArrayList<>();
		List<String> loreWwall = new ArrayList<>();
		loreWwall.add(ChatColor.GREEN + "35% ���� �� �������� ������!");
		loreWwall.add(ChatColor.GREEN + "x1.5 ����� �� �����!");
		loreWwall.add(ChatColor.GREEN + "15% ���� ��� ��������� �����");
		loreWwall.add(ChatColor.GREEN + "���������� ���� ������ ��-����!");
		GUI.Wclasses.add(new Mclass(itemsWwall, Material.CLAY_BRICK, ChatColor.GOLD + "�����������", loreWwall, 125));

		List<ItemStack> itemsWnin = new ArrayList<>();
		List<String> loreWnin = new ArrayList<>();
		loreWnin.add(ChatColor.GREEN + "��� ����������� � ������ ���������");
		loreWnin.add(ChatColor.GREEN + "������ ��������!");
		loreWnin.add(ChatColor.GREEN + "��� ����������� 3 �������");
		loreWnin.add(ChatColor.GREEN + "���������� �����������!");
		GUI.Wclasses.add(new Mclass(itemsWnin, Material.FEATHER, ChatColor.BLUE + "������", loreWnin, 125));

		shops.put("warWeap", new Shop(ChatColor.RED + "�������", new ArrayList<>()));
		shops.get("warWeap").sitems.add(
				new ShopItem(ItemUtil.createTool(Material.WOOD_SWORD, ChatColor.GOLD + "������", null, null, 0), 25));
		shops.get("warWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.STONE_SWORD, ChatColor.GOLD + "��������", null, null, 0), 100));
		shops.get("warWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.STONE_AXE, ChatColor.GOLD + "�������� �� �����", null, null, 0), 125));
		shops.get("warWeap").sitems.add(
				new ShopItem(ItemUtil.createTool(Material.IRON_SWORD, ChatColor.RED + "�����", null, null, 0), 250));
		shops.get("warWeap").sitems.add(
				new ShopItem(ItemUtil.createTool(Material.IRON_AXE, ChatColor.RED + "��������", null, null, 0), 300));
		shops.get("warWeap").sitems.add(
				new ShopItem(ItemUtil.createTool(Material.LEATHER_HELMET, ChatColor.RED + "����", null, null, 0), 25));
		shops.get("warWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.LEATHER_CHESTPLATE, ChatColor.RED + "����", null, null, 0), 40));
		shops.get("warWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.LEATHER_LEGGINGS, ChatColor.RED + "����", null, null, 0), 35));
		shops.get("warWeap").sitems.add(
				new ShopItem(ItemUtil.createTool(Material.LEATHER_BOOTS, ChatColor.RED + "����", null, null, 0), 20));
		shops.get("warWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.CHAINMAIL_HELMET, ChatColor.RED + "������", null, null, 0), 60));
		shops.get("warWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.CHAINMAIL_CHESTPLATE, ChatColor.RED + "������", null, null, 0), 100));
		shops.get("warWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.CHAINMAIL_LEGGINGS, ChatColor.RED + "������", null, null, 0), 85));
		shops.get("warWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.CHAINMAIL_BOOTS, ChatColor.RED + "������", null, null, 0), 50));
		shops.get("warWeap").sitems.add(
				new ShopItem(ItemUtil.createTool(Material.IRON_HELMET, ChatColor.RED + "�������", null, null, 0), 150));
		shops.get("warWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.IRON_CHESTPLATE, ChatColor.RED + "�������", null, null, 0), 250));
		shops.get("warWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.IRON_LEGGINGS, ChatColor.RED + "�������", null, null, 0), 200));
		shops.get("warWeap").sitems.add(
				new ShopItem(ItemUtil.createTool(Material.IRON_BOOTS, ChatColor.RED + "�������", null, null, 0), 125));
		if (full)
			shops.get("warWeap").sitems
					.add(new ShopItem(
							ItemUtil.createTool(Material.GOLD_AXE, ChatColor.GOLD + "����� �������",
									new String[] { ChatColor.GOLD + "�������� ����� �� ������� � �����",
											ChatColor.AQUA + "�������������: " + ChatColor.GREEN + "(���)" },
									null, 0),
							150));

		shops.put("castleWeap", new Shop(ChatColor.BLUE + "�������", new ArrayList<>()));
		shops.get("castleWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.WOOD_SWORD, ChatColor.GOLD + "������������� ���", null, null, 0), 25));
		shops.get("castleWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.STONE_SWORD, ChatColor.GOLD + "�/� ���", null, null, 0), 100));
		shops.get("castleWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.STONE_AXE, ChatColor.GOLD + "�/� �����", null, null, 0), 125));
		shops.get("castleWeap").sitems.add(
				new ShopItem(ItemUtil.createTool(Material.IRON_SWORD, ChatColor.BLUE + "���", null, null, 0), 250));
		shops.get("castleWeap").sitems.add(
				new ShopItem(ItemUtil.createTool(Material.IRON_AXE, ChatColor.BLUE + "�����", null, null, 0), 300));
		shops.get("castleWeap").sitems
				.add(new ShopItem(ItemUtil.createTool(Material.BOW, ChatColor.GOLD + "���", null, null, 0), 50));
		shops.get("castleWeap").sitems.add(new ShopItem(ItemUtil.create(Material.ARROW, 2, "", null, null, 0), 5));
		shops.get("castleWeap").sitems.add(
				new ShopItem(ItemUtil.createTool(Material.LEATHER_HELMET, ChatColor.RED + "����", null, null, 0), 25));
		shops.get("castleWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.LEATHER_CHESTPLATE, ChatColor.RED + "����", null, null, 0), 40));
		shops.get("castleWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.LEATHER_LEGGINGS, ChatColor.RED + "����", null, null, 0), 35));
		shops.get("castleWeap").sitems.add(
				new ShopItem(ItemUtil.createTool(Material.LEATHER_BOOTS, ChatColor.RED + "����", null, null, 0), 20));
		shops.get("castleWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.CHAINMAIL_HELMET, ChatColor.RED + "������", null, null, 0), 60));
		shops.get("castleWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.CHAINMAIL_CHESTPLATE, ChatColor.RED + "������", null, null, 0), 100));
		shops.get("castleWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.CHAINMAIL_LEGGINGS, ChatColor.RED + "������", null, null, 0), 85));
		shops.get("castleWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.CHAINMAIL_BOOTS, ChatColor.RED + "������", null, null, 0), 50));
		shops.get("castleWeap").sitems.add(
				new ShopItem(ItemUtil.createTool(Material.IRON_HELMET, ChatColor.RED + "�������", null, null, 0), 150));
		shops.get("castleWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.IRON_CHESTPLATE, ChatColor.RED + "�������", null, null, 0), 250));
		shops.get("castleWeap").sitems.add(new ShopItem(
				ItemUtil.createTool(Material.IRON_LEGGINGS, ChatColor.RED + "�������", null, null, 0), 200));
		shops.get("castleWeap").sitems.add(
				new ShopItem(ItemUtil.createTool(Material.IRON_BOOTS, ChatColor.RED + "�������", null, null, 0), 125));
		if (full)
			shops.get("castleWeap").sitems
					.add(new ShopItem(
							ItemUtil.createTool(Material.FLINT, ChatColor.RED + "���������",
									new String[] { ChatColor.GOLD + "����� ������������, ���",
											ChatColor.GOLD + "�������� �������, �� �����",
											ChatColor.GOLD + "������������ ������.",
											ChatColor.AQUA + "�������������: " + ChatColor.GREEN + "(���)" },
									null, 0),
							150));

		shopsUps.put("warGold", new Shop2(ChatColor.RED + "����", new ArrayList<>()));
		// int Max, double PriceUp, int Start
		shopsUps.get("warGold").sitems.add(new ShopUpgrade(
				ItemUtil.createTool(Material.IRON_PICKAXE, ChatColor.GOLD + "���������� �����",
						new String[] { ChatColor.GREEN + "���� �������� +1 ����� �� ������" }, null, 0),
				100, "steal", 3, 2, 1));
		if (full)
			shopsUps.get("warGold").sitems.add(new ShopUpgrade(
					ItemUtil.createTool(Material.DIAMOND_AXE, ChatColor.GOLD + "���� ������",
							new String[] { ChatColor.GREEN + "+2 ���. �������� ������ �������." }, null, 0),
					100, "axeRush", 2, 2, 0));
		if (full)
			shopsUps.get("warGold").sitems.add(new ShopUpgrade(
					ItemUtil.createTool(Material.GOLD_AXE, ChatColor.GOLD + "�������� ������",
							new String[] { ChatColor.GREEN + "-3 ���. �/� ������ �������." }, Enchantment.DIG_SPEED, 1),
					100, "axeReload", 2, 2, 0));

		shopsUps.put("castleGold", new Shop2(ChatColor.BLUE + "����", new ArrayList<>()));
		shopsUps.get("castleGold").sitems.add(new ShopUpgrade(
				ItemUtil.createTool(Material.GOLD_INGOT, ChatColor.GOLD + "�����",
						new String[] { ChatColor.GREEN + "+200 ������ � ����. ������ �� �����." }, null, 0),
				100, "waveGold", 2, 2, 0));
		if (full)
			shopsUps.get("castleGold").sitems.add(new ShopUpgrade(
					ItemUtil.createTool(Material.FLINT, ChatColor.GOLD + "������� ���������",
							new String[] { ChatColor.GREEN + "+1 ���. ����������� � ����������." }, null, 0),
					50, "jailTime", 3, 2, 0));
		shopsUps.get(
				"castleGold").sitems.add(
						new ShopUpgrade(
								ItemUtil.createTool(Material.EMERALD, ChatColor.GOLD + "�������� �������",
										new String[] { ChatColor.GREEN + "+5$ � +1% �������� $",
												ChatColor.GREEN + "�� ������ ��������." },
										null, 0),
								50, "killGold", 3, 2, 0));

		shopsUps.put("castleMed", new Shop2(ChatColor.BLUE + "���������", new ArrayList<>()));
		shopsUps.get("castleMed").sitems.add(new ShopUpgrade(
				ItemUtil.createTool(Material.RED_ROSE, ChatColor.LIGHT_PURPLE + "������ ��������",
						new String[] { ChatColor.GREEN + "����������� ��� �� 0.5 ��� �������." }, null, 0),
				75, "regen", 3, 1.5, 0));
		shopsUps.get("castleMed").sitems.add(new ShopUpgrade(
				ItemUtil.createTool(Material.FEATHER, ChatColor.AQUA + "�����",
						new String[] { ChatColor.GREEN + "+3 ���. �������� ��� �����������." }, null, 0),
				75, "speedTime", 3, 2, 0));

		shopsUps.put("warMed", new Shop2(ChatColor.RED + "���������", new ArrayList<>()));
		shopsUps.get("warMed").sitems.add(new ShopUpgrade(
				ItemUtil.createTool(Material.RED_ROSE, ChatColor.LIGHT_PURPLE + "������ ��������",
						new String[] { ChatColor.GREEN + "����������� ��� �� 0.5 ��� �������." }, null, 0),
				75, "regen", 3, 1.5, 0));
		shopsUps.get("warMed").sitems.add(new ShopUpgrade(
				ItemUtil.createTool(Material.FEATHER, ChatColor.AQUA + "�����",
						new String[] { ChatColor.GREEN + "+3 ���. �������� ��� �����������." }, null, 0),
				100, "speedTime", 3, 2, 0));

		Bukkit.getPluginManager().registerEvents(new Events(), this);
		Bukkit.getPluginManager().registerEvents(new GUI(), this);
		GUI.enchs.put(2, new ShopEnch(ItemUtil.create(Material.FEATHER, ChatColor.AQUA + "������ I"),
				Enchantment.KNOCKBACK, 1, 50));
		GUI.enchs.put(6, new ShopEnch(ItemUtil.create(Material.FIREBALL, ChatColor.GOLD + "����� I"),
				Enchantment.FIRE_ASPECT, 1, 75));
		GUI.enchs.put(15, new ShopEnch(ItemUtil.create(Material.FIREBALL, ChatColor.GOLD + "����� II"),
				Enchantment.FIRE_ASPECT, 2, 150));

		GUI.bowenchs.put(2, new ShopEnch(ItemUtil.create(Material.FEATHER, ChatColor.AQUA + "����������� I"),
				Enchantment.ARROW_KNOCKBACK, 1, 150));
		GUI.bowenchs.put(11, new ShopEnch(ItemUtil.create(Material.FEATHER, ChatColor.AQUA + "����������� II"),
				Enchantment.ARROW_KNOCKBACK, 2, 300));
		GUI.bowenchs.put(6, new ShopEnch(ItemUtil.create(Material.FIREBALL, ChatColor.GOLD + "����� I"),
				Enchantment.ARROW_FIRE, 1, 75));
		GUI.bowenchs.put(15, new ShopEnch(ItemUtil.create(Material.FIREBALL, ChatColor.GOLD + "����� II"),
				Enchantment.ARROW_FIRE, 2, 150));

		GUI.armenchs.put(2, new ShopEnch(ItemUtil.create(Material.IRON_SWORD, ChatColor.GRAY + "���� I"),
				Enchantment.THORNS, 1, 75));
		GUI.armenchs.put(11, new ShopEnch(ItemUtil.create(Material.IRON_SWORD, ChatColor.GRAY + "���� II"),
				Enchantment.THORNS, 2, 150));
		GUI.armenchs.put(5, new ShopEnch(ItemUtil.create(Material.FIREBALL, ChatColor.GOLD + "����-����� I"),
				Enchantment.PROTECTION_FIRE, 1, 45));
		GUI.armenchs.put(14, new ShopEnch(ItemUtil.create(Material.FIREBALL, ChatColor.GOLD + "����-����� II"),
				Enchantment.PROTECTION_FIRE, 2, 90));
		GUI.armenchs.put(23, new ShopEnch(ItemUtil.create(Material.FIREBALL, ChatColor.GOLD + "����-����� III"),
				Enchantment.PROTECTION_FIRE, 3, 150));
		GUI.armenchs.put(7, new ShopEnch(ItemUtil.create(Material.ARROW, ChatColor.WHITE + "����-������ I"),
				Enchantment.PROTECTION_PROJECTILE, 1, 55));
		GUI.armenchs.put(16, new ShopEnch(ItemUtil.create(Material.ARROW, ChatColor.WHITE + "����-������ II"),
				Enchantment.PROTECTION_PROJECTILE, 2, 110));
		GUI.armenchs.put(25, new ShopEnch(ItemUtil.create(Material.ARROW, ChatColor.WHITE + "����-������ III"),
				Enchantment.PROTECTION_PROJECTILE, 3, 175));

		Bukkit.getPluginCommand("save").setExecutor(new save());
		Bukkit.getPluginCommand("edit").setExecutor(new edit());
		Bukkit.getPluginCommand("pay").setExecutor(new pay());
		Bukkit.getPluginCommand("gift").setExecutor(new gift());
		File file = new File(main.instance.getDataFolder() + File.separator + "locs.yml");
		if (!file.exists()) {
			file.mkdir();
		}
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		wait = GepUtil.getLocFromConf(conf, "wait");
		File Tfile = new File(getDataFolder() + File.separator + "teams.yml");
		if (!Tfile.exists()) {
			Tfile.mkdir();
		}
		FileConfiguration Tconf = YamlConfiguration.loadConfiguration(Tfile);
		wars = new Warriors(Tconf);
		castle = new Castle(Tconf);
		doStart();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			int secrate = 0;
			int displayRate = 0;
			int help = 0;

			@Override
			public void run() {
				colorFactor++;
				secrate++;
				displayRate++;
				if (secrate >= 20) {
					secrate -= 20;
					if (Bukkit.getOnlinePlayers().size() >= 2 || !stage.equals("wait"))
						timer--;
					if (stage.equals("wait")) {
						if (timer != 120 && Bukkit.getOnlinePlayers().size() < 2) {
							GepUtil.globMessage(ChatColor.RED + "���-�� �����, ������ ����� ������ �� 120 ���.");
							timer = 120;
						}
						if (timer > 30 && Events.ready.size() >= Bukkit.getOnlinePlayers().size()) {
							timer = 30;
							GepUtil.globMessage(ChatColor.AQUA + "������ �������� �� 30!");
						}
						help++;
						if (help >= 10) {
							help -= 10;
							String[] mess = { "���� ����� �������, ���� ���� - ������.",
									"���� �� �������� ������ ����������� � 50% ����������.",
									"���� ��������� �� 2 ������. ��� ���������� ���� 40% ����������, � �� 20.",
									"��� ������ ������ �������� - ��� ������ ����� �������. ��� ������ ����� ���������� - ��� ������ ������� ���������.",
									"���� �������� - ������, � �� ������. ������ ���.",
									"���� �� ������, ���� ������� ������ - ����� �� � ����.",
									"���������� - ���� ������ ���� �� ������ ����������.",
									"��������� � ����� ����� ������� ���� �� ���� �����, �� �� ���������, ��� �����-�� ������ ����������...",
									"�� ������ ��������? ������ ������, � ���������� ������!",
									"����� �����, ����� � ������� � ���� ���� ���� � ������ �������� ����.",
									"�� ���������, ��� �����, � ��� ����? ������ �� ������: ������� - ����, ������� - ����������, ������� - ���������, �������� - �����." };
							String mes = mess[new Random().nextInt(mess.length)];
							GepUtil.globMessage(ChatColor.AQUA + "[���������] " + ChatColor.GREEN + mes,
									Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0, null, null, 0, 0, 0);
						}
					}
					if (stage.equals("shop")) {
						if (timer == 90) {
							if (wave == 1 && !full)
								for (String st : main.wars.players) {
									if (Bukkit.getPlayer(st) != null) {
										Player p = Bukkit.getPlayer(st);
										p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + ChatColor.UNDERLINE
												+ "---[���������!]---");
										p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "* ��� ��� ������? *");
										p.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD
												+ "* �� ����� � ����� ��� ������ ���������!");
										p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD
												+ "* ���� ��� 40% ���������� (������ �� 20) � x2 ������!");
										p.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD
												+ "* ����� ���������, ����� ��������� ���������!");
										p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + ChatColor.UNDERLINE
												+ "---[���������!]---");
									}
								}
							if (wave == 1 && !full)
								for (String st : main.castle.players) {
									if (Bukkit.getPlayer(st) != null) {
										Player p = Bukkit.getPlayer(st);
										p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + ChatColor.UNDERLINE
												+ "---[���������!]---");
										p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "* ��� ��� �����? *");
										p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD
												+ "* �� ������� � ����� �� ������� ���������!");
										p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD
												+ "* ���� ��� 40% ���������� (������ �� 20)!");
										p.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD
												+ "* ����� ���������, �� ��������� ����������!");
										p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + ChatColor.UNDERLINE
												+ "---[���������!]---");
									}
								}
							GepUtil.globMessage(
									ChatColor.AQUA + "[Info]" + ChatColor.YELLOW + "" + ChatColor.BOLD
											+ "������� ����� ������ ����, ���� ������� �� ������ ������.",
									Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 2, null, null, 0, 0, 0);
						}
						if (timer > 5 && timer < 90) {
							int ready = 0;
							for (String st : wars.players) {
								Player p = Bukkit.getPlayer(st);
								Block b = p.getLocation().subtract(0, 1, 0).getBlock();
								if (b.getType().equals(Material.WOOL)) {
									Wool w = (Wool) b.getState().getData();
									if (w.getColor().equals(DyeColor.RED)) {
										ready++;
									}
								}
							}
							if (100.0 / wars.players.size() * ready >= 80) {
								timer = 5;
								GepUtil.globMessage(ChatColor.BLUE + ">>>����� ��������� �� 5 ���.>>>",
										Sound.ENTITY_ARROW_SHOOT, 10, 0, null, null, 0, 0, 0);
							}
						}
					}
					if (timer <= 0) {
						changeStage();
					}
				}
				if (displayRate >= 5) {
					displayRate = 0;
					for (Player p : Bukkit.getOnlinePlayers()) {
						PlayerInfo pi = Events.plist.get(p.getName());
						GlobPInfo gpi = Events.gplist.get(p.getName());
						List<String> strings = new ArrayList<>();
						if (!stage.equals("wait"))
							strings.add(ChatColor.RED + "�����: " + ChatColor.GOLD + wave + ChatColor.YELLOW + "/5");
						if (stage.equals("wait")) {
							if (Bukkit.getOnlinePlayers().size() >= 2)
								strings.add(ChatColor.GREEN + "�� ������: " + ChatColor.AQUA + timer + " ���.");
							else
								strings.add(ChatColor.YELLOW + "�������� �������...");
							strings.add(ChatColor.YELLOW + "����� ���: " + ChatColor.GOLD + gpi.games);
							strings.add(ChatColor.GREEN + "�����: " + ChatColor.AQUA + gpi.wons);
						} else if (stage.equals("shop")) {
							strings.add(ChatColor.GOLD + "����������: " + ChatColor.YELLOW + timer + " ���.");
						} else if (stage.equals("upTime")) {
							if (!castle.players.contains(p.getName()))
								strings.add(ChatColor.RED + "����������: " + ChatColor.YELLOW + timer + " ���.");
							else
								strings.add(ChatColor.GRAY + "���������: " + ChatColor.YELLOW + timer + " ���.");
							strings.add(ChatColor.GOLD + "����������: " + ChatColor.YELLOW + pi.sMoney);
						} else {
							String[] prists = { ";", "'", "/", "%", "_", "-", "=" };
							String prist1 = prists[new Random().nextInt(prists.length)];
							String prist2 = prists[new Random().nextInt(prists.length)];
							String bord = "";
							for (int i = 0; i < 10; i++) {
								if (timer / 21.0 > i)
									bord += GepUtil.boolString(ChatColor.YELLOW + "#", ChatColor.BLACK + "#",
											i % 2 == 0);
								else
									bord += ChatColor.GRAY + "#";
							}
							strings.add(bord);
							strings.add(ChatColor.RED + prist1 + ChatColor.DARK_RED + "" + ChatColor.BOLD + "!����!"
									+ ChatColor.RED + prist2);
							strings.add(ChatColor.GOLD + "[" + ChatColor.BOLD + ChatColor.YELLOW + timer / 60 + ":"
									+ new DecimalFormat("#00").format(timer % 60) + ChatColor.GOLD + "]");
							strings.add(ChatColor.RED + bord);
						}
						if (stage.equals("MEAT")) {
							if (wars.players.contains(p.getName()))
								strings.add(ChatColor.GOLD + "����������: " + ChatColor.YELLOW + pi.sMoney);
							else
								strings.add(ChatColor.GOLD + "����������: " + ChatColor.YELLOW + pi.sMoney);
							if (MeatStage.equals("wall"))
								strings.add(ChatColor.BLUE + "��������� �����: " + ChatColor.GOLD + castle.wall);
							if (MeatStage.equals("destroy")) {
								strings.add(ChatColor.RED + "�������: "
										+ GepUtil.boolString(ChatColor.GOLD + "" + castle.weaponHP,
												ChatColor.DARK_RED + "[�����������]", castle.weaponHP > 0));
								strings.add(ChatColor.LIGHT_PURPLE + "����������: "
										+ GepUtil.boolString(ChatColor.GOLD + "" + castle.magicHP,
												ChatColor.DARK_RED + "[�����������]", castle.magicHP > 0));
								strings.add(ChatColor.YELLOW + "����: "
										+ GepUtil.boolString(ChatColor.GOLD + "" + castle.goldHP,
												ChatColor.DARK_RED + "[�����������]", castle.goldHP > 0));
								strings.add(ChatColor.LIGHT_PURPLE + "���������: "
										+ GepUtil.boolString(ChatColor.GOLD + "" + castle.medHP,
												ChatColor.DARK_RED + "[�����������]", castle.medHP > 0));
								strings.add(ChatColor.GOLD + "% ����� ����������: " + ChatColor.RED
										+ GepUtil.CylDouble(100 - castle.getDamage(), "#0.00") + "%");
								strings.add(ChatColor.DARK_PURPLE + "*" + ChatColor.ITALIC + " ������� ������: "
										+ ChatColor.GOLD + ChatColor.UNDERLINE + castle.baseHp() + "%");
							}
						} else if (!stage.equals("wait")) {
							strings.add(ChatColor.GOLD + "������: " + ChatColor.YELLOW + pi.money);
						}
						GepUtil.upscor(p, strings, ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "-==|="
								+ ChatColor.GOLD + "._Gep_Craft_." + ChatColor.RED + ChatColor.STRIKETHROUGH + "=|==-");
					}
				}
				// DISPLAYUP!!!!!
				for (Player p : Bukkit.getOnlinePlayers()) {
					PlayerInfo pi = Events.plist.get(p.getName());
					if (getKit(p.getName(), isCastle(p)).equals(ChatColor.BLUE + "������")) {
						boolean pef = false;
						for (String st : castle.players) {
							Player pl = Bukkit.getPlayer(st);
							if (pl.getLocation().distance(p.getLocation()) <= 3) {
								pef = true;
								break;
							}
						}
						if (pef) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 25, 0, true, false), true);
						}
					}
					if (stage.equals("MEAT") && pi.bools.contains("noSprint") && p.isSprinting()) {
						pi.bools.remove("noSprint");
						if (wave > 1) {
							p.sendMessage(ChatColor.RED + "���������� " + ChatColor.GOLD + "'���� �� ����' "
									+ ChatColor.RED + "�� ����� ��������. (�� ��������)");
						}
					}
					if (pi.bools.contains("head") && stage.equals("MEAT")) {
						if (p.getInventory().getHelmet() == null
								|| !p.getInventory().getHelmet().getType().equals(Material.LEATHER_HELMET)) {
							pi.bools.remove("head");
							if (wave > 1) {
								p.sendMessage(ChatColor.RED + "���������� " + ChatColor.GOLD + "'����� ������!' "
										+ ChatColor.RED + "�� ����� ��������. (�� ������� ��� �����)");
							}
						} else if (p.getInventory().getChestplate() != null || p.getInventory().getLeggings() != null
								|| p.getInventory().getBoots() != null) {
							pi.bools.remove("head");
							if (wave > 1) {
								p.sendMessage(ChatColor.RED + "���������� " + ChatColor.GOLD + "'����� ������!' "
										+ ChatColor.RED + "�� ����� ��������. (�� ����� ������ �����)");
							}
						}
					}
					pi.regen--;
					if (pi.regen <= 0) {
						pi.regen = 50 - 10 * pi.ups.get("regen");
						double d = p.getHealth();
						d += 1;
						if (d >= p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue())
							d = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
						p.setHealth(d);
					}
					for (String st : new ArrayList<>(pi.timers.keySet())) {// TODO timers
						if (GepUtil.HashMapReplacer(pi.timers, st, -1, true, false)) {
							if (st.equals("respawn")) {
								respawn(p);
							}
							if (st.equals("axeRush")) {
								p.sendMessage(ChatColor.RED + "����� ������� �������� �����������.");
							}
							if (st.equals("jail")) {
								p.sendMessage(ChatColor.RED + "��������� ��������� �����������.");
							}
							if (st.equals("unInv")) {
								for (Player pl : Bukkit.getOnlinePlayers()) {
									pl.showPlayer(p);
								}
								p.sendMessage(ChatColor.RED + "��������� ��������� �����������.");
							}
						}
					}
					if (pi.timers.containsKey("respawn")) {
						if (castle.players.contains(p.getName()) && stage.equals("MEAT") && MeatStage.equals("wall")) {
							ActionBar bar = new ActionBar(ChatColor.BLUE + "����������� ����� " + ChatColor.YELLOW
									+ (pi.timers.get("respawn") / 20 + 1));
							bar.sendToPlayer(p);
						} else
							p.sendTitle(ChatColor.RED + "�� ������!", ChatColor.GREEN + "����������� ����� "
									+ ChatColor.YELLOW + (pi.timers.get("respawn") / 20 + 1), 0, 5, 5);
					}
				}
				if (stage.equals("MEAT")) {
					if (timer <= 120 && MeatStage.equals("wall")) {
						castle.breakWall();
						GepUtil.globMessage(ChatColor.RED + "�������������� ���������� �����!");
						return;
					}
					if (MeatStage.equals("wall")) {
						if (castle.power.equals("volley")
								&& GepUtil.HashMapCounter(castle.waits, "volley", 200, true)) {
							castle.launch();
							castle.waits.remove("volley");
						}
					}
					for (String st : wars.players) {
						Player p = Bukkit.getPlayer(st);
						PlayerInfo pi = Events.plist.get(st);
						if (!p.getGameMode().equals(GameMode.ADVENTURE))
							continue;
						if (MeatStage.equals("wall")) {
							if (p.getLocation().getBlockZ() + 1 >= castle.wallBreak.getBlockZ()
									&& !pi.timers.containsKey("wallHurt")) {
								castle.wall -= 1;
								if (pi.timers.containsKey("axeRush"))
									castle.wall -= 1;
								GepUtil.globMessage(null, Sound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 10, 1, null, null, 0, 0,
										0);
								pi.timers.put("wallHurt", 30);
								Vector v = new Vector(0, 0.25, -1);
								p.setVelocity(v);
								p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1, 1));
								if (castle.wall <= 0) {
									castle.breakWall();
									break;
								}
							}
						} else {
							int coef = new Random().nextInt(pi.ups.get("steal")) + 1;
							boolean axe = pi.timers.containsKey("axeRush");
							if (axe)
								coef *= 2;
							int get = (new Random().nextInt(3) + 2) * coef * 3;
							if (get < 4 * coef && new Random().nextBoolean())
								get += 1;
							if (axe)
								get *= 0.85;
							boolean free = true;
							if (castle.weaponHP > 0 && p.getLocation().distance(castle.weaponRush) <= 10
									&& pointed(castle.weaponRush, p)) {
								if (pi.steal == null)
									pi.setSteal(castle.weaponRush);
								if (pi.steal(p)) {
									pi.setSteal(castle.weaponRush);
									castle.weaponHP -= coef;
									p.getWorld().playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 2, 1);
									pi.sMoney += get;
									for (String s : castle.players) {
										Player pl = Bukkit.getPlayer(s);
										if (pl == null)
											continue;
										double chance = 0.5;
										if (getKit(s, true).equals(ChatColor.BLUE + "��������"))
											chance = 0.35;
										for (ItemStack item : pl.getInventory()) {
											if (item != null && item.hasItemMeta()
													&& item.getEnchantments().size() > 0) {
												for (Enchantment ench : item.getEnchantments().keySet()) {
													if (ench.equals(Enchantment.DAMAGE_ALL)
															|| ench.equals(Enchantment.ARROW_DAMAGE)
															|| ench.equals(Enchantment.PROTECTION_ENVIRONMENTAL)) {
														double c = 1 - Math.exp(
																Math.log(chance * coef) / 20 * wars.players.size());
														if (new Random().nextDouble() <= c) {
															item.removeEnchantment(ench);
															p.sendMessage(
																	ChatColor.RED + "׸��! ��� ������ ����������� "
																			+ ChatColor.LIGHT_PURPLE + ChatColor.ITALIC
																			+ ench + ChatColor.RED + " �� "
																			+ ChatColor.GRAY + item.getType());
															p.sendMessage(ChatColor.GOLD + "�, ����������� "
																	+ ChatColor.LIGHT_PURPLE + ChatColor.ITALIC + ench
																	+ ChatColor.GOLD + " �� " + ChatColor.GRAY
																	+ item.getType() + ChatColor.GOLD + " ������ "
																	+ ChatColor.AQUA + pl.getName() + ChatColor.GOLD
																	+ " ��������!");
														}
													}
												}
											}
										}
									}
								}
								free = false;
							}
							if (castle.magicHP > 0 && p.getLocation().distance(castle.magicRush) <= 10
									&& pointed(castle.magicRush, p)) {
								if (pi.steal == null)
									pi.setSteal(castle.magicRush);
								if (pi.steal(p)) {
									pi.setSteal(castle.magicRush);
									castle.magicHP -= coef;
									p.getWorld().playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 2, 1);
									pi.sMoney += get;
									for (String s : castle.players) {
										Player pl = Bukkit.getPlayer(s);
										if (pl == null)
											continue;
										double chance = 0.5;
										if (getKit(s, true).equals(ChatColor.BLUE + "��������"))
											chance = 0.35;
										for (ItemStack item : pl.getInventory()) {
											if (item != null && item.hasItemMeta()
													&& item.getEnchantments().size() > 0) {
												for (Enchantment ench : item.getEnchantments().keySet()) {
													if (!ench.equals(Enchantment.DAMAGE_ALL)
															&& !ench.equals(Enchantment.ARROW_DAMAGE)
															&& !ench.equals(Enchantment.PROTECTION_ENVIRONMENTAL)) {
														double c = 1 - Math.exp(
																Math.log(chance * coef) / 20 * wars.players.size());
														if (new Random().nextDouble() <= c) {
															item.removeEnchantment(ench);
															p.sendMessage(
																	ChatColor.RED + "׸��! ��� ������ ����������� "
																			+ ChatColor.LIGHT_PURPLE + ChatColor.ITALIC
																			+ ench + ChatColor.RED + " �� "
																			+ ChatColor.GRAY + item.getType());
															p.sendMessage(ChatColor.GOLD + "�, ����������� "
																	+ ChatColor.LIGHT_PURPLE + ChatColor.ITALIC + ench
																	+ ChatColor.GOLD + " �� " + ChatColor.GRAY
																	+ item.getType() + ChatColor.GOLD + " ������ "
																	+ ChatColor.AQUA + pl.getName() + ChatColor.GOLD
																	+ " ��������!");
														}
													}
												}
											}
										}
									}
								}
								free = false;
							}
							if (castle.goldHP > 0 && p.getLocation().distance(castle.goldRush) <= 10
									&& pointed(castle.goldRush, p)) {
								if (pi.steal == null)
									pi.setSteal(castle.goldRush);
								if (pi.steal(p)) {
									pi.setSteal(castle.goldRush);
									castle.goldHP -= coef;
									p.getWorld().playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 2, 1);
									pi.sMoney += get * 1.5;
								}
								free = false;
							}
							if (castle.medHP > 0 && p.getLocation().distance(castle.medRush) <= 10
									&& pointed(castle.medRush, p)) {
								if (pi.steal == null)
									pi.setSteal(castle.medRush);
								if (pi.steal(p)) {
									pi.setSteal(castle.medRush);
									castle.medHP -= coef;
									p.getWorld().playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 2, 1);
									pi.sMoney += get;
								}
								free = false;
							}
							if (castle.baseRush) {
								int take = 0;
								for (String cp : castle.players) {
									PlayerInfo cpi = Events.plist.get(cp);
									take += (cpi.sMoney / 100.0) + 1;
								}
								if (p.getLocation().distance(castle.MainBase) <= 10 && pointed(castle.MainBase, p)
										&& pi.attack()) {
									double cf = (2.5 / wars.players.size());
									castle.baseHP -= cf;
									p.getWorld().playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 2);
									for (String wp : wars.players) {
										Player wpl = Bukkit.getPlayer(wp);
										wpl.sendTitle(ChatColor.RED + "���������� �������� ������!",
												ChatColor.AQUA + "" + castle.baseHp() + "%", 5, 10, 5);
									}
									GepUtil.globMessage(null, Sound.ENTITY_ENDERDRAGON_HURT, 2,
											new Random().nextFloat() * 2, null, null, 0, 0, 0);
									if (castle.baseHP <= 0) {
										won(true);
									}
									pi.sMoney += take;
									for (String cp : castle.players) {
										Player cpl = Bukkit.getPlayer(cp);
										cpl.sendTitle(ChatColor.RED + "������� ������ �������!",
												ChatColor.RED + "" + castle.baseHp() + "%", 5, 10, 5);
										PlayerInfo cpi = Events.plist.get(cp);
										cpi.sMoney -= (cpi.sMoney / 100.0);
									}
								}
							} else if (p.getLocation().distance(castle.MainBase) <= 10 && pointed(castle.MainBase, p)
									&& pi.attack()) {
								p.sendMessage(ChatColor.RED
										+ "���� �� ��. ������ �������� ����� 50% ����������! (���� ���� 40%)");
							}
							if (free && pi.steal != null)
								pi.steal = null;
						}
						if (!castle.baseRush && castle.getDamage() <= 50) {
							GepUtil.globMessage(null, Sound.ENTITY_ENDERDRAGON_FLAP, 10, 0,
									ChatColor.RED + "" + ChatColor.BOLD + "����������� ����� 50%!",
									ChatColor.DARK_RED + "" + ChatColor.ITALIC + "�������� ���� �� ��. ������!", 20, 40,
									40);
							for (Player pl : Bukkit.getOnlinePlayers()) {
								drawLine(pl.getLocation().add(0, 1, 0), castle.MainBase, 0.1, Particle.VILLAGER_HAPPY);
							}
							castle.baseRush = true;
							timer += 20;
							GepUtil.globMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + ChatColor.BOLD
									+ ChatColor.UNDERLINE + "+20 ������!");
						}
						if (castle.weaponHP <= 0) {
							if (castle.canBreak.contains("weapon")) {
								castle.weaponHP = 0;
								castle.canBreak.remove("weapon");
								GepUtil.globMessage(
										ChatColor.RED + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "�������"
												+ ChatColor.DARK_RED + " ���� �����������!",
										Sound.ENTITY_ENDERDRAGON_FLAP, 10, 1, null, "", 0, 0, 0);
								GepUtil.globMessage(ChatColor.DARK_PURPLE
										+ "� ���������� �������� ��������� �� ���������� �� ����� ���������.");
							}

						}
						if (castle.magicHP <= 0) {
							if (castle.canBreak.contains("magic")) {
								castle.magicHP = 0;
								castle.canBreak.remove("magic");
								GepUtil.globMessage(
										ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "����������"
												+ ChatColor.DARK_RED + " ���� �����������!",
										Sound.ENTITY_ENDERDRAGON_FLAP, 10, 1, null, "", 0, 0, 0);
								GepUtil.globMessage(ChatColor.DARK_PURPLE
										+ "� ���������� �������� ����������� �� ����� ���������.");
							}
						}
						if (castle.goldHP <= 0) {
							if (castle.canBreak.contains("gold")) {
								castle.canBreak.remove("gold");
								castle.goldHP = 0;
								GepUtil.globMessage(
										ChatColor.GOLD + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "����"
												+ ChatColor.DARK_RED + " ��� ����������!",
										Sound.ENTITY_ENDERDRAGON_FLAP, 10, 1, null, "", 0, 0, 0);
								GepUtil.globMessage(ChatColor.LIGHT_PURPLE
										+ "����� ���� �� ����� �������� x2 � ������ ����������.");
							}
						}
						if (castle.medHP <= 0) {
							if (castle.canBreak.contains("med")) {
								castle.medHP = 0;
								castle.canBreak.remove("med");
								GepUtil.globMessage(
										ChatColor.RED + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "���������"
												+ ChatColor.DARK_RED + " ��� ����������!",
										Sound.ENTITY_ENDERDRAGON_FLAP, 10, 1, null, "", 0, 0, 0);
								GepUtil.globMessage(ChatColor.LIGHT_PURPLE
										+ "����� ���� �� ��������� �������� ����������� ����������.");
							}
						}
					}
					if (castle.weaponHP <= 0) {
						castle.weaponRush.getWorld().spawnParticle(Particle.SMOKE_LARGE,
								castle.weaponRush.clone().add(0, 5, 0), 10, 2, 2, 2, 0);
					}
					if (castle.magicHP <= 0) {
						castle.magicRush.getWorld().spawnParticle(Particle.SMOKE_LARGE,
								castle.magicRush.clone().add(0, 5, 0), 10, 2, 2, 2, 0);
					}
					if (castle.goldHP <= 0) {
						castle.goldRush.getWorld().spawnParticle(Particle.SMOKE_LARGE,
								castle.goldRush.clone().add(0, 5, 0), 10, 2, 2, 2, 0);
					}
					if (castle.medHP <= 0) {
						castle.medRush.getWorld().spawnParticle(Particle.SMOKE_LARGE,
								castle.medRush.clone().add(0, 5, 0), 10, 2, 2, 2, 0);
					}
				}
			}
		}, 0, 1);
		for (Player p : Bukkit.getOnlinePlayers()) {
			Events.doJoin(p);
			// Events.plist.put(p.getName(), new PlayerInfo(p));
		}
	}

	public void onDisable() {
		// scon.SetInt("CanPlay", 0);
	}

	static boolean raspr() {
		int R = 0;
		int B = 0;
		for (Player p : Bukkit.getOnlinePlayers()) {
			PlayerInfo pi = Events.plist.get(p.getName());
			if (pi.bools.contains("redSelected"))
				R++;
			if (pi.bools.contains("blueSelected"))
				B++;
		}
		for (Player p : Bukkit.getOnlinePlayers()) {
			PlayerInfo pi = Events.plist.get(p.getName());
			if (!pi.bools.contains("redSelected") && !pi.bools.contains("blueSelected")) {
				if (R > B) {
					pi.toggleBool("blueSelected", true);
					B++;
				} else if (R < B) {
					pi.toggleBool("redSelected", true);
					R++;
				} else {
					if (new Random().nextBoolean()) {
						pi.toggleBool("blueSelected", true);
						B++;
					} else {
						pi.toggleBool("redSelected", true);
						R++;
					}
				}
			}
		}
		if (Math.max(R, B) * 100 / Bukkit.getOnlinePlayers().size() <= 60 || Math.max(R, B) - Math.min(R, B) <= 1)
			return true;
		else
			return false;
	}

	public void changeStage() {
		if (stage.equals("wait")) {
			// scon.SetInt("CanPlay", 0);
			stage = "shop";
			timer = 120;
			if (!raspr()) {
				GepUtil.globMessage(
						ChatColor.GOLD + "������� ���� ������� �����������. ��� ���� �������� ������������.");
				for (Player p : Bukkit.getOnlinePlayers()) {
					PlayerInfo pi = Events.plist.get(p.getName());
					pi.toggleBool("redSelected", false);
					pi.toggleBool("blueSelected", false);
				}
				raspr();
			}
			for (Player p : Bukkit.getOnlinePlayers()) {
				PlayerInfo pi = Events.plist.get(p.getName());
				GlobPInfo gpi = Events.gplist.get(p.getName());
				p.getInventory().clear();
				if (pi.bools.contains("redSelected")) {
					wars.players.add(p.getName());
					p.teleport(wars.base);
					Mclass c = GUI.getClass(gpi.kitW, false);
					for (ItemStack it : c.items) {
						p.getInventory().addItem(it);
					}
					p.setCustomName(ChatColor.RED + p.getName());
					p.setPlayerListName(ChatColor.RED + p.getName());
				} else {
					p.setCustomName(ChatColor.BLUE + p.getName());
					p.setPlayerListName(ChatColor.BLUE + p.getName());
					castle.players.add(p.getName());
					p.teleport(castle.base);
					Mclass c = GUI.getClass(gpi.kitC, true);
					for (ItemStack it : c.items) {
						p.getInventory().addItem(it);
					}
					if (c.name.equals(ChatColor.GOLD + "���������"))
						pi.sTrans(p, 50);
					if (full)
						p.getInventory().addItem(
								ItemUtil.createTool(Material.IRON_PICKAXE, ChatColor.GRAY + "�����", null, null, 0));
				}
				if (!full) {
					p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "---[���������!]---");
					p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD
							+ "* � ���������� ����� ��������� ����, ���� � �����!");
					p.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD
							+ "* � ����� ����������� ����� ������ ������ ��������!");
					p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "---[���������!]---");
				}
			}
			if (castle.players.size() != wars.players.size()) {
				String minTeam = ChatColor.BLUE + "���������";
				if (wars.players.size() < castle.players.size())
					minTeam = ChatColor.RED + "�������";
				double coefm = 1.0 * Math.max(castle.players.size(), wars.players.size())
						/ Math.min(castle.players.size(), wars.players.size());
				double coefd = 1 + (coefm - 1) / 2;
				GepUtil.globMessage(minTeam + ChatColor.YELLOW + " - ������� �������. ��� �������� " + ChatColor.GOLD
						+ "x" + GepUtil.CylDouble(coefm, "#0.00") + " ������" + ChatColor.YELLOW + " � "
						+ ChatColor.AQUA + "� " + coefd + " ��� ������ �����.");
				GepUtil.globMessage(minTeam + ChatColor.RED + "�� ����� ��� �� ������������ � " + ChatColor.GOLD
						+ GepUtil.CylDouble(coefm, "#0.00") + " ��� ���������.");

			}
			for (Player p : Bukkit.getOnlinePlayers()) {
				PlayerInfo pi = Events.plist.get(p.getName());
				pi.sTrans(p, 125);
				Scoreboard s = p.getScoreboard();
				Team war = s.getTeam("wars");
				Team cast = s.getTeam("cast");
				for (String st : castle.players) {
					cast.addEntry(st);
				}
				for (String st : wars.players) {
					war.addEntry(st);
				}
				pi.bools.add("noSprint");
				pi.bools.add("head");
			}
		} else if (stage.equals("shop")) {
			stage = "MEAT";
			if (castle.power.equals("")) {
				String[] pows = new String[] { "volley" };
				castle.power = pows[new Random().nextInt(pows.length)];
			}
			if (wars.power.equals("")) {
				String[] pows = new String[] { "break" };
				wars.power = pows[new Random().nextInt(pows.length)];
			}
			MeatStage = "wall";
			timer = 210;
			castle.repairWall();
			for (String st : main.wars.players) {
				if (Bukkit.getPlayer(st) != null) {
					Player p = Bukkit.getPlayer(st);
					p.teleport(wars.meat);
					p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1, true));
					p.sendTitle(ChatColor.DARK_RED + "� �����!!!", ChatColor.GOLD + "������, �������, �������!", 5, 20,
							5);
					p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 10, 1);
					p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 10, 0);
					p.removePotionEffect(PotionEffectType.SPEED);
				}
			}
			for (String st : main.castle.players) {
				if (Bukkit.getPlayer(st) != null) {
					Player p = Bukkit.getPlayer(st);
					PlayerInfo pi = Events.plist.get(p.getName());
					tp(p, castle.meat, 180);
					p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1, true));
					p.sendTitle(ChatColor.DARK_RED + "�������!!!", ChatColor.GOLD + "��������� ���� �����!", 5, 20, 5);
					p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 10, 1);
					p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 10, 0);
					p.removePotionEffect(PotionEffectType.SPEED);
					if (pi.money >= 1000) {
						Ach a = getAch(ChatColor.GOLD + "������� ��������");
						a.compl(p);
					}
				}
			}
		} else if (stage.equals("MEAT")) {
			if (wave >= 5) {
				won(false);
				return;
			}
			if (castle.getDamage() < 50) {
				castleBoost = 2;
				warBoost = 1;
				if (castle.getDamage() < 35) {
					castleBoost = 3;
					if (castle.getDamage() < 20) {
						castleBoost = 4;
					}
				}
			} else {
				warBoost = 2;
				castleBoost = 1;
				if (castle.getDamage() > 65) {
					warBoost = 3;
					if (castle.getDamage() > 80) {
						warBoost = 4;
					}
				}
			}
			stage = "upTime";
			if (full)
				timer = 25;
			else {
				timer = 0;
				for (String st : castle.players) {
					Player p = Bukkit.getPlayer(st);
					PlayerInfo pi = Events.plist.get(st);
					pi.money += 50 * castleBoost;
					p.sendMessage(ChatColor.GREEN + "�� �������� " + 50 * castleBoost
							+ " ������ �� ���� � ����������� ������ (������ ����).");
				}
				for (String st : wars.players) {
					Player p = Bukkit.getPlayer(st);
					PlayerInfo pi = Events.plist.get(st);
					pi.money += 50 * warBoost;
					p.sendMessage(ChatColor.GREEN + "�� �������� " + 50 * warBoost
							+ " ������ �� ���� � ����������� ������ (������ �������������).");
				}
			}
			for (Player p : Bukkit.getOnlinePlayers()) {
				for (Player pl : Bukkit.getOnlinePlayers()) {
					pl.hidePlayer(p);
				}
			}
			for (String st : castle.players) {
				Player p = Bukkit.getPlayer(st);
				PlayerInfo pi = Events.plist.get(st);
				p.setGameMode(GameMode.ADVENTURE);
				int max = 250 + pi.ups.get("waveGold") * 200;
				double coef = castle.getDamage();
				String kitC = getKit(st, true);
				p.sendMessage(ChatColor.DARK_GREEN + "+" + (int) (0.1 * coef) + " G$!");
				GepUtil.HashMapReplacer(money, p.getName(), (int) (0.1 * coef), false, false);
				if (coef < 50 && kitC.equals(ChatColor.BLUE + "��������")) {
					coef = 50;
				}
				if (full)
					p.sendTitle(ChatColor.GREEN + "����������������� ������!",
							ChatColor.GOLD + "������� ����, ����� ���������� ��� ������.", 20, 60, 20);
				int bonus = (int) (max / 100.0 * coef);
				pi.sTrans(p, pi.sMoney);
				p.sendMessage(ChatColor.GREEN + "��������� ��������! " + ChatColor.GOLD + "����������: " + pi.sMoney);
				p.sendMessage(ChatColor.GREEN + "�� �������� " + ChatColor.AQUA + bonus + ChatColor.GREEN
						+ " ��������� ������ �� " + ChatColor.GOLD + max + ChatColor.GREEN + " ��������� "
						+ ChatColor.YELLOW + "(" + GepUtil.CylDouble(coef, "#0.00") + "%) " + ChatColor.AQUA
						+ "�� ����� ������� ������ �� �����.");
				pi.sTrans(p, bonus);
				if (full)
					p.teleport(castle.mine);
			}
			for (String st : wars.players) {
				Player p = Bukkit.getPlayer(st);
				PlayerInfo pi = Events.plist.get(st);
				String kitW = getKit(st, false);
				for (Location loc : castle.mineBlocks) {
					Events.replaceMineBlock(loc);
				}
				if (full)
					p.sendTitle(ChatColor.RED + "����������� ������������!",
							ChatColor.GOLD + "����������� ����, ����� �������� ��� ������.", 20, 60, 20);
				p.sendMessage(ChatColor.GREEN + "��������� ��������! " + ChatColor.GOLD + "����������: " + pi.sMoney);
				if (pi.sMoney >= 1000) {
					Ach a = getAch(ChatColor.RED + "����� � ���");
					a.compl(p);
				}
				pi.sTrans(p, pi.sMoney);
				p.sendMessage(ChatColor.DARK_GREEN + "+" + (int) (pi.sMoney / 50) + " G$!");
				GepUtil.HashMapReplacer(money, p.getName(), (int) (pi.sMoney / 50), false, false);
				if (kitW.equals(ChatColor.RED + "���")) {
					p.sendMessage(ChatColor.RED + "[���] " + ChatColor.GOLD + "�� �������� �������� " + pi.sMoney * 0.15
							+ " ������!");
					pi.sMoney += pi.sMoney * 0.15;
				}
				if (full)
					p.teleport(wars.hunt);
				if (full)
					wars.gruz(p);
			}
			for (Player p : Bukkit.getOnlinePlayers()) {
				PlayerInfo pi = Events.plist.get(p.getName());
				GepUtil.HashMapReplacer(pi.waits, "deaths", 0, true, true);
				pi.sMoney = 0;
				if (full)
					p.setGameMode(GameMode.SURVIVAL);
				p.setFireTicks(0);
				GepUtil.HashMapReplacer(pi.timers, "respawn", 0, true, true);
			}
		} else if (stage.equals("upTime")) {
			stage = "shop";
			timer = 150;
			wave++;
			for (Player p : Bukkit.getOnlinePlayers()) {
				for (Player pl : Bukkit.getOnlinePlayers()) {
					pl.showPlayer(p);
				}
			}
			for (String st : main.wars.players) {
				Player p = Bukkit.getPlayer(st);
				p.setGameMode(GameMode.ADVENTURE);
				PlayerInfo pi = Events.plist.get(st);
				if (full)
					p.sendMessage(ChatColor.GREEN + "�� ������ �� ���������� " + ChatColor.DARK_GREEN + pi.sMoney);
				p.sendTitle(ChatColor.YELLOW + "����������!", ChatColor.GOLD + "���� ���������� ��-������!", 20, 50,
						20);
				if (full) {
					pi.sTrans(p, pi.sMoney);
					pi.sMoney = 0;
				}
				p.teleport(wars.base);
				p.getInventory().remove(Material.DIAMOND);
				p.getInventory().remove(Material.IRON_INGOT);
				p.getInventory().remove(Material.GOLD_INGOT);
				p.getInventory().remove(Material.COAL);
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 2));
			}
			for (String st : main.castle.players) {
				Player p = Bukkit.getPlayer(st);
				p.setGameMode(GameMode.ADVENTURE);
				PlayerInfo pi = Events.plist.get(st);
				if (full)
					p.sendMessage(ChatColor.GREEN + "� ����� �� ���������� " + ChatColor.DARK_GREEN + pi.sMoney);
				p.sendTitle(ChatColor.YELLOW + "����������!", ChatColor.GOLD + "���� ���������� ��-������!", 20, 50,
						20);
				if (full) {
					pi.sTrans(p, pi.sMoney);
					pi.sMoney = 0;
				}
				p.teleport(castle.base);
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 2));
			}
			castle.repairWall();
		} else if (stage.equals("won")) {
			doStart();
		}
	}

	public void respawn(Player p) {
		PlayerInfo pi = Events.plist.get(p.getName());
		if (castle.players.contains(p.getName())) {
			if (stage.equals("MEAT") && MeatStage.equals("wall"))
				tp(p, castle.meat, 180);
			else
				tp(p, castle.afterBreak, 180);
		} else {
			if (stage.equals("MEAT")) {
				if (MeatStage.equals("wall"))
					p.teleport(wars.meat);
				else {
					p.teleport(castle.wallBreak);
				}
			} else
				p.teleport(wars.base);
		}
		p.setGameMode(GameMode.ADVENTURE);
		pi.timers.put("protect", 25);
		int timeSpeed = 60 * pi.ups.get("speedTime");
		if (wars.players.contains(p.getName())) {
			String kitW = getKit(p.getName(), false);
			if (kitW.equals(ChatColor.RED + "������"))
				timeSpeed += new Random().nextInt(3) * 20 + 40;
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, new Random().nextInt(3) * 20 + 40, 0,
					false, false), true);
			if (kitW.equals(ChatColor.BLUE + "������")) {
				for (Player pl : Bukkit.getOnlinePlayers()) {
					pl.hidePlayer(p);
				}
				pi.timers.put("unInv", 60);
			}
		}
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, timeSpeed, 0, true, false), true);
	}

	public void circle(Player p, Location loc, double r, Particle part) {
		Location l = loc.clone();
		l.subtract(r, 0, r / 2);
		for (double t = 0; t < Math.PI * 2; t += 1) {
			double x = r * Math.sin(t);
			double z = r * Math.cos(t);
			l.add(x, 0, z);
			p.spawnParticle(part, l, 1, 0.1, 0.1, 0.1, 0);
		}
	}

	public void ALLcircle(Location loc, double r, Particle part) {
		Location l = loc.clone();
		l.subtract(r, 0, r / 2);
		for (double t = 0; t < Math.PI * 2; t += 1) {
			double x = r * Math.sin(t);
			double z = r * Math.cos(t);
			l.add(x, 0, z);
			l.getWorld().spawnParticle(part, l, 1, 0.1, 0.1, 0.1, 0);
		}
	}

	boolean pointed(Location loc, Player p) {
		Material mat = loc.clone().subtract(0, 1, 0).getBlock().getType();
		int y = loc.getBlockY() - 1;
		Location l = p.getLocation();
		l.setY(y);
		if (l.getBlock().getType().equals(mat))
			return true;
		return false;
	}

	public static void won(boolean war) {
		List<String> winners = new ArrayList<>();
		if (war) {
			GepUtil.globMessage(ChatColor.GOLD + "����� ��� ��������. ������� ��������!",
					Sound.ENTITY_ENDERDRAGON_DEATH, 10, 1,
					ChatColor.DARK_RED + "" + ChatColor.BOLD + ChatColor.ITALIC + "������� ��������!",
					ChatColor.AQUA + "������� �� ���� �� GepCraft!", 50, 30, 50);
			winners = wars.players;
		} else
			winners = castle.players;

		for (String st : winners) {
			PlayerInfo pi = Events.plist.get(st);
			GlobPInfo gpi = Events.gplist.get(st);
			Player p = Bukkit.getPlayer(st);
			if (!war) {
				if (castle.getDamage() == 0) {
					Ach a = getAch(ChatColor.DARK_RED + "FATALITY");
					a.compl(p);
				}
				if (castleBoost >= 4) {
					Ach a = getAch(ChatColor.AQUA + "������� ������� ���������.");
					a.compl(p);
				}
			} else {
				if (warBoost >= 4) {
					Ach a = getAch(ChatColor.AQUA + "������� ������� ���������.");
					a.compl(p);
				}
			}
			p.sendMessage(ChatColor.DARK_GREEN + "+ 25 G$ �� ������!");
			GepUtil.HashMapReplacer(money, p.getName(), 25, false, false);
			gpi.wons++;
			gpi.saveSQL();
			if (pi.bools.contains("noSprint")) {
				Ach a = getAch(ChatColor.GOLD + "���� �� ����");
				a.compl(p);
			}
			if (pi.bools.contains("head")) {
				Ach a = getAch(ChatColor.GOLD + "����� ������!");
				a.compl(p);
			}
			if (wave == 1) {
				Ach a = getAch(ChatColor.AQUA + "����������");
				a.compl(p);
			}
		}
		new BukkitRunnable() {
			int i = 0;

			@Override
			public void run() {
				i++;
				if (i == 15) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						ByteArrayOutputStream b = new ByteArrayOutputStream();
						DataOutputStream out = new DataOutputStream(b);
						try {
							out.writeUTF("Connect");
							out.writeUTF("lobby");
							p.sendPluginMessage(instance, "BungeeCord", b.toByteArray());
						} catch (IOException ee) {
							ee.printStackTrace();
							p.kickPlayer(ChatColor.GOLD + "������! ��� ������ ���� ���������� � �����.");
						}
					}
				}
				if (i == 20) {
					instance.stop();
				}
			}
		}.runTaskTimer(instance, 20, 20);
		timer = 25;
		wave = 1;
		stage = "won";
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(ChatColor.DARK_GREEN + "+ 5 G$ �� �������!");
			GepUtil.HashMapReplacer(money, p.getName(), 5, false, false);
			p.setGameMode(GameMode.SPECTATOR);
			PlayerInfo pi = Events.plist.get(p.getName());
			pi.timers.clear();
			Scoreboard s = p.getScoreboard();
			s.getTeam("wars").unregister();
			s.getTeam("cast").unregister();
			org.bukkit.scoreboard.Team wars = s.registerNewTeam("wars");
			wars.setPrefix(ChatColor.RED + "");
			org.bukkit.scoreboard.Team cast = s.registerNewTeam("cast");
			cast.setPrefix(ChatColor.BLUE + "");
			GlobPInfo gpi = Events.gplist.get(p.getName());
			// addG.addG$(p.getName(), money.get(p.getName()), false);
			if (p.hasPermission("unl"))
				p.sendMessage(ChatColor.AQUA + "����� �� ���� �� ���������� " + ChatColor.GREEN + ChatColor.BOLD
						+ money.get(p.getName()) + " G$!");
			gpi.games++;
			gpi.saveSQL();
		}
		money.clear();
	}

	public void stop() {
		getServer().dispatchCommand(getServer().getConsoleSender(), "stop");
	}

	void doStart() {
		stage = "wait";
		timer = 120;
		Events.plist.clear();
		for (Player p : Bukkit.getOnlinePlayers()) {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			try {
				out.writeUTF("Connect");
				out.writeUTF("lobby");
				p.sendPluginMessage(instance, "BungeeCord", b.toByteArray());
			} catch (IOException ee) {
				ee.printStackTrace();
				p.kickPlayer(ChatColor.GOLD + "������! ��� ������ ���� ���������� � �����.");
			}
		}
		castle.baseHP = 100;
		castle.repairWall();
		/*
		 * scon.SetInt("CanPlay", 1); scon.SetInt("Type", 3);
		 */
	}

	public static void tp(Player p, Location loc) {
		Location l = loc.clone();
		l.setPitch(p.getLocation().getPitch());
		l.setYaw(p.getLocation().getYaw());
		p.teleport(l);
	}

	public static void tp(Player p, Location loc, int yaw) {
		Location l = loc.clone();
		l.setYaw(yaw);
		p.teleport(l);
	}

	public static String getKit(String pname, boolean isCastle) {
		if (!main.full) {
			if (isCastle)
				return ChatColor.BLUE + "��������";
			else
				return ChatColor.RED + "������";
		}
		GlobPInfo gpi = Events.gplist.get(pname);
		if (isCastle) {
			return gpi.kitC;
		} else {
			return gpi.kitW;
		}
	}

	public static Ach getAch(String name) {
		for (Ach a : achs) {
			if (a.name.equals(name))
				return a;
		}
		GepUtil.globMessage(
				ChatColor.RED + "���� �������� ������! ������ � ������� " + name + ChatColor.RED + ", �������� ����!");
		return null;
	}

	public static void drawLine(Location point1, Location point2, double space, Particle part) {
		point1.setY(point1.getY() + 1);
		World world = point1.getWorld();
		Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
		double distance = point1.distance(point2);
		Vector p1 = point1.toVector();
		Vector p2 = point2.toVector();
		Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
		double length = 0;
		for (; length < distance; p1.add(vector)) {
			point1.getWorld().spawnParticle(part, p1.getX(), p1.getY(), p1.getZ(), 0);
			length += space;
		}
	}

	public boolean isCastle(Player p) {
		return main.castle.players.contains(p.getName());
	}
}
