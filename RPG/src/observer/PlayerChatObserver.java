package observer;

import role.Player;

/**
* @author 周亮 
* @version 创建时间：2016年10月12日 上午10:49:49
* 类说明：想和plalyer对话的角色就实现该接口
*/
public interface PlayerChatObserver {
	public void chatAction(Player player,double posx, double posy,double delta);
}
