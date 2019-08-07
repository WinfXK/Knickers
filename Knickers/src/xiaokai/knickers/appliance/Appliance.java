package xiaokai.knickers.appliance;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import xiaokai.knickers.form.MakeForm;
import xiaokai.knickers.mtp.Kick;
import xiaokai.knickers.mtp.Message;
import xiaokai.knickers.mtp.MyPlayer;
import xiaokai.tool.EnchantName;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Appliance {
	private static Kick kick;
	private static Message Msg;
	public static LinkedHashMap<String, Map<String, Object>> FormList;
	public static Config config;

	/**
	 * 自定义快捷工具工具
	 * 
	 * @param kick
	 */
	public Appliance(Kick kick) {
		Appliance.kick = kick;
		Msg = kick.Message;
		Appliance.enclose();
	}

	/**
	 * 开始处理玩家使用快捷工具的事件
	 * 
	 * @param player 使用自定义快捷工具的玩家对象
	 * @param item   使用的快捷工具的Item对象
	 * @return
	 */
	public boolean start(Player player, Item item) {
		CompoundTag Nbt = item.getNamedTag();
		Map<String, Object> Item;
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer = myPlayer == null ? new MyPlayer(player) : myPlayer;
		if (myPlayer.loadTime == null
				|| Duration.between(myPlayer.loadTime, Instant.now()).toMillis() > kick.config.getInt("屏蔽玩家双击间隔"))
			myPlayer.loadTime = Instant.now();
		else
			return false;
		if (Nbt != null && Nbt.getString("Their") != null && Nbt.getString("Their").equals(kick.mis.getName())) {
			String ConfigString = Nbt.getString("Data");
			if (ConfigString == null || ConfigString.isEmpty())
				return MakeForm.Tip(player, Msg.getSon("快捷工具", "自定义工具打开失败提示", new String[] { "{Player}", "{Error}" },
						new Object[] { player.getName(), "数据获取失败！" }));
			try {
				DumperOptions dumperOptions = new DumperOptions();
				dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
				Yaml yaml = new Yaml(dumperOptions);
				Item = new ConfigSection(yaml.loadAs(ConfigString, LinkedHashMap.class));
			} catch (Exception e) {
				return MakeForm.Tip(player, Msg.getSon("快捷工具", "自定义工具打开失败提示", new String[] { "{Player}", "{Error}" },
						new Object[] { player.getName(), "数据解析失败！" + e.getMessage() }));
			}
		} else {
			String ID = String.valueOf(item.getId());
			if (FormList.containsKey(ID))
				Item = FormList.get(ID);
			else if (FormList.containsKey(item.getId() + ":" + item.getDamage()))
				Item = FormList.get(item.getId() + ":" + item.getDamage());
			else
				return MakeForm.Tip(player, Msg.getSon("快捷工具", "自定义工具打开失败提示", new String[] { "{Player}", "{Error}" },
						new Object[] { player.getName(), "数据获取失败！" }));
		}
		return new Handle(player, Item).start();
	}

	/**
	 * 初始化自定义工具的配置文件对象和数据对象
	 */
	public static void enclose() {
		FormList = new LinkedHashMap<String, Map<String, Object>>();
		config = new Config(new File(kick.mis.getDataFolder(), kick.ApplianceName), Config.YAML);
		Map<String, Object> map = config.getAll();
		for (String ike : map.keySet())
			if (map.get(ike) != null && (map.get(ike) instanceof Map)) {
				Map<String, Object> FFFSB = (Map<String, Object>) map.get(ike);
				if (FFFSB.get("ID") != null && !FFFSB.get("ID").toString().isEmpty()) {
					FFFSB.put("Key", ike);
					FormList.put(FFFSB.get("ID").toString(), FFFSB);
				}
			} else
				kick.mis.getLogger().warning("§4自定义工具中的§6" + ike + "§4数据不合法！请检查。");
	}

	/**
	 * 检查一个物品是否是快捷工具列表内的物品
	 * 
	 * @param item
	 * @return
	 */
	public static boolean isGirl(Item item) {
		if (item == null || item.getId() == 0)
			return false;
		CompoundTag Nbt = item.getNamedTag();
		String ID = String.valueOf(item.getId());
		if (Nbt == null && !Appliance.FormList.containsKey(ID)
				&& !Appliance.FormList.containsKey(ID + ":" + item.getDamage()))
			return false;
		return (Nbt != null && Nbt.getString("Their") != null && Nbt.getString("Their").equals(kick.mis.getName()))
				|| Appliance.FormList.containsKey(ID) || Appliance.FormList.containsKey(ID + ":" + item.getDamage());
	}

	/**
	 * 给一个物品设置唯一数据
	 * 
	 * @param ID     要设置的物品的ID
	 * @param Damage 要设置的物品的特殊值
	 * @param map    要存入的数据
	 * @return
	 */
	public static Item setData(int ID, int Damage, Map<String, Object> map) {
		return setData(new Item(ID, Damage), map);
	}

	/**
	 * 给一个物品设置唯一数据
	 * 
	 * @param item 要设置的物品的Item对象
	 * @param map  要存入的数据
	 * @return
	 */
	public static Item setData(Item item, Map<String, Object> map) {
		if (item == null || item.getId() == 0)
			return item;
		CompoundTag Nbt = item.getNamedTag();
		Nbt = Nbt == null ? new CompoundTag() : Nbt;
		Nbt.putString("Their", kick.mis.getName());
		Nbt.putBoolean("isDrop", Tool.ObjToBool(map.get("允许丢弃")));
		DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(dumperOptions);
		Nbt.putString("Data", yaml.dump(map));
		item.setNamedTag(Nbt);
		if (map.get("Name") != null)
			item.setCustomName(Msg.getText(map.get("Name")));
		if (map.get("Lore") != null)
			item.setLore(Msg.getText(map.get("Lore")));
		if (map.get("Enchant") != null) {
			List<Enchantment> enchants = Arrays.asList(item.getEnchantments());
			if (map.get("Enchant") instanceof List) {
				List<Object> list = (List<Object>) map.get("Enchant");
				for (Object obj : list)
					if (!enchants.contains(EnchantName.UnknownToEnchant(obj)))
						item.addEnchantment(EnchantName.UnknownToEnchant(obj));
			} else if (!enchants.contains(EnchantName.UnknownToEnchant(map.get("Enchant"))))
				item.addEnchantment(EnchantName.UnknownToEnchant(map.get("Enchant")));
		}
		return item;
	}

	/**
	 * 返回一个快捷工具的数据
	 * 
	 * @param item
	 * @return
	 */
	public static Map<String, Object> getData(Item item) {
		if (!isGirl(item))
			return null;
		Map<String, Object> map = new HashMap<String, Object>();
		String Error;
		CompoundTag Nbt = item.getNamedTag();
		String ID = String.valueOf(item.getId());
		if (Nbt == null || Nbt.getString("Their") == null || !Nbt.getString("Their").equals(kick.mis.getName())) {
			map = Appliance.FormList.containsKey(ID) ? Appliance.FormList.get(ID)
					: Appliance.FormList.containsKey(ID + ":" + item.getDamage())
							? Appliance.FormList.get(ID + ":" + item.getDamage())
							: null;
			Error = "无法获取数据！";
		} else {
			String ike = Nbt.getString("Data");
			if (ike != null && !ike.isEmpty()) {
				Error = "数据解析失败！";
				try {
					DumperOptions dumperOptions = new DumperOptions();
					dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
					Yaml yaml = new Yaml(dumperOptions);
					map = new ConfigSection(yaml.loadAs(ike, LinkedHashMap.class));
				} catch (Exception e) {
					map = null;
					Error += e.getMessage();
				}
			} else {
				map = null;
				Error = "数据提取失败！";
			}
		}
		if (map == null) {
			map = new HashMap<String, Object>();
			map.put("OK", false);
			map.put("Error", Error);
		} else
			map.put("OK", true);
		return map;
	}

	/**
	 * 判断一个物品是否是快捷工具并且检测可以丢弃
	 * 
	 * @param item
	 * @return
	 */
	public static boolean isDrop(Item item) {
		if (!isGirl(item))
			return false;
		CompoundTag Nbt = item.getNamedTag();
		if (Nbt != null && Nbt.getString("Their").equals(kick.mis.getName()))
			return Nbt.getBoolean("isDrop");
		Map<String, Object> map = getData(item);
		return Tool.ObjToBool(map.get("isDrop"));
	}

	/**
	 * 判断玩家是不是已经拥有这个快捷工具了
	 * 
	 * @param player 要检查的玩家对象
	 * @param Key    要检查的数据的Key
	 * @return
	 */
	public static boolean isAlready(Player player, String Key) {
		Map<Integer, Item> Cons = player.getInventory().getContents();
		for (Integer ike : Cons.keySet()) {
			Item i = Cons.get(ike);
			if (Appliance.isGirl(i)) {
				Map<String, Object> mItem = Appliance.getData(i);
				if (mItem != null && mItem.get("Key") != null && Key != null && mItem.get("Key").equals(Key))
					return true;
			}
		}
		return false;
	}

	/**
	 * 检测玩家是否已经申请过这个快捷工具了
	 * 
	 * @param player 要检查的玩家对象
	 * @param Key    要检查的数据的Key
	 * @return
	 */
	public static boolean isRepetition(Player player, String Key) {
		Map<String, Object> map = config.get(Key) == null ? new HashMap<String, Object>()
				: (config.get(Key) instanceof Map) ? (HashMap<String, Object>) config.get(Key)
						: new HashMap<String, Object>();
		if (map.size() < 1)
			return false;
		List<String> list = map.get("Players") != null
				? (map.get("Players") instanceof List) ? (ArrayList<String>) map.get("Players")
						: new ArrayList<String>()
				: new ArrayList<String>();
		return list.contains(player.getName());
	}
}
