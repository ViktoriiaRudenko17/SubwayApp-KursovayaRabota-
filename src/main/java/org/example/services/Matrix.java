package org.example.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Launcher;

/**
 * Класс Matrix представляет собой структуру данных, которая хранит информацию о станциях и их взаимосвязях
 * в виде матрицы смежности. Он загружает данные из файлов и предоставляет методы для работы с ними.
 */
public class Matrix {

    private static final Logger logger = LogManager.getLogger(Matrix.class);
    private static final int EMPTY_CELL = 0; // используем int вместо Integer для значений
    public int numOfTheStations;
    public ArrayList<String> stationsNames;
    public int[][] adjacencyMatrix;

    /**
     * Конструктор класса Matrix, который инициализирует матрицу смежности.
     *
     * @param fileStations имя файла, содержащего названия станций.
     * @param fileEdges имя файла, содержащего связи между станциями и их веса.
     * @throws FileNotFoundException если один из указанных файлов не найден.
     */
    public Matrix(String fileStations, String fileEdges) throws FileNotFoundException {
        logger.info("Алгоритм заполнения матрицы смежности запущен.");
        fillMatrix(fileStations, fileEdges);
        logger.info("Матрица смежности успешно создана.");
    }

    /**
     * Заполняет матрицу смежности, считывая данные из файлов.
     *
     * @param fileStations имя файла, содержащего названия станций.
     * @param fileEdges имя файла, содержащего связи между станциями и их веса.
     * @throws FileNotFoundException если один из указанных файлов не найден.
     */
    public void fillMatrix(String fileStations, String fileEdges) throws FileNotFoundException {
        setStationNamesFromFile(fileStations);
        setNumOfTheStations();
        adjacencyMatrix = new int[numOfTheStations][numOfTheStations];
        Arrays.stream(adjacencyMatrix).forEach(row -> Arrays.fill(row, Integer.MAX_VALUE));
        initMatrix(fileEdges);
    }

    /**
     * Считывает названия станций из указанного файла и сохраняет их в список.
     *
     * @param fileName имя файла, содержащего названия станций.
     * @throws FileNotFoundException если файл не найден.
     */
    public void setStationNamesFromFile(String fileName) throws FileNotFoundException {
        Scanner namesScanner = new Scanner(Launcher.class.getResourceAsStream(fileName));
        stationsNames = new ArrayList<>();
        while (namesScanner.hasNextLine()) {
            stationsNames.add(namesScanner.nextLine());
        }
        logger.info("Названия станций успешно считаны.");
        namesScanner.close(); // Закрываем сканер
    }

    /**
     * Устанавливает количество станций на основе загруженных названий станций.
     */
    public void setNumOfTheStations() {
        numOfTheStations = stationsNames.size();
        logger.info("Количество станций успешно инициализировано.");
    }
    /**
     * инициализирует матрицу смежности, считывая данные из указанного файла.
     *
     * @param fileName имя файла, содержащего связи между станциями и их веса.
     * @throws FileNotFoundException если файл не найден.
     */
    public void initMatrix(String fileName) throws FileNotFoundException {
        Scanner matrixScanner = new Scanner(Launcher.class.getResourceAsStream(fileName));
        String[] node;
        while (matrixScanner.hasNextLine()) {
            node = matrixScanner.nextLine().split(",");
            try {
                int station1Index = getIndexOfTheStationForInit(node[0]);
                int station2Index = getIndexOfTheStationForInit(node[1]);
                int weight = Integer.parseInt(node[2]);
                addEdge(station1Index, station2Index, weight);
            } catch (Exception e) {
                throw e;
            }
        }
        matrixScanner.close();

        // Заполняем диагональ нулями
        IntStream.range(0, numOfTheStations).forEach(i -> adjacencyMatrix[i][i] = EMPTY_CELL);
        logger.info("Данные о расстояниях успешно загружены.");
    }

    /**
     * Получает индекс станции по её названию для инициализации матрицы.
     *
     * @param stationName название станции.
     * @return индекс станции в списке названий.
     * @throws ArrayIndexOutOfBoundsException если станция не найдена в списке.
     */
    public int getIndexOfTheStationForInit(String stationName) {
        int index = this.stationsNames.indexOf(stationName);
        if (index == -1) {
            throw new ArrayIndexOutOfBoundsException("Станция '" + stationName + "' не найдена, проверьте правильность заполнения файлов.");
        }
        return index;
    }

    /**
     * Получает индекс станции по её названию.
     *
     * @param stationName название станции.
     * @return индекс станции в списке названий.
     * @throws IllegalArgumentException если название станции пустое.
     * @throws ArrayIndexOutOfBoundsException если станция не найдена в списке.
     */
    public int getIndexOfTheStation(String stationName) {
        if (stationName.isEmpty()) {
            logger.error("Поле не заполнено.");
            throw new IllegalArgumentException("Заполните все поля.");
        }
        logger.info("Начат поиск станции {}", stationName);
        int index = this.stationsNames.indexOf(stationName);
        if (index == -1) {
            logger.error("Станция {} не найдена", stationName);
            throw new ArrayIndexOutOfBoundsException("Станция '" + stationName + "' не найдена.");
        }
        logger.info("индекс станции {} успешно найден", stationName);
        return index;
    }

    /**
     * добавляет ребро между двумя станциями в матрицу смежности с указанным весом.
     *
     * <p>Метод обновляет значения в матрице смежности, устанавливая вес ребра между
     * станциями, заданными индексами {@code station1} и {@code station2}. Если граф
     * неориентированный, вес устанавливается в обе стороны.</p>
     *
     * @param station1 индекс первой станции, к которой добавляется ребро.
     * @param station2 индекс второй станции, к которой добавляется ребро.
     * @param weight вес ребра между двумя станциями. Должен быть положительным числом.
     *
     * @throws ArrayIndexOutOfBoundsException если один из индексов {@code station1} или
     *         {@code station2} выходит за пределы диапазона от 0 до {@code numOfTheStations - 1}.
     */
    private void addEdge(int station1, int station2, int weight) {
        if (station1 >= 0 && station2 >= 0 && station1 < numOfTheStations && station2 < numOfTheStations) {
            adjacencyMatrix[station1][station2] = weight;
            adjacencyMatrix[station2][station1] = weight; // Если граф неориентированный
        }
    }
}