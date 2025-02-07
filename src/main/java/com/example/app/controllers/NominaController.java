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

import com.example.app.domain.Empleado;
import com.example.app.domain.Nomina;
import com.example.app.services.EmpleadoService;
import com.example.app.services.NominaService;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/nomina") //Añade como un "prefijo" a todas las rutas
@SessionAttributes("txtErr") // Para que nos almacene la variable en la sesión y poder acceder a ella
public class NominaController {

    @Autowired
    NominaService nominaService;
    @Autowired
    EmpleadoService empleadoService;
    private String txtErr; // Variable global para almacenar los textos de error
    private String txtNominasCargadas; //Variable para mensaje carga correcta

    @GetMapping({ "/list", "/" })
    public String showList(@RequestParam(required = false) Integer err, Model model) {
        // Si el RequestParam no es null se muestra el contenido de la variable txtErr
        if (err != null) {
            model.addAttribute("txtErr", txtErr);
        }
        //Pasamos el mensaje de nóminas cargadas si es el caso
        model.addAttribute("txtNominasCargadas", txtNominasCargadas);

        // Reseteamos variables mensaje
        txtErr = null;
        txtNominasCargadas = null;

        model.addAttribute("listaNominas", nominaService.obtenerTodos());
        return "nomina/listView";

    }

    //Ruta nóminas por empleado
    @GetMapping("/porEmpleado/{idEmpleado}")
    public String showListNominasEmpleado(@PathVariable Long idEmpleado, Model model) {
        
        //Obtenemos el empleado del que queremos saber las nóminas
        //Para sacar su nombre y array de nóminas
        Empleado empleado = empleadoService.obtenerPorId(idEmpleado);
           
        model.addAttribute("nombreEmpleado", empleado.getNombre());
        model.addAttribute("listaNominas", empleado.getNominas());
        return "nomina/listView";

    }

    //Cargar nóminas desde el CSV
    @GetMapping("/cargar")
    public String cargarNominas() {
        try {
            nominaService.cargarNominasFichero();
            txtNominasCargadas = "Nominas cargadas correctamente";
        } catch (Exception e) {
            txtErr = e.getMessage();
        }
        
        return "redirect:/nomina/list";
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

        if (txtErr != null) {
            model.addAttribute("textErr", txtErr);
            // Reseteamos la variable
            txtErr = null;

        }

        //Pasamos la lista de empleados para el formulario
        model.addAttribute("listaEmpleados", empleadoService.obtenerTodos());
        model.addAttribute("nominaForm", new Nomina());
        return "nomina/newFormView";
    }

    @PostMapping("/add/submit")
    public String showNewSubmit(@Valid Nomina nominaForm, BindingResult bindingResult, Model model) {
        // Para los errores que llegan por el @Valid
        if (bindingResult.hasErrors()) {
            return "redirect:/nomina/add?err=1";
        }
        try {
            nominaService.add(nominaForm);
        } catch (RuntimeException e) {
            // Capturamos las excepciones que llegan del service
            txtErr = e.getMessage();
            return "redirect:/nomina/add";
        }

        return "redirect:/nomina/list";
    }

    @GetMapping("/borrar/{id}")
    public String showDelete(@PathVariable Long id) {
        nominaService.eliminarPorId(id);
        return "redirect:/nomina/list";
    }


   
}
