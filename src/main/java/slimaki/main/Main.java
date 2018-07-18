package slimaki.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import slimaki.kontrolery.mainWindowController;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/mainWindow.fxml"));
		StackPane stackPane=loader.load();
		
		mainWindowController controller = loader.getController();
		
		Scene scene=new Scene(stackPane,400,500);
		primaryStage.setScene(scene);
		
		primaryStage.setTitle("Slimaki");
		primaryStage.show();
	}

}
