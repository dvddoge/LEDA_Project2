package Ordenacao.MergeSort;

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
 * A classe {@code MergeSortAttendance} realiza a ordenação de dados em
 * arquivos CSV usando o algoritmo de ordenação Merge Sort e ArrayList.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e pior) e
 * medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class MergeSortAttendance {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/MergeSort/";
    private String outputMedio = path + "matches_t2_attendance_mergeSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_attendance_mergeSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_attendance_mergeSort_piorCaso.csv";

    /**
     * Cria uma nova instância de {@code MergeSortAttendance} com o arquivo de
     * entrada especificado.
     *
     * @param inputFile O arquivo de entrada a ser ordenado.
     */
    public MergeSortAttendance(String inputFile) {
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

        System.out.println("Ordenando utilizando o algoritmo Merge Sort...");

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
     * crescente usando o algoritmo Merge Sort.
     */
    private void criarCasoMelhor() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        mergeSort(data, 6, 0, data.size() - 1); // 6 é o índice da coluna "attendance"
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o caso de ordenação pior ordenando o arquivo de entrada de forma
     * decrescente usando o algoritmo Merge Sort.
     */
    private void criarCasoPior() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        mergeSort(data, 6, 0, data.size() - 1); // 6 é o índice da coluna "attendance"
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
     * Realiza a ordenação usando o algoritmo Merge Sort.
     *
     * @param data      A lista de arrays de strings contendo os dados a serem ordenados.
     * @param columnIndex O índice da coluna pela qual a ordenação será feita.
     * @param left      O índice do elemento mais à esquerda.
     * @param right     O índice do elemento mais à direita.
     */
    private void mergeSort(List<String[]> data, int columnIndex, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;

            // Ordena a metade esquerda
            mergeSort(data, columnIndex, left, middle);

            // Ordena a metade direita
            mergeSort(data, columnIndex, middle + 1, right);

            // Combina as duas metades ordenadas
            merge(data, columnIndex, left, middle, right);
        }
    }

    /**
     * Combina duas metades ordenadas de uma lista de arrays de strings.
     *
     * @param data      A lista de arrays de strings contendo os dados a serem combinados.
     * @param columnIndex O índice da coluna pela qual a ordenação será feita.
     * @param left      O índice do elemento mais à esquerda.
     * @param middle    O índice do elemento do meio.
     * @param right     O índice do elemento mais à direita.
     */
    private void merge(List<String[]> data, int columnIndex, int left, int middle, int right) {
        // Tamanhos das sub-listas a serem mescladas
        int n1 = middle - left + 1;
        int n2 = right - middle;

        // Cria sub-listas temporárias
        List<String[]> leftList = new ArrayList<>();
        List<String[]> rightList = new ArrayList<>();

        // Copia os dados para as sub-listas temporárias
        for (int i = 0; i < n1; i++) {
            leftList.add(data.get(left + i));
        }
        for (int j = 0; j < n2; j++) {
            rightList.add(data.get(middle + 1 + j));
        }

        // Índices iniciais para as sub-listas esquerda e direita
        int i = 0, j = 0;

        // Índice inicial para a sub-lista resultante
        int k = left;

        // Combina as sub-listas
        while (i < n1 && j < n2) {
            int leftValue = parseToInt(leftList.get(i)[columnIndex]);
            int rightValue = parseToInt(rightList.get(j)[columnIndex]);

            if (leftValue <= rightValue) {
                data.set(k, leftList.get(i));
                i++;
            } else {
                data.set(k, rightList.get(j));
                j++;
            }
            k++;
        }

        // Copia os elementos restantes da sub-lista esquerda, se houver
        while (i < n1) {
            data.set(k, leftList.get(i));
            i++;
            k++;
        }

        // Copia os elementos restantes da sub-lista direita, se houver
        while (j < n2) {
            data.set(k, rightList.get(j));
            j++;
            k++;
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
     * Realiza a ordenação e mede o tempo de execução usando o algoritmo Merge Sort.
     * O tempo de execução é impresso no console.
     *
     * @param fileToOrder O arquivo a ser ordenado.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        List<String[]> data = carregarArquivoEmLista(fileToOrder);

        long startTime = System.currentTimeMillis();
        mergeSort(data, 6, 0, data.size() - 1); // 6 é o índice da coluna "attendance"
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria(); // Imprimir consumo de memória após a ordenação
    }
}
