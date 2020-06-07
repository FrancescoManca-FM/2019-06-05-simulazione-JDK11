package it.polito.tdp.crimes.model;

public class District {

	private int id;
	private double lat_centro;
	private double lang_centro;

	public District(int id) {
		super();
		this.id = id;
	}

	
	public int getId() {
		return id;
	}

	
	public void setId(int id) {
		this.id = id;
	}


	public double getLat_centro() {
		return lat_centro;
	}


	public void setLat_centro(double lat_centro) {
		this.lat_centro = lat_centro;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		District other = (District) obj;
		if (id != other.id)
			return false;
		return true;
	}


	public double getLang_centro() {
		return lang_centro;
	}


	public void setLang_centro(double lang_centro) {
		this.lang_centro = lang_centro;
	}
	
}
