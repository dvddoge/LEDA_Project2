package Ordenacao.QuickSort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

public class QuickSortFullDate {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/QuickSort/";
    private String outputMedio = path + "matches_t2_full_date_quickSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_full_date_quickSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_full_date_quickSort_piorCaso.csv";
    private int fullDateIndex = 13;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public QuickSortFullDate(String inputFile) {
        this.inputFile = inputFile;
    }

    public void ordenar() {
        criarCasoMelhor();
        criarCasoMedio();
        criarCasoPior();

        System.out.println("Ordenando utilizando o algoritmo Quick Sort...");

        ordenarEImprimirTempo(outputMelhor);
        ordenarEImprimirTempo(outputMedio);
        ordenarEImprimirTempo(outputPior);

        System.out.println("\nOrdenação concluída com sucesso!");
    }

    private void criarCasoMedio() {
        copiarArquivo(inputFile, outputMedio);
    }

    private void criarCasoMelhor() {
        String[][] data = carregarArquivoEmArray(inputFile);
        quickSortIterativo(data, fullDateIndex);
        escreverDados(data, outputMelhor);
    }

    private void criarCasoPior() {
        String[][] data = carregarArquivoEmArray(inputFile);
        quickSortIterativo(data, fullDateIndex);
        inverterDados(data);
        escreverDados(data, outputPior);
    }

    private void copiarArquivo(String origem, String destino) {
        try (BufferedReader br = new BufferedReader(new FileReader(origem));
                BufferedWriter writer = new BufferedWriter(new FileWriter(destino))) {
            String line;
            while ((line = br.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[][] carregarArquivoEmArray(String file) {
        String[][] data;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            data = br.lines().skip(1).map(line -> line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1))
                    .toArray(String[][]::new);
        } catch (IOException e) {
            e.printStackTrace();
            data = new String[0][];
        }
        return data;
    }

    private void escreverDados(String[][] data, String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(
                    "id,home,away,date,year,time (utc),attendance,venue,league,home_score,away_score,home_goal_scorers,away_goal_scorers,full_date");
            writer.newLine();

            for (int i = 0; i < data.length; i++) {
                writer.write(String.join(",", data[i]));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void inverterDados(String[][] data) {
        for (int i = 0; i < data.length / 2; i++) {
            String[] temp = data[i];
            data[i] = data[data.length - i - 1];
            data[data.length - i - 1] = temp;
        }
    }

    private void ordenarEImprimirTempo(String fileToOrder) {
        String[][] data = carregarArquivoEmArray(fileToOrder);
        long startTime = System.currentTimeMillis();
        quickSortIterativo(data, fullDateIndex);
        long endTime = System.currentTimeMillis();
        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria();
    }

    private void quickSortIterativo(String[][] data, int columnIndex) {
        Stack<Intervalo> pilha = new Stack<>();
        pilha.push(new Intervalo(0, data.length - 1));

        while (!pilha.isEmpty()) {
            Intervalo intervalo = pilha.pop();
            int low = intervalo.low;
            int high = intervalo.high;

            if (low < high) {
                int pi = partition(data, low, high, columnIndex);
                pilha.push(new Intervalo(low, pi - 1));
                pilha.push(new Intervalo(pi + 1, high));
            }
        }
    }

    private int partition(String[][] data, int low, int high, int columnIndex) {
        String pivot = data[high][columnIndex];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (isDateGreater(data[j][columnIndex], pivot)) {
                i++;
                String[] temp = data[i];
                data[i] = data[j];
                data[j] = temp;
            }
        }
        String[] temp = data[i + 1];
        data[i + 1] = data[high];
        data[high] = temp;
        return i + 1;
    }

    private boolean isDateGreater(String date1, String date2) {
        try {
            Date d1 = sdf.parse(date1.replace("\"", ""));
            Date d2 = sdf.parse(date2.replace("\"", ""));
            return d1.compareTo(d2) > 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void imprimirConsumoMemoria() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();
        long usedMemory = heapMemoryUsage.getUsed();
        System.out.println("Consumo de memória: " + usedMemory + " bytes");
    }

    private static class Intervalo {
        int low;
        int high;

        Intervalo(int low, int high) {
            this.low = low;
            this.high = high;
        }
    }
}