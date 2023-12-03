package Ordenacao.MergeSort;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A classe {@code MergeSortVenue} realiza a ordenação de dados em arquivos
 * CSV usando o algoritmo de ordenação Merge Sort, otimizado para usar ArrayLists e Collections.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e
 * pior cenário) e medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class MergeSortVenue {
    private String inputFile;
    private String path = "src/OrdenacaoResultados/MergeSort/";
    private String outputMedio = path + "matches_t2_venues_mergeSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_venues_mergeSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_venues_mergeSort_piorCaso.csv";
    private int venueIndex = 7;

    /**
     * Construtor da classe MergeSortVenue.
     *
     * @param inputFile O caminho do arquivo de entrada contendo os dados a serem ordenados.
     */
    public MergeSortVenue(String inputFile) {
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

        System.out.println("Ordenando utilizando o algoritmo Merge Sort...");

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
     * Cria o caso de cenário melhor ordenando os dados com o algoritmo Merge Sort e escreve os resultados no arquivo
     * de saída correspondente.
     */
    private void criarCasoMelhor() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        mergeSort(data, venueIndex, 0, data.size() - 1);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o caso de cenário pior ordenando os dados com o algoritmo Merge Sort em ordem decrescente e escreve os
     * resultados no arquivo de saída correspondente.
     */
    private void criarCasoPior() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        mergeSort(data, venueIndex, 0, data.size() - 1);
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
     * Ordena os dados e imprime o tempo de execução usando o algoritmo Merge Sort.
     *
     * @param fileToOrder O caminho do arquivo a ser ordenado e medido.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        List<String[]> data = carregarArquivoEmLista(fileToOrder);

        long startTime = System.currentTimeMillis();
        mergeSort(data, venueIndex, 0, data.size() - 1);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria();
    }

    /**
     * Realiza a ordenação de um subconjunto de dados usando o algoritmo Merge Sort.
     *
     * @param data       A lista de arrays de strings contendo os dados a serem ordenados.
     * @param venueIndex O índice da coluna de locais (venue) nos dados.
     * @param left       O índice de início do subconjunto.
     * @param right      O índice de fim do subconjunto.
     */
    private void mergeSort(List<String[]> data, int venueIndex, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(data, venueIndex, left, mid);
            mergeSort(data, venueIndex, mid + 1, right);
            merge(data, venueIndex, left, mid, right);
        }
    }

    /**
     * Realiza a fusão (merge) de dois subconjuntos ordenados em um único subconjunto ordenado.
     *
     * @param data       A lista de arrays de strings contendo os dados a serem mesclados.
     * @param venueIndex O índice da coluna de locais (venue) nos dados.
     * @param left       O índice de início do primeiro subconjunto.
     * @param mid        O índice de meio que divide os dois subconjuntos.
     * @param right      O índice de fim do segundo subconjunto.
     */
    private void merge(List<String[]> data, int venueIndex, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        List<String[]> leftList = new ArrayList<>(data.subList(left, mid + 1));
        List<String[]> rightList = new ArrayList<>(data.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (compareStrings(leftList.get(i)[venueIndex], rightList.get(j)[venueIndex]) <= 0) {
                data.set(k++, leftList.get(i++));
            } else {
                data.set(k++, rightList.get(j++));
            }
        }

        while (i < n1) {
            data.set(k++, leftList.get(i++));
        }

        while (j < n2) {
            data.set(k++, rightList.get(j++));
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

    private void imprimirConsumoMemoria() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();
        long usedMemory = heapMemoryUsage.getUsed();
        System.out.println("Consumo de memória: " + usedMemory + " bytes");
    }
}
