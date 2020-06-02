package cn.winfxk.knickers.module.cmd;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.tool.CustomForm;
import cn.winfxk.knickers.tool.Tool;

/**
 * 当要执行的命令包含变量时会显示的界面
 * 
 * @Createdate 2020/06/02 20:45:53
 * @author Winfxk
 */
public class UseVariable extends FormBase {
	private CommandData data;
	private static final String[] KK = { "{Index}" };
	private static final String[] KS = { "{Variable}" };
	private int count;
	private List<String> Hint, Tip;

	public UseVariable(Player player, FormBase upForm, CommandData data) {
		super(player, upForm);
		this.data = data;
		count = Tool.getSubCount_2(data.getClickCommand(), CommandButton.CommandVarSP);
		Hint = data.getHint();
		Tip = data.getTip();
	}

	@Override
	public boolean MakeMain() {
		CustomForm form = new CustomForm(getID(), getTitle());
		for (int i = 0; i < count; i--)
			form.addInput(Tip.size() > i ? msg.getText(Tip.get(i)) : getString("Variable", KK, new Object[] { i }), "",
					Hint.size() > i ? msg.getText(Hint.get(i)) : getString("Variable", KK, new Object[] { i }));
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse sb) {
		FormResponseCustom data = getCustom(sb);
		String[] strings = this.data.getClickCommand().split("\\{msg\\}");
		String string = strings[0], string2;
		for (int i = 0; i < strings.length; i++) {
			string2 = data.getInputResponse(i);
			if ((string2 == null || string2.isEmpty()) && !this.data.isAllowBlank()) {
				player.sendMessage(getString("notInputVariable", KS, new Object[] {
						Tip.size() > i ? msg.getText(Tip.get(i)) : getString("Variable", KK, new Object[] { i }) }));
				return MakeMain();
			}
			string += data.getInputResponse(i) + (strings.length > (i + 1) ? strings[i + 1] : "");
		}
		return Server.getInstance().dispatchCommand(player, msg.getText(string, this));
	}

	@Override
	protected String getTitle() {
		return msg.getText(data.getTitle(), this);
	}

	@Override
	protected String getContent() {
		return msg.getText(data.getContent(), this);
	}
}
