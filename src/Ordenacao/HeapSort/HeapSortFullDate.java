package Ordenacao.HeapSort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class HeapSortFullDate {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/HeapSort/";
    private String outputMedio = path + "matches_t2_full_date_heapSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_full_date_heapSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_full_date_heapSort_piorCaso.csv";
    private int fullDateIndex = 13;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Construtor que inicializa a classe com o arquivo de entrada fornecido.
     * 
     * @param inputFile O arquivo de entrada contendo os dados a serem ordenados.
     */
    public HeapSortFullDate(String inputFile) {
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

        System.out.println("Ordenando utilizando o algoritmo Heap Sort...");

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
     * Cria o caso de ordenação melhor ordenando o arquivo de entrada usando o
     * algoritmo Heap Sort.
     */
    private void criarCasoMelhor() {
        List<String[]> data = carregarArquivoEmArrayList(inputFile);
        heapSort(data, fullDateIndex);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o caso de ordenação pior ordenando o arquivo de entrada usando o
     * algoritmo Heap Sort e depois invertendo a ordem dos dados.
     */
    private void criarCasoPior() {
        List<String[]> data = carregarArquivoEmArrayList(inputFile);
        heapSort(data, fullDateIndex);
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
     * Carrega os dados de um arquivo CSV em um ArrayList de arrays de strings.
     * 
     * @param file O arquivo CSV a ser carregado.
     * @return Um ArrayList contendo os dados do arquivo, onde cada elemento é um
     *         array de strings representando uma linha do arquivo.
     */
    private List<String[]> carregarArquivoEmArrayList(String file) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Pular o cabeçalho
                }
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
     * @param data       A lista de arrays de strings contendo os dados.
     * @param outputFile O arquivo onde os dados serão escritos.
     */
    private void escreverDados(List<String[]> data, String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            // Escreva o cabeçalho
            writer.write(
                    "id,home,away,date,year,time (utc),attendance,venue,league,home_score,away_score,home_goal_scorers,away_goal_scorers,full_date");
            writer.newLine();

            // Escreva os dados
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ordena os dados na lista usando o algoritmo Heap Sort.
     *
     * @param data        A lista de arrays de strings a ser ordenada.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     */
    private void heapSort(List<String[]> data, int columnIndex) {
        int n = data.size();

        // Construir heap máximo
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(data, n, i, columnIndex);
        }

        // Extrair elementos um por um do heap
        for (int i = n - 1; i > 0; i--) {
            // Mover a raiz atual para o fim da lista
            Collections.swap(data, 0, i);

            // Chamar heapify no heap reduzido
            heapify(data, i, 0, columnIndex);
        }
    }

    /**
     * Função para construir o heap máximo.
     *
     * @param data        A lista de arrays de strings a ser ordenada.
     * @param n           O tamanho do heap.
     * @param i           O índice da raiz do heap.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     */
    private void heapify(List<String[]> data, int n, int i, int columnIndex) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && compareDates(data.get(left)[columnIndex], data.get(largest)[columnIndex]) > 0) {
            largest = left;
        }

        if (right < n && compareDates(data.get(right)[columnIndex], data.get(largest)[columnIndex]) > 0) {
            largest = right;
        }

        if (largest != i) {
            Collections.swap(data, i, largest);

            heapify(data, n, largest, columnIndex);
        }
    }

    /**
     * Realiza a ordenação e mede o tempo de execução usando o algoritmo Heap Sort.
     * O tempo de execução é impresso no console.
     *
     * @param fileToOrder O caminho do arquivo a ser ordenado e medido.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        List<String[]> data = carregarArquivoEmArrayList(fileToOrder);

        long startTime = System.currentTimeMillis();
        heapSort(data, fullDateIndex);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria(); // Imprimir consumo de memória após a ordenação
    }

    /**
     * Compara duas datas no formato "dd/MM/yyyy".
     *
     * @param date1 A primeira data a ser comparada.
     * @param date2 A segunda data a ser comparada.
     * @return Um valor negativo se date1 < date2, um valor positivo se date1 > date2,
     *         ou zero se date1 == date2.
     */
    private int compareDates(String date1, String date2) {
        try {
            Date d1 = sdf.parse(date1.replace("\"", "").trim());
            Date d2 = sdf.parse(date2.replace("\"", "").trim());
            return d1.compareTo(d2);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
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
