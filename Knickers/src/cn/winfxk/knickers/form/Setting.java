package cn.winfxk.knickers.form;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.money.MyEconomy;
import cn.winfxk.knickers.tool.CustomForm;
import cn.winfxk.knickers.tool.Tool;

/**
 * 插件设置界面
 * 
 * @Createdate 2020/05/30 18:43:26
 * @author Winfxk
 */
public class Setting extends FormBase {
	private Config config;
	private List<String> list;
	private String ToolID;

	public Setting(Player player, FormBase upForm) {
		super(player, upForm);
		Son = "Setting";
		config = ac.getConfig();
		list = ac.getEconomyManage().getEconomy();
		ToolID = config.getString("Tool");
	}

	@Override
	public boolean MakeMain() {
		CustomForm form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		form.addInput(getString("Tool"), ToolID, getString("Tool"));
		form.addInput(getString("EconomyName"), config.getString("EconomyName"), getString("EconomyName"));
		form.addDropdown(getString("Economy"), list, list.indexOf(ac.getEconomy().getEconomyName()));
		form.addToggle(getString("DisablesDisplay"), config.getBoolean("DisablesDisplay"));
		form.addToggle(getString("EnableTool"), config.getBoolean("EnableTool"));
		form.addToggle(getString("ForceTool"), config.getBoolean("ForceTool"));
		form.addToggle(getString("MonitorClick"), config.getBoolean("MonitorClick"));
		form.addToggle(getString("Monitordblclick"), config.getBoolean("Monitordblclick"));
		form.addInput(getString("MonitorTime"), config.getInt("MonitorTime"), getString("MonitorTime"));
		form.addToggle(getString("KickToolItem"), config.getBoolean("KickToolItem"));
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse sb) {
		FormResponseCustom data = getCustom(sb);
		String ID = data.getInputResponse(1);
		ID = ID == null || ID.isEmpty() ? null : ac.getItems().objToFullID(ID, true, null);
		String EconomyName = data.getInputResponse(2);
		if (EconomyName == null || EconomyName.isEmpty()) {
			player.sendMessage(getString("NotInputEconomyName"));
			return MakeMain();
		}
		MyEconomy Economy = ac.getEconomyManage().getEconomy(list.get(data.getDropdownResponse(3).getElementID()));
		boolean DisablesDisplay = data.getToggleResponse(4);
		boolean EnableTool = data.getToggleResponse(5) && ID != null && !ID.isEmpty();
		boolean ForceTool = data.getToggleResponse(6) && ID != null && !ID.isEmpty() && EnableTool;
		boolean MonitorClick = data.getToggleResponse(7);
		boolean Monitordblclick = data.getToggleResponse(8);
		String MonitorTimeString = data.getInputResponse(9);
		int MonitorTime = 30;
		if (MonitorTimeString == null || MonitorTimeString.isEmpty() || !Tool.isInteger(MonitorTimeString)
				|| (MonitorTime = Tool.ObjToInt(MonitorTimeString)) <= 0) {
			player.sendMessage(getString("MonitorTimeIllegality"));
			return MakeMain();
		}
		boolean KickToolItem = data.getToggleResponse(10);
		config.set("Tool", ID);
		config.set("EconomyName", EconomyName);
		ac.setEconomy(Economy.getEconomyName());
		ac.getEconomy().setEconomyName(EconomyName);
		config.set("DisablesDisplay", DisablesDisplay);
		config.set("EnableTool", EnableTool);
		config.set("ForceTool", ForceTool);
		config.set("MonitorClick", MonitorClick);
		config.set("Monitordblclick", Monitordblclick);
		config.set("MonitorTime", MonitorTime);
		config.set("KickToolItem", KickToolItem);
		if (EnableTool) {
			ac.setItem(null);
			if (ForceTool) {
				ac.getThread().setMonitorTime(MonitorTime);
			} else
				ac.getThread().setWhile(false);
		}
		sendMessage(getString("SettingSucceed"));
		return config.save() && isBack();
	}

	@Override
	protected String getString(String string) {
		return msg.getSon(Son, string, this);
	}

	@Override
	protected String getTitle() {
		return getString("Title");
	}

	@Override
	protected String getContent() {
		return getString("Content");
	}

	@Override
	protected String getString(String string, String[] K, Object[] D) {
		return msg.getSon(Son, string, K, D, this, player);
	}
}
