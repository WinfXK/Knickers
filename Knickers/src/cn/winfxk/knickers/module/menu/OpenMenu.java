package cn.winfxk.knickers.module.menu;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.utils.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.tool.SimpleForm;

/**
 * 打开一个菜单
 * 
 * @Createdate 2020/05/23 21:07:11
 * @author Winfxk
 */
public class OpenMenu extends FormBase {
	private Config config;

	/**
	 * 打开一个菜单
	 * 
	 * @param player 要打开菜单的玩家对象
	 * @param upForm 上个界面
	 * @param file   要打开的菜单的文件对象
	 */
	public OpenMenu(Player player, FormBase upForm, File file) {
		this(player, upForm, new Config(file, Config.YAML));
	}

	/**
	 * 打开一个菜单
	 * 
	 * @param player 要打开菜单的玩家对象
	 * @param upForm 上个界面
	 * @param config 要打开的菜单的配置文件对象
	 */
	public OpenMenu(Player player, FormBase upForm, Config config) {
		super(player, upForm);
		this.config = config;
	}

	@Override
	public boolean MakeMain() {
		SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());

		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		return true;
	}

	@Override
	protected String getContent() {
		return msg.getText(config.get("Content"), this, player);
	}

	@Override
	protected String getTitle() {
		return msg.getText(config.get("Name"), this, player);
	}
}
