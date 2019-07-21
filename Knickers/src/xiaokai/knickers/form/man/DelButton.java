package xiaokai.knickers.form.man;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.utils.Config;
import xiaokai.knickers.form.MakeForm;
import xiaokai.knickers.mtp.Kick;
import xiaokai.knickers.mtp.MyPlayer;
import xiaokai.tool.SimpleForm;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class DelButton {
	public static boolean delButton(Player player, File file) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player, Kick.kick.Message.getMessage("权限不足"));
		Kick kick = Kick.kick;
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.CacheFile = file;
		List<String> Items = new ArrayList<String>();
		Config config = new Config(file, Config.YAML);
		Map<String, Object> Buttons = (config.get("Buttons") != null && (config.get("Buttons") instanceof Map))
				? (HashMap<String, Object>) config.get("Buttons")
				: new HashMap<String, Object>();
		if (Buttons.size() < 1)
			return MakeForm.Tip(player, "§4这个界面没有发现按钮的存在！");
		SimpleForm form = new SimpleForm(kick.formID.getID(3), Kick.kick.Message.getText(config.getString("Title")),
				"§4请点击您想要删除的按钮");
		for (String ike : Buttons.keySet()) {
			Map<String, Object> Item = ((Buttons.get(ike) instanceof Map) && Buttons.get(ike) != null)
					? (HashMap<String, Object>) Buttons.get(ike)
					: new HashMap<String, Object>();
			if (Item.size() > 0) {
				String Path = (String) Item.get("IconPath");
				form.addButton(String.valueOf(Item.get("Text")), !String.valueOf(Item.get("IconPath")).equals("2"),
						Path == null || Path.isEmpty() ? null : Path);
				Items.add(ike);
			}
		}
		myPlayer.keyList = Items;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	public static boolean Del(Player player, FormResponseSimple data) {
		Kick kick = Kick.kick;
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		File file = myPlayer.CacheFile;
		List<String> Items = myPlayer.keyList;
		Config config = new Config(file, Config.YAML);
		Map<String, Object> Buttons = (config.get("Buttons") != null && (config.get("Buttons") instanceof Map))
				? (HashMap<String, Object>) config.get("Buttons")
				: new HashMap<String, Object>();
		if (Buttons.size() < 1)
			return MakeForm.Tip(player, "§4这个界面没有发现按钮的存在！");
		if (Items.size() <= data.getClickedButtonId())
			return MakeForm.OpenMenu(player, file);
		String key = Items.get(data.getClickedButtonId());
		if (!Buttons.containsKey(key))
			return MakeForm.Tip(player, "§4这个界面没有发现这个按钮的存在！");
		Map<String, Object> map = (Map<String, Object>) Buttons.get(key);
		SimpleForm form = new SimpleForm(kick.formID.getID(4),
				Kick.kick.Message.getText(String.valueOf(map.get("Text"))), "§4确定要删除这个按钮吗？");
		String string = "";
		for (String ike : map.keySet()) {
			String s = Tool.getRandColor();
			string += s + ike + "§f: " + s + map.get(ike) + "\n";
		}
		form.setContent(form.getContent() + "\n" + string);
		form.addButton("§6确定");
		form.addButton("§e取消");
		if (String.valueOf(map.get("Type")).toLowerCase().equals("open"))
			form.addButton("§4确定并删除子菜单");
		form.sendPlayer(player);
		return true;
	}

	public static boolean start(Player player, FormResponseSimple data) {
		int id = data.getClickedButtonId();
		Kick kick = Kick.kick;
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		File file = myPlayer.CacheFile;
		List<String> Items = myPlayer.keyList;
		String Key = Items.get(id);
		Config config = new Config(file, Config.YAML);
		Map<String, Object> Buttons = (config.get("Buttons") != null && (config.get("Buttons") instanceof Map))
				? (HashMap<String, Object>) config.get("Buttons")
				: new HashMap<String, Object>();
		if (id == 3) {
			Map<String, Object> map = (Map<String, Object>) Buttons.get(Key);
			String Type = String.valueOf(map.get("Type"));
			if (Type.toLowerCase().equals("open"))
				delFile(new File(new File(kick.mis.getDataFolder(), Kick.MenuConfigPath),
						String.valueOf(map.get("Config"))));
			Buttons.remove(Key);
		} else if (id == 0)
			Buttons.remove(Key);
		config.set("Buttons", Buttons);
		config.save();
		player.sendMessage("§6删除成功！");
		myPlayer.CacheFile = null;
		myPlayer.keyList = null;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		return MakeForm.OpenMenu(player, file);
	}

	public static void delFile(File file) {
		if (!file.exists())
			return;
		Config config = new Config(file, Config.YAML);
		Map<String, Object> Buttons = (config.get("Buttons") != null && (config.get("Buttons") instanceof Map))
				? (HashMap<String, Object>) config.get("Buttons")
				: new HashMap<String, Object>();
		if (Buttons.size() > 0)
			for (String ike : Buttons.keySet()) {
				Map<String, Object> map = (Map<String, Object>) Buttons.get(ike);
				String Type = String.valueOf(map.get("Type"));
				if (Type.toLowerCase().equals("open"))
					delFile(new File(new File(Kick.kick.mis.getDataFolder(), Kick.MenuConfigPath),
							String.valueOf(map.get("Config"))));
			}
		file.delete();
	}
}
