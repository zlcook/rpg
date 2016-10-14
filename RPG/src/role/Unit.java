package role;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
/**
 * 
 * @author dell
 * 游戏中所有人物角色的基类，该类描述了角色的最基本属性和提供基本的方法render、update、cutHp、comeBack等，
 * 如果子类想要实现自己的需求可以重写这些方法。
 */
public class Unit {
	// In pixels
    private double posx;
    private double posy;
    private int CoolDown;
    private int HP;
    private int MAX_HP;
    private int attack;
    private int coolDownTime = 30;
    private Image img = null;
    private Image img_flipped = null;
    private double width, height;
    private boolean face_left = false;
    private String name;
    /**
     * 判断角色是否灭绝，角色一旦灭绝就不可以复活，
     * 如果角色没有灭绝，当角色hp为0时，在复活等待期到达后则可以复活。
     * 目前，只有player可以复活。
     */
    private boolean isdieout=false;
    /**
     * 角色复活等待时间,in mills
     */
    private long resurgence=0L;
    /**
     * 复活计时器，角色死亡后进入复活期，当计时器为0时满血复活
     */
    private long resurgenceTimer=0L;
    
	public Unit(String image_path,double posx, double posy,int MAX_HP,int attack,int CoolDown)
        throws SlickException
    {
    	img = new Image(image_path);
    	img_flipped = img.getFlippedCopy(true, false);
    	this.posx = posx;
    	this.posy = posy;
    	this.width = img.getWidth();
        this.height = img.getHeight();
        this.HP = MAX_HP;
    	this.MAX_HP = MAX_HP;
    	this.attack = attack;
    	this.CoolDown= CoolDown;
    }
	public Unit(String image_path,double posx, double posy,int MAX_HP,int attack,int CoolDown,long resurgence,String name)
	        throws SlickException
    {
		this(image_path, posx, posy, MAX_HP, attack, CoolDown);
		this.name = name;
		this.resurgence=resurgence;
    }
	
    public String getName() {
		return name;
	}
	public boolean isDieout() {
		return isdieout;
	}
	public void setIsdieout(boolean isdieout) {
		this.isdieout = isdieout;
	}
	public long getResurgenceTimer() {
		return resurgenceTimer;
	}
	
	public long getResurgence() {
		return resurgence;
	}
	public double getPosx(){
    	return posx;
    }
    public double getPosy(){
    	return posy;
    }
    public void setPosx(double posx) {
		this.posx = posx;
	}
	public void setPosy(double posy) {
		this.posy = posy;
	}
	public double getHeight() {
		return height;
	}
	public double getWidth() {
		return width;
	}
	public int getCoolDown(){
    	return CoolDown;
    }
    public int getHP(){
    	return HP;
    }
    public void addMax_HP(int hp){
    	this.MAX_HP+=hp;
    	this.HP+=hp;
    }
    public void addAttack(int attack){
    	this.attack+=attack;
    }
    public void addCoolDown(int CoolDown){
    	this.CoolDown+=CoolDown;
    }
    public int getAttack(){
    	return attack;
    }
    public int getCoolDownTime(){
    	return coolDownTime;
    }
    /**
     * 开始技能冷却
     */
    public void startCoolDownTime(){
    	this.coolDownTime = CoolDown;
    }
    public int getMAX_HP() {
		return MAX_HP;
	}
	public void setHP(int hP) {
		HP = hP;
	}
	/** Draw the player to the screen at the correct place.
     * @param g The current Graphics context.
     * @param cam_x Camera x position in pixels.
     * @param cam_y Camera y position in pixels.
     */
    public void render(Graphics g)
    {
    	if( this.isActive()){
	        Image which_img;
	        which_img = this.face_left ? this.img_flipped : this.img;
	        which_img.drawCentered((int) posx, (int) posy);
    	}
    }
    /**
     * 减少血量
     * @param attackValue，被减少的值
     */
    public void cutHp(int attackValue){
    	if( this.HP>0){
			if( attackValue>0){
				int leftHp =this.HP-attackValue;
	    		this.HP=(leftHp<=0)?0:leftHp;
	    		if(this.HP<=0){
	    			//死亡
	    			death();
	    		}
	    	}
    	}
    }
    
    /**
     * 判断角色是否活跃，角色活跃则可以在地图上进行正常的角色活动，否则消失在地图上。
     * 1.如果角色已经灭绝isdeadout=true，则角色死亡。
     * 2.isdeadout=false,resurgenceTimer>0，则角色死亡。
     * 3.isdeadout=false,resurgenceTimer<=0,角色活跃着。
     * @return
     */
    public boolean isActive(){
    	if(!this.isdieout && resurgenceTimer<=0)
    		return true;
    	else
    		return false;
    }
    public void update(World world, int dir_x, int dir_y, int delta){
    	//角色没有灭绝则可以复活
    	if( !this.isdieout ){
	    	//角色没有灭绝，如果角色死亡则进入复活等待
	    	if( this.resurgenceTimer>=0){
	    		this.resurgenceTimer-=delta;
	    		if(this.resurgenceTimer<=0){
	    			//复活成功
	    			comeBack();
	    		}
	    	}
    	}
    	if( this.isActive()){
    		/**
			 * 攻击冷却期
			 */
			if( this.coolDownTime>0)
				 this.coolDownTime-=delta;
    	}
    }
    /**
     * 复活
     */
    public void comeBack(){
    	this.HP = this.MAX_HP;
		this.coolDownTime =0;
    }
    /**
     * 死亡
     */
    public void death(){
    	//判断是否可以复活的,resurgence>=0怎可以复活
		if( this.resurgence>=0){
			this.resurgenceTimer=this.resurgence;
		}else
			this.isdieout = true;
    }
    public double getPercentage(){
    	return (double)HP/(double)MAX_HP;
    }
    public double getCooldownPercentage(){
    	return (double)coolDownTime/(double)CoolDown;
    }
    /**
     * implement the Algorithm 1
     * 用于计算monster被player攻击或者和player战斗时移动的坐标
     * @param world 
     * @param speed 速度
     * @param delta  delta Time passed since last frame (milliseconds).
     * @param player_x player的x坐标
     * @param player_y player的y坐标
     * @param isflee  true:表示monster逃离player；false:monster靠近player
     */
    public  void calculateAttackMonsterCoordinate(World world,double speed,double delta,double player_x,double player_y,boolean isflee){
    
		// Move the monster automtic, as a multiple of delta * speed
      
        double amount =delta*speed;
	  	//使用计算坐标移动距离命令计算新的坐标移动距离
        double distx= player_x-this.posx;
        double disty= player_y-this.posy;
        double dir_x = 0L;
        double dir_y = 0L;
        //靠近player
        if( !isflee){
        	dir_x = distx>0?1:(distx<0?-1:0);
        	dir_y = disty>0?1:(disty<0?-1:0);
        }else{
        	//逃离，所以不可以取0，即不可以原地不动
        	
        	dir_x = distx>=0?-1:1;
        	dir_y = disty>=0?-1:1;
        	distx=-distx;
        	disty=-disty;
        }
        if (dir_x > 0)
            this.face_left = false;
        else if (dir_x < 0)
            this.face_left = true;
        
        double dist_total =Math.pow(( Math.pow(distx, 2)+Math.pow(disty, 2)),0.5);
        double dx = distx/dist_total*amount;
        double dy = disty/dist_total*amount;
  		
        double new_x = posx +  dx;
        double new_y = posy +  dy;
        // Move in x first
        double x_sign = Math.signum(dir_x);
        if(!world.terrainBlocks(new_x + x_sign * width / 4, posy + height / 4) 
                && !world.terrainBlocks(new_x + x_sign * width / 4, posy - height / 4)) {
            posx = new_x;
        }
        // Then move in y
        double y_sign = Math.signum(dir_y);
        if(!world.terrainBlocks(posx + width / 4, new_y + y_sign * height / 4) 
                && !world.terrainBlocks(posx - width / 4, new_y + y_sign * height / 4)){
            posy = new_y;
        }
    }
    /**
     * 计算新的坐标
     * @param world
     * @param dir_x The unit's movement in the x axis (-1, 0 or 1).  
     * @param dir_y The unit's movement in the y axis (-1, 0 or 1).
     * @param speed 角色移动速度
     * @param delta Time passed since last frame (milliseconds).
     */
    public  void calculateNewCoordinate(World world,double dir_x,double dir_y,double speed,double delta){
	  if (dir_x > 0)
            this.face_left = false;
        else if (dir_x < 0)
            this.face_left = true;

		// Move the monster automtic, as a multiple of delta * speed
         double amount =delta*speed;
  		 double new_x = posx + dir_x * amount;
         double new_y = posy + dir_y * amount;
        // Move in x first
        double x_sign = Math.signum(dir_x);
        if(!world.terrainBlocks(new_x + x_sign * width / 4, posy + height / 4) 
                && !world.terrainBlocks(new_x + x_sign * width / 4, posy - height / 4)) {
            posx = new_x;
        }
        // Then move in y
        double y_sign = Math.signum(dir_y);
        if(!world.terrainBlocks(posx + width / 4, new_y + y_sign * height / 4) 
                && !world.terrainBlocks(posx - width / 4, new_y + y_sign * height / 4)){
            posy = new_y;
        }
	}
    
    /**
	 * 绘制名称和status,名称居中并且距离bar左右各5个像素
	 * @param g
	 */
	protected void drawNameBar(Graphics g){
		//绘制状态指示图标（头上方显示角色名字和健康值）
		//Color redcolor = new Color(0xcc, 0x0E, 0x0E);

        Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp
        Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f);      // Red, transp
        
		int text_width = g.getFont().getWidth(this.name);
		float bar_width =text_width+10;
		
        int text_x = (int)(posx-text_width/2);
        int text_y = (int)(posy - RPG.NAME_BAR_HEIGHT - this.img.getHeight()/2);
        float bar_x =text_x-5;
        float bar_y= text_y; 
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, RPG.NAME_BAR_HEIGHT);
        
        int hp_bar_width = (int) (bar_width * this.getPercentage());// TODO: HP / Max-HP
        g.setColor(BAR);
        g.fillRect(bar_x, bar_y, hp_bar_width, RPG.NAME_BAR_HEIGHT);
        
        g.setColor(Color.white);
        g.drawString(this.name, text_x, text_y);
	}
}

