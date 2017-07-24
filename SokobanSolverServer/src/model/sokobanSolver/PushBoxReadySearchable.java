package model.sokobanSolver;

import java.util.LinkedList;
import java.util.List;

import common.Point;
import model.data.Box;
import model.data.GameObject;
import model.data.Sokoban;
import model.data.Target;
import model.data.Wall;
import search_lib.Action;
import search_lib.Searchable;
import search_lib.State;

public class PushBoxReadySearchable implements Searchable<LevelInfo>{

	
	@SuppressWarnings("serial")
	protected class BLevel extends LevelInfo{
		public BLevel(LevelInfo l){
			super(l);
		}
		@Override
		public String toString() {
			String res = super.toString();
			GameObject sok_host = super.getLvl().get_sokobans().get(0).get_host();
			if(sok_host == null)
				return res.replace('A', ' ');
			else if (sok_host instanceof Target)
				return res.replace('A', 'o');
			return res;
		}
	}
	
	State<LevelInfo> goal;
	State<LevelInfo> initial;
	protected int box_idx;
	
	public PushBoxReadySearchable(LevelInfo l, int box_idx, Point target_p) throws Exception {
		this.box_idx = box_idx;
		BLevel level = new BLevel(l);
		for(int i = 0; i < level.getLvl().get_boxes().size(); i++)
			if(i!=box_idx){
				Point box_loc = level.getLvl().get_boxes().get(i).get_location();
				level.getLvl().removeObject(box_loc);
				level.getLvl().placeObject(new Wall(), box_loc);
			}
		initial = new State<>(level);
		BLevel copy = new BLevel(level);
		if(copy.getLvl().get_boxes().size() == 0){
			throw new Exception("no boxes in level");
		}
			
		copy.getLvl().removeObject(level.getLvl().get_boxes().get(box_idx).get_location());
		copy.getLvl().placeObject(new Box(), target_p);
		goal = new State<>(copy);
	}
	
	@Override
	public State<LevelInfo> getInitialState() {
		return initial;
	}

	@Override
	public State<LevelInfo> getGoalState() {
		return goal;
	}

	@Override
	public List<State<LevelInfo>> getAllPossibleStates(State<LevelInfo> s) {
		List<State<LevelInfo>> res = new LinkedList<>();
		List<State<LevelInfo>> tmp;
		tmp = getAllPossibleStatesByDirection(s, "left");
		if(tmp != null)
			res.addAll(tmp);
		tmp = getAllPossibleStatesByDirection(s, "up");
		if(tmp != null)
			res.addAll(tmp);
		tmp = getAllPossibleStatesByDirection(s, "right");
		if(tmp != null)
			res.addAll(tmp);
		tmp = getAllPossibleStatesByDirection(s, "down");
		if(tmp != null)
			res.addAll(tmp);
		
		return res;
	}
	public List<State<LevelInfo>> getAllPossibleStatesByDirection(State<LevelInfo> s , String direction){
		Point box_loc = s.getState().getLvl().get_boxes().get(box_idx).get_location();
		String opposite_direction;
		
		if(direction.equals("right"))
			opposite_direction = "left";
		else if(direction.equals("left"))
			opposite_direction = "right";
		else if(direction.equals("up"))
			opposite_direction = "down";
		else if(direction.equals("down"))
			opposite_direction = "up";
		else return null;
		
		Point sokoban_loc = nextPoint(box_loc, opposite_direction);
		if((s.getState().getLvl().get_layout().get(sokoban_loc) != null  //there's something in the sokoban location
		 && !(s.getState().getLvl().get_layout().get(sokoban_loc) instanceof Sokoban)) //and it's not a sokoban player
		 || s.getState().getLvl().get_layout().get(sokoban_loc) == null) //or the sokoban is not there
			return null; //then the push is not even ready
		
		//push is ready
		List<State<LevelInfo>> res = new LinkedList<>();
		
		State<LevelInfo> last_state = s;
		State<LevelInfo> another_state = new State<>(new BLevel(last_state.getState()));
		another_state.getState().movePlayer(direction);
		another_state.setAction(new Action("move " + direction));
		another_state.setCost(last_state.getCost()+1);
		another_state.setFather(last_state);
		while(!another_state.equals(last_state)){
			res.add(another_state);
			last_state = another_state;
			another_state = new State<>(new BLevel(last_state.getState()));
			another_state.getState().movePlayer(direction);
			StringBuilder sb = new StringBuilder();
			sb.append(last_state.getAction().getName());
			sb.append("\n");
			sb.append("move " + direction);
			another_state.setAction(new Action(sb.toString()));
			another_state.setCost(last_state.getCost()+1);
			another_state.setFather(last_state);
		}
		
		return res;
	}
	
	protected Point nextPoint(Point p, String toDirection){
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
}
