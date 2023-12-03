package Ordenacao.QuickSort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Stack;

public class QuickSortAttendance {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/QuickSort/";
    private String outputMedio = path + "matches_t2_attendance_quickSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_attendance_quickSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_attendance_quickSort_piorCaso.csv";
    private int attendanceIndex = 6;

    public QuickSortAttendance(String inputFile) {
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
        quickSortIterativo(data, attendanceIndex);
        escreverDados(data, outputMelhor);
    }

    private void criarCasoPior() {
        String[][] data = carregarArquivoEmArray(inputFile);
        quickSortIterativo(data, attendanceIndex);
        inverterDados(data);
        escreverDados(data, outputPior);
    }

    private void copiarArquivo(String origem, String destino) {
        try (BufferedReader br = new BufferedReader(new FileReader(origem));
             FileWriter writer = new FileWriter(destino)) {
            String line;
            while ((line = br.readLine()) != null) {
                writer.write(line + "\n");
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
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(
                    "id,home,away,date,year,time (utc),attendance,venue,league,home_score,away_score,home_goal_scorers,away_goal_scorers,full_date\n");
            for (int i = 0; i < data.length; i++) {
                writer.write(String.join(",", data[i]) + "\n");
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
        quickSortIterativo(data, attendanceIndex);
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
                int pi = partition(data, columnIndex, low, high);
                pilha.push(new Intervalo(low, pi - 1));
                pilha.push(new Intervalo(pi + 1, high));
            }
        }
    }

    private int partition(String[][] data, int columnIndex, int low, int high) {
        // (Código de partição permanece o mesmo)
        // Escolha o pivô como a mediana entre low, middle e high
        int middle = (low + high) / 2;
        int pivotValue = medianOfThree(parseToInt(data[low][columnIndex]),
                parseToInt(data[middle][columnIndex]), parseToInt(data[high][columnIndex]));

        int pivotIndex = (pivotValue == parseToInt(data[low][columnIndex])) ? low
                : (pivotValue == parseToInt(data[middle][columnIndex])) ? middle : high;

        swap(data, pivotIndex, high);

        int pivot = parseToInt(data[high][columnIndex]);
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (parseToInt(data[j][columnIndex]) < pivot) {
                i++;
                swap(data, i, j);
            }
        }
        swap(data, i + 1, high);

        return i + 1;
    }

    private void swap(String[][] data, int i, int j) {
        String[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    private int medianOfThree(int a, int b, int c) {
        if ((a > b) == (a < c)) {
            return a;
        } else if ((b > a) == (b < c)) {
            return b;
        } else {
            return c;
        }
    }

    private int parseToInt(String value) {
        value = value.replace("\"", "").replace(",", "");
        if (value.isEmpty() || !isNumeric(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
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
