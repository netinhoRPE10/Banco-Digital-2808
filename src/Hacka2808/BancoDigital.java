package Hacka2808;

import java.util.*;

// Classe Cliente, com atributos definidos, construtor
class Cliente {
    private String nome;
    private String cpf;

    public Cliente(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }
    public String getCpf() {
        return cpf;
    }

    @Override
    public String toString() {
        return nome + " (CPF: " + cpf + ")";
    }
}

// Classe Conta, com atributos sendo definidos, definição de uma conta com saldo 0 e valores, e adição de metodos
class Conta {
    private int numero;
    private double saldo;
    private Cliente cliente;
    private List<String> historico;

    public Conta(int numero, Cliente cliente) {
        this.numero = numero;
        this.cliente = cliente;
        this.saldo = 0.0;
        this.historico = new ArrayList<>();
        historico.add("Conta criada para " + cliente.getNome());
    }

    public int getNumero() {
        return numero;
    }
    public double getSaldo() {
        return saldo;
    }
    public Cliente getCliente() {
        return cliente;
    }

    public void depositar(double valor) throws IllegalArgumentException {
        if (valor <= 0)
            throw new IllegalArgumentException("Valor de depósito deve ser positivo.");
        saldo += valor;
        historico.add("Depósito: +" + valor);
    }

    public void sacar(double valor) throws IllegalArgumentException {
        if (valor <= 0)
            throw new IllegalArgumentException("Valor de saque deve ser positivo.");
        if (valor > saldo)
            throw new IllegalArgumentException("Saldo insuficiente.");
        saldo -= valor;
        historico.add("Saque: -" + valor);
    }

    public void transferir(Conta destino, double valor) throws IllegalArgumentException {
        this.sacar(valor);
        destino.depositar(valor);
        historico.add("Transferência para conta " + destino.getNumero() + ": -" + valor);
        destino.historico.add("Transferência recebida da conta " + this.numero + ": +" + valor);
    }

    public void exibirHistorico() {
        System.out.println("Histórico da conta " + numero + ":");
        for (String evento : historico) {
            System.out.println("- " + evento);
        }
    }

    @Override
    public String toString() {
        return "Conta " + numero + " | Cliente: " + cliente.getNome() + " | Saldo: " + saldo;
    }
}

// Classe Principal
public class BancoDigital {
    private static Map<Integer, Conta> contas = new HashMap<>();
    private static int contadorContas = 1;
    private static Scanner ler = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao;

        do {
            System.out.println("\n--- BANCO DIGITAL ---");
            System.out.println("1. Criar conta");
            System.out.println("2. Depositar");
            System.out.println("3. Sacar");
            System.out.println("4. Transferir");
            System.out.println("5. Listar contas");
            System.out.println("6. Histórico de uma conta");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            opcao = ler.nextInt();
            ler.nextLine(); // consumir quebra de linha
            try {
                switch (opcao) {
                    case 1: criarConta(); break;
                    case 2: depositar(); break;
                    case 3: sacar(); break;
                    case 4: transferir(); break;
                    case 5: listarContas(); break;
                    case 6: historicoConta(); break;
                    case 0: System.out.println("Encerrando..."); break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (opcao != 0);
    }
    private static void criarConta() {
        System.out.print("Nome do cliente: ");
        String nome = ler.nextLine();
        System.out.print("CPF: ");
        String cpf = ler.nextLine();

        Cliente cliente = new Cliente(nome, cpf);
        Conta conta = new Conta(contadorContas++, cliente);
        contas.put(conta.getNumero(), conta);
        System.out.println("Conta criada: " + conta);
    }
    private static void depositar() {
        Conta conta = buscarConta();
        System.out.print("Valor do depósito: ");
        double valor = ler.nextDouble();
        conta.depositar(valor);
        System.out.println("Depósito realizado. Saldo atual: " + conta.getSaldo());
    }
    private static void sacar() {
        Conta conta = buscarConta();
        System.out.print("Valor do saque: ");
        double valor = ler.nextDouble();
        conta.sacar(valor);
        System.out.println("Saque realizado. Saldo atual: " + conta.getSaldo());
    }
    private static void transferir() {
        System.out.println("Conta de origem:");
        Conta origem = buscarConta();
        System.out.println("Conta de destino:");
        Conta destino = buscarConta();
        System.out.print("Valor da transferência: ");
        double valor = ler.nextDouble();
        origem.transferir(destino, valor);
        System.out.println("Transferência concluída!");
    }
    private static void listarContas() {
        if (contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.");
        } else {
            for (Conta c : contas.values()) {
                System.out.println(c);
            }
        }
    }
    private static void historicoConta() {
        Conta conta = buscarConta();
        conta.exibirHistorico();
    }
    private static Conta buscarConta() {
        System.out.print("Número da conta: ");
        int numero = ler.nextInt();
        ler.nextLine(); // consumir quebra de linha
        Conta conta = contas.get(numero);
        if (conta == null) throw new NoSuchElementException("Conta não encontrada.");
        return conta;
    }
}
