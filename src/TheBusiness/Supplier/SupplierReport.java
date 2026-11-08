package TheBusiness.Supplier;

import TheBusiness.Business.Business;
import TheBusiness.CustomerManagement.CustomerProfile;
import TheBusiness.OrderManagement.Order;
import TheBusiness.OrderManagement.OrderItem;
import TheBusiness.ProductManagement.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Assignment 9 - Report #3. Author: Shai Rashada-Parasram.
 * Aggregates actual OrderItem sales by supplier and UNIQUE customer. This means
 * repeat purchases by the same customer raise sales, not the loyalty numerator.
 */
public final class SupplierReport {
    private final List<SupplierSummary> summaries;

    public SupplierReport(Business business) {
        Map<Product, Supplier> productOwners = new IdentityHashMap<Product, Supplier>();
        Map<Supplier, Map<CustomerProfile, Long>> salesBySupplier =
                new HashMap<Supplier, Map<CustomerProfile, Long>>();

        for (Supplier supplier : business.getSupplierDirectory().getSuplierList()) {
            salesBySupplier.put(supplier, new HashMap<CustomerProfile, Long>());
            for (Product product : supplier.getProductCatalog().getProductList()) {
                productOwners.put(product, supplier);
            }
        }

        // Shai: Collect actual item totals, not supplier price-list values.
        for (Order order : business.getMasterOrderList().getOrders()) {
            for (OrderItem item : order.getOrderItems()) {
                Supplier supplier = productOwners.get(item.getSelectedProduct());
                if (supplier == null) continue; // Defensively ignore non-catalog products.
                Map<CustomerProfile, Long> customerSales = salesBySupplier.get(supplier);
                Long previous = customerSales.get(order.getCustomer());
                customerSales.put(order.getCustomer(),
                        (previous == null ? 0L : previous) + item.getOrderItemTotal());
            }
        }

        summaries = new ArrayList<SupplierSummary>();
        int allCustomers = business.getCustomerDirectory().getCustomerList().size();
        for (Supplier supplier : business.getSupplierDirectory().getSuplierList()) {
            Map<CustomerProfile, Long> perCustomer = salesBySupplier.get(supplier);
            long total = 0L;
            List<Long> customerTotals = new ArrayList<Long>(perCustomer.values());
            for (Long amount : customerTotals) total += amount;
            Collections.sort(customerTotals, Collections.reverseOrder());
            long topFive = 0L;
            for (int i = 0; i < Math.min(5, customerTotals.size()); i++) {
                topFive += customerTotals.get(i);
            }
            summaries.add(new SupplierSummary(supplier.getName(), total,
                    perCustomer.size(), allCustomers, topFive));
        }
        // Shai: Supplier report starts with highest-selling suppliers first.
        Collections.sort(summaries, new Comparator<SupplierSummary>() {
            @Override public int compare(SupplierSummary a, SupplierSummary b) {
                return Long.compare(b.getTotalSales(), a.getTotalSales());
            }
        });
    }

    public List<SupplierSummary> getSummaries() {
        return Collections.unmodifiableList(summaries);
    }
}
