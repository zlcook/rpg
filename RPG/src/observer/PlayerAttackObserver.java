package observer;
/**
* @author 周亮 
* @version 创建时间：2016年10月10日 下午4:46:10
* 类说明:监听攻击事件的监听者
*/
public interface PlayerAttackObserver {
	/**
	 * 开始攻击
	 * @param dir_x 攻击者移动的x方向
	 * @param dir_y 攻击者移动的y方向
	 * @param posx 攻击者的x坐标
	 * @param posy 攻击者的y坐标
	 * @param delta Time passed since last frame (milliseconds).
	 * @param attackValue 攻击产生的伤害值
	 */
	public void handleAttack(int dir_x,int dir_y,double posx, double posy,double delta,int attackValue);

}
