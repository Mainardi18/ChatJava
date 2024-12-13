import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TextMessageStrategy implements MessageStrategy {
    @Override
    public void sendMessage(ObjectOutputStream out, String sender, JFrame frame, JTextField messageField) throws IOException {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            try {
                out.writeObject(new Message(sender, message));
                out.flush();
                messageField.setText("");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
