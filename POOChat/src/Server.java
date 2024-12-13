import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server implements Subject {
    private static final List<ObjectOutputStream> clients = Collections.synchronizedList(new ArrayList<>());
    private static final List<Command> backlog = Collections.synchronizedList(new ArrayList<>());


    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor iniciado na porta 12345.");
            Server server = new Server();
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, server)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private final Server server;
        private String clientName;

        public ClientHandler(Socket socket, Server server) {
            this.socket = socket;
            this.server = server;
            this.clientName = null;
        }

        @Override
        public void run() {
            ObjectInputStream in = null;
            ObjectOutputStream out = null;

            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());

                synchronized (clients) {
                    clients.add(out);
                }

                sendBacklog(out);

                Message message = (Message) in.readObject();
                if (message != null && message.getMessage() instanceof String) {
                    clientName = (String) message.getMessage();
                    server.notifyObservers(new Message("Servidor", clientName + " entrou no chat."));
                }

                while ((message = (Message) in.readObject()) != null) {
                    Command command = new SendMessageCommand(message);
                    synchronized (backlog) {
                        backlog.add(command);
                    }

                    server.notifyObservers(message);
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                synchronized (clients) {
                    if (out != null) {
                        clients.remove(out);
                        server.notifyObservers(new Message("Servidor", clientName + " saiu do chat."));
                    }
                }
            }
        }
    }

    @Override
    public void notifyObservers(Message message) {
        synchronized (clients) {
            for (ObjectOutputStream client : clients) {
                try {
                    client.writeObject(message);
                    client.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    

    private static void sendBacklog(ObjectOutputStream out) {
        synchronized (backlog) {
            for (Command command : backlog) {
                if (command instanceof SendMessageCommand) {
                    ((SendMessageCommand) command).execute(out);
                }
            }
        }
    }
    
}
