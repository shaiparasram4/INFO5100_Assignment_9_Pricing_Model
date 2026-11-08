package TheBusiness;

import MarketingManagement.MarketingPersonProfile;
import TheBusiness.Business.Business;
import TheBusiness.CustomerManagement.CustomerProfile;
import TheBusiness.OrderManagement.Order;
import TheBusiness.Personnel.EmployeeProfile;
import TheBusiness.Personnel.Person;
import TheBusiness.ProductManagement.Product;
import TheBusiness.SalesManagement.SalesPersonProfile;
import TheBusiness.Supplier.Supplier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Assignment 9 - Pricing Model, Part 2.
 * Author: Shai Rashada-Parasram.
 * Generates a reproducible, realistically linked dataset for all three reports.
 * All quantities and prices are generated programmatically, not hard-coded records.
 */
public final class Assignment9DataGenerator {
    public static final int SUPPLIER_COUNT = 50;
    public static final int ACTIVE_SUPPLIER_COUNT = 30;
    public static final int PRODUCTS_PER_ACTIVE_SUPPLIER = 50;
    public static final int CUSTOMER_COUNT = 300;

    private Assignment9DataGenerator() { }

    /** Create exactly one Business and its suppliers, products, people, customers and orders. */
    public static Business createBusiness() {
        Business business = new Business("Xerox Pricing Analytics");
        // Shai: A fixed seed lets the professor reproduce the same random results.
        Random random = new Random(5100L);

        List<Supplier> allSuppliers = new ArrayList<Supplier>();
        for (int s = 1; s <= SUPPLIER_COUNT; s++) {
            allSuppliers.add(business.getSupplierDirectory().newSupplier(
                    String.format("Supplier %02d", s)));
        }

        // Shai: Randomly select exactly 30 of the 50 suppliers to sell 50 products each.
        List<Supplier> selectedSuppliers = new ArrayList<Supplier>(allSuppliers);
        Collections.shuffle(selectedSuppliers, random);
        List<Product> productsForOrdering = new ArrayList<Product>();
        for (Supplier supplier : selectedSuppliers.subList(0, ACTIVE_SUPPLIER_COUNT)) {
            for (int p = 1; p <= PRODUCTS_PER_ACTIVE_SUPPLIER; p++) {
                int floor = 500 + random.nextInt(4501); // $500 to $5,000
                int ceiling = floor + 500 + random.nextInt(2501);
                int target = floor + (ceiling - floor) / 2;
                Product product = supplier.getProductCatalog().newProduct(
                        supplier.getName() + " - Product " + String.format("%02d", p),
                        floor, ceiling, target);
                productsForOrdering.add(product);
            }
        }

        // Shai: Preserve working demo roles from the skeleton so existing screens still work.
        Person salesPerson = business.getPersonDirectory().newPerson("Xerox sales");
        Person marketingPerson = business.getPersonDirectory().newPerson("Xerox marketing");
        Person adminPerson = business.getPersonDirectory().newPerson("Xerox admin");
        SalesPersonProfile salesProfile = business.getSalesPersonDirectory()
                .newSalesPersonProfile(salesPerson);
        MarketingPersonProfile marketingProfile = business.getMarketingPersonDirectory()
                .newMarketingPersonProfile(marketingPerson);
        EmployeeProfile adminProfile = new EmployeeProfile(adminPerson);
        business.getUserAccountDirectory().newUserAccount(salesProfile, "sales", "XXXX");
        business.getUserAccountDirectory().newUserAccount(marketingProfile, "marketing", "XXXX");
        business.getUserAccountDirectory().newUserAccount(adminProfile, "admin", "XXXX");

        // Shai: Every one of the 300 customers receives 1-3 orders, with 1-10 items each.
        for (int c = 1; c <= CUSTOMER_COUNT; c++) {
            Person person = business.getPersonDirectory().newPerson(
                    String.format("Customer %03d", c));
            CustomerProfile customer = business.getCustomerDirectory().newCustomerProfile(person);
            int orderCount = 1 + random.nextInt(3);
            for (int o = 0; o < orderCount; o++) {
                Order order = business.getMasterOrderList().newOrder(customer, salesProfile);
                int itemCount = 1 + random.nextInt(10);
                for (int i = 0; i < itemCount; i++) {
                    Product product = productsForOrdering.get(random.nextInt(productsForOrdering.size()));
                    // Shai: Actual sale price stays inside the product's allowed price range.
                    int salePrice = product.getFloorPrice() + random.nextInt(
                            product.getCeilingPrice() - product.getFloorPrice() + 1);
                    int quantity = 1 + random.nextInt(4);
                    order.newOrderItem(product, salePrice, quantity);
                }
                order.Submit();
            }
        }
        return business;
    }
}
