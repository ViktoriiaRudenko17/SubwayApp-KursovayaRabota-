package org.example.front;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Launcher;
import org.example.services.Matrix;
import org.example.exceptions.ErrorHandler;

import java.io.FileNotFoundException;

import static org.example.services.Dijkstra.dijkstra;
/**
 * Главный класс приложения SubwayApp, реализующий интерфейс для работы с метрополитеном.
 * Приложение позволяет пользователю вводить станции отправления и прибытия,
 * а также рассчитывать минимальный путь между ними.
 */
public class SubwayApp extends Application {

    private static final Logger logger = LogManager.getLogger(SubwayApp.class);
    private TextField departureField;
    private TextField arrivalField;
    private TextField distanceField;
    private Matrix sbwMatrix;


    /**
     * Запускает приложение, создавая основной интерфейс и загружая данные о метрополитене.
     *
     * @param primaryStage основная стадия приложения
     */
    @Override
    public void start(Stage primaryStage) {
        logger.info("Начат запуск программы.");
        try {
            sbwMatrix = new Matrix("stations.txt", "rebra.txt");
            logger.info("Данные метрополитена загружены успешно.");
        } catch (FileNotFoundException e) {
            ErrorHandler.handleFileNotFoundException(e);
            logger.error("Не удалось загрузить данные.");
            return; // Завершаем выполнение, если не удалось загрузить данные
        }

        primaryStage.setTitle("Метрополитен Санкт-Петербурга");

        // Основной макет
        BorderPane borderPane = new BorderPane();

        // Загрузка изображения карты
        ImageView mapImageView = new ImageView();
        loadMapImage(mapImageView);
        borderPane.setCenter(mapImageView);

        // Создание полей для ввода станций
        departureField = new TextField();
        arrivalField = new TextField();
        distanceField = new TextField();
        distanceField.setEditable(false);

        // Кнопки для очистки полей
        Button clearDepartureButton = new Button("Очистить");
        clearDepartureButton.setOnAction(e -> {
            departureField.clear();
            logger.info("Поле 'станция отправления' успешно очищена");
        });

        Button clearArrivalButton = new Button("Очистить");
        clearArrivalButton.setOnAction(e -> {
            arrivalField.clear();
            logger.info("Поле 'станция прибытия' успешно очищена");
        });

        Button calculateButton = new Button("Рассчитать путь");
        calculateButton.setOnAction(e -> {
            try {
                int departureIdx = getIdx(sbwMatrix, departureField.getText());
                int arrivalIdx = getIdx(sbwMatrix, arrivalField.getText());
                logger.info("Данные о станциях успешно получены и перенаправлены на обработку.");
                String result = dijkstra(sbwMatrix.adjacencyMatrix, departureIdx, arrivalIdx);
                distanceField.setText(result);
                logger.info("Минимальное расстояние получено.");
            } catch (Exception ex) {
                logger.error("Введены некорректные данные.");
                ErrorHandler.handleError(ex.getMessage());
            }
        });

        Button exitButton = new Button("Выход");
        exitButton.setOnAction(e -> {
            primaryStage.close();
            logger.info("Приложение успешно закрылось.");
        });


        // Сетка для размещения полей и кнопок
        GridPane inputGrid = new GridPane();
        inputGrid.setPadding(new Insets(10));
        inputGrid.setVgap(10);
        inputGrid.setHgap(10);

        inputGrid.add(new Label("Станция отправления:"), 0, 0);
        inputGrid.add(departureField, 1, 0);
        inputGrid.add(clearDepartureButton, 2, 0);

        inputGrid.add(new Label("Станция прибытия:"), 0, 1);
        inputGrid.add(arrivalField, 1, 1);
        inputGrid.add(clearArrivalButton, 2, 1);

        inputGrid.add(new Label("Минимальный путь (мин):"), 0, 2);
        inputGrid.add(distanceField, 1, 2);

        inputGrid.add(calculateButton, 1, 3);
        inputGrid.add(exitButton, 2, 3);

        borderPane.setBottom(inputGrid);

        Scene scene = new Scene(borderPane, 600, 750);
        primaryStage.setScene(scene);
        primaryStage.show();
        logger.info("Программа успешно запущена");
    }

    /**
     * Загружает изображение карты метро и устанавливает его в указанный ImageView.
     *
     * @param imageView объект ImageView, в который будет загружено изображение
     */
    private void loadMapImage(ImageView imageView) {
        try {
            Image image = new Image(Launcher.class.getResourceAsStream("metro.png"));
            imageView.setImage(image);
            imageView.setFitWidth(600);
            imageView.setFitHeight(600);
            imageView.setPreserveRatio(true);
        } catch (Exception ex) {
            ErrorHandler.handleImageNotFound();
            logger.error("Файл с изображением не найден.");
        }
    }

    /**
     * Получает индекс станции в матрице на основе её названия.
     *
     * @param matrix матрица станций
     * @param station название станции
     * @return индекс станции в матрице
     * @throws Exception если станция не найдена
     */
    public int getIdx(Matrix matrix, String station) throws Exception {
        return matrix.getIndexOfTheStation(station.toUpperCase());
    }

    /**
     * Главный метод для запуска приложения.
     *
     * @param args аргументы командной строки
     */
    public static void init(String[] args) {
        launch(args);
    }
}