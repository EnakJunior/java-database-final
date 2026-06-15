package com.project.code.Service;

import com.project.code.Model.*;
import com.project.code.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

// 1. **saveOrder Method**:
//    - Processes a customer's order, including saving the order details and associated items.
//    - Parameters: `PlaceOrderRequestDTO placeOrderRequest` (Request data for placing an order)
//    - Return Type: `void` (This method doesn't return anything, it just processes the order)
    public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) {

// 2. **Retrieve or Create the Customer**:
//    - Check if the customer exists by their email using `findByEmail`.
//    - If the customer exists, use the existing customer; otherwise, create and save a new customer using `customerRepository.save()`.
        Customer customer = new Customer();
        if (customerRepository.findByEmail(placeOrderRequest.getCustomerEmail()) == null) {

        customer.setName(placeOrderRequest.getCustomerName());
        customer.setEmail(placeOrderRequest.getCustomerEmail());
        customer.setPhone(placeOrderRequest.getCustomerPhone());
        customerRepository.save(customer);
    } else {
        customer = customerRepository.findByEmail(placeOrderRequest.getCustomerEmail());
    }


// 3. **Retrieve the Store**:
//    - Fetch the store by ID from `storeRepository`.
//    - If the store doesn't exist, throw an exception. Use `storeRepository.findById()`.
        storeRepository.findById(placeOrderRequest.getStoreId()).orElseThrow(() -> new RuntimeException("Store not found"));

// 4. **Create OrderDetails**:
//    - Create a new `OrderDetails` object and set customer, store, total price, and the current timestamp.
//    - Set the order date using `java.time.LocalDateTime.now()` and save the order with `orderDetailsRepository.save()`.
    OrderDetails orderDetails = new OrderDetails();
    orderDetails.setCustomer(customerRepository.findByEmail(placeOrderRequest.getCustomerEmail()));
    orderDetails.setStore(storeRepository.findById(placeOrderRequest.getStoreId()).orElseThrow(() -> new RuntimeException("Store not found")));
    orderDetails.setTotalPrice(placeOrderRequest.getTotalPrice());
    orderDetails.setDate(java.time.LocalDateTime.now());
    orderDetailsRepository.save(orderDetails);

// 5. **Create and Save OrderItems**:
//    - For each product purchased, find the corresponding inventory, update stock levels, and save the changes using `inventoryRepository.save()`.
//    - Create and save `OrderItem` for each product and associate it with the `OrderDetails` using `orderItemRepository.save()`.
        placeOrderRequest.getPurchaseProduct().stream().map(purchaseProductDTO -> {
            Product product = productRepository.findByName(purchaseProductDTO.getName());
            inventoryRepository.findByProductId(product.getId()).stream().map(inventory -> {
                inventory.setStockLevel(inventory.getStockLevel() - purchaseProductDTO.getQuantity());
                inventoryRepository.save(inventory);

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(orderDetails);
                orderItem.setProduct(product);
                orderItem.setQuantity(purchaseProductDTO.getQuantity());
                orderItemRepository.save(orderItem);

                return null;
            });

            return null;
        });

    }
   
}
