package model.sokobanSolver;

import java.util.LinkedList;
import java.util.List;

import common.Point;
import model.data.Sokoban;
import model.data.Wall;
import search_lib.Action;
import search_lib.Searchable;
import search_lib.State;

public class NoPushSearchable implements Searchable<LevelInfo>{

	State<LevelInfo> initial;
	State<LevelInfo> goal;
	
	public NoPushSearchable(LevelInfo lvl,Point p) {
		LevelInfo copy = new LevelInfo(lvl);
		for(int i = 0; i < copy.getLvl().get_boxes().size(); i++){
			Point tmp_p = copy.getLvl().get_boxes().get(i).get_location();
			copy.getLvl().removeObject(tmp_p);
			copy.getLvl().placeObject(new Wall(), tmp_p);
		}
		
		initial = new State<>(copy);
		
		LevelInfo copy2 = new LevelInfo(copy);
		copy2.getLvl().removeObject(copy2.getLvl().get_sokobans().get(0).get_location());
		copy2.getLvl().placeObject(new Sokoban(), p);
		goal = new State<>(copy2);
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
		
		State<LevelInfo> tmp = getPossibleState(s,"up");
		res.add(tmp);
		tmp = getPossibleState(s,"down");
		if(!res.contains(tmp))
			res.add(tmp);
		tmp = getPossibleState(s,"right");
		if(!res.contains(tmp))
			res.add(tmp);
		tmp = getPossibleState(s,"left");
		if(!res.contains(tmp))
			res.add(tmp);
		
		if(res.isEmpty())
			return null;
		return res;
	}

	private State<LevelInfo> getPossibleState(State<LevelInfo> s ,String direction){
		LevelInfo next_lvl = new LevelInfo(s.getState());
		next_lvl.movePlayer(direction);
		State<LevelInfo> next_state = new State<>(next_lvl);
		next_state.setCost(1+s.getCost());
		next_state.setFather(s);
		next_state.setAction(new Action("move "+direction));
		
		return next_state;
	}
}
