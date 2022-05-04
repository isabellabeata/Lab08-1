package it.polito.tdp.extflightdelays.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;


public class Model {
	
	private ExtFlightDelaysDAO dao= new ExtFlightDelaysDAO();
	private Graph <Airport,DefaultWeightedEdge > grafo;
	List<Airport> aeroporti= new LinkedList<Airport>(this.dao.loadAllAirports());
	Map<Integer, Airport> map;
	public Model() {
		map= new HashMap<Integer, Airport>();
		for(Airport a: aeroporti) {
			map.put(a.getId(), a);
		}
				
	}

	
	public String creaGrafo(Integer distanza) {
		String s="";
		this.grafo= new SimpleWeightedGraph<Airport,DefaultWeightedEdge >(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, aeroporti);
		System.out.println(this.grafo);
		String s1= ("Vertici = "+this.grafo.vertexSet().size()+"\n");
		List<CoppiaId> coppie = new LinkedList<CoppiaId>(this.dao.getAllMedieTratte(distanza));
		for(Airport partenza : aeroporti) {
			for(Airport arrivo: aeroporti) {
				for(CoppiaId c1: coppie){
					if(c1.getIdPartenza()==partenza.getId() && c1.getIdArrivo()==arrivo.getId()) {
					this.grafo.addEdge(partenza, arrivo);
					this.grafo.setEdgeWeight(partenza, arrivo, c1.getMedia());
					}
				}
		
			}
			
		}
		String s2= ("Archi = "+this.grafo.edgeSet().size()+"\n");
		String s3="";
		for(CoppiaId c: coppie) {
			s3+= "Partenza: "+map.get(c.getIdPartenza()).getAirportName()+" Arrivo: "+ map.get(c.getIdArrivo()).getAirportName()+" Distanza: "+c.getMedia()+"\n";
		}
		s=s1+s2+s3;
		return s;
		
	}
	

}
