import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Executavel implements Runnable {
    private static File txt = new File("conta.txt");

    @Override
    public void run() {
        Scanner reader = new Scanner(System.in);
        Scanner scanFile;
        FileWriter writer;

        try {
            if (txt.createNewFile()) {
                writer = new FileWriter("conta.txt");

                writer.write("100");
                writer.close();
            }
        } catch (IOException e1) {
            System.out.println("Não foi possivel criar o arquivo \"conta.txt\".");
        }

        System.out.println("Selecione a operação: \n[C]onsultar, [H]istórico, [D]epositar, [S]acar");
        char operacao = reader.nextLine().charAt(0);

        if (operacao == 'c' || operacao == 'C') {
            try {
                scanFile = new Scanner(txt);

                int saldo;
                synchronized (Main.class) {
                    saldo = scanFile.nextInt();
                }
                System.out.println("Saldo em conta: " + saldo);

                scanFile.close();
            } catch (IOException e) {
                System.out.println("Arquivo \"conta.txt\" não encontrado!");
            }

        } else if (operacao == 'h' || operacao == 'H') {
            try {
                scanFile = new Scanner(txt);

                String saldo;
                synchronized (Main.class) {
                    saldo = scanFile.nextLine();
                    System.out.println("Saldo em conta: " + saldo);

                    System.out.println("Histórico:");
                    System.out.println("Saldo anteior | Operação | Valor movimentado | Data/hora");

                    while (scanFile.hasNextLine()) {
                        System.out.println(scanFile.nextLine());
                    }
                }

                scanFile.close();
            } catch (IOException e) {
                System.out.println("Arquivo \"conta.txt\" não encontrado!");
            }
        } else if (operacao == 'd' || operacao == 'd') {
            System.out.print("Valor a depositar: ");
            int valor = reader.nextInt();
            reader.nextLine();

            System.out.println("Data/hora: (Formato: 31/12/2021 23:59)");
            String data = reader.nextLine().trim();

            try {
                scanFile = new Scanner(txt);

                int saldo;
                synchronized (Main.class) {
                    saldo = scanFile.nextInt();
                }

                synchronized (Main.class) {
                    writeConta(saldo, valor, saldo + valor, "+", data);
                }

                System.out.println("Valor depositado com sucesso!");

                scanFile.close();
            } catch (IOException e) {
                System.out.println("Arquivo \"conta.txt\" não encontrado!");
            }
        } else if (operacao == 's' || operacao == 's') {
            System.out.print("Valor a sacar: ");
            int valor = reader.nextInt();
            reader.nextLine();

            System.out.println("Data/hora: (Formato: 31/12/2021 23:59)");
            String data = reader.nextLine().trim();

            try {
                scanFile = new Scanner(txt);

                int saldo;
                synchronized (Main.class) {
                    saldo = scanFile.nextInt();
                }

                if (saldo - valor >= 0) {
                    synchronized (Main.class) {
                        writeConta(saldo, valor, saldo - valor, "-", data);
                    }
                    System.out.println("Valor sacado com sucesso!");
                } else {
                    System.out.println("Saldo disponível insuficiente!");
                }

                scanFile.close();
            } catch (IOException e) {
                System.out.println("Arquivo \"conta.txt\" não encontrado!");
            }
        }

        reader.close();
    }

    public static void writeConta(int saldo, int valor, int saldoFinal, String operacao,
            String data) {
        Scanner fileScanner;
        try {
            fileScanner = new Scanner(txt);
            // ignora a primeira linha
            fileScanner.nextLine();

            FileWriter fileStream = new FileWriter("conta.txt");
            BufferedWriter out = new BufferedWriter(fileStream);

            // adiciona o saldo ao inicio do arquivo
            out.write(saldoFinal + "");
            out.newLine();

            while (fileScanner.hasNextLine()) {
                String next = fileScanner.nextLine();
                if (next.equals("\n"))
                    out.newLine();
                else
                    out.write(next);
                out.newLine();
            }

            // adiciona a operacao ao fim do arquivo
            out.write(saldo + " " + operacao + " " + valor + " (" + data + ")");

            out.close();
        } catch (IOException e) {
            System.out.println("Deu ruim, writeConta");
            e.printStackTrace();
        }
    }
}
