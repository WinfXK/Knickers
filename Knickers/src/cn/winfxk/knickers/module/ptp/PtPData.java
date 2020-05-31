package cn.winfxk.knickers.module.ptp;

import java.io.File;
import java.util.Map;

import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.module.ModuleData;
import cn.winfxk.knickers.tool.Tool;

/**
 * 点击后传送万家到服务器在线玩家的按钮数据
 * 
 * @Createdate 2020/05/31 20:32:52
 * @author Winfxk
 */
public class PtPData extends ModuleData {
	private String Accept, Refuse, SolicitedTitle, SolicitedMessage, ListTitle, ListContent, PlayerItem;
	private boolean ForceTransfer;

	public PtPData(Config config, String Key) {
		this(config, FunctionBase.getButtonMap(config, Key));
	}

	public PtPData(File file, Map<String, Object> map) {
		this(new Config(file, Config.YAML), map);
	}

	public PtPData(Config config, Map<String, Object> map) {
		super(config, map);
		Accept = Tool.objToString(map.get("Accept"));
		Refuse = Tool.objToString(map.get("Refuse"));
		SolicitedTitle = Tool.objToString(map.get("SolicitedTitle"));
		SolicitedMessage = Tool.objToString(map.get("SolicitedMessage"));
		ForceTransfer = Tool.ObjToBool(map.get("ForceTransfer"));
		ListTitle = Tool.objToString(map.get("ListTitle"));
		ListContent = Tool.objToString(map.get("ListContent"));
		PlayerItem = Tool.objToString(map.get("PlayerItem"));
	}

	/**
	 * 将ModuleData数据强转为PtPData数据
	 * 
	 * @param data
	 * @return
	 */
	public static PtPData getData(ModuleData data) {
		return data instanceof PtPData ? (PtPData) data : new PtPData(data.getConfig(), data.getMap());
	}

	public PtPData(File file, String Key) {
		this(new Config(file, Config.YAML), Key);
	}

	/**
	 * 获取同意按钮的文本
	 * 
	 * @return
	 */
	public String getAccept() {
		return Accept;
	}

	/**
	 * 获取拒绝按钮的文本
	 * 
	 * @return
	 */
	public String getRefuse() {
		return Refuse;
	}

	/**
	 * 获取请求内容的文本
	 * 
	 * @return
	 */
	public String getSolicitedMessage() {
		return SolicitedMessage;
	}

	/**
	 * 获取请求的标题
	 * 
	 * @return
	 */
	public String getSolicitedTitle() {
		return SolicitedTitle;
	}

	/**
	 * 返回玩家列表项目内容
	 * 
	 * @return
	 */
	public String getPlayerItem() {
		return PlayerItem;
	}

	/**
	 * 返回玩家列表的内容
	 * 
	 * @return
	 */
	public String getListContent() {
		return ListContent;
	}

	/**
	 * 返回玩家列表的标题
	 * 
	 * @return
	 */
	public String getListTitle() {
		return ListTitle;
	}

	/**
	 * 是否强制传送
	 * 
	 * @return
	 */
	public boolean isForceTransfer() {
		return ForceTransfer;
	}
}
