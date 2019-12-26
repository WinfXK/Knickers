package xiaokai.knickers.tool;

import xiaokai.knickers.Activate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class ItemID {
	public static ItemID itemID;
	private static Config config;
	private static Map<String, Map<String, String>> iDList;
	private static int Items;

	public ItemID() {
		itemID = this;
		Items = ItemID.load();
	}

	/**
	 * 初始化数据
	 * 
	 * @return
	 */
	public static int load() {
		Activate kick = Activate.getActivate();
		iDList = new HashMap<>();
		ArrayList<HashMap<String, Object>> All = ItemIDSunName.getAll();
		for (HashMap<String, Object> map2 : All) {
			Map<String, String> Item = new HashMap<>();
			String iDString = map2.get("ID") + ":" + map2.get("Damage");
			Item.put("ID", iDString);
			Item.put("Path", (String) map2.get("Path"));
			Item.put("Name", (String) map2.get("Name"));
			iDList.put(iDString, Item);
		}
		config = new Config(new File(kick.getKnickers().getDataFolder(), Activate.ItemIDConfigName), Config.YAML);
		Map<String, Object> map = config.getAll();
		Set<String> keys = map.keySet();
		for (String ID : keys) {
			Object obj = map.get(ID);
			int[] IDs = Tool.IDtoFullID(ID);
			ID = IDs[0] + ":" + IDs[1];
			if (obj instanceof Map) {
				Map<String, Object> map1 = (Map<String, Object>) obj;
				Map<String, String> Item = new HashMap<>();
				Item.put("ID", ID);
				String Path = (String) map1.get("Path");
				if (Path == null || Path.isEmpty())
					kick.getKnickers().getLogger().error(kick.getMessage().getSun("自定义物品列表", "构建失败", "Path",
							new String[] { "{ItemID}" }, new Object[] { ID }));
				Item.put("Path", Path);
				String Name = (String) map1.get("Name");
				if (Name == null || Name.isEmpty())
					kick.getKnickers().getLogger().error(kick.getMessage().getSun("自定义物品列表", "构建失败", "ID",
							new String[] { "{ItemID}" }, new Object[] { ID }));
				Item.put("Name", Name);
				iDList.put(ID, Item);
			} else
				kick.getKnickers().getLogger().error(kick.getMessage().getSun("自定义物品列表", "构建失败", "Unknown",
						new String[] { "{ItemID}" }, new Object[] { ID }));
		}
		ItemID.Items = iDList.size();
		return iDList.size();
	}

	/**
	 * 啥玩意都不知道获取Item对象
	 * 
	 * @param obj
	 * @return
	 */
	public static Item UnknownToItem(Object obj) {
		return UnknownToItem(obj, null);
	}

	/**
	 * 啥玩意都不知道获取Item对象
	 * 
	 * @param obj
	 * @param Default
	 * @return
	 */
	public static Item UnknownToItem(Object obj, Item Default) {
		if (obj == null || String.valueOf(obj).isEmpty())
			return Default;
		String ID = UnknownToID(obj, null);
		if (ID == null)
			return Default;
		int[] IDs = Tool.IDtoFullID(ID);
		return new Item(IDs[0], IDs[1]);
	}

	/**
	 * 根据物品的对象获取ID
	 * 
	 * @param item
	 * @return
	 */
	public static String getID(Item item) {
		return item.getId() + ":" + item.getDamage();
	}

	/**
	 * 根据物品的对象获取贴图路径
	 * 
	 * @param item
	 * @return
	 */
	public static String getPath(Item item) {
		return getPathByID(item.getId(), item.getDamage());
	}

	/**
	 * 根据物品的对象获取名字
	 * 
	 * @param item
	 * @return
	 */
	public static String getName(Item item) {
		return item.hasCustomName() ? item.getName() : getNameByID(item.getId(), item.getDamage());
	}

	/**
	 * 根据一个未知值，获取物品的名称贴图路径
	 * 
	 * @param obj 未知的参数
	 * @return
	 */
	public static String UnknownToName(Object obj) {
		String iDString = UnknownToID(obj);
		return getNameByID(iDString, null);
	}

	/**
	 * 根据一个未知值，获取物品的名称贴图路径
	 * 
	 * @param obj     未知的参数
	 * @param Default 若获取的值为空，默认返回的值
	 * @return
	 */
	public static String UnknownToName(Object obj, String Default) {
		String iDString = UnknownToID(obj);
		return getNameByID(iDString, Default);
	}

	/**
	 * 根据一个未知值，获取物品的贴图路径
	 * 
	 * @param obj 未知的参数
	 * @return
	 */
	public static String UnknownToPath(Object obj) {
		if (obj == null)
			return null;
		return UnknownToPath(obj, obj.toString());
	}

	/**
	 * 根据一个未知值，获取物品的贴图路径
	 * 
	 * @param obj     未知的参数
	 * @param Default 若获取的值为空，默认返回的值
	 * @return
	 */
	public static String UnknownToPath(Object obj, String Default) {
		String iDString = UnknownToID(obj);
		return getPathByID(iDString, Default);
	}

	/**
	 * 根据一个未知值获取ID
	 * 
	 * @param obj 已知的未知值
	 * @return
	 */
	public static String UnknownToID(Object obj) {
		if (obj == null)
			return null;
		return UnknownToID(obj, obj.toString());
	}

	/**
	 * 根据一个未知值获取ID
	 * 
	 * @param obj     已知的未知值
	 * @param Default 若获取值为空，默认返回的值
	 * @return
	 */
	public static String UnknownToID(Object obj, String Default) {
		String string = String.valueOf(obj);
		String iDString = string.contains(":") ? string : string + ":0";
		String Name = getNameByID(iDString);
		if (Name != null)
			return iDString;
		String ID = getIDByName(string);
		if (ID != null)
			return ID;
		Set<String> Keys = iDList.keySet();
		for (String ike : Keys) {
			Map<String, String> map = iDList.get(ike);
			if (map.get("Name").equals(string) || ike.equals(string) || map.get("Path").equals(string))
				return ike;
		}
		return Default;
	}

	/**
	 * 根据物品名称获取物品ID
	 * 
	 * @param Name 要获取ID的物品名称
	 * @return
	 */
	public static String getIDByName(String Name) {
		return getIDByName(Name, null);
	}

	/**
	 * 根据物品名称获取物品ID
	 * 
	 * @param Name    要获取ID的物品名称
	 * @param Default 若获取值为空，默认返回的物品ID
	 * @return
	 */
	public static String getIDByName(String Name, String Default) {
		Set<String> Keys = iDList.keySet();
		for (String ID : Keys) {
			Map<String, String> map = iDList.get(ID);
			if (map == null)
				continue;
			if (map.get("Name").equals(Name))
				return ID;
		}
		return Default;
	}

	/**
	 * 根据物品名称获取物品贴图路径
	 * 
	 * @param Name 物品的名称
	 * @return
	 */
	public static String getPathByName(String Name) {
		return getPathByName(Name, null);
	}

	/**
	 * 根据物品名称获取物品贴图路径
	 * 
	 * @param Name    物品的名称
	 * @param Default 当获取的值为空时默认返回的值
	 * @return
	 */
	public static String getPathByName(String Name, String Default) {
		String ID = getIDByName(Name);
		if (ID != null)
			return getPathByID(ID);
		return Default;
	}

	/**
	 * 根据ID获取物品的贴图路径
	 * 
	 * @param ID     要获取名称的物品ID
	 * @param Damage 要获取名称的物品特殊值
	 * @return
	 */
	public static String getPathByID(int ID, int Damage) {
		return getPathByID(ID + ":" + Damage, null);
	}

	/**
	 * 根据ID获取物品的贴图路径
	 * 
	 * @param ID      要获取名称的物品ID
	 * @param Damage  要获取名称的物品特殊值
	 * @param Default 若为空默认返回的贴图路径
	 * @return
	 */
	public static String getPathByID(int ID, int Damage, String Default) {
		return getPathByID(ID + ":" + Damage, Default);
	}

	/**
	 * 根据ID获取物品的贴图路径
	 * 
	 * @param ID 要获取名称的物品ID
	 * @return
	 */
	public static String getPathByID(int ID) {
		return getPathByID(ID, null);
	}

	/**
	 * 根据ID获取物品的贴图路径
	 * 
	 * @param ID      要获取名称的物品ID
	 * @param Default 若为空默认返回的名字
	 * @return
	 */
	public static String getPathByID(int ID, String Default) {
		return getPathByID(ID + ":0", Default);
	}

	/**
	 * 根据ID获取物品的贴图路径
	 * 
	 * @param ID 要获取名称的物品ID
	 * @return
	 */
	public static String getPathByID(String ID) {
		return getPathByID(ID, null);
	}

	/**
	 * 根据ID获取物品的贴图路径
	 * 
	 * @param ID      物品的贴图路径
	 * @param Default 若获取的物品贴图路径不存在默认返回的内容
	 * @return
	 */
	public static String getPathByID(String ID, String Default) {
		ID = ID.contains(":") ? ID : ID + ":0";
		String[] strings = ID.split(":");
		int intID = Tool.ObjectToInt(strings[0]);
		int intDD = Tool.ObjectToInt(strings[1]);
		Map<String, String> map = iDList.get(ID);
		if (map == null)
			if (Activate.getActivate().getConfig().getBoolean("近似ID") && intDD != 0) {
				if (iDList.get(intID + ":" + intDD) == null) {
					if (intDD > 0)
						for (int i = intDD; i > 0; i--)
							if (iDList.get(intID + ":" + intDD) != null)
								return iDList.get(intID + ":" + intDD).get("Path");
					return Default;
				} else
					map = iDList.get(intID + ":" + intDD);
			} else
				return Default;
		return map.get("Path");
	}

	/**
	 * 根据ID获取物品的名称
	 * 
	 * @param ID     要获取名称的物品ID
	 * @param Damage 要获取名称的物品特殊值
	 * @return
	 */
	public static String getNameByID(int ID, int Damage) {
		return getNameByID(ID + ":" + Damage, null);
	}

	/**
	 * 根据ID获取物品的名称
	 * 
	 * @param ID      要获取名称的物品ID
	 * @param Damage  要获取名称的物品特殊值
	 * @param Default 若为空默认返回的名字
	 * @return
	 */
	public static String getNameByID(int ID, int Damage, String Default) {
		return getNameByID(ID + ":" + Damage, Default);
	}

	/**
	 * 根据ID获取物品的名称
	 * 
	 * @param ID 要获取名称的物品ID
	 * @return
	 */
	public static String getNameByID(int ID) {
		return getNameByID(ID, null);
	}

	/**
	 * 根据ID获取物品的名称
	 * 
	 * @param ID      要获取名称的物品ID
	 * @param Default 若为空默认返回的名字
	 * @return
	 */
	public static String getNameByID(int ID, String Default) {
		return getNameByID(ID + ":0", Default);
	}

	/**
	 * 根据ID获取物品的名称
	 * 
	 * @param ID 要获取名称的物品ID
	 * @return
	 */
	public static String getNameByID(String ID) {
		return getNameByID(ID, null);
	}

	/**
	 * 根据ID获取物品的名称
	 * 
	 * @param ID      要获取名称的物品ID
	 * @param Default 若为空默认返回的名字
	 * @return
	 */
	public static String getNameByID(String ID, String Default) {
		ID = ID.contains(":") ? ID : ID + ":0";
		String[] strings = ID.split(":");
		int intID = Tool.ObjectToInt(strings[0]);
		int intDD = Tool.ObjectToInt(strings[1]);
		Map<String, String> map = iDList.get(ID);
		if (map == null)
			if (Activate.getActivate().getConfig().getBoolean("近似ID") && intDD != 0) {
				if (iDList.get(intID + ":" + intDD) == null) {
					if (intDD > 0)
						for (int i = intDD; i > 0; i--)
							if (iDList.get(intID + ":" + intDD) != null)
								return iDList.get(intID + ":" + intDD).get("Name");
					return Default;
				} else
					map = iDList.get(intID + ":" + intDD);
			} else
				return Default;
		return map.get("Name");
	}

	/**
	 * 获取自定义物品的物品配置文件
	 * 
	 * @return
	 */
	public static Config getConfig() {
		return config;
	}

	/**
	 * 获取已知的所有自定义物品数据
	 * 
	 * @return
	 */
	public static Map<String, Map<String, String>> getiDList() {
		return iDList;
	}

	/**
	 * 获取已经加载的物品数量
	 * 
	 * @return
	 */
	public static int getItems() {
		return Items;
	}
}
