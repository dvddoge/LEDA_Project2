package Ordenacao.SelectionSort;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A classe {@code SelectionSortAttendance} realiza a ordenação de dados em
 * arquivos CSV usando o algoritmo de ordenação Selection Sort para ordenação com
 * base na coluna "attendance".
 * Ela é projetada para criar três casos de ordenação (melhor, médio e pior) e
 * medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class SelectionSortAttendance {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/SelectionSort/";
    private String outputMedio = path + "matches_t2_attendance_selectionSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_attendance_selectionSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_attendance_selectionSort_piorCaso.csv";

    /**
     * Cria uma nova instância de {@code SelectionSortAttendance} com o arquivo de
     * entrada especificado.
     *
     * @param inputFile O arquivo de entrada a ser ordenado.
     */
    public SelectionSortAttendance(String inputFile) {
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

        System.out.println("Ordenando utilizando o algoritmo Selection Sort...");

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
     * crescente usando o algoritmo Selection Sort.
     */
    private void criarCasoMelhor() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        selectionSort(data, 6); // 6 é o índice da coluna "attendance"
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o caso de ordenação pior ordenando o arquivo de entrada de forma
     * decrescente usando o algoritmo Selection Sort.
     */
    private void criarCasoPior() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        selectionSort(data, 6); // 6 é o índice da coluna "attendance"
        Collections.reverse(data); // Inverte a lista
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
     * Carrega os dados de um arquivo CSV em uma lista de arrays de strings.
     *
     * @param file O arquivo CSV a ser carregado.
     * @return Uma lista de arrays de strings contendo os dados do arquivo.
     */
    private List<String[]> carregarArquivoEmLista(String file) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
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
     * @param outputFile O arquivo CSV de saída.
     */
    private void escreverDados(List<String[]> data, String outputFile) {
        try (FileWriter writer = new FileWriter(outputFile)) {
            if (outputFile != outputMelhor)
                writer.write(
                        "id,home,away,date,year,time (utc),attendance,venue,league,home_score,away_score,home_goal_scorers,away_goal_scorers,full_date\n");

            // Escreva os dados
            for (String[] values : data) {
                writer.write(String.join(",", values) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza a ordenação usando o algoritmo Selection Sort.
     *
     * @param data       A lista de arrays de strings contendo os dados a serem ordenados.
     * @param columnIndex O índice da coluna pela qual a ordenação será feita.
     */
    private void selectionSort(List<String[]> data, int columnIndex) {
        int n = data.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                int currentValue = parseToInt(data.get(j)[columnIndex]);
                int minValue = parseToInt(data.get(minIndex)[columnIndex]);

                if (currentValue < minValue) {
                    minIndex = j;
                }
            }

            // Trocar os elementos
            Collections.swap(data, i, minIndex);
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
     * numérica.
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
     * {@code false}.
     */
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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

    /**
     * Realiza a ordenação e mede o tempo de execução usando o algoritmo Selection Sort.
     * O tempo de execução é impresso no console.
     *
     * @param fileToOrder O arquivo a ser ordenado.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        List<String[]> data = carregarArquivoEmLista(fileToOrder);

        long startTime = System.currentTimeMillis();
        selectionSort(data, 6); // 6 é o índice da coluna "attendance"
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria(); // Imprimir consumo de memória após a ordenação
    }
}
