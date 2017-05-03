import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.record.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Server {
    private String FTPADDR = "localhost";
    JTable table;
    JTextArea log;

    public static void main(String[] args) {
        new Server();
    }

    public class EventExample
            implements HSSFListener {
        private SSTRecord sstrec;

        /**
         * This method listens for incoming records and handles them as required.
         *
         * @param record The record that was found while reading.
         */
        public void processRecord(Record record) {
            switch (record.getSid()) {
                // the BOFRecord can represent either the beginning of a sheet or the workbook
                case BOFRecord.sid:
                    BOFRecord bof = (BOFRecord) record;
                    if (bof.getType() == bof.TYPE_WORKBOOK) {
                        System.out.println("Encountered workbook");
                        // assigned to the class level member
                    } else if (bof.getType() == bof.TYPE_WORKSHEET) {
                        System.out.println("Encountered sheet reference");
                    }
                    break;
                case BoundSheetRecord.sid:
                    BoundSheetRecord bsr = (BoundSheetRecord) record;
                    System.out.println("New sheet named: " + bsr.getSheetname());
                    break;
                case RowRecord.sid:
                    RowRecord rowrec = (RowRecord) record;
                    System.out.println("Row found, first column at "
                            + rowrec.getFirstCol() + " last column at " + rowrec.getLastCol());
                    break;
                case NumberRecord.sid:
                    NumberRecord numrec = (NumberRecord) record;
                    System.out.println("Cell found with value " + numrec.getValue()
                            + " at row " + numrec.getRow() + " and column " + numrec.getColumn());
                    break;
                // SSTRecords store a array of unique strings used in Excel.
                case SSTRecord.sid:
                    sstrec = (SSTRecord) record;
                    for (int k = 0; k < sstrec.getNumUniqueStrings(); k++) {
                        System.out.println("String table value " + k + " = " + sstrec.getString(k));
                    }
                    break;
                case LabelSSTRecord.sid:
                    LabelSSTRecord lrec = (LabelSSTRecord) record;
                    System.out.println("String cell found with value "
                            + sstrec.getString(lrec.getSSTIndex()));
                    break;
            }
        }
    }

    private Server() {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JFrame frame = new JFrame("Savelin server 0.1");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(new LayoutPane());
            frame.pack();
            frame.setResizable(true);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            try {
                FileInputStream fin = new FileInputStream("database.xls");
                POIFSFileSystem poifs = new POIFSFileSystem(fin);
                InputStream din = poifs.createDocumentInputStream("Workbook");
                HSSFRequest req = new HSSFRequest();
                req.addListenerForAllRecords(new EventExample());
                HSSFEventFactory factory = new HSSFEventFactory();
                factory.processEvents(req, din);
                fin.close();
                din.close();
                System.out.println("done.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private class LayoutPane extends JPanel {

        private LayoutPane() {
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
            table = new JTable(5, 5);
            paneMid.add(table, gbc);
            gbc.gridy++;
            add(paneMid, gbc);

            //BOT

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.SOUTH;
            log = new JTextArea(8, 80);
            JScrollPane scrollPane = new JScrollPane(log);
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
}