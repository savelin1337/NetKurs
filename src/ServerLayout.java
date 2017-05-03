import javax.swing.*;
import java.awt.*;

public class ServerLayout extends JPanel {

    public ServerLayout() {
        JButton button = new JButton("Connect");
        button.setPreferredSize(new Dimension(90, 22));
        button.setEnabled(false);
        JLabel addressLabel = new JLabel("FTP Server:");
        JTextField addressField = new JTextField("localhost", 20);
        setLayout(new GridBagLayout());

        JPanel paneTop = new JPanel(new GridBagLayout());
        JPanel paneMid = new JPanel(new GridBagLayout());
        JPanel paneBot = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // TOP
        gbc.anchor = GridBagConstraints.NORTH;
        paneTop.add(addressLabel, gbc);
        gbc.gridx++;
        paneTop.add(addressField, gbc);
        gbc.gridx++;
        paneTop.add(button, gbc);
        gbc.gridx = 0;
        add(paneTop, gbc);

        // MID
        gbc.anchor = GridBagConstraints.CENTER;
        Server.table = new JTable(5, 5);
        paneMid.add(Server.table, gbc);
        gbc.gridy++;
        add(paneMid, gbc);

        //BOT
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        Server.log = new JTextArea(8, 80);
        JScrollPane scrollPane = new JScrollPane(Server.log);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        paneBot.add(scrollPane, gbc);
        JTextField consoleField = new JTextField();
        gbc.gridy++;
        paneBot.add(consoleField, gbc);
        gbc.gridy = 2;
        add(paneBot, gbc);
        button.addActionListener(e -> {

        });
    }
}