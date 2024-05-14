package com.rubsebedw.proyectoSena.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rubsebedw.proyectoSena.model.PrestamoLibro;
import com.rubsebedw.proyectoSena.service.PrestamoLibroService;

@Controller
//@RequestMapping("/prestamo")
public class PrestamoControlador {
	
	private PrestamoLibroService prestamoLibroService;

	public PrestamoControlador(PrestamoLibroService prestamoLibroService) {
		super();
		this.prestamoLibroService = prestamoLibroService;
	}
	
	@GetMapping("/")
	public String mostrarTodo(Model model) {
		var prestamos = prestamoLibroService.listar();
				
		model.addAttribute("prestamos", prestamos);
		
		return "index";
	}

	@PostMapping
	public ResponseEntity<Object> crearPrestamo(@RequestBody PrestamoLibro solicitud) {
	    LocalDate fechaDevolucion = LocalDate.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	    if (solicitud.getTipoUsuario() == 3) {
	        Optional<PrestamoLibro> prestamoExistente = prestamoLibroService.encontrarPorIdentificacionUsuario(solicitud.getIdentificacionUsuario());
	        if (prestamoExistente.isPresent()) {
	            return ResponseEntity.badRequest().body("{\"mensaje\": \"El usuario con identificación " + solicitud.getIdentificacionUsuario() + " ya tiene un libro prestado por lo cual no se le puede realizar otro préstamo\"}");
	        }
	        fechaDevolucion = addDaysSkippingWeekends(fechaDevolucion, 7);
	    } else if (solicitud.getTipoUsuario() == 1) {
	        fechaDevolucion = addDaysSkippingWeekends(fechaDevolucion, 10);
	    } else if (solicitud.getTipoUsuario() == 2) {
	        fechaDevolucion = addDaysSkippingWeekends(fechaDevolucion, 8);
	    } else if (solicitud.getTipoUsuario() > 3) {
	        return ResponseEntity.badRequest().body("{\"mensaje\": \"Tipo de usuario no permitido en la biblioteca\"}");
	    }

	    PrestamoLibro prestamo = new PrestamoLibro();
	    prestamo.setIsbn(solicitud.getIsbn()); 
	    prestamo.setIdentificacionUsuario(solicitud.getIdentificacionUsuario());
	    prestamo.setTipoUsuario(solicitud.getTipoUsuario());
	    prestamo.setFechaMaximaDevolucion(fechaDevolucion.format(formatter));

	    PrestamoLibro prestamoGuardado = prestamoLibroService.guardar(prestamo);

	    Map<String, Object> resultado = new HashMap<>();
	    resultado.put("id", prestamoGuardado.getId());
	    resultado.put("fechaMaximaDevolucion", prestamoGuardado.getFechaMaximaDevolucion());

	    return ResponseEntity.ok(resultado);
	}

	public static LocalDate addDaysSkippingWeekends(LocalDate date, int days) {
	    LocalDate result = date;
	    int addedDays = 0;
	    while (addedDays < days) {
	        result = result.plusDays(1);
	        ++addedDays;
	    }
	    return result;
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> encontrarPorId(@PathVariable int id) {
	    Optional<PrestamoLibro> prestamoOptional = prestamoLibroService.encontrarPorID(id);

	        Map<String, Object> resultado = new HashMap<>();
	        resultado.put("id", prestamoOptional.get().getId());	        
	        resultado.put("isbn", prestamoOptional.get().getIsbn());
	        resultado.put("identificacionUsuario", prestamoOptional.get().getIdentificacionUsuario());
	        resultado.put("tipoUsuario", prestamoOptional.get().getTipoUsuario());
	        resultado.put("fechaMaximaDevolucion", prestamoOptional.get().getFechaMaximaDevolucion());

	        return ResponseEntity.ok(resultado);
	    } 
	
    @GetMapping("/agregar")
    public String agregar(PrestamoLibro prestamoLibro) {
        return "agregar";
    }
    
    @PostMapping("/guardar")
    public String guardar(PrestamoLibro prestamoLibro) {
	    LocalDate fechaDevolucion = LocalDate.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	    if (prestamoLibro.getTipoUsuario() == 3) {
	        Optional<PrestamoLibro> prestamoExistente = prestamoLibroService.encontrarPorIdentificacionUsuario(prestamoLibro.getIdentificacionUsuario());
//	        if (prestamoExistente.isPresent()) {
//	            return ResponseEntity.badRequest().body("{\"mensaje\": \"El usuario con identificación " + prestamoLibro.getIdentificacionUsuario() + " ya tiene un libro prestado por lo cual no se le puede realizar otro préstamo\"}");
//	        }
	        fechaDevolucion = addDaysSkippingWeekends(fechaDevolucion, 7);
	    } else if (prestamoLibro.getTipoUsuario() == 1) {
	        fechaDevolucion = addDaysSkippingWeekends(fechaDevolucion, 10);
	    } else if (prestamoLibro.getTipoUsuario() == 2) {
	        fechaDevolucion = addDaysSkippingWeekends(fechaDevolucion, 8);
	    } 
//	    else if (prestamoLibro.getTipoUsuario() > 3) {
//	        return ResponseEntity.badRequest().body("{\"mensaje\": \"Tipo de usuario no permitido en la biblioteca\"}");
//	    }

	    PrestamoLibro prestamo = new PrestamoLibro();
	    prestamo.setIsbn(prestamoLibro.getIsbn()); 
	    prestamo.setIdentificacionUsuario(prestamoLibro.getIdentificacionUsuario());
	    prestamo.setTipoUsuario(prestamoLibro.getTipoUsuario());
	    prestamo.setFechaMaximaDevolucion(fechaDevolucion.format(formatter));

	    PrestamoLibro prestamoGuardado = prestamoLibroService.guardar(prestamo);

	    Map<String, Object> resultado = new HashMap<>();
	    resultado.put("id", prestamoGuardado.getId());
	    resultado.put("fechaMaximaDevolucion", prestamoGuardado.getFechaMaximaDevolucion());

	    return "redirect:/";
    }
    
//    @PostMapping("/guardar")
//    public String guardarPrestamo(@ModelAttribute("prestamolibro") PrestamoLibro prestamoLibro) {
//        // Lógica para guardar o actualizar el registro en la base de datos
//        prestamoLibroService.guardar(prestamoLibro);
//
//        // Redirige a la página principal u otra página después de guardar
//        return "redirect:/";
//    }
    
    
//    @GetMapping("/editar/{id}")
//    public String editar(@PathVariable int id, Model model) {
//        Optional<PrestamoLibro> optionalPrestamo = prestamoLibroService.encontrarPorID(id);
//
//        if (!optionalPrestamo.isPresent()) {
//            // Manejar el caso donde no se encuentra el préstamo con el ID proporcionado
//            return "index"; // Puedes redirigir a una página de error o manejarlo de otra manera
//        }
//
//        PrestamoLibro prestamo = optionalPrestamo.get();
//        model.addAttribute("prestamo", prestamo);
//        return "modificar";
//    }
    
	@GetMapping("/editar/{id}")
	public String encontrarId(@PathVariable int id, Model model) {
		Optional<PrestamoLibro> prestamoOptional = prestamoLibroService.encontrarPorID(id);
		
	    if (prestamoOptional.isPresent()) {
	        PrestamoLibro prestamolibro = prestamoOptional.get();

	        model.addAttribute("isbn", prestamolibro.getIsbn());
	        model.addAttribute("identificacionUsuario", prestamolibro.getIdentificacionUsuario());
	        model.addAttribute("tipoUsuario", prestamolibro.getTipoUsuario());

	        return "modificar";
	    } else {
	        // Manejar el caso en que el PrestamoLibro no está presente
	        return "redirect:/"; // Puedes redirigir a una página de error o hacer algo más
	    }
	}
    
//    @GetMapping("/editar/{id}")
//    public String encontrarId(@PathVariable int id, Model model) {
//        Optional<PrestamoLibro> prestamoOptional = prestamoLibroService.encontrarPorID(id);
//
//        if (prestamoOptional.isPresent()) {
//            PrestamoLibro prestamoLibro = prestamoOptional.get();
//            model.addAttribute("prestamolibro", prestamoLibro);
//            return "modificar";
//        } else {
//            return "error"; // Manejar caso de error
//        }
//    }
    
//    @GetMapping("/editar/{id}")
//    public String editar(@PathVariable int id, @ModelAttribute PrestamoLibro prestamolibro) {
//        Optional<PrestamoLibro> prestamoOptional = prestamoLibroService.encontrarPorID(id);
//
//        if (prestamoOptional.isPresent()) {
//            PrestamoLibro prestamo = prestamoOptional.get();
//
//            // Actualizar los campos del prestamo con los valores del formulario
//            prestamo.setIsbn(prestamolibro.getIsbn());
//            prestamo.setIdentificacionUsuario(prestamolibro.getIdentificacionUsuario());
//            prestamo.setTipoUsuario(prestamolibro.getTipoUsuario());
//
//            // Guardar el prestamo actualizado
//            prestamoLibroService.guardar(prestamo);
//        }
//
//        return "modificar";
//    }


    @GetMapping("/eliminar/{id}")
    public String delete(PrestamoLibro prestamoLibro){
    	prestamoLibroService.eliminar(prestamoLibro);
        return "redirect:/";
    }
}





