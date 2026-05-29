package com.ejemplo.quiz_matematicas;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class HistorialPreguntas {

    // Guarda el historial en la carpeta del proyecto
    private static final String ARCHIVO = "historial_preguntas.txt";
    private static final int MAX_HISTORIAL = 200;
    private static Set<String> historial = null;

    // Carga el historial del archivo
    private static Set<String> cargar() {
        if (historial == null) {
            historial = new LinkedHashSet<>();
            try {
                Path path = Paths.get(ARCHIVO);
                if (Files.exists(path)) {
                    List<String> lineas = Files.readAllLines(path);
                    historial.addAll(lineas);
                }
            } catch (Exception e) {
                System.out.println("Historial vacío, empezando desde cero.");
            }
        }
        return historial;
    }

    // Verifica si una pregunta ya fue usada recientemente
    public static boolean fueUsada(String enunciado) {
        return cargar().contains(enunciado.trim());
    }

    // Registra una pregunta como usada
    public static void registrar(String enunciado) {
        Set<String> h = cargar();
        h.add(enunciado.trim());

        // Si el historial es muy grande borra las más antiguas
        if (h.size() > MAX_HISTORIAL) {
            Iterator<String> it = h.iterator();
            it.next();
            it.remove();
        }

        guardar();
    }

    // Guarda el historial en el archivo
    private static void guardar() {
        try {
            Files.write(Paths.get(ARCHIVO), historial);
        } catch (Exception e) {
            System.out.println("Error guardando historial: " + e.getMessage());
        }
    }

    // Limpia el historial completamente
    public static void limpiar() {
        if (historial != null) historial.clear();
        try {
            Files.deleteIfExists(Paths.get(ARCHIVO));
        } catch (Exception e) {
            System.out.println("Error limpiando historial.");
        }
    }
}