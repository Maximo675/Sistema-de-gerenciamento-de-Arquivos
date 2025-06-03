import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Usuario {
    private static String logPath = "log.txt";
    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Configurar o caminho do arquivo de log
    public static void configurarCaminhoLog(String caminho) {
        logPath = caminho;
    }

    // Registrar log com informações detalhadas
    public static void registrarLog(String acao, String detalhes) {
        try (FileWriter fw = new FileWriter(logPath, true)) {
            String timestamp = LocalDateTime.now().format(FORMATADOR);
            String usuario = System.getProperty("user.name");
            String linha = String.format("[%s] [Usuário: %s] [Ação: %s] %s%n", timestamp, usuario, acao, detalhes);
            fw.write(linha);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no log: " + e.getMessage());
        }
    }
}

