package item;

import role.Unit;
import role.World;

/**
* @author 周亮 
* @version 创建时间：2016年10月10日 上午9:49:59
* 类说明
* 距离工具类，该类提供了计算两个物体坐标之间的直线距离
*/
public class DistUtils {

	/**
	 * calc the distance between des and obj
	 * @param desx  des的x坐标
	 * @param desy  des的y坐标
	 * @param objx  obj的x坐标
	 * @param objy  obj的y坐标
	 * @return 返回两个坐标之间的直线距离
	 */
	public static double dist(double desx,double desy,double objx,double objy){
		double distx = desx-objx;
		double disty = desy-objy;
		double dist =Math.pow(( Math.pow(distx, 2)+Math.pow(disty, 2)),0.5);
		return dist;
	}

}
