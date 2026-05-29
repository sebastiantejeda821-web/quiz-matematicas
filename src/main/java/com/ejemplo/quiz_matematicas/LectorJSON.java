package com.ejemplo.quiz_matematicas;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import java.util.*;

public class LectorJSON {

    private static List<PreguntaJSON> todasLasPreguntas = null;
    private static final Random random = new Random();

    private static List<PreguntaJSON> cargar() {
        if (todasLasPreguntas == null) {
            todasLasPreguntas = new ArrayList<>();
            try {
                ObjectMapper mapper = new ObjectMapper();
                ClassPathResource resource = new ClassPathResource("preguntas.json");
                JsonNode root = mapper.readTree(resource.getInputStream());
                JsonNode array = root.get("preguntas");

                for (JsonNode node : array) {
                    PreguntaJSON p = new PreguntaJSON();
                    p.enunciado = node.get("enunciado").asText();
                    p.respuesta = node.get("respuesta").asText();
                    p.modo      = node.get("modo").asText();
                    p.nivel     = node.get("nivel").asText();
                    p.opciones  = new ArrayList<>();
                    for (JsonNode op : node.get("opciones")) {
                        p.opciones.add(op.asText());
                    }
                    todasLasPreguntas.add(p);
                }
            } catch (Exception e) {
                System.out.println("Error leyendo preguntas.json: " + e.getMessage());
            }
        }
        return todasLasPreguntas;
    }

    public static Pregunta obtener(String modo, String nivel) {
        List<PreguntaJSON> filtradas = new ArrayList<>();
        for (PreguntaJSON p : cargar()) {
            if ((p.modo.equals(modo) || modo.equals("mixto")) && p.nivel.equals(nivel)) {
                filtradas.add(p);
            }
        }

        if (filtradas.isEmpty()) return null;

        PreguntaJSON pj = filtradas.get(random.nextInt(filtradas.size()));
        List<String> opciones = new ArrayList<>(pj.opciones);
        Collections.shuffle(opciones);
        return new Pregunta(pj.enunciado, opciones, pj.respuesta);
    }
}