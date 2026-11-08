package TheBusiness;

import TheBusiness.Business.Business;
import TheBusiness.CustomerManagement.CustomerProfile;
import TheBusiness.OrderManagement.Order;
import TheBusiness.OrderManagement.OrderItem;
import TheBusiness.ReportManagement.CustomerSalesRow;
import TheBusiness.ReportManagement.PricingReportService;
import TheBusiness.ReportManagement.ProductPriceRow;
import TheBusiness.Supplier.Supplier;
import TheBusiness.Supplier.SupplierReport;
import TheBusiness.Supplier.SupplierSummary;
import UserInterface.Reports.ReportViewerJPanel;
import java.util.List;
import javax.swing.JTable;

/**
 * Assignment 9 - Shai Rashada-Parasram.
 * Plain-Java smoke/regression test. Run this file to check the rubric without
 * needing JUnit or an external dependency. Throws an error if a check fails.
 */
public final class Assignment9SelfTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new IllegalStateException("FAIL: " + message);
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "true");
        final Business business = Assignment9DataGenerator.createBusiness();
        int suppliers = business.getSupplierDirectory().getSuplierList().size();
        check(suppliers == 50, "50 suppliers");
        int activeSuppliers = 0;
        int products = 0;
        for (Supplier supplier : business.getSupplierDirectory().getSuplierList()) {
            int n = supplier.getProductCatalog().getProductList().size();
            check(n == 0 || n == 50, "supplier has either 0 or 50 products");
            if (n != 0) activeSuppliers++;
            products += n;
        }
        check(activeSuppliers == 30, "30 active suppliers");
        check(products == 1500, "1,500 products");
        check(business.getCustomerDirectory().getCustomerList().size() == 300,
                "300 customers");

        int countOrders = 0;
        int countItems = 0;
        long totalSales = 0;
        for (CustomerProfile customer : business.getCustomerDirectory().getCustomerList()) {
            int n = customer.getCustomerOrders().size();
            check(n >= 1 && n <= 3, "1-3 orders per customer");
            countOrders += n;
            for (Order order : customer.getCustomerOrders()) {
                int m = order.getOrderItems().size();
                check(m >= 1 && m <= 10, "1-10 order items");
                check(order.getCustomer() == customer, "order belongs to customer");
                countItems += m;
                for (OrderItem item : order.getOrderItems()) {
                    check(item.getQuantity() > 0, "positive quantity");
                    check(item.getActualPrice() >= item.getSelectedProduct().getFloorPrice(),
                            "price above floor");
                    check(item.getActualPrice() <= item.getSelectedProduct().getCeilingPrice(),
                            "price below ceiling");
                    totalSales += item.getOrderItemTotal();
                }
            }
        }
        check(countOrders == business.getMasterOrderList().getOrders().size(),
                "master list and customer order counts equal");
        check(countOrders >= 300 && countOrders <= 900, "300-900 orders");
        check(countItems >= countOrders && countItems <= countOrders * 10,
                "order item count valid");

        PricingReportService service = new PricingReportService(business);
        List<ProductPriceRow> expensive = service.mostExpensiveProducts();
        check(expensive.size() == 1500, "report 1 has 1,500 products");
        for (int i = 1; i < expensive.size(); i++) {
            check(expensive.get(i-1).getPrice() >= expensive.get(i).getPrice(),
                    "report 1 descending prices");
        }
        List<CustomerSalesRow> customers = service.mostValuableCustomers();
        check(customers.size() == 300, "report 2 has 300 customers");
        long customerSales = 0;
        for (int i = 0; i < customers.size(); i++) {
            customerSales += customers.get(i).getTotalSales();
            if (i > 0) check(customers.get(i-1).getTotalSales() >=
                    customers.get(i).getTotalSales(), "report 2 descending sales");
        }
        check(totalSales == customerSales, "customer sales reconcile to orders");

        SupplierReport report = new SupplierReport(business);
        List<SupplierSummary> summary = report.getSummaries();
        check(summary.size() == 50, "report 3 contains every supplier");
        long supplierSales = 0;
        int zeroSuppliers = 0;
        for (int i = 0; i < summary.size(); i++) {
            SupplierSummary s = summary.get(i);
            supplierSales += s.getTotalSales();
            if (i > 0) check(summary.get(i-1).getTotalSales() >= s.getTotalSales(),
                    "report 3 descending sales");
            check(s.getLoyaltyScore() >= 0 && s.getLoyaltyScore() <= 1,
                    "loyalty ratio 0-1");
            check(s.getTopFiveSalesScore() >= 0 && s.getTopFiveSalesScore() <= 1.000001,
                    "top five ratio 0-1");
            check(Math.abs(s.getLoyaltyScore() - s.getDistinctCustomerCount()/300.0) < 1e-10,
                    "loyalty exact definition");
            if (s.getDistinctCustomerCount() == 0) {
                zeroSuppliers++;
                check(s.getTotalSales() == 0 && s.getAverageSpendingPerCustomer() == 0 &&
                        s.getTopFiveSalesScore() == 0, "zero supplier safe");
            } else {
                check(Math.abs(s.getAverageSpendingPerCustomer() -
                        (double) s.getTotalSales() / s.getDistinctCustomerCount()) < 1e-8,
                        "average spending correct");
            }
        }
        check(supplierSales == totalSales, "supplier sales reconcile to orders");
        check(zeroSuppliers >= 20, "20 zero-product suppliers handled");

        // Shai: Verify all three actual Swing JTable views with no display server.
        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
            @Override public void run() {
                ReportViewerJPanel viewer = new ReportViewerJPanel(business, () -> {});
                JTable table = viewer.getReportTable();
                check(table.getRowCount() == 1500, "JTable report 1 rows");
                check(table.getColumnCount() == 3, "JTable report 1 columns");
                check(table.getRowSorter() != null, "JTable sorting enabled");
                viewer.selectReport(1);
                check(table.getRowCount() == 300 && table.getColumnCount() == 2,
                        "JTable report 2 shape");
                viewer.selectReport(2);
                check(table.getRowCount() == 50 && table.getColumnCount() == 5,
                        "JTable report 3 shape");
                check(table.getModel().getColumnClass(1) == Long.class,
                        "Total sales stays a sortable number");
            }
        });
        check(business.getUserAccountDirectory().AuthenticateUser("admin", "XXXX") != null,
                "Admin login remains available");
        System.out.println("PASS: 1 business, " + suppliers + " suppliers, " + activeSuppliers +
                " stocked suppliers, " + products + " products, 300 customers, " +
                countOrders + " orders, " + countItems + " order items.");
        System.out.println("PASS: all 3 reports, sorting, numeric types, supplier formulas, " +
                "zero-sales suppliers, total sales reconciliation and JTable views.");
        System.out.println("PASS: $" + totalSales + " in sales reconciles for all reports.");
    }
}
