package xiaokai.knickers.appliance;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.utils.Config;
import xiaokai.knickers.form.MakeForm;
import xiaokai.knickers.form.man.AddButton;
import xiaokai.knickers.mtp.Kick;
import xiaokai.knickers.mtp.MyPlayer;
import xiaokai.tool.CustomForm;
import xiaokai.tool.EnchantName;
import xiaokai.tool.SimpleForm;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class EstablishForm {
	public static Kick kick = Kick.kick;

	public static boolean Main(Player player) {
		return false;
	}

	/**
	 * 显示一个界面给玩家，让玩家选择要添加那种类型的快捷工具
	 * 
	 * @param player 要显示这个界面的玩家对象
	 * @return
	 */
	public static boolean AddItemByStyle(Player player) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player, Kick.kick.Message.getMessage("权限不足"));
		SimpleForm form = new SimpleForm(kick.formID.getID(14), "§6请选择想要添加的快捷工具类型",
				Tool.getColorFont("目前仅支持这几种，如有更新更好的建议请联系作者"));
		for (String ButtonName : Kick.FastToolType)
			form.addButton(Tool.getRandColor() + ButtonName);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 添加一个使用后打开界面的工具数据
	 * 
	 * @param player
	 * @return
	 */
	public static boolean OpenForm(Player player) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player, Kick.kick.Message.getMessage("权限不足"));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.CacheFile = new File(kick.mis.getDataFolder(), Kick.MenuConfigPath);
		if (myPlayer.CacheFile.list().length < 1)
			return MakeForm.Tip(player, "当前还没有可以添加的界面！请先添加一个吧！");
		ArrayList<String> Ops = new ArrayList<String>();
		for (String FileName : myPlayer.CacheFile.list())
			Ops.add(FileName + "§f(§9"
					+ kick.Message.getText(
							(new Config(new File(myPlayer.CacheFile, FileName), Config.YAML)).getString("Title", ""),
							new String[] { "{Player}" }, new Object[] { player.getName() })
					+ "§f)");
		CustomForm form = new CustomForm(kick.formID.getID(15), Tool.getColorFont(Kick.FastToolType[0]));
		form.addDropdown("§6请选择想要打开的界面，若不存在，请先创建！", Ops);
		form.addInput("§6要添加使用的快捷工具的名称或者ID", AddButton.Dispose.getHandItemID(player), "若不需要，可以留空，可使用命令将属性添加到单独物品");
		form.addInput("§6请输入工具的名称",
				kick.Message.getMessage("快捷工具名称", new String[] { "{Player}" }, new Object[] { player.getName() }));
		form.addInput("§6请输入工具的Lore",
				kick.Message.getMessage("快捷工具名称2", new String[] { "{Player}" }, new Object[] { player.getName() }));
		form.addInput("§6请输入工具的附魔，若多个附魔请使用；分割\n" + getEnchantString(), "", "可以支持ID或附魔名称");
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 添加模拟点击类型的工具处理类
	 * 
	 * @author Winfxk
	 *
	 */
	public static class ButtonByPlayer {
		/**
		 * 添加一个使用后模拟点击按钮的工具数据
		 * 
		 * @param player
		 * @return
		 */
		public static boolean addByType(Player player) {
			if (!Kick.isAdmin(player))
				return MakeForm.Tip(player, Kick.kick.Message.getMessage("权限不足"));
			MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
			myPlayer.CacheFile = new File(kick.mis.getDataFolder(), Kick.MenuConfigPath);
			if (myPlayer.CacheFile.list().length < 1)
				return MakeForm.Tip(player, "当前还没有可以添加的界面！请先添加一个吧！");
			ArrayList<String> Ops = new ArrayList<String>();
			for (String FileName : myPlayer.CacheFile.list())
				Ops.add(FileName + "§f(§9"
						+ kick.Message.getText((new Config(new File(myPlayer.CacheFile, FileName), Config.YAML))
								.getString("Title", ""), new String[] { "{Player}" }, new Object[] { player.getName() })
						+ "§f)");
			CustomForm form = new CustomForm(kick.formID.getID(16), Tool.getColorFont(Kick.FastToolType[1]));
			form.addDropdown("§6请选择想要打开的界面，若不存在，请先创建！", Ops);
			kick.PlayerDataMap.put(player.getName(), myPlayer);
			form.sendPlayer(player);
			return true;
		}

		/**
		 * 开始从已经选取的文件里面读取按钮列表
		 * 
		 * @param player
		 * @param data
		 * @return
		 */
		public static boolean add(Player player, FormResponseCustom data) {
			if (!Kick.isAdmin(player))
				return MakeForm.Tip(player, Kick.kick.Message.getMessage("权限不足"));
			MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
			File file = new File(myPlayer.CacheFile,
					myPlayer.CacheFile.list()[data.getDropdownResponse(0).getElementID()]);
			myPlayer.CacheFile = file;
			Config config = new Config(file, Config.YAML);
			Object object = config.get("Buttons");
			Map<String, Object> Buttons = (object != null && object instanceof Map) ? (HashMap<String, Object>) object
					: new HashMap<String, Object>();
			if (Buttons.size() < 1)
				return MakeForm.Tip(player, "当前还没有可以添加的界面！请先添加一个吧！");
			CustomForm form = new CustomForm(kick.formID.getID(17), Tool.getColorFont(Kick.FastToolType[1]));
			ArrayList<String> buttonStrings = new ArrayList<String>();
			List<String> Keys = new ArrayList<String>();
			for (String s : Buttons.keySet()) {
				buttonStrings.add(s + "§f(" + kick.Message.getText(((Map<String, Object>) Buttons.get(s)).get("Text"),
						new String[] { "{Player}" }, new Object[] { player.getName() }) + "§f)");
				Keys.add(s);
			}
			form.addDropdown("请选择想要模拟的按钮", buttonStrings);
			form.addInput("§6要添加使用的快捷工具的名称或者ID", AddButton.Dispose.getHandItemID(player), "若不需要，可以留空，可使用命令将属性添加到单独物品");
			form.addInput("§6请输入工具的名称",
					kick.Message.getMessage("快捷工具名称", new String[] { "{Player}" }, new Object[] { player.getName() }));
			form.addInput("§6请输入工具的Lore",
					kick.Message.getMessage("快捷工具名称2", new String[] { "{Player}" }, new Object[] { player.getName() }));
			form.addInput("§6请输入工具的附魔，若多个附魔请使用；分割\n" + getEnchantString(), "", "可以支持ID或附魔名称");
			myPlayer.keyList = Keys;
			kick.PlayerDataMap.put(player.getName(), myPlayer);
			form.sendPlayer(player);
			return true;
		}
	}

	/**
	 * 获取一个附魔列表的字符串
	 * 
	 * @return
	 */
	public static String getEnchantString() {
		String string = "";
		List<EnchantName> EnchantS = EnchantName.getAll();
		int a = -1;
		for (EnchantName Enchant : EnchantS)
			string += "§2" + Enchant.getID() + "§f>§5" + Enchant.getName() + (((a++) - 1) % 3 == 0 ? "\n" : "  ");
		return string;
	}
}
