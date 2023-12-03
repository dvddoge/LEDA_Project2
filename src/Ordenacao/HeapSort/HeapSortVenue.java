package Ordenacao.HeapSort;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A classe {@code HeapSortVenue} realiza a ordenação de dados em arquivos
 * CSV usando o algoritmo de ordenação Heap Sort, otimizado para usar ArrayLists e Collections.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e
 * pior cenário) e medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class HeapSortVenue {
    private String inputFile;
    private String path = "src/OrdenacaoResultados/HeapSort/";
    private String outputMedio = path + "matches_t2_venues_heapSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_venues_heapSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_venues_heapSort_piorCaso.csv";
    private int venueIndex = 7;

    /**
     * Construtor da classe HeapSortVenue.
     *
     * @param inputFile O caminho do arquivo de entrada contendo os dados a serem ordenados.
     */
    public HeapSortVenue(String inputFile) {
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

        System.out.println("Ordenando utilizando o algoritmo Heap Sort...");

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
     * Cria o caso de cenário melhor ordenando os dados com o algoritmo Heap Sort e escreve os resultados no arquivo
     * de saída correspondente.
     */
    private void criarCasoMelhor() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        heapSort(data, venueIndex);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o caso de cenário pior ordenando os dados com o algoritmo Heap Sort em ordem decrescente e escreve os
     * resultados no arquivo de saída correspondente.
     */
    private void criarCasoPior() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        heapSort(data, venueIndex);
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
     * Ordena os dados e imprime o tempo de execução usando o algoritmo Heap Sort.
     *
     * @param fileToOrder O caminho do arquivo a ser ordenado e medido.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        List<String[]> data = carregarArquivoEmLista(fileToOrder);

        long startTime = System.currentTimeMillis();
        heapSort(data, venueIndex);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria();
    }

    /**
     * Realiza a ordenação dos dados usando o algoritmo Heap Sort.
     *
     * @param data       A lista de arrays de strings contendo os dados a serem ordenados.
     * @param venueIndex O índice da coluna de locais (venue) nos dados.
     */
    private void heapSort(List<String[]> data, int venueIndex) {
        int n = data.size();

        // Construir um heap máximo
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(data, n, i, venueIndex);
        }

        // Extrair elementos do heap um por um
        for (int i = n - 1; i > 0; i--) {
            // Mover o elemento atual para o final do array
            Collections.swap(data, 0, i);

            // Chamar heapify na subárvore reduzida
            heapify(data, i, 0, venueIndex);
        }
    }

    /**
     * Função para construir e manter um heap máximo.
     *
     * @param data       A lista de arrays de strings contendo os dados a serem ordenados.
     * @param n          O tamanho do heap.
     * @param i          O índice do elemento raiz do heap.
     * @param venueIndex O índice da coluna de locais (venue) nos dados.
     */
    private void heapify(List<String[]> data, int n, int i, int venueIndex) {
        int maior = i; // Inicializar o maior como raiz
        int left = 2 * i + 1; // Índice do filho esquerdo
        int right = 2 * i + 2; // Índice do filho direito

        // Se o filho esquerdo for maior que a raiz
        if (left < n && compareStrings(data.get(left)[venueIndex], data.get(maior)[venueIndex]) > 0) {
            maior = left;
        }

        // Se o filho direito for maior que o maior até agora
        if (right < n && compareStrings(data.get(right)[venueIndex], data.get(maior)[venueIndex]) > 0) {
            maior = right;
        }

        // Se o maior não for a raiz
        if (maior != i) {
            // Trocar o elemento raiz com o maior
            Collections.swap(data, i, maior);

            // Chamar recursivamente a função heapify na subárvore afetada
            heapify(data, n, maior, venueIndex);
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
