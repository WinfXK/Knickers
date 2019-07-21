package xiaokai.knickers.mtp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.Utils;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Belle {
	private Kick kick;

	public Belle(Kick kick) {
		this.kick = kick;
	}

	public void start() {
		File file;
		for (String DirName : Kick.LoadDirList) {
			file = new File(kick.mis.getDataFolder(), DirName);
			if (!file.exists())
				file.mkdirs();
		}
		for (String FileName : kick.LoadFileName) {
			file = new File(kick.mis.getDataFolder(), FileName);
			if (!file.exists())
				try {
					kick.mis.getLogger().info("§6初始化资源：§c" + FileName);
					InputStream rand = this.getClass().getResourceAsStream("/resources/" + FileName);
					if (rand == null) {
						String QQName = "";
						try {
							QQName = Tool.doPost("http://tool.epicfx.cn/", "s=qs&qq=2508543202");
							if (QQName == null)
								QQName = "";
						} catch (Exception e) {
						}
						kick.mis.getLogger().error("初始化资源包失败！可能是插件已经损坏或被人为修改！请联系作者！" + QQName + "QQ：2508543202 ");
					} else
						Utils.writeFile(file, rand);
				} catch (IOException e) {
					kick.mis.getLogger().error("§4资源初始化失败！请检查！§f" + e.getMessage());
					kick.mis.setEnabled(false);
				}
		}
		for (String formidKey : kick.FormIDName)
			if (!kick.formIdConfig.exists(formidKey)) {
				kick.mis.getLogger().info("§4未发现§6" + formidKey + "§4对应的表单值！正在写入...");
				kick.formIdConfig.set(formidKey, Tool.getRand());
				kick.formIdConfig.save();
			}
		for (String FileNae : kick.isLoadFileName)
			try {
				kick.mis.getLogger().info("§6正在检查文件" + FileNae);
				String content = Utils.readFile(this.getClass().getResourceAsStream("/resources/" + FileNae));
				DumperOptions dumperOptions = new DumperOptions();
				dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
				Yaml yaml = new Yaml(dumperOptions);
				LinkedHashMap<String, Object> map = new ConfigSection(yaml.loadAs(content, LinkedHashMap.class));
				Config config = new Config(new File(kick.mis.getDataFolder(), FileNae), Config.YAML);
				Map<String, Object> cg = config.getAll();
				isMap(map, cg, config);
			} catch (IOException e) {
				kick.mis.getLogger().info("§4在检查数据中遇到错误！请尝试删除该文件§9[§d" + FileNae + "§9]\n§f" + e.getMessage());
			}
	}

	public void isMap(Map<String, Object> map, Map<String, Object> cg, Config config) {
		for (String ike : map.keySet())
			if (!cg.containsKey(ike)) {
				cg.put(ike, map.get(ike));
				kick.mis.getLogger().info("§6" + ike + "§4所属的数据错误！已回复默认");
				continue;
			} else if (!(((cg.get(ike) instanceof Map) || (map.get(ike) instanceof Map))
					|| ((cg.get(ike) instanceof List) && (map.get(ike) instanceof List)
							|| ((cg.get(ike) instanceof String) && (map.get(ike) instanceof String)))
					|| ((map.get(ike) instanceof Integer) && (cg.get(ike) instanceof Integer))
					|| ((map.get(ike) instanceof Boolean) && (cg.get(ike) instanceof Boolean))
					|| ((map.get(ike) instanceof Float) && (cg.get(ike) instanceof Float)))) {
				cg.put(ike, map.get(ike));
				kick.mis.getLogger().info("§6" + ike + "§4属性不匹配！已回复默认");
				continue;
			} else if (map.get(ike) instanceof Map)
				cg.put(ike, icMap((Map<String, Object>) map.get(ike), (Map<String, Object>) cg.get(ike)));
		config.setAll((LinkedHashMap<String, Object>) cg);
		config.save();
	}

	public Map<String, Object> icMap(Map<String, Object> map, Map<String, Object> cg) {
		for (String ike : map.keySet())
			if (!cg.containsKey(ike)) {
				cg.put(ike, map.get(ike));
				kick.mis.getLogger().info("§6" + ike + "§4所属的数据错误！已回复默认");
				continue;
			} else if (!(((cg.get(ike) instanceof Map) && (map.get(ike) instanceof Map))
					|| ((cg.get(ike) instanceof List) && (map.get(ike) instanceof List)
							|| ((cg.get(ike) instanceof String) && (map.get(ike) instanceof String))))) {
				cg.put(ike, map.get(ike));
				kick.mis.getLogger().info("§6" + ike + "§4属性不匹配！已回复默认");
				continue;
			} else if (map.get(ike) instanceof Map)
				cg.put(ike, icMap((Map<String, Object>) map.get(ike), (Map<String, Object>) cg.get(ike)));
		return cg;
	}

	/**
	 * 判断玩家背包是否拥有快捷工具
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isMaterials(Player player) {
		Inventory inventory = player.getInventory();
		Map<Integer, Item> Items = inventory.getContents();
		for (Integer ike : Items.keySet())
			if (isMaterials(Items.get(ike)))
				return true;
		return false;
	}

	/**
	 * 检查玩家是否拥有快捷工具，没有的话给与一个
	 * 
	 * @param player
	 */
	public static void exMaterials(Player player) {
		if (isMaterials(player))
			return;
		giveMaterials(player);
		player.sendMessage(
				Kick.kick.Message.getMessage("进服给快捷工具", new String[] { "{Player" }, new Object[] { player.getName() }));

	}

	/**
	 * 检查玩家的物品是否是快捷工具
	 * 
	 * @param item
	 * @return
	 */
	public static boolean isMaterials(Item item) {
		if (item == null || !Tool.isMateID(item.getId() + ":" + item.getDamage(), Kick.kick.config.getString("快捷工具")))
			return false;
		CompoundTag tag = item.getNamedTag();
		return (tag != null
				&& tag.getString("ID").equals(Kick.kick.getClass().getName() + Kick.kick.mis.getFullName()));
	}

	public static void giveMaterials(Player player) {
		Kick kick = Kick.kick;
		int[] IDS = Tool.IDtoFullID(kick.config.getString("快捷工具"));
		Item item = new Item(IDS[0], IDS[1]);
		CompoundTag tag = new CompoundTag();
		tag.putString("ID", Kick.class.getName() + kick.mis.getFullName());
		item.setNamedTag(tag);
		item.setCustomName(
				kick.Message.getMessage("快捷工具名称", new String[] { "{Player" }, new Object[] { player.getName() }));
		item.setLore(kick.Message.getMessage("快捷工具名称2", new String[] { "{Player" }, new Object[] { player.getName() }));
		item.addEnchantment(Enchantment.get(Enchantment.ID_SILK_TOUCH));
		player.getInventory().addItem(item);
	}
}
