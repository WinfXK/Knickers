package xiaokai.knickers.form;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import xiaokai.knickers.mtp.Kick;
import xiaokai.knickers.mtp.Message;
import xiaokai.knickers.mtp.MyPlayer;
import xiaokai.tool.ModalForm;
import xiaokai.tool.SimpleForm;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class MakeForm {
	public static boolean Setting(Player player) {
		return true;
	}

	public static boolean OpenMenu(Player player, File file) {
		return OpenMenu(player, file, false);
	}

	public static boolean OpenMenu(Player player, File file, boolean isBack) {
		Kick kick = Kick.kick;
		Message msg = kick.Message;
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		
		return true;
	}

	/**
	 * 创建菜单主页
	 * 
	 * @param player
	 */
	public static boolean Main(Player player) {
		Kick kick = Kick.kick;
		Message msg = kick.Message;
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		if (myPlayer.loadTime == null
				|| Duration.between(myPlayer.loadTime, Instant.now()).toMillis() > kick.config.getInt("屏蔽玩家双击间隔"))
			myPlayer.loadTime = Instant.now();
		else
			return false;
		File file = new File(kick.mis.getDataFolder(), kick.MainFileName);
		myPlayer.OpenMenuList = Arrays.asList(file);
		myPlayer.BackFile = file;
		Config config = new Config(file, Config.YAML);
		List<Map<String, Object>> Items = new ArrayList<Map<String, Object>>();
		SimpleForm form = new SimpleForm(kick.formID.getID(0),
				msg.getText(config.getString("Title", ""), new String[] { "{Player}" },
						new Object[] { player.getName() }),
				msg.getText(config.getString("Content", ""), new String[] { "{Player}" },
						new Object[] { player.getName() }));
		Map<String, Object> Buttons = ((config.get("Buttons") instanceof Map) && config.get("Buttons") != null)
				? (HashMap<String, Object>) config.get("Buttons")
				: new HashMap<String, Object>();
		for (String ike : Buttons.keySet()) {
			Map<String, Object> Item = ((Buttons.get(ike) instanceof Map) && Buttons.get(ike) != null)
					? (HashMap<String, Object>) Buttons.get(ike)
					: new HashMap<String, Object>();
			if (Item.size() > 0) {
				String Path = (String) Item.get("IconPath");
				form.addButton(String.valueOf(Item.get("Text")), !String.valueOf(Item.get("IconPath")).equals("2"),
						Path == null || Path.isEmpty() ? null : Path);
				Item.put("Key", ike);
				Items.add(Item);
			}
		}
		if (form.getButtonSize() < 1)
			form.setContent(form.getContent() + (form.getContent() != null && !form.getContent().isEmpty() ? "\n" : "")
					+ msg.getMessage("没有按钮时提示", new String[] { "{Player}" }, new Object[] { player.getName() }));
		if (Kick.isAdmin(player))
			form.addButton(Tool.getRandColor() + "添加按钮").addButton(Tool.getRandColor() + "删除按钮")
					.addButton(Tool.getRandColor() + "系统设置");
		myPlayer.Items = Items;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
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
		return Tip(player, Title, Content, back, false);
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
