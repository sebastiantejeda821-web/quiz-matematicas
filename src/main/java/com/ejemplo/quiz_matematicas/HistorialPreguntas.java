package com.ejemplo.quiz_matematicas;

import java.util.LinkedHashSet;
import java.util.Set;

public class HistorialPreguntas {

    private static final int MAX_HISTORIAL = 300;

    // Static — vive en memoria mientras el servidor esté corriendo
    private static final Set<String> historial = new LinkedHashSet<>();

    public static synchronized boolean fueUsada(String enunciado) {
        return historial.contains(enunciado.trim());
    }

    public static synchronized void registrar(String enunciado) {
        historial.add(enunciado.trim());

        // Si supera el límite elimina las más antiguas
        if (historial.size() > MAX_HISTORIAL) {
            String primero = historial.iterator().next();
            historial.remove(primero);
        }
    }
}