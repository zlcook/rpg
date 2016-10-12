package observer;

import role.Player;

/**
* @author 周亮 
* @version 创建时间：2016年10月9日 下午4:46:11
* 类说明
*/
public interface PlayerMoveObserver {
	/**
	 * 
	 * @param player
	 * @param posx
	 * @param posy
	 * @param delta
	 */
	public void action(Player player,double posx, double posy,double delta);
}
