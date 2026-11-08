package UserInterface.Reports;

import TheBusiness.Business.Business;
import TheBusiness.OrderManagement.Order;
import TheBusiness.ReportManagement.CustomerSalesRow;
import TheBusiness.ReportManagement.PricingReportService;
import TheBusiness.ReportManagement.ProductPriceRow;
import TheBusiness.Supplier.SupplierReport;
import TheBusiness.Supplier.SupplierSummary;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * Assignment 9 - Shai Rashada-Parasram.
 * Report Viewer JPanel: dropdown changes between three assignment reports,
 * each displayed in a JTable with clickable, properly typed sort columns.
 * Built in plain Swing to work with the existing NetBeans 8+ project.
 */
public final class ReportViewerJPanel extends JPanel {
    private final Business business;
    private final PricingReportService service;
    private final JComboBox<String> reportSelector;
    private final JTable reportTable;
    private final JLabel reportInfo;

    public ReportViewerJPanel(Business business, Runnable onBack) {
        this.business = business;
        this.service = new PricingReportService(business);
        setLayout(new BorderLayout(14, 14));
        setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Assignment 9 | Pricing Analytics Reports");
        title.setFont(new Font("SansSerif", Font.BOLD, 23));
        title.setForeground(new Color(30, 40, 54));

        int productCount = 0;
        for (TheBusiness.Supplier.Supplier supplier :
                business.getSupplierDirectory().getSuplierList()) {
            productCount += supplier.getProductCatalog().getProductList().size();
        }
        int orderItems = 0;
        for (Order order : business.getMasterOrderList().getOrders()) {
            orderItems += order.getOrderItems().size();
        }
        JLabel overview = new JLabel(String.format(
                "1 Business   |   %d Suppliers   |   %,d Products   |   %d Customers   |   %d Orders   |   %,d Order Items",
                business.getSupplierDirectory().getSuplierList().size(), productCount,
                business.getCustomerDirectory().getCustomerList().size(),
                business.getMasterOrderList().getOrders().size(), orderItems));
        overview.setForeground(new Color(60, 72, 87));
        overview.setFont(new Font("SansSerif", Font.PLAIN, 12));

        reportSelector = new JComboBox<String>(new String[]{
            "Report 1 - Most Expensive Products",
            "Report 2 - Most Valuable Customers",
            "Report 3 - Supplier Report & Summary"
        });
        reportSelector.setFont(new Font("SansSerif", Font.PLAIN, 14));
        reportSelector.setPreferredSize(new Dimension(355, 33));
        reportSelector.getAccessibleContext().setAccessibleName("Select pricing report");
        reportSelector.getAccessibleContext().setAccessibleDescription(
                "Choose most expensive products, valuable customers or supplier report");

        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(e -> onBack.run());
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 3));
        actions.setOpaque(false);
        actions.add(new JLabel("Choose Report:"));
        actions.add(reportSelector);
        actions.add(backButton);

        reportInfo = new JLabel(" ");
        reportInfo.setForeground(new Color(65, 74, 84));
        reportInfo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JPanel header = new JPanel();
        header.setLayout(new javax.swing.BoxLayout(header, javax.swing.BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.add(title);
        header.add(javax.swing.Box.createVerticalStrut(8));
        header.add(overview);
        header.add(javax.swing.Box.createVerticalStrut(14));
        header.add(actions);
        header.add(javax.swing.Box.createVerticalStrut(7));
        header.add(reportInfo);
        add(header, BorderLayout.NORTH);

        reportTable = new JTable();
        reportTable.setRowHeight(27);
        reportTable.setShowGrid(false);
        reportTable.setShowHorizontalLines(true);
        reportTable.setGridColor(new Color(225, 230, 233));
        reportTable.setFillsViewportHeight(true);
        reportTable.getTableHeader().setReorderingAllowed(false);
        reportTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reportTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        reportTable.getAccessibleContext().setAccessibleName("Assignment 9 report results");
        add(new JScrollPane(reportTable), BorderLayout.CENTER);

        JLabel footer = new JLabel("Select a report above. Click column headers to sort ascending or descending.");
        footer.setForeground(new Color(75, 85, 95));
        add(footer, BorderLayout.SOUTH);
        reportSelector.addActionListener(e -> displayReport(reportSelector.getSelectedIndex()));
        displayReport(0);
    }

    // Shai: Test helpers also let the professor confirm each report works.
    public void selectReport(int index) { reportSelector.setSelectedIndex(index); }
    public JTable getReportTable() { return reportTable; }

    private void displayReport(int choice) {
        List<Object[]> rows = new ArrayList<Object[]>();
        String[] headers;
        Class<?>[] classes;
        int defaultSort;
        int[] currencyColumns;
        int[] percentColumns;
        if (choice == 0) {
            headers = new String[]{"Product Name", "Supplier Name", "Product Price ($)"};
            classes = new Class<?>[]{String.class, String.class, Integer.class};
            defaultSort = 2;
            currencyColumns = new int[]{2};
            percentColumns = new int[]{};
            for (ProductPriceRow product : service.mostExpensiveProducts()) {
                rows.add(new Object[]{product.getProductName(), product.getSupplierName(),
                    product.getPrice()});
            }
            reportInfo.setText("1,500 products from 30 suppliers. Price = target/list price. Highest first.");
        } else if (choice == 1) {
            headers = new String[]{"Customer Name", "Total Sales ($)"};
            classes = new Class<?>[]{String.class, Long.class};
            defaultSort = 1;
            currencyColumns = new int[]{1};
            percentColumns = new int[]{};
            for (CustomerSalesRow customer : service.mostValuableCustomers()) {
                rows.add(new Object[]{customer.getCustomerName(), customer.getTotalSales()});
            }
            reportInfo.setText("300 customers. Total sales sums each customer's orders; highest first.");
        } else {
            headers = new String[]{"Supplier Name", "Total Sales ($)", "Loyalty Score",
                "Avg Spending / Customer ($)", "Top 5 Sales Score"};
            classes = new Class<?>[]{String.class, Long.class, Double.class,
                Double.class, Double.class};
            defaultSort = 1;
            currencyColumns = new int[]{1, 3};
            percentColumns = new int[]{2, 4};
            for (SupplierSummary supplier : new SupplierReport(business).getSummaries()) {
                rows.add(new Object[]{supplier.getSupplierName(), supplier.getTotalSales(),
                    supplier.getLoyaltyScore(), supplier.getAverageSpendingPerCustomer(),
                    supplier.getTopFiveSalesScore()});
            }
            reportInfo.setText("50 suppliers. Loyalty = unique buyers / 300; Top 5 = top buyer sales / supplier sales.");
        }

        final Class<?>[] columnClasses = classes;
        DefaultTableModel model = new DefaultTableModel(headers, 0) {
            @Override public Class<?> getColumnClass(int col) { return columnClasses[col]; }
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        for (Object[] row : rows) model.addRow(row);
        reportTable.setModel(model);

        // Shai: Preserve numeric values in the model and only format their display.
        for (int col : currencyColumns) {
            reportTable.getColumnModel().getColumn(col).setCellRenderer(new NumberRenderer(false));
        }
        for (int col : percentColumns) {
            reportTable.getColumnModel().getColumn(col).setCellRenderer(new NumberRenderer(true));
        }
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        reportTable.setRowSorter(sorter);
        sorter.setSortKeys(Collections.singletonList(
                new RowSorter.SortKey(defaultSort, SortOrder.DESCENDING)));
        sorter.sort();
    }

    /** Formatting only; TableRowSorter sees underlying Number values. */
    private static final class NumberRenderer extends DefaultTableCellRenderer {
        private final NumberFormat formatter;
        NumberRenderer(boolean percent) {
            formatter = percent ? NumberFormat.getPercentInstance(Locale.US) :
                    NumberFormat.getCurrencyInstance(Locale.US);
            formatter.setMaximumFractionDigits(percent ? 2 : 2);
            formatter.setMinimumFractionDigits(percent ? 2 : 2);
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
        @Override public void setValue(Object value) {
            setText(value instanceof Number ? formatter.format(value) : "");
        }
    }
}
