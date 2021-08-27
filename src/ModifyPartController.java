import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller class for the Modify Parts form
 * @author Daniel Harper
 */
public class ModifyPartController {

    public static Part partToModify; //Object to modify

    @FXML
    private RadioButton RadInHouse;

    @FXML
    private ToggleGroup TGrpInHouseOutsourcedRadGrp;

    @FXML
    private RadioButton RadOutsourced;

    @FXML
    private TextField TxtBxID;

    @FXML
    private TextField TxtBxName;

    @FXML
    private TextField TxtBxInv;

    @FXML
    private TextField TxtBxPriceCost;

    @FXML
    private TextField TxtBxMax;

    @FXML
    private TextField TxtBxMachineID;

    @FXML
    private TextField TxtBxMin;

    @FXML
    private Label LabelMachineID;

    @FXML
    private Button BtnSave;

    @FXML
    private Button BtnCancel;

    /**
     * Initializes the form.  Runs after the fxml loads.
     */
    @FXML
    void initialize() {
        //Fill out the form with the partToModify
        TxtBxID.setText(Integer.toString(partToModify.getId()));
        TxtBxInv.setText(Integer.toString(partToModify.getStock()));
        TxtBxName.setText(partToModify.getName());
        TxtBxMax.setText(Integer.toString(partToModify.getMax()));
        TxtBxMin.setText(Integer.toString(partToModify.getMin()));
        TxtBxPriceCost.setText(Double.toString(partToModify.getPrice()));
        if (partToModify instanceof Outsourced) {
            TxtBxMachineID.setText(((Outsourced) partToModify).getCompanyName());
            RadOutsourced.setSelected(true);
        }
        else if (partToModify instanceof InHouse) {
            TxtBxMachineID.setText(Integer.toString(((InHouse) partToModify).getMachineId()));
            RadInHouse.setSelected(true);
        }
    }

    /**
     * If they press the Cancel button
     * @param event Mouse Event
     */
    @FXML
    void CancelButtonOnClick(MouseEvent event) {
        Stage addPartStage = (Stage) BtnCancel.getScene().getWindow();
        addPartStage.close();
    }

    /**
     * If they press the Save button
     * RUNTIME ERROR: Added error catching routines for more specific errors.
     * @param event The mouse event
     * @throws Exception Throws exceptions during the parsing of integers/doubles and if adding null parts to the inventories
     */
    @FXML
    void SaveButtonOnClick(MouseEvent event) throws Exception {
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
                //Add InHouse part
                if (RadInHouse.isSelected()) {
                    InHouse part = new InHouse(partToModify.getId(), TxtBxName.getText(), Double.parseDouble(TxtBxPriceCost.getText()), inv,
                            min, max, Integer.parseInt(TxtBxMachineID.getText()));
                    Main.inventory.updatePart(partToModify, part);
                    Stage addPartStage = (Stage) BtnCancel.getScene().getWindow();
                    addPartStage.close();
                }
                //Add Outsourced Part
                else {
                    Outsourced part = new Outsourced(partToModify.getId(), TxtBxName.getText(), Double.parseDouble(TxtBxPriceCost.getText()), inv,
                            min, max, TxtBxMachineID.getText());
                    Main.inventory.updatePart(partToModify, part);
                    Stage addPartStage = (Stage) BtnCancel.getScene().getWindow();
                    addPartStage.close();
                }
            }

        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please fill out all the fields", ButtonType.OK);
            alert.showAndWait();
        }
    }


    /**
     * Changes the label text when the different radio buttons are clicked.
     * @param event MouseEvent when RadInHouse is clicked
     */
    @FXML
    void RadInHouseOnClick(MouseEvent event) {
        if (RadInHouse.isSelected()) {
            LabelMachineID.setText("Machine ID");
        }
        else {
            LabelMachineID.setText("Company Name");
        }
    }

    /**
     * Changes the label text when the different radio buttons are clicked.
     * @param event MouseEvent when RadInHouse is clicked
     */
    @FXML
    void RadOutsourcedOnClick(MouseEvent event) {
        if (RadOutsourced.isSelected()) {
            LabelMachineID.setText("Company Name");
        }
        else {
            LabelMachineID.setText("Machine ID");
        }
    }
}
