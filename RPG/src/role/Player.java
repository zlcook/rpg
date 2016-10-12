package role;
/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Sample Solution
 * Author: Matt Giuca <mgiuca>
 */

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import org.newdawn.slick.SlickException;

import item.Amulet;
import item.DistUtils;
import item.Elixir;
import item.Item;
import item.Sword;
import item.Tome;
import observer.MonsterAttackObserver;
import observer.PlayerAttackObservable;
import observer.PlayerAttackObserver;
import observer.PlayerChatObservable;
import observer.PlayerChatObserver;
import observer.PlayerMoveObserver;
import observer.PlayerMoveObservable;

/** The character which the user plays as.
 */
public class Player extends Unit implements PlayerMoveObservable,PlayerAttackObservable,PlayerChatObservable,MonsterAttackObserver
{
	//存放监听player移动动作的监听者。 查找用的多一点，所以使用Array
	private List<PlayerMoveObserver> observers=new ArrayList<PlayerMoveObserver>();
	//存放监听player攻击动作的监听者。
	private List<PlayerAttackObserver> attackObservers=new ArrayList<PlayerAttackObserver>();
	//存放监听player会话动作的监听者。
	private List<PlayerChatObserver> chatObservers=new ArrayList<PlayerChatObserver>();
    // Pixels per millisecond
    private static final double SPEED = 0.25;
    private LinkedHashMap<String,Item> itemsmap = new LinkedHashMap<>();
    //whether get the elixir
    private boolean elixir;
    /**
     *在(0-attack)之间随机产生攻击值
     */
    private Random rd ;
    
    /** Creates a new Player.
     * @param image_path Path of player's image file.
     * @param x The Player's starting x location in pixels.
     * @param y The Player's starting y location in pixels.
     */
    public Player(String image_path,double posx, double posy,int MAX_HP,int attack,int CoolDown,long resurgence)
        throws SlickException
    {
    	super(image_path, posx, posy,MAX_HP, attack, CoolDown,resurgence,null);
    	this.elixir = false;
    	rd= new Random();
    }
	public boolean isElixir() {
		return elixir;
	}
	@Override
	public void addMoveObserver(PlayerMoveObserver observer) {
		// TODO Auto-generated method stub
		observers.add(observer);
	}
	@Override
	public void deleteMoveObserver(PlayerMoveObserver observer) {
		// TODO Auto-generated method stub
		observers.remove(observer);
	}
	@Override
	public void notifyObservers(double posx, double posy,double delta) {
		// TODO Auto-generated method stub
		for(PlayerMoveObserver mob: observers){
			mob.action(this,posx, posy,delta);
		}
	}
	public LinkedHashMap<String,Item> getItems() {
		return itemsmap;
	}
	public Item getItem(String itemName){
		return itemsmap.get(itemName);
	}

	public void putItem(Item item){
		synchronized (this) {
			if( !itemsmap.containsKey(item.getName())){
				itemsmap.put(item.getName(), item);
				//you can also just identify item by name
				if( item instanceof Amulet){
					Amulet amulet = (Amulet) item;
					this.addMax_HP(amulet.getHp());
				}
				if( item instanceof Sword){
					Sword sword = (Sword) item;
					this.addAttack(sword.getDamage());
				}
				if(item instanceof Tome){
					Tome tome = (Tome) item;
					this.addCoolDown(tome.getCooldown());
				}
				if( item instanceof Elixir)
					this.elixir =true;
			}
		}
		
	}
	public void removeItem(String itemname){
		synchronized (this) {
			if( itemsmap.containsKey(itemname)){
				itemsmap.remove(itemname);
			}
		}
	}

	/**
	 * player produce a attack
	 * @param dir_x player移动的x方向
	 * @param dir_y player移动的y方向
	 * @param delta  delta Time passed since last frame (milliseconds).
	 */
	public void attack(int dir_x,int dir_y,int delta) {
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
	 * 开始会话，按T时会进行会话，通知监听会话的角色
	 * @param delta
	 */
	public void openChat(int delta){
		this.notifyChatObservers(this.getPosx(), this.getPosy(), delta);
	}
	/**
	 * 处理主动攻击monster的攻击行为
	 */
	@Override
	public void handleAttack(int dir_x, int dir_y, double posx, double posy, double delta, int attackValue) {
		// TODO Auto-generated method stub
		//是否活跃着
		if(this.isActive()){
			//是否在攻击范围内
			double dist = DistUtils.dist(this.getPosx(), this.getPosy(), posx, posy);
			if( dist <=50 ){
				//被攻击
				this.cutHp(attackValue);
			}
		}
	}
	/** Move the player in a given direction.
     * Prevents the player from moving outside the map space, and also updates
     * the direction the player is facing.
     * @param world The world the player is on (to check blocking).
     * @param dir_x The player's movement in the x axis (-1, 0 or 1).
     * @param dir_y The player's movement in the y axis (-1, 0 or 1).
     * @param delta Time passed since last frame (milliseconds).
     */
    public void move(World world, double dir_x, double dir_y, double delta)
    {
        // calculate  new  coordinate
    	calculateNewCoordinate(world, dir_x, dir_y, SPEED, delta);
        notifyObservers(this.getPosx(),this.getPosy(),delta);
    }
	@Override
	public void update(World world, int dir_x, int dir_y, int delta) {
		super.update(world, dir_x, dir_y, delta);
		if( this.isActive()){
			move(world, dir_x, dir_y, delta);
		}
	}
	/**
	 * 重写复活函数，让player的复活位置出现在最初位置
	 */
	@Override
	public void comeBack() {
		// TODO Auto-generated method stub
		super.comeBack();
		this.setPosx(World.PLAYER_START_X);
		this.setPosy(World.PLAYER_START_Y);
	}
	
	@Override
	public void addAttackObserver(PlayerAttackObserver observer) {
		// TODO Auto-generated method stub
		attackObservers.add(observer);
	}
	@Override
	public void delAttackObserver(PlayerAttackObserver observer) {
		// TODO Auto-generated method stub
		attackObservers.remove(observer);
	}

	@Override
	public void notifyAttackObservers(int dir_x,int dir_y,double posx, double posy, double delta,int attackValue) {
		// TODO Auto-generated method stub
		for(PlayerAttackObserver observer : attackObservers){
			observer.handleAttack( dir_x, dir_y,posx, posy, delta, attackValue);
		}
	}
	@Override
	public void addChatObserver(PlayerChatObserver observer) {
		// TODO Auto-generated method stub
		chatObservers.add(observer);
	}
	@Override
	public void deleteChatObserver(PlayerChatObserver observer) {
		// TODO Auto-generated method stub
		chatObservers.remove(observer);
	}
	@Override
	public void notifyChatObservers(double posx, double posy, double delta) {
		// TODO Auto-generated metho
		for(PlayerChatObserver observer : chatObservers){
			observer.chatAction(this,posx, posy, delta);
		}
	}
	
}
