package Ordenacao.SelectionSort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * A classe {@code InsertionSortAttendance} realiza a ordenação de dados em
 * arquivos CSV usando o algoritmo de ordenação Insertion Sort.
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
    private int attendanceIndex = 6;

    /**
     * Cria uma nova instância de {@code InsertionSortAttendance} com o arquivo de
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
     * crescente.
     */
    private void criarCasoMelhor() {
        try {
            // Contar linhas do arquivo
            int rowCount = contarLinhas(inputFile);

            String[][] data = carregarArquivoEmArray(inputFile, rowCount);

            selectionSort(data, attendanceIndex, rowCount); // Ordenando o array

            // Escrevendo no arquivo de saída
            try (FileWriter writer = new FileWriter(outputMelhor)) {
                for (int i = 0; i < rowCount; i++) {
                    writer.write(String.join(",", data[i]) + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cria o caso de ordenação pior ordenando o arquivo de entrada de forma
     * decrescente.
     */
    private void criarCasoPior() {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
                FileWriter writer = new FileWriter(outputPior, false)) {

            int rowCount = 0;
            while (br.readLine() != null)
                rowCount++;

            br.close();

            String[][] data = new String[rowCount][14];

            BufferedReader newBr = new BufferedReader(new FileReader(inputFile));
            int index = 0;
            String line;
            while ((line = newBr.readLine()) != null) {
                data[index++] = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            }
            newBr.close();

            String[] header = data[0]; // Separando o cabeçalho
            String[][] dataArray = new String[rowCount - 1][14]; // Array para os dados sem cabeçalho
            System.arraycopy(data, 1, dataArray, 0, rowCount - 1); // Copiando os dados sem o cabeçalho

            selectionSort(dataArray, attendanceIndex, rowCount - 1);

            for (int i = 0; i < dataArray.length / 2; i++) {
                String[] temp = dataArray[i];
                dataArray[i] = dataArray[dataArray.length - i - 1];
                dataArray[dataArray.length - i - 1] = temp;
            }

            writer.write(String.join(",", header) + "\n"); // Escrevendo o cabeçalho primeiro
            for (int i = 0; i < rowCount - 1; i++) { // Escrevendo os dados invertidos
                writer.write(String.join(",", dataArray[i]) + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * Ordena os dados no arquivo especificado e imprime o tempo de execução.
     *
     * @param fileToOrder O arquivo a ser ordenado.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        try {
            // Contar linhas do arquivo
            int rowCount = contarLinhas(fileToOrder);

            String[][] data = carregarArquivoEmArray(fileToOrder, rowCount);

            String[][] dataArray = new String[rowCount - 1][14]; // Array para os dados sem cabeçalho
            System.arraycopy(data, 1, dataArray, 0, rowCount - 1); // Copiando os dados sem o cabeçalho

            // Início da medição de tempo
            long startTime = System.currentTimeMillis();

            selectionSort(dataArray, attendanceIndex, rowCount - 1);

            // Fim da medição de tempo
            long endTime = System.currentTimeMillis();
            System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
            imprimirConsumoMemoria(); // Imprimir consumo de memória após a ordenação

            // NÃO gravaremos os dados ordenados no arquivo para manter os casos como estão
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Conta o número de linhas em um arquivo.
     *
     * @param fileToOrder O arquivo para o qual as linhas serão contadas.
     * @return O número de linhas no arquivo.
     * @throws IOException Se ocorrer um erro de leitura do arquivo.
     */
    private int contarLinhas(String fileToOrder) throws IOException {
        int rowCount = 0;
        try (BufferedReader counter = new BufferedReader(new FileReader(fileToOrder))) {
            while (counter.readLine() != null)
                rowCount++;
        }
        return rowCount;
    }

    /**
     * Carrega os dados de um arquivo CSV em um array bidimensional.
     *
     * @param fileToOrder O arquivo CSV a ser carregado.
     * @param rowCount    O número de linhas no arquivo.
     * @return Um array bidimensional contendo os dados do arquivo.
     * @throws IOException Se ocorrer um erro de leitura do arquivo.
     */
    private String[][] carregarArquivoEmArray(String fileToOrder, int rowCount) throws IOException {
        String[][] data = new String[rowCount][14];

        int index = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileToOrder))) {
            String line;
            while ((line = br.readLine()) != null) {
                data[index++] = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            }
        }
        return data;
    }

    /**
     * Ordena um array bidimensional com base em uma coluna específica usando o
     * algoritmo Selection Sort.
     *
     * @param data        O array bidimensional a ser ordenado.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     * @param rowCount    O número de linhas no array.
     */
    private void selectionSort(String[][] data, int columnIndex, int rowCount) {
        for (int i = 0; i < rowCount - 1; i++) {
            int min_idx = i;
            for (int j = i + 1; j < rowCount; j++) {
                String currentData = data[j][columnIndex].replace("\"", "").replace(",", "");
                String minData = data[min_idx][columnIndex].replace("\"", "").replace(",", "");

                if (!isNumeric(currentData)) {
                    currentData = "0";
                }
                if (!isNumeric(minData)) {
                    minData = "0";
                }

                int currentVal = Integer.parseInt(currentData);
                int minVal = Integer.parseInt(minData);

                if (currentVal < minVal) {
                    min_idx = j;
                }
            }

            String[] temp = data[min_idx];
            data[min_idx] = data[i];
            data[i] = temp;
        }
    }

    /**
     * Verifica se uma string pode ser convertida em um número inteiro.
     *
     * @param str A string a ser verificada.
     * @return {@code true} se a string for um número inteiro válido, caso contrário
     *         {@code false}.
     */
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void imprimirConsumoMemoria() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();

        long usedMemory = heapMemoryUsage.getUsed();

        System.out.println("Consumo de memória: " + usedMemory + " bytes");
    }
}
