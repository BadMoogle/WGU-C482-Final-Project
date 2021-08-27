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
 * Controller class for the Modify product form.
 * @author Daniel Harper
 */
public class ModifyProductController {

    public static Product productToModify; //Object to modify

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
     * Runs after the form initializes
     */
    @FXML
    void initialize() {
        //Binds the columns to the two ObservableList objects
        TblCmnAllPartsID.setCellValueFactory(new PropertyValueFactory<>("id"));
        TblLmnAllPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TblCmnAllPartsInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TblCmnAllPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TblCmnSelectedPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        TblCmnSelectedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TblCmnSelectedPartInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TblCmnSelectedPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        //Populate the two tables and the text fields
        for (Part p: Main.inventory.getAllParts()) {
            allParts.add(p);
        }
        TbleAvailableParts.itemsProperty().bind(allPartListProperty);
        for (Part p : productToModify.getAllAssociatedParts()) {
            selectedParts.add(p);
        }
        TbleSelectedParts.itemsProperty().bind(selectedPartListProperty);
        TxtBxID.setText(Integer.toString(productToModify.getId()));
        TxtBxName.setText(productToModify.getName());
        TxtBxInv.setText(Integer.toString(productToModify.getStock()));
        TxtBxMin.setText(Integer.toString(productToModify.getMin()));
        TxtBxMax.setText(Integer.toString(productToModify.getMax()));
        TxtBxPrice.setText(Double.toString(productToModify.getPrice()));
    }

    /**
     * Event handler when the Add Part button is clicked.
     * @param event MouseEvent when the Add part button is clicked.
     */
    @FXML
    void BtnAddOnMouseClick(MouseEvent event) {
        if (TbleAvailableParts.getSelectionModel().getSelectedItem() != null) {
            Part partToAdd = TbleAvailableParts.getSelectionModel().getSelectedItem();
            selectedParts.add(partToAdd);
        }
    }

    /**
     * Event handler when the cancel button is clicked.
     * @param event MouseEvent when the Cancel button is clicked.
     */
    @FXML
    void BtnCancelOnMouseClick(MouseEvent event) {
        Stage modifyProductStage = (Stage) BtnSave.getScene().getWindow();
        modifyProductStage.close();
    }

    /**
     * Event handler when the remove part button is clicked.
     * @param event Mouse Event when the Remove Part button is clicked.
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
     * Even when the user clicks on the save button.
     * RUNTIME ERROR: Added error catching routines for more specific errors.
     * @param event Mouse event when the user clicks on the save button
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
                Product newProduct = new Product(productToModify.getId(), TxtBxName.getText(), Double.parseDouble(TxtBxPrice.getText()), inv, min, max);
                newProduct.addAssociatedPart(selectedParts);
                Main.inventory.updateProduct(productToModify, newProduct);
                Stage modifyProductStage = (Stage) BtnCancel.getScene().getWindow();
                modifyProductStage.close();
            }
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please fill out all the fields", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Search all the parts if they contain the string when the user presses enter on the search box.
     * @param event Event when the enter key is pressed
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
