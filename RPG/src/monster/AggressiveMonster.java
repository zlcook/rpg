package monster;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import item.DistUtils;
import observer.MonsterAttackObservable;
import observer.MonsterAttackObserver;
import observer.PlayerAttackObserver;
import observer.PlayerMoveObserver;
import role.Player;
import role.Unit;
import role.World;

/**
* @author 周亮 
* @version 创建时间：2016年10月9日 下午2:49:28
* 类说明
* 描述主动怪兽信息的类。
* 该类监听了player的移动、攻击动作，当player作出相应动作时从而做出反应。
*/
public class AggressiveMonster extends Unit implements PlayerMoveObserver ,MonsterAttackObservable,PlayerAttackObserver{

	private static double SPEED = 0.25;
	private int dir_y=0;
	private int dir_x=0;
	/**
	 * 是否处于战斗状态，距离player小于等于50像素为战斗状态。
	 * 战斗状态player进行攻击。
	 */
	private boolean isCombat= false;
	/**
	 * 移动状态，距离player在50-150之间（包含)时进入移动状态，monster会靠近player.超过150则原地不动。
	 * 移动状态不会攻击玩家。
	 * 战斗状态和移动状态是分开的，战斗时说明距离够了就不会移动，移动时说明距离不够就不会战斗。
	 */
	private boolean isMove=false;
	//存放监听aggressive Monster攻击动作的监听者。
	private List<MonsterAttackObserver> attackObservers=new ArrayList<MonsterAttackObserver>();
    /**
     *在(0-attack)之间随机产生攻击值
     */
    private Random rd ;
	public AggressiveMonster(String image_path, double posx, double posy, int MAX_HP, int attack, int CoolDown,
			long resurgence, String name) throws SlickException {
		super(image_path, posx, posy, MAX_HP, attack, CoolDown, resurgence, name);
		rd= new Random();
	}
	public boolean isCombat() {
		return isCombat;
	}
	public boolean isMove() {
		return isMove;
	}
	public void setMove(boolean isMove) {
		this.isMove = isMove;
	}
	public void setCombat(boolean isCombat) {
		this.isCombat = isCombat;
	}

	/**
	 * 监听玩家移动行为
	 * 距离玩家150进入移动状态：即向玩家靠近
	 * 距离玩家50像素进入战斗状态：即可以攻击玩家
	 */
	@Override
	public void action(Player player, double posx, double posy, double delta) {
		// TODO Auto-generated method stub
		if( this.isActive()&&player.isActive()){
			double dist = DistUtils.dist(this.getPosx(), this.getPosy(), posx, posy);
			if( dist>150 || dist<0){
				//禁止状态
				this.setMove(false);
				this.setCombat(false);
			}else if( dist<=150 && dist>50 ){//30表示怪兽可以主动靠近player 30像素，如果为0则表示怪兽可以靠近player的距离为0像素
				//移动状态
				this.setMove(true);
				if( dist<=50)
					this.setCombat(true);
				else
					this.setCombat(false);
				//this.setCombat(false);
			}else{
				//进入战斗状态
				this.setCombat(true);
				this.setMove(false);
			}
		}else{
			this.setCombat(false);
			this.setMove(false);
		}
	}
	/**
	 * 发起攻击
	 */
	public void attack(double delta){
		//判断是否可以攻击
		if( this.getCoolDownTime()<=0){
			//在0-attack之间随机产生攻击值
			int attValue = rd.nextInt(this.getAttack());
		    //通知监听player攻击动作的监听者
			this.notifyAttackObservers( dir_x, dir_y,this.getPosx(), this.getPosy(), delta,attValue);
			//冷却
			this.startCoolDownTime();
		}
	}
	/**
	 * 监听player攻击行为
	 */
	@Override
	public void handleAttack(int dir_x, int dir_y, double posx, double posy, double delta, int attackValue) {
		// TODO Auto-generated method stub
		if( this.isActive()){
			//距离
			double dist = DistUtils.dist(this.getPosx(), this.getPosy(), posx, posy);
			if( dist<=50 ){
				//受伤
				this.cutHp(attackValue);
			}
		}
	}
	public void move( World world ,Player player,double delta){
		calculateAttackMonsterCoordinate(world, SPEED, delta,player.getPosx(),player.getPosy(),false);
	}
	
	public void update(World world, int dir_x, int dir_y, int delta,Player player) {
		super.update(world, dir_x, dir_y, delta);
		
		//活跃并且处于战斗状态进行攻击
		if( this.isActive()&&player.isActive()){
			//战斗
			if(this.isCombat())
			   attack(delta);
			//移动
			if(this.isMove())
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
	 * 复活时战斗状态为false
	 */
	@Override
	public void comeBack() {
		// TODO Auto-generated method stub
		super.comeBack();
		this.setCombat(false);
	}

	@Override
	public void addAttackObserver(MonsterAttackObserver observer) {
		// TODO Auto-generated method stub
		attackObservers.add(observer);
	}
	@Override
	public void delAttackObserver(MonsterAttackObserver observer) {
		// TODO Auto-generated method stub
		attackObservers.remove(observer);
	}
	@Override
	public void notifyAttackObservers(int dir_x,int dir_y,double posx, double posy, double delta,int attackValue) {
		// TODO Auto-generated method stub
		for(MonsterAttackObserver observer : attackObservers){
			observer.handleAttack( dir_x, dir_y,posx, posy, delta, attackValue);
		}
	}

}
