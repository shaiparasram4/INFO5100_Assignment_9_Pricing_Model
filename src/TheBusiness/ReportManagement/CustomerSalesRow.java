package TheBusiness.ReportManagement;

/** Assignment 9 report row. Author: Shai Rashada-Parasram. */
public final class CustomerSalesRow {
    private final String customerName;
    private final long totalSales;

    public CustomerSalesRow(String customerName, long totalSales) {
        this.customerName = customerName;
        this.totalSales = totalSales;
    }
    public String getCustomerName() { return customerName; }
    public long getTotalSales() { return totalSales; }
}
