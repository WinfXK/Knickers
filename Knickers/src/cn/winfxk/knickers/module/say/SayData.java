package cn.winfxk.knickers.module.say;

import java.io.File;
import java.util.Map;

import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.module.ModuleData;
import cn.winfxk.knickers.tool.Tool;

/**
 * @Createdate 2020/05/30 19:10:24
 * @author Winfxk
 */
public class SayData extends ModuleData {
	private String Say;

	public SayData(Config config, String Key) {
		this(config, FunctionBase.getButtonMap(config, Key));
	}

	public SayData(File file, Map<String, Object> map) {
		this(new Config(file, Config.YAML), map);
	}

	public SayData(Config config, Map<String, Object> map) {
		super(config, map);
		Say = Tool.objToString(map.get("Say"));
	}

	public SayData(File file, String Key) {
		this(new Config(file, Config.YAML), Key);
	}

	/**
	 * 将ModuleData转换为SayData
	 * 
	 * @param data
	 * @return
	 */
	public static SayData getSayData(ModuleData data) {
		return new SayData(data.getConfig(), data.getMap());
	}

	/**
	 * 返回玩家想要说的话
	 * 
	 * @return
	 */
	public String getSay(FormBase form) {
		return msg.getText(getSay(), form);
	}

	/**
	 * 返回玩家想要说的话
	 * 
	 * @return
	 */
	public String getSay() {
		return Say;
	}
}
