import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Inventory class used to house all the inventory data for all parts and products
 * @author Daniel Harper
 */
public class Inventory {
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private int nextPartID = 1;
    private int nextProductID = 1;

    /**
     * Default constructor
     */
    public Inventory() {
    }

    /**
     * Adds a part to the inventory.
     * @param newPart New Part to be added
     * @throws Exception Throws exception if parameter is null.
     */
    public void addPart(Part newPart) throws Exception {
        if(newPart == null) {
            throw new Exception("Part cannot be null");
        }
        allParts.add((newPart));
        nextPartID++;
    }

    /**
     * Adds a new product to the inventory.
     * @param newProduct New Product to be added
     * @throws Exception Throws exception if parameter is null.
     */
    public void addProduct(Product newProduct) throws Exception {
        if(newProduct == null) {
            throw new Exception("Product cannot be null");
        }
        allProducts.add((newProduct));
        nextProductID++;
    }

    /**
     * Lookup the part based on its PartID.
     * @param partId Part ID to search for
     * @return The part if found, null otherwise
     */
    public Part lookupPart(int partId) {
        for (Part partSearch : allParts) {
            if (partSearch.getId() == partId) {
                return partSearch;
            }
        }
        return null;
    }

    /**
     * Lookup the product based on its ProductID.
     * @param productId Product ID to search for
     * @return The Product if found, null otherwise
     */
    public Product lookupProduct(int productId) {
        for (Product productSearch : allProducts) {
            if (productSearch.getId() == productId) {
                return productSearch;
            }
        }
        return null;
    }

    /**
     * Searches all parts that contain the partName parameter in their name.
     * @param partName Part Name to be searched for (case insensitive)
     * @return an ObservableList of found matches, null if none found
     */
    public ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> tempList = null;
        for (Part partSearch : allParts) {
            if (partSearch.getName().toUpperCase().contains(partName.toUpperCase())) {
                tempList.add(partSearch);
            }
        }
        return tempList;
    }

    /**
     * Searches all products that contain the productName parameter in their name.
     * @param productName Product Name to be searched for (case insensitive)
     * @return an ObservableList of found matches, null if none found
     */
    public ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> tempList = null;
        for (Product productSearch : allProducts) {
            if (productSearch.getName().toUpperCase().contains(productName.toUpperCase())) {
                tempList.add(productSearch);
            }
        }
        return tempList;
    }

    /**
     * Updates the part at the specified index
     * @param index Index to update part at
     * @param selectedPart Part to set at the specified index
     */
    public void updatePart(int index, Part selectedPart) {
        allParts.set(index,selectedPart);
    }

    /**
     * Updates the part given the original part
     * FUTURE ENHANCEMENT: Update the part instead of removing it, then adding it.
     * @param originalPart Originalpart to remove
     * @param updatedPart Part to update to
     * @throws NullPointerException if part is null;
     */
    public void updatePart(Part originalPart, Part updatedPart) throws NullPointerException {
        for (Product p : allProducts) {
            if (p.getAllAssociatedParts().contains(originalPart)) {
                p.deleteAssociatedPart(originalPart);
                try {
                    p.addAssociatedPart(updatedPart);
                }
                catch (Exception e) {
                    throw new NullPointerException();
                }
            }
        }
        allParts.removeAll(originalPart);
        allParts.add(updatedPart);
    }

    /**
     * Updates the product at the specified index
     * @param index Index to update product at
     * @param newProduct Product to set at the specified index
     */
    public void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * Updates the product given the original part
     * @param originalProduct Original product to remove
     * @param updatedProduct Updated product to add
     */
    public void updateProduct(Product originalProduct, Product updatedProduct) {
        allProducts.removeAll(originalProduct);
        allProducts.add(updatedProduct);
    }

    /**
     * Deletes the specified part from the list
     * FUTURE ENHANCEMENT: Why this program allows the deletion of a part when it may be apart of a product is unknown.  A product consists of parts, not the other way around.
     * @param selectedPart Deletes all parts that match this part
     * @return true if removed, false if none removed
     */
    public boolean deletePart(Part selectedPart) {
        return allParts.removeAll(selectedPart);
    }

    /**
     * Deletes the specified product from the list
     * @param selectedProduct Deletes all products that match this products
     * @return true if removed, false if none removed
     * @throws Exception if product has associated parts
     */
    public boolean deleteProduct(Product selectedProduct) throws Exception {
        if (selectedProduct.getAllAssociatedParts().stream().count() != 0) {
            throw new Exception("Product has associated parts");
        }
        return allProducts.removeAll(selectedProduct);
    }

    /**
     * Returns an ObservableList of all the parts in inventory.
     * FUTURE ENHANCEMENT: Sort the list by ID.
     * @return Returns an ObservableList of all parts
     */
    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Returns an ObservableList of all the products in inventory.
     * FUTURE ENHANCEMENT: Sort the list by ID
     * @return Returns an ObservableList of all products
     */
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Returns the next available PartID.
     * @return The next available PartID
     */
    public int getNextPartID() { return nextPartID; };

    /**
     * Returns the next available ProductID.
     * @return The next available ProductID
     */
    public int getNextProductID() { return nextProductID; };
}
