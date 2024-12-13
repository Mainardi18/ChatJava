import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;

public class Client {
    private static final int DOOR = 12345;
    private static final String SERVER_ADDRESS = "localhost";

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String name;
    private MessageStrategy strategy;

    private JFrame frame;
    private JTextPane chatPane;
    private JTextField messageField;
    private JButton sendButton;
    private JButton imageButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Client client = new Client();
            client.setName();
            client.createGUI();
        });
    }

    public Client() {
        try {
            socket = new Socket(SERVER_ADDRESS, DOOR);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível conectar ao servidor.");
            System.exit(1);
        }
    }

    private void setName() {
        this.name = JOptionPane.showInputDialog(null, "Digite seu nome:", "Nome do Cliente", JOptionPane.PLAIN_MESSAGE);
        if (this.name == null || this.name.trim().isEmpty()) {
            this.name = "Anônimo";
        }
    }

    public void createGUI() {
        frame = new JFrame("Chat Cliente - " + name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        chatPane = new JTextPane();
        chatPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(chatPane);

        messageField = new JTextField();
        sendButton = new JButton("Enviar");
        imageButton = new JButton("Enviar Imagem");

        Context context = new Context();
        sendButton.addActionListener(context);
        imageButton.addActionListener(context);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.add(imageButton, BorderLayout.WEST);


        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        try {
            out.writeObject(new Message("Servidor", name));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(this::listen).start();
    }

    private class Context implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(actionEvent.getSource() == sendButton){
                try {
                    strategy = new TextMessageStrategy();
                    strategy.sendMessage(out, name, null, messageField);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else{
                try {
                    strategy = new ImageMessageStrategy();
                    strategy.sendMessage(out, name, frame, null);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void listen() {
        try {
            while (true) {
                Message message = (Message) in.readObject();
                SwingUtilities.invokeLater(() -> {
                    String sender = message.getSender();
                    Object content = message.getMessage();

                    if (content instanceof String) {
                        if (sender.equals(name)) {
                            appendToChat("Você: " + content);
                        } else {
                            appendToChat(sender + ": " + content);
                        }
                    } else if (content instanceof byte[]) {
                        try {
                            int width = 200;
                            int height = 200;
                            ImageIcon resizedImage = resizeImage((byte[]) content, width, height);

                            if (sender.equals(name)) {
                                appendToChat("Você enviou uma imagem:");
                            } else {
                                appendToChat(sender + " enviou uma imagem:");
                            }
                            appendImage(resizedImage);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            appendToChat("Erro ao carregar a imagem de " + sender + ".");
                        }
                    }
                });
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void appendToChat(String message) {
        StyledDocument doc = chatPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), message + "\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void appendImage(ImageIcon imageIcon) {
        StyledDocument doc = chatPane.getStyledDocument();
        chatPane.setCaretPosition(doc.getLength());
        chatPane.insertIcon(imageIcon);
        try {
            doc.insertString(doc.getLength(), "\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private ImageIcon resizeImage(byte[] imageData, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(imageData);
        Image img = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
