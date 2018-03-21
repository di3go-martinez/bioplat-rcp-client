package edu.unlp.medicine.bioplat.poc.rcp.personas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.unlp.medicine.entity.generic.AbstractEntity;

public class Persona extends AbstractEntity {

	public Persona(List<String> nombres, String apellido) {
		super();
		this.nombres = nombres;
		this.apellido = apellido;
	}

	private List<String> nombres = new ArrayList<String>();
	private String apellido = "";

	public static Persona random() {
		List<String> nombres = new ArrayList<String>();
		nombres.add("diego");
		nombres.add("ariel");
		for (int i = 0; i < 300; i++)
			nombres.add(String.valueOf(new Random().nextInt()));
		return new Persona(nombres, "Martínez" + String.valueOf(new Random().nextInt()));
	}

	public List<String> getNombres() {
		return nombres;
	}

	public void setNombres(List<String> nombres) {
		this.nombres = nombres;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public static Persona nullPersona() {
		return new Persona(new ArrayList<String>(), "");
	}

}
