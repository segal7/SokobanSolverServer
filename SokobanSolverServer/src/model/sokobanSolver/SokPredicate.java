package model.sokobanSolver;

import strips.Predicate;

public class SokPredicate extends Predicate{

	public SokPredicate(String type, String id, String value) {
		super(type, id, value);
	}
	
	@Override
	public boolean contradicts(Predicate p) {
		
		if(this.satisfies(p))
			return false;
		
		if(this.getValue().equals(p.getValue())){ //same location
			if((p.getType().equals("box") && !this.getType().equals("box")) ||
			   (!p.getType().equals("box") && this.getType().equals("box"))){
				return true;
			}
			if(this.getType().equals("box") && p.getType().equals("box") && !this.getId().equals(p.getId())) //2 boxes
				return true;
		}
		
		return super.contradicts(p);
	}
}
