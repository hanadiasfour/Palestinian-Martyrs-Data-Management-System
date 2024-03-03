package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

	// Date formatter to read/write dates in a specific form.
	private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

	// CircularDoublyLinkedList which contains the location objects
	private CircularDoublyLinkedList locationList = new CircularDoublyLinkedList();

	// instances of each custom pane
	private MainPage front = new MainPage();
	private locationPane locPage = new locationPane();
	private martyrPane martPage = new martyrPane();

	// instance of statistics class to perform calculations
	private Statistics stat = new Statistics();

	// Observable Lists to present information through tableViews
	private ObservableList<Location> locations = FXCollections.observableArrayList();
	private ObservableList<Location> Sortedlocations = locations.sorted();
	private ObservableList<Statistics> calculations = FXCollections.observableArrayList(stat);

	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();

		// setting observable lists to each table
		locPage.getLocationTable().setItems(Sortedlocations);
		locPage.getStatTableS().setItems(calculations);
		front.getStatTotTableG().setItems(calculations);

		// creating stages and scenes
		Stage locationStage = new Stage();
		Stage martyrStage = new Stage();
		Scene frontScene = new Scene(root, 695, 550);
		Scene locationScene = new Scene(locPage, 690, 550);
		Scene martyrScene = new Scene(martPage, 650, 610);
		Scene martyrEditorScene = new Scene(martPage.getEditor(), 550, 550);
		Scene reportScene = new Scene(front.getStatPage(), 850, 700);
		Scene locationEditorScene = new Scene(locPage.getEditor(), 560, 400);
		Scene locationTableScene = new Scene(locPage.getLocationTablePane(), 500, 500);
		locationStage.setTitle("Locations");
		martyrStage.setTitle("Martyrs");

		// =========//
		// ACTIONS //
		// =========//
		front.getBack().setOnAction(e -> primaryStage.setScene(frontScene)); // back to mainPage

		front.getLoad().setOnAction(e -> readInfo());// to read the file

		front.getNavLocations().setOnAction(e -> {// locations stage
			// showing the first location when opening
			Location l = (Location) locationList.getFirst();
			assignLocation(l);// sets the location on the page
			locationStage.setScene(locationScene);
			locationStage.show();
		});

		front.getReports().setOnAction(e -> {// general statistics
			setTable();
			primaryStage.setScene(reportScene);
		});

		front.getSave().setOnAction(e -> {// saving information to CVS file O(n*m)

			front.getWarning().setVisible(false);

			// top row (headers of the columns)
			String header = "Name, Age, Event location – District, Date of death, Gender, Status";

			try {
				// choosing file from desktop
				FileChooser fileChooser = new FileChooser();
				File selectedFile = fileChooser.showOpenDialog(new Stage());

				// using printWriter
				PrintWriter output = new PrintWriter(selectedFile);

				output.print("");// clearing all contents of the file
				output.println(header);// printing the header once only

				DNode currentLocation = locationList.getFirstNode();// the head node in the CDLL
				// the head node of the SLL at the first location
				SNode currentMartyr = ((Location) currentLocation.getElement()).getList().getFirstNode();

				for (int i = 0; i < locationList.getSize(); i++) {// looping through all locations(n)
					while (currentMartyr != null) {// looping until we reach the last Martyr(m)

						Martyr m = (Martyr) currentMartyr.getElement();
						output.println(m.getName() + "," + m.getAge() + ","
								+ ((Location) currentLocation.getElement()).getPlace() + "," + m.getDate() + ","
								+ m.getGender() + "," + m.getStatus());// printing
						currentMartyr = currentMartyr.getNext();// next Martyr node

					}
					currentLocation = currentLocation.getNext();// next location node
					// head node in this locations SLL
					currentMartyr = ((Location) currentLocation.getElement()).getList().getFirstNode();
				}

				output.close();// closing printWriter
				front.getWarning().setVisible(true);
				front.getWarning().setText("Succesfully Saved!");

			} catch (FileNotFoundException | NullPointerException r) {// when file isn't chosen or not found
				front.getWarning().setVisible(true);
				front.getWarning().setText("Error While Saving.");
			}
		});

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		locPage.getBack().setOnAction(e -> locationStage.setScene(locationScene));// back to locationPage

		locPage.getViewAll().setOnAction(e -> {// view locations in a tableView
			refreshTables();
			locPage.getLocationTable().getSelectionModel().clearSelection();
			locationStage.setScene(locationTableScene);

		});

		locPage.getNext().setOnAction(e -> {// next location (O(n))
			emptyFields();
			Location l = locationList.getNext(locPage.getLocate());// gets the next location in the CDLL
			assignLocation(l);// sets the location to l

		});

		locPage.getPrev().setOnAction(e -> {// previous location (O(n))
			emptyFields();
			Location l = locationList.getPrevious(locPage.getLocate());// gets the previous location in the CDLL
			assignLocation(l);// sets the location to l

		});

		locPage.getDelete().setOnAction(e -> {// delete selected location (O(n))

			Location l = locPage.getLocate();
			locPage.setLocate(locationList.getNext(l));
			// deleting this location
			locationList.delete(l);// from CDLL O(n)
			locations.remove(l);// from Observable List O(log(n))
			locPage.getWarning1().setText("Successfully deleted " + l.getPlace());
			l = locPage.getLocate();
			assignLocation(l);// sets the location to l

		});

		locPage.getAddNew().setOnAction(e -> {// opens stage to add a location O(1)

			locPage.getHeading().setText("NEW LOCATION:");
			// switching buttons visibility
			locPage.getUp().setVisible(false);
			locPage.getAdd().setVisible(true);
			emptyFields();// emptying textFields
			locationStage.setScene(locationEditorScene);
		});

		locPage.getAdd().setOnAction(e -> {// adds new location to CDLL (O(n))

			String s = locPage.getLocationtxf().getText();// name from TextField

			if (s.isEmpty()) {// no location name
				locPage.getWarning2().setText("Enter a location to proceed.");
			} else {
				Location l = new Location(s);

				if (locationList.get(l) == null) {// when the location DNE in the CDLL O(n)

					// adding the new location
					locations.add(l);// O(log(n))
					locationList.addSorted(l);// O(n)
					locPage.getWarning1().setText("Location succesfully added!");
					assignLocation(l);// sets the location to the new location
					locationStage.setScene(locationScene);

				} else// location already exists
					locPage.getWarning2().setText("Fail...Location already exists.");
			}

		});

		locPage.getUpdate().setOnAction(e -> {// opens stage to update location name (O(1))

			locPage.getHeading().setText("UPDATE LOCATION:");
			emptyFields();
			Location l = locPage.getLocate();
			locPage.getLocationtxf().setText(l.getPlace());
			// switching buttons visibility
			locPage.getUp().setVisible(true);
			locPage.getAdd().setVisible(false);
			locationStage.setScene(locationEditorScene);

		});

		locPage.getUp().setOnAction(e -> {// updates location (O(n))

			String s = locPage.getLocationtxf().getText();// location name form TextField

			if (s.isEmpty()) {// empty name
				locPage.getWarning2().setText("Enter location name to proceed.");
			} else {// assigning the location with the new name

				Location temp = (Location) locationList.get(locPage.getLocate());// saving location to reserve SLL O(n)
				locations.remove(temp);// removing from table O(log(n))
				locationList.delete(temp);// removing from CDLL O(n)
				temp.setPlace(s);// changing name
				locationList.addSorted(temp);// adding back the location with changed name O(n)
				locations.add(temp);// adding updated location to table O(log(n))
				assignLocation(temp);
				locPage.getWarning1().setText("Location succesfully updated!");
				locationStage.setScene(locationScene);
			}

		});

		locPage.getCancel().setOnAction(e -> {// returns to locations scene (O(1))
			emptyFields();// emptying textFields
			locationStage.setScene(locationScene);
		});

		locPage.getSearch().setOnAction(e -> {// searching for a location

			String s = locPage.getLocationSearchtxf().getText();

			if (!s.isEmpty()) {
				Location l = (Location) locationList.get(s);// gets location with a certain name (O(n))

				if (l != null) {// when location exists in CDLL
					emptyFields();// emptying textFields
					assignLocation(l);// sets the location to the one found

				} else// search failed when no location with this name is found
					locPage.getWarning1().setText("This location does not exist.");

			} else// no name to search
				locPage.getWarning1().setText("Enter a location to complete search.");

		});

		locPage.getOpenMartyrs().setOnAction(e -> {// opens the martyrs stage located in the location selected
			// (O(1))
			Location l = locPage.getLocate();
			martPage.getMartyrtable().setItems(l.getSortedData());// setting items in martyr table to list in location l
			martPage.setLocate(l);
			martPage.getLocationlbl().setText(l.getPlace());
			martyrStage.setScene(martyrScene);
			martyrStage.show();

		});

		locPage.getLocationTable().setOnMouseClicked(e -> {// allows to select a location when double clicking the name

			if (e.getClickCount() == 2 && !locPage.getLocationTable().getSelectionModel().isEmpty()) {
				// when user double clicked on a row and the selection is not empty
				Location l = locPage.getLocationTable().getSelectionModel().getSelectedItem();
				assignLocation(l);// setting location to the one selected
				locationStage.setScene(locationScene);
			}
		});
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		martPage.getMartyrtable()// selecting a martyr from the table
				.setOnMouseClicked(
						e -> martPage.setMartyr(martPage.getMartyrtable().getSelectionModel().getSelectedItem()));

		martPage.getBack().setOnAction(e -> {// back to martyr scene (O(1))
			refreshTables();// refreshing tables
			clearSelected();// clearing radioButton selections
			emptyFields();// emptying textFields
			martPage.getMartyrtable().getSelectionModel().clearSelection();
			martPage.setMartyr(null);// clearing selected martyr
			martyrStage.setScene(martyrScene);// changing scenes

		});

		martPage.getSearch().setOnAction(e -> {// searching for martyr by name (O(n+m))

			String s = martPage.getMartyrSearchingtxf().getText();

			if (!s.isEmpty()) {// textField not empty
				Location l = martPage.getLocate();// getting current selected location
				Martyr m = (Martyr) ((Location) locationList.get(l)).getList().get(s);// retrieving martyr with part of
																						// the name (O(n+m))
				if (m != null) {// martyr found
					martPage.setMartyr(m);
					martPage.getMartyrtable().getSelectionModel().select(m);// selects martyr from table
					martPage.getMartyrtable()
							.scrollTo(martPage.getMartyrtable().getSelectionModel().getSelectedIndex());
					martPage.getWarning1().setText("This martyr was found and selected!");
					martPage.getMartyrSearchingtxf().setText("");

				} else// martyr not found
					martPage.getWarning1().setText("This martyr does not exist.");

			} else// no text to search
				martPage.getWarning1().setText("Please enter a name to search.");
		});

		martPage.getAddnew().setOnAction(e -> {// opens stage to add a new Martyr (O(1))
			// switching buttons visibility
			martPage.getAdd().setVisible(true);
			martPage.getUp().setVisible(false);
			emptyFields();// emptying textFields
			clearSelected();// clearing selected gender and status
			martPage.getEditorHeading().setText("NEW MARTYR");
			martyrStage.setScene(martyrEditorScene);

		});

		martPage.getAdd().setOnAction(e -> {// adding new martyr to location (O(n+m))

			String name;
			byte age;
			Date date;

			try {
				// setting the variable to text written by user
				name = martPage.getNametxf().getText();
				age = Byte.parseByte(martPage.getAgetxf().getText());
				date = formatter.parse(martPage.getDatetxf().getText());

				// checking if the radio buttons are selected
				if (!martPage.getFbox().isSelected() && !martPage.getMbox().isSelected()
						|| !martPage.getMarriedBox().isSelected() && !martPage.getSingleBox().isSelected())
					throw new NumberFormatException();

				else {// radioButtons are selected

					Martyr m;
					// constructing martyr m
					if (martPage.getFbox().isSelected()) {// female
						if (martPage.getMarriedBox().isSelected()) // female is married
							m = new Martyr(name, age, date, 'F', "Married");

						else // female is single
							m = new Martyr(name, age, date, 'F', "Single");

					} else {// male

						if (martPage.getMarriedBox().isSelected()) // male is married
							m = new Martyr(name, age, date, 'M', "Married");

						else // male is single
							m = new Martyr(name, age, date, 'M', "Single");
					}

					// adding the martyr only if it doesn't already exist in the list
					if (((Location) locationList.get(martPage.getLocate())).getList().get(m) == null) {// O(n+m)

						((Location) locationList.get(martPage.getLocate())).getList().addSorted(m);// O(n+m)
						martPage.getLocate().getData().add(m);// O(log(n))
						doStat(martPage.getLocate());// recalculating statistics O(n)
						refreshTables();// refreshing tables
						martPage.getWarning1().setText("Martyr Successfully added!");
						martyrStage.setScene(martyrScene);

					} else// cannot add same martyr twice
						martPage.getWarning2().setText("This Martyr already exists.");
				}

			} catch (IllegalArgumentException | ParseException h) {
				martPage.getWarning2().setText("Enter all values in their correct form.");
			}

		});

		martPage.getUpdate().setOnAction(e -> {// opens stage to update the martyr selected (O(1))
			// switching buttons visibility
			martPage.getAdd().setVisible(false);
			martPage.getUp().setVisible(true);
			emptyFields();// emptying textFields
			clearSelected();// clearing selected gender and status

			Martyr m = martPage.getMartyr();

			// when martyr is selected (not null)
			if (m != null) {
				// filling the textFields and radioButtons with the info of the martyr selected
				martPage.getEditorHeading().setText("MARTYR UPDATE");
				martPage.getNametxf().setText(m.getName());
				martPage.getAgetxf().setText(m.getAge() + "");
				martPage.getDatetxf().setText(m.getDate());

				// setting status //
				if (m.getStatus().equalsIgnoreCase("single")) {// when single
					martPage.getSingleBox().setSelected(true);
				} else // when married
					martPage.getMarriedBox().setSelected(true);

				// setting gender //
				if (m.getGender() == 'M') {// when male
					martPage.getMbox().setSelected(true);
				} else // when female
					martPage.getFbox().setSelected(true);
				martyrStage.setScene(martyrEditorScene);

			} else// when martyr not selected
				martPage.getWarning1().setText("Please select/search a martyr to update.");

			refreshTables();// refreshing table after update

		});

		martPage.getUp().setOnAction(e -> {// updates martyr selected (O(n+m))

			String name;
			byte age;
			Date date;

			try {
				// setting the variable to text written by user
				name = martPage.getNametxf().getText();
				age = Byte.parseByte(martPage.getAgetxf().getText());
				date = formatter.parse(martPage.getDatetxf().getText());

				// checking if the values are assigned correctly
				if (name.isEmpty() || !martPage.getFbox().isSelected() && !martPage.getMbox().isSelected()
						|| !martPage.getMarriedBox().isSelected() && !martPage.getSingleBox().isSelected())
					throw new IllegalArgumentException();
				else {// all fields are filled with the right values

					Martyr m;
					// constructing martyr m
					if (martPage.getFbox().isSelected()) {// female
						if (martPage.getMarriedBox().isSelected()) // female is married
							m = new Martyr(name, age, date, 'F', "Married");
						else // female is single
							m = new Martyr(name, age, date, 'F', "Single");

					} else {// male

						if (martPage.getMarriedBox().isSelected()) // male is married
							m = new Martyr(name, age, date, 'M', "Married");
						else// male is single
							m = new Martyr(name, age, date, 'M', "Single");

					}

					Location l = ((Location) locationList.get(martPage.getLocate()));// getting location from CDLL O(n)
					l.getData().remove(martPage.getMartyr());// removing from table O(log(m))
					l.getList().delete(martPage.getMartyr());// removing from SLL O(m)
					l.getList().addSorted(m);// adding updated martyr sorted O(m)
					l.getData().add(m);// adding updated martyr to table O(log(m)

					doStat(martPage.getLocate());// recalculating statistics O(n)
					refreshTables();// refreshing tables
					martPage.getMartyrtable().getSelectionModel().clearSelection();
					martPage.setMartyr(null);// clearing selections
					clearSelected();// clearing selected radioButtons
					emptyFields();// emptying fields
					martPage.getWarning1().setText("Martyr Successfully updated!");
					martyrStage.setScene(martyrScene);

				}

			} catch (IllegalArgumentException | ParseException h) {// error in one of the fields
				martPage.getWarning2().setText("Enter all values in their correct form.");
			}

		});

		martPage.getDelete().setOnAction(e -> {// deletes selected martyr (O(n+m))

			Martyr m = martPage.getMartyr();
			if (m != null) {// when a martyr was selected (not null)

				martPage.getLocate().getData().remove(m);// remove from observable List (O(log(m)))
				((Location) locationList.get(martPage.getLocate())).getList().delete(m);// remove from SLL (O(n+m))
				martPage.getWarning1().setText(m.getName() + " successfully deleted!");

			} else// martyr not selected
				martPage.getWarning1().setText("Please select/search a martyr to delete.");

			doStat(martPage.getLocate());// recalculating statistics O(n)
			martPage.getMartyrtable().getSelectionModel().clearSelection();
			martPage.setMartyr(null);// clearing selections
			refreshTables();// refreshing tables

		});

		// opening main stage and front page
		root.setCenter(front);
		primaryStage.setScene(frontScene);
		primaryStage.show();
		primaryStage.setTitle("Palestinian Martyrs");

		// setting columns for the tableViews
		setColumns();
		// button movements in the front page
		buttonMovments();
	}

	private void assignLocation(Location l) {// assigns location l to the page O(n)
		locPage.setLocate(l);// sets selected location to l
		locPage.getLocationlbl().setText(l.getPlace());// changes label to location name
		doStat(l);// calculates statistics
		refreshTables();// refreshing tables
	}

	private void doStat(Location l) {// calculating statistics O(n)
		stat.calculateStat(l);// O(n)
		stat.calculateMaxMartyrDate(l);// O(n)
	}

	private void emptyFields() {// emptying all textFields and warning labels O(1)
		locPage.getLocationSearchtxf().setText("");
		locPage.getLocationtxf().setText("");
		locPage.getWarning2().setText("");
		locPage.getWarning1().setText("");
		martPage.getNametxf().setText("");
		martPage.getAgetxf().setText("");
		martPage.getDatetxf().setText("");
		martPage.getWarning1().setText("");
		martPage.getWarning2().setText("");
	}

	private void clearSelected() {// clears all selected radioButtons (gender & status) O(1)
		martPage.getMbox().setSelected(false);
		martPage.getFbox().setSelected(false);
		martPage.getMarriedBox().setSelected(false);
		martPage.getSingleBox().setSelected(false);
	}

	private void refreshTables() {// refreshes all tablesO(1)
		locPage.getLocationTable().refresh();
		locPage.getStatTableS().refresh();
		martPage.getMartyrtable().refresh();
		front.getStatTableG().refresh();
		front.getStatTotTableG().refresh();
	}

	private void setTable() {

		DNode curr = locationList.getFirstNode();// first location
		ObservableList generalCalculations = FXCollections.observableArrayList();
		front.getStatTableG().setItems(generalCalculations);// setting items

		// filling the observable list with each location
		for (int i = 0; i < locationList.getSize(); i++) {
			generalCalculations.add(new Statistics((Location) curr.getElement()));
			curr = curr.getNext();
		}
	}

	/**
	 * The main method is only needed for the IDE with limited JavaFX support. Not
	 * needed for running from the command line.
	 */
	public static void main(String[] args) {
		launch(args);// launching application
	}

	private void readInfo() {// reads information from CVS file (O(k*(m+n))) k->#lines | m->SLL | n->CDLL

		front.getWarning().setVisible(false);

		// choosing file from desktop
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(new Stage());
		// to read string form of date
		try {
			Scanner input = new Scanner(selectedFile);// scanning file
			input.nextLine();// skipping header

			while (input.hasNextLine()) {// reading line by line O(k)
				Scanner read = new Scanner(input.nextLine());// reading word by word
				read.useDelimiter(",");
				try {
					// filling variables with values from the line
					String name = read.next();
					Byte age = Byte.parseByte(read.next());
					String location = read.next();
					Date date = formatter.parse(read.next());
					String gender = read.next();
					String status = read.next();

					// determining whether all values are correct
					if (location.isEmpty() || gender.isEmpty() || status.isEmpty()
							|| !gender.equalsIgnoreCase("m") && !gender.equalsIgnoreCase("f"))
						throw new IllegalArgumentException();

					else {// values are correct
						Martyr m = new Martyr(name, age, date, gender.charAt(0), status);// creating martyr object
						Location l = new Location(location);// creating location Object

						if (locationList.get(l) != null) {// Location already exists in CDLL O(m+n)
							l = (Location) locationList.get(l);// O(n)
							l.getData().add(m);// adding martyr to the location O(log(m))
							l.getList().addSorted(m);// O(m)

						} else {// adding new Location to CDLL
							locationList.addSorted(l);// O(n)
							locations.add(l);// O(log(n))
							l = (Location) locationList.get(l);// O(n)
							l.getData().add(m);// adding martyr to the location O(log(m))
							l.getList().addSorted(m);// O(m)
						}
					}
				} catch (IllegalArgumentException | ParseException e) {
					// Skipping lines with illegal inputs
				}

			}
			// allowing interactions with other buttons after reading the file successfully
			front.getReports().setDisable(false);
			front.getNavLocations().setDisable(false);
			front.getSave().setDisable(false);
			input.close();// closing Scanner

		} catch (NullPointerException | FileNotFoundException | NoSuchElementException e1) {
			front.getWarning().setVisible(true);
			front.getWarning().setText("Faild To Read File.");
		}

	}

	private void setColumns() {// setting columns in the tables and their properties

		// Location Table
		TableColumn<Location, String> loc = new TableColumn<Location, String>("Location");

		loc.setCellValueFactory(new PropertyValueFactory<Location, String>("place"));
		loc.setMinWidth(390);
		loc.setStyle("-fx-alignment: CENTER;-fx-header-background:green;");

		loc.setSortable(false);
		locPage.getLocationTable().getColumns().add(loc);

		//////////////////////////////////////////////////////////////////////////////////////

		// Martyr Table
		TableColumn<Martyr, String> name = new TableColumn<Martyr, String>("Name");
		TableColumn<Martyr, Byte> age = new TableColumn<Martyr, Byte>("Age");
		TableColumn<Martyr, String> date = new TableColumn<Martyr, String>("Date Of Death");
		TableColumn<Martyr, String> status = new TableColumn<Martyr, String>("Status");
		TableColumn<Martyr, Character> gender = new TableColumn<Martyr, Character>("Gender");

		name.setCellValueFactory(new PropertyValueFactory<Martyr, String>("name"));
		date.setCellValueFactory(new PropertyValueFactory<Martyr, String>("date"));
		gender.setCellValueFactory(new PropertyValueFactory<Martyr, Character>("gender"));
		status.setCellValueFactory(new PropertyValueFactory<Martyr, String>("status"));
		age.setCellValueFactory(new PropertyValueFactory<Martyr, Byte>("age"));

		name.setStyle("-fx-alignment: CENTER;-fx-pref-width: 300;");
		date.setStyle("-fx-alignment: CENTER;-fx-pref-width: 100;");
		gender.setStyle("-fx-alignment: CENTER;-fx-pref-width: 60;");
		age.setStyle("-fx-alignment: CENTER;-fx-pref-width: 60;");
		status.setStyle("-fx-alignment: CENTER;-fx-pref-width: 60;");

		// preventing automatic sorting
		name.setSortable(false);
		date.setSortable(false);
		gender.setSortable(false);
		status.setSortable(false);
		age.setSortable(false);

		// to prevent moving the columns
		name.setReorderable(false);
		date.setReorderable(false);
		gender.setReorderable(false);
		status.setReorderable(false);
		age.setReorderable(false);

		// setting columns for each table
		martPage.getMartyrtable().getColumns().addAll(name, gender, age, status, date);

		//////////////////////////////////////////////////////////////////////////////////////

		// specific Statistics Table
		// main column
		TableColumn<Statistics, Integer> byAge = new TableColumn<Statistics, Integer>("Number Of Martyrs");
		TableColumn<Statistics, Integer> children = new TableColumn<Statistics, Integer>("0 -- 17");
		TableColumn<Statistics, Integer> adults = new TableColumn<Statistics, Integer>("18 -- 64");
		TableColumn<Statistics, Integer> seniors = new TableColumn<Statistics, Integer>("65 & Above");
		TableColumn<Statistics, Integer> female = new TableColumn<Statistics, Integer>("Female");
		TableColumn<Statistics, Integer> male = new TableColumn<Statistics, Integer>("Male");
		TableColumn<Statistics, String> marriedS = new TableColumn<Statistics, String>("Married");
		TableColumn<Statistics, String> singleS = new TableColumn<Statistics, String>("Single");

		byAge.getColumns().addAll(children, adults, seniors, male, female, marriedS, singleS);

		// main column
		TableColumn<Statistics, Integer> avgAge = new TableColumn<Statistics, Integer>("Average\nAge");
		// main column
		TableColumn<Statistics, Integer> mostDate = new TableColumn<Statistics, Integer>("Date With The\nMost Martyrs");
		TableColumn<Statistics, String> dateValue = new TableColumn<Statistics, String>("Date");
		TableColumn<Statistics, Integer> amount = new TableColumn<Statistics, Integer>("Amount");

		mostDate.getColumns().addAll(dateValue, amount);

		children.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("childrenCount"));
		adults.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("adultCount"));
		seniors.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("seniorCount"));
		male.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("maleCount"));
		female.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("femaleCount"));
		avgAge.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("avgAge"));
		dateValue.setCellValueFactory(new PropertyValueFactory<Statistics, String>("maxDateString"));
		amount.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("maxCount"));
		marriedS.setCellValueFactory(new PropertyValueFactory<Statistics, String>("marriedCount"));
		singleS.setCellValueFactory(new PropertyValueFactory<Statistics, String>("singleCount"));

		locPage.getStatTableS().getColumns().addAll(byAge, avgAge, mostDate);

		children.setStyle("-fx-alignment: CENTER;-fx-font-size: 15;");
		adults.setStyle("-fx-alignment: CENTER;-fx-font-size: 15;");
		seniors.setStyle("-fx-alignment: CENTER;-fx-font-size: 15;-fx-pref-width: 100;");
		male.setStyle("-fx-alignment: CENTER;-fx-font-size: 15;");
		female.setStyle("-fx-alignment: CENTER;-fx-font-size: 15;");
		marriedS.setStyle("-fx-alignment: CENTER;-fx-font-size: 15;");
		singleS.setStyle("-fx-alignment: CENTER;-fx-font-size: 15;");
		avgAge.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;");
		dateValue.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;");
		amount.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;");
		byAge.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 437;");
		avgAge.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;");
		mostDate.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;");

		// preventing automatic sorting
		children.setSortable(false);
		adults.setSortable(false);
		seniors.setSortable(false);
		male.setSortable(false);
		female.setSortable(false);
		marriedS.setSortable(false);
		singleS.setSortable(false);
		avgAge.setSortable(false);
		dateValue.setSortable(false);
		amount.setSortable(false);
		byAge.setSortable(false);
		avgAge.setSortable(false);
		mostDate.setSortable(false);

		// to prevent moving the columns
		children.setReorderable(false);
		adults.setReorderable(false);
		seniors.setReorderable(false);
		male.setReorderable(false);
		female.setReorderable(false);
		marriedS.setReorderable(false);
		singleS.setReorderable(false);
		avgAge.setReorderable(false);
		dateValue.setReorderable(false);
		amount.setReorderable(false);
		byAge.setReorderable(false);
		avgAge.setReorderable(false);
		mostDate.setReorderable(false);

		/////////////////////////////////////////////////////////
		// General Statistics Table
		// total statistics

		TableColumn<Statistics, Integer> tot = new TableColumn<Statistics, Integer>("-- Total Number Of Martyrs --");
		TableColumn<Statistics, Integer> m = new TableColumn<Statistics, Integer>("Male");
		TableColumn<Statistics, Integer> f = new TableColumn<Statistics, Integer>("Female");
		TableColumn<Statistics, Integer> married = new TableColumn<Statistics, Integer>("Married");
		TableColumn<Statistics, Integer> single = new TableColumn<Statistics, Integer>("Single");
		TableColumn<Statistics, Integer> child = new TableColumn<Statistics, Integer>("0 -- 17");
		TableColumn<Statistics, Integer> adult = new TableColumn<Statistics, Integer>("18 -- 64");
		TableColumn<Statistics, Integer> senior = new TableColumn<Statistics, Integer>("65 & Above");

		m.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("COUNT_M"));
		f.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("COUNT_F"));
		single.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("COUNT_SINGLE"));
		married.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("COUNT_MARRIED"));
		child.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("COUNT_CHILDREN"));
		adult.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("COUNT_ADULT"));
		senior.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("COUNT_SENIOR"));

		tot.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 848;");
		m.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 120;");
		f.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 120;");
		single.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 120;");
		married.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 120;");
		child.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 120;");
		adult.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 110;");
		senior.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 110;");

		// preventing automatic sorting
		tot.setSortable(false);
		m.setSortable(false);
		f.setSortable(false);
		single.setSortable(false);
		married.setSortable(false);
		child.setSortable(false);
		adult.setSortable(false);
		senior.setSortable(false);

		// to prevent moving the columns
		tot.setReorderable(false);
		m.setReorderable(false);
		f.setReorderable(false);
		single.setReorderable(false);
		married.setReorderable(false);
		child.setReorderable(false);
		adult.setReorderable(false);
		senior.setReorderable(false);

		tot.getColumns().addAll(child, adult, senior, m, f, married, single);

		front.getStatTotTableG().getColumns().addAll(tot);

		//////////////////////////////////////////////////////////
		// all locations

		TableColumn<Statistics, String> locName = new TableColumn<Statistics, String>("location");
		TableColumn<Statistics, Integer> childrenG = new TableColumn<Statistics, Integer>("0 -- 17");
		TableColumn<Statistics, Integer> adultsG = new TableColumn<Statistics, Integer>("18 -- 64");
		TableColumn<Statistics, Integer> seniorsG = new TableColumn<Statistics, Integer>("65 & Above");
		TableColumn<Statistics, Integer> marriedG = new TableColumn<Statistics, Integer>("Married");
		TableColumn<Statistics, Integer> singleG = new TableColumn<Statistics, Integer>("Single");
		TableColumn<Statistics, Integer> femaleG = new TableColumn<Statistics, Integer>("Female");
		TableColumn<Statistics, Integer> maleG = new TableColumn<Statistics, Integer>("Male");

		locName.setCellValueFactory(new PropertyValueFactory<Statistics, String>("location"));
		childrenG.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("childrenCount"));
		adultsG.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("adultCount"));
		seniorsG.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("seniorCount"));
		marriedG.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("marriedCount"));
		singleG.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("singleCount"));
		femaleG.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("femaleCount"));
		maleG.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("maleCount"));

		locName.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 150;");
		childrenG.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 94;");
		adultsG.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 94;");
		seniorsG.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 94;");
		marriedG.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 94;");
		singleG.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 94;");
		femaleG.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 94;");
		maleG.setStyle("-fx-alignment: CENTER;-fx-font-size: 14;-fx-pref-width: 94;");

		// preventing automatic sorting
		locName.setSortable(false);
		childrenG.setSortable(false);
		adultsG.setSortable(false);
		seniorsG.setSortable(false);
		marriedG.setSortable(false);
		singleG.setSortable(false);
		femaleG.setSortable(false);
		maleG.setSortable(false);

		// to prevent moving the columns
		locName.setReorderable(false);
		childrenG.setReorderable(false);
		adultsG.setReorderable(false);
		seniorsG.setReorderable(false);
		marriedG.setReorderable(false);
		singleG.setReorderable(false);
		femaleG.setReorderable(false);
		maleG.setReorderable(false);

		front.getStatTableG().getColumns().addAll(locName, childrenG, adultsG, seniorsG, femaleG, maleG, marriedG,
				singleG);

	}

	private void buttonMovments() {// button moves when entering and exiting it

		front.getLoad().setOnMouseEntered(e -> front.getLoad().setTranslateY(front.getLoad().getTranslateY() - 7));

		front.getLoad().setOnMouseExited(e -> front.getLoad().setTranslateY(front.getLoad().getTranslateY() + 7));

		front.getNavLocations().setOnMouseEntered(
				e -> front.getNavLocations().setTranslateY(front.getNavLocations().getTranslateY() - 7));

		front.getNavLocations().setOnMouseExited(
				e -> front.getNavLocations().setTranslateY(front.getNavLocations().getTranslateY() + 7));

		front.getReports()
				.setOnMouseEntered(e -> front.getReports().setTranslateY(front.getReports().getTranslateY() - 7));

		front.getReports()
				.setOnMouseExited(e -> front.getReports().setTranslateY(front.getReports().getTranslateY() + 7));

		front.getSave().setOnMouseEntered(e -> front.getSave().setTranslateY(front.getSave().getTranslateY() - 7));

		front.getSave().setOnMouseExited(e -> front.getSave().setTranslateY(front.getSave().getTranslateY() + 7));

	}

}
