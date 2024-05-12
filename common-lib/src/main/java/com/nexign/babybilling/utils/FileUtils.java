package com.nexign.babybilling.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

@UtilityClass
public class FileUtils {

    /**
     * Создает родительские папки
     * @param path исходный файл
     */
    @SneakyThrows
    public static void createParentDir(Path path) {
        Path parent = path.getParent();
        if(!Files.isDirectory(parent)) {
            Files.createDirectories(parent);
        }
    }

    /**
     * Записывает данные в файл
     * @param file файл, куда записать данные
     * @param data данные на запись
     */
    @SneakyThrows
    public static void writeData(Path file, String data) {
        Files.writeString(file, data);
    }

    /**
     * Удаляет директорию со всеми файлами
     * @param path директория на удаление
     */
    @SneakyThrows
    public static void deleteDir(Path path) {
        if(!Files.isDirectory(path)) return;
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }
}
