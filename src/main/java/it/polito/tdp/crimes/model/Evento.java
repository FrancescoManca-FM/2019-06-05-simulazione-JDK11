package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{

	public enum EventType{
		EVENTO_CRIMINOSO, AGENTE_ARRIVATO, CRIMINE_RISOLTO
	}
	
	private Event event;
	private EventType tipo;
	private LocalDateTime ora;
	private Agente agente;
	
	public Evento(Event evento, EventType tipo, LocalDateTime ora, Agente agente) {
		super();
		this.event = evento;
		this.tipo = tipo;
		this.ora = ora;
		this.agente = agente;
	}
	
	
	
	@Override
	public String toString() {
		return "Evento [event=" + event.getOffense_category_id() + ", tipo=" + tipo + ", ora=" + ora + ", agente=";
	}



	public Agente getAgente() {
		return agente;
	}



	public void setAgente(Agente agente) {
		this.agente = agente;
	}



	public LocalDateTime getOra() {
		return ora;
	}



	public void setOra(LocalDateTime ora) {
		this.ora = ora;
	}



	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public EventType getTipo() {
		return tipo;
	}
	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}



	@Override
	public int compareTo(Evento o) {
		return this.ora.compareTo(o.ora);
	}
	
}
