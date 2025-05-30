import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String opcao;

        System.out.println("Usuário logado: " + System.getProperty("user.name"));

        do {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Abrir arquivos/pastas");
            System.out.println("2. Renomear arquivo/pasta");
            System.out.println("3. Mover arquivo");
            System.out.println("4. Copiar arquivo");
            System.out.println("5. Excluir arquivo/pasta");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextLine();

            switch (opcao) {
                case "1":
                    AdminFile.listarArquivos();
                    break;
                case "2":
                    AdminFile.renomearArquivoOuPasta(sc);
                    break;
                case "3":
                    AdminFile.moverArquivo(sc);
                    break;
                case "4":
                    AdminFile.copiarArquivo(sc);
                    break;
                case "5":
                    AdminFile.excluirArquivoOuPasta(sc);
                    break;
                case "6":
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (!opcao.equals("6"));

        sc.close();
    }
}


