package MeatWars;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import cmds.addG;
import objMeat.Ach;
import objMeat.GlobPInfo;
import objMeat.PlayerInfo;
import objMeat.Team;
import utilsMeat.ActionBar;
import utilsMeat.GepUtil;
import utilsMeat.ItemUtil;

public class Events implements Listener{
	public static HashMap<String, PlayerInfo> plist = new HashMap<>();
	public static HashMap<String, GlobPInfo> gplist = new HashMap<>();
	public static ArrayList<String> ready=new ArrayList<>();
	@EventHandler
	public void j(PlayerJoinEvent e){
		Player p=e.getPlayer();
		if(!main.stage.equals("wait")){
			p.kickPlayer(ChatColor.AQUA+"Игра уже началась.");
			e.setJoinMessage(null);
			return;
		}
		doJoin(p);
	}
	public static void doJoin(Player p){
		if(!plist.containsKey(p.getName()))plist.put(p.getName(), new PlayerInfo(p));
		Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
		org.bukkit.scoreboard.Team wars = s.registerNewTeam("wars");
		wars.setPrefix(ChatColor.RED+"");
		org.bukkit.scoreboard.Team cast = s.registerNewTeam("cast");
		cast.setPrefix(ChatColor.BLUE+"");
		p.setScoreboard(s);
		Objective o = s.registerNewObjective("stats", "dummy");
		o.setDisplayName(ChatColor.GOLD+"Castle "+ChatColor.DARK_RED+ChatColor.BOLD+"RUSH");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		p.setScoreboard(s);
		p.getInventory().clear();
		if(main.con.GetText(p.getName(), "KitC")==null){
			main.con.NewStats(p.getName());
		}
		if(Bukkit.getOnlinePlayers().size()==1){
			GlobPInfo gpi = new GlobPInfo(p.getName());
			if(gpi.games>=3&&gpi.wons>=1){
				main.full=true;
				main.scon.SetInt("Type", 2);
			}
			else{
				main.full=false;
				main.scon.SetInt("Type", 1);
			}
		}
		if(!main.full){
			p.sendMessage(ChatColor.AQUA+"[INFO] "+ChatColor.GREEN+"Вы играете в урезанную версию игры. Если вы хотите играть в полную версию, вам нужна минимум 1 победа и 3 завершённой игры. Сделано ради баланса.");
			p.sendMessage(ChatColor.AQUA+"[INFO] "+ChatColor.BLUE+"В полной версии есть уникальные предметы за обе команды, доступны наборы и есть стадия восстановления!");
		}
		p.getInventory().addItem(ItemUtil.create(Material.COMPASS,"Выбор команды"));
		p.getInventory().addItem(ItemUtil.create(Material.EXP_BOTTLE,ChatColor.AQUA+"Достижения"));
		if(main.full){
			p.getInventory().addItem(ItemUtil.create(Material.CHEST,ChatColor.GOLD+"Классы защитников"));
			p.getInventory().addItem(ItemUtil.create(Material.CHEST,ChatColor.GOLD+"Классы варваров"));
		}
		p.getInventory().setItem(8,ItemUtil.create(Material.TOTEM,ChatColor.GREEN+"Готов!"));
		if(!p.getGameMode().equals(GameMode.CREATIVE)){
			p.teleport(main.wait);
			p.setGameMode(GameMode.ADVENTURE);
		}
		gplist.put(p.getName(), new GlobPInfo(p.getName()));
		Ach a=main.getAch(ChatColor.LIGHT_PURPLE+"Я родился!");
		a.compl(p);
		main.money.put(p.getName(),0);
	}
	@EventHandler
	public void leave(PlayerQuitEvent e){
		Player p =e.getPlayer();
		boolean isCastle=false;
		Team t=main.wars;
		if(main.castle.players.contains(p.getName())){
			isCastle=true;
			t=main.castle;
			if(t.players.contains(p.getName()))t.players.remove(p.getName());
		}
		if(!main.stage.equals("wait")&&!main.stage.equals("won")&&t.players.size()==0){
			main.won(isCastle);
			GepUtil.globMessage("Все враги ливнули)");
		}
		gplist.remove(p.getName());
		if(main.money.containsKey(p.getName()))addG.addG$(p.getName(), main.money.get(p.getName()), false);
		main.money.remove(p.getName());
		doLeave(p);
		if(Bukkit.getOnlinePlayers().size()==1){
			main.scon.SetInt("CanPlay", 1);
			main.scon.SetInt("Type", 3);
		}
	}
	public static void doLeave(Player p){
		if(main.wars.players.contains(p.getName()))main.wars.players.remove(p.getName());
		if(main.castle.players.contains(p.getName()))main.castle.players.remove(p.getName());
		if(ready.contains(p.getName()))ready.remove(p.getName());
	}
	@EventHandler
	public void interact(PlayerInteractEvent e){
		Player p = e.getPlayer();
		PlayerInfo pi=plist.get(p.getName());
		ItemStack hitem = p.getInventory().getItemInMainHand();
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)||e.getAction().equals(Action.RIGHT_CLICK_AIR)){
			if(!e.getHand().equals(EquipmentSlot.HAND)){e.setCancelled(true);return;}
			if(hitem.getType().equals(Material.TOTEM)){
				e.setCancelled(true);
				if(ready.contains(p.getName())){
					ready.remove(p.getName());
					GepUtil.globMessage(ChatColor.YELLOW+p.getName()+ChatColor.GOLD+" не готов... "+ChatColor.GRAY+"("+ChatColor.RED+ready.size()+ChatColor.GOLD+"/"+Bukkit.getOnlinePlayers().size()+ChatColor.GRAY+")");
				}else{
					ready.add(p.getName());
					GepUtil.globMessage(ChatColor.YELLOW+p.getName()+ChatColor.GREEN+" готов! "+ChatColor.GRAY+"("+ChatColor.GREEN+ready.size()+ChatColor.GOLD+"/"+Bukkit.getOnlinePlayers().size()+ChatColor.GRAY+")");
				}
			}
			if(hitem.getType().equals(Material.COMPASS)){
				GUI.teamSel(p);
				e.setCancelled(true);
			}
			if(hitem.getType().equals(Material.CHEST)){
				e.setCancelled(true);
				if(hitem.getItemMeta().getDisplayName().equals(ChatColor.GOLD+"Классы защитников")){
					GUI.classesC(p);
				}
				else{
					GUI.classesW(p);
				}
			}
			if(hitem.getType().equals(Material.EXP_BOTTLE)){
				e.setCancelled(true);
				GUI.achs(p);
			}
			if(hitem.getType().equals(Material.GOLD_AXE)){
				if(!pi.timers.containsKey("axe")){
					pi.timers.put("axe", 500-pi.ups.get("axeReload")*60);
					pi.timers.put("axeRush", 80+pi.ups.get("axeRush")*40);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 2, 1);
					p.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"x2 скорость разрушений на "+(4+pi.ups.get("axeRush")*2)+" сек!");
				}
				else{
					p.sendMessage(ChatColor.RED+"Топор разрухи недоступен ещё "+ChatColor.GOLD+GepUtil.CylDouble(pi.timers.get("axe")/20.00, "#0.00")+" сек.");
				}
			}
			if(p.getInventory().getItemInMainHand().getType().equals(Material.FLINT)){
				if(!pi.timers.containsKey("noJail")){
					pi.timers.put("jail", 400);
					pi.timers.put("noJail", 1000);
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 2, 1);
					p.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"Наручники активированы на 20 сек!");
				}
				else{
					p.sendMessage(ChatColor.RED+"Наручники недоступны ещё "+ChatColor.GOLD+GepUtil.CylDouble(pi.timers.get("noJail")/20.00, "#0.00")+" сек.");
				}
			}
		}
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			Block b=e.getClickedBlock();
			Material mat=b.getType();
			if(mat.equals(Material.FURNACE)||mat.equals(Material.BEACON)||mat.equals(Material.WORKBENCH)){
				e.setCancelled(true);
				return;
			}
			if((mat.equals(Material.COAL_BLOCK)&&p.getInventory().contains(Material.COAL))
					||(mat.equals(Material.IRON_BLOCK)&&p.getInventory().contains(Material.IRON_INGOT))
					||(mat.equals(Material.GOLD_BLOCK)&&p.getInventory().contains(Material.GOLD_INGOT))
					||(mat.equals(Material.DIAMOND_BLOCK)&&p.getInventory().contains(Material.DIAMOND))){
				pi.sMoney+=2*main.warBoost;
				ActionBar bar = new ActionBar(ChatColor.GREEN+"+"+ChatColor.DARK_GREEN+2+ChatColor.LIGHT_PURPLE+ChatColor.BOLD+ChatColor.ITALIC+" x"+main.warBoost);
				bar.sendToPlayer(p);
				p.getInventory().remove(Material.DIAMOND);
				p.getInventory().remove(Material.IRON_INGOT);
				p.getInventory().remove(Material.GOLD_INGOT);
				p.getInventory().remove(Material.COAL);
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
				main.wars.gruz(p);
			}
			if(e.getClickedBlock()!=null&&e.getClickedBlock().getType().equals(Material.ENCHANTMENT_TABLE)){
				e.setCancelled(true);
				if(main.stage.equals("shop")){
					List<Material> mats = new ArrayList<>();
					mats.add(Material.WOOD_SWORD);
					mats.add(Material.STONE_SWORD);
					mats.add(Material.IRON_SWORD);
					mats.add(Material.STONE_AXE);
					mats.add(Material.IRON_AXE);
					List<Material> arms = new ArrayList<>();
					arms.add(Material.LEATHER_HELMET);
					arms.add(Material.LEATHER_CHESTPLATE);
					arms.add(Material.LEATHER_LEGGINGS);
					arms.add(Material.LEATHER_BOOTS);
					arms.add(Material.CHAINMAIL_HELMET);
					arms.add(Material.CHAINMAIL_CHESTPLATE);
					arms.add(Material.CHAINMAIL_LEGGINGS);
					arms.add(Material.CHAINMAIL_BOOTS);
					arms.add(Material.IRON_HELMET);
					arms.add(Material.IRON_CHESTPLATE);
					arms.add(Material.IRON_LEGGINGS);
					arms.add(Material.IRON_BOOTS);
					if(hitem.getType().equals(Material.AIR)){
						p.sendMessage(ChatColor.GOLD+"Вы что, РУКУ собрались зачаровывать?");
						return;
					}
					if(hitem.getType().equals(Material.BOW)){
						Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE+"Зачарования");
						for(int i:GUI.bowenchs.keySet()){
							inv.setItem(i, GUI.bowenchs.get(i).GUIItem(pi));
						}
						p.openInventory(inv);
						return;
					}
					else if(arms.contains(hitem.getType())){
						Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE+"Зачарования");
						for(int i:GUI.armenchs.keySet()){
							inv.setItem(i, GUI.armenchs.get(i).GUIItem(pi));
						}
						p.openInventory(inv);
						return;
					}
					else if(mats.contains(hitem.getType())){
						Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE+"Зачарования");
						for(int i:GUI.enchs.keySet()){
							inv.setItem(i, GUI.enchs.get(i).GUIItem(pi));
						}
						p.openInventory(inv);
					}
					else {
						p.sendMessage(ChatColor.RED+"Этот предмет нельзя зачаровать.");
					}
				}
			}
			else if(e.getClickedBlock()!=null&&e.getClickedBlock().getType().equals(Material.ANVIL)){
				e.setCancelled(true);
				if(main.stage.equals("shop")){
					List<Material> mats = new ArrayList<>();
					mats.add(Material.WOOD_SWORD);
					mats.add(Material.STONE_SWORD);
					mats.add(Material.IRON_SWORD);
					mats.add(Material.STONE_AXE);
					mats.add(Material.IRON_AXE);
					if(hitem.getType().equals(Material.AIR)){
						p.sendMessage(ChatColor.GOLD+"Вы что, РУКУ собрались затачивать?");
						return;
					}
					if(hitem.getType().equals(Material.BOW)){
						int lvl=hitem.getEnchantmentLevel(Enchantment.ARROW_DAMAGE)+1;
						if(lvl>5){
							p.sendTitle(ChatColor.AQUA+"Максимальный уровень усиления!", ChatColor.GREEN+"Больше просто некуда!", 10, 50, 20);
							return;
						}
						if(!pi.timers.containsKey("sharp")){
							p.sendTitle(ChatColor.YELLOW+"Усилить за "+GepUtil.boolCol(pi.money>=lvl*75)+lvl*75+"?", ChatColor.AQUA+"Клик ещё раз, чтобы усилить.", 10, 50, 20);
							pi.timers.put("sharp", 80);
						}
						else if(pi.money>=lvl*75){
							if(lvl==5){
								Ach a=main.getAch(ChatColor.AQUA+"Супер-лук");
								a.compl(p);
							}
							pi.timers.remove("sharp");
							ItemMeta meta = hitem.getItemMeta();
							if(lvl>1)meta.removeEnchant(Enchantment.ARROW_DAMAGE);
							meta.addEnchant(Enchantment.ARROW_DAMAGE, lvl, true);
							hitem.setItemMeta(meta);
							pi.money-=lvl*75;
							p.sendTitle(ChatColor.LIGHT_PURPLE+"Усилено!", ChatColor.RED+"Сила "+ChatColor.GOLD+lvl, 10, 20, 10);
							p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 2, 1);
						}
						return;
					}
					List<Material> arms = new ArrayList<>();
					arms.add(Material.LEATHER_HELMET);
					arms.add(Material.LEATHER_CHESTPLATE);
					arms.add(Material.LEATHER_LEGGINGS);
					arms.add(Material.LEATHER_BOOTS);
					arms.add(Material.CHAINMAIL_HELMET);
					arms.add(Material.CHAINMAIL_CHESTPLATE);
					arms.add(Material.CHAINMAIL_LEGGINGS);
					arms.add(Material.CHAINMAIL_BOOTS);
					arms.add(Material.IRON_HELMET);
					arms.add(Material.IRON_CHESTPLATE);
					arms.add(Material.IRON_LEGGINGS);
					arms.add(Material.IRON_BOOTS);
					if(arms.contains(hitem.getType())){
						int lvl=hitem.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL)+1;
						if(lvl>5){
							p.sendTitle(ChatColor.AQUA+"Максимальный уровень укрепления!", ChatColor.GREEN+"Больше просто некуда!", 10, 50, 20);
							return;
						}
						if(!pi.timers.containsKey("sharp")){
							p.sendTitle(ChatColor.YELLOW+"Укрепить за "+GepUtil.boolCol(pi.money>=lvl*30)+lvl*30+"?", ChatColor.AQUA+"Клик ещё раз, чтобы укрепить.", 10, 50, 20);
							pi.timers.put("sharp", 80);
						}
						else if(pi.money>=lvl*30){
							pi.timers.remove("sharp");
							ItemMeta meta = hitem.getItemMeta();
							if(lvl>1)meta.removeEnchant(Enchantment.PROTECTION_ENVIRONMENTAL);
							meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, lvl, true);
							hitem.setItemMeta(meta);
							pi.money-=lvl*30;
							p.sendTitle(ChatColor.LIGHT_PURPLE+"Укреплено!", ChatColor.RED+"Защита "+ChatColor.GOLD+lvl, 10, 20, 10);
							p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 2, 1);
						}
					}
					else if(mats.contains(hitem.getType())){
						int lvl=hitem.getEnchantmentLevel(Enchantment.DAMAGE_ALL)+1;
						if(lvl>5){
							p.sendTitle(ChatColor.AQUA+"Максимальный уровень заточки!", ChatColor.GREEN+"Больше просто некуда!", 10, 50, 20);
							return;
						}
						if(!pi.timers.containsKey("sharp")){
							p.sendTitle(ChatColor.YELLOW+"Заточить за "+GepUtil.boolCol(pi.money>=lvl*50)+lvl*50+"?", ChatColor.AQUA+"Клик ещё раз, чтобы заточить.", 10, 50, 20);
							pi.timers.put("sharp", 80);
						}
						else if(pi.money>=lvl*50){
							pi.timers.remove("sharp");
							ItemMeta meta = hitem.getItemMeta();
							if(lvl>1)meta.removeEnchant(Enchantment.DAMAGE_ALL);
							meta.addEnchant(Enchantment.DAMAGE_ALL, lvl, true);
							hitem.setItemMeta(meta);
							pi.money-=lvl*50;
							p.sendTitle(ChatColor.LIGHT_PURPLE+"Заточено!", ChatColor.RED+"Острота "+ChatColor.GOLD+lvl, 10, 20, 10);
							p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 2, 1);
						}
					}
					else p.sendMessage(ChatColor.RED+"Этот предмет нельзя заточить.");
				}
			}
		}
	}
	@EventHandler
	public void Entityinteract(PlayerInteractEntityEvent e){
		Player p = e.getPlayer();
		Entity en = e.getRightClicked();
		if(en.getType().equals(EntityType.VILLAGER)){
			e.setCancelled(true);
			if(main.wars.players.contains(p.getName())){
				if(en.getUniqueId().equals(main.wars.weapons)){
					main.shops.get("warWeap").openGUI(p);
				}
				if(en.getUniqueId().equals(main.wars.gold)){
					main.shopsUps.get("warGold").openGUI(p);
				}
				if(en.getUniqueId().equals(main.wars.med)){
					main.shopsUps.get("warMed").openGUI(p);
				}
			}
			else{
				if(en.getUniqueId().equals(main.castle.weapons)){
					main.shops.get("castleWeap").openGUI(p);
				}
				if(en.getUniqueId().equals(main.castle.gold)){
					main.shopsUps.get("castleGold").openGUI(p);
				}
				if(en.getUniqueId().equals(main.castle.med)){
					main.shopsUps.get("castleMed").openGUI(p);
				}
			}
		}
	}
	@EventHandler
	public void place(BlockPlaceEvent e){
		Player p = e.getPlayer();
		PlayerInfo pi=plist.get(p.getName());
		Block b=e.getBlock();
		if(p.isOp()&&p.getGameMode().equals(GameMode.CREATIVE)){
			if(p.isSneaking()){
				if(b.getType().equals(Material.BEACON)){
					e.setCancelled(true);
					main.wait=b.getLocation().add(0.5, 0, 0.5);
					GepUtil.globMessage(ChatColor.LIGHT_PURPLE+"Точка ожидания установлена.");
					File file = new File(main.instance.getDataFolder()+File.separator+"locs.yml");
					FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
					GepUtil.saveLocToConf(conf, "wait", b.getLocation().add(0.5, 0, 0.5));
					GepUtil.saveCfg(conf, file);
				}
				if(b.getType().equals(Material.STONE)){
					if(pi.bools.contains("mine")){
						main.castle.mineBlocks.add(b.getLocation());
						replaceMineBlock(b.getLocation());
					}
				}
			}
		}
		else e.setCancelled(true);
	}
	@EventHandler
	public void Break(BlockBreakEvent e){
		Player p = e.getPlayer();
		PlayerInfo pi=plist.get(p.getName());
		Block b=e.getBlock();
		if(!p.getGameMode().equals(GameMode.CREATIVE)){
			e.setCancelled(true);
		}
		if(main.castle.mineBlocks.contains(b.getLocation())){
			Material mat = b.getType();
			int get=0;
			if(mat.equals(Material.STONE))get=1;
			if(mat.equals(Material.COAL_ORE))get=2;
			if(mat.equals(Material.IRON_ORE))get=3;
			if(mat.equals(Material.GOLD_ORE))get=4;
			if(mat.equals(Material.DIAMOND_ORE))get=5;
			if(mat.equals(Material.EMERALD_ORE))get=6;
			pi.sMoney+=get*main.castleBoost;
			ActionBar bar = new ActionBar(ChatColor.GREEN+"+"+ChatColor.DARK_GREEN+get+ChatColor.LIGHT_PURPLE+ChatColor.BOLD+ChatColor.ITALIC+" x"+main.castleBoost);
			bar.sendToPlayer(p);
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
			replaceMineBlock(b.getLocation());
		}
	}
	@EventHandler
	public void die(PlayerDeathEvent e){
		Player p=e.getEntity();
		playerDie(p);
	}
	@EventHandler
	public void hurt(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			double d=e.getDamage();
			Player p=(Player) e.getEntity();
			PlayerInfo pi=plist.get(p.getName());
			GlobPInfo gpi=Events.gplist.get(p.getName());
			if(pi.timers.containsKey("protect"))e.setCancelled(true);
			if(e.getCause().equals(DamageCause.FALL)&&main.castle.players.contains(p.getName())&&main.MeatStage.equals("wall")){
				playerDie(p);
				e.setCancelled(true);
				return;
			}
			if(e instanceof EntityDamageByEntityEvent){
				EntityDamageByEntityEvent ee = (EntityDamageByEntityEvent) e;
				if(gpi.wons<=0){
					d=ee.getDamage()-ee.getDamage()*0.15;
				}
				Player damager = null;
				if(ee.getDamager() instanceof Player||ee.getDamager() instanceof Projectile){
					Projectile proj = null;
					if(ee.getDamager() instanceof Player)damager = (Player) ee.getDamager();
					else{
						proj=(Projectile) ee.getDamager();
						if(((Projectile) ee.getDamager()).getShooter() instanceof Player){
							damager=(Player) proj.getShooter();
						}
					}
					if(damager==null){
						if(proj.getScoreboardTags().contains("castler")){
							e.setCancelled(true);
							if(main.wars.players.contains(p.getName())){
								p.damage(15);
							}
						}
						return;
					}
					PlayerInfo dpi=plist.get(damager.getName());
					if(dpi.timers.containsKey("protect")){
						e.setCancelled(true);
						return;
					}
					if(!main.stage.equals("MEAT")){
						e.setCancelled(true);
						return;
					}
					if(main.castle.players.contains(p.getName())&&main.castle.players.contains(damager.getName())){
						e.setCancelled(true);
						if(proj!=null){
							proj.setFireTicks(0);
						}
						return;
					}
					if(main.castle.players.contains(p.getName())&&main.castle.players.contains(damager.getName())){
						e.setCancelled(true);
						return;
					}
					if(main.wars.players.contains(p.getName())&&main.wars.players.contains(damager.getName())){
						e.setCancelled(true);
						return;
					}
					Team t=null;
					Team et=null;
					boolean isCastle=main.castle.players.contains(p.getName());
					if(isCastle){t=main.castle;et=main.wars;}
					else{t=main.wars;et=main.castle;}
					if(t.players.size()<et.players.size()){
						double coef=1+(1.0*et.players.size()/t.players.size()-1)/2;
						d/=coef;
					}
					if(damager.getInventory().getItemInMainHand().getType().equals(Material.GOLD_AXE)){
						e.setCancelled(true);
						e.setDamage(0);
					}
					pi.lastHurted=damager.getName();
				}
			}
			e.setDamage(d);
			if(!e.isCancelled()&&p.getHealth()-e.getFinalDamage()<=0){
				e.setCancelled(true);
				playerDie(p);
			}
		}
	}
	public void playerDie(Player p){
		PlayerInfo pi=plist.get(p.getName());
		p.setHealth(20);
		pi.deaths++;
		if(pi.deaths==25){
			Ach a=main.getAch(ChatColor.RED+"БОЛЬ!");
			a.compl(p);
		}
		p.setGameMode(GameMode.SPECTATOR);
		if(pi.timers.containsKey("respawn"))return;
		if(main.castle.players.contains(p.getName())){
			pi.timers.put("respawn", (int) (pi.respTime(p)*20+(80-80/(50.0*main.wars.players.size())*main.castle.medHP)));
		}
		else pi.timers.put("respawn", (int) (pi.respTime(p)*20));
		if(main.stage.equals("MEAT")&&main.MeatStage.equals("wall")&&main.castle.players.contains(p.getName())){
			p.setGameMode(GameMode.ADVENTURE);
			main.tp(p, main.castle.arch, 180);
			if(p.getInventory().addItem(new ItemStack(Material.ARROW, 8))==null){
				p.sendMessage(ChatColor.RED+"Боже, чем ты так заполнил инвентарь? Ну сори, братан, стрел не получишь :/");
			}
			String kitC=main.getKit(p.getName(), true);
			if(kitC.equals(ChatColor.DARK_GREEN+"Лучник"))if(p.getInventory().addItem(new ItemStack(Material.ARROW, 8))==null){
				p.sendMessage(ChatColor.RED+"Боже, чем ты так заполнил инвентарь? Ну сори, братан, стрел не получишь :/");
			}
			GepUtil.HashMapReplacer(pi.timers, "respawn", 300, false, true);
		}
		if(!pi.lastHurted.equals("")){
			Player damager = Bukkit.getPlayer(pi.lastHurted);
			if(damager==null)return;
			PlayerInfo dpi=plist.get(damager.getName());
			dpi.kills++;
			p.sendMessage(ChatColor.GOLD+"Вас убил "+ChatColor.RED+damager.getName()+ChatColor.GOLD+"!"+ChatColor.YELLOW+" Это ваша смерть №"+ChatColor.RED+pi.deaths+ChatColor.YELLOW+" и его убийство №"+ChatColor.DARK_RED+dpi.kills+ChatColor.YELLOW+"!");
			damager.sendMessage(ChatColor.GOLD+"Вы убили "+ChatColor.RED+p.getName()+ChatColor.GOLD+"!"+ChatColor.YELLOW+" Это его смерть №"+ChatColor.RED+dpi.deaths+ChatColor.YELLOW+" и ваше убийство №"+ChatColor.DARK_RED+pi.kills+ChatColor.YELLOW+"!");
			if(new Random().nextBoolean()){
				damager.sendMessage(ChatColor.DARK_GREEN+"+ 1 G$ "+ChatColor.GOLD+"(50% шанс получить G$ за убийство)");
				GepUtil.HashMapReplacer(main.money, damager.getName(), 1, false, false);
			}
			if(main.castle.players.contains(damager.getName())){
				int coef=5;
				coef+=pi.ups.get("killGold");
				dpi.sMoney+=pi.sMoney/100.0*coef+10+pi.ups.get("killGold")*5;
				pi.sMoney-=pi.sMoney/100.0*coef;
				if(dpi.timers.containsKey("jail")){
					GepUtil.HashMapReplacer(pi.timers, "respawn", (int) (pi.respTime(p)*20+40+dpi.ups.get("jailTime")*20), false, true);
					damager.sendMessage(ChatColor.BLUE+""+ChatColor.BOLD+"Игрок "+p.getName()+" будет возраждаться "+GepUtil.CylDouble((pi.respTime(p)+2+dpi.ups.get("jailTime")),"#0.00")+" сек!");
				}
				if(!main.MeatStage.equals("wall")){
					double d = 0.05;
					String kitW=main.getKit(p.getName(), false);
					String kitC=main.getKit(damager.getName(), true);
					if(kitW.equals(ChatColor.RED+"Варвар"))d = 0.02;
					if(kitC.equals(ChatColor.DARK_GREEN+"Лучник"))if(damager.getInventory().addItem(new ItemStack(Material.ARROW, 2))==null){
						damager.sendMessage(ChatColor.RED+"Боже, чем ты так заполнил инвентарь? Ну сори, братан, стрел не получишь :/");
					}
					for(ItemStack item:p.getInventory()){
						if(item!=null&&item.hasItemMeta()&&item.getEnchantments().size()>0){
							for(Enchantment ench:item.getEnchantments().keySet()){
								if(new Random().nextDouble()<=d){
									item.removeEnchantment(ench);
									p.sendMessage(ChatColor.RED+"Чёрт! При смерти пропало зачарование "+ChatColor.DARK_PURPLE+ench.getName()+ChatColor.RED+" на предмете "+ChatColor.GRAY+item.getType());
								}
							}
						}
					}
				}
			}
			else{
				String kitW=main.getKit(damager.getName(), false);
				if(kitW.equals(ChatColor.RED+"Вор")){
					dpi.sMoney+=pi.sMoney/100.0*5;
					pi.sMoney-=pi.sMoney/100.0*5;
				}
			}
		}
	}
	@EventHandler
	public void chat(AsyncPlayerChatEvent e){
		e.setCancelled(true);
		Player p=e.getPlayer();
		String mes = e.getMessage();
		if(p.isOp()){
			if(mes.contains("time")){
				main.timer=GepUtil.intFromString(mes);
				return;
			}
		}
		if(!main.stage.equals("wait")){
			if((mes.charAt(0)+"").equals("!")){
				mes.substring(1, mes.length());
				mes=ChatColor.GOLD+"("+ChatColor.YELLOW+"Всем"+ChatColor.GOLD+") "+ChatColor.GRAY+"["+main.getKit(p.getName(), main.castle.players.contains(p.getName()))+ChatColor.GRAY+"] "+GepUtil.boolCol(ChatColor.BLUE, ChatColor.RED, main.castle.players.contains(p.getName()))+p.getName()+": "+ChatColor.YELLOW+mes.substring(1,mes.length());
				GepUtil.globMessage(mes);
			}
			else{
				mes=ChatColor.DARK_GREEN+"("+ChatColor.GREEN+"Команде"+ChatColor.DARK_GREEN+") "+ChatColor.GRAY+"["+main.getKit(p.getName(), main.castle.players.contains(p.getName()))+ChatColor.GRAY+"] "+GepUtil.boolCol(ChatColor.BLUE, ChatColor.RED, main.castle.players.contains(p.getName()))+p.getName()+": "+ChatColor.AQUA+mes;
				for(Player pl:Bukkit.getOnlinePlayers()){
					if(main.castle.players.contains(p.getName())==main.castle.players.contains(pl.getName()))pl.sendMessage(mes);
				}
				//TODO FIX IT!
			}
		}
		else{
			mes=ChatColor.YELLOW+p.getName()+ChatColor.WHITE+": "+mes;
			GepUtil.globMessage(mes);
		}
	}
	@EventHandler
	public void drop(PlayerDropItemEvent e){
		e.setCancelled(true);
	}
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e){
		e.setFoodLevel(20);
    }
	@EventHandler
	public void shoot(ProjectileLaunchEvent e){
		if(e.getEntity().getShooter() instanceof Player){
			Player p=((Player) e.getEntity().getShooter());
			PlayerInfo pi=plist.get(p.getName());
			String kit=main.getKit(p.getName(), true);
			double chance=0.5;
			if(kit.equals(ChatColor.DARK_GREEN+"Лучник"))chance=0.75;
			if(!pi.timers.containsKey("respawn")&&new Random().nextDouble()>=chance){
				e.getEntity().remove();
				p.sendTitle("", ChatColor.RED+""+GepUtil.CylDouble(chance*100, "#00")+"% шанс выстрела!", 10, 10, 10);
			}
		}
    }
	@EventHandler
	public void arrow(ProjectileHitEvent e){
		Projectile en=e.getEntity();
		en.remove();
	}
	public static void replaceMineBlock(Location loc){
		String mat=GepUtil.chancesByCoef(new String[]{"STONE","COAL_ORE","IRON_ORE","GOLD_ORE","DIAMOND_ORE","EMERALD_ORE"}, new int[]{6,5,4,3,2,1});
		loc.getBlock().setType(Material.getMaterial(mat));
	}
}
