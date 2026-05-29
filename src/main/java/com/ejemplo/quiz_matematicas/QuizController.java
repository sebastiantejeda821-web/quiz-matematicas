package com.ejemplo.quiz_matematicas;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
public class QuizController {

    private static final int TOTAL = 10;

    @GetMapping("/")
    public String inicio() {
        return "menu";
    }

    @PostMapping("/iniciar")
    public String iniciar(@RequestParam String modo,
                          @RequestParam String nivel,
                          @RequestParam(defaultValue = "0") int tiempo,
                          HttpSession session) {

        // Reinicia el estado en la sesión del usuario
        session.setAttribute("modo",             modo);
        session.setAttribute("nivel",            nivel);
        session.setAttribute("tiempo",           tiempo);
        session.setAttribute("respondidas",      0);
        session.setAttribute("correctas",        0);
        session.setAttribute("preguntasUsadas",  new HashSet<String>());

        return "redirect:/jugar";
    }

    @GetMapping("/jugar")
    public String jugar(HttpSession session, Model model) {
        String modo     = (String)  session.getAttribute("modo");
        String nivel    = (String)  session.getAttribute("nivel");
        int tiempo      = (int)     session.getAttribute("tiempo");
        int respondidas = (int)     session.getAttribute("respondidas");
        int correctas   = (int)     session.getAttribute("correctas");

        if (modo == null) return "redirect:/";

        Pregunta pregunta = generarSinRepetir(modo, nivel, session);

        model.addAttribute("pregunta",       pregunta);
        model.addAttribute("mensaje",        "");
        model.addAttribute("correctas",      correctas);
        model.addAttribute("incorrectas",    0);
        model.addAttribute("restantes",      TOTAL);
        model.addAttribute("numeroPregunta", respondidas + 1);
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
                            HttpSession session, Model model) {

        String modo     = (String)  session.getAttribute("modo");
        String nivel    = (String)  session.getAttribute("nivel");
        int tiempo      = (int)     session.getAttribute("tiempo");
        int respondidas = (int)     session.getAttribute("respondidas");
        int correctas   = (int)     session.getAttribute("correctas");

        if (modo == null) return "redirect:/";

        // Evitar doble envío — si ya respondió todas no procesa más
        if (respondidas >= TOTAL) return "redirect:/jugar";

        boolean tiempoAgotado = respuestaUsuario.equals("__tiempo_agotado__");
        boolean esCorrecta    = !tiempoAgotado && respuestaUsuario.equals(respuestaCorrecta);

        if (esCorrecta) correctas++;
        respondidas++;

        session.setAttribute("respondidas", respondidas);
        session.setAttribute("correctas",   correctas);

        boolean finalizado  = respondidas >= TOTAL;
        int incorrectas     = respondidas - correctas;
        int restantes       = Math.max(TOTAL - respondidas, 0);
        int progresoPct     = Math.min((respondidas * 100) / TOTAL, 100);
        int numeroPregunta  = finalizado ? TOTAL : respondidas + 1;

        String msg = tiempoAgotado
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
            model.addAttribute("pregunta", generarSinRepetir(modo, nivel, session));
        }
        return "quiz";
    }

    @SuppressWarnings("unchecked")
    private Pregunta generarSinRepetir(String modo, String nivel, HttpSession session) {
        Set<String> usadas = (Set<String>) session.getAttribute("preguntasUsadas");
        if (usadas == null) {
            usadas = new HashSet<>();
            session.setAttribute("preguntasUsadas", usadas);
        }

        Pregunta pregunta;
        int intentos = 0;
        do {
            pregunta = GeneradorPreguntas.generar(modo, nivel);
            intentos++;
        } while ((usadas.contains(pregunta.getEnunciado()) ||
                HistorialPreguntas.fueUsada(pregunta.getEnunciado()))
                && intentos < 30);

        usadas.add(pregunta.getEnunciado());
        HistorialPreguntas.registrar(pregunta.getEnunciado());
        session.setAttribute("preguntasUsadas", usadas);
        return pregunta;
        }
}
