import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class Notepad extends JFrame implements ActionListener {
    JTextArea t;
    JFrame f;

    private boolean textChanged = false;

    Notepad() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        f = new JFrame("Notepad By Zaid & Sameeh");
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        t = new JTextArea();
        t.setBackground(Color.LIGHT_GRAY);
        t.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textChanged = true;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textChanged = true;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // ignored for now
            }
        });

        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("File");
        JMenuItem mi1 = new JMenuItem("New");
        JMenuItem mi2 = new JMenuItem("Open");
        JMenuItem mi3 = new JMenuItem("Save");
        JMenuItem mi9 = new JMenuItem("Print");

        mi1.addActionListener(this);
        mi2.addActionListener(this);
        mi3.addActionListener(this);
        mi9.addActionListener(this);

        setButtonPreferredSize(mi1);
        setButtonPreferredSize(mi2);
        setButtonPreferredSize(mi3);
        setButtonPreferredSize(mi9);

        m1.add(mi1);
        m1.add(mi2);
        m1.add(mi3);
        m1.add(mi9);

        JMenu m2 = new JMenu("Edit");
        JMenuItem mi4 = new JMenuItem("Cut");
        JMenuItem mi5 = new JMenuItem("Copy");
        JMenuItem mi6 = new JMenuItem("Paste");

        mi4.addActionListener(this);
        mi5.addActionListener(this);
        mi6.addActionListener(this);

        setButtonPreferredSize(mi4);
        setButtonPreferredSize(mi5);
        setButtonPreferredSize(mi6);

        m2.add(mi4);
        m2.add(mi5);
        m2.add(mi6);

        JMenuItem mi7 = new JMenuItem("Select All");
        mi7.addActionListener(this);
        setButtonPreferredSize(mi7);
        m2.add(mi7);

        mb.add(m1);
        mb.add(m2);

        JMenuItem changeBgButton = new JMenuItem("Change Background");
        changeBgButton.addActionListener(e -> {
            Color bgColor = JColorChooser.showDialog(null, "Choose Background Color", t.getBackground());
            if (bgColor != null) {
                t.setBackground(bgColor);
            }
        });
        setButtonPreferredSize(changeBgButton);

        JMenuItem changeFontButton = new JMenuItem("Change Font Style");
        changeFontButton.addActionListener(e -> {
            String[] fontStyleValues = { "PLAIN", "BOLD", "ITALIC" };
            String selectedFontStyle = (String) JOptionPane.showInputDialog(null, "Choose Font Style",
                    "Font Style", JOptionPane.PLAIN_MESSAGE, null, fontStyleValues, fontStyleValues[0]);
            if (selectedFontStyle != null) {
                int style = Font.PLAIN;
                if (selectedFontStyle.equals("BOLD")) {
                    style = Font.BOLD;
                } else if (selectedFontStyle.equals("ITALIC")) {
                    style = Font.ITALIC;
                }
                t.setFont(new Font(t.getFont().getName(), style, t.getFont().getSize()));
            }
        });
        setButtonPreferredSize(changeFontButton);

        JMenuItem changeFontFamilyButton = new JMenuItem("Change Font Family");
        changeFontFamilyButton.addActionListener(e -> {
            String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            String selectedFont = (String) JOptionPane.showInputDialog(null, "Choose Font Family",
                    "Font Family", JOptionPane.PLAIN_MESSAGE, null, fontNames, fontNames[0]);
            if (selectedFont != null) {
                t.setFont(new Font(selectedFont, t.getFont().getStyle(), t.getFont().getSize()));
            }
        });
        setButtonPreferredSize(changeFontFamilyButton);

        mb.add(m1);
        mb.add(m2);
        mb.add(changeBgButton);
        mb.add(changeFontButton);
        mb.add(changeFontFamilyButton);

        JMenuItem aboutUsMenuItem = new JMenuItem("About Us");
        aboutUsMenuItem.addActionListener(e -> {
            showAboutUsDialog();
        });
        setButtonPreferredSize(aboutUsMenuItem);
        // Add the "About Us" button to the menu bar
        mb.add(aboutUsMenuItem);

        // Set the menu bar
        f.setJMenuBar(mb);

        f.add(new JScrollPane(t));
        f.setSize(710, 450);
        f.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private void setButtonPreferredSize(AbstractButton button) {
        Dimension dim = new Dimension(120, button.getPreferredSize().height);
        button.setPreferredSize(dim);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorderPainted(true);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.DARK_GRAY); // Dark gray background color
        button.setFocusPainted(true);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setOpaque(true);
        button.repaint();
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Cut" -> t.cut();
            case "Copy" -> t.copy();
            case "Paste" -> t.paste();
            case "Select All" -> onSelectAll();
            case "Save" -> onSave();
            case "Print" -> onPrint();
            case "Open" -> onOpen();
            case "New" -> onNew();
            case "Close" -> onClose();
        }
    }

    private void onClose() {
        if (textChanged) {
            switch (askAboutUnsavedChanges()) {
                case JOptionPane.CLOSED_OPTION, JOptionPane.CANCEL_OPTION -> {
                }
                case JOptionPane.YES_OPTION -> {
                    // Leave frame open if text was not saved for some reason
                    if (!onSave())
                        return;

                    f.dispose();
                }
                case JOptionPane.NO_OPTION -> f.dispose();
            }
        } else
            f.dispose();
    }

    /**
     * @return {@code true} if file was saved successfully, {@code false} if not
     */
    private boolean onSave() {
        JFileChooser j = new JFileChooser("f:");
        int r = j.showSaveDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            File fi = new File(j.getSelectedFile().getAbsolutePath());
            try (FileWriter wr = new FileWriter(fi, false); BufferedWriter w = new BufferedWriter(wr)) {
                w.write(t.getText());
                textChanged = false;
                return true;
            } catch (Exception evt) {
                JOptionPane.showMessageDialog(f, evt.getMessage());
            }
        }

        return false;
    }

    private void onPrint() {
        try {
            t.print();
        } catch (Exception evt) {
            JOptionPane.showMessageDialog(f, evt.getMessage());
        }
    }

    private void onSelectAll() {
        t.requestFocusInWindow();
        int textLength = t.getDocument().getLength();
        if (textLength > 0) {
            t.setCaretPosition(0);
            t.moveCaretPosition(textLength);
        }
    }

    private void onOpen() {
        JFileChooser j = new JFileChooser("f:");
        int r = j.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            File fi = new File(j.getSelectedFile().getAbsolutePath());
            try (FileReader fr = new FileReader(fi); BufferedReader br = new BufferedReader(fr)) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                t.setText(sb.toString());
            } catch (Exception evt) {
                JOptionPane.showMessageDialog(f, evt.getMessage());
            }
        }
    }

    private void onNew() {
        if (textChanged) {
            switch (askAboutUnsavedChanges()) {
                case JOptionPane.CLOSED_OPTION, JOptionPane.CANCEL_OPTION -> {
                    return;
                }
                case JOptionPane.YES_OPTION -> {
                    if (!onSave())
                        return;
                }
            }
        }

        t.setText("");
        textChanged = false;
    }

    /**
     * Fires dialog window when current text is not saved
     */
    private int askAboutUnsavedChanges() {
        return JOptionPane.showConfirmDialog(
                f,
                "New file has been modified, save changes?",
                "Save changes?",
                JOptionPane.YES_NO_CANCEL_OPTION);
    }

    private void showAboutUsDialog() {
        String message = "<html>" +
                "Made with ❤️ by Zaid and Team<br><br>" +
                "Copyright @2024-2025<br>" +
                "This project is under GitHub's MIT License.<br><br>" +
                "The Product is available for fair use<br><br>" +
                "Illegal Use of Product can lead to Copyright Infringements<br><br>" +
                "This Product is Open Source on Github feel free to contribute<br><br><br>" +
                "<a href=\"https://github.com/zaid-commits/Notepad-JavaApplication\">Contribute on GitHub</a></html>";

        JLabel label = new JLabel("<html>" + message + "</html>");
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/zaid-commits/Notepad-JavaApplication"));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JOptionPane.showMessageDialog(f, label, "About Us", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new Notepad();
    }
}
