package item;

import org.newdawn.slick.SlickException;

/**
* @author 周亮 
* @version 创建时间：2016年10月10日 上午10:12:42
* 类说明
* 描述Elixir的类，该类继承Item类，没有其它属性。
*/
public class Elixir extends Item {
	
	public Elixir(String img_path, double posx, double posy, String name) throws SlickException {
		super(img_path, posx, posy, name);
	}
}
