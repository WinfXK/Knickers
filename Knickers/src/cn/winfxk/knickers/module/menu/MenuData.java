package cn.winfxk.knickers.module.menu;

import java.io.File;
import java.util.Map;

import cn.nukkit.utils.Config;
import cn.winfxk.knickers.module.ModuleData;
import cn.winfxk.knickers.tool.Tool;

/**
 * 菜单按钮的按钮数据
 * 
 * @Createdate 2020/05/26 19:04:27
 * @author Winfxk
 */
public class MenuData extends ModuleData {
	private File file;
	private Config config;
	private String MenuFileName;

	public MenuData(Map<String, Object> map) {
		super(map);
		MenuFileName = Tool.objToString(map.get("Menu"));
		file = new File(functionMag.getFile(), getMenufilename());
		config = new Config(MenuFileName, Config.YAML);
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
		return file;
	}

	/**
	 * 返回这个按钮点击后会打开的菜单的配置文件对象
	 * 
	 * @return
	 */
	public Config getMenuconfig() {
		return config;
	}
}
