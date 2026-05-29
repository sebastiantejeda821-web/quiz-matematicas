package com.ejemplo.quiz_matematicas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class QuizController {

    private static final int TOTAL = 10;
    private int respondidas = 0;
    private int correctas   = 0;
    private String modo     = "mixto";
    private String nivel    = "facil";
    private int tiempo      = 0;

    @GetMapping("/")
    public String inicio() {
        return "menu";
    }

    @PostMapping("/iniciar")
    public String iniciar(@RequestParam String modo,
                          @RequestParam String nivel,
                          @RequestParam(defaultValue = "0") int tiempo,
                          Model model) {
        this.modo        = modo;
        this.nivel       = nivel;
        this.tiempo      = tiempo;
        this.respondidas = 0;
        this.correctas   = 0;

        model.addAttribute("pregunta",       generarSinRepetir(modo, nivel));
        model.addAttribute("mensaje",        "");
        model.addAttribute("correctas",      0);
        model.addAttribute("incorrectas",    0);
        model.addAttribute("restantes",      TOTAL);
        model.addAttribute("numeroPregunta", 1);
        model.addAttribute("total",          TOTAL);
        model.addAttribute("progresoPct",    0);
        model.addAttribute("finalizado",     false);
        model.addAttribute("modo",           modo);
        model.addAttribute("nivel",          nivel);
        model.addAttribute("tiempo",         tiempo);
        return "quiz";
    }

    @PostMapping("/responder")
    public String responder(@RequestParam String respuestaUsuario,
                            @RequestParam String respuestaCorrecta,
                            Model model) {

        boolean esCorrecta = respuestaUsuario.equals(respuestaCorrecta)
                && !respuestaUsuario.equals("__tiempo_agotado__");

        if (esCorrecta) correctas++;
        respondidas++;

        boolean finalizado  = respondidas >= TOTAL;
        int incorrectas     = respondidas - correctas;
        int restantes       = TOTAL - respondidas;
        int progresoPct     = (respondidas * 100) / TOTAL;
        int numeroPregunta  = finalizado ? TOTAL : respondidas + 1;

        String msg = respuestaUsuario.equals("__tiempo_agotado__")
                ? "⏰ ¡Tiempo agotado! La respuesta era: " + respuestaCorrecta
                : esCorrecta
                ? "✅ ¡Correcto!"
                : "❌ Incorrecto. La respuesta era: " + respuestaCorrecta;

        model.addAttribute("mensaje",        msg);
        model.addAttribute("correctas",      correctas);
        model.addAttribute("incorrectas",    incorrectas);
        model.addAttribute("restantes",      restantes);
        model.addAttribute("numeroPregunta", numeroPregunta);
        model.addAttribute("total",          TOTAL);
        model.addAttribute("progresoPct",    progresoPct);
        model.addAttribute("finalizado",     finalizado);
        model.addAttribute("modo",           modo);
        model.addAttribute("nivel",          nivel);
        model.addAttribute("tiempo",         tiempo);

        if (!finalizado) {
            model.addAttribute("pregunta", generarSinRepetir(modo, nivel));
        }
        return "quiz";
    }

    private Pregunta generarSinRepetir(String modo, String nivel) {
        Pregunta pregunta;
        int intentos = 0;
        do {
            pregunta = GeneradorPreguntas.generar(modo, nivel);
            intentos++;
        } while (HistorialPreguntas.fueUsada(pregunta.getEnunciado()) && intentos < 30);

        HistorialPreguntas.registrar(pregunta.getEnunciado());
        return pregunta;
    }
}