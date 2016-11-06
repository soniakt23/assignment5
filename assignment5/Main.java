package assignment5;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
	static GridPane grid = new GridPane();
	static Stage displayStage = new Stage();


	@Override
	public void start(Stage primaryStage) {
		try {
			Stage controllerStage = new Stage();
			AnchorPane controllerPane = (AnchorPane) FXMLLoader.load(Main.class.getResource("Controller.fxml"));
			Scene controllerScene = new Scene(controllerPane);
			grid.setGridLinesVisible(true);

			//Scene scene = new Scene(grid, 500, 500);
			controllerStage.setScene(controllerScene);
			
			controllerStage.show();
			

			
			// Paints the icons.
			Painter.paint();
			
		} catch(Exception e) {
			e.printStackTrace();		
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
