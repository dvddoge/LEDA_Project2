package Ordenacao.CountingSort;

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
 * Classe para ordenação de dados usando o algoritmo Counting Sort.
 */
public class CountingSortFullDate {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/CountingSort/";
    private String outputMedio = path + "matches_t2_full_date_countingSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_full_date_countingSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_full_date_countingSort_piorCaso.csv";
    private int fullDateIndex = 13;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Construtor que inicializa a classe com o arquivo de entrada fornecido.
     *
     * @param inputFile O arquivo de entrada contendo os dados a serem ordenados.
     */
    public CountingSortFullDate(String inputFile) {
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

        System.out.println("Ordenando utilizando o algoritmo Counting Sort...");

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
     * algoritmo Counting Sort.
     */
    private void criarCasoMelhor() {
        List<String[]> data = carregarArquivoEmArrayList(inputFile);
        countingSort(data, fullDateIndex);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o caso de ordenação pior ordenando o arquivo de entrada usando o
     * algoritmo Counting Sort e depois invertendo a ordem dos dados.
     */
    private void criarCasoPior() {
        List<String[]> data = carregarArquivoEmArrayList(inputFile);
        countingSort(data, fullDateIndex);
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
     * Ordena os dados na lista usando o algoritmo Counting Sort.
     *
     * @param data        A lista de arrays de strings a ser ordenada.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     */
    private void countingSort(List<String[]> data, int columnIndex) {
        int n = data.size();
    
        // Encontra o valor máximo e mínimo convertido em dias
        int maxValue = Integer.MIN_VALUE;
        int minValue = Integer.MAX_VALUE;
    
        for (String[] row : data) {
            int value = dateToInt(row[columnIndex]);
            if (value > maxValue) {
                maxValue = value;
            }
            if (value < minValue) {
                minValue = value;
            }
        }
    
        int range = maxValue - minValue + 1;
        int[] count = new int[range];
    
        // Contagem das frequências
        for (String[] row : data) {
            int value = dateToInt(row[columnIndex]) - minValue;
            count[value]++;
        }
    
        // Acumulando as contagens
        for (int i = 1; i < range; i++) {
            count[i] += count[i - 1];
        }
    
        String[][] sortedData = new String[n][];
    
        // Ordenação
        for (int i = n - 1; i >= 0; i--) {
            int value = dateToInt(data.get(i)[columnIndex]) - minValue;
            sortedData[count[value] - 1] = data.get(i);
            count[value]--;
        }
    
        // Copiando os dados ordenados de volta para a lista original
        for (int i = 0; i < n; i++) {
            data.set(i, sortedData[i]);
        }
    }

    private int dateToInt(String dateStr) {
        try {
            Date date = sdf.parse(dateStr.replace("\"", "").trim());
            Date baseDate = sdf.parse("01/01/2000");
            long diffInMillies = date.getTime() - baseDate.getTime();
            return (int) (diffInMillies / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Realiza a ordenação e mede o tempo de execução usando o algoritmo Counting Sort.
     * O tempo de execução é impresso no console.
     *
     * @param fileToOrder O caminho do arquivo a ser ordenado e medido.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        List<String[]> data = carregarArquivoEmArrayList(fileToOrder);

        long startTime = System.currentTimeMillis();
        countingSort(data, fullDateIndex);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria(); // Imprimir consumo de memória após a ordenação
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
