import java.io.IOException;
import java.io.ObjectOutputStream;

public class SendMessageCommand implements Command {
    private final Message message;

    public SendMessageCommand(Message message) {
        this.message = message;
    }

    @Override
    public void execute(ObjectOutputStream out) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
