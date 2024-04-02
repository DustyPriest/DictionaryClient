import javax.swing.*;
import java.awt.*;

public class MainGUI {
    private final MessagePasser msgPasser;
    private final JFrame frame;
    private JPanel currentView;
    private JPanel rootPanel;
    // Main view components
    private JPanel mainViewPanel;
    private JTextField inputField;
    private JTextArea displayArea;
    private JButton searchButton;
    private JButton deleteButton;
    private JButton addButton;
    private JButton modifyButton;
    // Add word view components
    private JPanel addWordPanel;
    private JTextField addWordTitle;
    private JTextArea addWordField;
    private JButton addConfirmButton;
    private JButton addCancelButton;

    public MainGUI(MessagePasser msgPasser) {

        this.msgPasser = msgPasser;

        // Setup action listeners
        setupMainViewFunctions();
        setupAddViewFunctions();

        // complete GUI settings and display
        currentView = mainViewPanel;
        frame = new JFrame("Dictionary");
        frame.setContentPane(currentView);
        frame.setMinimumSize(new Dimension(500, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void handleResponse(String input, NetworkMessage response) {
        switch (response.getStatus()) {
            case SUCCESS_WORD_FOUND:
                System.out.println(response.getData()[0]);
                displayArea.setText(input + "\n");
                for (String s : response.getData()) {
                    displayArea.append("  -  " + s + "\n");
                }
                break;
            case SUCCESS_WORD_ADDED:
                JOptionPane.showMessageDialog(frame, response.getStatus().getMessage() + ": " + input);
                break;
            case SUCCESS_WORD_REMOVED:
                displayArea.setText("Word removed from dictionary");
                break;
            case SUCCESS_WORD_UPDATED:
                displayArea.setText("Word updated in dictionary");
                break;
            case FAILURE_NOT_FOUND:
                displayArea.setText("Word not found in dictionary");
                break;
            case FAILURE_WORD_EXISTS:
                displayArea.setText("Word already exists in dictionary");
                break;
            case FAILURE_INVALID_INPUT:
                displayArea.setText("Invalid input");
                break;
        }
    }

    private void switchView(JPanel view) {
        resetView(currentView);
        currentView = view;
        frame.setContentPane(currentView);
        frame.revalidate();
        frame.repaint();
    }

    private void resetView(JPanel view) {
        // TODO finish for other views
        if (view == mainViewPanel) {
            inputField.setText("");
            displayArea.setText("");
        } else if (view == addWordPanel) {
            addWordField.setText("");
        }
    }

    // checks if searched/inputted word is of a valid form
    private boolean wordIsValid(String input) {
        boolean valid = input.matches("[A-Za-z'-]+");
        if (!valid) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid word.");
        }
        return valid;
    }

    // action listeners for home view of dictionary
    private void setupMainViewFunctions() {
        searchButton.addActionListener(e -> {
            String input = inputField.getText();
            if (wordIsValid(input)) {
                handleResponse(input, msgPasser.sendMessage(new NetworkMessage(Status.TASK_QUERY, input)));
            }
        });
        addButton.addActionListener(e -> {
            String input = inputField.getText();
            if (wordIsValid(input)) {
                addWordTitle.setText(input);
                switchView(addWordPanel);
            }
        });
        deleteButton.addActionListener(e -> {
            String input = inputField.getText();

            if (wordIsValid(input)) {
                int ans = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete '" + input.toUpperCase() + "' and all of its associated definitions?");
                if (ans == JOptionPane.YES_OPTION) {
                    handleResponse(input, msgPasser.sendMessage(new NetworkMessage(Status.TASK_REMOVE, input)));
                }
            }
        });
        modifyButton.addActionListener(e -> {
            String input = inputField.getText();
            if (wordIsValid(input)) {
                handleResponse(input, msgPasser.sendMessage(new NetworkMessage(Status.TASK_UPDATE, input)));
            }
        });
    }

    // action listeners for adding a word to the dictionary
    private void setupAddViewFunctions() {
        addCancelButton.addActionListener(e -> switchView(mainViewPanel));
        addConfirmButton.addActionListener(e -> {
            String word = addWordTitle.getText();
            String definition = addWordField.getText();
            String[] data = {word, definition};
            handleResponse(word, msgPasser.sendMessage(new NetworkMessage(Status.TASK_ADD, data)));
            switchView(mainViewPanel);
        });
    }


}

