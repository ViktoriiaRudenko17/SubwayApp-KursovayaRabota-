package org.example.exceptions;

import javafx.scene.control.Alert;

import java.io.FileNotFoundException;

/**
 * Класс ErrorHandler предоставляет методы для обработки и отображения сообщений об ошибках
 * в приложении. Он использует всплывающие окна для уведомления пользователя о возникших
 * проблемах, таких как отсутствие файлов или другие ошибки.
 */
public class ErrorHandler {

    /**
     * Отображает сообщение об ошибке в виде всплывающего окна.
     *
     * @param message текст сообщения об ошибке, который будет отображен пользователю.
     */
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Обрабатывает исключение FileNotFoundException и отображает соответствующее сообщение об ошибке.
     *
     * @param e исключение FileNotFoundException, содержащее информацию о причине ошибки.
     */
    public static void handleFileNotFoundException(FileNotFoundException e) {
        showError("Ошибка загрузки данных, проверьте правильность названия файлов и их расположение: " + e.getMessage());
    }

    /**
     * Отображает сообщение об ошибке, если файл изображения не найден.
     */
    public static void handleImageNotFound() {
        showError("Ошибка: Файл изображения не найден.");
    }

    /**
     * Отображает сообщение об ошибке с заданным текстом.
     *
     * @param message текст сообщения об ошибке, который будет отображен пользователю.
     */
    public static void handleError(String message) {
        showError(message);
    }
}
