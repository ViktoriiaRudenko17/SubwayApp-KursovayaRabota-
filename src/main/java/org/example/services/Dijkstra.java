package org.example.services;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс Dijkstra реализует алгоритм Дейкстры для поиска минимального расстояния
 * между двумя вершинами в графе, представленном матрицей смежности.
 * Этот класс предоставляет статический метод для вычисления кратчайшего пути
 * от одной вершины до другой с использованием алгоритма, который учитывает
 * веса ребер графа.
 *
 * <p>Алгоритм Дейкстры работает путем инициализации расстояний от исходной станции
 * до всех остальных станций и итеративного обновления этих расстояний на основе
 * найденных минимальных значений. Он подходит для графов с неотрицательными весами
 * ребер.</p>
 */
public class Dijkstra {

    private static final Logger logger = LogManager.getLogger(Dijkstra.class);

    /**
     * Находит минимальное расстояние между двумя станциями в графе,
     * представленном матрицей смежности, с использованием алгоритма Дейкстры.
     *
     * @param adjacencyMatrix матрица смежности, представляющая граф, где
     *                        значение adjacencyMatrix[i][j] - вес ребра между
     *                        станциями i и j. Если ребра нет, то значение должно
     *                        быть Integer.MAX_VALUE.
     * @param departureIdx индекс станции отправления.
     * @param arrivalIdx индекс станции назначения.
     * @return строка, представляющая минимальное расстояние от станции
     *         отправления до станции назначения.
     */
    public static String dijkstra(int[][] adjacencyMatrix, int departureIdx, int arrivalIdx) {
        logger.info("Начат поиск минимального расстояния.");
        int numOfStations = adjacencyMatrix.length;
        int[] distances = initializeDistances(numOfStations, departureIdx);
        boolean[] visited = new boolean[numOfStations];

        IntStream.range(0, numOfStations).forEach(val -> {
            int currentStation = findMinDistanceStation(distances, visited);
            visited[currentStation] = true;
            updateDistances(adjacencyMatrix, distances, visited, currentStation);
        });
        logger.info("Минимальное расстояние найдено и перенаправлено.");
        return Integer.toString(distances[arrivalIdx]);
    }

    /**
     * инициализирует массив расстояний от станции отправления до всех других станций.
     *
     * @param numOfStations общее количество станций в графе.
     * @param departureIdx индекс станции отправления.
     * @return массив расстояний, где значение по индексу соответствует
     *         расстоянию от станции отправления до соответствующей станции.
     */
    private static int[] initializeDistances(int numOfStations, int departureIdx) {
        logger.info("Начало инициализации расстояний");
        int[] distances = new int[numOfStations];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[departureIdx] = 0;
        logger.info("Расстояния успешно инициализированы");
        return distances;
    }

    /**
     * Находит индекс станции с минимальным расстоянием,
     * которая еще не была посещена.
     *
     * @param distances массив расстояний от станции отправления до всех других станций.
     * @param visited массив, указывающий, были ли посещены станции.
     * @return индекс станции с минимальным расстоянием.
     */
    private static int findMinDistanceStation(int[] distances, boolean[] visited) {
        AtomicInteger minIndex = new AtomicInteger(-1);
        AtomicInteger minValue = new AtomicInteger(Integer.MAX_VALUE);

        IntStream.range(0, distances.length).forEach(i -> {
            if (!visited[i] && distances[i] < minValue.get()) {
                minValue.set(distances[i]);
                minIndex.set(i);
            }
        });
        return minIndex.get();
    }

    /**
     * Обновляет массив расстояний на основе текущей станции и матрицы смежности.
     *
     * @param adjacencyMatrix матрица смежности графа.
     * @param distances массив расстояний от станции отправления до всех других станций.
     * @param visited массив, указывающий, были ли посещены станции.
     * @param currentStation индекс текущей станции.
     */
    private static void updateDistances(int[][] adjacencyMatrix, int[] distances, boolean[] visited, int currentStation) {
        IntStream.range(0, adjacencyMatrix.length).forEach(j -> {
            if (!visited[j] && adjacencyMatrix[currentStation][j] != Integer.MAX_VALUE &&
                    distances[currentStation] != Integer.MAX_VALUE &&
                    distances[currentStation] + adjacencyMatrix[currentStation][j] < distances[j]) {
                distances[j] = distances[currentStation] + adjacencyMatrix[currentStation][j];
            }
        });
    }
}
