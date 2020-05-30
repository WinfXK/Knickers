package cn.winfxk.knickers.module;

import java.io.File;
import java.util.Map;

import cn.nukkit.utils.Config;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.MyMap;
import cn.winfxk.knickers.module.cmd.CommandButton;
import cn.winfxk.knickers.module.menu.MenuButton;
import cn.winfxk.knickers.module.say.SayButton;
import cn.winfxk.knickers.module.tp.TransferButton;

/**
 * 管理菜单拥有的功能的管理器
 * 
 * @Createdate 2020/05/22 19:26:43
 * @author Winfxk
 */
public class FunctionMag {
	private final MyMap<String, FunctionBase> Function = new MyMap<>();
	private Config config;
	private static FunctionMag mag;
	private File file;

	public FunctionMag(Activate activate) {
		config = activate.getFunctionConfig();
		file = new File(activate.getPluginBase().getDataFolder(), Activate.MenuDataDirName);
		mag = this;
		FunctionBase[] bases = { new MenuButton(activate), new CommandButton(activate), new SayButton(activate),
				new TransferButton(activate) };
		for (FunctionBase base : bases)
			Function.put(base.getModuleKey(), base);
		loadConfig();
	}

	/**
	 * 获取菜单配置 文件存储位置
	 * 
	 * @return
	 */
	public File getFile() {
		return file;
	}

	/**
	 * 返回对外接口
	 * 
	 * @return
	 */
	public static FunctionMag getMag() {
		return mag;
	}

	/**
	 * 加载功能启用关闭列表
	 */
	private void loadConfig() {
		Map<String, Object> map = config.getAll();
		for (FunctionBase base : Function.values())
			if (!map.containsKey(base.getModuleKey()))
				config.set(base.getModuleKey(), true);
		config.save();
	}

	/**
	 * 删除一个已经支持的功能
	 * 
	 * @param FunctionKey
	 * @return
	 */
	public boolean removeFunction(FunctionBase function) {
		if (Function.containsValue(function))
			Function.removeValues(function);
		return !Function.containsKey(function.getModuleKey()) && !Function.containsValue(function);
	}

	/**
	 * 删除一个已经支持的功能
	 * 
	 * @param FunctionKey 功能的Key
	 * @return
	 */
	public boolean removeFunction(String FunctionKey) {
		if (!Function.containsKey(FunctionKey))
			return false;
		Function.get(FunctionKey).setEnable(false);
		Function.remove(FunctionKey);
		return !Function.containsKey(FunctionKey);
	}

	/**
	 * 添加一个功能
	 * 
	 * @param function
	 */
	public void addFunction(FunctionBase function) {
		Function.put(function.getModuleKey(), function);
		loadConfig();
	}

	/**
	 * 返回一个功能
	 * 
	 * @param Key
	 * @return
	 */
	public FunctionBase getFunction(String Key) {
		if (Function.containsKey(Key))
			return Function.get(Key);
		return null;
	}

	/**
	 * 返回功能的列表
	 * 
	 * @return
	 */
	public Map<String, FunctionBase> getFunction() {
		return Function;
	}
}
