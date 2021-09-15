package cn.winfxk.knickers.form.more;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.MakeForm;
import cn.winfxk.knickers.form.base.CustomForm;
import cn.winfxk.knickers.rec.SecurityPermissions;
import cn.winfxk.knickers.tool.Tool;

/**
 * @Createdate 2021/08/01 12:54:57
 * @author Winfxk
 */
public class Command extends FormBase {
	private String Command;
	private List<String> Hint, Msg;
	private String[] CMD;
	private boolean isEmpty;
	private String Permission;

	public Command(Player player, FormBase upForm, Map<String, Object> map) {
		super(player, upForm);
		Command = Tool.objToString(map.get("Command"));
		Command = Command == null ? "" : Command;
		Object obj = map.get("Hint");
		Hint = obj == null || !(obj instanceof List) ? new ArrayList<>() : (ArrayList<String>) obj;
		obj = map.get("Msg");
		Son = "Command";
		Permission = Tool.objToString(map.get("Commander"), "Player");
		Permission = Permission == null ? "Player" : Permission;
		isEmpty = Tool.ObjToBool(map.get("isEmpty"));
		Msg = obj != null && obj instanceof List ? (ArrayList<String>) obj : new ArrayList<>();
	}

	@Override
	public boolean MakeMain() {
		if (Command == null || Command.isEmpty())
			return setForm(new MakeForm(player, upForm, getString("Tip"), getString("CommandNull"))).make();
		CMD = msg.getText(Command, this).split("\\{msg\\}");
		if (Hint == null || Msg == null || CMD.length <= 0)
			return setForm(new MakeForm(player, upForm, getString("Tip"), getString("MsgNull"))).make();
		CustomForm form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		for (int i = 0; i < CMD.length; i++) {
			if (i >= Msg.size() || i >= Hint.size())
				break;
			form.addInput(msg.getText(Msg.get(i), this), "", msg.getText(Hint.get(i)));
		}
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		String C = "";
		FormResponseCustom d = getCustom(data);
		String string;
		for (int i = 0; i < CMD.length - 1; i++) {
			if (i >= Msg.size() || i >= Hint.size())
				break;
			string = d.getInputResponse(i + 1);
			if (!isEmpty && string == null || string.isEmpty())
				return setForm(new MakeForm(player, upForm, getString("Tip"), getString("InputNull"), true, true)).make();
			C = (C.isEmpty() ? CMD[0] : C) + string + CMD[i + 1];
		}
		String[] cmds;
		switch (Permission.toLowerCase()) {
		case "console":
		case "c":
		case "控制台":
			cmds = C.split("\\{m\\}");
			for (String s : cmds)
				if (s != null && !s.isEmpty())
					try {
						server.dispatchCommand(new ConsoleCommandSender(), s);
					} catch (Exception e) {
					}
			return true;
		case "op":
		case "admin":
		case "管理员":
			if (player.isOp())
				return server.dispatchCommand(player, C);
			myPlayer.SecurityPermissions = true;
			player.setOp(true);
			try {
				cmds = C.split("\\{m\\}");
				for (String s : cmds)
					if (s != null && !s.isEmpty())
						try {
							server.dispatchCommand(player, s);
						} catch (Exception e) {
						}
			} catch (Exception e) {
				return sendMessage(msg.getSun(t, "Command", "Error", this));
			} finally {
				new SecurityPermissions(myPlayer).start();
			}
			return true;
		default:
			cmds = C.split("\\{m\\}");
			for (String s : cmds)
				if (s != null && !s.isEmpty())
					try {
						server.dispatchCommand(player, s);
					} catch (Exception e) {
					}
			return true;
		}
	}

	@Override
	protected String getTitle() {
		return msg.getSun("Command", "FormCommand", "Title", this);
	}

	@Override
	protected String getContent() {
		return msg.getSun("Command", "FormCommand", "Content", this);
	}
}
