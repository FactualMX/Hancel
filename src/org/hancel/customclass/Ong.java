package org.hancel.customclass;

public class Ong {
	private String id;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the nombreOng
	 */
	public String getNombreOng() {
		return NombreOng;
	}

	private String NombreOng;

	public Ong(String id,String nombreOng,String descripcion) {
		this.id = id;
		this.NombreOng = nombreOng;
		this.ongDescripcion = descripcion;
	}
	private String ongDescripcion;
	/**
	 * @return the ongDescripcion
	 */
	public String getOngDescripcion() {
		return ongDescripcion;
	}

	/**
	 * @param ongDescripcion the ongDescripcion to set
	 */
	public void setOngDescripcion(String ongDescripcion) {
		this.ongDescripcion = ongDescripcion;
	}
	

}
