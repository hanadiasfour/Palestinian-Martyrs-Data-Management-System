package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainPage extends Pane {
	private Button NavLocations, load, back, save;
	private Label warning;

	public MainPage() {

		warning = new Label("");
		save = new Button("Save To File");
		back = new Button("Back");
		load = new Button("Load Info From File");
		NavLocations = new Button("Navigate Locations");

		save.setDisable(true);
		NavLocations.setDisable(true);
		load.setPrefWidth(160);
		load.setPrefHeight(35);
		NavLocations.setPrefWidth(150);
		NavLocations.setPrefHeight(35);
		save.setPrefWidth(150);
		save.setPrefHeight(35);
		load.setStyle("-fx-background-color: rgb(204, 255, 229);");
		NavLocations.setStyle("-fx-background-color: rgb(204, 255, 229);");
		save.setStyle("-fx-background-color: rgb(204, 255, 229);");

		Label heading = new Label("Palestinian\nMartyrs");
		heading.setFont(Font.font("Elephant", FontWeight.BOLD, 40));
		heading.setLayoutX(245);
		heading.setLayoutY(70);
		heading.setTextFill(Color.WHITE);

		warning.setTranslateX(460);
		warning.setTranslateY(10);
		warning.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
		warning.setTextFill(Color.RED);
		warning.setVisible(false);

		ImageView img = new ImageView("front.png");
		img.setScaleX(1);
		img.setScaleY(1);
		img.setX(0);
		img.setY(0);

		HBox vb = new HBox(10);
		vb.setSpacing(10);
		vb.setAlignment(Pos.CENTER);
		vb.getChildren().addAll(load, NavLocations, save);
		vb.setLayoutX(28);
		vb.setLayoutY(490);

		setStyle("-fx-background-color: rgb(6, 151, 122);");

		getChildren().addAll(img, vb, heading, warning);

	}



	public Button getLoad() {
		return load;
	}

	public Button getNavLocations() {
		return NavLocations;
	}

	public Button getBack() {
		return back;
	}

	public Button getSave() {
		return save;
	}

	public Label getWarning() {
		return warning;
	}

}
