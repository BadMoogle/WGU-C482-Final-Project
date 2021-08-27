import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Controller class for the AddProduct form.
 * @author Daniel Harper
 */
public class AddProductController {

    @FXML
    private TextField TxtBxID;

    @FXML
    private TextField TxtBxName;

    @FXML
    private TextField TxtBxInv;

    @FXML
    private TextField TxtBxPrice;

    @FXML
    private TextField TxtBxMin;

    @FXML
    private TextField TxtBxMax;

    @FXML
    private TextField TxtBxSearchProduct;

    @FXML
    private TableView<Part> TbleAvailableParts;

    @FXML
    private TableColumn<Part, String> TblCmnAllPartsID;

    @FXML
    private TableColumn<Part, String> TblLmnAllPartsName;

    @FXML
    private TableColumn<Part, Integer> TblCmnAllPartsInventory;

    @FXML
    private TableColumn<Part, Double> TblCmnAllPartsPrice;

    @FXML
    private Button BtnAdd;

    @FXML
    private TableView<Part> TbleSelectedParts;

    @FXML
    private TableColumn<Part, String> TblCmnSelectedPartID;

    @FXML
    private TableColumn<Part, String> TblCmnSelectedPartName;

    @FXML
    private TableColumn<Part, Integer> TblCmnSelectedPartInventory;

    @FXML
    private TableColumn<Part, Double> TblCmnSelectedPartPrice;

    @FXML
    private Button BtnRemove;

    @FXML
    private Button BtnSave;

    @FXML
    private Button BtnCancel;

    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Part> selectedParts = FXCollections.observableArrayList();
    private Property<ObservableList<Part>> allPartListProperty = new SimpleObjectProperty<>(allParts);
    private Property<ObservableList<Part>> selectedPartListProperty = new SimpleObjectProperty<>(selectedParts);


    /**
     * Initialize the form.  Binds the relevant columns to the data.
     */
    @FXML
    void initialize() {
        TblCmnAllPartsID.setCellValueFactory(new PropertyValueFactory<>("id"));
        TblLmnAllPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TblCmnAllPartsInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TblCmnAllPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TblCmnSelectedPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        TblCmnSelectedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TblCmnSelectedPartInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TblCmnSelectedPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        for (Part p: Main.inventory.getAllParts()) {
            allParts.add(p);
        }
        TbleAvailableParts.itemsProperty().bind(allPartListProperty);
        TbleSelectedParts.itemsProperty().bind(selectedPartListProperty);
        TxtBxID.setText(Integer.toString(Main.inventory.getNextProductID()));
    }


    /**
     * Event for when the Add button is clicked.
     * @param event The Mouse event
     */
    @FXML
    void BtnAddOnMouseClick(MouseEvent event) {
        if (TbleAvailableParts.getSelectionModel().getSelectedItem() != null) {
            Part partToAdd = TbleAvailableParts.getSelectionModel().getSelectedItem();
            selectedParts.add(partToAdd);
        }
    }

    /**
     * Event for when the Cancel button is clicked.
     * @param event The mouse event
     */
    @FXML
    void BtnCancelOnMouseClick(MouseEvent event) {
        Stage addProductStage = (Stage) BtnSave.getScene().getWindow();
        addProductStage.close();
    }

    /**
     * Event for when the Remove button is clicked.
     * @param event The mouse event
     */
    @FXML
    void BtnRemoveOnMouseClick(MouseEvent event) {
        if (TbleSelectedParts.getSelectionModel().getSelectedItem() != null) {
            Part partToRemove = TbleSelectedParts.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Part Removal");
            alert.setContentText("Are you sure you wish to remove " + partToRemove.getName() + "?");
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes) {
                selectedParts.remove(partToRemove);
            }
        }
    }

    /**
     * Event for when the Save button is clicked.
     * RUNTIME ERROR: Added more specific errors.
     * @param event The mouse event
     */
    @FXML
    void BtnSaveOnMouseClick(MouseEvent event) {
        try {
            //Test that the values are correct.
            int min = Integer.parseInt(TxtBxMin.getText());
            int max = Integer.parseInt(TxtBxMax.getText());
            int inv = Integer.parseInt(TxtBxInv.getText());
            boolean valueFlag = false;
            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Min cannot be greater than Max", ButtonType.OK);
                alert.showAndWait();
                valueFlag = true;
            }
            if (inv > max || inv < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inv has to be between Min and Max", ButtonType.OK);
                alert.showAndWait();
                valueFlag = true;
            }
            if(!valueFlag)
            {
                Product newProduct = new Product(Main.inventory.getNextProductID(), TxtBxName.getText(), Double.parseDouble(TxtBxPrice.getText()), inv, min, max);
                newProduct.addAssociatedPart(selectedParts);
                Main.inventory.addProduct(newProduct);
                Stage addPartStage = (Stage) BtnCancel.getScene().getWindow();
                addPartStage.close();
            }
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please fill out all the fields", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Searches the Parts when the user presses enter on the search box.
     * @param event The event when the enter button is pressed
     */
    @FXML
    void TxtBxSearchProductOnEnterPressed(ActionEvent event) {
        String searchText = TxtBxSearchProduct.getText();
        allParts.clear();
        if (searchText == "" || searchText == null) {
            for (Part p: Main.inventory.getAllParts()) {
                allParts.add(p);
            }
        }
        else {
            for (Part p: Main.inventory.getAllParts()) {
                if (p.getName().contains(searchText) || Integer.toString(p.getId()).contains(searchText)){
                    allParts.add(p);
                }
            }
        }
    }

}
