package xiaokai.knickers;

/**
 * @author Winfxk
 */
public class EconomyAPI extends MyMoney {
	private me.onebone.economyapi.EconomyAPI eApi;
	public final static String Name = "EconomyAPI";

	/**
	 * 增加对EconomyAPI的支持</br>
	 * Added support for EconomyAPI
	 * 
	 * @param ac
	 */
	public EconomyAPI(Activate ac) {
		super(Name, ac.getConfig().getString("货币名称"));
		eApi = me.onebone.economyapi.EconomyAPI.getInstance();
	}

	@Override
	public double getMoney(String player) {
		return eApi.myMoney(player);
	}

	@Override
	public double addMoney(String player, double Money) {
		eApi.addMoney(player, Money);
		return getMoney(player);
	}

	@Override
	public double reduceMoney(String player, double Money) {
		eApi.reduceMoney(player, Money);
		return getMoney(player);
	}

	@Override
	public double setMoney(String player, double Money) {
		eApi.setMoney(player, Money);
		return getMoney(player);
	}

}
