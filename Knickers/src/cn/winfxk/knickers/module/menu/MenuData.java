package cn.winfxk.knickers.module.menu;

import java.io.File;
import java.util.Map;

import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.module.ModuleData;
import cn.winfxk.knickers.tool.Tool;

/**
 * 菜单按钮的按钮数据
 * 
 * @Createdate 2020/05/26 19:04:27
 * @author Winfxk
 */
public class MenuData extends ModuleData {
	private File Menufile;
	private Config Menuconfig;
	private String MenuFileName, MenuContent, MenuTitle;

	public MenuData(Config config, String Key) {
		this(config, FunctionBase.getButtonMap(config, Key));
	}

	public MenuData(File file, Map<String, Object> map) {
		this(new Config(file, Config.YAML), map);
	}

	public MenuData(Config config, Map<String, Object> map) {
		super(config, map);
		MenuFileName = Tool.objToString(map.get("Menu"));
		Menufile = new File(functionMag.getFile(), getMenufilename());
		Menuconfig = new Config(Menufile, Config.YAML);
		MenuContent = Menuconfig.getString("Content");
		MenuTitle = Menuconfig.getString("Name");
	}

	public MenuData(File file, String Key) {
		this(new Config(file, Config.YAML), Key);
	}

	/**
	 * 尝试将ModuleData转换为MenuData
	 * 
	 * @param data ModuleData数据
	 * @return
	 */
	public static MenuData getMenuData(ModuleData data) {
		return new MenuData(data.getConfig(), data.getKey());
	}

	/**
	 * 返回按钮会打开的菜单的标题
	 * 
	 * @return
	 */
	public String getMenuTitle() {
		return MenuTitle;
	}

	/**
	 * 返回点击后会打开的菜单的文件名称
	 * 
	 * @return
	 */
	public String getMenufilename() {
		return MenuFileName;
	}

	/**
	 * 返回这个按钮点击后会打开的菜单的文件对象
	 * 
	 * @return
	 */
	public File getMenufile() {
		return Menufile;
	}

	/**
	 * 返回这个按钮点击后会打开的菜单的配置文件对象
	 * 
	 * @return
	 */
	public Config getMenuconfig() {
		return Menuconfig;
	}

	/**
	 * 返回菜单的内容
	 * 
	 * @return
	 */
	public String getMenuContent() {
		return MenuContent;
	}
}
