import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.UIManager;

public class Notepad extends JFrame implements ActionListener {
    JTextArea t;
    JFrame f;

    Notepad() {
        f = new JFrame("Notepad By Zaid & Sameeh");

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        t = new JTextArea();

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

        JMenuItem mc = new JMenuItem("Close");
        mc.addActionListener(this);
        setButtonPreferredSize(mc);

        // Add a background changing button
        JMenuItem changeBgButton = new JMenuItem("Change Background");
        changeBgButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color bgColor = JColorChooser.showDialog(null, "Choose Background Color", t.getBackground());
                if (bgColor != null) {
                    t.setBackground(bgColor);
                }
            }
        });
        setButtonPreferredSize(changeBgButton);

        // Add a font style changing button
        JMenuItem changeFontButton = new JMenuItem("Change Font Style");
        changeFontButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
            }
        });
        setButtonPreferredSize(changeFontButton);

        mb.add(m1);
        mb.add(m2);
        mb.add(changeBgButton);
        mb.add(changeFontButton);
        mb.add(mc);

        f.setJMenuBar(mb);
        f.add(t);
        f.setSize(600, 500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private void setButtonPreferredSize(AbstractButton button) {
        Dimension dim = new Dimension(120, button.getPreferredSize().height);
        button.setPreferredSize(dim);
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        if (s.equals("Cut")) {
            t.cut();
        } else if (s.equals("Copy")) {
            t.copy();
        } else if (s.equals("Paste")) {
            t.paste();
        } else if (s.equals("Save")) {
            JFileChooser j = new JFileChooser("f:");
            int r = j.showSaveDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                File fi = new File(j.getSelectedFile().getAbsolutePath());
                try (FileWriter wr = new FileWriter(fi, false); BufferedWriter w = new BufferedWriter(wr)) {
                    w.write(t.getText());
                } catch (Exception evt) {
                    JOptionPane.showMessageDialog(f, evt.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(f, "The user cancelled the operation");
            }
        } else if (s.equals("Print")) {
            try {
                t.print();
            } catch (Exception evt) {
                JOptionPane.showMessageDialog(f, evt.getMessage());
            }
        } else if (s.equals("Open")) {
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
            } else {
                JOptionPane.showMessageDialog(f, "The user cancelled the operation");
            }
        } else if (s.equals("New")) {
            t.setText("");
        } else if (s.equals("Close")) {
            f.setVisible(false);
        }
    }

    public static void main(String args[]) {
        new Notepad();
    }
}
