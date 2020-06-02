package cn.winfxk.knickers.module.tip;

import java.util.List;

import cn.nukkit.Server;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.tool.ModalForm;
import cn.winfxk.knickers.tool.SimpleForm;

/**
 * 点击Tip按钮时执行调用的界面
 * 
 * @Createdate 2020/06/02 19:40:30
 * @author Winfxk
 */
public class ClickTip extends FormBase {
	private TipData data;

	public ClickTip(FormBase upForm, TipData data) {
		super(upForm.getPlayer(), upForm);
		this.data = data;
	}

	@Override
	public boolean MakeMain() {
		if (data.getTipType().equals(TipButton.TypeKey.get(0))) {
			SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
			form.addButton(msg.getText(data.getButton1(), this));
			form.addButton(msg.getText(data.getButton2(), this));
			form.sendPlayer(player);
			return true;
		}
		ModalForm form = new ModalForm(getID(), getTitle(), getContent());
		form.setButton1(msg.getText(data.getButton1(), this));
		form.setButton2(msg.getText(data.getButton2(), this));
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		int ID;
		ID = this.data.getTipType().equals(TipButton.TypeKey.get(0)) ? getSimple(data).getClickedButtonId()
				: getModal(data).getClickedButtonId();
		Server server = Server.getInstance();
		List<String> list = ID == 0 ? this.data.getCommand1() : this.data.getCommand2();
		if (list.size() > 0)
			for (String string : this.data.getCommand1())
				server.dispatchCommand(player, msg.getText(string, this));
		return isBack();
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
