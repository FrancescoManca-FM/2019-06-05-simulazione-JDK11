package it.polito.tdp.crimes.model;

import java.awt.Choice;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.model.Evento.EventType;

public class Simulator {

	//coda degli eventi
	private PriorityQueue<Evento> coda;
	
	
	//parametri simulazione
	private int N; //numero agenti
	private List<Agente> agenti;
	private double velocitaAgente; 
	
	private LocalDate GIORNO_SIMULAZIONE;
	
	private final Duration DURATION_OTHER_CRIMES = Duration.of(1, ChronoUnit.HOURS);
	private final Duration DURATION_CRIMES = Duration.of(2, ChronoUnit.HOURS);
	private final Duration TIMEOUT_CRIMES = Duration.of(15, ChronoUnit.MINUTES);
	
	private SimpleWeightedGraph<District, DefaultWeightedEdge> grafo;
	private Model model;
	
	//parametri output
	private int crimini;
	private int criminiMalGestiti;
	
	public void init(int N, LocalDate GiornoSimulazione, SimpleWeightedGraph<District, DefaultWeightedEdge> grafo, Model model) {
		this.N = N;
		this.velocitaAgente = 60.00;
		this.agenti = new ArrayList<>();
		this.GIORNO_SIMULAZIONE = GiornoSimulazione;
		this.grafo = grafo;
		this.model = model;
		for(int i=0; i<N; i++) {
			this.agenti.add(new Agente(i, this.model.getDistrettoMinimo(GiornoSimulazione.getYear()), true));
		}
		this.crimini = 0;
		this.criminiMalGestiti = 0;
		this.coda = new PriorityQueue<>();
		
		for(Event e : this.model.getEventiGiorno(GiornoSimulazione.getYear(), GiornoSimulazione.getMonthValue(), GiornoSimulazione.getDayOfMonth())) {
			Evento ev = new Evento(e, EventType.EVENTO_CRIMINOSO, e.getReported_date(), null);
			this.coda.add(ev);
		}
	}
	
	public void run() {
		while(!this.coda.isEmpty()) {
			processEvent(this.coda.poll());
		}
	}
	
	public void processEvent(Evento e) {
		System.out.println(e.toString());
		switch(e.getTipo()) {
		case EVENTO_CRIMINOSO:
			this.crimini++;
			Agente agente = this.agenteLiberoVicino(trovaDistretto(e.getEvent().getDistrict_id()));
			if(agente==null) {
				this.criminiMalGestiti++;
				break;
			}else if(agente.getPosizione().getId()==e.getEvent().getDistrict_id()){
				long viaggio = 0;
				System.out.println(viaggio);
				this.coda.add(new Evento(e.getEvent(), EventType.AGENTE_ARRIVATO, e.getOra(), agente));
				agente.setLibero(false);
			}else {
				long viaggio = (long)(this.grafo.getEdgeWeight(this.grafo.getEdge(agente.getPosizione(), trovaDistretto(e.getEvent().getDistrict_id())))/this.velocitaAgente*3600);
				System.out.println(viaggio);
				this.coda.add(new Evento(e.getEvent(), EventType.AGENTE_ARRIVATO, e.getOra().plus(Duration.of(viaggio, ChronoUnit.SECONDS)), agente));
				agente.setLibero(false);
			}
			break;
			
		case AGENTE_ARRIVATO:
			e.getAgente().setPosizione(trovaDistretto(e.getEvent().getDistrict_id()));
			if(e.getOra().isAfter(e.getEvent().getReported_date().plus(this.TIMEOUT_CRIMES))) {
				this.criminiMalGestiti++;
				e.getAgente().setLibero(true);
				break;
			}else {
				if(e.getEvent().getOffense_category_id().equals("all_other_crimes")) {
					Double casuale = Math.random();
					if(casuale<=0.5) {
						this.coda.add(new Evento(e.getEvent(), EventType.CRIMINE_RISOLTO, e.getOra().plus(DURATION_OTHER_CRIMES), e.getAgente()));
					}else {
						this.coda.add(new Evento(e.getEvent(), EventType.CRIMINE_RISOLTO, e.getOra().plus(DURATION_CRIMES), e.getAgente()));
					}
				}else {
					this.coda.add(new Evento(e.getEvent(), EventType.CRIMINE_RISOLTO, e.getOra().plus(DURATION_CRIMES), e.getAgente()));

				}
			}
			break;
			
		case CRIMINE_RISOLTO:
			e.getAgente().setLibero(true);
			break;
		}
	}
	
	public int getCrimini() {
		return crimini;
	}

	public int getCriminiMalGestiti() {
		return criminiMalGestiti;
	}

	public Agente agenteLiberoVicino(District distrettoCrimine) {
		double distanza = Double.MAX_VALUE;
		Agente best = null;
		for(Agente a : this.agenti) {
			if(distrettoCrimine.getId()==a.getPosizione().getId() && a.libero) {
				best = a;
				distanza = 0.0;
			} else if(this.grafo.getEdge(a.getPosizione(), distrettoCrimine)!=null) {
				if(a.libero && this.grafo.getEdgeWeight(this.grafo.getEdge(a.getPosizione(), distrettoCrimine))<distanza) {
					best = a;
					distanza = this.grafo.getEdgeWeight(this.grafo.getEdge(a.getPosizione(), distrettoCrimine));
				}
			}else {
				if(a.libero && this.grafo.getEdgeWeight(this.grafo.getEdge(distrettoCrimine, a.getPosizione()))<distanza) {
					best = a;
					distanza = this.grafo.getEdgeWeight(this.grafo.getEdge(a.getPosizione(), distrettoCrimine));
				}
			}
		}
		return best;
	}
	
	public District trovaDistretto(int id) {
		for(District d : this.grafo.vertexSet()) {
			if(d.getId()==id) {
				return d;
			}
		}
		return null;
	}
}
