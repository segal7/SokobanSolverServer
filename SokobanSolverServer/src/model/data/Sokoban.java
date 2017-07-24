package model.data;

@SuppressWarnings("serial")
public class Sokoban extends GameObject{
	public Sokoban(){}

	@Override
	public GameObject clone() {
		Sokoban sok= new Sokoban();
		if(this.get_host() != null)
			sok.set_host(this.get_host().clone());
		else
			sok.set_host(null);
		sok.set_location(this.get_location());
		return sok;
		
	}
}
