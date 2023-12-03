package Ordenacao.CountingSort;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A classe {@code CountingSortVenue} realiza a ordenação de dados em arquivos
 * CSV usando o algoritmo de ordenação Counting Sort, otimizado para usar ArrayLists e Collections.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e
 * pior cenário) e medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class CountingSortVenue {
    private String inputFile;
    private String path = "src/OrdenacaoResultados/CountingSort/";
    private String outputMedio = path + "matches_t2_venues_countingSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_venues_countingSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_venues_countingSort_piorCaso.csv";
    private int venueIndex = 7;

    /**
     * Construtor da classe CountingSortVenue.
     *
     * @param inputFile O caminho do arquivo de entrada contendo os dados a serem ordenados.
     */
    public CountingSortVenue(String inputFile) {
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

        System.out.println("Ordenando utilizando o algoritmo Counting Sort...");

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
     * Cria o caso de cenário melhor ordenando os dados com o algoritmo Counting Sort e escreve os resultados no arquivo
     * de saída correspondente.
     */
    private void criarCasoMelhor() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        countingSort(data, venueIndex);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o caso de cenário pior ordenando os dados com o algoritmo Counting Sort em ordem decrescente e escreve os
     * resultados no arquivo de saída correspondente.
     */
    private void criarCasoPior() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        countingSort(data, venueIndex);
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
     * Ordena os dados e imprime o tempo de execução usando o algoritmo Counting Sort.
     *
     * @param fileToOrder O caminho do arquivo a ser ordenado e medido.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        List<String[]> data = carregarArquivoEmLista(fileToOrder);

        long startTime = System.currentTimeMillis();
        countingSort(data, venueIndex);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria();
    }

    /**
     * Remove caracteres não alfanuméricos e converte para minúsculas.
     *
     * @param str A string a ser normalizada.
     * @return A string normalizada.
     */
    private String normalizeString(String str) {
        return str.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }

    /**
     * Cria uma lista ordenada e única de strings a partir dos dados.
     *
     * @param data       A lista de arrays de strings contendo os dados.
     * @param columnIndex O índice da coluna a ser considerada.
     * @return Uma lista ordenada e única de strings.
     */
    private List<String> createSortedUniqueStringList(List<String[]> data, int columnIndex) {
        List<String> allStrings = new ArrayList<>();
        for (String[] row : data) {
            String normalizedString = normalizeString(row[columnIndex]);
            if (!allStrings.contains(normalizedString)) {
                allStrings.add(normalizedString);
            }
        }
        Collections.sort(allStrings);
        return allStrings;
    }

    /**
     * Realiza a ordenação dos dados usando o algoritmo Counting Sort.
     *
     * @param data       A lista de arrays de strings contendo os dados a serem ordenados.
     * @param columnIndex O índice da coluna a ser considerada.
     */
    private void countingSort(List<String[]> data, int columnIndex) {
        List<String> sortedUniqueStrings = createSortedUniqueStringList(data, columnIndex);

        // Preenche o array de contagem
        int[] count = new int[sortedUniqueStrings.size()];
        for (String[] row : data) {
            String normalizedString = normalizeString(row[columnIndex]);
            int index = sortedUniqueStrings.indexOf(normalizedString);
            count[index]++;
        }

        // Atualiza o array de contagem
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        // Ordena os dados
        List<String[]> sortedData = new ArrayList<>(Collections.nCopies(data.size(), null));
        for (int i = data.size() - 1; i >= 0; i--) {
            String normalizedString = normalizeString(data.get(i)[columnIndex]);
            int index = sortedUniqueStrings.indexOf(normalizedString);
            sortedData.set(--count[index], data.get(i));
        }

        data.clear();
        data.addAll(sortedData);
    }

    /**
     * Imprime o consumo de memória.
     */
    private void imprimirConsumoMemoria() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();
        long usedMemory = heapMemoryUsage.getUsed();
        System.out.println("Consumo de memória: " + usedMemory + " bytes");
    }
}
