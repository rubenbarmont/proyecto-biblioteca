package com.rubsebedw.proyectoSena.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rubsebedw.proyectoSena.model.PrestamoLibro;
import com.rubsebedw.proyectoSena.repository.PrestamoLibroRepositorio;
import com.rubsebedw.proyectoSena.service.PrestamoLibroService;

import jakarta.transaction.Transactional;

@Service
public class PrestamoLibroImpl implements PrestamoLibroService{
	
	@Autowired
	private PrestamoLibroRepositorio prestamoLibroRepositorio;

	@Override
	public PrestamoLibro guardar(PrestamoLibro prestamo) {
		return prestamoLibroRepositorio.save(prestamo);
	}

	@Override
	public Optional<PrestamoLibro> encontrarPorID(Integer id) {
		Optional<PrestamoLibro> encontrar = prestamoLibroRepositorio.findById(id);
		return encontrar;
	}
	
	public Optional<PrestamoLibro> encontrarPorIdentificacionUsuario(String identificacionUsuario){
		Optional<PrestamoLibro> encontrarIdentificacionUsuario = prestamoLibroRepositorio.findByIdentificacionUsuario(identificacionUsuario);
		return encontrarIdentificacionUsuario;
	}

	@Override
	public List<PrestamoLibro> listar() {
		return (List<PrestamoLibro>) prestamoLibroRepositorio.findAll();
	}

	@Override
//	@Transactional(readOnly = true)
	public PrestamoLibro encontrarPrestamo(PrestamoLibro prestamo) {
		return prestamoLibroRepositorio.findById(prestamo.getId()).orElse(null);
	}

	@Override
	public void eliminar(PrestamoLibro prestamo) {
		prestamoLibroRepositorio.delete(prestamo);
		
	}

}
