package UserInterface.Reports;

import TheBusiness.Business.Business;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Shai Rashada-Parasram - Friendly home screen, with one-click access to reports. */
public final class WelcomeJPanel extends JPanel {
    public WelcomeJPanel(Business business, Runnable openReports) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(26, 32, 26, 32));
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(8, 0, 8, 0);
        JLabel title = new JLabel("INFO 5100 - Assignment 9");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        body.add(title, g);
        g.gridy++;
        JLabel subtitle = new JLabel("The Pricing Model, Part 2 | Shai Rashada-Parasram");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        body.add(subtitle, g);
        g.gridy++;
        JLabel description = new JLabel("Generated data is ready: 50 suppliers, 1,500 products, 300 customers and linked orders.");
        body.add(description, g);
        g.gridy++;
        JButton reports = new JButton("Open All Three Reports");
        reports.setFont(new Font("SansSerif", Font.BOLD, 16));
        reports.setMargin(new Insets(12, 18, 12, 18));
        reports.addActionListener(e -> openReports.run());
        body.add(reports, g);
        g.gridy++;
        body.add(new JLabel("Or choose Reports on the left. Existing Sales / Marketing / Admin demo logins remain available."), g);
        g.gridy++;
        body.add(new JLabel("Demo credentials: sales / XXXX, marketing / XXXX, admin / XXXX."), g);
        add(body, BorderLayout.NORTH);
    }
}
