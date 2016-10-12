package role;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import item.DistUtils;
import observer.PlayerChatObserver;
import observer.PlayerMoveObserver;

/**
* @author 周亮 
* @version 创建时间：2016年10月9日 上午10:45:30
* 类说明
*/
public class Villagers extends Unit implements PlayerChatObserver,PlayerMoveObserver {

	/**
	 * 每次交谈的内容
	 */
	private String chat="";
	/**
	 * true:显示会话内容 false:不显示会话内容
	 * 每次交谈开始后，4秒内显示会话内容，超过4秒不显示内容。
	 * 
	 */
	private boolean chatvisible = false;
	/**
	 * 每次交谈的持续时间
	 */
	private long chatperiod=0L;
	//角色会话内容
	private static Map<String,String[]> chatmsg;
	
	//private Timer timer;
	/*初始化会话内容*/
	static{
		chatmsg = new HashMap<>();
		chatmsg.put("PrinceAldric", new String[]{"Please seek out the Elixir of Life to cure the king.","The elixir! My father is cured! Thank you!"});
		chatmsg.put("Elvira", new String[]{"Return to me if you ever need healing.","You're looking much healthier now."});
		chatmsg.put("Garth", new String[]{"Find the Amulet of Vitality, across the river to the west.","Find the Sword of Strength - cross the bridge to the east, then head south."
				,"Find the Tome of Agility, in the Land of Shadows.","You have found all the treasure I know of."});
	
	}
	
	public Villagers(String image_path,double posx, double posy,int MAX_HP,int attack,int CoolDown,String name) throws SlickException {
		super(image_path, posx, posy, MAX_HP, attack, CoolDown,0,name);
	}
	@Override
	public void render(Graphics g) {
		super.render(g);
    	//绘制状态指示图标（头上方显示角色名字和健康值）
		drawNameBar(g);
        //交谈
        if( chatvisible){
        	drawChat(g);
        }
	}
	/**
	 * 绘制会话
	 * @param g
	 */
	private void drawChat(Graphics g){
		 Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
         Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black
         int text_width = g.getFont().getWidth(this.chat);
         float bar_height = 18;
         float bar_width;
         if(text_width>64){
         	bar_width = text_width+6;
         }
         else{
         	bar_width = 70;
         }
         int text_x = (int)(this.getPosx()-text_width/2);
         int text_y = (int)(this.getPosy() - bar_height - this.getHeight()/2-RPG.NAME_BAR_HEIGHT);
         float bar_x =text_x;
         float bar_y= text_y;          // Size of red (HP) rectangle
         g.setColor(BAR_BG);
         g.fillRect(bar_x, bar_y, bar_width, bar_height);
         g.setColor(VALUE);
         g.drawString(this.chat, text_x, text_y);
	}
	
	
	@Override
	public void update(World world, int dir_x, int dir_y, int delta) {
		// TODO Auto-generated method stub
		super.update(world, dir_x, dir_y, delta);
		//如果正在会话，则计时。
		if(chatvisible ){
			chatperiod+=delta; 
			long seconds=chatperiod/1000;
			if(seconds >=4){//大于4秒结束对话
				chatvisible=false;
				chatperiod=0L;
			}
		}
	}
	@Override
	public void chatAction(Player player,double posx, double posy, double delta) {

			//检查player距离是否在50像素之内
			double dist =DistUtils.dist(posx, posy,this.getPosx(), this.getPosy());
			//距离小于50像素
			if( dist<=50){
					//确定交谈内容
					String chats[]=null;
					if( this.getName().equalsIgnoreCase("PrinceAldric")){
						chats =chatmsg.get("PrinceAldric");
						if( !player.isElixir() ){
							chat= chats[0];
						}else{
							chat = chats[1];
							player.removeItem("elixir");
						}
					}else if(this.getName().equalsIgnoreCase("Elvira")){
						chats =chatmsg.get("Elvira");
						//正在显示会话，则处理加血内容
						if( chatvisible ){
							if(player.getHP()<player.getMAX_HP()){
								//加血
								player.setHP(player.getMAX_HP());
							}
						}else{
							if(player.getHP()<player.getMAX_HP()){
								chat=chats[1];
								//加血
								player.setHP(player.getMAX_HP());
							}else
								chat = chats[0];
						}
					}else if(this.getName().equalsIgnoreCase("Garth")){
						chats =chatmsg.get("Garth");
						if( player.getItem("amulet")==null ){
							chat= chats[0];
						}else if(player.getItem("sword")==null ){
							chat=chats[1];
						}else if(player.getItem("tome")==null ){
							chat = chats[2];
						}else 
							chat=chats[3];
					}
				//显示会话
				chatvisible=true;
			}
		
	}
	@Override
	public void action(Player player, double posx, double posy, double delta) {
		// TODO Auto-generated method stub
		//检查player距离是否在50像素之内
		/*double dist =DistUtils.dist(posx, posy,this.getPosx(), this.getPosy());
		//距离小于50像素
		if( dist<=50){
			//加血
			if(this.getName().equalsIgnoreCase("Elvira")){
				if(player.getHP()<player.getMAX_HP()){
					//加血
					player.setHP(player.getMAX_HP());
				}
			}
		}*/
	}
}
