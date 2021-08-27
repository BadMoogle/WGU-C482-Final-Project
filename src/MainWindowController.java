import java.io.IOException;
import java.util.Optional;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Modality;

/**
 * Controller class for the main window
 * @author Daniel Harper
 */
public class MainWindowController {

    @FXML
    private TableView<Part> TblParts;

    @FXML
    private TextField TxtBxPartsSearch;

    @FXML
    private Button BtnPartsAdd;

    @FXML
    private Button BtnPartsModify;

    @FXML
    private Button BtnPartsDelete;

    @FXML
    private TableView<Product> TblProducts;

    @FXML
    private TextField TxtBxProductsSearch;

    @FXML
    private Button BtnProductsAdd;

    @FXML
    private Button BtnProductsModify;

    @FXML
    private Button BtnProductsDelete;

    @FXML
    private TableColumn<Part, String> TblCmnPartId;

    @FXML
    private TableColumn<Part, String> TblCmnPartName;

    @FXML
    private TableColumn<Part, Integer> TblCmnPartInventory;

    @FXML
    private TableColumn<Part, Double> TblCmnPartPrice;

    @FXML
    private TableColumn<Product, String> TblCmnProductsId;

    @FXML
    private TableColumn<Product, String> TblCmnProductsName;

    @FXML
    private TableColumn<Product, Integer> TblCmnProductsInventory;

    @FXML
    private TableColumn<Product, Double> TblCmnProductsPrice;

    @FXML
    private Button BtnExit;

    private ObservableList<Part> availableParts = FXCollections.observableArrayList();
    private ObservableList<Product> availableProducts = FXCollections.observableArrayList();

    private Property<ObservableList<Part>> partListProperty = new SimpleObjectProperty<>(availableParts);
    private Property<ObservableList<Product>> productListProperty = new SimpleObjectProperty<>(availableProducts);


    /**
     * This method is called by the FXMLLoader when initialization of the form is complete
     */
    @FXML
    void initialize() {
        TblCmnPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TblCmnPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TblCmnPartInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TblCmnPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TblCmnProductsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TblCmnProductsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TblCmnProductsInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TblCmnProductsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        for (Part p : Main.inventory.getAllParts()) {
            availableParts.add(p);
        }
        for (Product p : Main.inventory.getAllProducts()) {
            availableProducts.add(p);
        }
        TblParts.itemsProperty().bind(partListProperty);
        TblProducts.itemsProperty().bind(productListProperty);
    }

    /**
     * Listener event for the Add Parts button
     * @param event Event raised during the mouse action
     * @throws IOException Throws exception if unable to load the fxml document
     */
    @FXML
    void BtnPartsAddListener(ActionEvent event) throws IOException {
        Stage addPartStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        addPartStage.setTitle("Add Part");
        addPartStage.setScene(new Scene(root, 615, 412));
        addPartStage.setResizable(false);
        addPartStage.initModality(Modality.APPLICATION_MODAL);
        addPartStage.showAndWait();
        availableParts.clear();
        for (Part p : Main.inventory.getAllParts()) {
            availableParts.add(p);
        }
        BtnPartsModify.setDisable(true);
        BtnPartsDelete.setDisable(true);
    }

    /**
     * Event handler for when the Delete Parts button is clicked
     * @param event Event for the action on the button
     */
    @FXML
    void BtnPartsDeleteListener(ActionEvent event) {
        if (TblParts.getSelectionModel().getSelectedItem() != null) {
            Part partToDelete = TblParts.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Part Deletion");
            alert.setContentText("Are you sure you wish to delete " + partToDelete.getName() + "?");
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes) {
                try {
                    Main.inventory.deletePart(partToDelete);
                    availableParts.remove(partToDelete);
                }
                catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Part is used in a product", ButtonType.OK);
                    errorAlert.showAndWait();
                }
            }
        }
    }

    /**
     * Event handler for when the Modify Parts button is clicked
     * @param event Event raised by interacting with the Modify parts button
     * @throws IOException Throws exception if unable to load the fxml file
     */
    @FXML
    void BtnPartsModifyListener(ActionEvent event) throws IOException {
        if (TblParts.getSelectionModel().getSelectedItem() != null) {
            ModifyPartController.partToModify = TblParts.getSelectionModel().getSelectedItem();
            Stage modifyPartStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("ModifyPart.fxml"));
            modifyPartStage.setTitle("Modify Part");
            modifyPartStage.setScene(new Scene(root, 615, 412));
            modifyPartStage.setResizable(false);
            modifyPartStage.initModality(Modality.APPLICATION_MODAL);
            modifyPartStage.showAndWait();
            availableParts.clear();
            for (Part p : Main.inventory.getAllParts()) {
                availableParts.add(p);
            }
            BtnPartsModify.setDisable(true);
            BtnPartsDelete.setDisable(true);
        }
    }

    /**
     * Event handler for when the Add Product button is pressed
     * @param event Event for when the Add Products button is clicked
     * @throws IOException Throws exception if unable to load the fxml file
     */
    @FXML
    void BtnProductsAddListener(ActionEvent event) throws IOException {
        Stage addProductsStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        addProductsStage.setTitle("Add Product");
        addProductsStage.setScene(new Scene(root, 1033, 622));
        addProductsStage.setResizable(false);
        addProductsStage.initModality(Modality.APPLICATION_MODAL);
        addProductsStage.showAndWait();
        availableProducts.clear();
        for (Product p : Main.inventory.getAllProducts()) {
            availableProducts.add(p);
        }
        BtnProductsModify.setDisable(true);
        BtnProductsDelete.setDisable(true);
    }

    /**
     * Event handler for when the Delete Products button is pressed.
     * @param event ActionEvent for when the delete products button is pressed
     */
    @FXML
    void BtnProductsDeleteListener(ActionEvent event) {
        if (TblProducts.getSelectionModel().getSelectedItem() != null) {
            Product productToDelete = TblProducts.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Part Deletion");
            alert.setContentText("Are you sure you wish to delete " + productToDelete.getName() + "?");
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes) {
                try {
                    Main.inventory.deleteProduct(productToDelete);
                    availableProducts.remove(productToDelete);
                }
                catch (Exception e) {
                    Alert exceptionAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                    exceptionAlert.showAndWait();
                }
            }
        }
    }

    /**
     * Event handler for the modify products button.
     * @param event Event raised for the Modify Products button
     * @throws IOException Throws exception if unable to load fxml file
     */
    @FXML
    void BtnProductsModifyListener(ActionEvent event) throws IOException {
        if (TblProducts.getSelectionModel().getSelectedItem() != null) {
            ModifyProductController.productToModify = TblProducts.getSelectionModel().getSelectedItem();
            Stage modifyProductsStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));
            modifyProductsStage.setTitle("Modify Product");
            modifyProductsStage.setScene(new Scene(root, 1033, 622));
            modifyProductsStage.setResizable(false);
            modifyProductsStage.initModality(Modality.APPLICATION_MODAL);
            modifyProductsStage.showAndWait();
            availableProducts.clear();
            for (Product p : Main.inventory.getAllProducts()) {
                availableProducts.add(p);
            }
            BtnProductsModify.setDisable(true);
            BtnProductsDelete.setDisable(true);
        }
    }


    /**
     * Event handler when the Parts table is clicked on.
     * @param event Mouseclick event when the table is clicked on
     */
    @FXML
    void TblPartsOnMouseClick(MouseEvent event) {
        if (TblParts.getSelectionModel().getSelectedItem() != null) {
            BtnPartsModify.setDisable(false);
            BtnPartsDelete.setDisable(false);
        }
        else {
            BtnPartsModify.setDisable(true);
            BtnPartsDelete.setDisable(true);
        }
    }

    /**
     * Event handler when the Products table is clicked on.
     * @param event Mouseclick event when the table is clicked on
     */
    @FXML
    void TblProductsOnMouseClick(MouseEvent event) {
        if (TblProducts.getSelectionModel().getSelectedItem() != null) {
            BtnProductsModify.setDisable(false);
            BtnProductsDelete.setDisable(false);
        }
        else {
            BtnProductsModify.setDisable(true);
            BtnProductsDelete.setDisable(true);
        }
    }

    /**
     * Searches the parts list if the enter key is pressed.
     * @param event ActionEvent when the Textbox for Parts search has an enter pressed
     */
    @FXML
    void TxtBxPartsOnEnterPressed(ActionEvent event) {
        String searchText = TxtBxPartsSearch.getText();
        availableParts.clear();
        if (searchText == "" || searchText == null) {
            for (Part p: Main.inventory.getAllParts()) {
                availableParts.add(p);
            }
        }
        else {
            for (Part p: Main.inventory.getAllParts()) {
                if (p.getName().toUpperCase().contains(searchText.toUpperCase()) || Integer.toString(p.getId()).contains(searchText)){
                    availableParts.add(p);
                }
            }
        }
    }

    /**
     * Searches the product list if the enter key is pressed.
     * @param event ActionEvent when the Text Box for the Product search has an enter key pressed.
     */
    @FXML
    void TxtBxProductsOnEnterPressed(ActionEvent event) {
        String searchText = TxtBxProductsSearch.getText();
        availableProducts.clear();
        if (searchText == "" || searchText == null) {
            for (Product p: Main.inventory.getAllProducts()) {
                availableProducts.add(p);
            }
        }
        else {
            //FUTURE ENHANCEMENT: Pass this search off to the main inventory object.  Original class specifications would require adding additional methods.
            for (Product p: Main.inventory.getAllProducts()) {
                if (p.getName().toUpperCase().contains(searchText.toUpperCase()) || Integer.toString(p.getId()).contains(searchText)){
                    availableProducts.add(p);
                }
            }
        }
    }

    /**
     * Event handler when the Exit button is clicked.
     * @param event MouseEvent when the Exit button is clicked.
     */
    @FXML
    void BtnExitOnMouseClick(MouseEvent event) {
        Stage mainWindowStage = (Stage) BtnExit.getScene().getWindow();
        mainWindowStage.close();
    }
}
