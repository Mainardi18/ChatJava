import java.io.ObjectOutputStream;

public interface Command {
    void execute(ObjectOutputStream out);
}
