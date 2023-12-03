package Ordenacao.InsertionSort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * Classe que realiza a ordenação de dados em arquivos CSV usando o algoritmo de
 * ordenação Insertion Sort para ordenação com base na coluna "full_date".
 */
public class InsertionSortFullDate {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/InsertionSort/";
    private String outputMedio = path + "matches_t2_full_date_insertionSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_full_date_insertionSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_full_date_insertionSort_piorCaso.csv";
    private int fullDateIndex = 13;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Construtor que inicializa a classe com o arquivo de entrada fornecido.
     *
     * @param inputFile O arquivo de entrada contendo os dados a serem ordenados.
     */
    public InsertionSortFullDate(String inputFile) {
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

        System.out.println("Ordenando utilizando o algoritmo Insertion Sort...");

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
     * algoritmo Insertion Sort.
     */
    private void criarCasoMelhor() {
        List<String[]> data = carregarArquivoEmArrayList(inputFile);
        insertionSort(data, fullDateIndex);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o caso de ordenação pior ordenando o arquivo de entrada usando o
     * algoritmo Insertion Sort e depois invertendo a ordem dos dados.
     */
    private void criarCasoPior() {
        List<String[]> data = carregarArquivoEmArrayList(inputFile);
        insertionSort(data, fullDateIndex);
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
     * Ordena os dados na lista usando o algoritmo Insertion Sort.
     *
     * @param data        A lista de arrays de strings a ser ordenada.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     */
    private void insertionSort(List<String[]> data, int columnIndex) {
        int n = data.size();
        for (int i = 1; i < n; ++i) {
            String[] key = data.get(i);
            int j = i - 1;

            while (j >= 0 && compareDates(data.get(j)[columnIndex], key[columnIndex]) > 0) {
                data.set(j + 1, data.get(j));
                j = j - 1;
            }
            data.set(j + 1, key);
        }
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

    /**
     * Realiza a ordenação e mede o tempo de execução usando o algoritmo Insertion
     * Sort. O tempo de execução é impresso no console.
     *
     * @param fileToOrder O caminho do arquivo a ser ordenado e medido.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        List<String[]> data = carregarArquivoEmArrayList(fileToOrder);

        long startTime = System.currentTimeMillis();
        insertionSort(data, fullDateIndex);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria(); // Imprimir consumo de memória após a ordenação
    }
}
