import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class AdminFile {

    public static void listarArquivos(Scanner sc) {
        System.out.println("Escolha uma unidade de armazenamento para listar os arquivos:");
        File[] unidades = File.listRoots();
        for (int i = 0; i < unidades.length; i++) {
            System.out.println((i + 1) + ". " + unidades[i].getAbsolutePath());
        }

        System.out.print("Digite o número da unidade desejada: ");
        int escolhaUnidade;
        try {
            escolhaUnidade = Integer.parseInt(sc.nextLine());
            if (escolhaUnidade < 1 || escolhaUnidade > unidades.length) {
                System.out.println("Opção inválida.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
            return;
        }

        File unidadeSelecionada = unidades[escolhaUnidade - 1];
        System.out.println("Você escolheu: " + unidadeSelecionada.getAbsolutePath());

        System.out.println("\nDeseja filtrar por tipo de arquivo?");
        System.out.println("1. Fotos (jpg, png, gif)");
        System.out.println("2. Vídeos (mp4, avi, mkv)");
        System.out.println("3. Documentos (pdf, docx, txt)");
        System.out.println("4. Todos os arquivos");
        System.out.print("Escolha uma opção: ");
        String opcaoFiltro = sc.nextLine();

        String[] extensoes = null;
        switch (opcaoFiltro) {
            case "1":
                AdminFile.listarArquivos(sc); // Adicione o parâmetro Scanner
                break;
            case "2":
                extensoes = new String[] {".mp4", ".avi", ".mkv"};
                break;
            case "3":
                extensoes = new String[] {".pdf", ".docx", ".txt"};
                break;
            case "4":
                // Sem filtro
                break;
            default:
                System.out.println("Opção inválida.");
                return;
        }

        System.out.println("\nListando arquivos em: " + unidadeSelecionada.getAbsolutePath());
        File[] arquivos = unidadeSelecionada.listFiles();
        if (arquivos != null) {
            for (File arquivo : arquivos) {
                if (extensoes == null || correspondeExtensao(arquivo.getName(), extensoes)) {
                    System.out.println(" - " + arquivo.getName());
                }
            }
        } else {
            System.out.println("Não foi possível acessar os arquivos.");
        }
    }

    private static boolean correspondeExtensao(String nomeArquivo, String[] extensoes) {
        for (String extensao : extensoes) {
            if (nomeArquivo.toLowerCase().endsWith(extensao)) {
                return true;
            }
        }
        return false;
    }
    public static void moverArquivo(Scanner sc) {
        System.out.print("Informe o caminho completo do arquivo que deseja mover: ");
        String origem = sc.nextLine();
        File arquivoOrigem = new File(origem);

        if (!arquivoOrigem.exists() || !arquivoOrigem.isFile()) {
            System.out.println("Arquivo não encontrado ou não é um arquivo válido.");
            return;
        }

        File pastaDestino = solicitarDiretorioDestino(sc);
        if (pastaDestino == null) return;

        File novoArquivo = new File(pastaDestino, arquivoOrigem.getName());

        try {
            Files.move(arquivoOrigem.toPath(), novoArquivo.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Arquivo movido para: " + novoArquivo.getPath());
            Usuario.registrarLog("Arquivo movido de " + origem + " para " + novoArquivo.getPath());
        } catch (IOException e) {
            System.out.println("Erro ao mover o arquivo: " + e.getMessage());
            Usuario.registrarLog("Erro ao mover arquivo de " + origem + " para " + pastaDestino.getPath());
        }
    }





    public static void renomearArquivoOuPasta(Scanner sc) {
        System.out.print("Informe o caminho completo do arquivo ou pasta que deseja renomear: ");
        String caminhoAtual = sc.nextLine();
        File arquivoAtual = new File(caminhoAtual);

        if (!arquivoAtual.exists()) {
            System.out.println("Erro: Arquivo ou pasta não encontrado.");
            return;
        }

        System.out.print("Informe o novo nome (sem o caminho): ");
        String novoNome = sc.nextLine();
        File novoArquivo = new File(arquivoAtual.getParent(), novoNome);

        if (novoArquivo.exists()) {
            System.out.println("Erro: Já existe um arquivo ou pasta com esse nome.");
            return;
        }

        if (arquivoAtual.renameTo(novoArquivo)) {
            System.out.println("Renomeado com sucesso para: " + novoArquivo.getPath());
            Usuario.registrarLog("Renomeado: " + caminhoAtual + " para " + novoArquivo.getPath());
        } else {
            System.out.println("Erro ao renomear. Verifique se o arquivo está em uso ou se você tem permissões suficientes.");
        }
    }
    // Método para copiar arquivo
    public static void copiarArquivo(Scanner sc) {
        System.out.print("Informe o caminho do arquivo que deseja copiar: ");
        String origem = sc.nextLine();
        File arquivoOrigem = new File(origem);

        if (!arquivoOrigem.exists() || !arquivoOrigem.isFile()) {
            System.out.println("Arquivo não encontrado ou inválido.");
            return;
        }

        File pastaDestino = solicitarDiretorioDestino(sc);
        if (pastaDestino == null) return;

        File novoArquivo = new File(pastaDestino, arquivoOrigem.getName());

        try {
            Files.copy(arquivoOrigem.toPath(), novoArquivo.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Arquivo copiado para: " + novoArquivo.getPath());
            Usuario.registrarLog("Arquivo copiado de " + origem + " para " + novoArquivo.getPath());
        } catch (IOException e) {
            System.out.println("Erro ao copiar o arquivo: " + e.getMessage());
        }
    }

    // Método para excluir arquivo ou pasta
    public static void excluirArquivoOuPasta(Scanner sc) {
        System.out.print("Informe o caminho do arquivo ou pasta que deseja excluir: ");
        String caminho = sc.nextLine();
        File arquivo = new File(caminho);

        if (!arquivo.exists()) {
            System.out.println("Arquivo ou pasta não encontrado.");
            return;
        }

        if (arquivo.isDirectory()) {
            String[] conteudo = arquivo.list();
            if (conteudo == null || conteudo.length > 0) {
                System.out.println("A pasta não está vazia ou ocorreu um erro ao acessá-la.");
                return;
            }
        }

        if (arquivo.delete()) {
            System.out.println("Excluído com sucesso: " + caminho);
            Usuario.registrarLog("Excluído: " + caminho);
        } else {
            System.out.println("Erro ao excluir.");
        }
    }

    // Método auxiliar para solicitar diretório de destino
    private static File solicitarDiretorioDestino(Scanner sc) {
        System.out.print("Informe o diretório de destino (apenas o caminho da pasta): ");
        String destinoDiretorio = sc.nextLine();
        File pastaDestino = new File(destinoDiretorio);

        if (!pastaDestino.exists() || !pastaDestino.isDirectory()) {
            System.out.println("Diretório de destino inválido.");
            return null;
        }

        return pastaDestino;
    }
}