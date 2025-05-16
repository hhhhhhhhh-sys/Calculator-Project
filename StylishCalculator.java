import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class StylishCalculator extends JFrame {
    // Calculator state
    private String currentInput = "0";
    private String previousInput = "0";
    private String operation = null;
    private boolean resetInput = false;
    private List<String> history = new ArrayList<>();
    private boolean darkMode = false;

    // UI Components
    private JPanel calculatorPanel;
    private JLabel resultLabel;
    private JLabel operationLabel;
    private JPanel historyPanel;
    private JToggleButton modeToggle;
    
    // Colors for light mode
    private Color bgColor = new Color(245, 245, 247);
    private Color calcBg = new Color(255, 255, 255);
    private Color buttonBg = new Color(233, 233, 235);
    private Color buttonHover = new Color(209, 209, 214);
    private Color opButtonBg = new Color(255, 159, 10);
    private Color equalButtonBg = new Color(52, 199, 89);
    private Color specialButtonBg = new Color(94, 92, 230);
    private Color textColor = new Color(28, 28, 30);
    private Color displayText = new Color(0, 0, 0);
    private Color buttonText = new Color(28, 28, 30);
    private Color opButtonText = new Color(255, 255, 255);
    
    // Colors for dark mode
    private Color darkBgColor = new Color(28, 28, 30);
    private Color darkCalcBg = new Color(44, 44, 46);
    private Color darkButtonBg = new Color(58, 58, 60);
    private Color darkButtonHover = new Color(72, 72, 74);
    private Color darkTextColor = new Color(245, 245, 247);
    private Color darkDisplayText = new Color(255, 255, 255);
    private Color darkButtonText = new Color(255, 255, 255);

    public StylishCalculator() {
        // Basic frame setup
        setTitle("Stylish Calculator");
        setSize(350, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel
        calculatorPanel = new JPanel();
        calculatorPanel.setLayout(new BorderLayout(10, 10));
        calculatorPanel.setBackground(calcBg);
        calculatorPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header panel with title and toggle
        JPanel headerPanel = createHeaderPanel();
        calculatorPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Display panel
        JPanel displayPanel = createDisplayPanel();
        calculatorPanel.add(displayPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = createButtonsPanel();
        calculatorPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        // History panel
        historyPanel = createHistoryPanel();
        calculatorPanel.add(historyPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(calculatorPanel);
        
        // Initialize UI
        updateDisplay();
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(calcBg);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("Advanced Calculator");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(textColor);
        
        // Create toggle button
        modeToggle = new JToggleButton("Light/Dark");
        modeToggle.setFocusPainted(false);
        modeToggle.setBackground(specialButtonBg);
        modeToggle.setForeground(opButtonText);
        modeToggle.setBorderPainted(false);
        modeToggle.addActionListener(e -> toggleMode());
        
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(modeToggle, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(calcBg);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Operation display (top line)
        operationLabel = new JLabel("");
        operationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        operationLabel.setForeground(textColor);
        operationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        // Result display (main number)
        resultLabel = new JLabel("0");
        resultLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        resultLabel.setForeground(displayText);
        resultLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        panel.add(operationLabel, BorderLayout.NORTH);
        panel.add(resultLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 4, 10, 10));
        panel.setBackground(calcBg);
        
        // First row
        addSpecialButton(panel, "AC", e -> clearAll());
        addSpecialButton(panel, "⌫", e -> deleteChar());
        addSpecialButton(panel, "%", e -> addOperator("%"));
        addOperationButton(panel, "/", e -> addOperator("/"));
        
        // Second row
        addNumberButton(panel, "7");
        addNumberButton(panel, "8");
        addNumberButton(panel, "9");
        addOperationButton(panel, "×", e -> addOperator("*"));
        
        // Third row
        addNumberButton(panel, "4");
        addNumberButton(panel, "5");
        addNumberButton(panel, "6");
        addOperationButton(panel, "-", e -> addOperator("-"));
        
        // Fourth row
        addNumberButton(panel, "1");
        addNumberButton(panel, "2");
        addNumberButton(panel, "3");
        addOperationButton(panel, "+", e -> addOperator("+"));
        
        // Fifth row
        addNumberButton(panel, "0");
        addSimpleButton(panel, ".", e -> addDecimal());
        addEqualButton(panel, "=", e -> calculate());
        
        return panel;
    }
    
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(buttonBg);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(350, 100));
        
        return panel;
    }
    
    private void addNumberButton(JPanel panel, String text) {
        JButton button = new JButton(text);
        styleButton(button, buttonBg, buttonText);
        button.addActionListener(e -> addNumber(text));
        panel.add(button);
    }
    
    private void addOperationButton(JPanel panel, String text, ActionListener action) {
        JButton button = new JButton(text);
        styleButton(button, opButtonBg, opButtonText);
        button.addActionListener(action);
        panel.add(button);
    }
    
    private void addSpecialButton(JPanel panel, String text, ActionListener action) {
        JButton button = new JButton(text);
        styleButton(button, specialButtonBg, opButtonText);
        button.addActionListener(action);
        panel.add(button);
    }
    
    private void addEqualButton(JPanel panel, String text, ActionListener action) {
        JButton button = new JButton(text);
        styleButton(button, equalButtonBg, opButtonText);
        button.addActionListener(action);
        panel.add(button);
    }
    
    private void addSimpleButton(JPanel panel, String text, ActionListener action) {
        JButton button = new JButton(text);
        styleButton(button, buttonBg, buttonText);
        button.addActionListener(action);
        panel.add(button);
    }
    
    private void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setBorderPainted(false);
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bg.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bg);
            }
        });
    }
    
    // Calculator functions
    private void addNumber(String number) {
        if (currentInput.equals("0") || resetInput) {
            currentInput = number;
            resetInput = false;
        } else {
            // Limit to 12 digits
            if (currentInput.replaceAll("[^0-9]", "").length() < 12) {
                currentInput += number;
            }
        }
        updateDisplay();
    }
    
    private void addDecimal() {
        if (resetInput) {
            currentInput = "0.";
            resetInput = false;
        } else if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        updateDisplay();
    }
    
    private void addOperator(String op) {
        calculate();
        previousInput = currentInput;
        operation = op;
        resetInput = true;
        updateDisplay();
    }
    
    private void calculate() {
        if (operation == null) return;
        
        double result = 0;
        double prev = Double.parseDouble(previousInput);
        double current = Double.parseDouble(currentInput);
        
        switch (operation) {
            case "+":
                result = prev + current;
                break;
            case "-":
                result = prev - current;
                break;
            case "*":
                result = prev * current;
                break;
            case "/":
                result = prev / current;
                break;
            case "%":
                result = prev % current;
                break;
        }
        
        // Add to history
        String calculation = previousInput + " " + operation + " " + currentInput + " = " + result;
        history.add(0, calculation);
        if (history.size() > 5) {
            history.remove(history.size() - 1);
        }
        updateHistory();
        
        currentInput = String.valueOf(result);
        operation = null;
        previousInput = "0";
        resetInput = true;
        updateDisplay();
    }
    
    private void clearAll() {
        currentInput = "0";
        previousInput = "0";
        operation = null;
        resetInput = false;
        updateDisplay();
    }
    
    private void deleteChar() {
        if (currentInput.length() == 1 || 
            (currentInput.length() == 2 && currentInput.startsWith("-"))) {
            currentInput = "0";
        } else {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
        }
        updateDisplay();
    }
    
    private void updateDisplay() {
        resultLabel.setText(currentInput);
        if (operation != null) {
            operationLabel.setText(previousInput + " " + operation);
        } else {
            operationLabel.setText(previousInput.equals("0") ? "" : previousInput);
        }
    }
    
    private void updateHistory() {
        historyPanel.removeAll();
        
        for (String item : history) {
            JLabel historyItem = new JLabel(item);
            historyItem.setForeground(textColor);
            historyItem.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            historyPanel.add(historyItem);
        }
        
        historyPanel.revalidate();
        historyPanel.repaint();
    }
    
    private void toggleMode() {
        darkMode = !darkMode;
        
        if (darkMode) {
            // Switch to dark mode
            calculatorPanel.setBackground(darkCalcBg);
            historyPanel.setBackground(darkButtonBg);
            resultLabel.setForeground(darkDisplayText);
            operationLabel.setForeground(darkTextColor);
            
            // Update all child components recursively
            updateComponentTreeUI(calculatorPanel);
        } else {
            // Switch to light mode
            calculatorPanel.setBackground(calcBg);
            historyPanel.setBackground(buttonBg);
            resultLabel.setForeground(displayText);
            operationLabel.setForeground(textColor);
            
            // Update all child components recursively
            updateComponentTreeUI(calculatorPanel);
        }
    }
    
    private void updateComponentTreeUI(Container container) {
        Component[] components = container.getComponents();
        
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                panel.setBackground(darkMode ? darkCalcBg : calcBg);
                updateComponentTreeUI(panel);
            } else if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setForeground(darkMode ? darkTextColor : textColor);
            } else if (component instanceof JButton) {
                JButton button = (JButton) component;
                // We would need to know which type of button this is
                // This is a simplified approach
                Color originalBg = button.getBackground();
                
                // Try to determine button type and set appropriate color
                if (originalBg.equals(opButtonBg) || originalBg.equals(opButtonBg.darker())) {
                    // Operation buttons stay the same color in both modes
                } else if (originalBg.equals(equalButtonBg) || originalBg.equals(equalButtonBg.darker())) {
                    // Equal button stays the same color in both modes
                } else if (originalBg.equals(specialButtonBg) || originalBg.equals(specialButtonBg.darker())) {
                    // Special buttons stay the same color in both modes
                } else {
                    // Regular number buttons
                    button.setBackground(darkMode ? darkButtonBg : buttonBg);
                    button.setForeground(darkMode ? darkButtonText : buttonText);
                }
            }
        }
    }
    
    public static void main(String[] args) {
        // Set system look and feel for better integration
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show calculator
        SwingUtilities.invokeLater(() -> {
            try {
                StylishCalculator calculator = new StylishCalculator();
                calculator.setVisible(true);
                
                // Force repaint and bring to front
                calculator.repaint();
                calculator.setAlwaysOnTop(true);
                calculator.setAlwaysOnTop(false);
                
                // Debug message to console
                System.out.println("Calculator window should be visible now");
            } catch (Exception e) {
                System.err.println("Error creating calculator: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}