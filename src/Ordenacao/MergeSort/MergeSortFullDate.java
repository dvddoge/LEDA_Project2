package Ordenacao.MergeSort;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;

/**
 * A classe {@code MergeSortFullDate} realiza a ordenação de dados em arquivos
 * CSV usando o algoritmo de ordenação Merge Sort. Ela oferece três cenários de ordenação
 * (melhor, médio e pior caso) e mede o tempo de execução para cada cenário.
 */
public class MergeSortFullDate {
    private String inputFile;
    private String path = "src/OrdenacaoResultados/MergeSort/";
    private String outputMedio = path + "matches_t2_full_date_mergeSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_full_date_mergeSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_full_date_mergeSort_piorCaso.csv";
    private int fullDateIndex = 13;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Construtor que inicializa a classe com o arquivo de entrada fornecido.
     *
     * @param inputFile O arquivo de entrada contendo os dados a serem ordenados.
     */
    public MergeSortFullDate(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Realiza a ordenação dos dados nos cenários de melhor, médio e pior caso e imprime os tempos de execução.
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
     * Cria o cenário de ordenação médio copiando o conteúdo do arquivo de entrada para o arquivo de saída.
     */
    private void criarCasoMedio() {
        copiarArquivo(inputFile, outputMedio);
    }

    /**
     * Cria o cenário de ordenação de melhor caso ordenando o arquivo de entrada usando o algoritmo Merge Sort.
     */
    private void criarCasoMelhor() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        mergeSort(data, fullDateIndex, 0, data.size() - 1);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o cenário de ordenação de pior caso ordenando o arquivo de entrada usando o algoritmo Merge Sort
     * e depois invertendo a ordem dos dados.
     */
    private void criarCasoPior() {
        List<String[]> data = carregarArquivoEmLista(inputFile);
        mergeSort(data, fullDateIndex, 0, data.size() - 1);
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
     * Carrega os dados de um arquivo CSV em uma lista de arrays de strings.
     *
     * @param file O arquivo CSV a ser carregado.
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
     * @param outputFile O arquivo onde os dados serão escritos.
     */
    private void escreverDados(List<String[]> data, String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            // Escreva o cabeçalho
            writer.write(
                    "id,home,away,date,year,time (utc),attendance,venue,league,home_score,away_score,home_goal_scorers,away_goal_scorers,full_date");
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
     * Ordena os dados no arquivo especificado usando o algoritmo Merge Sort e imprime o tempo de execução.
     *
     * @param fileToOrder O arquivo a ser ordenado.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        List<String[]> data = carregarArquivoEmLista(fileToOrder);

        long startTime = System.currentTimeMillis();
        mergeSort(data, fullDateIndex, 0, data.size() - 1);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria();
    }

    /**
     * Utiliza o algoritmo Merge Sort para ordenar uma subparte da lista de arrays de strings com base no índice da coluna fornecida.
     *
     * @param data       A lista de arrays de strings a ser ordenada.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     * @param left       O índice inicial da subparte a ser ordenada.
     * @param right      O índice final da subparte a ser ordenada.
     */
    private void mergeSort(List<String[]> data, int columnIndex, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(data, columnIndex, left, mid);
            mergeSort(data, columnIndex, mid + 1, right);
            merge(data, columnIndex, left, mid, right);
        }
    }

    /**
     * Realiza a fusão (merge) de duas subpartes ordenadas da lista de arrays de strings em uma única subparte ordenada.
     *
     * @param data       A lista de arrays de strings a serem mesclados.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     * @param left       O índice inicial da primeira subparte.
     * @param mid        O índice final da primeira subparte.
     * @param right      O índice final da segunda subparte.
     */
    private void merge(List<String[]> data, int columnIndex, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        List<String[]> leftList = new ArrayList<>(data.subList(left, mid + 1));
        List<String[]> rightList = new ArrayList<>(data.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            String leftDate = leftList.get(i)[columnIndex].replace("\"", "").trim();
            String rightDate = rightList.get(j)[columnIndex].replace("\"", "").trim();

            if (!leftDate.isEmpty() && !rightDate.isEmpty()) {
                if (compareDates(leftDate, rightDate) <= 0) {
                    data.set(k++, leftList.get(i++));
                } else {
                    data.set(k++, rightList.get(j++));
                }
            } else {
                if (leftDate.isEmpty()) {
                    data.set(k++, rightList.get(j++));
                } else {
                    data.set(k++, leftList.get(i++));
                }
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
     * Compara duas datas no formato "dd/MM/yyyy".
     *
     * @param date1 A primeira data a ser comparada.
     * @param date2 A segunda data a ser comparada.
     * @return Um valor negativo se date1 for anterior a date2, zero se forem iguais
     *         ou um valor positivo se date1 for posterior a date2.
     */
    private int compareDates(String date1, String date2) {
        try {
            Date d1 = sdf.parse(date1.replace("\"", ""));
            Date d2 = sdf.parse(date2.replace("\"", ""));
            return d1.compareTo(d2);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
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
}
