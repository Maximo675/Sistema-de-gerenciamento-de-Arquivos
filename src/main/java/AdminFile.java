import java.io.File;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class AdminFile {
    private static final String[] PASTAS_PADRAO = {
            System.getProperty("user.home") + "\\Documents",
            System.getProperty("user.home") + "\\Videos",
            System.getProperty("user.home") + "\\Pictures",
            System.getProperty("user.home") + "\\Downloads"
    };

    public static void listarArquivos() {
        System.out.println("Listando pastas padrão:");
        for (String caminho : PASTAS_PADRAO) {
            File pasta = new File(caminho);
            if (pasta.exists() && pasta.isDirectory()) {
                System.out.println("Conteúdo de: " + caminho);
                File[] arquivos = pasta.listFiles();
                if (arquivos != null) {
                    for (File arquivo : arquivos) {
                        System.out.println(" - " + arquivo.getName());
                    }
                } else {
                    System.out.println(" (vazio)");
                }
            }
        }
        Usuario.registrarLog("Listagem de arquivos executada.");
    }

    public static void renomearArquivoOuPasta(Scanner sc) {
        System.out.print("Informe o caminho completo do arquivo ou pasta que deseja renomear: ");
        String caminhoOriginal = sc.nextLine();

        File arquivoOriginal = new File(caminhoOriginal);
        if (!arquivoOriginal.exists()) {
            System.out.println("Arquivo ou pasta não encontrado.");
            return;
        }

        System.out.print("Informe o novo nome (sem caminho, apenas o nome): ");
        String novoNome = sc.nextLine();

        File novoArquivo = new File(arquivoOriginal.getParent(), novoNome);

        boolean sucesso = arquivoOriginal.renameTo(novoArquivo);

        if (sucesso) {
            System.out.println("Renomeado com sucesso para: " + novoArquivo.getPath());
            Usuario.registrarLog("Renomeado: " + caminhoOriginal + " -> " + novoArquivo.getPath());
        } else {
            System.out.println("Falha ao renomear.");
            Usuario.registrarLog("Erro ao renomear: " + caminhoOriginal);
        }
    }
    public static void moverArquivo(Scanner sc) {
        System.out.print("Informe o caminho completo do arquivo que deseja mover: ");
        String origem = sc.nextLine();
        File arquivoOrigem = new File(origem);

        if (!arquivoOrigem.exists()) {
            System.out.println("Arquivo não encontrado.");
            return;
        }

        File pastaDestino = solicitarDiretorioDestino(sc);
        if (pastaDestino == null) return;

        File novoArquivo = new File(pastaDestino, arquivoOrigem.getName());

        try {
            Files.move(arquivoOrigem.toPath(), novoArquivo.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Arquivo movido para: " + novoArquivo.getPath());
            Usuario.registrarLog("Arquivo movido de " + origem + " para " + novoArquivo.getPath());
        } catch (Exception e) {
            System.out.println("Erro ao mover o arquivo: " + e.getMessage());
            Usuario.registrarLog("Erro ao mover arquivo de " + origem + " para " + pastaDestino.getPath());
        }
    }


    public static void copiarArquivo(Scanner sc) {
        System.out.print("Informe o caminho completo do arquivo que deseja copiar: ");
        String origem = sc.nextLine();
        File arquivoOrigem = new File(origem);

        if (!arquivoOrigem.exists() || !arquivoOrigem.isFile()) {
            System.out.println("Arquivo não encontrado ou não é um arquivo válido.");
            return;
        }

        File pastaDestino = solicitarDiretorioDestino(sc);
        if (pastaDestino == null) return;

        File arquivoDestino = new File(pastaDestino, arquivoOrigem.getName());

        try {
            Files.copy(arquivoOrigem.toPath(), arquivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Arquivo copiado para: " + arquivoDestino.getPath());
            Usuario.registrarLog("Arquivo copiado de " + origem + " para " + arquivoDestino.getPath());
        } catch (Exception e) {
            System.out.println("Erro ao copiar o arquivo: " + e.getMessage());
            Usuario.registrarLog("Erro ao copiar arquivo de " + origem + " para " + pastaDestino.getPath());
        }
    }

    public static void excluirArquivoOuPasta(Scanner sc) {
        System.out.print("Informe o caminho completo do arquivo ou pasta que deseja excluir: ");
        String caminho = sc.nextLine();
        File alvo = new File(caminho);

        if (!alvo.exists()) {
            System.out.println("Arquivo ou pasta não encontrado.");
            return;
        }

        boolean sucesso;
        if (alvo.isDirectory()) {
            sucesso = excluirDiretorioRecursivo(alvo);
        } else {
            sucesso = alvo.delete();
        }

        if (sucesso) {
            System.out.println("Exclusão concluída.");
            Usuario.registrarLog("Excluído: " + caminho);
        } else {
            System.out.println("Erro ao excluir.");
            Usuario.registrarLog("Erro ao excluir: " + caminho);
        }
    }

    private static boolean excluirDiretorioRecursivo(File pasta) {
        File[] arquivos = pasta.listFiles();
        if (arquivos != null) {
            for (File f : arquivos) {
                if (f.isDirectory()) {
                    excluirDiretorioRecursivo(f);
                } else {
                    boolean deletado = f.delete();
                    if (!deletado) {
                        System.out.println("Não foi possível excluir o arquivo: " + f.getAbsolutePath());
                        Usuario.registrarLog("Erro ao excluir arquivo: " + f.getAbsolutePath());
                    }
                }
            }
        }

        boolean deletado = pasta.delete();
        if (!deletado) {
            System.out.println("Não foi possível excluir a pasta: " + pasta.getAbsolutePath());
            Usuario.registrarLog("Erro ao excluir pasta: " + pasta.getAbsolutePath());
        }

        return deletado;
    }
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

