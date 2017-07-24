package model.data;

@SuppressWarnings("serial")
public class Wall extends GameObject {
	public Wall(){}

	@Override
	public GameObject clone() {
		Wall W= new Wall();
		if(this.get_host() != null)
			W.set_host(this.get_host().clone());
		else
			W.set_host(null);
		W.set_location(this.get_location());
		return W;
		
	}
}
