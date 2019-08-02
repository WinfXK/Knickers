package xiaokai.knickers.appliance;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import xiaokai.knickers.mtp.Kick;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Appliance {
	private Kick kick;
	private LinkedHashMap<String, Map<String, Object>> FormList;
	private Config config;

	/**
	 * 自定义快捷工具工具
	 * 
	 * @param kick
	 */
	public Appliance(Kick kick) {
		this.kick = kick;
		this.enclose();
	}

	/**
	 * 取得自定义表单对象
	 * 
	 * @return
	 */
	public static LinkedHashMap<String, Map<String, Object>> getFormList() {
		return Kick.kick.App.FormList;
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
		return (getFormList().containsKey(String.valueOf(item.getId()))
				|| getFormList().containsKey(item.getId() + ":" + item.getDamage()));
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
		String ID = String.valueOf(item.getId());
		Map<String, Object> map = getFormList().containsKey(ID) ? getFormList().get(ID)
				: getFormList().get(ID + ":" + item.getDamage());
		return Tool.ObjToBool(map.get("允许丢弃"));
	}

	/**
	 * 开始处理玩家使用快捷工具的事件
	 * 
	 * @param player 使用自定义快捷工具的玩家对象
	 * @param item   使用的快捷工具的Item对象
	 * @return
	 */
	public boolean start(Player player, Item item) {
		
		return true;
	}
}
