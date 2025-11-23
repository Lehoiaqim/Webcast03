// Grupo : 

// Fábio Kaio Gois Torres
// Júlia Amorim da Costa Rabello
// Lehoiaqim de Souza Silva 
// Rayane Rafaela do Carmo Silva
// Rubem de Morais Falcão Neto

// Professor enviamos em arquivo pois tivermos dificuldades de enviar através pelo VScode, mas aqui está o nosso código em funcionamento.

import java.util.Scanner;
import java.io.*;
import java.util.*;

public class web {

    private String nome;
    private String telefone;
    private String email;

    public web (String nome, String telefone, String email) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Tente de novo. O nome não pode estar vazio.");
        }
        if (!telefone.matches("\\d{8,15}")) {
            throw new IllegalArgumentException("O telefone tem que conter apenas números (8 a 15 dígitos).");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Email inválido.");
        }

        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "📇 Contato {" +
               " Nome='" + nome + '\'' +
               ", Telefone='" + telefone + '\'' +
               ", Email='" + email + '\'' +
               " }";
    }
    public static interface GerenciadorContatos {

    void adicionarContato(web contato) throws ContatoExistenteException;

    web buscarContato(String nome) throws ContatoNaoEncontradoException;

    void removerContato(String nome) throws ContatoNaoEncontradoException;

    List<web> listarTodosContatos();
}
public static class ContatoExistenteException extends Exception {
    public ContatoExistenteException(String mensagem) {
        super(" ERRO: " + mensagem);
    }
}
public static class ContatoNaoEncontradoException extends Exception {
    public ContatoNaoEncontradoException(String mensagem) {
        super(" ERRO: " + mensagem);
    }
}
public static class AgendaManager implements GerenciadorContatos {

    private List<web> contatos = new ArrayList<>();

    @Override
    public void adicionarContato(web contato) throws ContatoExistenteException {
        for (web c : contatos) {
            if (c.getNome().equalsIgnoreCase(contato.getNome())) {
                throw new ContatoExistenteException("Contato já existe!");
            }
        }
        contatos.add(contato);
    }

    @Override
    public web buscarContato(String nome) throws ContatoNaoEncontradoException {
        for (web c : contatos) {
            if (c.getNome().equalsIgnoreCase(nome)) {
                return c;
            }
        }
        throw new ContatoNaoEncontradoException("Contato não encontrado!");
    }

    @Override
    public void removerContato(String nome) throws ContatoNaoEncontradoException {
        web contato = buscarContato(nome);
        contatos.remove(contato);
    }

    @Override
    public List<web> listarTodosContatos() {
        return contatos;
    }

    // ------------------ CSV ------------------

    public void salvarContatosCSV(String nomeArquivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {

            for (web c : contatos) {
                writer.println(c.getNome() + ";" + c.getTelefone() + ";" + c.getEmail());
            }

            System.out.println("Contatos salvos com sucesso!");

        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    public void carregarContatosCSV(String nomeArquivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {

            contatos.clear(); // limpa lista antes de carregar

            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 3) {
                    contatos.add(new web(partes[0], partes[1], partes[2]));
                }
            }

            System.out.println("Contatos carregados com sucesso!");

        } catch (IOException e) {
            System.out.println("Erro ao carregar arquivo: " + e.getMessage());
        }
    }

    // ------------------ Ordenação ------------------

    public List<web> listarContatosOrdenados() {
        List<web> ordenada = new ArrayList<>(contatos);

        ordenada.sort(Comparator.comparing(
                c -> c.getNome().toLowerCase()
        ));

        return ordenada;
    }

    // ------------------ Buscar por domínio ------------------

    public List<web> buscarPorDominioEmail(String dominio) {
        List<web> filtrados = new ArrayList<>();

        for (web c : contatos) {
            if (c.getEmail().endsWith("@" + dominio)) {
                filtrados.add(c);
            }
        }

        return filtrados;
    }
}
public static class AgendaApplication {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        AgendaManager manager = new AgendaManager();

        int opcao;

        do {
             System.out.println("\n");
            System.out.println("==============================================");
            System.out.println("                A G E N D A ");
            System.out.println("==============================================");
            System.out.println("1°  Adicionar Contato");
            System.out.println("2°  Buscar Contato");
            System.out.println("3°  Remover Contato");
            System.out.println("4°  Listar Todos os Contatos");
            System.out.println("5°  Salvar em CSV");
            System.out.println("6°  Carregar de CSV");
            System.out.println("7°  Sair");
            System.out.println("==============================================");
            System.out.print(" Escolha uma opção: ");

            while (!scanner.hasNextInt()) {
            System.out.print("Digite um número válido: ");
            scanner.next();
            }

            opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {

                case 1:
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();

                    System.out.print("Telefone: ");
                    String telefone = scanner.nextLine();

                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    try {
                        manager.adicionarContato(new web(nome, telefone, email));
                        System.out.println("Contato adicionado!");
                    } catch (ContatoExistenteException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Digite o nome para buscar: ");
                    String nomeBusca = scanner.nextLine();

                    try {
                        web c = manager.buscarContato(nomeBusca);
                        System.out.println("Encontrado: " + c);
                    } catch (ContatoNaoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    System.out.print("Nome para remover: ");
                    String nomeRemover = scanner.nextLine();

                    try {
                        manager.removerContato(nomeRemover);
                        System.out.println("Contato removido.");
                    } catch (ContatoNaoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    List<web> lista = manager.listarTodosContatos();
                    if (lista.isEmpty()) {
                        System.out.println("Nenhum contato encontrado.");
                    } else {
                        System.out.println("Seus Contatos: ");
                        lista.forEach(System.out::println);
                    }
                    break;

                case 5:
                    System.out.print("Nome do arquivo CSV para salvamento: ");
                    String nomeArquivoSalvar = scanner.nextLine();
                    manager.salvarContatosCSV(nomeArquivoSalvar);
                    break;

                case 6:
                    System.out.print("Nome do arquivo CSV para carregar: ");
                    String nomeArquivoCarregar = scanner.nextLine();
                    manager.carregarContatosCSV(nomeArquivoCarregar);
                    break;

                case 7:
                    System.out.println("Saindo da agenda... Até logo!!");
                    break;

                default:
                    System.out.println("Opção inválida .");
            }

        } while (opcao != 7);

        scanner.close();
    }
}
}
