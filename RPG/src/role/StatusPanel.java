package role;

import java.util.LinkedHashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import item.Item;
import observer.PlayerMoveObserver;

/**
* @author 周亮 
* @version 创建时间：2016年10月9日 下午6:57:29
* 类说明
*/
public class StatusPanel implements PlayerMoveObserver{

	private Image panel ;
    private float posx;
    private float posy;
    private float width;
    private float height;
    private Player player;
    public StatusPanel(String image_path,float posx, float posy, float width, float height,Player player) {
		super();
		this.posx = posx;
		this.posy = posy;
		this.width = width;
		this.height = height;
		try {
			panel = new Image(image_path);//"/assets/panel.png"
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.player =player;
	}


	/** Draw the status panel to the screen at the correct place.
     * @param g The current Graphics context.
     */
    public void render(Graphics g)
    {
      /* Color bg = new Color(24f, 30f, 29f, 1.0f);
       g.setColor(bg);
       g.fillRect(posx, posy, width, height);*/
       renderPanel(g);
       
    }

	@Override
	public void action(Player player, double posx, double posy,double delta) {
		// TODO Auto-generated method stub
		drawPanel(posx, posy);
		
		
	}
	
	/**
	 * 绘制面板位置
	 * @param posx
	 * @param posy
	 */
	private void drawPanel( double posx, double posy){
		double fixdisx=RPG.SCREEN_WIDTH/2;
		double fixdisy=RPG.SCREEN_HEIGHT/2-this.height;
		double distx = posx-this.posx;
		double disty =this.posy-posy;
		distx-=fixdisx;
		disty-=fixdisy;
		this.posx+=distx;
		this.posy-=disty;
		this.posy=this.posy+this.height/2;
	}
	
	 public void renderPanel(Graphics g)
	    {
	        // Panel colours
	        Color LABEL = new Color(0.9f, 0.9f, 0.4f);          // Gold
	        Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
	        Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp
	        Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f);      // Red, transp
	        Color BLUE = new Color(0.0f, 0.0f, 0.9f, 0.8f);      // BLUE, transp

	        // Variables for layout
	        String text;                // Text to display
	        int text_x, text_y;         // Coordinates to draw text
	        int bar_x, bar_y;           // Coordinates to draw rectangles
	        int bar_width, bar_height;  // Size of rectangle to draw
	        int hp_bar_width;           // Size of red (HP) rectangle
	        int inv_x, inv_y;           // Coordinates to draw inventory item
	        float health_percent;       // Player's health, as a percentage

	        // Panel background image
	        //panel.draw(0, RPG.SCREEN_HEIGHT - RPG.STATUS_PANEL_HEIGTH);
	        panel.draw(posx, posy);

	        // Display the player's health
	        text_x =(int) (posx+ 15);
	        text_y=(int) (this.posy+25);
	        g.setColor(LABEL);
	        g.drawString("Health:", text_x, text_y);
	        text = String.valueOf(player.getHP())+'/'+String.valueOf(player.getMAX_HP());                                 // TODO: HP / Max-HP

	        bar_x = (int) (posx+90);
	        bar_y = (int) (posy + 20);
	        bar_width = 90;
	        bar_height = 30;                                                 
	        hp_bar_width = (int) (bar_width * player.getPercentage());// TODO: HP / Max-HP
	        text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
	        g.setColor(BAR_BG);
	        g.fillRect(bar_x, bar_y, bar_width, bar_height);
	        g.setColor(BAR);
	        g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
	        g.setColor(VALUE);
	        g.drawString(text, text_x, text_y);

	        // Display the player's damage and cooldown
	        text_x = (int) (posx+200);
	        g.setColor(LABEL);
	        g.drawString("Damage:", text_x, text_y);
	        text_x += 80;
	        text = String.valueOf(player.getAttack());                                    // TODO: Damage
	        g.setColor(VALUE);
	        g.drawString(text, text_x, text_y);
	        text_x += 40;
	        g.setColor(LABEL);
	        g.drawString("Rate:", text_x, text_y);
	        
	        
	        text = String.valueOf(player.getCoolDownTime())+'/'+String.valueOf(player.getCoolDown());                                 // TODO: HP / Max-HP
	        bar_x=text_x+55;
	        bar_width-=40;
	        hp_bar_width=(int) (bar_width * player.getCooldownPercentage());// TODO: coolDownTime / CoolDown
	        g.setColor(BAR_BG);
	        g.fillRect(bar_x, bar_y, bar_width, bar_height);
	        g.setColor(BLUE);
	        g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);

	        text_x=bar_x+10;
	        text = String.valueOf(player.getCoolDown());                                    // TODO: Cooldown
	        g.setColor(VALUE);
	        g.drawString(text, text_x, text_y);
	        
	        // Display the player's inventory
	        g.setColor(LABEL);
	        g.drawString("Items:",text_x+50, text_y);
	        bar_x= text_x+115;
	        bar_y= text_y-14;
	        bar_width = 288;
	        bar_height = bar_height + 20;
	        g.setColor(BAR_BG);
	        g.fillRect(bar_x, bar_y, bar_width, bar_height);
	        
	        inv_x = bar_x;
	        inv_y = (int)player.getPosy()+RPG.SCREEN_HEIGHT/2 - RPG.STATUS_PANEL_HEIGTH/2
	            + ((RPG.STATUS_PANEL_HEIGTH - 72) / 2);
	        LinkedHashMap<String, Item> items =player.getItems();
	        for(String key :items.keySet()){
	        	Item item = items.get(key);
	        	item.getImage().draw(inv_x,inv_y);
	        	inv_x+=72;
	        }
	       
	    }
	 /*
	 public void getDraw(Item item,int inv_x,int inv_y){
	    	boolean collect = true;
	    	if(collect){
	    		item.getImage().draw(inv_x,inv_y);
	    	}
	    	else{
	    		item.getImage().drawCentered((int)item.getX(), (int)item.getY());
	    	}
	    }
	 public Item[] getItem() {
		 	Item[] item=null;
			try {
				item = new Item[4];
				item[0] = new Item(RPG.ASSETS_PATH+"/items/amulet.png",965,3563);
				item[1] = new Item(RPG.ASSETS_PATH+"/items/sword.png",500,700);
				item[2] = new Item(RPG.ASSETS_PATH+"/items/tome.png",600,700);
				item[3] = new Item(RPG.ASSETS_PATH+"/items/elixir.png",700,700);
			} catch (SlickException e) {
				e.printStackTrace();
			}
	    	return item;
	    }*/
}
