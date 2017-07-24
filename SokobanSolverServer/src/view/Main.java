package view;
	
import javafx.application.Application;
import javafx.stage.Stage;
import model.MyServerModel;
import model.ServerModel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	View v;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxl = new FXMLLoader();
			BorderPane root = fxl.load(getClass().getResource("AdminFX.fxml").openStream());
			
			primaryStage.setTitle("sokoban solver server");
			AdminLogic al = fxl.getController();
			MyServerModel m = new MyServerModel(2138, 10);
			ViewModel vm = new ViewModel(m);
			this.v = al;
			m.addObserver(vm);
			al.setViewModel(vm);
			vm.addObserver(al);
			
			Scene scene = new Scene(root,400,300);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void stop() throws Exception {
		
		super.stop();
	}
}
