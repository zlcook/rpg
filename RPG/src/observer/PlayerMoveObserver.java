package observer;

import role.Player;

/**
* @author 周亮 
* @version 创建时间：2016年10月9日 下午4:46:11
* 观察者接口。
* 想观察plalyer移动动作的角色就实现该接口
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
