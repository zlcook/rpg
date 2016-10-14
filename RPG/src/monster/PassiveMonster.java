package monster;
import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import item.DistUtils;
import observer.PlayerAttackObserver;
import role.Player;
import role.Unit;
import role.World;

/**
* @author 周亮 
* @version 创建时间：2016年10月9日 下午2:02:56
* 类说明
* 描述被动怪兽信息的类。
* 该类监听了player的攻击动作，当player作出相应动作时从而做出反应。
*/
public class PassiveMonster extends Unit implements PlayerAttackObserver{
	private static double SPEED = 0.2;
	
	/**
	 * 是否安全，安全则可以自由移动，不安全时会逃离攻击者
	 */
	private boolean safe=true;
	/**
	 * 改变方向计时器，3秒换一次方向。in mills
	 */
	private long directChagetimer =0L;
	/**
	 * 不安全期计时器。从最后一次攻击时间到当前时间之间的间隔毫秒数，大于5秒则安全，in mills
	 */
	private long unsafePeriodTimer=0L;
	// move in the direction current.
	private int dir_y=0;
	private int dir_x=0;
	private Random dirRdm ;
	public PassiveMonster(String image_path,double posx, double posy,int MAX_HP,int attack,int CoolDown,long resurgence,String name)
	        throws SlickException
	{
			super(image_path, posx, posy, MAX_HP, attack, CoolDown,resurgence,name);
	    	dirRdm = new Random(1);
	}
	public void move( World world ,Player player,double delta){
		/**
		 * 未移动前的坐标
		 */
		double pre_x= this.getPosx();
		double pre_y= this.getPosy();
		// calculate  new  coordinate
		//calculateNewCoordinate(world, dir_x, dir_y, SPEED, delta,player.getPosx(),player.getPosy());
		
		//安全时随机移动
		if(safe){
			calculateNewCoordinate(world, dir_x, dir_y, SPEED,delta);
			
			//get the direction
			if(directChagetimer ==0L){
				//change direction
				dir_y=dirRdm.nextInt(3)-1;// -1 to 1
				dir_x=dirRdm.nextInt(3)-1;// -1 to 1
				directChagetimer+=delta;
			}else{
				directChagetimer+=delta;
				long seconds = directChagetimer/1000;
				if( seconds>=3)
					directChagetimer=0L;
			}
		}else{   
			//不安全期逃离player
			calculateAttackMonsterCoordinate(world, SPEED, delta,player.getPosx(),player.getPosy(),true);
			
			//如果villager没有度过安全期，还是不安全的
			unsafePeriodTimer+=delta;
			long seconds = unsafePeriodTimer/1000;
			if( seconds >=5)//villager渡过5秒安全期,变成安全状态，可以自由移动
				safe = true;
			
			/*
			 * 对比前后位移是否变化从而判断是否遇到障碍物，因为逃离时的dir_x和dir_y不会取0，所以除了遇到障碍物外不可能原地不动
			        不安全期如果遇到障碍物要自动更换方向,位置没有变则遇到障碍物
			*/
			if(pre_x == this.getPosx() && pre_y==this.getPosy())
			{
				dir_x=dirRdm.nextInt(3)-1;// -1 to 1
				dir_y=dirRdm.nextInt(3)-1;// -1 to 1
				calculateNewCoordinate(world, dir_x, dir_y, SPEED,delta);
			}	
		}
	}

	public void update(World world, int dir_x, int dir_y, int delta,Player player) {
		super.update(world, dir_x, dir_y, delta);
		//1.此处使用!this.isDieOut()方法可以让没有灭绝的角色的复活地点不在原地 。
		if(this.isActive()){
			move(world, player, delta);
		}
	}
	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		super.render(g);
		if( this.isActive()){
			//绘制状态指示图标（头上方显示角色名字和健康值）
			this.drawNameBar(g);
		}
	}
	/**
	 * 监听player攻击行为
	 */
	@Override
	public void handleAttack(int dir_x,int dir_y,double posx, double posy,double delta,int attackValue){
		// TODO Auto-generated method stub
		//如果角色没灭绝，被玩家攻击
		if( this.isActive()){
			//计算和player的距离
			double dist = DistUtils.dist(this.getPosx(), this.getPosy(), posx, posy);
			if( dist<=50){//在被攻击范围
				//不安全
				safe = false;
				//减少hp值
				this.cutHp(attackValue);
				//如果被打死则进入安全期
				if(this.isDieout()){
					safe = true;
				}else{
					//不安全期计时器设为0
					unsafePeriodTimer=0L;
					/**
					 * 改变移动方向，逃离player
					 * bug：
					 * player攻击时才改变方向，如果在该方向上遇到障碍就会停止，只有等到player再次攻击时才改变方向。
					 * 在不安全期内如果player不攻击就会一直停在那里，直达渡过不安全期才会恢复自由移动。
					 * 
					 * 已修复：在不安全期间遇到障碍物时就自动改变方向。
					 */
					/*if( dir_x==0 )
						this.dir_x=(dirRdm.nextInt(2)==0?-1:1);
					else
						this.dir_x=dir_x;
					if( dir_y==0 )
						this.dir_y=(dirRdm.nextInt(2)==0?-1:1);
					else
						this.dir_y=dir_y;*/
				}
				
			}
		}
			
	}
}
