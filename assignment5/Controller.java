/* CRITTERS Controller.java
 * EE422C Project 5 submission by
 * Replace <...> with your actual data.
 * Mary Gwozdz
 * mlg3646
 * 16450
 * Sonia Taneja
 * skt638
 * 16445
 * Slip days used: <0>
 * Fall 2016
 */

package assignment5;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller implements Initializable {
	private ObservableList<String> critterClassesList = FXCollections.observableArrayList(getCritterClasses());
	private ObservableList<String> statsClassesList = FXCollections.observableArrayList(getStatsCritterClasses());
	private TextArea textArea = new TextArea();
	private ToggleGroup group = new ToggleGroup();
	private boolean selected = false;
	
	Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			for (int i = 0; i < Integer.parseInt(numFrames.getText()); i++) { //user choses number of frames
				//Call worldTimeStep
				try {
					Critter.worldTimeStep();
				} catch (InvalidCritterException e) {
					e.printStackTrace();
				}
			}
			//display world
			Critter.displayWorld();
			
			//display runstats
			stats();

		}
	}));
	
	
	@FXML
	private Button display;
	
	@FXML
	private Button animation;
	
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
	private Slider frameSlider;
	
	@FXML
	private TextField numFrames;
	
	@FXML
	private TextField seed;
	
	@FXML
	private TextField numTimeSteps;
	
	@FXML
	private TextField numCritsBox;
	
	@FXML
	private ChoiceBox crittersToMake;
	
	@FXML
	private ChoiceBox statsCritters;
	
	@FXML
	private ToggleButton toggleButton;
	
	@Override
	public void initialize(URL fxmlFileLoction, ResourceBundle resources) {
		crittersToMake.setItems(critterClassesList);
		statsCritters.setItems(statsClassesList);
		crittersToMake.setValue("Craig");
		statsCritters.setValue("Critter");
		
		Stage statsStage = new Stage();
		textArea.setEditable(false);
		textArea.setWrapText(true);
		StackPane layout = new StackPane();
		layout.getChildren().add(textArea);
		Scene statsScene = new Scene(layout);
		statsStage.setScene(statsScene);
		statsStage.show();
		
		toggleButton.setToggleGroup(group);
		toggleButton.selectedProperty().addListener((p, o, n) -> {
			selected = !selected;
			if (selected) {
				//Gray out other buttons
				display.setDisable(true);
				stats.setDisable(true);
				addCritter.setDisable(true);
				timeStep.setDisable(true);
				setSeed.setDisable(true);
				critSlider.setDisable(true);
				seedSlider.setDisable(true);
				stepSlider.setDisable(true);
				numTimeSteps.setDisable(true);
				seed.setDisable(true);
				numCritsBox.setDisable(true);
				statsCritters.setDisable(true);
				crittersToMake.setDisable(true);
				
				timeline.setCycleCount(Animation.INDEFINITE);
				timeline.play();
			} else {
				timeline.stop();
				
				display.setDisable(false);
				stats.setDisable(false);
				addCritter.setDisable(false);
				timeStep.setDisable(false);
				setSeed.setDisable(false);
				critSlider.setDisable(false);
				seedSlider.setDisable(false);
				stepSlider.setDisable(false);
				numTimeSteps.setDisable(false);
				seed.setDisable(false);
				numCritsBox.setDisable(false);
				statsCritters.setDisable(false);
				crittersToMake.setDisable(false);
			}
		});
		
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
		
		frameSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
				numFrames.setText(((Integer)newValue.intValue()).toString());
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
				stats();
			}
		});

		addCritter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Integer nCrits = Integer.parseInt(numCritsBox.getText());
				for (int i = 0; i < nCrits; i++)
					try {
						Critter.makeCritter((String) crittersToMake.getValue());
					} catch (InvalidCritterException e) {
						e.printStackTrace();
					}
			}
		});
		
		timeStep.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for (int i =0; i < Integer.parseInt(numTimeSteps.getText()); i++) {
					try {
						Critter.worldTimeStep();
						stats();
					} catch (InvalidCritterException e) {
						e.printStackTrace();
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
		
		critterClasses.remove("Critter");
		return critterClasses;
	}
	
	private ArrayList<String> getStatsCritterClasses() {
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
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			PrintStream old = System.out;
			System.setOut(ps);
			java.util.List<Critter> instances = null;
			instances = Critter.getInstances((String) statsCritters.getValue());	
        	Class<?>[] types = {java.util.List.class};
        	Class<?> c = Class.forName("assignment5" + "." + (String) statsCritters.getValue());
        	Method runStats = c.getMethod("runStats", types);
        	runStats.invoke(null, instances);
        	textArea.clear();
        	textArea.appendText(baos.toString());
        	System.out.flush();
        	System.setOut(old);
		} catch(Exception e) {
			e.printStackTrace();			
		}
	}
}


