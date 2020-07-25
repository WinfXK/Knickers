package cn.winfxk.knickers.form;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.am.ModuleDetails;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.tool.SimpleForm;
import cn.winfxk.knickers.tool.Tool;

/**
 * 显示功能列表
 * 
 * @Createdate 2020/06/03 00:29:53
 * @author Winfxk
 */
public class AllModule extends FormBase {

	public AllModule(Player player, FormBase upForm) {
		super(player, upForm);
		Son = "AllModule";
	}

	@Override
	public boolean MakeMain() {
		SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
		for (FunctionBase base : ac.getFunctionMag().getFunction().values()) {
			form.addButton(getString("", new String[] { "{ModuleName}", "{ModuleKey}", "{Enable}" },
					new Object[] { base.getName(), base.getModuleKey(), base.isEnable() }));
			listKey.add(base.getModuleKey());
		}
		form.addButton(getBack());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		int ID = getSimple(data).getClickedButtonId();
		if (ID < listKey.size())
			return myPlayer
					.showForm(new ModuleDetails(player, this, ac.getFunctionMag().getFunction().get(listKey.get(ID))));
		return isBack();
	}

	@Override
	protected String getString(String string) {
		return msg.getSon(Son, string, this);
	}

	@Override
	protected String getString(String string, String[] K, Object[] D) {
		return msg.getSon(Son, string, Tool.Arrays(this.K, K), Tool.Arrays(this.D, D), player);
	}

	@Override
	protected String getTitle() {
		return getString("Title");
	}

	@Override
	protected String getContent() {
		return getString("Content");
	}
}
