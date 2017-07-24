package model.sokobanSolver;

import java.util.List;

import strips.Action;

public class SokoAction extends Action{

	List<String> moveSquence;
	
	public SokoAction(String type, String id, String value, String... sequence){
		super(type,id,value);
		
	}
	public SokoAction(String type, String id, String value) {
		super(type, id, value);
	}
	public List<String> getMoveSquence() {
		return moveSquence;
	}
	public void setMoveSquence(List<String> moveSquence) {
		this.moveSquence = moveSquence;
	}

	
}
