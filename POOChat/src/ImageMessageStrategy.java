import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ImageMessageStrategy implements MessageStrategy {
    @Override
    public void sendMessage(ObjectOutputStream out, String sender, JFrame frame, JTextField messageField) throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imagens", "png", "jpg", "jpeg", "gif"));

        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                byte[] imageBytes = new byte[(int) file.length()];
                try (FileInputStream fis = new FileInputStream(file)) {
                    fis.read(imageBytes);
                }

                out.writeObject(new Message(sender, imageBytes));
                out.flush();

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Erro ao enviar a imagem.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
