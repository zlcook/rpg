package observer;
/**
* @author 周亮 
* @version 创建时间：2016年10月12日 上午10:50:29
* 被观察者接口。
* player通过该接口添加和删除监听player的会话动作。
* player实现该接口可以管理监听自己会话(chat)动作的目标类，目标类要实现PlayerChatObserver接口.  
*/
public interface PlayerChatObservable {
	//增加一个观察者
	public void addChatObserver(PlayerChatObserver observer);
	//删除一个观察者
	public void deleteChatObserver(PlayerChatObserver observer);
	//既然要观察，我发生改变了他也应该有所动作，通知观察者
	public void notifyChatObservers(double posx, double posy,double delta);
}
