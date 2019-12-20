package xiaokai.knickers;

import cn.nukkit.plugin.PluginBase;

/**
 * @author Winfxk
 */
public class Knickers extends PluginBase {
	private static Activate ac;

	@Override
	public void onDisable() {
		getLogger().info(getName() + " has stopped");
	}

	@Override
	public void onEnable() {
		ac = new Activate(this);
	}

	public static Activate getInstance() {
		return ac;
	}

	@Override
	public void onLoad() {
		getLogger().info(getName() + " load...");
	}
}
