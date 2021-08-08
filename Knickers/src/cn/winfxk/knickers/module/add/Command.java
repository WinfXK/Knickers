package cn.winfxk.knickers.module.add;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.base.CustomForm;
import cn.winfxk.knickers.module.BaseMake;
import cn.winfxk.knickers.tool.Tool;

/**
 * @Createdate 2021/08/07 21:48:33
 * @author Winfxk
 */
public class Command extends BaseMake {
	public Command(Player player, File file, FormBase upForm) {
		super(player, file, upForm);
	}

	public Command(Player player, File file, FormBase upForm, String Key) {
		super(player, file, upForm, Key);
	}

	@Override
	public boolean MakeMain() {
		if (!super.MakeMain())
			return false;
		return true;
	}

	@Override
	protected CustomForm getForm() {
		form = new CustomForm(getID(), Key == null ? getTitle() : getString("AlterTitle"));
		form.addLabel(Key == null ? getContent() : getString("AlterContent"));
		form.addInput(InputName(), Key == null ? "" : map.get("Text"), InputName());
		form.addInput(InputMoney(), Key == null ? 0 : map.get("Money"), InputMoney());
		form.addInput(InputCommand(), Key == null ? "" : map.get("Command"), InputCommand());
		form.addStepSlider(getPermission(), getPermissions(), Key == null ? 0 : Tool.ObjToInt(map.get("Permission")));
		form.addStepSlider(getPlayerFiltra(), getFiltras(), Key == null ? 0 : Tool.ObjToInt(map.get("FilteredModel")));
		form.addInput(getFiltreList(), Key == null ? "" : getFiltreList(map.get("FilteredPlayer")), getFiltreList());
		form.addStepSlider(getWorldFiltras(), getFiltras(), Key == null ? 0 : Tool.ObjToInt(map.get("LevelFilteredModel")));
		form.addInput(getFiltreList(), Key == null ? "" : getFiltreList(map.get("LevelList")), getFiltreList());
		form.addStepSlider(getIconType(), getIconTypes(), Tool.ObjToInt(map.get("IconType")));
		form.addInput(InputIconPath(), map.get("IconPath"), InputIconPath());
		location = form.getElements().size() - 1;
		form.addInput(getString("Msg"), Key == null ? "" : getFiltreList(map.get("Msg")), getString("Msg"));
		form.addInput(getString("Hint"), Key == null ? "" : getFiltreList(map.get("Hint")), getString("Hint"));
		form.sendPlayer(player);
		return form;
	}

	@Override
	public boolean disMain(FormResponse data) {
		if (!super.disMain(data))
			return false;
		List<String> Hint = getList(d.getInputResponse(location + 2)), Msg = getList(d.getInputResponse(location + 1));
		map.put("Hint", Hint);
		map.put("Msg", Msg);
		save();
		sendMessage(getString("Succeed"));
		return isBack();
	}

	@Override
	protected List<String> getList(String s) {
		List<String> list = new ArrayList<>();
		if (s != null && !s.isEmpty()) {
			String[] strings = s.split(";");
			for (String string : strings)
				if (string != null && !string.isEmpty())
					list.add(string);
		}
		int Count = Command.split("\\{msg\\}").length;
		while (Count - 1 > list.size())
			list.add(msg.config.getString("Tip", ""));
		return list;
	}

	@Override
	protected String InputCommand() {
		return getString("InputCommand");
	}
}
