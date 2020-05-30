package cn.winfxk.knickers;

import java.io.File;

import cn.nukkit.utils.ConfigSection;

/**
 * @Createdate 2020/05/30 15:54:15
 * @author Winfxk
 */
public class Config extends cn.nukkit.utils.Config {
	public File file;

	public Config() {
	}

	public Config(int type) {
		super(type);
	}

	public Config(String file) {
		super(file);
	}

	public Config(File file) {
		super(file);
	}

	public Config(String file, int type) {
		super(file, type);
	}

	public Config(File file, int type) {
		super(file, type);
	}

	public Config(String file, int type, ConfigSection defaultMap) {
		super(file, type, defaultMap);
	}

	public Config(File file, int type, ConfigSection defaultMap) {
		super(file, type, defaultMap);
	}
}
