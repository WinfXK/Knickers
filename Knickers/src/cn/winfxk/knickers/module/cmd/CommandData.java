package cn.winfxk.knickers.module.cmd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.module.ModuleData;
import cn.winfxk.knickers.tool.Tool;

/**
 * Command按钮的数据
 * 
 * @Createdate 2020/06/02 19:54:17
 * @author Winfxk
 */
public class CommandData extends ModuleData {
	private List<String> Tip, Hint;
	private String ClickCommand, Title, Content;
	private boolean AllowBlank;

	public CommandData(Config config, String Key) {
		this(config, FunctionBase.getButtonMap(config, Key));
	}

	public CommandData(File file, Map<String, Object> map) {
		this(new Config(file, Config.YAML), map);
	}

	public CommandData(Config config, Map<String, Object> map) {
		super(config, map);
		Object obj = map.get("Hint");
		Hint = obj != null && obj instanceof List ? (ArrayList<String>) obj : new ArrayList<>();
		obj = map.get("Tip");
		Tip = obj != null && obj instanceof List ? (ArrayList<String>) obj : new ArrayList<>();
		ClickCommand = Tool.objToString(map.get("ClickCommand"));
		Title = Tool.objToString(map.get("VariableTitle"));
		Content = Tool.objToString(map.get("VariableContent"));
		AllowBlank = Tool.ObjToBool(map.get("AllowBlank"));
	}

	/**
	 * 判断是否允许变量留空
	 * 
	 * @return
	 */
	public boolean isAllowBlank() {
		return AllowBlank;
	}

	public CommandData(File file, String Key) {
		this(new Config(file, Config.YAML), Key);
	}

	/**
	 * 返回变量页标题
	 * 
	 * @return
	 */
	public String getTitle() {
		return Title;
	}

	/**
	 * 返回变量页内容
	 * 
	 * @return
	 */
	public String getContent() {
		return Content;
	}

	/**
	 * 返回点击将会调用的按钮
	 * 
	 * @return
	 */
	public String getClickCommand() {
		return ClickCommand;
	}

	/**
	 * 此方法已弃用
	 * 
	 * @return
	 */
	@Deprecated
	@Override
	public List<String> getCommand() {
		return super.getCommand();
	}

	/**
	 * 将ModuleData数据转换为CommandData数据
	 * 
	 * @param data
	 * @return
	 */
	public static CommandData getCommandData(ModuleData data) {
		return data instanceof CommandData ? (CommandData) data : new CommandData(data.getConfig(), data.getMap());
	}

	/**
	 * 若按钮包括变量则返回变量提示文本列表
	 * 
	 * @return
	 */
	public List<String> getTip() {
		return Tip;
	}

	/**
	 * 若按钮包括变量则返回变量Hint文本列表
	 * 
	 * @return
	 */
	public List<String> getHint() {
		return Hint;
	}
}
