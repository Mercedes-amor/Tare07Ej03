package com.example.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.app.domain.Departamento;
import com.example.app.services.DepartamentoService;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/depto") //Añade como un "prefijo" a todas las rutas
@SessionAttributes("txtErr") // Para que nos almacene la variable en la sesión y poder acceder a ella
public class DepartamentoController {

    @Autowired(required = true)
    DepartamentoService departamentoService;

    private String txtErr; // Variable global para almacenar los textos de error


    @GetMapping({ "/list", "/" })
    public String showList(@RequestParam(required = false) Integer err, Model model) {
        // Si el RequestParam no es null se muestra el contenido de la variable txtErr
        if (err != null) {
            model.addAttribute("txtErr", txtErr);
        }
        // Reseteamos variable del mensaje
        txtErr = null;

        model.addAttribute("listaDepartamentos", departamentoService.obtenerTodos());
        return "departamento/listView";

    }


    @GetMapping("/add")
    public String showNew(
            @RequestParam(required = false) Integer err,
            Model model) {

        // Para los errores genéricos que llegan por el @Valid
        // Como el formato del email o los campos vacíos.
        if (err != null) {
            model.addAttribute("textErr", "Hubo un error en los datos del formulario");
        }

        // Para los errores que llegan por la excepción lanzada desde el servicio
        // Id repetido y salario <18000
        if (txtErr != null) {
            model.addAttribute("textErr", txtErr);
            // Reseteamos la variable
            txtErr = null;

        }

        model.addAttribute("departamentoForm", new Departamento());
        return "departamento/newFormView";
    }

    @PostMapping("/add/submit")
    public String showNewSubmit(@Valid Departamento departamentoForm, BindingResult bindingResult, Model model) {
        // Para los errores que llegan por el @Valid
        if (bindingResult.hasErrors()) {
            return "redirect:/depto/add?err=1";
        }
        try {
            departamentoService.add(departamentoForm);
        } catch (RuntimeException e) {
            // Capturamos las excepciones que llegan del service
            txtErr = e.getMessage();
            return "redirect:/depto/add";
        }

        return "redirect:/depto/list";
    }

    @GetMapping("/edit/{id}")
    public String getEdit(
            @PathVariable long id,
            @RequestParam(required = false) Integer err,
            Model model) {

        // Para los errores genéricos que llegan por el @Valid
        // Como el formato del email o los campos vacíos.
        if (err != null) {
            model.addAttribute("text2Err", "Hubo un error en los datos actualizados");
        }

        // Para los errores que llegan por la excepción lanzada desde el servicio
        // Id repetido y salario <18000
        if (txtErr != null) {
            model.addAttribute("text2Err", txtErr);
            // Reseteamos la variable
            txtErr = null;
        }
        Departamento departamento = departamentoService.obtenerPorId(id);
        model.addAttribute("departamentoForm", departamento);
        return "departamento/editFormView";
    }

    @PostMapping("/edit/submit")
    public String getEditSubmit(
            @Valid Departamento departamentoForm,
            BindingResult bindingResult) {
        // Para los errores que llegan por el @Valid
        if (bindingResult.hasErrors()) {
            return "redirect:/depto/edit/submit?err=1";
        }
        try {
            departamentoService.actualizar(departamentoForm);
        } catch (RuntimeException e) {
            // Capturamos las excepciones que llegan del service
            txtErr = e.getMessage();
            return "redirect:/depto/edit/submit";
        }

        return "redirect:/depto/list";
    }

    @GetMapping("/borrar/{id}")
    public String showDelete(@PathVariable Long id) {
        departamentoService.eliminarPorId(id);
        return "redirect:/depto/list";
    }

   
}
