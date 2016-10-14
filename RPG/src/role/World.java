package role;
/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Sample Solution
 * Author: Matt Giuca <mgiuca>
 */

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import item.Amulet;
import item.Elixir;
import item.Item;
import item.Sword;
import item.Tome;
import item.UnitsData;
import item.UnitsData.Record;
import monster.AggressiveMonster;
import monster.PassiveMonster;

/** Represents the entire game world.
 * (Designed to be instantiated just once for the whole game).
 */
public class World
{
	/**
	 * 被动攻击者复活等待时间
	 * 等待复活时间小于0表示角色不可以复活，一旦hp<=0，则直接灭绝。in mills
	 * 等待复活时间大于0表示角色可以复活，一旦hp<=0，则角色进入复活等待时间，等待时间到后角色进行原地复活。
	 */
	private static final long PassiveMonsterResurgence=-1L;
	/**
	 * 主攻击者复活等待时间,为-l表示不可以复活，一旦hp不大于0，则直接灭绝。in mills
	 */
	private static final long AggressiveMonsterResurgence=-1L;
	/**
	 * 玩家复活等待时间,一旦hp不大于0，玩家可以。in mills
	 */
	private static final long PlayerResurgence=3000L;
    public static final int PLAYER_START_X = 756, PLAYER_START_Y = 684;
    private static final int STATUS_PANEL_X=PLAYER_START_X-RPG.SCREEN_WIDTH/2;
    private static final int STATUS_PANEL_Y=PLAYER_START_Y+RPG.SCREEN_HEIGHT/2-RPG.STATUS_PANEL_HEIGTH;
    private Player player = null;
    //villager
    private List<Villagers> villagers=null;
    
    private Item[] items=null;
    /**
     * 被动怪兽
     */
    private PassiveMonster[] passiveMs;
    //主动怪兽
    private List<AggressiveMonster> aggressiveMs=null;
    
    private StatusPanel statusP;
    
    private TiledMap map = null;
    private Camera camera = null;
    private UnitsData unitData;

    /** Map width, in pixels. */
    private int getMapWidth()
    {
        return map.getWidth() * getTileWidth();
    }

    /** Map height, in pixels. */
    private int getMapHeight()
    {
        return map.getHeight() * getTileHeight();
    }

    /** Tile width, in pixels. */
    private int getTileWidth()
    {
        return map.getTileWidth();
    }

    /** Tile height, in pixels. */
    private int getTileHeight()
    {
        return map.getTileHeight();
    }

    /** Create a new World object. */
    public World()
    throws SlickException
    {
    	unitData =UnitsData.getInstance("units.dat") ;
        map = new TiledMap(RPG.ASSETS_PATH + "/map.tmx", RPG.ASSETS_PATH);
        
        player = new Player(RPG.ASSETS_PATH + "/units/player.png", PLAYER_START_X, PLAYER_START_Y,100,26,600,PlayerResurgence);
        
        /**
         * 初始化村民
         */
        initVillager(unitData,player);
        
		items = new Item[4];
		
		items[0] = new Amulet(RPG.ASSETS_PATH+"/items/amulet.png",965,3563,"amulet");
		items[1] = new Sword(RPG.ASSETS_PATH+"/items/sword.png",546,6707,"sword");
		items[2] = new Tome(RPG.ASSETS_PATH+"/items/tome.png",4791,1253,"tome");
		items[3] = new Elixir(RPG.ASSETS_PATH+"/items/elixir.png",1976,402,"elixir");
        
		  /**
         * 初始化被动怪兽
         */
        initPassiveMonster(unitData,player);  
        /**
         * 初始化主动怪兽
         */
        initAggressiveMonster(unitData,player);
        statusP=new StatusPanel(RPG.ASSETS_PATH+"/panel.png" ,STATUS_PANEL_X, STATUS_PANEL_Y, RPG.SCREEN_WIDTH, RPG.STATUS_PANEL_HEIGTH,player);
        
        
        player.addMoveObserver(items[0]);
        player.addMoveObserver(items[1]);
        player.addMoveObserver(items[2]);
        player.addMoveObserver(items[3]);
        
        player.addMoveObserver(statusP);
        
        camera = new Camera(player, RPG.SCREEN_WIDTH, RPG.SCREEN_HEIGHT-RPG.STATUS_PANEL_HEIGTH);
        
    }
    /**
     * 初始化村民
     */
    private void initVillager(UnitsData unitData2, Player player2) throws SlickException {
    	 villagers=new ArrayList<>();
        List<Record> records =unitData.getRecords("PrinceAldric");
     	if( records!=null){
     		for( int i =0;i < records.size();i++){
     			Record rec = records.get(i);
     			Villagers vl = new Villagers(RPG.ASSETS_PATH+"/units/prince.png",  rec.getPosx(), rec.getPosy(),1,0,0,rec.getName()); 
     		player2.addChatObserver(vl);
     			villagers.add(vl);
     		}
     	}
     	records =unitData.getRecords("Elvira");
      	if( records!=null){
      		for( int i =0;i < records.size();i++){
      			Record rec = records.get(i);
      			Villagers vl = new Villagers(RPG.ASSETS_PATH+"/units/shaman.png",  rec.getPosx(), rec.getPosy(),1,0,0,rec.getName()); 
      			player2.addChatObserver(vl);
      			villagers.add(vl);
      		}
      	}
      	records =unitData.getRecords("Garth");
      	if( records!=null){
      		for( int i =0;i < records.size();i++){
      			Record rec = records.get(i);
      			Villagers vl = new Villagers(RPG.ASSETS_PATH+"/units/peasant.png",  rec.getPosx(), rec.getPosy(),1,0,0,rec.getName()); 
      			player2.addChatObserver(vl);
      			villagers.add(vl);
      		}
      	}
	}
    /**
     * 初始化主动怪兽
     */
	private void initAggressiveMonster(UnitsData unitData2, Player player2) throws SlickException {
    	aggressiveMs=new ArrayList<AggressiveMonster>();
		// TODO Auto-generated method stub
    	List<Record> records =unitData.getRecords("Zombie");
    	if( records!=null){
    		for( int i =0;i < records.size();i++){
    			Record rec = records.get(i);
    			AggressiveMonster am = new AggressiveMonster(RPG.ASSETS_PATH+"/units/zombie.png",  rec.getPosx(), rec.getPosy(), 60, 10, 800, AggressiveMonsterResurgence, rec.getName());
    			am.addAttackObserver(player2);
    			player2.addAttackObserver(am);
    			player2.addMoveObserver(am);
    			aggressiveMs.add(am);
    		}
    	}
    	List<Record> bandits =unitData.getRecords("Bandit");
    	if( bandits!=null){
    		for( int i =0;i < bandits.size();i++){
    			Record rec = bandits.get(i);
    			AggressiveMonster am = new AggressiveMonster(RPG.ASSETS_PATH+"/units/bandit.png",  rec.getPosx(), rec.getPosy(), 40, 8, 200, AggressiveMonsterResurgence,  rec.getName());
    			am.addAttackObserver(player2);
    			player2.addAttackObserver(am);
    			player2.addMoveObserver(am);
    			aggressiveMs.add(am);
    		}
    	}
    	List<Record> skeletons =unitData.getRecords("Skeleton");
    	if( skeletons!=null){
    		for( int i =0;i < skeletons.size();i++){
    			Record rec = skeletons.get(i);
    			AggressiveMonster am = new AggressiveMonster(RPG.ASSETS_PATH+"/units/skeleton.png",  rec.getPosx(), rec.getPosy(), 100, 16, 500, AggressiveMonsterResurgence,  rec.getName());
    			am.addAttackObserver(player2);
    			player2.addAttackObserver(am);
    			player2.addMoveObserver(am);
    			aggressiveMs.add(am);
    		}
    	}
    	List<Record> draelics =unitData.getRecords("Draelic");
    	if( draelics!=null){
    		for( int i =0;i < draelics.size();i++){
    			Record rec = draelics.get(i);
    			AggressiveMonster am = new AggressiveMonster(RPG.ASSETS_PATH+"/units/necromancer.png",  rec.getPosx(), rec.getPosy(), 140, 30, 400, AggressiveMonsterResurgence,  rec.getName());
    			am.addAttackObserver(player2);
    			player2.addAttackObserver(am);
    			player2.addMoveObserver(am);
    			aggressiveMs.add(am);
    		}
    	}
	}
	/**
     * 初始化被动怪兽
     */
	private void initPassiveMonster(UnitsData unitData,Player player) throws SlickException{
    	List<Record> giants =unitData.getRecords("GiantBat");
    	if( giants!=null){
    		passiveMs=new PassiveMonster[giants.size()];
    		for( int i =0;i < giants.size();i++){
    			Record rec = giants.get(i);
    			passiveMs[i]=new PassiveMonster(RPG.ASSETS_PATH+"/units/dreadbat.png", rec.getPosx(), rec.getPosy(), 40, 0, 0,PassiveMonsterResurgence, rec.getName());

    	        player.addAttackObserver(passiveMs[i]);
    		}
    	}
    }

    /** Update the game state for a frame.
     * @param dir_x The player's movement in the x axis (-1, 0 or 1).  
     * @param dir_y The player's movement in the y axis (-1, 0 or 1).
     * @param delta Time passed since last frame (milliseconds).
     */
    public void update(int dir_x, int dir_y, int delta)
    throws SlickException
    {
        player.update(this, dir_x, dir_y, delta);
        for( PassiveMonster pm: passiveMs)
        	pm.update(this, dir_x, dir_y, delta,player);
        for(AggressiveMonster am : aggressiveMs)
        	am.update(this, dir_x, dir_y, delta,player);
        for(Villagers vl : villagers)
        	vl.update(this, dir_x, dir_y, delta);
        camera.update();
    }

    /** Render the entire screen, so it reflects the current game state.
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(Graphics g)
    throws SlickException
    {
        // Render the relevant section of the map
        int x = -(camera.getMinX() % getTileWidth());
        int y = -(camera.getMinY() % getTileHeight());
        int sx = camera.getMinX() / getTileWidth();
        int sy = camera.getMinY()/ getTileHeight();
        int w = (camera.getMaxX() / getTileWidth()) - (camera.getMinX() / getTileWidth()) + 1;
        int h = (camera.getMaxY() / getTileHeight()) - (camera.getMinY() / getTileHeight()) + 1;
        map.render(x, y, sx, sy, w, h);
        
        // Translate the Graphics object
        g.translate(-camera.getMinX(), -camera.getMinY());

        // Render the player
        player.render(g);
        for( Villagers villager :villagers)
        	villager.render(g);
        for(Item item :items)
        	item.render(g);
        
        for( PassiveMonster pm: passiveMs)
        	pm.render(g);
        for(AggressiveMonster am : aggressiveMs)
        	am.render(g);
        statusP.render(g);
        
    }

    /** Determines whether a particular map coordinate blocks movement.
     * @param x Map x coordinate (in pixels).
     * @param y Map y coordinate (in pixels).
     * @return true if the coordinate blocks movement.
     */
    public boolean terrainBlocks(double x, double y)
    {
        // Check we are within the bounds of the map
        if (x < 0 || y < 0 || x > getMapWidth() || y > getMapHeight()) {
            return true;
        }
        
        // Check the tile properties
        int tile_x = (int) x / getTileWidth();
        int tile_y = (int) y / getTileHeight();
        int tileid = map.getTileId(tile_x, tile_y, 0);
        String block = map.getTileProperty(tileid, "block", "0");
        return !block.equals("0");
    }
    /**
     * 开始会话
     */
	public void openChat(int delta) {
		  player.openChat(delta);
	}
	/**
	 * player开始攻击
	 * @param dir_x player移动的x方向
	 * @param dir_y player移动的y方向
	 * @param delta  delta Time passed since last frame (milliseconds).
	 */
	public void operatePlayerAttack(int dir_x,int dir_y,int delta) {
		player.attack( dir_x, dir_y,delta);
	}
}
