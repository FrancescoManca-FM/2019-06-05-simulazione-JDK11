package it.polito.tdp.crimes.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polito.tdp.crimes.model.District;
import it.polito.tdp.crimes.model.Event;



public class EventsDao {
	
	public List<Event> listEventsAnno(int anno){
		String sql = "SELECT *  " + 
				"FROM events " + 
				"WHERE YEAR(reported_date)=?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Event> getEventiGiorno(int anno, int mese, int giorno){
		String sql = "SELECT * " + 
				"FROM EVENTS " + 
				"WHERE YEAR(reported_date)=? AND MONTH(reported_date)=? AND DAY(reported_date)=? " + 
				"ORDER BY reported_date";
		List<Event> risultato = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, mese);
			st.setInt(3, giorno);
			
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				risultato.add(new Event(res.getLong("incident_id"),
						res.getInt("offense_code"),
						res.getInt("offense_code_extension"), 
						res.getString("offense_type_id"), 
						res.getString("offense_category_id"),
						res.getTimestamp("reported_date").toLocalDateTime(),
						res.getString("incident_address"),
						res.getDouble("geo_lon"),
						res.getDouble("geo_lat"),
						res.getInt("district_id"),
						res.getInt("precinct_id"), 
						res.getString("neighborhood_id"),
						res.getInt("is_crime"),
						res.getInt("is_traffic")));
			}
			conn.close();
			return risultato;
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nel caricamento dati del database");
		}
	}
	
	public List<Integer> getMesi(){
		
		String sql = "SELECT distinct MONTH(reported_date) as mese " + 
				"FROM EVENTS " + 
				"ORDER BY reported_date";
		List<Integer> risultato = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				risultato.add(rs.getInt("mese"));
			}
			conn.close();
			Collections.sort(risultato);
			return risultato;
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nel caricamento dati dal database");
		}
	}
	
	public List<Integer> getGiorni(){
		
		String sql = "SELECT distinct DAY(reported_date) as giorno " + 
				"FROM EVENTS " + 
				"ORDER BY reported_date";
		List<Integer> risultato = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				risultato.add(rs.getInt("giorno"));
			}
			conn.close();
			Collections.sort(risultato);
			return risultato;
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nel caricamento dati dal database");
		}
	}
	
	
	public List<Integer> getAnni(){
		
		String sql = "SELECT distinct YEAR(reported_date) as anno " + 
				"FROM EVENTS " + 
				"ORDER BY reported_date";
		List<Integer> risultato = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				risultato.add(rs.getInt("anno"));
			}
			conn.close();
			return risultato;
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nel caricamento dati dal database");
		}
	}
	
	public District getDistrettoMinimo(int anno) {
		
		String sql = "SELECT district_id " + 
				"FROM events " + 
				"WHERE YEAR(reported_date)=? " + 
				"GROUP BY district_id " + 
				"ORDER BY COUNT(*) ASC " + 
				"LIMIT 1";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();
			rs.next();
			District d = new District(rs.getInt("district_id"));
			conn.close();
			return d;
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nel caricamento dei dati dal database");
		}
		
		
		
	}
	
	
	public List<Event> ListEventsDistrettoAnno(int anno, District d) {
		
		String sql = "SELECT * " + 
				"FROM events " + 
				"WHERE YEAR(reported_date)=? AND district_id=?";
		
		List<Event> risultato = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, d.getId());
			
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				risultato.add(new Event(res.getLong("incident_id"),
						res.getInt("offense_code"),
						res.getInt("offense_code_extension"), 
						res.getString("offense_type_id"), 
						res.getString("offense_category_id"),
						res.getTimestamp("reported_date").toLocalDateTime(),
						res.getString("incident_address"),
						res.getDouble("geo_lon"),
						res.getDouble("geo_lat"),
						res.getInt("district_id"),
						res.getInt("precinct_id"), 
						res.getString("neighborhood_id"),
						res.getInt("is_crime"),
						res.getInt("is_traffic")));
			}
			conn.close();
			return risultato;
			
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nel caricamento dati del database");
		}
	}
	
	public List<District> ListAllDistretti(){
		
		String sql = "SELECT distinct district_id FROM events ORDER BY district_id";
		List<District> res = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				res.add(new District(rs.getInt("district_id")));
			}
			conn.close();
			return res;
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nel caricamento dati del database");
		}
	}

	


}
