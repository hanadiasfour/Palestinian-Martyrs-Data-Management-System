package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();

		// creating stages and scenes
		Stage locationStage = new Stage();
		Stage martyrStage = new Stage();
		Scene frontScene = new Scene(root, 695, 550);
		Scene locationScene = new Scene(locPage, 1300, 750);
		Scene martyrScene = new Scene(martPage, 650, 610);
		Scene martyrEditorScene = new Scene(martPage.getEditor(), 550, 550);
		Scene locationEditorScene = new Scene(locPage.getEditor(), 560, 400);
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

		front.getSave().setOnAction(e -> {// saving information to CVS file O(n*m)

			front.getWarning().setVisible(false);

			// top row (headers of the columns)
			String header = "Name, Age, Event location – District, Date of death, Gender";

			try {
				// choosing file from desktop
				FileChooser fileChooser = new FileChooser();
				File selectedFile = fileChooser.showOpenDialog(new Stage());

				// using printWriter
				PrintWriter output = new PrintWriter(selectedFile);

				output.print("");// clearing all contents of the file
				output.println(header);// printing the header once only

				DNode currentLocation = locationList.getFirstNode();// the head node in the CDLL

				for (int i = 0; i < locationList.getSize(); i++) {

					Location l = (Location) currentLocation.getElement();

					if (!((Location) currentLocation.getElement()).equals(locPage.getLocate())) {// when the queue is
																									// not empty
						SLLQueue dummy = new SLLQueue();

						while (l.getList() != null && !l.getList().isEmpty()) {

							Martyr m = (Martyr) l.getList().peek();

							output.println(m.getName() + "," + m.getAge() + "," + l.getPlace() + "," + m.getDate() + ","
									+ m.getGender());// printing

							dummy.enqueue(l.getList().dequeue());

						}
						while (!dummy.isEmpty())// moving elements back
							l.getList().enqueue(dummy.dequeue());

					} else {// reading form the avl instead of the queue
						if (!l.getAVL_Name().isEmpty())
							writeToFile(output, l.getAVL_Name().getRoot(), l);

					}

					currentLocation = currentLocation.getNext();
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

		locPage.getNext().setOnAction(e -> {// next location (O(n))
			emptyFields();
			deconstructAVLs(locPage.getLocate());
			Location l = locationList.getNext(locPage.getLocate());// gets the next location in the CDLL
			assignLocation(l);// sets the location to l

		});

		locPage.getPrev().setOnAction(e -> {// previous location (O(n))
			emptyFields();
			deconstructAVLs(locPage.getLocate());
			Location l = locationList.getPrevious(locPage.getLocate());// gets the previous location in the CDLL
			assignLocation(l);// sets the location to l

		});

		locPage.getDelete().setOnAction(e -> {// delete selected location (O(n))

			Location l = locPage.getLocate();
			deconstructAVLs(l);
			locPage.setLocate(locationList.getNext(l));
			// deleting this location
			locationList.delete(l);// from CDLL O(n)
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
					locationList.addSorted(l);// O(n)
					locPage.getWarning1().setText("Location succesfully added!");
					deconstructAVLs(locPage.getLocate());
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
				locationList.delete(temp);// removing from CDLL O(n)
				deconstructAVLs(temp);
				temp.setPlace(s);// changing name
				locationList.addSorted(temp);// adding back the location with changed name O(n)
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
					deconstructAVLs(locPage.getLocate());
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
			martPage.getMartyrtable().setItems(l.getAVL2Items());// setting items in martyr table to list in location l
			martPage.setLocate(l);
			martPage.getLocationlbl().setText(l.getPlace());
			martyrStage.setScene(martyrScene);
			martyrStage.show();

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
				Martyr m = (Martyr) ((Location) locationList.get(l)).getAVL_Name().findByName(s);// retrieving martyr

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
				if (!martPage.getFbox().isSelected() && !martPage.getMbox().isSelected())
					throw new NumberFormatException();

				else {// radioButtons are selected

					Martyr m;
					// constructing martyr m
					if (martPage.getFbox().isSelected()) // female
						m = new Martyr(name, age, date, 'F');

					else // male
						m = new Martyr(name, age, date, 'M');

					// adding the martyr only if it doesn't already exist in the list
					if (martPage.getLocate().getAVL_Name().findnode(m) == null) {// O(n+m)

						martPage.getLocate().getAVL_Name().insert(m);// into AVL1
						insertToAVL2(martPage.getLocate(), m);// into AVL2
						assignLocation(martPage.getLocate());
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
				if (name.isEmpty() || !martPage.getFbox().isSelected() && !martPage.getMbox().isSelected())
					throw new IllegalArgumentException();

				else {// all fields are filled with the right values
					Martyr m;
					// constructing martyr m
					if (martPage.getFbox().isSelected()) // female
						m = new Martyr(name, age, date, 'F');

					else // male
						m = new Martyr(name, age, date, 'M');

					Location l = martPage.getLocate();// getting location from CDLL O(n)

					if (!date.equals(martPage.getMartyr().getDateOfDeath())) {
						deleteFromAVL2(l, martPage.getMartyr());
						l.getAVL_Name().update(m, l.getAVL_Name().findnode(martPage.getMartyr()));
						insertToAVL2(l, martPage.getMartyr());

					} else
						l.getAVL_Name().update(m, l.getAVL_Name().findnode(martPage.getMartyr()));

					assignLocation(l);
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

				martPage.getLocate().getAVL_Name().delete(m);// remove from SLL (O(n+m))
				deleteFromAVL2(martPage.getLocate(), martPage.getMartyr());
				martPage.getWarning1().setText(m.getName() + " successfully deleted!");
				assignLocation(martPage.getLocate());

			} else// martyr not selected
				martPage.getWarning1().setText("Please select/search a martyr to delete.");

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

	private void writeToFile(PrintWriter output, TNode node, Location l) {

		if (node.getLeft() != null)
			writeToFile(output, node.getLeft(), l);

		Martyr m = (Martyr) node.getData();
		output.println(m.getName() + "," + m.getAge() + "," + l.getPlace() + "," + m.getDate() + "," + m.getGender());

		if (node.getRight() != null)
			writeToFile(output, node.getRight(), l);

	}

	private void assignLocation(Location l) {// assigns location l to the page O(n)
		locPage.setLocate(l);// sets selected location to l
		locPage.getLocationlbl().setText(l.getPlace());// changes label to location name
		constructAVLs(l);
		locPage.getAVL1().setItems(l.getAVL1Items());
		locPage.getAVL2().setItems(l.getAVL2Items());
		// setting items in martyr table to list in location l
		martPage.getMartyrtable().setItems(l.getAVL2Items());
		l.getStat().doStat(l);
		locPage.getTotTable().setItems(l.getCalculations());
		refreshTables();// refreshing tables

	}

	private void constructAVLs(Location l) {

		while (!l.getList().isEmpty()) {

			Martyr m = (Martyr) l.getList().dequeue();

			l.getAVL_Name().insert(m);// for AVL 1

			insertToAVL2(l, m);
		}

	}

	private void insertToAVL2(Location l, Martyr m) {

		// for AVL 2
		Date d = m.getDateOfDeath();
		TNode node = l.getAVL_Dates().findnode(new DateStack(d));

		if (node != null)// date exists
			((DateStack) node.getData()).getMartyrStack().push(m);
		else {

			l.getAVL_Dates().insert(new DateStack(d));
			node = l.getAVL_Dates().findnode(new DateStack(d));
			((DateStack) node.getData()).getMartyrStack().push(m);

		}

	}

	private void deleteFromAVL2(Location l, Martyr m) {

		Date d = m.getDateOfDeath();

		DateStack dateStack = (DateStack) l.getAVL_Dates().findnode(new DateStack(d)).getData();

		SLLStack dummy = new SLLStack();
		SLLStack s = dateStack.getMartyrStack();

		while (!s.isEmpty()) {// saving all references except for the martyr we want to delete
			if (s.peek().equals(m)) {
				s.pop();// skipping Martyr
				continue;
			}
			dummy.push(s.pop());
		}

		while (!dummy.isEmpty()) // putting it back
			s.push(dummy.pop());

		if (s.isEmpty())// deleting the whole datStack if there isn't anything in the stack
			l.getAVL_Dates().delete(dateStack);

	}

	private void deconstructAVLs(Location l) {

		// First AVL
		while (l.getAVL_Name().getRoot() != null) {
			l.getList().enqueue(l.getAVL_Name().getRoot().getData());// adding back to queue
			l.getAVL_Name().delete(l.getAVL_Name().getRoot().getData());
		}
		// second AVL
		while (l.getAVL_Dates().getRoot() != null) {
			l.getAVL_Dates().delete(l.getAVL_Dates().getRoot().getData());
		}
	}

	private void emptyFields() {// emptying all textFields and warning labels O(1)
		locPage.getLocationSearchtxf().setText("");
		locPage.getLocationtxf().setText("");
		locPage.getWarning2().setText("");
		locPage.getWarning1().setText("");
		martPage.getNametxf().setText("");
		martPage.getAgetxf().setText("");
		Calendar c = new GregorianCalendar();
		String d = c.get(Calendar.MONTH) + 1 + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR);
		martPage.getDatetxf().setText(d);
		martPage.getWarning1().setText("");
		martPage.getWarning2().setText("");
	}

	private void clearSelected() {// clears all selected radioButtons (gender & status) O(1)
		martPage.getMbox().setSelected(false);
		martPage.getFbox().setSelected(false);
	}

	private void refreshTables() {// refreshes all tablesO(1)
		martPage.getMartyrtable().refresh();
		locPage.getTotTable().refresh();
		locPage.getAVL1().refresh();
		locPage.getAVL2().refresh();
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

					// determining whether all values are correct
					if (location.isEmpty() || gender.isEmpty())
						throw new IllegalArgumentException();

					else {// values are correct
						Martyr m = new Martyr(name, age, date, gender.charAt(0));// creating martyr object
						Location l = new Location(location);// creating location Object

						if (locationList.get(l) != null) {// Location already exists in CDLL O(m+n)
							l = (Location) locationList.get(l);// O(n)
							l.getList().enqueue(m);// O(1)

						} else {// adding new Location to CDLL
							locationList.addSorted(l);// O(n)
							l = (Location) locationList.get(l);// O(n)
							l.getList().enqueue(m);// O(1)

						}
					}
				} catch (IllegalArgumentException | ParseException e) {
					// Skipping lines with illegal inputs
				}

			}
			// allowing interactions with other buttons after reading the file successfully
			front.getNavLocations().setDisable(false);
			front.getSave().setDisable(false);
			input.close();// closing Scanner

		} catch (NullPointerException | FileNotFoundException | NoSuchElementException e1) {
			front.getWarning().setVisible(true);
			front.getWarning().setText("Faild To Read File.");
		}

	}

	private void setColumns() {// setting columns in the tables and their properties

		// Martyr Table
		TableColumn<Martyr, String> name = new TableColumn<Martyr, String>("Name");
		TableColumn<Martyr, Byte> age = new TableColumn<Martyr, Byte>("Age");
		TableColumn<Martyr, String> date = new TableColumn<Martyr, String>("Date Of Death");
		TableColumn<Martyr, Character> gender = new TableColumn<Martyr, Character>("Gender");

		name.setCellValueFactory(new PropertyValueFactory<Martyr, String>("name"));
		date.setCellValueFactory(new PropertyValueFactory<Martyr, String>("date"));
		gender.setCellValueFactory(new PropertyValueFactory<Martyr, Character>("gender"));
		age.setCellValueFactory(new PropertyValueFactory<Martyr, Byte>("age"));

		name.setStyle("-fx-alignment: CENTER;-fx-pref-width: 300;");
		date.setStyle("-fx-alignment: CENTER;-fx-pref-width: 100;");
		gender.setStyle("-fx-alignment: CENTER;-fx-pref-width: 60;");
		age.setStyle("-fx-alignment: CENTER;-fx-pref-width: 60;");

		// preventing automatic sorting
		name.setSortable(false);
		date.setSortable(false);
		gender.setSortable(false);
		age.setSortable(false);

		// to prevent moving the columns
		name.setReorderable(false);
		date.setReorderable(false);
		gender.setReorderable(false);
		age.setReorderable(false);

		// setting columns for each table
		martPage.getMartyrtable().getColumns().addAll(name, gender, age, date);

		//////////////////////////////////////////////////////////////////////////////////////
		// specific Statistics Table
		// main column
		TableColumn<Statistics, Integer> totMartyrs = new TableColumn<Statistics, Integer>("Total Martyrs");
		TableColumn<Statistics, Integer> height1 = new TableColumn<Statistics, Integer>("AVL1 Height");
		TableColumn<Statistics, Integer> height2 = new TableColumn<Statistics, Integer>("AVL2 Height");
		TableColumn<Statistics, String> mostDeathDate = new TableColumn<Statistics, String>("Most Deaths On");

		totMartyrs.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("size"));
		height1.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("height1"));
		height2.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("height2"));
		mostDeathDate.setCellValueFactory(new PropertyValueFactory<Statistics, String>("mostDate"));

		locPage.getTotTable().getColumns().addAll(totMartyrs, height1, height2, mostDeathDate);

		totMartyrs.setStyle("-fx-alignment: CENTER;-fx-font-size: 15;-fx-pref-width: 140;");
		height1.setStyle("-fx-alignment: CENTER;-fx-font-size: 15;-fx-pref-width: 140;");
		height2.setStyle("-fx-alignment: CENTER;-fx-font-size: 15;-fx-pref-width: 140;");
		mostDeathDate.setStyle("-fx-alignment: CENTER;-fx-font-size: 15;-fx-pref-width: 170;");

		// preventing automatic sorting
		totMartyrs.setSortable(false);
		height1.setSortable(false);
		height2.setSortable(false);
		mostDeathDate.setSortable(false);

		// to prevent moving the columns
		totMartyrs.setReorderable(false);
		height1.setReorderable(false);
		height2.setReorderable(false);
		mostDeathDate.setReorderable(false);

		//////////////////////////////////////////////////////////////////////////////////////
		// AVL 1 Columns

		TableColumn<Martyr, String> title1 = new TableColumn<Martyr, String>("Level-By-Level: AVL1");
		TableColumn<Martyr, String> name1 = new TableColumn<Martyr, String>("Name");
		TableColumn<Martyr, Byte> age1 = new TableColumn<Martyr, Byte>("Age");
		TableColumn<Martyr, String> date1 = new TableColumn<Martyr, String>("Date Of Death");
		TableColumn<Martyr, Character> gender1 = new TableColumn<Martyr, Character>("Gender");

		name1.setCellValueFactory(new PropertyValueFactory<Martyr, String>("name"));
		date1.setCellValueFactory(new PropertyValueFactory<Martyr, String>("date"));
		gender1.setCellValueFactory(new PropertyValueFactory<Martyr, Character>("gender"));
		age1.setCellValueFactory(new PropertyValueFactory<Martyr, Byte>("age"));

		name1.setStyle("-fx-alignment: CENTER;-fx-pref-width: 300;");
		date1.setStyle("-fx-alignment: CENTER;-fx-pref-width: 100;");
		gender1.setStyle("-fx-alignment: CENTER;-fx-pref-width: 100;");
		age1.setStyle("-fx-alignment: CENTER;-fx-pref-width: 100;");

		// preventing automatic sorting
		name1.setSortable(false);
		date1.setSortable(false);
		gender1.setSortable(false);
		age1.setSortable(false);

		// to prevent moving the columns
		name1.setReorderable(false);
		date1.setReorderable(false);
		gender1.setReorderable(false);
		age1.setReorderable(false);

		title1.getColumns().addAll(name1, gender1, age1, date1);

		// setting columns for each table
		locPage.getAVL1().getColumns().add(title1);

//////////////////////////////////////////////////////////////////////////////////////
//AVL 2 Columns

		TableColumn<Martyr, String> title2 = new TableColumn<Martyr, String>("Traverse-Backwards: AVL2");

		TableColumn<Martyr, String> name2 = new TableColumn<Martyr, String>("Name");
		TableColumn<Martyr, Byte> age2 = new TableColumn<Martyr, Byte>("Age");
		TableColumn<Martyr, String> date2 = new TableColumn<Martyr, String>("Date Of Death");
		TableColumn<Martyr, Character> gender2 = new TableColumn<Martyr, Character>("Gender");

		name2.setCellValueFactory(new PropertyValueFactory<Martyr, String>("name"));
		date2.setCellValueFactory(new PropertyValueFactory<Martyr, String>("date"));
		gender2.setCellValueFactory(new PropertyValueFactory<Martyr, Character>("gender"));
		age2.setCellValueFactory(new PropertyValueFactory<Martyr, Byte>("age"));

		name2.setStyle("-fx-alignment: CENTER;-fx-pref-width: 300;");
		date2.setStyle("-fx-alignment: CENTER;-fx-pref-width: 100;");
		gender2.setStyle("-fx-alignment: CENTER;-fx-pref-width: 100;");
		age2.setStyle("-fx-alignment: CENTER;-fx-pref-width: 100;");

		// preventing automatic sorting
		name2.setSortable(false);
		date2.setSortable(false);
		gender2.setSortable(false);
		age2.setSortable(false);

		// to prevent moving the columns
		name2.setReorderable(false);
		date2.setReorderable(false);
		gender2.setReorderable(false);
		age2.setReorderable(false);

		title2.getColumns().addAll(name2, gender2, age2, date2);

		// setting columns for each table
		locPage.getAVL2().getColumns().add(title2);

	}

	private void buttonMovments() {// button moves when entering and exiting it

		front.getLoad().setOnMouseEntered(e -> front.getLoad().setTranslateY(front.getLoad().getTranslateY() - 7));

		front.getLoad().setOnMouseExited(e -> front.getLoad().setTranslateY(front.getLoad().getTranslateY() + 7));

		front.getNavLocations().setOnMouseEntered(
				e -> front.getNavLocations().setTranslateY(front.getNavLocations().getTranslateY() - 7));

		front.getNavLocations().setOnMouseExited(
				e -> front.getNavLocations().setTranslateY(front.getNavLocations().getTranslateY() + 7));

		front.getSave().setOnMouseEntered(e -> front.getSave().setTranslateY(front.getSave().getTranslateY() - 7));

		front.getSave().setOnMouseExited(e -> front.getSave().setTranslateY(front.getSave().getTranslateY() + 7));

	}

}
