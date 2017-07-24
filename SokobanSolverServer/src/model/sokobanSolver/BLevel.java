package model.sokobanSolver;

import model.data.GameObject;
import model.data.Target;

@SuppressWarnings("serial")
public class BLevel extends LevelInfo{
	public BLevel(LevelInfo l){
		super(l);
	}
	
	private String IgnoreSokoban() {
		String res = super.toString();
		GameObject sok_host = super.getLvl().get_sokobans().get(0).get_host();
		if(sok_host == null)
			return res.replace('A', ' ');
		else if (sok_host instanceof Target)
			return res.replace('A', 'o');
		return res;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof BLevel))
			return false;
		return this.IgnoreSokoban().equals(((BLevel)obj).IgnoreSokoban());
	}
}