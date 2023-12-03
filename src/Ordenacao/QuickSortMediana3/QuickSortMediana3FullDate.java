package Ordenacao.QuickSortMediana3;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A classe {@code QuickSortMediana3FullDate} realiza a ordenação de dados em arquivos
 * CSV usando o algoritmo de ordenação QuickSort com mediana de 3. Ela oferece
 * três cenários de ordenação (melhor, médio e pior caso) e mede o tempo de
 * execução para cada cenário.
 */
public class QuickSortMediana3FullDate {
    private String inputFile;
    private String path = "src/OrdenacaoResultados/QuickSortMediana3/";
    private String outputMedio = path + "matches_t2_full_date_quickSortMediana3_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_full_date_quickSortMediana3_melhorCaso.csv";
    private String outputPior = path + "matches_t2_full_date_quickSortMediana3_piorCaso.csv";
    private int fullDateIndex = 13;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Construtor que inicializa a classe com o arquivo de entrada fornecido.
     *
     * @param inputFile O arquivo de entrada contendo os dados a serem ordenados.
     */
    public QuickSortMediana3FullDate(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Realiza a ordenação dos dados nos cenários de melhor, médio e pior caso e imprime os tempos de execução.
     */
    public void ordenar() {
        criarCasoMelhor();
        criarCasoMedio();
        criarCasoPior();

        System.out.println("Ordenando utilizando o algoritmo QuickSort com mediana de 3...");

        ordenarEImprimirTempo(outputMelhor);
        ordenarEImprimirTempo(outputMedio);
        ordenarEImprimirTempo(outputPior);
        System.out.println("\nOrdenação concluída com sucesso!");
    }

    /**
     * Cria o cenário de ordenação médio copiando o conteúdo do arquivo de entrada para o arquivo de saída.
     */
    private void criarCasoMedio() {
        copiarArquivo(inputFile, outputMedio);
    }

    /**
     * Cria o cenário de ordenação de melhor caso ordenando o arquivo de entrada usando o algoritmo QuickSort com mediana de 3.
     */
    private void criarCasoMelhor() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        quickSort(data, fullDateIndex, 0, data.size() - 1);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o cenário de ordenação de pior caso ordenando o arquivo de entrada usando o algoritmo QuickSort com mediana de 3
     * e depois invertendo a ordem dos dados.
     */
    private void criarCasoPior() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        quickSort(data, fullDateIndex, 0, data.size() - 1);
        Collections.reverse(data);
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

    /**
     * Carrega os dados de um arquivo CSV em uma lista de arrays de strings.
     *
     * @param file O arquivo CSV a ser carregado.
     * @return Uma lista de arrays de strings contendo os dados do arquivo.
     */
    private List<String[]> carregarArquivoEmLista(String file) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // Ignorar o cabeçalho
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Escreve os dados de uma lista de arrays de strings em um arquivo CSV.
     *
     * @param data       A lista de arrays de strings contendo os dados a serem escritos.
     * @param outputFile O arquivo onde os dados serão escritos.
     */
    private void escreverDados(List<String[]> data, String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            // Escreva o cabeçalho
            writer.write(
                    "id,home,away,date,year,time (utc),attendance,venue,league,home_score,away_score,home_goal_scorers,away_goal_scorers,full_date");
            writer.newLine();

            // Escreva os dados
            for (String[] values : data) {
                writer.write(String.join(",", values));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ordena os dados no arquivo especificado usando o algoritmo QuickSort com mediana de 3 e imprime o tempo de execução.
     *
     * @param fileToOrder O arquivo a ser ordenado.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        List<String[]> data = carregarArquivoEmLista(fileToOrder);

        long startTime = System.currentTimeMillis();
        quickSort(data, fullDateIndex, 0, data.size() - 1);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria();
    }

    /**
     * Utiliza o algoritmo QuickSort com escolha da mediana de 3 para ordenar uma subparte da lista de arrays de strings
     * com base no índice da coluna fornecida.
     *
     * @param data       A lista de arrays de strings a ser ordenada.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     * @param left       O índice inicial da subparte a ser ordenada.
     * @param right      O índice final da subparte a ser ordenada.
     */
    private void quickSort(List<String[]> data, int columnIndex, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(data, columnIndex, left, right);
            quickSort(data, columnIndex, left, pivotIndex - 1);
            quickSort(data, columnIndex, pivotIndex + 1, right);
        }
    }

    /**
     * Realiza a partição dos dados para o algoritmo QuickSort com mediana de 3.
     *
     * @param data       A lista de arrays de strings a serem particionados.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     * @param left       O índice inicial da subparte a ser particionada.
     * @param right      O índice final da subparte a ser particionada.
     * @return O índice do pivô após a partição.
     */
    private int partition(List<String[]> data, int columnIndex, int left, int right) {
        int mid = left + (right - left) / 2;
        String leftDate = data.get(left)[columnIndex].replace("\"", "").trim();
        String midDate = data.get(mid)[columnIndex].replace("\"", "").trim();
        String rightDate = data.get(right)[columnIndex].replace("\"", "").trim();

        String pivotDate;

        if (compareDates(leftDate, midDate) <= 0 && compareDates(midDate, rightDate) <= 0) {
            pivotDate = midDate;
            swap(data, mid, right);
        } else if (compareDates(midDate, leftDate) <= 0 && compareDates(leftDate, rightDate) <= 0) {
            pivotDate = leftDate;
        } else {
            pivotDate = rightDate;
            swap(data, left, right);
        }

        int i = left - 1;

        for (int j = left; j <= right - 1; j++) {
            String currentDate = data.get(j)[columnIndex].replace("\"", "").trim();
            if (compareDates(currentDate, pivotDate) <= 0) {
                i++;
                swap(data, i, j);
            }
        }

        swap(data, i + 1, right);
        return i + 1;
    }

    /**
     * Compara duas datas no formato "dd/MM/yyyy".
     *
     * @param date1 A primeira data a ser comparada.
     * @param date2 A segunda data a ser comparada.
     * @return Um valor negativo se date1 for anterior a date2, zero se forem iguais
     *         ou um valor positivo se date1 for posterior a date2.
     */
    private int compareDates(String date1, String date2) {
        try {
            Date d1 = sdf.parse(date1.replace("\"", ""));
            Date d2 = sdf.parse(date2.replace("\"", ""));
            return d1.compareTo(d2);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Realiza a troca de elementos em uma lista de arrays de strings.
     *
     * @param data A lista de arrays de strings onde a troca será realizada.
     * @param i    O índice do primeiro elemento a ser trocado.
     * @param j    O índice do segundo elemento a ser trocado.
     */
    private void swap(List<String[]> data, int i, int j) {
        Collections.swap(data, i, j);
    }

    /**
     * Imprime o consumo de memória atual.
     */
    private void imprimirConsumoMemoria() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();

        long usedMemory = heapMemoryUsage.getUsed();

        System.out.println("Consumo de memória: " + usedMemory + " bytes");
    }
}
