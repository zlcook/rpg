package observer;

import role.Player;

/**
* @author 周亮 
* @version 创建时间：2016年10月12日 上午10:49:49
* 观察者接口。
* 想和plalyer对话的角色就实现该接口
* villagers实现该接口可以监听player的会话动作.
*/
public interface PlayerChatObserver {
	public void chatAction(Player player,double posx, double posy,double delta);
}
