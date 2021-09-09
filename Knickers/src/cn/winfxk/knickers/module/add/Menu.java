package cn.winfxk.knickers.module.add;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.Knickers;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.BaseMake;
import cn.winfxk.knickers.tool.Config;
import cn.winfxk.knickers.tool.Tool;

/**
 * @Createdate 2021/08/07 21:49:14
 * @author Winfxk
 */
public class Menu extends BaseMake implements FileFilter {
	private static final File MenuDir = new File(Knickers.kis.getDataFolder(), Knickers.Menus);
	private List<File> MenuFiles = new ArrayList<>();
	private List<String> list = new ArrayList<>();
	private Config c;

	public Menu(Player player, File file, FormBase upForm, String Key) {
		super(player, file, upForm, Key);
		c = new Config(new File(MenuDir, Tool.objToString(map.get("Config"))));
	}

	public Menu(Player player, File file, FormBase upForm) {
		super(player, file, upForm);
	}

	@Override
	public boolean MakeMain() {
		if (!super.MakeMain())
			return false;
		(list = (list == null ? new ArrayList<>() : list)).clear();
		(MenuFiles = (MenuFiles == null ? new ArrayList<>() : MenuFiles)).clear();
		form.addInput(getString("InputTitle"), Key == null ? msg.config.get("Tip") : c.get("Title"), getString("InputTitle"));
		form.addInput(getString("InputContent"), Key == null ? "" : c.get("Content"), getString("InputContent"));
		form.addDropdown(getString("SelectMenuConfig"), getMenus(), list.indexOf(map.get("Config")));
		form.addInput(getString("InputMenuConfig"), "", getString("InputMenuConfig"));
		form.addToggle(getString("InjectionRules"));
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		if (!super.disMain(data))
			return false;
		String Title = d.getInputResponse(location + 1);
		String Content = d.getInputResponse(location + 2);
		File MenuConfig = MenuFiles.get(d.getDropdownResponse(location + 3).getElementID());
		String MenuName = d.getInputResponse(location + 4);
		if (MenuName != null && !MenuName.isEmpty())
			MenuConfig = new File(MenuDir, MenuName);
		boolean InjectionRules = d.getToggleResponse(location + 5);
		map.put("Config", MenuConfig.getName());
		save();
		if (!MenuConfig.exists() || InjectionRules) {
			Config c = new Config(MenuConfig);
			c.set("Title", Title);
			c.set("Content", Content);
			c.set(!MenuConfig.exists() ? "creaPlayer" : "alterPlayer", player.getName());
			c.set(!MenuConfig.exists() ? "creaTime" : "alterTime", Tool.getDate() + " " + Tool.getTime());
			if (InjectionRules) {
				c.set("FilteredModel", PlayerFilteredModel);
				c.set("FilteredPlayer", PlayerFiltered);
				c.set("LevelFilteredModel", LevelFilteredModel);
				c.set("LevelList", WorldFiltered);
				c.set("Permission", Permission);
			}
			c.set("Buttons", new HashMap<>());
			c.save();
		}
		sendMessage(getString("Succeed"));
		return isBack();
	}

	/**
	 * 返回一个现有的菜单配置文件夹
	 * 
	 * @return
	 */
	protected List<String> getMenus() {
		list = new ArrayList<>();
		File[] Files = MenuDir.listFiles(this);
		for (File f : Files) {
			MenuFiles.add(f);
			list.add(file.getName());
		}
		if (list.size() <= 0) {
			MenuFiles.add(new File(MenuDir, "Main.yml"));
			list.add("Main.yml");
		}
		return list;
	}

	/**
	 * 确保返回的都是文件而没有文件夹
	 */
	@Override
	public boolean accept(File pathname) {
		return pathname.isFile();
	}
}
