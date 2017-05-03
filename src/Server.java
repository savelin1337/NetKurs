import org.apache.poi.hssf.usermodel.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 9)	Сервер должен хранить на локальной машине файл в виде таблицы, содержащей минимум 5 полей.
 * Клиент должен иметь возможность получения количества записей в таблице,
 * а также возможность добавления пометки на удаление и редактирования записей из этой таблицы.
 * Кроме того, необходимо реализовать возможность поиска записей по заданным значениям полей
 * и получения всех или только указанных записей из этой таблицы. Записи, помеченные на удаление,
 * не должны участвовать в поиске и передаваться клиенту.
 * Необходимо предусмотреть возможность оповещения других клиентов при различных изменениях таблицы.
 * Как минимум, оповещения должны включать в себя номера записей,
 * для которых производились операции редактирования, добавления и пометки на удаление.
 */

public class Server {
    private String FTPADDR = "localhost";
    static JTable table;
    static JTextArea log;

    public static void main(String[] args) {
        new Server();
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
            frame.add(new ServerLayout());
            frame.pack();
            frame.setResizable(true);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow row = sheet.createRow(0);
        HSSFCell name = row.createCell((short) 0);
        HSSFRichTextString rts = new HSSFRichTextString("CHELEN");
        name.setCellValue(rts);
        sheet.autoSizeColumn((short) 30);
        try {
            FileOutputStream fos = new FileOutputStream("database.xls");
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}