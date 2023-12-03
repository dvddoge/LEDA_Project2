package Ordenacao.QuickSort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * A classe {@code QuickSortAttendance} realiza a ordenação de dados em arquivos
 * CSV usando o algoritmo de ordenação Quick Sort.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e pior) e
 * medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class QuickSortAttendance {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/QuickSort/";
    private String outputMedio = path + "matches_t2_attendance_quickSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_attendance_quickSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_attendance_quickSort_piorCaso.csv";
    private int attendanceIndex = 6;

    /**
     * Cria uma nova instância de {@code QuickSortAttendance} com o arquivo de
     * entrada especificado.
     *
     * @param inputFile O arquivo de entrada a ser ordenado.
     */
    public QuickSortAttendance(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Realiza a ordenação dos dados nos casos de melhor, médio e pior e imprime os
     * tempos de execução.
     */
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

    /**
     * Cria o caso de ordenação médio copiando o conteúdo do arquivo de entrada para
     * o arquivo de saída.
     */
    private void criarCasoMedio() {
        copiarArquivo(inputFile, outputMedio);
    }

    /**
     * Cria o caso de ordenação melhor ordenando o arquivo de entrada de forma
     * crescente usando o algoritmo QuickSort.
     */
    private void criarCasoMelhor() {
        String[][] data = carregarArquivoEmArray(inputFile);
        quickSort(data, attendanceIndex, 0, data.length - 1);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o caso de ordenação pior ordenando o arquivo de entrada de forma
     * decrescente usando o algoritmo QuickSort.
     */
    private void criarCasoPior() {
        String[][] data = carregarArquivoEmArray(inputFile);
        quickSort(data, attendanceIndex, 0, data.length - 1);
        inverterDados(data);
        escreverDados(data, outputPior);
    }

    /**
     * Copia um arquivo de origem para um arquivo de destino.
     *
     * @param origem  O arquivo de origem a ser copiado.
     * @param destino O arquivo de destino onde o conteúdo será copiado.
     */
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

    /**
     * Carrega os dados de um arquivo CSV em um array bidimensional.
     *
     * @param file O arquivo CSV a ser carregado.
     * @return Um array bidimensional contendo os dados do arquivo.
     */
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

    /**
     * Escreve os dados de um array bidimensional em um arquivo CSV.
     *
     * @param data       O array bidimensional contendo os dados a serem escritos.
     * @param outputFile O arquivo CSV de saída.
     */
    private void escreverDados(String[][] data, String outputFile) {
        try (FileWriter writer = new FileWriter(outputFile)) {
            // Escreva o cabeçalho
            writer.write(
                    "id,home,away,date,year,time (utc),attendance,venue,league,home_score,away_score,home_goal_scorers,away_goal_scorers,full_date\n");

            // Escreva os dados
            for (int i = 0; i < data.length; i++) {
                writer.write(String.join(",", data[i]) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inverte a ordem dos dados em um array bidimensional.
     *
     * @param data O array bidimensional a ser invertido.
     */
    private void inverterDados(String[][] data) {
        for (int i = 0; i < data.length / 2; i++) {
            String[] temp = data[i];
            data[i] = data[data.length - i - 1];
            data[data.length - i - 1] = temp;
        }
    }

    /**
     * Realiza a ordenação e mede o tempo de execução usando o algoritmo QuickSort.
     * O tempo de execução é impresso no console.
     *
     * @param fileToOrder O arquivo a ser ordenado.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        String[][] data = carregarArquivoEmArray(fileToOrder);

        long startTime = System.currentTimeMillis();
        quickSort(data, attendanceIndex, 0, data.length - 1);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria(); // Imprimir consumo de memória após a ordenação

    }

    /**
     * Realiza a ordenação de um subarray usando o algoritmo QuickSort.
     *
     * @param data       O array bidimensional contendo os dados a serem ordenados.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     * @param low        O índice de início do subarray.
     * @param high       O índice de fim do subarray.
     */
    private void quickSort(String[][] data, int columnIndex, int low, int high) {
        if (low < high) {
            int pi = partition(data, columnIndex, low, high);
            quickSort(data, columnIndex, low, pi - 1);
            quickSort(data, columnIndex, pi + 1, high);
        }
    }

    /**
     * Método de partição usado pelo algoritmo QuickSort.
     *
     * @param data       O array bidimensional contendo os dados a serem particionados.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     * @param low        O índice de início do subarray.
     * @param high       O índice de fim do subarray.
     * @return O índice de partição.
     */
    private int partition(String[][] data, int columnIndex, int low, int high) {
        // Escolha o pivô como a mediana entre low, middle e high
        int middle = (low + high) / 2;
        int pivotValue = medianOfThree(parseToInt(data[low][columnIndex]), 
                                       parseToInt(data[middle][columnIndex]), 
                                       parseToInt(data[high][columnIndex]));
    
        int pivotIndex = (pivotValue == parseToInt(data[low][columnIndex])) ? low : 
                         (pivotValue == parseToInt(data[middle][columnIndex])) ? middle : high;
    
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

    /**
     * Converte uma string em um número inteiro.
     * Se a string estiver vazia ou não for numérica, retorna 0.
     * O valor pode ter aspas duplas e vírgulas, que são removidas antes da
     * conversão.
     *
     * @param value A string a ser convertida em um número inteiro.
     * @return O valor inteiro correspondente à string ou 0 se a string não for
     *         numérica.
     */
    private int parseToInt(String value) {
        value = value.replace("\"", "").replace(",", "");
        if (value.isEmpty() || !isNumeric(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    /**
     * Verifica se uma string pode ser convertida em um número inteiro.
     * Esta função é usada para garantir que tentativas de conversão sejam seguras.
     *
     * @param str A string a ser verificada.
     * @return {@code true} se a string for um número inteiro válido, caso contrário
     *         {@code false}.
     */
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
}