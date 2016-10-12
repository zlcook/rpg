package item;

import org.newdawn.slick.SlickException;

import role.Player;

/**
* @author 周亮 
* @version 创建时间：2016年10月9日 下午10:55:18
* 类说明
* Name                Image file     Effect           Position (x, y) in pixels
-----------------------------------------------------------------------------
Amulet of Vitality  amulet.png     HP/Max HP +80    965, 3563
*/
public class Amulet extends Item {

	private int hp = 80;
	
	public Amulet(String img_path, double posx, double posy, String name) throws SlickException {
		super(img_path, posx, posy, name);
	}

	public int getHp() {
		return hp;
	}

	

}
