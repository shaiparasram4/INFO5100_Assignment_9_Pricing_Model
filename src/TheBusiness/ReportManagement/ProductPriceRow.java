package TheBusiness.ReportManagement;

/** Assignment 9 report row. Author: Shai Rashada-Parasram. */
public final class ProductPriceRow {
    private final String productName;
    private final String supplierName;
    private final int price;

    public ProductPriceRow(String productName, String supplierName, int price) {
        this.productName = productName;
        this.supplierName = supplierName;
        this.price = price;
    }
    public String getProductName() { return productName; }
    public String getSupplierName() { return supplierName; }
    public int getPrice() { return price; }
}
