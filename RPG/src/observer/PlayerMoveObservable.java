package observer;
/**
* @author 周亮 
* @version 创建时间：2016年10月9日 下午4:43:21
* 被观察者接口。
* player通过该接口添加和删除监听player的移动动作。
* player实现该接口可以管理监听自己移动(move)动作的目标类，目标类要实现PlayerMoveObserver接口.  
*/
public interface PlayerMoveObservable {
	
	//增加一个观察者
	public void addMoveObserver(PlayerMoveObserver observer);
	//删除一个观察者
	public void deleteMoveObserver(PlayerMoveObserver observer);
	/**
	 * 既然要观察，我发生改变了他也应该有所动作，通知观察者
	 * @param posx  player的x坐标
	 * @param posy         y坐标
	 * @param delta        
	 */
	public void notifyObservers(double posx, double posy,double delta);
}
