/**
 * Class used for InHouse Parts.
 * @author Daniel Harper
 */
public class InHouse extends Part {

    private int machineId;

    /**
     * Parameterized Constructor.
     * @param id ID of the part
     * @param name String name of the part
     * @param price Double price of the part
     * @param stock Inventory
     * @param min Minimum inventory to have
     * @param max Maximum inventory
     * @param machineId MachineID for this part
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Returns the MachineID of this InHouse part
     * @return Returns the machineId
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * Sets the MachineID of this part
     * @param machineId Sets the machineId
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
