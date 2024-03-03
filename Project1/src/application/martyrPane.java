package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class martyrPane extends VBox {
	private Button add, up, search, delete, update, addnew, back;
	private TableView<Martyr> martyrtable;
	private TextField martyrSearchingtxf, nametxf, agetxf, datetxf;
	private RadioButton Fbox, Mbox, marriedBox, singleBox;
	private VBox editor = new VBox(30);
	private Location locate;
	private Label locationlbl, editorHeading, warning1, warning2;
	private Martyr martyr;

	public martyrPane() {

		locationlbl = new Label("");
		warning1 = new Label("");
		warning2 = new Label("");
		martyrSearchingtxf = new TextField();
		nametxf = new TextField();
		agetxf = new TextField();
		datetxf = new TextField();
		singleBox = new RadioButton("Single");
		marriedBox = new RadioButton("Married");
		Mbox = new RadioButton("Male");
		Fbox = new RadioButton("Female");
		back = new Button("Back");
		add = new Button("Add");
		up = new Button("Update");
		addnew = new Button("Add New");
		search = new Button("Search");
		delete = new Button("Delete");
		update = new Button("Update");
		martyrtable = new TableView<>();

		datetxf.setPromptText("MM/DD/YYYY");
		datetxf.setStyle("-fx-prompt-text-fill: gray;");

		setPrperties();

		Label name = new Label("MARTYRS IN: ");
		name.setStyle("-fx-text-fill: rgb(204, 255, 229);");
		locationlbl.setFont(Font.font("ELEPHANT", FontWeight.BOLD, 30));
		locationlbl.setStyle("-fx-text-fill: rgb(204, 255, 229);");
		name.setFont(Font.font("ELEPHANT", FontWeight.BOLD, 30));

		HBox heading = new HBox(10);
		heading.getChildren().addAll(name, locationlbl);
		heading.setAlignment(Pos.CENTER);

		HBox searching = new HBox(10);
		searching.getChildren().addAll(martyrSearchingtxf, search);
		searching.setAlignment(Pos.CENTER);

		HBox buttons = new HBox(15);
		buttons.getChildren().addAll(addnew, update, delete);
		buttons.setAlignment(Pos.CENTER);

		getChildren().addAll(heading, martyrtable, buttons, searching, warning1);
		setSpacing(20);
		setPadding(new Insets(20, 20, 20, 20));
		makeEditor();

	}

	private void makeEditor() {
		Label namelbl = new Label("Name:");
		Label agelbl = new Label("Age:");
		Label datelbl = new Label("Date Of Death:");
		Label genderlbl = new Label("Gender:");
		Label statuslbl = new Label("Status:");
		editorHeading = new Label("Al-3inwan");
		editorHeading.setFont(Font.font("ELEPHANT", FontWeight.BOLD, 30));
		editorHeading.setStyle("-fx-text-fill:rgb(0, 102, 51);");

		namelbl.setFont(Font.font("ELEPHANT", 20));
		agelbl.setFont(Font.font("ELEPHANT", 20));
		datelbl.setFont(Font.font("ELEPHANT", 20));
		genderlbl.setFont(Font.font("ELEPHANT", 20));
		statuslbl.setFont(Font.font("ELEPHANT", 20));

		namelbl.setStyle("-fx-text-fill:rgb(0, 102, 51)");
		agelbl.setStyle("-fx-text-fill:rgb(0, 102, 51)");
		datelbl.setStyle("-fx-text-fill:rgb(0, 102, 51)");
		genderlbl.setStyle("-fx-text-fill:rgb(0, 102, 51)");
		statuslbl.setStyle("-fx-text-fill:rgb(0, 102, 51)");

		GridPane gp = new GridPane();

		gp.add(namelbl, 0, 1);
		gp.add(agelbl, 0, 2);
		gp.add(datelbl, 0, 3);
		gp.add(genderlbl, 0, 4);
		gp.add(statuslbl, 0, 5);

		gp.add(nametxf, 1, 1);
		gp.add(agetxf, 1, 2);
		gp.add(datetxf, 1, 3);

		HBox gender = new HBox(20);
		gender.getChildren().addAll(Mbox, Fbox);
		HBox status = new HBox(20);
		status.getChildren().addAll(singleBox, marriedBox);

		gp.add(gender, 1, 4);
		gp.add(status, 1, 5);

		gp.setHgap(15);
		gp.setVgap(20);
		gp.setAlignment(Pos.CENTER);

		gp.add(up, 0, 7);
		gp.add(add, 1, 7);
		gp.add(back, 2, 7);
		gp.setPadding(new Insets(0, 0, 0, 20));

		editor.getChildren().addAll(editorHeading, gp, warning2);
		editor.setStyle("-fx-background-color: rgb(204, 255, 229);");

		editor.setAlignment(Pos.CENTER);
	}

	private void setPrperties() {

		ToggleGroup statusTg = new ToggleGroup();
		ToggleGroup genderTg = new ToggleGroup();

		singleBox.setToggleGroup(statusTg);
		marriedBox.setToggleGroup(statusTg);

		Mbox.setToggleGroup(genderTg);
		Fbox.setToggleGroup(genderTg);

		addnew.setPrefWidth(80);
		addnew.setPrefHeight(30);

		search.setPrefWidth(80);
		search.setPrefHeight(30);

		delete.setPrefWidth(80);
		delete.setPrefHeight(30);

		update.setPrefWidth(80);
		update.setPrefHeight(30);

		warning1.setStyle("-fx-text-fill:rgb(204, 255, 229);");
		warning1.setFont(Font.font("Courier New", FontWeight.BOLD, 22));
		warning1.setPadding(new Insets(15));
		warning2.setStyle("-fx-text-fill:rgb(0, 102, 51);");
		warning2.setFont(Font.font("Courier New", FontWeight.BOLD, 22));
		warning2.setPadding(new Insets(15));

		martyrtable.setStyle(
				"-fx-border-color:black;-fx-border-Width:3;-fx-background-color:white;-fx-control-inner-background:rgb(204, 255, 229);");
		back.setStyle("-fx-background-color: rgb(204, 255, 229);-fx-border-color:black;-fx-border-width: 1;");
		add.setStyle("-fx-background-color: rgb(204, 255, 229);-fx-border-color:black;-fx-border-width: 1;");
		up.setStyle("-fx-background-color: rgb(204, 255, 229);-fx-border-color:black;-fx-border-width: 1;");
		addnew.setStyle("-fx-background-color: rgb(204, 255, 229);-fx-border-color:black;-fx-border-width: 1;");
		search.setStyle("-fx-background-color: rgb(204, 255, 229);-fx-border-color:black;-fx-border-width: 1;");
		delete.setStyle("-fx-background-color: rgb(204, 255, 229);-fx-border-color:black;-fx-border-width: 1;");
		update.setStyle("-fx-background-color: rgb(204, 255, 229);-fx-border-color:black;-fx-border-width: 1;");
		Mbox.setStyle("-fx-background-color: rgb(204, 255, 229);");
		Fbox.setStyle("-fx-background-color: rgb(204, 255, 229);");
		singleBox.setStyle("-fx-background-color: rgb(204, 255, 229);");
		marriedBox.setStyle("-fx-background-color: rgb(204, 255, 229);");

		setStyle("-fx-background-color: rgb(6, 151, 122);");
		nametxf.setStyle("-fx-background-color:rgb(204, 255, 229);-fx-border-color:black;-fx-border-width:2;");
		datetxf.setStyle(
				"-fx-background-color:rgb(204, 255, 229);-fx-border-color:black;-fx-border-width:2;-fx-prompt-text-fill: green;");

		martyrSearchingtxf
				.setStyle("-fx-background-color:rgb(204, 255, 229);-fx-border-color:black;-fx-border-width:3;");
		agetxf.setStyle("-fx-background-color:rgb(204, 255, 229);-fx-border-color:black;-fx-border-width:2;");

	}

	public Button getBack() {
		return back;
	}

	public Button getUp() {
		return up;
	}

	public Button getAddnew() {
		return addnew;
	}

	public Button getAdd() {
		return add;
	}

	public Button getSearch() {
		return search;
	}

	public Button getDelete() {
		return delete;
	}

	public Button getUpdate() {
		return update;
	}

	public TextField getNametxf() {
		return nametxf;
	}

	public TextField getAgetxf() {
		return agetxf;
	}

	public TextField getMartyrSearchingtxf() {
		return martyrSearchingtxf;
	}

	public RadioButton getMarriedBox() {
		return marriedBox;
	}

	public RadioButton getSingleBox() {
		return singleBox;
	}

	public Label getWarning1() {
		return warning1;
	}

	public Label getWarning2() {
		return warning2;
	}

	public TextField getDatetxf() {
		return datetxf;
	}

	public RadioButton getFbox() {
		return Fbox;
	}

	public RadioButton getMbox() {
		return Mbox;
	}

	public Location getLocate() {
		return locate;
	}

	public void setLocate(Location locate) {
		this.locate = locate;
	}

	public TableView<Martyr> getMartyrtable() {
		return martyrtable;
	}

	public Label getLocationlbl() {
		return locationlbl;
	}

	public VBox getEditor() {
		return editor;

	}

	public Label getEditorHeading() {
		return editorHeading;
	}

	public Martyr getMartyr() {
		return martyr;
	}

	public void setMartyr(Martyr martyr) {
		this.martyr = martyr;
	}

}
