package assignment5;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Controller implements Initializable {
	private ObservableList<String> critterClassesList = FXCollections.observableArrayList(getCritterClasses());
	private String statsCritterClass = "Critter";
	private TextArea textArea = new TextArea();
	
	@FXML
	private Button display;
	
	@FXML
	private Button stats;
	
	@FXML
	private Button addCritter;
	
	@FXML
	private Button timeStep;
	
	@FXML
	private Button quit;
	
	@FXML
	private Button setSeed;
	
	@FXML
	private Slider critSlider;
	
	@FXML
	private Slider seedSlider;
	
	@FXML
	private Slider stepSlider;
	
	@FXML
	private TextField seed;
	
	@FXML
	private TextField numTimeSteps;
	
	@FXML
	private TextField numCritsBox;
	
	@FXML
	private DialogPane errorOutput;
	
	@FXML
	private ChoiceBox crittersToMake;
	
	@FXML
	private ChoiceBox statsCritters;
	
	@Override
	public void initialize(URL fxmlFileLoction, ResourceBundle resources) {
		crittersToMake.setItems(critterClassesList);
		statsCritters.setItems(critterClassesList);
		
		
		Stage statsStage = new Stage();
		textArea.setEditable(false);
		textArea.setWrapText(true);
		StackPane layout = new StackPane();
		layout.getChildren().add(textArea);
		Scene statsScene = new Scene(layout);
		statsStage.setScene(statsScene);
		statsStage.show();
		
		critSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
				numCritsBox.setText(((Integer)newValue.intValue()).toString());
			}
		});
		
		stepSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
				numTimeSteps.setText(((Integer)newValue.intValue()).toString());
			}
		});
		
		seedSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
				seed.setText(((Integer)newValue.intValue()).toString());
			}
		});
		
		display.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Critter.displayWorld();
			}
		});
		
		stats.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				statsCritterClass = (String) statsCritters.getValue();
				stats();
			}
		});

		addCritter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				errorOutput.setContentText("");
				Integer nCrits = Integer.parseInt(numCritsBox.getText());
				for (int i = 0; i < nCrits; i++)
					try {
						Critter.makeCritter((String) crittersToMake.getValue());
					} catch (InvalidCritterException e) {
						errorOutput.setContentText("Please select a critter type.");
					}
			}
		});
		
		timeStep.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				errorOutput.setContentText("");
				for (int i =0; i < Integer.parseInt(numTimeSteps.getText()); i++) {
					try {
						Critter.worldTimeStep();
						stats();
					} catch (InvalidCritterException e) {
						errorOutput.setContentText("Error executing timestep.");
					}
				}
				Critter.displayWorld();
				
				
			}
		});
		
		setSeed.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Critter.setSeed(Integer.parseInt(seed.getText()));
			}
		});
		
		quit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
		
	}
	
	private ArrayList<String> getCritterClasses() {
		ArrayList<String> critterClasses = new ArrayList<String>();
		File dir = new File("assignment5/assignment5");
		File[] fileList = dir.listFiles();
		
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isFile()) {
				String fileName = fileList[i].getName();
				if (fileName.endsWith(".java")) {
					fileName = fileName.substring(0, fileName.length() - 5);
				} else {
					continue;
				}
				try {
					Class<?> parentCritter = Class.forName("assignment5.Critter");
					Class<?> critter = Class.forName("assignment5" + "." + fileName);
					if (parentCritter.isAssignableFrom(critter)) {
						critterClasses.add(fileName);
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return critterClasses;
	}
	
	private void stats() {
		try {
			errorOutput.setContentText("");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			PrintStream old = System.out;
			System.setOut(ps);
			java.util.List<Critter> instances = null;
			instances = Critter.getInstances(statsCritterClass);	
        	Class<?>[] types = {java.util.List.class};
        	Class<?> c = Class.forName("assignment5" + "." + statsCritterClass);
        	Method runStats = c.getMethod("runStats", types);
        	runStats.invoke(null, instances);
        	textArea.clear();
        	textArea.appendText(baos.toString());
        	System.out.flush();
        	System.setOut(old);
		} catch(Exception e) {
			errorOutput.setContentText("Please select a critter type.");
			
		}
	}
	
}
