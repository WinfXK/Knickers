package cn.winfxk.knickers.module;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.utils.Config;
import cn.winfxk.knickers.form.ButtonBase;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.tool.Format;
import cn.winfxk.knickers.tool.SimpleForm;

/**
 * 删除按钮时调用
 * 
 * @Createdate 2020/05/23 18:02:51
 * @author Winfxk
 */
public class DeleteFunction extends FormBase {
	protected Config config;
	protected String Key;
	protected Map<String, Object> Buttons, map;

	/**
	 * 删除一个按钮
	 * 
	 * @param player 要删除按钮的玩家对象
	 * @param upForm 上个页面
	 * @param config 要删除按钮的配置文件对象
	 * @param Key    要删除的按钮的Key
	 */
	public DeleteFunction(Player player, FormBase upForm, Config config, String Key) {
		super(player, upForm);
		this.config = config;
		this.Key = Key;
		t = "Function";
		Son = "DeleteFunction";
		setK("{Player}", "{Money}", "{ButtonText}", "{ButtonData}");
		Buttons = ButtonBase.getButtons(config);
	}

	@Override
	public boolean MakeMain() {
		setD(player.getName(), myPlayer.getMoney(), map.get("ButtonText"), getButtonData());
		SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
		form.addButton(getBack());
		form.addButton(getConfirm());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		if (getSimple(data).getClickedButtonId() == 1) {
			ButtonBase.removeButton(config, Key);
			sendMessage(getString("DeleteSucceed"));
		}
		return isBack();
	}

	/**
	 * 返回按钮数据
	 * 
	 * @return
	 */
	protected String getButtonData() {
		return new Format<>((LinkedHashMap<String, Object>) ButtonBase.getButtonMap(Buttons, Key)).getString();
	}
}
