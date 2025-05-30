import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Usuario {
    private static final String LOG_PATH = "log.txt";
    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void registrarLog(String mensagem) {
        try (FileWriter fw = new FileWriter(LOG_PATH, true)) {
            String timestamp = LocalDateTime.now().format(FORMATADOR);
            String linha = "[" + timestamp + "] " + mensagem + "\n";
            fw.write(linha);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no log: " + e.getMessage());
        }
    }
}

