import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.awt.Desktop; // Necessário para abrir arquivos com o programa padrão do SO

public class AdminFile {

    // Método para listar arquivos e pastas e permitir a abertura
    public static void listarArquivos(Scanner sc) {
        System.out.println("Escolha uma opção para listar arquivos/pastas:");
        System.out.println("1. Pastas padrão (Documents, Videos, Pictures, Downloads)");
        System.out.println("2. Unidades de armazenamento");
        System.out.println("3. Voltar ao Menu Principal");

        String escolhaListagem = sc.nextLine();
        File[] arquivosNoDiretorio = null;
        String caminhoDiretorioAtual = null;

        if ("1".equals(escolhaListagem)) { // Se a opção for pastas padrão [cite: 17]
            String[] pastasPadrao = {"Documents", "Videos", "Pictures", "Downloads"};
            System.out.println("\n--- Listando Pastas Padrão ---");
            for (String pasta : pastasPadrao) {
                File diretorio = new File(System.getProperty("user.home") + File.separator + pasta);
                if (diretorio.exists() && diretorio.isDirectory()) {
                    System.out.println("\nConteúdo em: " + diretorio.getAbsolutePath());
                    arquivosNoDiretorio = diretorio.listFiles(); // Lista os arquivos no diretório [cite: 19]
                    if (arquivosNoDiretorio != null && arquivosNoDiretorio.length > 0) {
                        for (File arquivo : arquivosNoDiretorio) {
                            System.out.println(" - " + (arquivo.isDirectory() ? "[Pasta] " : "[Arquivo] ") + arquivo.getName());
                        }
                    } else {
                        System.out.println("   (Nenhum arquivo ou pasta encontrado aqui)");
                    }
                } else {
                    System.out.println("\nDiretório padrão não encontrado: " + diretorio.getAbsolutePath());
                }
            }

            // Permite ao usuário selecionar uma pasta padrão específica para tentar abrir um arquivo
            System.out.print("\nSelecione o número da pasta padrão (1-" + pastasPadrao.length + ") para abrir um arquivo dela, ou digite 0 para pular: ");
            int idxPasta = -1;
            try {
                idxPasta = Integer.parseInt(sc.nextLine()) - 1;
            } catch (NumberFormatException e) {
                // Não é um número válido, continua como -1
            }

            if (idxPasta >= 0 && idxPasta < pastasPadrao.length) {
                File dirSelecionado = new File(System.getProperty("user.home") + File.separator + pastasPadrao[idxPasta]);
                if (dirSelecionado.exists() && dirSelecionado.isDirectory()) {
                    caminhoDiretorioAtual = dirSelecionado.getAbsolutePath();
                    arquivosNoDiretorio = dirSelecionado.listFiles(); // Atualiza arquivosNoDiretorio para a pasta selecionada
                    System.out.println("Você está na pasta: " + caminhoDiretorioAtual);
                    if (arquivosNoDiretorio != null && arquivosNoDiretorio.length > 0) {
                        System.out.println("\nArquivos em: " + dirSelecionado.getAbsolutePath());
                        for (File arquivo : arquivosNoDiretorio) {
                            System.out.println(" - " + (arquivo.isDirectory() ? "[Pasta] " : "[Arquivo] ") + arquivo.getName());
                        }
                    } else {
                        System.out.println("   (Nenhum arquivo ou pasta encontrado aqui para abrir)");
                        Usuario.registrarLog("Listar/Abrir (Falha)", "Pasta padrão selecionada vazia para abrir: " + dirSelecionado.getAbsolutePath());
                        return; // Não há arquivos para abrir
                    }
                } else {
                    System.out.println("A pasta selecionada não existe ou não é um diretório válido.");
                    Usuario.registrarLog("Listar/Abrir (Falha)", "Pasta padrão selecionada inválida/não existe: " + pastasPadrao[idxPasta]);
                    return;
                }
            } else if (idxPasta != -1) { // Se não for 0 e for inválido
                System.out.println("Seleção de pasta inválida.");
                Usuario.registrarLog("Listar/Abrir (Falha)", "Seleção de pasta padrão inválida: " + (idxPasta + 1));
                return;
            }

        } else if ("2".equals(escolhaListagem)) { // Se a opção for unidades de armazenamento [cite: 18]
            File[] unidades = File.listRoots();
            System.out.println("\n--- Unidades de Armazenamento ---");
            for (int i = 0; i < unidades.length; i++) {
                System.out.println((i + 1) + ". " + unidades[i].getAbsolutePath());
            }

            System.out.print("Digite o número da unidade desejada: ");
            int escolhaUnidade;
            try {
                escolhaUnidade = Integer.parseInt(sc.nextLine());
                if (escolhaUnidade < 1 || escolhaUnidade > unidades.length) {
                    System.out.println("Opção inválida.");
                    Usuario.registrarLog("Listar/Abrir (Falha)", "Opção de unidade inválida: " + escolhaUnidade);
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
                Usuario.registrarLog("Listar/Abrir (Falha)", "Entrada de unidade não numérica.");
                return;
            }

            File unidadeSelecionada = unidades[escolhaUnidade - 1];
            System.out.println("Você escolheu: " + unidadeSelecionada.getAbsolutePath());
            caminhoDiretorioAtual = unidadeSelecionada.getAbsolutePath();

            arquivosNoDiretorio = unidadeSelecionada.listFiles(); // Lista os arquivos no diretório [cite: 19]
            if (arquivosNoDiretorio != null && arquivosNoDiretorio.length > 0) {
                System.out.println("\nArquivos e Pastas encontrados:");
                for (File arquivo : arquivosNoDiretorio) {
                    System.out.println(" - " + (arquivo.isDirectory() ? "[Pasta] " : "[Arquivo] ") + arquivo.getName());
                }
            } else {
                System.out.println("Nenhum arquivo ou pasta encontrado na unidade selecionada.");
                Usuario.registrarLog("Listar/Abrir (Sucesso)", "Unidade listada, mas vazia: " + unidadeSelecionada.getAbsolutePath());
            }
        } else if ("3".equals(escolhaListagem)) { // Opção para voltar
            System.out.println("Voltando ao Menu Principal...");
            Usuario.registrarLog("Listar/Abrir (Voltar)", "Usuário voltou ao menu principal.");
            return;
        } else {
            System.out.println("Opção inválida!");
            Usuario.registrarLog("Listar/Abrir (Falha)", "Opção de listagem inválida: " + escolhaListagem);
            return;
        }

        // --- Funcionalidade de Abrir Arquivo --- [cite: 20]
        // Só tenta abrir se um diretório foi selecionado e tem arquivos para listar
        if (caminhoDiretorioAtual != null && arquivosNoDiretorio != null && arquivosNoDiretorio.length > 0) {
            System.out.print("\nDigite o NOME COMPLETO do arquivo que deseja abrir (ou digite 'voltar' para o menu principal): ");
            String nomeArquivoParaAbrir = sc.nextLine();

            if (nomeArquivoParaAbrir.equalsIgnoreCase("voltar")) {
                System.out.println("Voltando ao Menu Principal...");
                return;
            }

            File arquivoParaAbrir = new File(caminhoDiretorioAtual, nomeArquivoParaAbrir);

            // Tenta verificar se o usuário digitou um caminho completo (fallback caso o nome não seja encontrado no diretório atual)
            if (!arquivoParaAbrir.exists()) {
                arquivoParaAbrir = new File(nomeArquivoParaAbrir);
            }

            if (arquivoParaAbrir.exists()) {
                if (arquivoParaAbrir.isFile()) {
                    try {
                        if (Desktop.isDesktopSupported()) { // Verifica se a funcionalidade Desktop é suportada pelo SO
                            Desktop.getDesktop().open(arquivoParaAbrir); // Abre o arquivo com o programa padrão do SO
                            System.out.println("Arquivo '" + arquivoParaAbrir.getName() + "' aberto com o programa padrão do sistema.");
                            Usuario.registrarLog("Abrir Arquivo (Sucesso)", "Arquivo '" + arquivoParaAbrir.getAbsolutePath() + "' aberto.");
                        } else {
                            System.out.println("Funcionalidade de abertura de arquivo não suportada neste sistema operacional.");
                            Usuario.registrarLog("Abrir Arquivo (Falha)", "Funcionalidade Desktop não suportada para '" + arquivoParaAbrir.getAbsolutePath() + "'");
                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao tentar abrir o arquivo: " + e.getMessage());
                        Usuario.registrarLog("Abrir Arquivo (Falha)", "Erro de IO ao abrir '" + arquivoParaAbrir.getAbsolutePath() + "': " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Caminho do arquivo inválido para abertura: " + e.getMessage());
                        Usuario.registrarLog("Abrir Arquivo (Falha)", "Caminho inválido ao abrir '" + arquivoParaAbrir.getAbsolutePath() + "': " + e.getMessage());
                    }
                } else if (arquivoParaAbrir.isDirectory()) {
                    System.out.println("O item selecionado é uma PASTA, não um arquivo. Não é possível 'abrir' pastas com esta opção.");
                    Usuario.registrarLog("Abrir Arquivo (Falha)", "Tentou abrir uma pasta: " + arquivoParaAbrir.getAbsolutePath());
                }
            } else {
                System.out.println("Arquivo ou pasta não encontrado no diretório atual ou o caminho completo está incorreto.");
                Usuario.registrarLog("Abrir Arquivo (Falha)", "Arquivo não encontrado para abrir: " + nomeArquivoParaAbrir + " no diretório: " + caminhoDiretorioAtual);
            }
        } else if (caminhoDiretorioAtual == null || arquivosNoDiretorio == null || arquivosNoDiretorio.length == 0) {
            System.out.println("Não há itens para abrir no diretório selecionado ou nenhuma seleção foi feita.");
            Usuario.registrarLog("Listar/Abrir (Informação)", "Não há itens ou diretório não selecionado para abrir.");
        }
    }

    // Método para renomear arquivos ou pastas [cite: 22]
    public static void renomearArquivoOuPasta(Scanner sc) {
        System.out.print("Informe o caminho COMPLETO do arquivo ou pasta que deseja renomear: ");
        String caminhoAtual = sc.nextLine();
        File arquivoAtual = new File(caminhoAtual);

        if (!arquivoAtual.exists()) {
            System.out.println("Erro: Arquivo ou pasta não encontrado.");
            Usuario.registrarLog("Renomear (Falha)", "Arquivo ou pasta não encontrado: " + caminhoAtual);
            return;
        }

        System.out.print("Informe o NOVO NOME (sem o caminho, apenas o nome): ");
        String novoNome = sc.nextLine();
        File novoArquivo = new File(arquivoAtual.getParent(), novoNome);

        if (novoArquivo.exists()) {
            System.out.println("Erro: Já existe um arquivo ou pasta com esse nome no destino.");
            Usuario.registrarLog("Renomear (Falha)", "Novo nome já existe: " + novoArquivo.getPath());
            return;
        }

        if (arquivoAtual.renameTo(novoArquivo)) {
            System.out.println("Renomeado com sucesso para: " + novoArquivo.getPath());
            Usuario.registrarLog("Renomear (Sucesso)", "De '" + caminhoAtual + "' para '" + novoArquivo.getPath() + "'");
        } else {
            System.out.println("Erro ao renomear. Verifique se o arquivo está em uso, se você tem permissões suficientes ou se é uma pasta não vazia.");
            Usuario.registrarLog("Renomear (Falha)", "Erro desconhecido ao renomear '" + caminhoAtual + "'");
        }
    }

    // Método para mover arquivos [cite: 23, 24]
    public static void moverArquivo(Scanner sc) {
        System.out.print("Informe o caminho COMPLETO do arquivo que deseja mover: ");
        String origem = sc.nextLine();
        File arquivoOrigem = new File(origem);

        if (!arquivoOrigem.exists() || !arquivoOrigem.isFile()) { // Mover é para arquivo, não pasta
            System.out.println("Erro: Arquivo não encontrado ou inválido. Esta opção é para arquivos, não pastas.");
            Usuario.registrarLog("Mover (Falha)", "Arquivo de origem não encontrado ou inválido: " + origem);
            return;
        }

        System.out.print("Informe o diretório de DESTINO COMPLETO para onde mover: ");
        String destino = sc.nextLine();
        File pastaDestino = new File(destino);

        if (!pastaDestino.exists() || !pastaDestino.isDirectory()) {
            System.out.println("Erro: Diretório de destino inválido ou não existe.");
            Usuario.registrarLog("Mover (Falha)", "Diretório de destino inválido: " + destino);
            return;
        }

        File novoArquivoNoDestino = new File(pastaDestino, arquivoOrigem.getName());
        try {
            Files.move(arquivoOrigem.toPath(), novoArquivoNoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Arquivo movido com sucesso para: " + novoArquivoNoDestino.getPath());
            Usuario.registrarLog("Mover (Sucesso)", "Arquivo movido de '" + origem + "' para '" + novoArquivoNoDestino.getPath() + "'");
        } catch (IOException e) {
            System.out.println("Erro ao mover o arquivo: " + e.getMessage());
            Usuario.registrarLog("Mover (Falha)", "Erro ao mover '" + origem + "' para '" + novoArquivoNoDestino.getPath() + "': " + e.getMessage());
        }
    }

    // Método para copiar arquivos [cite: 25, 26]
    public static void copiarArquivo(Scanner sc) {
        System.out.print("Informe o caminho COMPLETO do arquivo que deseja copiar: ");
        String original = sc.nextLine();
        File arquivoOrigem = new File(original);

        if (!arquivoOrigem.exists() || !arquivoOrigem.isFile()) { // Copiar é para arquivo, não pasta
            System.out.println("Erro: Arquivo não encontrado ou inválido. Esta opção é para arquivos, não pastas.");
            Usuario.registrarLog("Copiar (Falha)", "Arquivo de origem não encontrado ou inválido: " + original);
            return;
        }

        System.out.print("Informe o diretório de DESTINO COMPLETO para onde copiar: ");
        String destino = sc.nextLine();
        File pastaDestino = new File(destino);

        if (!pastaDestino.exists() || !pastaDestino.isDirectory()) {
            System.out.println("Erro: Diretório de destino inválido ou não existe.");
            Usuario.registrarLog("Copiar (Falha)", "Diretório de destino inválido: " + destino);
            return;
        }

        File novoArquivoNoDestino = new File(pastaDestino, arquivoOrigem.getName());
        try {
            Files.copy(arquivoOrigem.toPath(), novoArquivoNoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Arquivo copiado com sucesso para: " + novoArquivoNoDestino.getPath());
            Usuario.registrarLog("Copiar (Sucesso)", "Arquivo copiado de '" + original + "' para '" + novoArquivoNoDestino.getPath() + "'");
        } catch (IOException e) {
            System.out.println("Erro ao copiar o arquivo: " + e.getMessage());
            Usuario.registrarLog("Copiar (Falha)", "Erro ao copiar '" + original + "' para '" + novoArquivoNoDestino.getPath() + "': " + e.getMessage());
        }
    }

    // Método para excluir arquivos ou pastas [cite: 27, 28, 29]
    public static void excluirArquivoOuPasta(Scanner sc) {
        System.out.println("Escolha uma opção para excluir:");
        System.out.println("1. Listar pastas padrão para exclusão");
        System.out.println("2. Informar caminho COMPLETO do arquivo/pasta a ser excluído diretamente");
        System.out.print("Escolha: ");
        String escolhaOpcao = sc.nextLine();

        String caminhoParaExcluir = null;

        if ("1".equals(escolhaOpcao)) { // Se a opção for listar pastas padrão [cite: 27]
            String[] pastasPadrao = {"Documents", "Videos", "Pictures", "Downloads"};
            File diretorioSelecionado = null;

            System.out.println("\nPastas padrão disponíveis:");
            for (int i = 0; i < pastasPadrao.length; i++) {
                File tempDir = new File(System.getProperty("user.home") + File.separator + pastasPadrao[i]);
                if (tempDir.exists() && tempDir.isDirectory()) {
                    System.out.println((i + 1) + ". " + tempDir.getAbsolutePath());
                } else {
                    System.out.println((i + 1) + ". (Não encontrada) " + tempDir.getAbsolutePath());
                }
            }

            System.out.print("Digite o número da pasta padrão que deseja listar para exclusão: ");
            try {
                int numPasta = Integer.parseInt(sc.nextLine());
                if (numPasta > 0 && numPasta <= pastasPadrao.length) {
                    diretorioSelecionado = new File(System.getProperty("user.home") + File.separator + pastasPadrao[numPasta - 1]);
                } else {
                    System.out.println("Opção de pasta inválida.");
                    Usuario.registrarLog("Excluir (Falha)", "Opção de pasta padrão inválida: " + numPasta);
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                Usuario.registrarLog("Excluir (Falha)", "Entrada não numérica para seleção de pasta.");
                return;
            }

            if (diretorioSelecionado != null && diretorioSelecionado.exists() && diretorioSelecionado.isDirectory()) {
                System.out.println("\nConteúdo em: " + diretorioSelecionado.getAbsolutePath());
                File[] arquivos = diretorioSelecionado.listFiles(); // Exibe os arquivos armazenados [cite: 28, 29]
                if (arquivos != null && arquivos.length > 0) {
                    for (File arquivo : arquivos) {
                        System.out.println(" - " + (arquivo.isDirectory() ? "[Pasta] " : "[Arquivo] ") + arquivo.getName());
                    }
                } else {
                    System.out.println("   (Nenhum arquivo ou pasta encontrado aqui)");
                    Usuario.registrarLog("Excluir (Informação)", "Pasta padrão selecionada vazia: " + diretorioSelecionado.getAbsolutePath());
                }
                System.out.print("Informe o NOME (não o caminho completo) do arquivo ou pasta que deseja excluir de '" + diretorioSelecionado.getAbsolutePath() + "': ");
                String nomeParaExcluir = sc.nextLine();
                caminhoParaExcluir = new File(diretorioSelecionado, nomeParaExcluir).getAbsolutePath();

            } else {
                System.out.println("A pasta padrão selecionada não existe ou não é um diretório válido.");
                Usuario.registrarLog("Excluir (Falha)", "Pasta padrão inválida/não existe: " + (diretorioSelecionado != null ? diretorioSelecionado.getAbsolutePath() : "null"));
                return;
            }

        } else if ("2".equals(escolhaOpcao)) { // Se a opção for informar caminho completo diretamente
            System.out.print("Informe o caminho COMPLETO do arquivo ou pasta que deseja excluir: ");
            caminhoParaExcluir = sc.nextLine();
        } else {
            System.out.println("Opção inválida!");
            Usuario.registrarLog("Excluir (Falha)", "Opção de exclusão inicial inválida: " + escolhaOpcao);
            return;
        }

        if (caminhoParaExcluir == null || caminhoParaExcluir.trim().isEmpty()) {
            System.out.println("Nenhum caminho válido informado para exclusão.");
            return;
        }

        File arquivoOuPasta = new File(caminhoParaExcluir);

        if (!arquivoOuPasta.exists()) {
            System.out.println("Erro: Arquivo ou pasta não encontrado.");
            Usuario.registrarLog("Excluir (Falha)", "Arquivo ou pasta não encontrado para exclusão: " + caminhoParaExcluir);
            return;
        }

        // Confirmação para exclusão, essencial para evitar perdas acidentais
        System.out.print("ATENÇÃO: Tem certeza que deseja EXCLUIR '" + arquivoOuPasta.getAbsolutePath() + "'? (s/n): ");
        String confirmacao = sc.nextLine();
        if (!confirmacao.equalsIgnoreCase("s")) {
            System.out.println("Operação de exclusão cancelada.");
            Usuario.registrarLog("Excluir (Cancelada)", "Exclusão cancelada para: " + arquivoOuPasta.getAbsolutePath());
            return;
        }

        if (arquivoOuPasta.delete()) {
            System.out.println("Excluído com sucesso: " + caminhoParaExcluir);
            Usuario.registrarLog("Excluir (Sucesso)", "Excluído: " + caminhoParaExcluir);
        } else {
            System.out.println("Erro ao excluir. Verifique se o arquivo está em uso ou se você tem permissões suficientes.");
            // Observação: File.delete() não exclui pastas não vazias. Para isso, seria necessário recursão.
            if (arquivoOuPasta.isDirectory() && arquivoOuPasta.listFiles() != null && arquivoOuPasta.listFiles().length > 0) {
                System.out.println("    (Nota: Não é possível excluir pastas não vazias diretamente com esta função. Esvazie a pasta primeiro.)");
            }
            Usuario.registrarLog("Excluir (Falha)", "Erro ao excluir '" + caminhoParaExcluir + "'. Possíveis causas: em uso, permissão, pasta não vazia.");
        }
    }
}