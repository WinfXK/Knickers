package xiaokai.knickers.appliance;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Appliance {
	private static Kick kick;
	private static Message Msg;
	private LinkedHashMap<String, Map<String, Object>> FormList;
	private ArrayList<Object> ItemList = new ArrayList<Object>();
	private Config config;

	/**
	 * 自定义快捷工具工具
	 * 
	 * @param kick
	 */
	public Appliance(Kick kick) {
		Appliance.kick = kick;
		Msg = kick.Message;
		this.enclose();
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
		return new Handle(kick, player, Item, item).start();
	}

	/**
	 * 取得自定义表单对象
	 * 
	 * @return
	 */
	public static LinkedHashMap<String, Map<String, Object>> getFormList() {
		return kick.App.FormList;
	}

	/**
	 * 初始化自定义工具的配置文件对象和数据对象
	 */
	public void enclose() {
		config = new Config(new File(kick.mis.getDataFolder(), kick.ApplianceName), Config.YAML);
		Map<String, Object> map = config.getAll();
		for (String ike : map.keySet())
			if (map.get(ike) != null && (map.get(ike) instanceof Map))
				FormList.put(ike, (Map<String, Object>) map.get(ike));
			else
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
		if (Nbt == null && !kick.App.ItemList.contains(item.getId())
				&& !kick.App.ItemList.contains(item.getId() + ":" + item.getDamage()))
			return false;
		return (Nbt.getString("Their") != null && Nbt.getString("Their").equals(kick.mis.getName()))
				|| kick.App.ItemList.contains(item.getId())
				|| kick.App.ItemList.contains(item.getId() + ":" + item.getDamage());
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
		item.setNamedTag(Nbt);
		DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(dumperOptions);
		Nbt.putString("Data", yaml.dump(map));
		item.setNamedTag(Nbt);
		return item;
	}

	/**
	 * 判断一个物品是否是快捷工具并且检测可以丢弃
	 * 
	 * @param item
	 * @return
	 */
	public static boolean isDrop(Item item) {
		return isGirl(item) ? item.getNamedTag().getBoolean("isDrop") : false;
	}

}
