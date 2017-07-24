package model.data;

@SuppressWarnings("serial")
public class Target extends GameObject {

	public Target(){}

	@Override
	public  GameObject clone() {
		Target T= new Target();
		if(this.get_host() != null)
			T.set_host(this.get_host().clone());
		else
			T.set_host(null);
		T.set_location(this.get_location());
		return T;
		
	}
	
}
