import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI {
    private JTextField inputField;
    private JTextArea displayArea;
    private JButton searchButton;
    private JButton deleteButton;
    private JButton addButton;
    private JPanel mainPanel;
    private JButton modifyButton;

    public MainGUI() {

        searchButton.addActionListener(e -> {
            System.out.println("searched: " + inputField.getText());
//            Main.sendMessage(new Message(Status.TASK_QUERY, inputField.getText()));
        });
        addButton.addActionListener(e -> {
            System.out.println("added: " + inputField.getText());
        });
        deleteButton.addActionListener(e -> {
            System.out.println("deleted: " + inputField.getText());
        });
        modifyButton.addActionListener(e -> {
            System.out.println("modified: " + inputField.getText());
        });


        // complete GUI settings and display
        JFrame frame = new JFrame("Dictionary");
        frame.setContentPane(mainPanel);
        frame.setMinimumSize(new Dimension(500, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}

