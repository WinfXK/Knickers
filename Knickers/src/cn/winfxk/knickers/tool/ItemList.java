package cn.winfxk.knickers.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.item.Item;
import cn.winfxk.knickers.Knickers;
import cn.winfxk.knickers.form.FormException;

/**
 * @Createdate 2020/05/11 16:51:47
 * @author Winfxk
 */
public class ItemList {
	private String Name, Path;
	public static ItemList item;
	private int ID, Damage;
	private List<ItemList> list = new ArrayList<>();
	private static final Config config = new Config(new File(Knickers.kis.getDataFolder(), Knickers.ItemList));

	/**
	 * 通过Item对象获取数据对象
	 * 
	 * @param item Item对象
	 * @return
	 */
	public ItemList getItemList(Item item) {
		return getItemList(item, false, null);
	}

	/**
	 * 通过Item对象获取数据对象
	 * 
	 * @param item    Item对象
	 * @param match   是否模糊搜索匹配
	 * @param Default 默认返回的内容
	 * @return
	 */
	public ItemList getItemList(Item item, boolean match, ItemList Default) {
		for (ItemList itemList : list)
			if (itemList.getID() == item.getId() && (match || item.getDamage() == itemList.getDamage()))
				return itemList;
		return Default;
	}

	/**
	 * 根据一串未知数据返回一个物品ID
	 * 
	 * @param obj     未知数据<包含的可能性为：ID、名称、ID:Damage>
	 * @param match   是否粗略匹配
	 * @param Default 匹配失败将会返回的内容
	 * @return
	 */
	public String objToFullID(Object obj, boolean match, String Default) {
		String string = Tool.objToString(obj);
		if (obj == null || string == null || string.isEmpty())
			return Default;
		if (obj instanceof Item) {
			Item item = (Item) obj;
			return item.getId() + ":" + item.getDamage();
		}
		if (obj instanceof ItemList)
			return ((ItemList) obj).getFullID();
		if (!Tool.isInteger(string)) {
			if (string.contains(":")) {
				String[] strings = string.split(":");
				if (Tool.isInteger(strings[0]))
					return Tool.ObjToInt(strings[0]) + ":" + (strings.length >= 2 ? strings[1] : "0");
			}
			for (ItemList list : this.list)
				if (string.equals(list.Name) || (match && string.toLowerCase().equals(list.Name.toLowerCase())))
					return list.ID + ":" + list.Damage;
		} else
			return string + ":0";
		return Default;
	}

	/**
	 * 将一个未知数据转换为Item
	 * 
	 * @param obj 未知数据
	 * @return
	 */
	public Item getItem(Object obj) {
		return objToItem(obj, true, null);
	}

	/**
	 * 将一个未知数据转换为Item
	 * 
	 * @param obj     未知数据
	 * @param match   是否粗略匹配
	 * @param Default 若全部匹配失败将会返回的内容
	 * @return
	 */
	public Item objToItem(Object obj, boolean match, Item Default) {
		String string = Tool.objToString(obj);
		if (obj == null || string == null || string.isEmpty())
			return Default;
		if (obj instanceof Item)
			return (Item) obj;
		if (obj instanceof ItemList)
			return ((ItemList) obj).getItem();
		if (Tool.isInteger(string))
			return new Item(Tool.ObjToInt(obj));
		if (string.contains(":")) {
			String[] strings = string.split(":");
			if (Tool.isInteger(strings[0]))
				return new Item(Tool.ObjToInt(strings[0]),
						strings.length >= 2 && Tool.isInteger(strings[1]) ? Tool.ObjToInt(strings[0]) : 0);
		}
		for (ItemList list : this.list)
			if (string.equals(list.Name) || (match && string.toLowerCase().equals(list.Name.toLowerCase())))
				return getItem();
		return Default;
	}

	/**
	 * 根据一串位置数据返回一个物品ID
	 * 
	 * @param obj     未知数据<包含的可能性为：ID、名称、ID:Damage>
	 * @param match   是否粗略匹配
	 * @param Default 匹配失败将会返回的内容
	 * @return
	 */
	public int objToID(Object obj, boolean match, Object Default) {
		String string = Tool.objToString(obj);
		if (obj == null || string == null || string.isEmpty())
			return Tool.isInteger(Default) ? Tool.ObjToInt(Default) : 0;
		if (obj instanceof Item)
			return ((Item) obj).getId();
		if (!Tool.isInteger(string)) {
			if (string.contains(":")) {
				String[] strings = string.split(":");
				if (Tool.isInteger(strings[0]))
					return Tool.ObjToInt(strings[0]);
			}
			for (ItemList list : this.list)
				if (string.equals(list.Name) || (match && string.toLowerCase().equals(list.Name.toLowerCase())))
					return list.ID;
		} else
			return Tool.ObjToInt(string);
		return Tool.isInteger(Default) ? Tool.ObjToInt(Default) : 0;
	}

	/**
	 * 根据一串位置数据匹配返回一个物品的名称
	 * 
	 * @param obj 位置数据<包含的可能性为：ID、名称、ID:Damage>
	 * @return
	 */
	public String objToName(Object obj) {
		return objToName(obj, true, null);
	}

	/**
	 * 根据一串位置数据匹配返回一个物品的名称
	 * 
	 * @param obj     位置数据<包含的可能性为：ID、名称、ID:Damage>
	 * @param match   是否粗略匹配
	 * @param Default 匹配失败将会返回的内容
	 * @return
	 */
	public String objToName(Object obj, boolean match, Object Default) {
		String string = Tool.objToString(obj);
		if (obj == null || string == null || string.isEmpty())
			return Tool.objToString(Default);
		if (obj instanceof Item)
			return getName((Item) obj, match, Default);
		if (!Tool.isInteger(string)) {
			if (string.contains(":")) {
				int ID, Damage = 0;
				String[] strings = string.split(":");
				if (Tool.isInteger(strings[0])) {
					ID = Tool.ObjToInt(strings[0]);
					if (strings.length >= 2 && Tool.isInteger(strings[1]))
						Damage = Tool.ObjToInt(strings[1]);
					return getName(ID, Damage, match, Default);
				}
			}
			for (ItemList list : this.list)
				if (string.equals(list.Name) || (match && string.toLowerCase().equals(list.Name.toLowerCase())))
					return list.Name;
		} else
			return getName(Tool.ObjToInt(string), 0, match, Default);
		return Tool.objToString(Default);
	}

	/**
	 * 根据一串位置数据匹配返回一个物品的贴图
	 * 
	 * @param obj 位置数据<包含的可能性为：ID、名称、ID:Damage>
	 * @return
	 */
	public String objToPath(Object obj) {
		return objToPath(obj, true, null);
	}

	/**
	 * 根据一串位置数据匹配返回一个物品的贴图
	 * 
	 * @param obj     位置数据<包含的可能性为：ID、名称、ID:Damage>
	 * @param match   是否粗略匹配
	 * @param Default 匹配失败将会返回的内容
	 * @return
	 */
	public String objToPath(Object obj, boolean match, Object Default) {
		String string = Tool.objToString(obj);
		if (obj == null || string == null || string.isEmpty())
			return Tool.objToString(Default);
		if (obj instanceof Item)
			return getPath((Item) obj, match, Default);
		if (!Tool.isInteger(string)) {
			if (string.contains(":")) {
				int ID, Damage = 0;
				String[] strings = string.split(":");
				if (Tool.isInteger(strings[0])) {
					ID = Tool.ObjToInt(strings[0]);
					if (strings.length >= 2 && Tool.isInteger(strings[1]))
						Damage = Tool.ObjToInt(strings[1]);
					return getPath(ID, Damage, match, Default);
				}
			}
			return getPath(string, match, Default);
		} else
			return getPath(Tool.ObjToInt(obj), 0, match, Default);
	}

	/**
	 * 根据物品名称返回物品贴图
	 * 
	 * @param Name 物品名称
	 * @return
	 */
	public String getPath(String Name) {
		return getPath(Name, true, null);
	}

	/**
	 * 根据物品名称返回物品贴图
	 * 
	 * @param Name    物品名称
	 * @param match   物品名称是否忽略大小写
	 * @param Default 若匹配失败将会返回的内容
	 * @return
	 */
	public String getPath(String Name, boolean match, Object Default) {
		ItemList item = null;
		for (ItemList list : this.list)
			if (list.getName().equals(Name) || (match && list.getName().toLowerCase().equals(Name.toLowerCase()))) {
				item = list;
				break;
			}
		return item != null ? item.Path : Default != null ? Tool.objToString(Default) : null;
	}

	/**
	 * 根据物品名称返回物品ID
	 * 
	 * @param Name  物品名称
	 * @param match 是否忽略大小写
	 * @return
	 */
	public int getID(String Name) {
		return getID(Name, true, 0);
	}

	/**
	 * 根据物品名称返回物品ID
	 * 
	 * @param Name    物品名称
	 * @param match   是否忽略大小写
	 * @param Default 当匹配失败时将会返回的内容
	 * @return
	 */
	public int getID(String Name, boolean match, Object Default) {
		ItemList item = null;
		for (ItemList list : this.list)
			if (list.getName().equals(Name) || (match && list.getName().toLowerCase().equals(Name.toLowerCase()))) {
				item = list;
				break;
			}
		return item != null ? item.getID() : Default != null && Tool.isInteger(Default) ? Tool.ObjToInt(Default) : 0;
	}

	/**
	 * 根据物品对象返回物品ID
	 * 
	 * @param item 物品对象
	 * @return
	 */
	public int getID(Item item) {
		return item.getId();
	}

	/**
	 * 根据物品对象返回物品的贴图
	 * 
	 * @param item 物品对象
	 * @return
	 */
	public String getPath(Item item) {
		return getPath(item.getId(), item.getDamage());
	}

	/**
	 * 根据物品对象返回物品的贴图
	 * 
	 * @param item    物品对象
	 * @param match   是否近似匹配物品特殊值
	 * @param Default 若匹配失败将会返回的内容
	 * @return
	 */
	public String getPath(Item item, boolean match, Object Default) {
		return getPath(item.getId(), item.getDamage(), match, Default);
	}

	/**
	 * 根据物品ID和特殊值返回物品的贴图
	 * 
	 * @param ID     物品ID
	 * @param Damage 物品特殊值
	 * @return
	 */
	public String getPath(int ID, int Damage) {
		return getPath(ID, Damage, true, null);
	}

	/**
	 * 根据物品ID返回物品的贴图
	 * 
	 * @param ID 物品ID
	 * @return
	 */
	public String getPath(int ID) {
		return getPath(ID, 0, true, null);
	}

	/**
	 * 根据物品ID和特殊值返回物品的贴图
	 * 
	 * @param ID      物品ID
	 * @param Damage  物品特殊值
	 * @param match   是否近似匹配物品特殊值
	 * @param Default 若匹配失败将会返回的内容
	 * @return
	 */
	public String getPath(int ID, int Damage, boolean match, Object Default) {
		ItemList list = null;
		for (ItemList l : this.list)
			if (l.ID == ID && l.Damage == Damage) {
				list = l;
				break;
			} else if (match && l.ID == ID)
				list = list == null ? list = l
						: (Math.abs(list.Damage - Damage) > Math.abs(l.Damage - Damage)) ? l : list;
		return list != null ? list.getPath() : Default == null ? null : Tool.objToString(Default);
	}

	/**
	 * 根据物品ID返回一个物品的名称
	 * 
	 * @param ID 物品ID
	 * @return
	 */
	public String getName(int ID) {
		return getName(ID, 0, true, null);
	}

	/**
	 * 根据物品对象获取一个物品的名称
	 * 
	 * @param item 物品的对象
	 * @return
	 */
	public String getName(Item item) {
		return getName(item, true, item.getName());
	}

	/**
	 * 根据物品对象获取一个物品的名称
	 * 
	 * @param item    物品的对象
	 * @param match   是否近似匹配特殊值
	 * @param Default 若匹配失败将会返回的内容
	 * @return
	 */
	public String getName(Item item, boolean match, Object Default) {
		return item.hasCustomName() ? item.getName() : getName(item.getId(), item.getDamage(), match, Default);
	}

	/**
	 * 根据物品ID和特殊值返回一个物品的名称
	 * 
	 * @param ID     物品ID
	 * @param Damage 物品特殊值
	 * @param match  是否近似匹配物品特殊值
	 * @return
	 */
	public String getName(int ID, int Damage, boolean match) {
		return getName(ID, Damage, match, null);
	}

	/**
	 * 根据物品ID和特殊值返回一个物品的名称
	 * 
	 * @param ID      物品ID
	 * @param Damage  物品特殊值
	 * @param match   是否近似匹配物品特殊值
	 * @param Default 若匹配失败将会返回的内容
	 * @return
	 */
	public String getName(int ID, int Damage, boolean match, Object Default) {
		ItemList list = null;
		for (ItemList l : this.list)
			if (l.ID == ID && l.Damage == Damage) {
				list = l;
				break;
			} else if (match && l.ID == ID)
				list = list == null ? list = l
						: (Math.abs(list.Damage - Damage) > Math.abs(l.Damage - Damage)) ? l : list;
		return list != null ? list.getName() : Default == null ? null : Tool.objToString(Default);
	}

	/**
	 * 重新加载物品列表
	 * 
	 * @return
	 */
	public int reload() {
		list = new ArrayList<>();
		Map<String, Object> map = config.getMap();
		Map<String, Object> item;
		for (Object obj : map.values()) {
			item = obj != null && obj instanceof Map ? (HashMap<String, Object>) obj : new HashMap<>();
			if (item.size() <= 0)
				continue;
			try {
				list.add(new ItemList(item.get("ID"), item.get("Damage"), item.get("Name"), item.get("Path")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list.size();
	}

	/**
	 * 新建一个物品对象
	 * 
	 * @param ID     物品ID
	 * @param Damage 物品特殊值
	 * @param Name   物品名称
	 * @param Path   物品贴图
	 * @throws Exception
	 */
	private ItemList(Object ID, Object Damage, Object Name, Object Path) throws Exception {
		if (ID == null || Damage == null || Name == null || Path == null)
			throw new FormException("The item data passed is null");
		if (!Tool.isInteger(Damage) || !Tool.isInteger(ID))
			throw new FormException("Item ID or Damage does not match (need to be a pure integer greater than zero)!");
		this.ID = Tool.ObjToInt(ID);
		this.Name = Tool.objToString(Name);
		this.Path = Tool.objToString(Path);
		this.Damage = Tool.ObjToInt(Damage);
	}

	/**
	 * 获取物品特殊值
	 * 
	 * @return
	 */
	public int getDamage() {
		return Damage;
	}

	/**
	 * 获取物品ID
	 * 
	 * @return
	 */
	public int getID() {
		return ID;
	}

	/**
	 * 返回完整的ID
	 * 
	 * @return
	 */
	public String getFullID() {
		return ID + ":" + Damage;
	}

	@Override
	public String toString() {
		return "Name: " + getName() + "\nID: " + getID() + "\nDamage: " + getDamage() + "\nPath: " + getPath();
	}

	/**
	 * 获取物品名称
	 * 
	 * @return
	 */
	public String getName() {
		return Name;
	}

	/**
	 * 获取物品贴图
	 * 
	 * @return
	 */
	public String getPath() {
		return Path;
	}

	/**
	 * 获取所有数据
	 * 
	 * @return
	 */
	public List<ItemList> getAll() {
		return new ArrayList<>(list);
	}

	/**
	 * 返回对应的物品
	 * 
	 * @return
	 */
	public Item getItem() {
		return new Item(ID, Damage);
	}

	/**
	 * 外部构造
	 * 
	 * @param activate
	 */
	public ItemList() {
		reload();
		item = this;
	}
}
