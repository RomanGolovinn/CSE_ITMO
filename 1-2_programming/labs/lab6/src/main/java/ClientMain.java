import io.github.cdimascio.dotenv.Dotenv;
import io.net.Client;
import io.ui.AskManager;
import io.ui.Console;

import java.net.InetAddress;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String host = dotenv.get("SERVER_HOST", "127.0.0.1");
        int port = Integer.parseInt(dotenv.get("SERVER_PORT", "8080"));

        try {
            System.out.println("Запуск клиента. Подключение к " + host + ":" + port);

            InetAddress address = InetAddress.getByName(host);
            Client client = new Client(address, port);

            Scanner scanner = new Scanner(System.in);
            AskManager askManager = new AskManager(scanner);
            Console console = new Console(client, askManager);

            console.start();

        } catch (Exception e) {
            System.out.println("Критическая ошибка при запуске клиента: " + e.getMessage());
        }
    }
}