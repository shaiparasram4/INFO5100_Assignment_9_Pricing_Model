package TheBusiness.ReportManagement;

import TheBusiness.Business.Business;
import TheBusiness.CustomerManagement.CustomerProfile;
import TheBusiness.OrderManagement.Order;
import TheBusiness.ProductManagement.Product;
import TheBusiness.Supplier.Supplier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Assignment 9 reports 1 and 2. Author: Shai Rashada-Parasram.
 * Keeps reporting calculations separate from Swing presentation code.
 */
public final class PricingReportService {
    private final Business business;

    public PricingReportService(Business business) {
        this.business = business;
    }

    /** Report #1: All products (1,500), most expensive TARGET price first. */
    public List<ProductPriceRow> mostExpensiveProducts() {
        List<ProductPriceRow> result = new ArrayList<ProductPriceRow>();
        for (Supplier supplier : business.getSupplierDirectory().getSuplierList()) {
            for (Product product : supplier.getProductCatalog().getProductList()) {
                result.add(new ProductPriceRow(product.getName(), supplier.getName(),
                        product.getTargetPrice()));
            }
        }
        // Shai: Integer.compare uses numeric ordering, not string/alphabetical ordering.
        Collections.sort(result, new Comparator<ProductPriceRow>() {
            @Override public int compare(ProductPriceRow a, ProductPriceRow b) {
                return Integer.compare(b.getPrice(), a.getPrice());
            }
        });
        return result;
    }

    /** Report #2: Every customer, total sales across ALL their orders, high to low. */
    public List<CustomerSalesRow> mostValuableCustomers() {
        List<CustomerSalesRow> result = new ArrayList<CustomerSalesRow>();
        for (CustomerProfile customer : business.getCustomerDirectory().getCustomerList()) {
            long sales = 0L;
            for (Order order : customer.getCustomerOrders()) {
                sales += order.getOrderTotal();
            }
            result.add(new CustomerSalesRow(customer.getCustomerId(), sales));
        }
        Collections.sort(result, new Comparator<CustomerSalesRow>() {
            @Override public int compare(CustomerSalesRow a, CustomerSalesRow b) {
                return Long.compare(b.getTotalSales(), a.getTotalSales());
            }
        });
        return result;
    }
}
