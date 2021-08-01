package cn.winfxk.knickers.form.more;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;

/**
 * @Createdate 2021/08/01 12:54:57
 * @author Winfxk
 */
public class Command extends FormBase {
	private Map<String, Object> map;

	public Command(Player player, FormBase upForm, Map<String, Object> map) {
		super(player, upForm);
		this.map = map;
	}

	@Override
	public boolean MakeMain() {
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		return true;
	}
}
