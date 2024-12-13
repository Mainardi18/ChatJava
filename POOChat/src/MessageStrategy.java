import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;

public interface MessageStrategy {
    void sendMessage(ObjectOutputStream out, String sender, JFrame frame, JTextField messageField) throws IOException;
}
