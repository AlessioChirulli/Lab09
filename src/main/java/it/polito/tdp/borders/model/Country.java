package it.polito.tdp.borders.model;

public class Country {

	private int ccode;
	private String nomeAbb;
	private String nomeCom;
	
	public Country(int ccode, String nomeAbb, String nomeCom) {
		super();
		this.ccode = ccode;
		this.nomeAbb = nomeAbb;
		this.nomeCom = nomeCom;
	}

	public int getCcode() {
		return ccode;
	}

	public void setCcode(int ccode) {
		this.ccode = ccode;
	}

	public String getNomeAbb() {
		return nomeAbb;
	}

	public void setNomeAbb(String nomeAbb) {
		this.nomeAbb = nomeAbb;
	}

	public String getNomeCom() {
		return nomeCom;
	}

	public void setNomeCom(String nomeCom) {
		this.nomeCom = nomeCom;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ccode;
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
		Country other = (Country) obj;
		if (ccode != other.ccode)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nomeCom ;
	}
	
	
}
