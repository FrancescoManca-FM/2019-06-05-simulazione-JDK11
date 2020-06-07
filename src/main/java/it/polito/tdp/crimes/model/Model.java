package it.polito.tdp.crimes.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private SimpleWeightedGraph<District, DefaultWeightedEdge> grafo;
	private Simulator sim;
	
	public Model() {
		this.dao = new EventsDao();
		this.sim = new Simulator();
	}
	
	public List<Integer> getAnni(){
		return this.dao.getAnni();
	}
	
	public List<Integer> getMesi(){
		return this.dao.getMesi();
	}
	public List<Integer> getGiorni(){
		return this.dao.getGiorni();
	}
	
	public int verticiSize() {
		return this.grafo.vertexSet().size();
	}
	
	public int archiSize() {
		return this.grafo.edgeSet().size();
	}
	
	public List<District> getVertici(){
		return this.dao.ListAllDistretti();
	}
	
	public void creaGrafo(int anno) {
		this.grafo = new SimpleWeightedGraph<District, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		List<District> distretti = this.dao.ListAllDistretti();
		for(District a : distretti) {
			impostaCentro(a, anno);
		}
		
		Graphs.addAllVertices(this.grafo, distretti);
		for(District a : distretti) {
			for(District b : distretti) {
				if(!a.equals(b) && this.grafo.getEdge(a, b)==null) {
					Graphs.addEdgeWithVertices(this.grafo, a, b, distanzaPunti(a.getLat_centro(), a.getLang_centro(), b.getLat_centro(), b.getLang_centro()));
				}
			}
		}
	}
	

	
	public void impostaCentro(District distretto, int anno) {
		List<Event> eventi = this.dao.ListEventsDistrettoAnno(anno, distretto);
		double latMax = eventi.get(0).getGeo_lat();
		double latMin = eventi.get(0).getGeo_lat();
		double langMax = eventi.get(0).getGeo_lon();
		double langMin = eventi.get(0).getGeo_lon();
		for(Event e : eventi) {
			if(e.getGeo_lat()>latMax) {
				latMax = e.getGeo_lat();
			}else if(e.getGeo_lat()<latMin) {
				latMin = e.getGeo_lat();
			}
			
			if(e.getGeo_lon()>langMax) {
				langMax = e.getGeo_lon();
			}else if(e.getGeo_lon()<langMin) {
				langMin = e.getGeo_lon();
			}
		}
		distretto.setLang_centro((langMax-langMin)/2);
		distretto.setLat_centro((latMax-latMin)/2);
	}
	
	public double distanzaPunti(double lat1, double long1, double lat2, double long2) {
		LatLng centro1 = new LatLng(lat1, long1);
		LatLng centro2 = new LatLng(lat2, long2);
		return LatLngTool.distance(centro1, centro2, LengthUnit.KILOMETER);
		
	}
	
	public List<DistrictPeso> getDistrettiPerPeso(District distretto){
		List<District> vicini = Graphs.neighborListOf(this.grafo, distretto);
		List<DistrictPeso> risultato = new ArrayList<>();
		for(District d : vicini) {
			DistrictPeso dp = new DistrictPeso(distretto, d, this.grafo.getEdgeWeight(this.grafo.getEdge(distretto, d)));
			risultato.add(dp);
		}
		Collections.sort(risultato);
		return risultato;
	}
	
	public List<Event> getEventiGiorno(int anno, int mese, int giorno){
		return this.dao.getEventiGiorno(anno, mese, giorno);
	}
	
	public District getDistrettoMinimo(int anno) {
		return this.dao.getDistrettoMinimo(anno);
	}
	
	
	public void initSim(int anno, int mese, int giorno, int N) {
		this.sim.init(N, LocalDate.of(anno, mese, giorno), this.grafo, this);
	}
	
	public void runSim() {
		this.sim.run();
	}
	public int getCriminiMalGestiti() {
		return this.sim.getCriminiMalGestiti();
	}
	public int getCrimini() {
		return this.sim.getCrimini();
	}
}
