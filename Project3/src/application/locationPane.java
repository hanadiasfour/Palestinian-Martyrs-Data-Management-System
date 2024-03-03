package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class locationPane extends VBox {

	private Button addNew, search, delete, update, openMartyrs, add, cancel, up, next, prev;
	private TableView<Martyr> AVL1, AVL2;
	private TableView<Statistics> totTable;

	private TextField locationtxf, locationSearchtxf;
	private Label warning1, warning2, locationlbl, heading;
	private Location locate;

	public locationPane() {

		ImageView img = new ImageView("arrow.png");
		img.setRotate(180);

		next = new Button("", new ImageView("arrow.png"));
		prev = new Button("", img);
		openMartyrs = new Button("Open Martyrs");
		addNew = new Button("Add New");
		search = new Button("Search");
		delete = new Button("Delete");
		update = new Button("Update");
		up = new Button("Update Location");
		cancel = new Button("Cancel");
		add = new Button("add Location");
		locationSearchtxf = new TextField("");
		locationtxf = new TextField("");
		locationlbl = new Label("location name");
		warning1 = new Label("");
		warning2 = new Label("");
		AVL1 = new TableView<>();
		AVL2 = new TableView<>();
		totTable = new TableView<>();

		setProperties();

		// heading
		Label name = new Label("LOCATIONS");
		name.setFont(Font.font("Elephant", FontWeight.BOLD, 30));
		name.setStyle("-fx-text-fill: rgb(204, 255, 229);");

		// setting top
		HBox top = new HBox(70);
		top.setAlignment(Pos.CENTER);
		top.getChildren().addAll(prev, locationlbl, next);

		// setting buttons
		FlowPane buttons = new FlowPane();
		buttons.getChildren().addAll(addNew, update, delete, openMartyrs);
		buttons.setHgap(10);
		buttons.setVgap(15);
		buttons.setAlignment(Pos.CENTER);

		HBox searching = new HBox(10);
		searching.getChildren().addAll(locationSearchtxf, search);
		searching.setAlignment(Pos.CENTER);

		HBox statStuff = new HBox(5);
		statStuff.setAlignment(Pos.CENTER);
		statStuff.getChildren().addAll(AVL1, AVL2);

		VBox generalStat = new VBox(5);
		generalStat.getChildren().addAll(statStuff, totTable);
		generalStat.setAlignment(Pos.CENTER);

		// collects all HBoxes together
		VBox center = new VBox(25);
		center.getChildren().addAll(generalStat, buttons, searching);
		center.setPadding(new Insets(20, 0, 20, 0));

		getChildren().addAll(name, top, center, warning1);

		setSpacing(30);
		setAlignment(Pos.TOP_CENTER);
		setPadding(new Insets(30, 10, 30, 10));
		setStyle("-fx-background-color: rgb(6, 151, 122);");

	}

	public BorderPane getEditor() {
		heading = new Label("");
		heading.setFont(Font.font("Elephant", FontWeight.BOLD, 25));
		heading.setStyle("-fx-text-fill:rgb(0, 102, 51);");

		GridPane buttons = new GridPane();
		buttons.setAlignment(Pos.CENTER);
		buttons.setHgap(30);
		buttons.add(add, 0, 0);
		buttons.add(up, 2, 0);
		buttons.add(cancel, 1, 0);

		VBox vb = new VBox(20);
		vb.setPadding(new Insets(80, 10, 10, 10));
		vb.setAlignment(Pos.TOP_CENTER);
		vb.getChildren().addAll(locationtxf, buttons, warning2);

		BorderPane root = new BorderPane();
		root.setAlignment(heading, Pos.CENTER);
		root.setAlignment(vb, Pos.BOTTOM_CENTER);
		root.setPadding(new Insets(50, 20, 20, 20));
		root.setStyle("-fx-background-color: rgb(204, 255, 229);");

		root.setTop(heading);
		root.setCenter(vb);
		return root;

	}

	private void setProperties() {

		// setting button sizes
		next.setPrefWidth(50);
		next.setPrefHeight(30);
		prev.setPrefWidth(50);
		prev.setPrefHeight(30);
		addNew.setPrefWidth(80);
		addNew.setPrefHeight(30);
		search.setPrefWidth(80);
		search.setPrefHeight(30);
		delete.setPrefWidth(80);
		delete.setPrefHeight(30);
		update.setPrefWidth(80);
		update.setPrefHeight(30);
		openMartyrs.setPrefWidth(100);
		openMartyrs.setPrefHeight(30);

		add.setStyle("-fx-background-color: rgb(204, 255, 229);-fx-border-color:black;-fx-border-width: 2;");
		up.setStyle("-fx-background-color: rgb(204, 255, 229);-fx-border-color:black;-fx-border-width: 2;");
		cancel.setStyle("-fx-background-color: rgb(204, 255, 229);-fx-border-color:black;-fx-border-width: 2;");
		next.setStyle("-fx-background-color: transparent;");
		prev.setStyle("-fx-background-color: transparent;");
		addNew.setStyle("-fx-background-color: rgb(204, 255, 229);-fx-border-color:black;-fx-border-width: 1;");
		search.setStyle("-fx-background-color: rgb(204, 255, 229);-fx-border-color:black;-fx-border-width: 1;");
		update.setStyle("-fx-background-color: rgb(204, 255, 229);-fx-border-color:black;-fx-border-width: 1;");
		openMartyrs.setStyle("-fx-background-color: rgb(204, 255, 229);-fx-border-color:black;-fx-border-width: 1;");
		delete.setStyle("-fx-background-color: rgb(204, 255, 229);-fx-border-color:black;-fx-border-width: 1;");
		
		AVL1.setStyle(
				"-fx-border-color:black;-fx-border-Width:3;-fx-background-color:rgb(204, 255, 229);-fx-control-inner-background:rgb(204, 255, 229);");

		AVL2.setStyle(
				"-fx-border-color:black;-fx-border-Width:3;-fx-background-color:rgb(204, 255, 229);-fx-control-inner-background:rgb(204, 255, 229);");

		totTable.setStyle(
				"-fx-border-color:black;-fx-border-Width:3;-fx-background-color:rgb(204, 255, 229);-fx-control-inner-background:rgb(204, 255, 229);");

		warning1.setStyle("-fx-text-fill:rgb(204, 255, 229);");
		warning1.setFont(Font.font("Courier New", FontWeight.BOLD, 22));
		warning1.setPadding(new Insets(15));

		warning2.setStyle("-fx-text-fill:rgb(0, 102, 51);");
		warning2.setFont(Font.font("Courier New", FontWeight.BOLD, 22));
		warning2.setPadding(new Insets(15));

		locationlbl.setStyle(
				"-fx-border-style: solid;-fx-border-color: bLack;-fx-border-width: 2;-fx-background-color: rgb(204, 255, 229);");
		locationlbl.setScaleX(1.7);
		locationlbl.setScaleY(1.7);
		locationlbl.setFont(Font.font("Times New Roman", 20));

		locationtxf.setAlignment(Pos.CENTER);
		locationtxf.setMaxWidth(300);
		locationtxf.setMinHeight(40);
		locationtxf.setFont(Font.font(15));
		locationtxf.setStyle("-fx-background-color: rgb(204, 255, 229);-fx-border-color:black;-fx-border-width:3;");

		locationSearchtxf
				.setStyle("-fx-background-color:rgb(204, 255, 229);-fx-border-color:black;-fx-border-width:3;");

		prev.setScaleX(1.5);
		prev.setScaleY(1.5);

		next.setScaleX(1.5);
		next.setScaleY(1.5);

		AVL1.setPrefHeight(300);
		AVL1.setPrefWidth(610);
		AVL2.setPrefHeight(300);
		AVL2.setPrefWidth(610);

		totTable.setPrefHeight(150);
		totTable.setMaxWidth(610);
		

	}

	// getters & setters
	public TextField getLocationSearchtxf() {
		return locationSearchtxf;
	}

	public Label getWarning1() {
		return warning1;
	}

	public Label getWarning2() {
		return warning2;
	}

	public Button getAddNew() {
		return addNew;
	}

	public Button getCancel() {
		return cancel;
	}

	public Button getUp() {
		return up;
	}

	public TextField getLocationtxf() {
		return locationtxf;
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

	public Button getOpenMartyrs() {
		return openMartyrs;
	}

	public TableView<Martyr> getAVL1() {
		return AVL1;
	}

	public TableView<Martyr> getAVL2() {
		return AVL2;
	}

	public TableView<Statistics> getTotTable() {
		return totTable;
	}


	public Location getLocate() {
		return locate;
	}

	public void setLocate(Location locate) {
		this.locate = locate;
	}

	public Button getNext() {
		return next;
	}

	public Button getPrev() {
		return prev;
	}

	public Label getLocationlbl() {
		return locationlbl;
	}

	public Label getHeading() {
		return heading;
	}

}
