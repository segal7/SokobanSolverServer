package model.data;

@SuppressWarnings("serial")
public class Box extends GameObject {
	
	
	public Box(){
		
	}

	@Override
	public GameObject clone() {
		Box b = new Box();
		if(this.get_host() != null)
			b.set_host(this.get_host().clone());
		else
			b.set_host(null);
		
		b.set_location(this.get_location());
		return b;
	}	 
		
	 
}
