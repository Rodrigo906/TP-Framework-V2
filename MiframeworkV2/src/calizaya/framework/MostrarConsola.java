package calizaya.framework;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class MostrarConsola {

	private List<Action> acciones;

	public MostrarConsola(String pathArchivoPropiedades) {
		this.acciones = new ArrayList<Action>();
		Properties propiedades = new Properties();
		try {
			InputStream archivoConfig = getClass().getResourceAsStream(pathArchivoPropiedades);
			propiedades.load(archivoConfig);

			Enumeration<Object> claves = propiedades.keys();
			Action accion;
			while (claves.hasMoreElements()) {
				Object clave = claves.nextElement();
				String nombreClase = (String) propiedades.get(clave);
				Class<?> clase = Class.forName(nombreClase);
				accion = (Action) clase.getDeclaredConstructor().newInstance();
				acciones.add(accion);
			}

		} catch (Exception e) {
			throw new RuntimeException("Error al crear las instancias", e);
		}
	}

	public void mostrarMenu() {
		EstructuraMenu menu = new EstructuraMenu(acciones);
		menu.cargarMenu();

	}
}
