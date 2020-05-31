package cn.winfxk.knickers.module.menu;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.MakeBase;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.money.MyEconomy;
import cn.winfxk.knickers.tool.CustomForm;
import cn.winfxk.knickers.tool.Tool;

/**
 * 创建菜单按钮时调用
 * 
 * @Createdate 2020/05/23 13:00:53
 * @author Winfxk
 */
public class MakeMenu extends MakeBase {
	protected File file;
	protected List<File> list;
	protected int Count;
	protected List<String> allFileNames = new ArrayList<>();

	public MakeMenu(Player player, FormBase upForm, Config config, FunctionBase base) {
		super(player, upForm, config, base);
		file = ac.getFunctionMag().getFile();
		Config c;
		String[] Ks = { "{Player}", "{Money}", "{FileName}", "{MenuName}" };
		for (File f : file.listFiles((a, b) -> new File(a, b).isFile()))
			try {
				c = new Config(f, Config.YAML);
				listKey.add(getString("MenuItem", Ks,
						new Object[] { player.getName(), myPlayer.getMoney(), f.getName(), c.get("MenuName") }));
				list.add(f);
				Count++;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (f != null)
					allFileNames.add(f.getName());
			}
		listKey.add(getString("newMenu"));
	}

	@Override
	public boolean MakeMain() {
		CustomForm form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		form.addInput(getButtonText(), "", getButtonText());
		form.addInput(getString("MenuContent"), "", getString("MenuContent"));
		form.addDropdown(getString("SelectMenu"), listKey, listKey.size() - 1);
		form.addInput(getClickCommand(), "", getClickCommand());
		form.addDropdown(getPlayerBlacklistMode(), getModeList());
		form.addInput(getInputBlacklistPlayer(), "", getInputBlacklistPlayer());
		form.addDropdown(getWorldBlacklistMode(), getModeList());
		form.addInput(getInputBlacklistWorld(), "", getInputBlacklistWorld());
		form.addInput(getMoney(), "", getMoney());
		form.addDropdown(getMoneyEconomy(), ac.getEconomyManage().getEconomy());
		form.addDropdown(getPermission(), getPermissions());
		form.addInput(getString("InputTitle"), "", getString("InputTitle"));
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		FormResponseCustom d = getCustom(data);
		String ButtonText = d.getInputResponse(1);
		if (ButtonText == null || ButtonText.isEmpty()) {
			sendMessage(getNotInputButtonText());
			return MakeMain();
		}
		String MenuContent = d.getInputResponse(2);
		if (MenuContent == null || MenuContent.isEmpty()) {
			sendMessage(getString("NotInputMenuContent"));
			return MakeMain();
		}
		int i = d.getDropdownResponse(3).getElementID();
		String Menu = i > Count ? getMenuFileName(1) : list.get(i).getName();
		List<String> Command = getCommand(d.getInputResponse(4));
		String PlayerBlacklistMode = getFiltertype(d.getDropdownResponse(5).getElementID());
		List<String> PlayerBlacklist = getList(d.getInputResponse(6));
		String WorldBlacklistMode = getFiltertype(d.getDropdownResponse(7).getElementID());
		List<String> WorldBlacklist = getList(d.getInputResponse(8));
		String MoneyString = d.getInputResponse(9);
		if (MoneyString != null && !MoneyString.isEmpty() && !Tool.isInteger(MoneyString)) {
			sendMessage(getMoneyillegal());
			return MakeMain();
		}
		double Money = Tool.objToDouble(MoneyString);
		Money = Money <= 0 ? 0 : Money;
		MyEconomy economy = ac.getEconomyManage().getEconomy(d.getDropdownResponse(10).getElementContent());
		String Permission = getPermissionsType(d.getDropdownResponse(11).getElementID());
		String MenuTitle = d.getInputResponse(12);
		Map<String, Object> map = new HashMap<>();
		map.put("Menu", Menu);
		File file = new File(this.file, Menu);
		if (!file.exists()) {
			Config config = new Config(file, Config.YAML);
			config.set("Player", player.getName());
			config.set("Date", Tool.getDate() + " " + Tool.getTime());
			config.set("Content", MenuContent);
			config.set("Name", MenuTitle == null || MenuTitle.isEmpty() ? ButtonText : MenuTitle);
			config.set("Playerfilter", PlayerBlacklistMode);
			config.set("Playerfilterlist", PlayerBlacklist);
			config.set("Worldfilter", WorldBlacklistMode);
			config.set("Worldfilterlist", WorldBlacklist);
			config.set("Permission", Permission);
			config.set("Buttons", new HashMap<>());
			config.save();
		}
		if (!file.exists()) {
			sendMessage(getString("Createfailure"));
			return MakeMain();
		} else
			sendMessage(getString(Key == null ? "CreateOK" : "AlterOK"));
		boolean isOK = save(map, ButtonText, Command, PlayerBlacklistMode, PlayerBlacklist, WorldBlacklistMode,
				WorldBlacklist, Money, economy, Permission, Key);
		return isOK && isBack();
	}

	/**
	 * 返回一个不重复的菜单文件
	 * 
	 * @param JJLength
	 * @return
	 */
	protected String getMenuFileName(int JJLength) {
		String string = "";
		for (int i = 0; i < JJLength; i++)
			string += Tool.getRandString("0123456789abcdefghijklmnopqrstuvwxyz_");
		string += ".yml";
		return allFileNames.contains(string) ? getMenuFileName(JJLength + 1) : string;
	}
}
