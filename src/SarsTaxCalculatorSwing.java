import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SarsTaxCalculatorSwing extends JFrame {

    private JTextField monthlyIncomeField;
    private JTextField ageField;
    private JTextArea resultArea;
    private JComboBox<String> yearComboBox;

    public SarsTaxCalculatorSwing() {
        setTitle("SARS Tax Calculator");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        monthlyIncomeField = new JTextField();
        ageField = new JTextField();
        resultArea = new JTextArea(10, 20); // Increased rows and columns
        resultArea.setEditable(false);

        // Add a JComboBox for selecting the tax year
        yearComboBox = new JComboBox<>(new String[]{"2024", "2023", "2022"});

        JButton calculateButton = new JButton("Calculate");

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double monthlyIncome = Double.parseDouble(monthlyIncomeField.getText());
                    int age = Integer.parseInt(ageField.getText());
                    int selectedYear = Integer.parseInt((String) yearComboBox.getSelectedItem());

                    double annualIncome = calculateAnnualIncome(monthlyIncome);
                    double rebate = calculateRebate(age, selectedYear);
                    double taxableIncome = annualIncome - rebate;
                    double tax = calculateTax(age, taxableIncome, selectedYear);
                    double takeHomePayBeforeUIF = annualIncome - tax;
                    double uifContribution = calculateUIFContribution(annualIncome);
                    double netIncome = takeHomePayBeforeUIF - uifContribution;
                    double monthlyTakeHomePay = netIncome / 12;

                    resultArea.setText(
                            "Your annual income is: " + annualIncome + "\n" +
                                    "Your tax rebate is: " + rebate + "\n" +
                                    "Your taxable income after rebate is: " + taxableIncome + "\n" +
                                    "Your income tax is: " + tax + "\n" +
                                    "Your take-home pay before UIF is: " + takeHomePayBeforeUIF + "\n" +
                                    "Your UIF contribution is: " + uifContribution + "\n" +
                                    "Your net income after tax and UIF is: " + netIncome + "\n" +
                                    "Your monthly take-home pay is: " + monthlyTakeHomePay
                    );
                } catch (NumberFormatException ex) {
                    resultArea.setText("Invalid input. Please enter valid numeric values.");
                }
            }
        });

        panel.add(new JLabel("Monthly Gross Income:"));
        panel.add(monthlyIncomeField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Tax Year:"));
        panel.add(yearComboBox);
        panel.add(calculateButton);
        panel.add(new JLabel("Results:"));
        panel.add(new JScrollPane(resultArea)); // Use JScrollPane for multiline text area

        add(panel);
        setVisible(true);
    }

    public static double calculateAnnualIncome(double monthlyIncome) {
        return monthlyIncome * 12;
    }

    public static double calculateRebate(int age, int year) {
        double rebate = 0.0;

        switch (year) {
            case 2024:
                if (age >= 75) {
                    rebate = 3145; // Tertiary rebate
                } else if (age >= 65) {
                    rebate = 9444; // Secondary rebate
                } else {
                    rebate = 17235; // Primary rebate
                }
                break;
            case 2023:
                if (age >= 75) {
                    rebate = 2997; // Tertiary rebate for 2023
                } else if (age >= 65) {
                    rebate = 9000; // Secondary rebate for 2023
                } else {
                    rebate = 16425; // Primary rebate for 2023
                }
                break;
            case 2022:
                if (age >= 75) {
                    rebate = 2871; // Tertiary rebate for 2022
                } else if (age >= 65) {
                    rebate = 8613; // Secondary rebate for 2022
                } else {
                    rebate = 15714; // Primary rebate for 2022
                }
                break;
            // Add cases for other years as needed

            default:
                // Use the latest rebate values if the year is not specified
                // or handled explicitly
                if (age >= 75) {
                    rebate = 3145; // Tertiary rebate
                } else if (age >= 65) {
                    rebate = 9444; // Secondary rebate
                } else {
                    rebate = 17235; // Primary rebate
                }
        }

        return rebate;
    }


    public static double calculateTax(int age, double taxableIncome, int year) {
        double tax = 0.0;

        switch (year) {
            case 2024:
                if (taxableIncome > 0) {
                    double threshold = getTaxThreshold(age, year);

                    if (taxableIncome > threshold) {
                        tax = calculateTaxForAgeGroup(taxableIncome, year);
                    }
                }
                break;
            case 2023:
                if (taxableIncome > 0) {
                    double threshold = getTaxThreshold(age, year);

                    if (taxableIncome > threshold) {
                        tax = calculateTaxForAgeGroup(taxableIncome, year);
                    }
                }
                break;
            case 2022:
                if (taxableIncome > 0) {
                    double threshold = getTaxThreshold(age, year);

                    if (taxableIncome > threshold) {
                        tax = calculateTaxForAgeGroup(taxableIncome, year);
                    }
                }
                break;
            // Add cases for other years as needed

            default:
                // Use the latest tax rates if the year is not specified
                // or handled explicitly (in this case, 2024 rates)
                if (taxableIncome > 0) {
                    double threshold = getTaxThreshold(age, year);

                    if (taxableIncome > threshold) {
                        tax = calculateTaxForAgeGroup(taxableIncome, year);
                    }
                }
        }

        return tax;
    }


    private static double calculateTaxForAgeGroup(double taxableIncome, int year) {
        double tax = 0.0;

        switch (year) {
            case 2024:
                if (taxableIncome <= 237100) {
                    tax = 0.18 * taxableIncome;
                } else if (taxableIncome <= 370500) {
                    tax = 42678 + 0.26 * (taxableIncome - 237100);
                } else if (taxableIncome <= 512800) {
                    tax = 77362 + 0.31 * (taxableIncome - 370500);
                } else if (taxableIncome <= 673000) {
                    tax = 121475 + 0.36 * (taxableIncome - 512800);
                } else if (taxableIncome <= 857900) {
                    tax = 179147 + 0.39 * (taxableIncome - 673000);
                } else if (taxableIncome <= 1817000) {
                    tax = 251258 + 0.41 * (taxableIncome - 857900);
                } else {
                    tax = 644489 + 0.45 * (taxableIncome - 1817000);
                }
                break;
            case 2023:
                if (taxableIncome <= 226000) {
                    tax = 0.18 * taxableIncome;
                } else if (taxableIncome <= 353100) {
                    tax = 40680 + 0.26 * (taxableIncome - 226000);
                } else if (taxableIncome <= 488700) {
                    tax = 73726 + 0.31 * (taxableIncome - 353100);
                } else if (taxableIncome <= 641400) {
                    tax = 115762 + 0.36 * (taxableIncome - 488700);
                } else if (taxableIncome <= 817600) {
                    tax = 170734 + 0.39 * (taxableIncome - 641400);
                } else if (taxableIncome <= 1731600) {
                    tax = 239452 + 0.41 * (taxableIncome - 817600);
                } else {
                    tax = 614192 + 0.45 * (taxableIncome - 1731600);
                }
                break;
            case 2022:
                if (taxableIncome <= 216200) {
                    tax = 0.18 * taxableIncome;
                } else if (taxableIncome <= 337800) {
                    tax = 38916 + 0.26 * (taxableIncome - 216200);
                } else if (taxableIncome <= 467500) {
                    tax = 70532 + 0.31 * (taxableIncome - 337800);
                } else if (taxableIncome <= 613600) {
                    tax = 110739 + 0.36 * (taxableIncome - 467500);
                } else if (taxableIncome <= 782200) {
                    tax = 163335 + 0.39 * (taxableIncome - 613600);
                } else if (taxableIncome <= 1656600) {
                    tax = 229089 + 0.41 * (taxableIncome - 782200);
                } else {
                    tax = 587593 + 0.45 * (taxableIncome - 1656600);
                }
                break;
            // Add cases for other years as needed

            default:
                // Use the latest tax rates if the year is not specified
                // or handled explicitly (in this case, 2024 rates)
                if (taxableIncome <= 237100) {
                    tax = 0.18 * taxableIncome;
                } else if (taxableIncome <= 370500) {
                    tax = 42678 + 0.26 * (taxableIncome - 237100);
                } else if (taxableIncome <= 512800) {
                    tax = 77362 + 0.31 * (taxableIncome - 370500);
                } else if (taxableIncome <= 673000) {
                    tax = 121475 + 0.36 * (taxableIncome - 512800);
                } else if (taxableIncome <= 857900) {
                    tax = 179147 + 0.39 * (taxableIncome - 673000);
                } else if (taxableIncome <= 1817000) {
                    tax = 251258 + 0.41 * (taxableIncome - 857900);
                } else {
                    tax = 644489 + 0.45 * (taxableIncome - 1817000);
                }
        }

        return tax;
    }


    private static double getTaxThreshold(int age, int year) {
        double threshold = 0.0;

        switch (year) {
            case 2024:
                if (age >= 75) {
                    threshold = 165689;
                } else if (age >= 65) {
                    threshold = 148217;
                } else {
                    threshold = 95750;
                }
                break;
            case 2023:
                if (age >= 75) {
                    threshold = 157900;
                } else if (age >= 65) {
                    threshold = 141250;
                } else {
                    threshold = 91250;
                }
                break;
            case 2022:
                if (age >= 75) {
                    threshold = 151100; // Tax threshold for 75 and older for 2022
                } else if (age >= 65) {
                    threshold = 135150; // Tax threshold for 65 and older for 2022
                } else {
                    threshold = 87300; // Tax threshold for under 65 for 2022
                }
                break;
            // Add cases for other years as needed

            default:
                // Use the latest threshold values if the year is not specified
                // or handled explicitly
                if (age >= 75) {
                    threshold = 165689;
                } else if (age >= 65) {
                    threshold = 148217;
                } else {
                    threshold = 95750;
                }
        }

        return threshold;
    }


    public static double calculateUIFContribution(double grossIncome) {
        // UIF contribution is typically 1% of gross income
        return grossIncome * 0.01;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SarsTaxCalculatorSwing());
    }
}
