package Ordenacao.SelectionSort;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A classe {@code SelectionSortVenue} realiza a ordenação de dados em arquivos
 * CSV usando o algoritmo de ordenação Selection Sort, otimizado para usar ArrayLists e Collections.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e
 * pior cenário) e medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class SelectionSortVenue {
    private String inputFile;
    private String path = "src/OrdenacaoResultados/SelectionSort/";
    private String outputMedio = path + "matches_t2_venues_selectionSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_venues_selectionSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_venues_selectionSort_piorCaso.csv";
    private int venueIndex = 7;

    /**
     * Construtor da classe SelectionSortVenue.
     *
     * @param inputFile O caminho do arquivo de entrada contendo os dados a serem ordenados.
     */
    public SelectionSortVenue(String inputFile) {
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

        System.out.println("Ordenando utilizando o algoritmo Selection Sort...");

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
     * Cria o caso de cenário melhor ordenando os dados com o algoritmo Selection Sort e escreve os resultados no arquivo
     * de saída correspondente.
     */
    private void criarCasoMelhor() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        selectionSort(data, venueIndex);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o caso de cenário pior ordenando os dados com o algoritmo Selection Sort em ordem decrescente e escreve os
     * resultados no arquivo de saída correspondente.
     */
    private void criarCasoPior() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        selectionSort(data, venueIndex);
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
     * Ordena os dados e imprime o tempo de execução usando o algoritmo Selection Sort.
     *
     * @param fileToOrder O caminho do arquivo a ser ordenado e medido.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        List<String[]> data = carregarArquivoEmLista(fileToOrder);

        long startTime = System.currentTimeMillis();
        selectionSort(data, venueIndex);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria();
    }

    /**
     * Realiza a ordenação dos dados usando o algoritmo Selection Sort.
     *
     * @param data       A lista de arrays de strings contendo os dados a serem ordenados.
     * @param venueIndex O índice da coluna de locais (venue) nos dados.
     */
    private void selectionSort(List<String[]> data, int venueIndex) {
        int n = data.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (compareStrings(data.get(j)[venueIndex], data.get(minIndex)[venueIndex]) < 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                Collections.swap(data, i, minIndex);
            }
        }
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
     * Imprime o consumo de memória após a ordenação.
     */
    private void imprimirConsumoMemoria() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();
        long usedMemory = heapMemoryUsage.getUsed();
        System.out.println("Consumo de memória: " + usedMemory + " bytes");
    }
}
