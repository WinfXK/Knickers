package xiaokai.knickers.appliance;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.item.Item;
import xiaokai.knickers.mtp.Kick;

/**
 * @author Winfxk
 */
public class Handle {
	private Kick kick;

	/**
	 * 开始处理使用快捷工具的玩家对象
	 * 
	 * @param kick   Kick对象
	 * @param player 使用快捷工具的玩家对象
	 * @param Item   使用的跨界工具的项目数据对象
	 * @param item   使用的快捷工具的物品对象
	 */
	public Handle(Kick kick, Player player, Map<String, Object> Item, Item item) {
		this.kick = kick;
	}

	/**
	 * 开始判断玩家使用的快捷工具的类型
	 * 
	 * @return
	 */
	public boolean start() {
		return false;
	}

	public static class Form {
		public static boolean Switch(FormResponseSimple data) {
			// { "打开一个界面", "模拟点击按钮" };
			int ID = data.getClickedButtonId();
			return false;
		}

		public static boolean name() {
			return false;

		}
	}
}
