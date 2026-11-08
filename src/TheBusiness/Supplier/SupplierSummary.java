package TheBusiness.Supplier;

/**
 * Assignment 9 - Report #3 row. Author: Shai Rashada-Parasram.
 * Score ratios are 0.0 to 1.0; the viewer formats them as percentages.
 */
public final class SupplierSummary {
    private final String supplierName;
    private final long totalSales;
    private final int distinctCustomerCount;
    private final double loyaltyScore;
    private final double averageSpendingPerCustomer;
    private final double topFiveSalesScore;

    public SupplierSummary(String name, long sales, int distinctCustomers, int allCustomers,
            long topFiveCustomerSales) {
        this.supplierName = name;
        this.totalSales = sales;
        this.distinctCustomerCount = distinctCustomers;
        // Shai: Zero-safe for a supplier that has no products or no sales.
        this.loyaltyScore = allCustomers == 0 ? 0.0 :
                (double) distinctCustomers / allCustomers;
        this.averageSpendingPerCustomer = distinctCustomers == 0 ? 0.0 :
                (double) sales / distinctCustomers;
        this.topFiveSalesScore = sales == 0 ? 0.0 :
                (double) topFiveCustomerSales / sales;
    }

    public String getSupplierName() { return supplierName; }
    public long getTotalSales() { return totalSales; }
    public int getDistinctCustomerCount() { return distinctCustomerCount; }
    public double getLoyaltyScore() { return loyaltyScore; }
    public double getAverageSpendingPerCustomer() { return averageSpendingPerCustomer; }
    public double getTopFiveSalesScore() { return topFiveSalesScore; }
}
