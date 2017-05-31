import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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

public class Client {
    static String FTPADDR = "localhost";
    static JTable table;
    static JTextArea log;
    static JButton button;
    static JTextField portField;
    boolean works = false;
    int port = 0;

    public static void main(String[] args) throws IOException {
        new Client();
    }

    void readXML(String filename) {
        try {
            File xmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element : " + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("row");
            System.out.println("-------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element : " + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    int id = Integer.parseInt(eElement.getAttribute("id"));
                    String firstname = eElement.getElementsByTagName("firstname").item(0).getTextContent();
                    String lastname = eElement.getElementsByTagName("lastname").item(0).getTextContent();
                    String nickname = eElement.getElementsByTagName("nickname").item(0).getTextContent();
                    String game = eElement.getElementsByTagName("game").item(0).getTextContent();
                    System.out.println("Person id : " + id);
                    System.out.println("Firstname : " + firstname);
                    System.out.println("Lastname : " + lastname);
                    System.out.println("Nickname : " + nickname);
                    System.out.println("Game : " + game);
                    table.setValueAt(id, id + 1, 0);
                    table.setValueAt(firstname, id + 1, 1);
                    table.setValueAt(lastname, id + 1, 2);
                    table.setValueAt(nickname, id + 1, 3);
                    table.setValueAt(game, id + 1, 4);
                }
            }
            button.addActionListener(e -> {
                if (!works) {
                    works = true;
                    button.setText("Stop");
                    port = Integer.parseInt(portField.getText());
                    new Thread(() -> {
                        try {
                            ServerSocket serverSocket = new ServerSocket(port);
                            while (true) {
                                Socket clientSocket = serverSocket.accept();
                                new Thread(() -> {
                                    try {
                                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                });
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    });
                } else {
                    works = false;
                    button.setText("Listen");
                }
            });
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

    }

    private Client() throws IOException {
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
            readXML("database.xml");
            table.setValueAt("id", 0, 0);
            table.setValueAt("firstname", 0, 1);
            table.setValueAt("lastname", 0, 2);
            table.setValueAt("nickname", 0, 3);
            table.setValueAt("game", 0, 4);
        });


    }
}