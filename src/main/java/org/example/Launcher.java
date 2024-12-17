package org.example;

import org.example.front.SubwayApp;

/**
 * Класс Launcher служит точкой входа в приложение SubwayApp.
 * Он содержит метод main, который запускает приложение.
 */
public class Launcher {

    /**
     * Главный метод приложения, который запускает SubwayApp.
     *
     * @param args массив строковых аргументов командной строки, переданных при запуске приложения.
     *             Этот параметр может использоваться для передачи дополнительных данных в приложение,
     *             но в данном случае не используется.
     */
    public static void main(String[] args) {
        SubwayApp.init(args);
    }
}