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
    private JButton updateButton;
    // Add word view components
    private JPanel addWordPanel;
    private JTextField addWordTitle;
    private JTextArea addWordField;
    private JButton addConfirmButton;
    private JButton addCancelButton;
    // Update word view components
    private JPanel updateWordPanel;
    private JTextArea updateWordField;
    private JButton updateConfirmButton;
    private JButton updateCancelButton;
    private JTextField updateWordTitle;

    public MainGUI(MessagePasser msgPasser) {

        this.msgPasser = msgPasser;

        // Setup action listeners
        setupMainViewFunctions();
        setupAddViewFunctions();
        setupUpdateViewFunction();

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
                // TODO: should this then show the word (i.e. search for it?)
                JOptionPane.showMessageDialog(frame, response.getStatus().getMessage() + ": " + input);
                break;
            case SUCCESS_WORD_REMOVED:
                JOptionPane.showMessageDialog(frame, response.getStatus().getMessage() + ": " + input);
                break;
            case SUCCESS_WORD_UPDATED:
                JOptionPane.showMessageDialog(frame, response.getStatus().getMessage() + ": " + input);
                break;
            case FAILURE_NOT_FOUND:
                JOptionPane.showMessageDialog(frame, response.getStatus().getMessage() + ": " + input);
                break;
            case FAILURE_WORD_EXISTS:
                JOptionPane.showMessageDialog(frame, response.getStatus().getMessage() + ": " + input);
                break;
            case FAILURE_INVALID_INPUT:
                JOptionPane.showMessageDialog(frame, response.getStatus().getMessage() + ": " + input);
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
        if (view == mainViewPanel) {
            inputField.setText("");
            displayArea.setText("");
        } else if (view == addWordPanel) {
            addWordField.setText("");
            addWordTitle.setText("");
        } else if (view == updateWordPanel) {
            updateWordField.setText("");
            updateWordTitle.setText("");
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

    private boolean definitionIsValid(String definition) {
        boolean valid = definition.matches("^[A-Za-z,;'&\"\\s]+[.?!]*$");
        if (!valid) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid word.");
        }
        return valid;
    }

    // action listeners for home view of dictionary
    private void setupMainViewFunctions() {
        searchButton.addActionListener(e -> search());
        addButton.addActionListener(e -> initialiseAdd());
        deleteButton.addActionListener(e -> delete());
        updateButton.addActionListener(e -> initialiseUpdate());
    }

    // action listeners for adding a word to the dictionary
    private void setupAddViewFunctions() {
        addCancelButton.addActionListener(e -> switchView(mainViewPanel));
        addConfirmButton.addActionListener(e -> confirmAdd());
    }

    // action listeners for updating word definitions
    private void setupUpdateViewFunction() {
        updateCancelButton.addActionListener(e -> switchView(mainViewPanel));
        updateConfirmButton.addActionListener(e -> confirmUpdate());
    }

    // Search for a word in the dictionary
    private void search() {
        String input = inputField.getText().trim();
        if (wordIsValid(input)) {
            handleResponse(input, msgPasser.sendMessage(new NetworkMessage(Status.TASK_QUERY, input)));
        }
    }

    // chooses a word to add to the dictionary, changes to the add word view
    private void initialiseAdd() {
        String input = inputField.getText().trim();
        if (wordIsValid(input)) {
            addWordTitle.setText(input);
            switchView(addWordPanel);
        }
    }

    // delete a word from the dictionary
    private void delete() {
        String input = inputField.getText().trim();
        if (wordIsValid(input)) {
            int ans = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete '" + input.toUpperCase() + "' and all of its associated definitions?", "Delete word?", JOptionPane.YES_NO_OPTION);
            if (ans == JOptionPane.YES_OPTION) {
                handleResponse(input, msgPasser.sendMessage(new NetworkMessage(Status.TASK_REMOVE, input)));
            }
        }
    }

    // add a definition to an existing word in the dictionary
    private void initialiseUpdate() {
        String input = inputField.getText().trim();
        if (wordIsValid(input)) {
            updateWordTitle.setText(input);
            switchView(updateWordPanel);
        }
    }

    private void confirmAdd() {
        String word = addWordTitle.getText().trim();
        String definition = addWordField.getText().trim();
        if (definitionIsValid(definition)) {
            String[] data = {word, definition};
            handleResponse(word, msgPasser.sendMessage(new NetworkMessage(Status.TASK_ADD, data)));
            switchView(mainViewPanel);
        }
    }

    private void confirmUpdate() {
        String word = updateWordTitle.getText().trim();
        String definition = updateWordField.getText().trim();
        if (definitionIsValid(definition)) {
            String[] data = {word, definition};
            handleResponse(word, msgPasser.sendMessage(new NetworkMessage(Status.TASK_UPDATE, data)));
            switchView(mainViewPanel);
        }
    }

    private void showErrorPopup(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }
}

