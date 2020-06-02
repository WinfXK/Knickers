package cn.winfxk.knickers.form.am;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.AllModule;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.tool.SimpleForm;

/**
 * 模块详情
 * 
 * @Createdate 2020/06/03 00:52:28
 * @author Winfxk
 */
public class ModuleDetails extends AllModule {
	private FunctionBase base;

	public ModuleDetails(Player player, FormBase upForm, FunctionBase base) {
		super(player, upForm);
		this.base = base;
		setK("{ModuleName}", "{ModuleKey}", "{Enable}", "{Player}", "{Money}");
	}

	@Override
	public boolean MakeMain() {
		setD(base.getName(), base.getModuleKey(), base.isEnable(), player.getName(), myPlayer.getMoney());
		SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
		form.addButton(getString(base.isEnable() ? "Close" : "Open"));
		form.addButton(getBack());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		if (getSimple(data).getClickedButtonId() != 0)
			return isBack();
		base.setEnable(!base.isEnable());
		return sendMessage(getString(base.isEnable() ? "OpenModule" : "CloseModule"));
	}

	@Override
	protected String getContent() {
		return getString("DetailsContent");
	}
}
