package cn.winfxk.knickers.module.tip;

import java.io.File;
import java.util.Map;

import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.module.ModuleData;
import cn.winfxk.knickers.tool.Tool;

/**
 * Tip按钮的数据
 * 
 * @Createdate 2020/05/31 23:03:02
 * @author Winfxk
 */
public class TipData extends ModuleData {
	private String TipTitle, TipContent, TipButton1, TipButton2;

	public TipData(Config config, String Key) {
		this(config, FunctionBase.getButtonMap(config, Key));
	}

	public TipData(File file, Map<String, Object> map) {
		this(new Config(file, Config.YAML), map);
	}

	public TipData(Config config, Map<String, Object> map) {
		super(config, map);
		TipTitle = Tool.objToString(map.get("TipTitle"));
		TipContent = Tool.objToString(map.get("TipContent"));
		TipButton2 = Tool.objToString(map.get("TipButton2"));
		TipButton1 = Tool.objToString(map.get("TipButton1"));

	}

	public TipData(File file, String Key) {
		this(new Config(file, Config.YAML), Key);
	}

	/**
	 * 将ModuleData转换为TipData
	 * 
	 * @param data
	 * @return
	 */
	public static TipData geTipData(ModuleData data) {
		return data instanceof TipData ? (TipData) data : new TipData(data.getConfig(), data.getMap());
	}

	/**
	 * 返回UI的按钮一的文本
	 * 
	 * @return
	 */
	public String getButton1() {
		return TipButton1;
	}

	/**
	 * 返回UI的按钮二的文本
	 * 
	 * @return
	 */
	public String getButton2() {
		return TipButton2;
	}

	/**
	 * 返回UI的界面内容
	 * 
	 * @return
	 */
	public String getContent() {
		return TipContent;
	}

	/**
	 * 返回UI的标题
	 * 
	 * @return
	 */
	public String getTitle() {
		return TipTitle;
	}
}
