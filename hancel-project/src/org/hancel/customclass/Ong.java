package org.hancel.customclass;
/*This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
Created by Javier Mejia @zenyagami
zenyagami@gmail.com
	*/
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
