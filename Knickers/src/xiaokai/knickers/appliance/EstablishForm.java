package xiaokai.knickers.appliance;

import xiaokai.knickers.form.MakeForm;
import xiaokai.knickers.form.man.AddButton;
import xiaokai.knickers.mtp.Kick;
import xiaokai.knickers.mtp.MyPlayer;
import xiaokai.knickers.tool.CustomForm;
import xiaokai.knickers.tool.EnchantName;
import xiaokai.knickers.tool.ItemIDSunName;
import xiaokai.knickers.tool.SimpleForm;
import xiaokai.knickers.tool.Tool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class EstablishForm {
	public static Kick kick = Kick.kick;

	/**
	 * 处理玩家点击删除快捷工具的数据，这是处理列表返回的
	 * 
	 * @param player 玩家对象
	 * @param data   返回的数据
	 * @return 
	 */
	public static boolean disDelTool(Player player, FormResponseSimple data) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player, Kick.kick.Message.getMessage("权限不足"));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		String Key = myPlayer.keyList.get(data.getClickedButtonId());
		Map<String, Object> map = (Map<String, Object>) Appliance.config.get(Key);
		String ItemData = "§4确定要删除该工具吗？？\n\n\n\n";
		int ii = 0;
		for (String ike : map.keySet()) {
			String Color = Tool.getRandColor();
			ItemData += Color + ike + "§f: " + Color + map.get(ike) + (ii++ < map.size() ? "\n" : "");
		}
		SimpleForm form = new SimpleForm(kick.formID.getID(22), kick.Message.getSun("界面", "快捷工具列表页", "项目打开的标题",
				new String[] { "{Player}", "{ItemName}" }, new Object[] { player.getName(), kick.Message
						.getText(map.get("Name"), new String[] { "{Player}" }, new Object[] { player.getName() }) }),
				ItemData);
		form.addButton("§6取消");
		form.addButton("§4确定删除");
		myPlayer.Key = Key;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 处理玩家查看快捷工具列表点击的事件
	 * 
	 * @param player 点击的玩家
	 * @param data   返回的数据
	 * @return
	 */
	public static boolean disToolList(Player player, FormResponseSimple data) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		if (data.getClickedButtonId() > myPlayer.keyList.size())
			return MakeForm.Tip(player, kick.Message.getSun("界面", "快捷工具列表页", "项目查看异常", new String[] { "{Player}" },
					new Object[] { player.getName() }));
		String Key = myPlayer.keyList.get(data.getClickedButtonId());
		Map<String, Object> map = (Map<String, Object>) myPlayer.Item.get(Key);
		String ItemData = "";
		int ii = 0;
		for (String ike : map.keySet()) {
			String Color = Tool.getRandColor();
			ItemData += Color + ike + "§f: " + Color + map.get(ike) + (ii++ < map.size() ? "\n" : "");
		}
		SimpleForm form = new SimpleForm(kick.formID.getID(21),
				kick.Message.getSun("界面", "快捷工具列表页", "项目打开的标题", new String[] { "{Player}", "{ItemName}" },
						new Object[] { player.getName(),
								kick.Message.getText(map.get("Name"), new String[] { "{Player}" },
										new Object[] { player.getName() }) }),
				kick.Message.getSun("界面", "快捷工具列表页", "项目打开的内容", new String[] { "{Player}", "{ItemData}" },
						new Object[] { player.getName(), ItemData }));
		List<String> It = new ArrayList<>();
		form.addButton(kick.Message.getSun("界面", "快捷工具列表页", "确定按钮", new String[] { "{Player}", player.getName() },
				new Object[] { player.getName() }));
		It.add("ok");
		if (Kick.isAdmin(player) || (Tool.ObjToBool(map.get("isPrice")) && !Appliance.isAlready(player, Key)
				&& !Appliance.isRepetition(player, Key))) {
			int[] IDs = Tool.IDtoFullID(map.get("ID"));
			if (IDs[0] != 0) {
				It.add("get");
				form.addButton(kick.Message.getSun("界面", "快捷工具列表页", "获取工具",
						new String[] { "{Player}", player.getName() }, new Object[] { player.getName() }));
			}
		}
		if (Kick.isAdmin(player) || (Tool.ObjToBool(map.get("isWrite")) && !Appliance.isAlready(player, Key)
				&& !Appliance.isRepetition(player, Key))) {
			It.add("put");
			form.addButton(kick.Message.getSun("界面", "快捷工具列表页", "导入按钮", new String[] { "{Player}", player.getName() },
					new Object[] { player.getName() }));
		} else
			form.addButton(kick.Message.getSun("界面", "快捷工具列表页", "取消按钮", new String[] { "{Player}", player.getName() },
					new Object[] { player.getName() }));
		myPlayer.Key = Key;
		myPlayer.Item = map;
		myPlayer.keyList = It;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 删除快捷工具
	 * 
	 * @param player
	 * @return
	 */
	public static boolean delTool(Player player) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player, Kick.kick.Message.getMessage("权限不足"));
		Map<String, Object> map = Appliance.config.getAll();
		if (map.size() < 1)
			return MakeForm.Tip(player, kick.Message.getSun("界面", "快捷工具列表页", "没有快捷工具", new String[] { "{Player}" },
					new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		SimpleForm form = new SimpleForm(kick.formID.getID(20),
				kick.Message.getSun("界面", "快捷工具列表页", "标题", new String[] { "{Player}" },
						new Object[] { player.getName() }),
				kick.Message.getSun("界面", "快捷工具列表页", "内容", new String[] { "{Player}" },
						new Object[] { player.getName() }));
		List<String> Keys = new ArrayList<>();
		for (String ike : map.keySet()) {
			Map<String, Object> Item = (Map<String, Object>) map.get(ike);
			Object obj = Item.get("ID");
			String ID = obj == null ? null : String.valueOf(obj);
			ID = ID == null || ID.isEmpty() ? null : ID;
			if (ID != null)
				ID = ItemIDSunName.UnknownToID(ID);
			form.addButton(kick.Message.getSun("界面", "快捷工具列表页", "列表",
					new String[] { "{Player}", "{Key}", "{Name}", "{ItemName}", "{ItemID}" },
					new Object[] { player.getName(), ike,
							kick.Message.getText(Item.get("Name"), new String[] { "{Player}" },
									new Object[] { player.getName() }),
							ID == null ? "null" : ItemIDSunName.getIDByName(ID), ID == null ? "null" : ID }));
			Keys.add(ike);
		}
		myPlayer.keyList = Keys;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 查看已有的快捷工具列表
	 * 
	 * @param player
	 * @return
	 */
	public static boolean showToolList(Player player) {
		Map<String, Object> map = Appliance.config.getAll();
		if (map.size() < 1)
			return MakeForm.Tip(player, kick.Message.getSun("界面", "快捷工具列表页", "没有快捷工具", new String[] { "{Player}" },
					new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		SimpleForm form = new SimpleForm(kick.formID.getID(19),
				kick.Message.getSun("界面", "快捷工具列表页", "标题", new String[] { "{Player}" },
						new Object[] { player.getName() }),
				kick.Message.getSun("界面", "快捷工具列表页", "内容", new String[] { "{Player}" },
						new Object[] { player.getName() }));
		List<String> Keys = new ArrayList<>();
		for (String ike : map.keySet()) {
			Map<String, Object> Item = (Map<String, Object>) map.get(ike);
			Object obj = Item.get("ID");
			String ID = obj == null ? null : String.valueOf(obj);
			ID = ID == null || ID.isEmpty() ? null : ID;
			if (ID != null)
				ID = ItemIDSunName.UnknownToID(ID);
			form.addButton(
					kick.Message.getSun("界面", "快捷工具列表页", "列表",
							new String[] { "{Player}", "{Key}", "{Name}", "{ItemName}", "{ItemID}" },
							new Object[] { player.getName(), ike,
									kick.Message.getText(Item.get("Name"), new String[] { "{Player}" },
											new Object[] { player.getName() }),
									ID == null ? "null" : ItemIDSunName.getIDByName(ID), ID == null ? "null" : ID }),
					true,
					ID != null ? kick.config.getBoolean("自定义工具列表显示工具图标") ? ItemIDSunName.getIDByPath(ID) : null : null);
			Keys.add(ike);
		}
		myPlayer.keyList = Keys;
		myPlayer.Item = map;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 自定义工具主页
	 * 
	 * @param player
	 * @return
	 */
	public static boolean Main(Player player) {
		SimpleForm form = new SimpleForm(kick.formID.getID(18),
				kick.Message.getSun("界面", "快捷工具", "标题", new String[] { "{Player}" }, new Object[] { player.getName() }),
				kick.Message.getSun("界面", "快捷工具", "内容", new String[] { "{Player}" },
						new Object[] { player.getName() }));
		form.addButton(kick.Message.getSun("界面", "快捷工具", "查看工具", new String[] { "{Player}" },
				new Object[] { player.getName() }));
		if (Kick.isAdmin(player))
			form.addButton("添加工具").addButton("删除工具");
		form.sendPlayer(player);
		return true;
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
		ArrayList<String> Ops = new ArrayList<>();
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
		form.addToggle("§6玩家是否可以通过“快捷工具”页面免费获取该工具", true);
		form.addToggle("§6是否异步强制玩家拥有该工具", true);
		form.addToggle("§6玩家是否可以丢弃该工具", false);
		form.addToggle("§6是否允许将数据导入到非指定的其他物品", true);
		form.addToggle("§6是否允许玩家多次获取", true);
		form.addToggle("§6是否允许玩家已经拥有该物品还在次申请", true);
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
			ArrayList<String> Ops = new ArrayList<>();
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
					: new HashMap<>();
			if (Buttons.size() < 1)
				return MakeForm.Tip(player, "这个界面当前还没有可以添加的按钮！请先添加一个吧！");
			CustomForm form = new CustomForm(kick.formID.getID(17), Tool.getColorFont(Kick.FastToolType[1]));
			ArrayList<String> buttonStrings = new ArrayList<>();
			List<String> Keys = new ArrayList<>();
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
			form.addToggle("§6玩家是否可以通过“快捷工具”页面免费获取该工具", true);
			form.addToggle("§6是否异步强制玩家拥有该工具", true);
			form.addToggle("§6玩家是否可以丢弃该工具", false);
			form.addToggle("§6是否允许将数据导入到非指定的其他物品", true);
			form.addToggle("§6是否允许玩家多次获取", true);
			form.addToggle("§6是否允许玩家已经拥有该物品还在次申请", true);
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
