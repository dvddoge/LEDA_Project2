import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.lang.Thread;

import Ordenacao.InsertionSort.*;
import Ordenacao.MergeSort.*;
import Ordenacao.CountingSort.*;
import Ordenacao.SelectionSort.*;
import Ordenacao.HeapSort.*;
import Ordenacao.QuickSort.*;
import Ordenacao.QuickSortMediana3.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String inputMatches = "src/data/matches.csv";
        String outputMatchesT1 = "src/TransformaçõesResultados/matches_T1.csv";
        String[] columnsToKeep = { "id", "home", "away", "date", "year", "time (utc)", "attendance", "venue", "league",
                "home_score", "away_score", "home_goal_scorers", "away_goal_scorers" };

        try (BufferedReader br = new BufferedReader(new FileReader(inputMatches));
                FileWriter writer = new FileWriter(outputMatchesT1)) {

            String line;
            int[] indicesToKeep = new int[columnsToKeep.length];

            if ((line = br.readLine()) != null) {
                String[] headers = line.split(",");

                for (int i = 0; i < headers.length; i++) {
                    for (int j = 0; j < columnsToKeep.length; j++) {
                        if (headers[i].equals(columnsToKeep[j])) {
                            indicesToKeep[j] = i;
                        }
                    }
                }

                for (int i = 0; i < indicesToKeep.length; i++) {
                    writer.write(headers[indicesToKeep[i]]);
                    if (i < indicesToKeep.length - 1) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }

            while ((line = br.readLine()) != null) {
                StringBuilder sb = new StringBuilder();
                boolean insideQuotes = false;
                int valueCount = 0;
                String[] values = new String[50];

                for (char c : line.toCharArray()) {
                    if (c == '\"') {
                        insideQuotes = !insideQuotes;
                    }
                    if (c == ',' && !insideQuotes) {
                        if (valueCount == values.length) {
                            String[] newValues = new String[values.length * 2];
                            System.arraycopy(values, 0, newValues, 0, values.length);
                            values = newValues;
                        }
                        values[valueCount] = sb.toString();
                        sb = new StringBuilder();
                        valueCount++;
                    } else {
                        sb.append(c);
                    }
                }
                if (valueCount == values.length) {
                    String[] newValues = new String[values.length * 2];
                    System.arraycopy(values, 0, newValues, 0, values.length);
                    values = newValues;
                }
                values[valueCount] = sb.toString();

                for (int i = 0; i < indicesToKeep.length; i++) {
                    if (indicesToKeep[i] <= valueCount) {
                        writer.write(values[indicesToKeep[i]]);
                    }
                    if (i < indicesToKeep.length - 1) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }
            /*
             * System.out.println("Criando arquivo " + outputMatchesT1);
             * Thread.sleep(3000); // Pause de 3 segundos
             */

        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputMatchesT1 = "src/TransformaçõesResultados/matches_T1.csv";
        String outputMatchesT2 = "src/TransformaçõesResultados/matches_T2.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(inputMatchesT1));
                FileWriter writer = new FileWriter(outputMatchesT2)) {

            String line;

            if ((line = br.readLine()) != null) {
                writer.write(line + ",full_date\n");
            }

            SimpleDateFormat inputFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

            while ((line = br.readLine()) != null) {
                StringBuilder sb = new StringBuilder();
                boolean insideQuote = false;
                String[] values = new String[50];
                int valueCount = 0;

                for (char c : line.toCharArray()) {
                    if (c == '"') {
                        insideQuote = !insideQuote;
                    }
                    if (c == ',' && !insideQuote) {
                        values[valueCount] = sb.toString().trim();
                        sb.setLength(0);
                        valueCount++;
                    } else {
                        sb.append(c);
                    }
                }
                values[valueCount] = sb.toString().trim();

                String dateStr = values[3].replaceAll("\"", "") + ", " + values[4];
                Date date = inputFormat.parse(dateStr);
                String fullDate = outputFormat.format(date);

                writer.write(line + "," + fullDate + "\n");
            }
            /*
             * System.out.println("Criando arquivo " + outputMatchesT2);
             * Thread.sleep(3000); // Pause de 3 segundos
             */

        } catch (IOException | java.text.ParseException e) {
            e.printStackTrace();
        }

        String inputMatchesT2 = "src/TransformaçõesResultados/matches_T2.csv";
        String outputMatchesF1 = "src/TransformaçõesResultados/matches_F1.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(inputMatchesT2));
                FileWriter writer = new FileWriter(outputMatchesF1)) {

            String line;

            if ((line = br.readLine()) != null) {
                writer.write(line + "\n");
            }

            int leagueIndex = 8;

            while ((line = br.readLine()) != null) {
                StringBuilder sb = new StringBuilder();
                boolean insideQuote = false;
                String[] values = new String[50];
                int valueCount = 0;

                for (char c : line.toCharArray()) {
                    if (c == '"') {
                        insideQuote = !insideQuote;
                    }
                    if (c == ',' && !insideQuote) {
                        values[valueCount] = sb.toString().trim();
                        sb.setLength(0);
                        valueCount++;
                    } else {
                        sb.append(c);
                    }
                }
                values[valueCount] = sb.toString().trim();

                if (values[leagueIndex].contains("English Premier League")) {
                    writer.write(line + "\n");
                }
            }
            /*
             * System.out.println("Criando arquivo " + outputMatchesF1);
             * Thread.sleep(3000); // Pause de 3 segundos
             */

        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputMatchesF1 = "src/TransformaçõesResultados/matches_F1.csv";
        String outputMatchesF2 = "src/TransformaçõesResultados/matches_F2.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(inputMatchesF1));
                FileWriter writer = new FileWriter(outputMatchesF2)) {

            String line;

            if ((line = br.readLine()) != null) {
                writer.write(line + "\n");
            }

            int attendanceIndex = 6;

            while ((line = br.readLine()) != null) {
                StringBuilder sb = new StringBuilder();
                boolean insideQuote = false;
                String[] values = new String[50];
                int valueCount = 0;

                for (char c : line.toCharArray()) {
                    if (c == '"') {
                        insideQuote = !insideQuote;
                    }
                    if (c == ',' && !insideQuote) {
                        values[valueCount] = sb.toString().trim();
                        sb.setLength(0);
                        valueCount++;
                    } else {
                        sb.append(c);
                    }
                }
                values[valueCount] = sb.toString().trim();

                if (!values[attendanceIndex].isEmpty()) {
                    int attendance = Integer.parseInt(values[attendanceIndex].replaceAll("[^0-9]", ""));

                    if (attendance > 20000) {
                        writer.write(line + "\n");
                    }
                }
            }
            /*
             * System.out.println("Criando arquivo " + outputMatchesF2);
             * Thread.sleep(3000); // Pause de 3 segundos
             */

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        int fieldChoice, algorithmChoice;

        do {
            System.out.println("Escolha o campo para ordenar:");
            System.out.println("1- Ordenar por 'venue'");
            System.out.println("2- Ordenar por 'attendance'");
            System.out.println("3- Ordenar por 'full_date'");
            System.out.println("0- Sair");
            System.out.print("Sua escolha: ");
            fieldChoice = scanner.nextInt();
            System.out.println();

            if (fieldChoice == 0)
                break;

            do {
                System.out.println("Escolha o algoritmo de ordenação:");
                System.out.println("1- InsertionSort");
                System.out.println("2- SelectionSort");
                System.out.println("3- CountingSort");
                System.out.println("4- HeapSort");
                System.out.println("5- MergeSort");
                System.out.println("6- QuickSort");
                System.out.println("7- QuickSortMediana3");
                System.out.println("0- Voltar");
                System.out.print("Sua escolha: ");
                algorithmChoice = scanner.nextInt();
                System.out.println("\nCriando Arquivos... (isso pode levar um tempo)");

                if (algorithmChoice == 0)
                    break;

                switch (fieldChoice) {
                    case 1:
                        switch (algorithmChoice) {
                            case 1:
                                InsertionSortVenue insertionVenue = new InsertionSortVenue(outputMatchesT2);
                                insertionVenue.ordenar();
                                break;
                            case 2:
                                SelectionSortVenue selectionVenue = new SelectionSortVenue(outputMatchesT2);
                                selectionVenue.ordenar();
                                break;
                            case 3:
                                CountingSortVenue countingVenue = new CountingSortVenue(outputMatchesT2);
                                countingVenue.ordenar();
                                break;
                            case 4:
                                HeapSortVenue heapVenue = new HeapSortVenue(outputMatchesT2);
                                heapVenue.ordenar();
                                break;
                            case 5:
                                MergeSortVenue mergeVenue = new MergeSortVenue(outputMatchesT2);
                                mergeVenue.ordenar();
                                break;
                            case 6:
                                QuickSortVenue quickVenue = new QuickSortVenue(outputMatchesT2);
                                quickVenue.ordenar();
                                break;
                            case 7:
                                QuickSortMediana3Venue quickMediana3Venue = new QuickSortMediana3Venue(outputMatchesT2);
                                quickMediana3Venue.ordenar();
                                break;

                        }
                        break;

                    case 2:
                        switch (algorithmChoice) {
                            case 1:
                                InsertionSortAttendance insertionAttendance = new InsertionSortAttendance(
                                        outputMatchesT2);
                                insertionAttendance.ordenar();
                                break;
                            case 2:
                                SelectionSortAttendance selectionAttendance = new SelectionSortAttendance(
                                        outputMatchesT2);
                                selectionAttendance.ordenar();
                                break;
                            case 3:
                                CountingSortAttendance countingAttendance = new CountingSortAttendance(outputMatchesT2);
                                countingAttendance.ordenar();
                                break;
                            case 4:
                                HeapSortAttendance heapAttendance = new HeapSortAttendance(outputMatchesT2);
                                heapAttendance.ordenar();
                                break;
                            case 5:
                                MergeSortAttendance mergeAttendance = new MergeSortAttendance(outputMatchesT2);
                                mergeAttendance.ordenar();
                                break;
                            case 6:
                                QuickSortAttendance quickAttendance = new QuickSortAttendance(outputMatchesT2);
                                quickAttendance.ordenar();
                                break;
                            case 7:
                                QuickSortMediana3Attendance quickMediana3Attendance = new QuickSortMediana3Attendance(
                                        outputMatchesT2);
                                quickMediana3Attendance.ordenar();
                                break;
                        }
                        break;

                    case 3:
                        switch (algorithmChoice) {
                            case 1:
                                InsertionSortFullDate insertionFullDate = new InsertionSortFullDate(outputMatchesT2);
                                insertionFullDate.ordenar();
                                break;
                            case 2:
                                SelectionSortFullDate selectionFullDate = new SelectionSortFullDate(outputMatchesT2);
                                selectionFullDate.ordenar();
                                break;
                            case 3:
                                CountingSortFullDate countingFullDate = new CountingSortFullDate(outputMatchesT2);
                                countingFullDate.ordenar();
                                break;
                            case 4:
                                HeapSortFullDate heapFullDate = new HeapSortFullDate(outputMatchesT2);
                                heapFullDate.ordenar();
                                break;
                            case 5:
                                MergeSortFullDate mergeFullDate = new MergeSortFullDate(outputMatchesT2);
                                mergeFullDate.ordenar();
                                break;
                            case 6:
                                QuickSortFullDate quickFullDate = new QuickSortFullDate(outputMatchesT2);
                                quickFullDate.ordenar();
                                break;
                            case 7:
                                QuickSortMediana3FullDate quickMediana3FullDate = new QuickSortMediana3FullDate(
                                        outputMatchesT2);
                                quickMediana3FullDate.ordenar();
                                break;
                        }
                        break;
                }

            } while (algorithmChoice != 0);

        } while (fieldChoice != 0);

        scanner.close();
    }
}