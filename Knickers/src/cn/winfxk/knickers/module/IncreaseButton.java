package cn.winfxk.knickers.module;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.tool.SimpleForm;

/**
 * 增加菜单的按钮的页面
 * 
 * @Createdate 2020/05/30 16:54:54
 * @author Winfxk
 */
public class IncreaseButton extends FormBase {
	private Config config;
	private FunctionMag mag;
	private List<FunctionBase> bases = new ArrayList<>();

	/**
	 * 增加菜单的按钮的页面
	 * 
	 * @param player 创建型按钮的玩家对象
	 * @param upForm 上一个页面
	 * @param file   要创建按钮的菜单配置文件对象
	 */
	public IncreaseButton(Player player, FormBase upForm, Config config) {
		super(player, upForm);
		this.config = config;
		t = FunctionBase.FunctionKey;
		mag = ac.getFunctionMag();
	}

	@Override
	public boolean MakeMain() {
		SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
		String[] KK = { "{Player}", "{Money}", "{TypeName}", "{TypeKey}" };
		for (FunctionBase base : mag.getFunction().values()) {
			form.addButton(getString("TypeItem", KK,
					new Object[] { player.getName(), myPlayer.getMoney(), base.getName(), base.getModuleKey() }));
			bases.add(base);
		}
		form.addButton(getBack());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		int ID = getSimple(data).getClickedButtonId();
		if (bases.size() > ID)
			return bases.get(ID).makeButton(player, config, this);
		return isBack();
	}
}
