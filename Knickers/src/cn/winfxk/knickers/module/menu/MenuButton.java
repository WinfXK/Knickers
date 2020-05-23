package cn.winfxk.knickers.module.menu;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.MyPlayer;
import cn.winfxk.knickers.form.ButtonBase;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.tool.Tool;

/**
 * 菜单功能，点击后打开一个新的菜单
 * 
 * @Createdate 2020/05/23 12:36:26
 * @author Winfxk
 */
public class MenuButton extends FunctionBase {

	public MenuButton(Activate ac) {
		super(ac, "Menu");
	}

	@Override
	public boolean ClickButton(ButtonBase form, String Key) {
		MyPlayer myPlayer = ac.getPlayers(form.getPlayer());
		myPlayer.form = new OpenMenu(form.getPlayer(), form,
				new File(mag.getFile(), Tool.objToString(form.getMap().get("Menu"))));
		return myPlayer.form.MakeMain();
	}

	@Override
	protected FormBase getForm(Player player, Config config, FormBase base) {
		return new MakeMenu(player, base, config, this);
	}

	@Override
	public boolean delButton(ButtonBase base, String Key) {
		MyPlayer myPlayer = ac.getPlayers(base.getPlayer());
		myPlayer.form = new DeleteMenu(base.getPlayer(), base, base.getConfig(), Key);
		return myPlayer.form.MakeMain();
	}

	@Override
	public boolean delButton(Player player, Config config, String Key) {
		MyPlayer myPlayer = ac.getPlayers(player);
		myPlayer.form = new DeleteMenu(player, null, config, Key);
		return myPlayer.form.MakeMain();
	}
}
