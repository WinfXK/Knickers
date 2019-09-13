package xiaokai.knickers.form;

import xiaokai.knickers.form.man.AlterButton;
import xiaokai.knickers.mtp.Kick;
import xiaokai.knickers.mtp.Message;
import xiaokai.knickers.mtp.MyPlayer;
import xiaokai.knickers.tool.CustomForm;
import xiaokai.knickers.tool.ModalForm;
import xiaokai.knickers.tool.SimpleForm;
import xiaokai.knickers.tool.Tool;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */ 
@SuppressWarnings("unchecked")
public class MakeForm {
	/**
	 * 更多设置界面
	 * 
	 * @param player 要显示这个界面的玩家对象
	 * @param file   上级界面
	 * @return
	 */
	public static boolean MoreSettings(Player player, File file) {
		Kick kick = Kick.kick;
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player, kick.Message.getMessage("权限不足"));
		MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player.getName());
		myPlayer.CacheFile = file;
		Config config = new Config(file, Config.YAML);
		SimpleForm form = new SimpleForm(kick.formID.getID(25), kick.Message.getText(config.getString("Title", ""),
				new String[] { "{Player}" }, new Object[] { player.getName() }));
		List<String> kis = new ArrayList<>();
		Map<String, Object> Buttons = ((config.get("Buttons") instanceof Map) && config.get("Buttons") != null)
				? (HashMap<String, Object>) config.get("Buttons")
				: new HashMap<>();
		kis.add("add");
		form.addButton(Tool.getRandColor() + "添加按钮");
		if (Buttons.size() > 0) {
			kis.add("del");
			form.addButton(Tool.getRandColor() + "删除按钮");
			kis.add("alter");
			form.addButton(Tool.getRandColor() + "修改按钮");
		}
		kis.add("sf");
		form.addButton(Tool.getRandColor() + "修改界面");
		kis.add("set");
		form.addButton(Tool.getRandColor() + "系统设置");
		kis.add("back");
		form.addButton(Tool.getRandColor() + "返回上级");
		myPlayer.UIAdminButtonKis = kis;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 修改一个界面
	 * 
	 * @param player 要修改界面的玩家对象
	 * @param file   要修改得到界面配件
	 * @return
	 */
	public static boolean AlterForm(Player player, File file) {
		Kick kick = Kick.kick;
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player, kick.Message.getMessage("权限不足"));
		MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player.getName());
		myPlayer.CacheFile = file;
		Config config = new Config(file, Config.YAML);
		CustomForm form = new CustomForm(Kick.kick.formID.getID(24),
				Kick.kick.Message.getText(config.getString("Title")) + "Set");
		form.addInput("界面的标题", config.getString("Title"));
		form.addInput("界面的内容", config.getString("Content"));
		form.addStepSlider("过滤模式", Kick.FilteredModel, Tool.ObjectToInt("FilteredModel"));
		form.addInput("屏蔽列表", AlterButton.Dispose.ListToString(config.get("FilteredPlayer")));
		form.sendPlayer(player);
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		return true;
	}

	/**
	 * 打开一个界面，这个界面是用来设置系统配置的
	 * 
	 * @param player
	 * @return
	 */
	protected static boolean Setting(Player player) {
		Kick kick = Kick.kick;
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player, kick.Message.getMessage("权限不足"));
		Config config = kick.config;
		CustomForm form = new CustomForm(kick.formID.getID(9), Tool.getColorFont(kick.mis.getName() + "-Setting"));
		form.addInput("快捷工具的物品ID", config.get("快捷工具"), "请输入物品ID或物品名称，如： " + config.get("快捷工具"));
		form.addInput("服务器货币名称", config.get("货币单位"), config.get("货币单位"));
		form.addToggle("插件启动时检测更新", config.getBoolean("检测更新"));
		int s = Float.valueOf(Tool.isInteger(config.get("检测更新间隔")) ? String.valueOf(config.get("检测更新间隔")) : "21600")
				.intValue();
		form.addInput("服务器自动更新间隔（单位：秒）", s, "服务器在后台多久检测更新一次，如：" + s);
		s = Float.valueOf(Tool.isInteger(config.get("屏蔽玩家双击间隔")) ? String.valueOf(config.get("屏蔽玩家双击间隔")) : "500")
				.intValue();
		form.addInput("屏蔽玩家双击时间（单位：毫秒）", s, "服务器在后台多久检测更新一次，如：" + s);
		form.addToggle("仅允许白名单管理菜单", config.getBoolean("仅允许白名单管理菜单"));
		String string = "";
		List<Object> list = config.getList("白名单");
		if (list.size() > 0)
			for (int i = 0; i < list.size(); i++)
				string += list.get(i) + (((i + 1) < list.size()) ? ";" : "");
		form.addInput("菜单管理白名单\n如将“仅允许白名单管理菜单”选项关闭则此项可以忽略\n多个玩家请使用;分割", string);
		form.addToggle("是否撤销玩家使用快捷工具打开菜单产生的事件", config.getBoolean("打开撤销"));
		form.addInput("自动检查玩家是否拥有快捷工具的时间间隔\n当这个值小于等于零时不启用该功能", config.getInt("定时检查快捷工具间隔"));
		form.addToggle("是否允许玩家丢弃快捷工具", config.getBoolean("是否允许玩家丢弃快捷工具"));
		form.addToggle("自定义工具列表显示工具图标", config.getBoolean("自定义工具列表显示工具图标"));
		form.addInput("自定义工具异步检查持有间隔", config.get("自定义工具异步检查持有间隔"));
		form.addToggle("定时检查快捷工具", config.getBoolean("定时检查快捷工具"));
		form.addToggle("自定义工具异步检查持有", config.getBoolean("自定义工具异步检查持有"));
		form.addSlider("OP命令延时撤销\n\n这个值决定了命令型按钮在使用“玩家OP权限”执行命令后多久撤销玩家OP，建议值在500到1500之间", 50, 5000, 5,
				Tool.ObjectToInt(config.get("OP命令延时撤销"), 1000));
		form.addToggle("折叠更多设置", config.getBoolean("折叠更多设置"));
		form.addToggle("快捷工具监听点击", config.getBoolean("快捷工具监听点击"));
		form.addToggle("快捷工具监听破坏", config.getBoolean("快捷工具监听破坏"));
		form.addToggle("快捷工具监听快捷栏双击", config.getBoolean("快捷工具监听快捷栏双击"));
		form.addInput("快捷工具快捷栏双击间隔", config.get("快捷工具快捷栏双击间隔"));
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 打开一个界面
	 * 
	 * @param player 要显示这个界面的玩家对象
	 * @param file   要打开的界面的文件对象
	 * @return
	 */
	public static boolean OpenMenu(Player player, File file) {
		return OpenMenu(player, file, false);
	}

	/**
	 * 打开一个界面
	 * 
	 * @param player 要显示这个界面的玩家对象
	 * @param file   要打开的界面的文件对象
	 * @param isBack 是否存储该页为上一页
	 * @return
	 */
	public static boolean OpenMenu(Player player, File file, boolean isBack) {
		return OpenMenu(player, file, isBack, false);
	}

	/**
	 * 打开一个界面
	 * 
	 * @param player 要显示这个界面的玩家对象
	 * @param file   要打开的界面的文件对象
	 * @param isBack 是否存储该页为上一页
	 * @param isMain 是否是主页
	 * @return
	 */
	public static boolean OpenMenu(Player player, File file, boolean isBack, boolean isMain) {
		Config config = new Config(file, Config.YAML);
		Object obj = config.get("FilteredPlayer");
		Kick kick = Kick.kick;
		Message msg = kick.Message;
		List<String> Players = obj != null && (obj instanceof List) ? (ArrayList<String>) obj : new ArrayList<>();
		if ((config.getInt("FilteredModel") == 1 && Players.contains(player.getName()))
				|| (config.getInt("FilteredModel") == 2 && !Players.contains(player.getName()))) {
			return MakeForm.Tip(player,
					msg.getSon("界面", "被列入黑名单", new String[] { "{Player}" }, new String[] { player.getName() }));
		}
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		if (isBack)
			myPlayer.OpenMenuList.add(file);
		List<Map<String, Object>> Items = new ArrayList<>();
		int ID = isMain ? kick.formID.getID(0) : getID(myPlayer);
		SimpleForm form = new SimpleForm(ID,
				msg.getText(config.getString("Title", ""), new String[] { "{Player}" },
						new Object[] { player.getName() }),
				msg.getText(config.getString("Content", ""), new String[] { "{Player}" },
						new Object[] { player.getName() }));
		Map<String, Object> Buttons = ((config.get("Buttons") instanceof Map) && config.get("Buttons") != null)
				? (HashMap<String, Object>) config.get("Buttons")
				: new HashMap<>();
		for (String ike : Buttons.keySet()) {
			Map<String, Object> Item = ((Buttons.get(ike) instanceof Map) && Buttons.get(ike) != null)
					? (HashMap<String, Object>) Buttons.get(ike)
					: new HashMap<>();
			if (Item.size() > 0) {
				String Path = (String) Item.get("IconPath");
				form.addButton(
						msg.getText(Item.get("Text"), new String[] { "{Player}" }, new Object[] { player.getName() }),
						!String.valueOf(Item.get("IconPath")).equals("2"),
						Path == null || Path.isEmpty() ? null : Path);
				Item.put("Key", ike);
				Items.add(Item);
			}
		}
		List<String> kis = new ArrayList<>();
		boolean isShow = true;
		if (isShow = (form.getButtonSize() < 1))
			form.setContent(form.getContent() + (form.getContent() != null && !form.getContent().isEmpty() ? "\n" : "")
					+ msg.getMessage("没有按钮时提示", new String[] { "{Player}" }, new Object[] { player.getName() }));
		if (!isMain)
			if (myPlayer.OpenMenuList == null || myPlayer.OpenMenuList.size() < 1
					|| file.getAbsolutePath()
							.equals(new File(kick.mis.getDataFolder(), kick.MainFileName).getAbsolutePath())
					|| (myPlayer.BackFile != null
							&& file.getAbsolutePath().equals(myPlayer.BackFile.getAbsolutePath()))) {
				form.addButton(
						msg.getSon("界面", "取消按钮", new String[] { "{Player}" }, new Object[] { player.getName() }));
				kis.add("quit");
			} else {
				form.addButton(
						msg.getSon("界面", "返回上级", new String[] { "{Player}" }, new Object[] { player.getName() }));
				kis.add("back");
			}
		if (Kick.isAdmin(player)) {
			if (!kick.config.getBoolean("折叠更多设置")) {
				kis.add("add");
				form.addButton(Tool.getRandColor() + "添加按钮");
				if (!isShow) {
					kis.add("del");
					form.addButton(Tool.getRandColor() + "删除按钮");
					kis.add("alter");
					form.addButton(Tool.getRandColor() + "修改按钮");
				}
				kis.add("set");
				form.addButton(Tool.getRandColor() + "系统设置");
				kis.add("sf");
				form.addButton(Tool.getRandColor() + "修改界面");
			} else {
				kis.add("ss");
				form.addButton(Tool.getRandColor() + "更多设置");
			}
		}
		myPlayer.BackFile = file;
		myPlayer.Items = Items;
		myPlayer.isMain = isMain;
		myPlayer.UIAdminButtonKis = kis;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 创建菜单主页
	 * 
	 * @param player
	 */
	public static boolean Main(Player player) {
		Kick kick = Kick.kick;
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer = myPlayer == null ? new MyPlayer(player) : myPlayer;
		if (myPlayer.loadTime == null
				|| Duration.between(myPlayer.loadTime, Instant.now()).toMillis() > kick.config.getInt("屏蔽玩家双击间隔"))
			myPlayer.loadTime = Instant.now();
		else
			return false;
		myPlayer.OpenMenuList = new ArrayList<>();
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		return OpenMenu(player, new File(kick.mis.getDataFolder(), kick.MainFileName), true, true);
	}

	public static int getID(MyPlayer myPlayer) {
		int i = myPlayer.Formid;
		if (i >= 0 && i <= 3) {
			i++;
		} else
			i = 0;
		myPlayer.Formid = i;
		Kick.kick.PlayerDataMap.put(myPlayer.player.getName(), myPlayer);
		return Kick.kick.formID.getID("子页" + i);
	}

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  要显示弹窗的玩家对象
	 * @param Content 弹窗的内容
	 * @return <b>back</b>
	 */
	public static boolean Tip(Player player, String Content) {
		return Tip(player, Tool.getRandColor() + Kick.kick.mis.getName(), Content, false);
	}

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  要显示弹窗的玩家对象
	 * @param Content 弹窗的内容
	 * @param back    返回的布尔值
	 * @return <b>back</b>
	 */
	public static boolean Tip(Player player, String Content, boolean back) {
		return Tip(player, Tool.getRandColor() + Kick.kick.mis.getName(), Content, back);
	}

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  要显示弹窗的玩家对象
	 * @param Title   弹窗的标题
	 * @param Content 弹窗的内容
	 * @param back    返回的布尔值
	 * @return <b>back</b>
	 */
	public static boolean Tip(Player player, String Title, String Content, boolean back) {
		return Tip(player, Title, Content, back, true);
	}

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  要显示弹窗的玩家对象
	 * @param Title   弹窗的标题
	 * @param Content 弹窗的内容
	 * @param back    返回的布尔值
	 * @param Modal   是否是Modal型弹窗
	 * @return <b>back</b>
	 */
	public static boolean Tip(Player player, String Title, String Content, boolean back, boolean Modal) {
		return Tip(player, Title, Content, "确定", "取消", back, Modal);
	}

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  要显示弹窗的玩家对象
	 * @param Title   弹窗的标题
	 * @param Content 弹窗的内容
	 * @param Button1 弹窗的第一个按钮文本内容
	 * @param Button2 弹窗的第二个按钮文本内容
	 * @param back    返回的布尔值
	 * @param Modal   是否是Modal型弹窗
	 * @return <b>back</b>
	 */
	public static boolean Tip(Player player, String Title, String Content, String Button1, String Button2, boolean back,
			boolean Modal) {
		if (Modal) {
			ModalForm form = new ModalForm(Tool.getRand(), Title, Button1, Button2);
			form.setContent(Content);
			form.sendPlayer(player);
		} else {
			SimpleForm form = new SimpleForm(Tool.getRand(), Title, Content);
			form.addButton(Button1).addButton(Button2);
			form.sendPlayer(player);
		}
		return back;
	}
}
