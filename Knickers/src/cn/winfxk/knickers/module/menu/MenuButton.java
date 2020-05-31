package cn.winfxk.knickers.module.menu;

import cn.nukkit.Player;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.module.ModuleData;

/**
 * 菜单功能，点击后打开一个新的菜单
 * 
 * @Createdate 2020/05/23 12:36:26
 * @author Winfxk
 */
public class MenuButton extends FunctionBase {
	public static final String MenuKey = "Menu";

	public MenuButton(Activate ac) {
		super(ac, MenuKey);
	}

	@Override
	protected FormBase getMakeForm(Player player, Config config, FormBase base) {
		return new MakeMenu(player, base, config, this);
	}

	@Override
	public boolean delButton(FormBase base, ModuleData data) {
		return (ac.getPlayers(base.getPlayer()).form = new DeleteMenu(base.getPlayer(), base, data.getConfig(),
				data.getKey())).MakeMain();
	}

	@Override
	public boolean delButton(Player player, Config config, String Key) {
		return (ac.getPlayers(player).form = new DeleteMenu(player, null, config, Key)).MakeMain();
	}

	@Override
	public MenuData getModuleData(Config config, String Key) {
		return new MenuData(config, Key);
	}

	@Override
	public FormBase getAlterForm(FormBase form, ModuleData data) {
		return new AlterMenu(form, MenuData.getMenuData(data));
	}

	@Override
	public boolean ClickButton(FormBase form, ModuleData data) {
		return (ac.getPlayers(form.getPlayer()).form = new OpenMenu(form.getPlayer(), form,
				MenuData.getMenuData(data).getMenuconfig())).MakeMain();
	}
}
