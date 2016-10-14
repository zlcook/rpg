package item;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import role.RPG;

/**
* @author 周亮 
* @version 创建时间：2016年10月10日 下午8:58:08
* 类说明
* 读取和存储配置文件unit.dat中信息的类。
* 该类只会实例化一次，因为配置文件只会在游戏启动是读取一次，所以该类被设计成单例模式。
* 在World中villagers、monster的初始化数据都会调用该类的getRecords(String name)来获取。
*/
public class UnitsData {

	private Map<String,List<Record>> datacontext= new HashMap<>();
	private static UnitsData instance =null;
	public static UnitsData getInstance(String path){
		if( instance==null)
			instance=new UnitsData(path);
		return instance;
	}
	/**
	 * units.dat在在classes中的位置
	 * @param path units.dat
	 */
	private UnitsData(String path) {
		super();
		path =UnitsData.class.getClassLoader().getResource(path).getPath();
		try {
			FileReader file = new FileReader(path);
			BufferedReader br = new BufferedReader(file);
			String line = null;
			while((line=br.readLine())!=null){
				String[] its = line.split(",");
				Record record = new Record(its[0], Integer.parseInt(its[1]),  Integer.parseInt(its[2]));
				List<Record> list =datacontext.get(its[0]);
				if( list==null)
					list = new ArrayList<>();
				list.add(record);
				datacontext.put(its[0],list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("读取 "+path+" 文件出错."+e.getMessage());
		}
	}
	
	
	public  static void main(String[] args){
		UnitsData d =UnitsData.getInstance("units.dat") ;
	}
	
	public List<Record> getRecords(String rolekey){
		return datacontext.get(rolekey);
	}
	public static class Record{
		private String name;
		private int posx;
		private int posy;
		public Record(String name, int posx, int posy) {
			super();
			this.name = name;
			this.posx = posx;
			this.posy = posy;
		}
		public String getName() {
			return name;
		}
		public int getPosx() {
			return posx;
		}
		public int getPosy() {
			return posy;
		}
		
	}
}
