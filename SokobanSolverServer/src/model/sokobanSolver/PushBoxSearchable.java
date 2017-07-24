package model.sokobanSolver;
import java.util.LinkedList;
import java.util.List;

import common.Point;
import model.data.Sokoban;
import search_lib.Action;
import search_lib.Dijkstra;
import search_lib.Searchable;
import search_lib.Searcher;
import search_lib.State;

public class PushBoxSearchable extends PushBoxReadySearchable implements Searchable<LevelInfo>{

	//private Searcher<Level> bfs;
	
	public PushBoxSearchable(LevelInfo l,int box_idx,Point target_p) throws Exception {
		super(l,box_idx,target_p);
		//this.bfs = new BFS<>(new SokoDistanceComparator(target_p));
	}

	@Override
	public List<State<LevelInfo>> getAllPossibleStates(State<LevelInfo> s) {
		
		List<State<LevelInfo>> res = new LinkedList<>();
		List<State<LevelInfo>> tmp = getAllPossibleStates(s, "right");
		if(tmp != null)
			res.addAll(tmp);
		tmp = getAllPossibleStates(s, "up");
		if(tmp != null)
			res.addAll(tmp);
		tmp = getAllPossibleStates(s, "down");
		if(tmp != null)
			res.addAll(tmp);
		tmp = getAllPossibleStates(s, "left");
		if(tmp != null)
			res.addAll(tmp);
		
		return res;
	}

	private List<State<LevelInfo>> getAllPossibleStates(State<LevelInfo> s,String direction) {
		Point box_loc = s.getState().getLvl().get_boxes().get(box_idx).get_location();
		Point soko_loc = nextPoint(box_loc, direction);
		
		NoPushSearchable nps = new NoPushSearchable(s.getState(), soko_loc);
		Searcher<LevelInfo> sokoSearcher = new Dijkstra<LevelInfo>(new SokoDistanceComparator(soko_loc));
		List<Action> move_sokoban_actions = sokoSearcher.search(nps);
		if(move_sokoban_actions == null)
			return null;
		
		State<LevelInfo> ready_state = new State<>(new BLevel(s.getState()));
		ready_state.getState().getLvl().removeObject(ready_state.getState().getLvl().get_sokobans().get(0).get_location());
		ready_state.getState().getLvl().placeObject(new Sokoban(), soko_loc);
		
		StringBuilder sb = new StringBuilder();
		for(Action a : move_sokoban_actions)
			sb.append(a.getName() + "\n");
		
		List<State<LevelInfo>> res = super.getAllPossibleStates(ready_state);
		for(State<LevelInfo> state : res){
			state.setAction(new Action(sb.toString() + state.getAction()));
			state.setCost(state.getCost()+ready_state.getCost());
		}
		
		return res;
	}
}
