package item;

import org.newdawn.slick.SlickException;

/**
* @author 周亮 
* @version 创建时间：2016年10月10日 上午10:09:36
* 类说明
* 描述Sword的类， 除了继承Item的属性外，还具有damage属性。player得到Sword后可以增加相应的伤害值。
*/
public class Sword extends Item {

	private int damage=30;
	public Sword(String img_path, double posx, double posy, String name) throws SlickException {
		super(img_path, posx, posy, name);
		// TODO Auto-generated constructor stub
	}
	public int getDamage() {
		return damage;
	}
	

}
