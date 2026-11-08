# INFO 5100 — Assignment 9: Pricing Model, Part 2

Shai Rashada-Parasram  
**Project:** `RangePricingApplication` (original professor's NetBeans/Ant skeleton, extended)

## Open and run in NetBeans

1. Unzip this submission. In NetBeans, select **File → Open Project** and choose the `RangePricingApplication` folder (contains `nbproject` and `build.xml`).
2. Right-click the project, then choose **Clean and Build**.
3. Right-click the project, then choose **Run** (or press F6). The application starts the new Assignment 9 home screen.
4. Click **Open All Three Reports** on the home screen or **View Reports** on the left.
5. Choose reports using the dropdown. Click a table's price/sales column header to change sorting direction. Scroll vertically for more rows.
6. The starter application's demo logins remain available: `sales`, `marketing`, `admin` — password `XXXX`. Reports do **not** require login.

> If your NetBeans asks for a main class, select `TheBusiness.RangePricingApplication`.

## Rubric coverage

| Requirement | Implementation |
| --- | --- |
| 1 Business | `Assignment9DataGenerator.createBusiness()` |
| 50 suppliers, 30 stocked with 50 products | Randomized selected suppliers = 1,500 products total |
| 300 customers | Random customer generation and `CustomerDirectory` |
| 1–3 orders per customer | Random generator using `MasterOrderList.newOrder(...)` |
| 1–10 items per order | `Order.newOrderItem(...)` with valid actual unit prices and quantities |
| Report #1 (30 pts) | All 1,500 products, product/supplier names and numeric **target/list price**, descending; sortable JTable |
| Report #2 (30 pts) | 300 customers, total actual order sales, descending; sortable JTable |
| Report #3 (30 pts) | `SupplierReport` and `SupplierSummary`, all 50 suppliers, all 5 required columns, zero-safe aggregation |
| Report viewer | `ReportViewerJPanel`, dropdown and JTable, readable numeric/currency/percentage formatting |

### Report #3 calculations

- **Total Sales:** sum of all `actualPrice × quantity` order items associated with each supplier.
- **Loyalty Score:** number of **distinct purchasing customers** for the supplier ÷ **300** total customers.
- **Average Spending per Customer:** supplier sales ÷ number of distinct supplier customers (0 if none).
- **Top 5 Sales Score:** sum of sales from the supplier's five highest-spending unique customers ÷ total supplier sales (0 if no sales).
- Loyalty and Top 5 scores are displayed as **percentages** (underlying values remain numeric ratios).
- The 20 suppliers without stock are still included in Supplier Report with 0 sales and 0 scores.

## Added or modified code (includes student attribution/comments)

**New:**
- `src/TheBusiness/Assignment9DataGenerator.java` — deterministic random test data
- `src/TheBusiness/ReportManagement/ProductPriceRow.java`
- `src/TheBusiness/ReportManagement/CustomerSalesRow.java`
- `src/TheBusiness/ReportManagement/PricingReportService.java`
- `src/TheBusiness/Supplier/SupplierReport.java`
- `src/TheBusiness/Supplier/SupplierSummary.java`
- `src/UserInterface/Reports/ReportViewerJPanel.java`
- `src/UserInterface/Reports/WelcomeJPanel.java`
- `src/TheBusiness/Assignment9SelfTest.java` — automated checks

**Updated (existing starter classes):**
- `TheBusiness/RangePricingApplication.java` — launches GUI on Run Project
- `UserInterface/Main/ConfigureABusiness.java` — uses generator, replaces hard-coded sample
- `UserInterface/Main/PricingMainFrame.java` — report navigation and helpful home screen
- `TheBusiness/ProductManagement/Product.java` — product name getter
- `TheBusiness/CustomerManagement/CustomerDirectory.java` — customer list getter
- `TheBusiness/CustomerManagement/CustomerProfile.java` — customer orders getter
- `TheBusiness/OrderManagement/MasterOrderList.java` — master orders getter
- `TheBusiness/OrderManagement/Order.java` — customer and order-items getters

**Unmodified starter UI forms:** NetBeans `.form` files stay intact. New report panels are constructed in Java to avoid damaging generated form code.

## Verification performed before packaging

- **Ant `clean jar`: BUILD SUCCESSFUL** using Java 8 source compatibility.
- **Run the built-in self-test** by running the `TheBusiness.Assignment9SelfTest` main class in NetBeans. This checks model counts, bounds on orders/items/prices, customer & supplier aggregation, numeric sort order, all three JTable row/column counts, and zero-sales edge cases.
- Deterministic example: **611 orders**, **3,313 order items**, **$30,157,307** actual sales; totals reconcile across reports.
- Report 1 and Report 3 visually inspected under Swing with a virtual display. Your personal NetBeans screenshots will provide final confirmation on your computer.

## Final submission checklist

- [ ] Open in NetBeans, Clean and Build, Run.
- [ ] Screenshot home screen / data counts.
- [ ] Screenshot Report 1; click Price heading to demonstrate sortable columns.
- [ ] Screenshot Report 2 (sorted high to low).
- [ ] Screenshot Report 3 (all five metrics visible).
- [ ] Push all source files and project files to your **GitHub Lab Repository**.
- [ ] Screenshot GitHub repository with folders / pushed files visible.
- [ ] Upload project ZIP and GitHub screenshot to Canvas before Sunday at 11:59 p.m. ET.
