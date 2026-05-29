package com.ejemplo.quiz_matematicas;
import java.util.List;
public class Pregunta {
    private String enunciado;
    private List<String> opciones;
    private String respuesta;

    public Pregunta(String enunciado, List<String> opciones, String respuesta) {
        this.enunciado = enunciado;
        this.opciones  = opciones;
        this.respuesta = respuesta;
    }

    public String getEnunciado() { return enunciado; }
    public List<String> getOpciones() { return opciones; }
    public String getRespuesta() { return respuesta; }
}
