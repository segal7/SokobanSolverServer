package model.sokobanSolver;

import java.util.List;

import common.Level;
import strips.Plannable;
import strips.Planner;
import strips.STRIPS;

public class SokobanSolver {
	private Planner planner;
	
	public SokobanSolver(){
		this.planner = new STRIPS();
	}
	
	public String solveLevel(Level l){
		
		LevelInfo level = new LevelInfo(l);
		Plannable soko_plannable = new SokoPlannable(level);
		List<strips.Action> actions = planner.plan(soko_plannable);
		if(actions == null)
			return "can't find a solution";
		else{
			StringBuilder builder = new StringBuilder();
			for(strips.Action act : actions){
				SokoAction ac = (SokoAction)act;
				for(String s : ac.getMoveSquence()){
					for(String move : s.split("\n")){
						if(move.equals("move up"))
							builder.append("u");
						else if(move.equals("move down"))
							builder.append("d");
						else if(move.equals("move right"))
							builder.append("r");
						else if(move.equals("move left"))
							builder.append("l");
					}
				}
			}
			return builder.toString();
		}
	}
}
