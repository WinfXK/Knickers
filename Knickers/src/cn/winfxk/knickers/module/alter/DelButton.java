package cn.winfxk.knickers.module.alter;

import java.io.File;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;

/**
 * 删除按钮前显示按钮列表的界面
 * 
 * @Createdate 2021/08/01 14:51:02
 * @author Winfxk
 */
public class DelButton extends AlterButton {

	public DelButton(Player player, FormBase upForm, File file) {
		super(player, upForm, file);
	}

	@Override
	public boolean disMain(FormResponse data) {
		int ID = getSimple(data).getClickedButtonId();
		if (ID >= listKey.size())
			return isBack();
		String Key = listKey.get(ID);
		Map<String, Object> map = (Map<String, Object>) Buttons.get(Key);
		if (MoreKey.containsKey(Key) && kis.getButtons().get(MoreKey.get(Key)).onDel(upForm, map))
			return true;
		return setForm(new ConfirmDeletion(player, upForm, new Data(Key, map, Buttons, config))).make();
	}
}
