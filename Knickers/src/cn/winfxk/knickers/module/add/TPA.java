package cn.winfxk.knickers.module.add;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.BaseMake;

/**
 * @Createdate 2021/08/07 21:48:44
 * @author Winfxk
 */
public class TPA extends BaseMake {

	public TPA(Player player, File file, FormBase upForm) {
		super(player, file, upForm);
	}

	@Override
	public boolean MakeMain() {
		if (!super.MakeMain())
			return false;
		form.addInput(getString("InputToPlayer"), "", getString("InputToPlayer"));
		form.addStepSlider(getString("isAffirm"), getTPAModle());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		if (!super.disMain(data))
			return false;
		String ToPlayer=d.getInputResponse(location+1);
		
		return true;
	}

	/**
	 * 返回传送模式的字符串
	 * 
	 * @return
	 */
	protected String[] getTPAModle() {
		return new String[] { getString("Request"), getString("Sompel") };
	}
}
