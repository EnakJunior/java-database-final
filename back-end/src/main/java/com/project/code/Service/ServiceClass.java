package com.project.code.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceClass {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ProductRepository productRepository;
    
// 1. **validateInventory Method**:
//    - Checks if an inventory record exists for a given product and store combination.
//    - Parameters: `Inventory inventory`
//    - Return Type: `boolean` (Returns `false` if inventory exists, otherwise `true`)
    public boolean validateInventory(Inventory inventory) {
        // Implement logic to check if the inventory record exists for the given product and store combination.
        if (inventory.equals(inventoryRepository.findByProductIdandStoreId(inventory.getProduct().getId(), inventory.getStore().getId()))) {
            return false; // Inventory record exists
        }
        // Return false if it exists, true otherwise
        return true;
    }

// 2. **validateProduct Method**:
//    - Checks if a product exists by its name.
//    - Parameters: `Product product`
//    - Return Type: `boolean` (Returns `false` if a product with the same name exists, otherwise `true`)
    public boolean validateProduct(Product product) {
        // Implement logic to check if a product with the same name exists.
        if (product.equals(productRepository.findByName(product.getName()))) {
            return false; // Product with the same name exists
        }
        // Return false if it exists, true otherwise
        return true;
    }

// 3. **ValidateProductId Method**:
//    - Checks if a product exists by its ID.
//    - Parameters: `long id`
//    - Return Type: `boolean` (Returns `false` if the product does not exist with the given ID, otherwise `true`)
    public boolean ValidateProductId(long id) {
        // Implement logic to check if a product exists by its ID.
        if (productRepository.findById(id).isPresent()) {
            return true; // Product exists
        }
        // Return false if it does not exist, true otherwise
        return false;
    }

// 4. **getInventoryId Method**:
//    - Fetches the inventory record for a given product and store combination.
//    - Parameters: `Inventory inventory`
//    - Return Type: `Inventory` (Returns the inventory record for the product-store combination)
    public Inventory getInventoryId(Inventory inventory) {
        // Implement logic to fetch the inventory record for the given product and store combination.
        return inventoryRepository.findByProductIdandStoreId(inventory.getProduct().getId(), inventory.getStore().getId());
    }

}
