package model.sokobanSolver;

import java.util.Comparator;

import common.Point;
import search_lib.State;

public class SokoDistanceComparator implements Comparator<State<LevelInfo>> {
	
	private Point target_p;
	public SokoDistanceComparator(int x, int y) {
		this.target_p = new Point(x,y);
	}
	public SokoDistanceComparator() {
		this.target_p = new Point();
	}
	public SokoDistanceComparator(Point p){
		this.target_p = p;		
	}
	
	
	
	public Point getTarget_p() {
		return target_p;
	}
	public void setTarget_p(Point target_p) {
		this.target_p = target_p;
	}
	@Override
	public int compare(State<LevelInfo> o1, State<LevelInfo> o2) {

		Point p1 = o1.getState().getLvl().get_sokobans().get(0).get_location();
		Point p2 = o2.getState().getLvl().get_sokobans().get(0).get_location();
		int d1 = (p1.x-target_p.x)*(p1.x-target_p.x)+(p1.y-target_p.y)*(p1.y-target_p.y);
		int d2 = (p2.x-target_p.x)*(p2.x-target_p.x)+(p2.y-target_p.y)*(p2.y-target_p.y);
		int res = d1-d2;
		if(res == 0)
		{
			if(o1.getCost() > o2.getCost())
				return 1;
			if(o1.getCost() == o2.getCost())
				return 0;
			else
				return -1;
		}
		else
			return res;
	}

}
