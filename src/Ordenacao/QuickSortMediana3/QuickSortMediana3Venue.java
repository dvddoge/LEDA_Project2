package Ordenacao.QuickSortMediana3;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A classe {@code QuickSortVenue} realiza a ordenação de dados em arquivos
 * CSV usando o algoritmo de ordenação QuickSort com a escolha da mediana de 3.
 * Ela é otimizada para usar ArrayLists e Collections e oferece três cenários
 * de ordenação (melhor, médio e pior caso), medindo o tempo de execução para cada cenário.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class QuickSortMediana3Venue {
    private String inputFile;
    private String path = "src/OrdenacaoResultados/QuickSortMediana3/";
    private String outputMedio = path + "matches_t2_venues_quickSortMediana3_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_venues_quickSortMediana3_melhorCaso.csv";
    private String outputPior = path + "matches_t2_venues_quickSortMediana3_piorCaso.csv";
    private int venueIndex = 7;

    /**
     * Construtor da classe QuickSortVenue.
     *
     * @param inputFile O caminho do arquivo de entrada contendo os dados a serem ordenados.
     */
    public QuickSortMediana3Venue(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Método principal que executa a ordenação e gera os resultados para os casos de melhor, médio e pior cenário,
     * além de medir e imprimir o tempo de execução para cada caso.
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
     * Cria o caso de cenário médio copiando o arquivo de entrada para o arquivo de saída correspondente.
     */
    private void criarCasoMedio() {
        copiarArquivo(inputFile, outputMedio);
    }

    /**
     * Cria o caso de cenário melhor ordenando os dados com o algoritmo QuickSort com mediana de 3 e escreve os resultados
     * no arquivo de saída correspondente.
     */
    private void criarCasoMelhor() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        quickSort(data, venueIndex, 0, data.size() - 1);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o caso de cenário pior ordenando os dados com o algoritmo QuickSort com mediana de 3 em ordem decrescente e
     * escreve os resultados no arquivo de saída correspondente.
     */
    private void criarCasoPior() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        quickSort(data, venueIndex, 0, data.size() - 1);
        Collections.reverse(data);
        escreverDados(data, outputPior);
    }

    /**
     * Copia um arquivo de origem para um arquivo de destino.
     *
     * @param origem  O caminho do arquivo de origem.
     * @param destino O caminho do arquivo de destino.
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
     * @param file O caminho do arquivo CSV a ser carregado.
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
     * @param outputFile O caminho do arquivo CSV de saída.
     */
    private void escreverDados(List<String[]> data, String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            // Escreva o cabeçalho
            writer.write("id,home,away,date,year,time (utc),attendance,venue,league,home_score,away_score,home_goal_scorers,away_goal_scorers,full_date");
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
     * Ordena os dados e imprime o tempo de execução usando o algoritmo QuickSort com mediana de 3.
     *
     * @param fileToOrder O caminho do arquivo a ser ordenado e medido.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        List<String[]> data = carregarArquivoEmLista(fileToOrder);

        long startTime = System.currentTimeMillis();
        quickSort(data, venueIndex, 0, data.size() - 1);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria();
    }

    /**
     * Realiza a ordenação de um subconjunto de dados usando o algoritmo QuickSort com mediana de 3.
     *
     * @param data       A lista de arrays de strings contendo os dados a serem ordenados.
     * @param venueIndex O índice da coluna de locais (venue) nos dados.
     * @param left       O índice de início do subconjunto.
     * @param right      O índice de fim do subconjunto.
     */
    private void quickSort(List<String[]> data, int venueIndex, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(data, venueIndex, left, right);
            quickSort(data, venueIndex, left, pivotIndex - 1);
            quickSort(data, venueIndex, pivotIndex + 1, right);
        }
    }

    /**
     * Realiza a partição dos dados para o algoritmo QuickSort com mediana de 3.
     *
     * @param data       A lista de arrays de strings a serem particionados.
     * @param venueIndex O índice da coluna de locais (venue) nos dados.
     * @param left       O índice inicial da subparte a ser particionada.
     * @param right      O índice final da subparte a ser particionada.
     * @return O índice do pivô após a partição.
     */
    private int partition(List<String[]> data, int venueIndex, int left, int right) {
        int mid = left + (right - left) / 2;
        String leftVenue = data.get(left)[venueIndex].replace("\"", "").trim();
        String midVenue = data.get(mid)[venueIndex].replace("\"", "").trim();
        String rightVenue = data.get(right)[venueIndex].replace("\"", "").trim();

        String pivotVenue;

        if (compareStrings(leftVenue, midVenue) <= 0 && compareStrings(midVenue, rightVenue) <= 0) {
            pivotVenue = midVenue;
            swap(data, mid, right);
        } else if (compareStrings(midVenue, leftVenue) <= 0 && compareStrings(leftVenue, rightVenue) <= 0) {
            pivotVenue = leftVenue;
        } else {
            pivotVenue = rightVenue;
            swap(data, left, right);
        }

        int i = left - 1;

        for (int j = left; j <= right - 1; j++) {
            String currentVenue = data.get(j)[venueIndex].replace("\"", "").trim();
            if (compareStrings(currentVenue, pivotVenue) <= 0) {
                i++;
                swap(data, i, j);
            }
        }

        swap(data, i + 1, right);
        return i + 1;
    }

    /**
     * Compara duas strings de acordo com a ordem lexicográfica, ignorando maiúsculas/minúsculas e espaços.
     *
     * @param str1 A primeira string a ser comparada.
     * @param str2 A segunda string a ser comparada.
     * @return Um valor negativo se str1 < str2, um valor positivo se str1 > str2, ou zero se str1 == str2.
     */
    private int compareStrings(String str1, String str2) {
        str1 = str1.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        str2 = str2.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        return str1.compareTo(str2);
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
