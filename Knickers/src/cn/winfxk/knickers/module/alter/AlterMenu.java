package cn.winfxk.knickers.module.alter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.base.CustomForm;
import cn.winfxk.knickers.tool.Config;
import cn.winfxk.knickers.tool.Tool;

/**
 * 修改当前界面时现实的界面
 * 
 * @Createdate 2021/08/01 14:55:08
 * @author Winfxk
 */
public class AlterMenu extends FormBase {
	private Config config;

	public AlterMenu(Player player, FormBase upForm, File file) {
		super(player, upForm);
		config = new Config(file);
		t = "MakeButton";
		setK("{Title}", "{Content}", "{Buttons}", "{AlterButton}");
	}

	@Override
	public boolean MakeMain() {
		Object obj = config.get("Buttons");
		Map<String, Object> map = obj != null && obj instanceof Map ? (HashMap<String, Object>) obj : new HashMap<>();
		setD(msg.getText(config.get("Title")), msg.getText(config.get("Content")), map.size(), msg.getSon("Form", "AlterButton", this));
		if (!myPlayer.isAdmin())
			return Tip(getNotPermission(), false);
		CustomForm form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		form.addInput(getString("setTitle"), config.get("Title"), getString("setTitle"));
		form.addInput(getString("setContent"), config.get("Content"), getString("setContent"));
		form.addStepSlider(getPermission(), getPermissions(), Tool.ObjToInt(config.get("Permission")));
		form.addStepSlider(getPlayerFiltra(), getFiltras(), Tool.ObjToInt(config.get("FilteredModel")));
		form.addInput(getFiltreList(), getFiltreList(config.get("FilteredPlayer")), getFiltreList());
		form.addStepSlider(getWorldFiltras(), getFiltras(), Tool.ObjToInt(config.get("LevelFilteredModel")));
		form.addInput(getFiltreList(), getFiltreList(config.get("LevelList")), getFiltreList());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		FormResponseCustom d = getCustom(data);
		String Title = d.getInputResponse(1);
		String Content = d.getInputResponse(2);
		int Permission = d.getStepSliderResponse(3).getElementID();
		int FilteredModel = d.getStepSliderResponse(4).getElementID();
		List<String> FilteredPlayer = getList(d.getInputResponse(5));
		int LevelFilteredModel = d.getStepSliderResponse(6).getElementID();
		List<String> LevelList = getList(d.getInputResponse(7));
		config.set("Title", Title == null ? "" : Title);
		config.set("Content", Content == null ? "" : Content);
		config.set("Permission", Permission);
		config.set("FilteredModel", FilteredModel);
		config.set("FilteredPlayer", FilteredPlayer);
		config.set("LevelFilteredModel", LevelFilteredModel);
		config.set("LevelList", LevelList);
		config.set("alterPlayer", player.getName());
		config.set("alterTime", Tool.getDate() + " " + Tool.getTime());
		config.save();
		sendMessage(getString("Succeed: 设置成功！"));
		return isBack();
	}

	/**
	 * 返回当前界面警告词
	 * 
	 * @param Key
	 * @return
	 */
	protected String getWarning(String Key) {
		return msg.getSon("MakeButton", Key, this);
	}

	/**
	 * 将过滤字符串转换为数组
	 * 
	 * @param s
	 * @return
	 */
	protected List<String> getList(String s) {
		List<String> list = new ArrayList<>();
		if (s != null && !s.isEmpty()) {
			String[] strings = s.split(";");
			for (String string : strings)
				if (string != null && !string.isEmpty())
					list.add(string);
		}
		return list;
	}

	/**
	 * 返回过滤列表的字符串
	 * 
	 * @return
	 */
	protected String getFiltreList() {
		return getWarning("FiltreList");
	}

	/**
	 * 返回可以选择的权限
	 * 
	 * @return
	 */
	protected String[] getPermissions() {
		return new String[] { msg.getSon("Form", "All"), msg.getSon("Form", "OP"), msg.getSon("Form", "Player") };
	}

	/**
	 * 返回过滤模式的字符串
	 * 
	 * @return
	 */
	protected String[] getFiltras() {
		return new String[] { msg.getMessage("Nonuselist", this), msg.getMessage("Blacklist", this), msg.getMessage("Whitelist", this) };
	}

	/**
	 * 返回玩家过滤模式的字符串
	 * 
	 * @return
	 */
	protected String getPlayerFiltra() {
		return getWarning("PlayerFiltra");
	}

	/**
	 * 返回地图过滤的字符串
	 * 
	 * @return
	 */
	protected String getWorldFiltras() {
		return getWarning("WorldFiltra");
	}

	/**
	 * 返回显示权限字符串
	 * 
	 * @return
	 */
	protected String getPermission() {
		return getWarning("Permission");
	}

	/**
	 * 吧字符数组转换为字符串
	 * 
	 * @param obj
	 * @return
	 */
	protected String getFiltreList(Object obj) {
		List<String> list = obj != null && obj instanceof List ? (ArrayList<String>) obj : new ArrayList<>();
		String string = "";
		for (String s : list)
			string = string + (string.isEmpty() ? s : ";" + s);
		return string;
	}
}
