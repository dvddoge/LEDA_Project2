package Ordenacao.CountingSort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A classe {@code CountingSortAttendance} realiza a ordenação de dados em
 * arquivos CSV usando o algoritmo de ordenação Counting Sort para ordenar com
 * base na coluna "attendance". Ela é projetada para criar três casos de
 * ordenação (melhor, médio e pior) e medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class CountingSortAttendance {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/CountingSort/";
    private String outputMedio = path + "matches_t2_attendance_countingSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_attendance_countingSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_attendance_countingSort_piorCaso.csv";

    /**
     * Cria uma nova instância de {@code CountingSortAttendance} com o arquivo de
     * entrada especificado.
     *
     * @param inputFile O arquivo de entrada a ser ordenado.
     */
    public CountingSortAttendance(String inputFile) {
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
     * Cria o caso de ordenação melhor ordenando o arquivo de entrada de forma
     * crescente usando o algoritmo Counting Sort.
     */
    private void criarCasoMelhor() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        countingSort(data, 6); // 6 é o índice da coluna "attendance"
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o caso de ordenação pior ordenando o arquivo de entrada de forma
     * decrescente usando o algoritmo Counting Sort.
     */
    private void criarCasoPior() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        countingSort(data, 6); // 6 é o índice da coluna "attendance"
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
            // Escreva o cabeçalho apenas uma vez
            if (!outputFile.equals(outputMelhor)) {
                writer.write(
                        "id,home,away,date,year,time (utc),attendance,venue,league,home_score,away_score,home_goal_scorers,away_goal_scorers,full_date\n");
            }

            // Escreva os dados
            for (String[] values : data) {
                writer.write(String.join(",", values) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza a ordenação usando o algoritmo Counting Sort.
     *
     * @param data       A lista de arrays de strings contendo os dados a serem ordenados.
     * @param columnIndex O índice da coluna pela qual a ordenação será feita.
     */
    private void countingSort(List<String[]> data, int columnIndex) {
        int n = data.size();

        int maxAttendance = Integer.MIN_VALUE;
        int minAttendance = Integer.MAX_VALUE;

        List<String[]> sortedData = new ArrayList<>(Collections.nCopies(n, null));

        for (int i = 0; i < n; i++) {
            int attendance = parseToInt(data.get(i)[columnIndex]);
            if (attendance > maxAttendance) {
                maxAttendance = attendance;
            }
            if (attendance < minAttendance) {
                minAttendance = attendance;
            }
        }

        int[] count = new int[maxAttendance - minAttendance + 1];

        for (int i = 0; i < n; i++) {
            int attendance = parseToInt(data.get(i)[columnIndex]);
            count[attendance - minAttendance]++;
        }

        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        for (int i = n - 1; i >= 0; i--) {
            int attendance = parseToInt(data.get(i)[columnIndex]);
            int sortedIndex = count[attendance - minAttendance]--;
    
            if (sortedIndex - 1 < n) {
                sortedData.set(sortedIndex - 1, data.get(i));
            } else {
                System.err.println("Índice fora dos limites: " + (sortedIndex - 1));
            }
        }
    
        // Atualizar a lista original com os elementos ordenados
        for (int i = 0; i < n; i++) {
            if (sortedData.get(i) != null) {
                data.set(i, sortedData.get(i));
            } else {
                System.err.println("Dados nulos encontrados na posição: " + i);
            }
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
     * Realiza a ordenação e mede o tempo de execução usando o algoritmo Counting Sort.
     * O tempo de execução é impresso no console.
     *
     * @param fileToOrder O arquivo a ser ordenado.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        List<String[]> data = carregarArquivoEmLista(fileToOrder);

        long startTime = System.currentTimeMillis();
        countingSort(data, 6); // 6 é o índice da coluna "attendance"
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria(); // Imprimir consumo de memória após a ordenação
    }
}
