package item;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import observer.PlayerMoveObserver;
import role.Player;

public class Item implements PlayerMoveObserver{
	private Image img;
	private double posx;
	private double posy;
	private String name;
	private boolean visible;
    public Item(String img_path,double posx,double posy,String name) 
    		throws SlickException{
    	this.img = new Image(img_path);
    	this.posx = posx;
    	this.posy = posy;
    	this.name = name;
    	this.visible = true;
    }
    
    public String getName() {
		return name;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public double getX(){
    	return posx;
    }
    public double getY(){
    	return posy;
    }
    public Image getImage(){
    	return img;
    }
    public void render(Graphics g)
    {
       if(this.img!=null&& this.visible)
         img.drawCentered((int) posx, (int) posy);
       
    }
	@Override
	public void action(Player player, double posx, double posy,double delta) {
		//如果物品可见
		if( this.isVisible()){
			//距离50像素
			double dist =DistUtils.dist(posx, posy, this.posx, this.posy);
			if( dist<=50){
				//player  get the item
				player.putItem(this);
				
				this.visible= false;
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((img == null) ? 0 : img.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (img == null) {
			if (other.img != null)
				return false;
		} else if (!img.equals(other.img))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
