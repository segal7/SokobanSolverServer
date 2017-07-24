package model.sokobanSolver;

import common.Level;
import common.Point;
import model.policy.*;
import model.data.GameObject;
import model.data.Sokoban;
import model.data.TextLevelDisplayer;


public class LevelInfo {
	// data members:
	private Level lvl;

	private Policy _policy;
	

	// Default contractor
	public LevelInfo(Level l){
		lvl = l;
		_policy = new MySokobanPolicy();
	}
	
	public LevelInfo() {
		lvl = new Level();
		_policy = new MySokobanPolicy();
	}

	public LevelInfo(LevelInfo other) {
		this.lvl = new Level(other.lvl);
		this._policy = other._policy;
	}


	public Level getLvl() {
		return lvl;
	}

	public void setLvl(Level lvl) {
		this.lvl = lvl;
	}

	public Policy get_policy() {
		return _policy;
	}

	public void set_policy(Policy _policy) {
		this._policy = _policy;
	}

	public void movePlayer(String direction){
		Sokoban s = this.lvl.get_sokobans().get(0);
		move(s,direction);
	}
	
	private boolean move(GameObject obj, String direction) {
		boolean willmove = false;

		Point currentLocation = obj.get_location();
		Point nextLocation = nextPoint(currentLocation, direction);
		GameObject nextObj = lvl.get_layout().get(nextLocation);

		if (_policy.canStepOn(obj, nextObj))
			willmove = true;
		else if (_policy.canPush(obj, nextObj))
			if (move(nextObj, direction))
				willmove = true;

		if (willmove) {
			this.lvl.removeObject(currentLocation);
			this.lvl.placeObject(obj, nextLocation);
		}

		return willmove;
	}

	private Point nextPoint(Point p, String toDirection){
		Point res = new Point(p.x, p.y);
		switch (toDirection.toLowerCase()) {

		case ("up"):
			res.x = p.x;
			res.y = p.y - 1;
			break;
		case ("down"):
			res.x = p.x;
			res.y = p.y + 1;
			break;
		case ("left"):
			res.x = p.x - 1;
			res.y = p.y;
			break;
		case ("right"):
			res.x = p.x + 1;
			res.y = p.y;
			break;
		default:
			return p;
		}
		return res;
	}

	
	@Override
	public String toString() {
		TextLevelDisplayer displayer = new TextLevelDisplayer();
		return displayer.display(this.lvl);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof LevelInfo))
			return false;
		return this.toString().equals(((LevelInfo)obj).toString());
	}
}
