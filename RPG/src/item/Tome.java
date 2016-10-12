package item;

import org.newdawn.slick.SlickException;

/**
* @author 周亮 
* @version 创建时间：2016年10月10日 上午10:11:44
* 类说明
*/
public class Tome extends Item {
	private int cooldown =-300;
	
	public Tome(String img_path, double posx, double posy, String name) throws SlickException {
		super(img_path, posx, posy, name);
		// TODO Auto-generated constructor stub
	}

	public int getCooldown() {
		return cooldown;
	}

	

}
