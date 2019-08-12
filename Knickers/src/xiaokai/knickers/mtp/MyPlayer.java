package xiaokai.knickers.mtp;
/**
 * @author Winfxk
 */

import java.io.File;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;

public class MyPlayer {
	public Instant loadTime;
	public boolean isPlayerOnJoin = true;
	/**
	 * 玩家对象
	 */
	public Player player;
	/**
	 * 玩家已经打开了的菜单列表
	 */
	public List<File> OpenMenuList;

	public File BackFile;
	/**
	 * 玩家打开的界面的项目列表
	 */
	public List<Map<String, Object>> Items;
	/**
	 * 用来临时存储文件对象，一般是创建按钮或者是删除按钮的时候用来保存正在操作的页面是哪个，不用BackFile是因为防止界面改变
	 */
	public File CacheFile;
	/**
	 * 在玩家添加按钮的时候临时存储添加的按钮类型
	 */
	public String AddButtonType;
	/**
	 * 打开的菜单的Key列表，第一次使用时在删除按钮
	 */
	public List<String> keyList;
	/**
	 * 要删除的菜单Key
	 */
	public String Key;
	/**
	 * 显示在线玩家列表的时候用来存储当时在线的玩家对象
	 */
	public List<Player> players;
	/**
	 * 存储项目数据
	 */
	public Map<String, Object> Item;
	/**
	 * 在Tpa传送的时候用来存储发送Tpa请求的玩家对象
	 */
	public Player TpaPlayer;
	/**
	 * 在执行命令按钮里面临时存储命令数据
	 */
	public List<String> Commnds;
	/**
	 * 在执行命令按钮里面临时存储执行命令的对象
	 */
	public String Commander;
	/**
	 * 是否是主页
	 */
	public boolean isMain;
	/**
	 * 表单ID顺序
	 */
	public int Formid = 0;
	/**
	 * 删除的时候的文件列表
	 */
	public List<File> DelFileList;
	/**
	 * 查看到底显示了哪些管理员按钮
	 */
	public List<String> UIAdminButtonKis;
	/**
	 * 是修改还是创建
	 */
	public boolean isAlter;
	public MyPlayer(Player player) {
		this.player = player;
	}
}
