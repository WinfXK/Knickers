package cn.winfxk.knickers.module.tip;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
	private String TipTitle, TipContent, TipButton1, TipButton2, TipType;
	private List<String> Command1, Command2;

	public TipData(Config config, String Key) {
		this(config, FunctionBase.getButtonMap(config, Key));
	}

	public TipData(File file, Map<String, Object> map) {
		this(new Config(file, Config.YAML), map);
	}

	public TipData(Config config, Map<String, Object> map) {
		super(config, map);
		TipTitle = Tool.objToString(map.get("Title"));
		TipContent = Tool.objToString(map.get("Content"));
		TipButton2 = Tool.objToString(map.get("Button1"));
		TipButton1 = Tool.objToString(map.get("Button2"));
		TipType = Tool.objToString(map.get("TipType"));
		Object obj = map.get("Command1");
		Command1 = obj == null || !(obj instanceof List) ? new ArrayList<>() : (ArrayList<String>) obj;
		obj = map.get("Command2");
		Command2 = obj == null || !(obj instanceof List) ? new ArrayList<>() : (ArrayList<String>) obj;
	}

	public TipData(File file, String Key) {
		this(new Config(file, Config.YAML), Key);
	}

	/**
	 * 返回按钮1的文本
	 * 
	 * @return
	 */
	public List<String> getCommand1() {
		return Command1;
	}

	/**
	 * 返回按钮2的文本
	 * 
	 * @return
	 */
	public List<String> getCommand2() {
		return Command2;
	}

	/**
	 * 返回窗口类型
	 * 
	 * @return
	 */
	public String getTipType() {
		return TipType;
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
