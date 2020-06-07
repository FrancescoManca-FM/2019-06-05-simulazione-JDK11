package it.polito.tdp.crimes.model;

public class DistrictPeso implements Comparable<DistrictPeso>{

	private District d1;
	private District d2;
	private double distanza;
	public DistrictPeso(District d1, District d2, double distanza) {
		super();
		this.d1 = d1;
		this.d2 = d2;
		this.distanza = distanza;
	}
	public District getD1() {
		return d1;
	}
	public void setD1(District d1) {
		this.d1 = d1;
	}
	public District getD2() {
		return d2;
	}
	public void setD2(District d2) {
		this.d2 = d2;
	}
	public double getDistanza() {
		return distanza;
	}
	public void setDistanza(double distanza) {
		this.distanza = distanza;
	}
	@Override
	public int compareTo(DistrictPeso o) {
		if(this.distanza<o.distanza) {
			return -1;
		}else if(this.distanza==o.distanza) {
			return 0;
		}else {
			return 1;
		}
	}
	
	
}
