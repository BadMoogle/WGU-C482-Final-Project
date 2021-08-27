/**
 * Class for Outsourced Parts
 * @author Daniel Harper
 */
public class Outsourced extends Part {

    private String companyName;

    /**
     * Constructor
     * @param id ID to set for the Outsourced part
     * @param name Name to set for the Outsourced part
     * @param price Price to set for the outsourced part
     * @param stock Stock/Inventory to set for the outsourced part
     * @param min Min to set for the outsourced part
     * @param max Max to set for the outsourced part
     * @param companyName CompanyName to set for the outsourced part.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Returns the company name
     * @return Returns the Company Name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the company name
     * @param companyName Company Name to set this to
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
