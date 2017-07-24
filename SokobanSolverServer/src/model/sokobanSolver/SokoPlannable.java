package model.sokobanSolver;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import common.Point;
import model.data.*;
import model.data.Target;
import search_lib.BFS;
import search_lib.Searchable;
import search_lib.Searcher;
import strips.Action;
import strips.AndClause;
import strips.Plannable;
import strips.Predicate;

public class SokoPlannable implements Plannable {

	LevelInfo lvl;
	AndClause Goal;
	AndClause kb;

	public SokoPlannable(LevelInfo lvl) {
		this.lvl = lvl;

		Goal = new AndClause();
		for (Target t : lvl.getLvl().get_targets())
			Goal.add(new SokPredicate("box", "?", t.get_location().toString())); // "Box ? is at location x,y
		generateKB();
	}

	@Override
	public AndClause getGoal() {
		return Goal;
	}

	@Override
	public AndClause getKnoledgeBase() {
		return kb;
	}

	@Override
	public Set<Action> getAllActions(Predicate p) {
		Set<Action> res = new HashSet<>();
		
		if(p.getType().equals("box")){ //we need a box somewhere
			int x = Integer.parseInt(p.getValue().split(",")[0]);
			int y = Integer.parseInt(p.getValue().split(",")[1]);
			Point tp = new Point(x,y);
			for(int i = 0; i < lvl.getLvl().get_boxes().size(); i++){
				try{
				if(lvl.getLvl().get_boxes().get(i).get_host() != null && lvl.getLvl().get_boxes().get(i).get_host() instanceof Target)
					continue;
				Searcher<LevelInfo> bfs = new BFS<>(new BoxDistanceComparator(i,tp));
				Searchable<LevelInfo> searchable = new PushBoxSearchable(lvl, i, tp);
				List<search_lib.Action> move_seq = bfs.search(searchable);
				if(move_seq == null) //can't reach that box
					continue; //this action is not possible
				List<String> actionMoveSeq = new LinkedList<>();
				for(search_lib.Action a : move_seq)
					actionMoveSeq.add(a.getName());
				
				SokoAction a = new SokoAction("moveBox",""+i,p.getValue());
				a.setMoveSquence(actionMoveSeq); //the followed action to fulfill this action
				a.addEffects(new SokPredicate("box",""+i,p.getValue()));
				res.add(a);
				} catch(Exception e) { continue; } //there are no boxes in this level to move
			}
			
			for(int i = 0; i < lvl.getLvl().get_boxes().size(); i++){
				try{
					if(lvl.getLvl().get_boxes().get(i).get_host() != null && lvl.getLvl().get_boxes().get(i).get_host() instanceof Target){
						Searcher<LevelInfo> bfs = new BFS<>(new BoxDistanceComparator(i,tp));
						Searchable<LevelInfo> searchable = new PushBoxSearchable(lvl, i, tp);
						List<search_lib.Action> move_seq = bfs.search(searchable);
						if(move_seq == null) //can't reach that box
							continue; //this action is not possible
						List<String> actionMoveSeq = new LinkedList<>();
						for(search_lib.Action a : move_seq)
							actionMoveSeq.add(a.getName());
						
						SokoAction a = new SokoAction("moveBox",""+i,p.getValue());
						a.setMoveSquence(actionMoveSeq); //the followed action to fulfill this action
						a.addEffects(new SokPredicate("box",""+i,p.getValue()));
						res.add(a);
					}
				}catch (Exception e) { continue;}
			}
		}
		if(res.isEmpty())
			return null;
		return res;
	}

	@Override
	public Action getAction(Predicate p) {
		Set<Action> actions = getAllActions(p);
		if(actions == null)
			return null;
		return actions.iterator().next();
	}

	public void generateKB() {
		kb = new AndClause();			
		for(int i = 0; i < lvl.getLvl().get_boxes().size(); i++)
			kb.add(new SokPredicate("box", "" + i, lvl.getLvl().get_boxes().get(i).get_location().toString())); //boxes positions
		for(Wall w : lvl.getLvl().get_walls())
			kb.add(new SokPredicate("wall","",w.get_location().toString())); //walls positions
		kb.add(new SokPredicate("sokoban", "", lvl.getLvl().get_sokobans().get(0).get_location().toString())); //sokoban position
		for (int i = 0; i < lvl.getLvl().getXEdge(); i++)
			for (int j = 0; j < lvl.getLvl().getYEdge(); j++) {
				Point pos = new Point(i, j);
				GameObject obj = lvl.getLvl().get_layout().get(pos);
				if (!lvl.getLvl().get_boxes().contains(obj) && !lvl.getLvl().get_walls().contains(obj))
					kb.add(new SokPredicate("clear", "", pos.toString()));
			}
		
	}
	@Override
	public void updateKB(Action a) {
		if(!(a instanceof SokoAction))
			Plannable.super.updateKB(a);
		else{
			SokoAction ac = (SokoAction)a;
			for(String s : ac.getMoveSquence()){
				for(String s2 : s.split("\n"))
					lvl.movePlayer(s2.split(" ")[1]);
			}
			this.generateKB();
		}
	}
}
