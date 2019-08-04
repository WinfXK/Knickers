package xiaokai.knickers.appliance;

import cn.nukkit.Player;
import xiaokai.knickers.mtp.Kick;
import xiaokai.tool.SimpleForm;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
public class EstablishForm {
	public static Kick kick = Kick.kick;

	/**
	 * 显示一个界面给玩家，让玩家选择要添加那种类型的快捷工具
	 * 
	 * @param player 要显示这个界面的玩家对象
	 * @return
	 */
	public static boolean AddItemByStyle(Player player) {
		SimpleForm form = new SimpleForm(kick.formID.getID(14), "§6请选择想要添加的快捷工具类型",
				Tool.getColorFont("目前仅支持这几种，如有更新更好的建议请联系作者"));
		for (String ButtonName : Kick.FastToolType)
			form.addButton(Tool.getRandColor() + ButtonName);
		form.sendPlayer(player);
		return false;
	}

	public static boolean Main(Player player) {
		return false;
	}
}
