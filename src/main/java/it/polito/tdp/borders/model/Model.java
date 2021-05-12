package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	private BordersDAO dao;
	private Graph<Country,DefaultEdge>grafo;	
	private Map<Integer,Country>idMap;
	private Map<Country,Country> predecessore;
	private List<Country> visitate;
	
	public Model() {
		dao=new BordersDAO();
		
	}
	public void creaGrafo(int anno) {
		idMap=new HashMap<Integer,Country>();
		grafo=new SimpleGraph<Country,DefaultEdge>(DefaultEdge.class);
		
		dao.loadAllCountries(idMap,anno);
		Graphs.addAllVertices(grafo,idMap.values());
		for(Border b : dao.getCountryPairs(idMap,anno)) {
		this.grafo.addEdge( b.getC1(), b.getC2());
		}
	}
	
	public Set<Country> getCountries(){
		return grafo.vertexSet();
		/*
		List<String> result=new LinkedList<String>();
		for(Country c:idMap.values()) {
			result.add(c.getNomeCom());
		}
		return result;
		*/
	}
	
	public Map<Country,Integer> getCountryCounts(){
		HashMap<Country,Integer> result=new HashMap<Country,Integer>();
		for(Country c:grafo.vertexSet()) {
			result.put(c, this.grafo.degreeOf(c));
		}
		return result;
	}
	
	public int getNArchi() {
		return grafo.edgeSet().size();
	}
	
	public int getNVertici() {
		return grafo.vertexSet().size();
	}
	public int getNumberOfConnectedComponents() {
		ConnectivityInspector<Country,DefaultEdge> ci=new ConnectivityInspector(grafo);
		return ci.connectedSets().size();
	}
	
	public List<Country> fermateRaggiungibili(Country partenza) {
		//AMPIEZZA
		BreadthFirstIterator<Country,DefaultEdge>bfv=new BreadthFirstIterator<>(this.grafo,partenza);
	
		this.predecessore=new HashMap<>();
		this.predecessore.put(partenza, null);
		
		bfv.addTraversalListener(new TraversalListener<Country,DefaultEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
				DefaultEdge arco=e.getEdge();
				Country a=grafo.getEdgeSource(arco);
				Country b=grafo.getEdgeTarget(arco);
				//ho scoperto 'a' arrivando da 'b' (se 'b' lo conoscevo) b->a
				if(predecessore.containsKey(b) && !predecessore.containsKey(a)) {
					predecessore.put(a, b);
					//System.out.println(a+" scoperto da "+b);
				}else if(predecessore.containsKey(a) && !predecessore.containsKey(b)){
					// di sicuro conoscevo 'a' e quindi ho scoperto 'b'
					predecessore.put(b, a);
					//System.out.println(b+" scoperto da "+a);
				}
				
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Country> e) {
				/*
				//System.out.println(e.getVertex());
				Fermata nuova=e.getVertex();
			//Fermata precedente = vertice adiacente a 'nuova' che sia già raggiunto
					//(cioè è già presente nella key della mappa);
				predecessore.put(nuova,precedente);
				*/
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Country> e) {
				// TODO Auto-generated method stub
				
			}});
		
		List<Country> result=new ArrayList<>();
		
		//PROFONDITA'
		//DepthFirstIterator<Fermata,DefaultEdge>dfv=new DepthFirstIterator<>(this.grafo,partenza);
		
		while(bfv.hasNext()) {
			Country f=bfv.next();
			result.add(f);
		}
		
		return result;
	}
	
	public List<Country> fermateRaggiungibiliRicorsivo(Country partenza){
		visitate=new ArrayList<Country>();
		List<Country>daVisitare=new ArrayList<Country>();
		daVisitare.add(partenza);
		cerca(daVisitare,0);
		return visitate;
	}
	private void cerca( List<Country> daVisitare, int L) {
		
		//caso terminale
		if(daVisitare.size()==visitate.size()) {
			return ;
		}
		
		//altrimenti
		Country c=daVisitare.get(L);
		Set<DefaultEdge>archi=this.grafo.edgesOf(c);
		
		for (DefaultEdge a: archi) {
			if((!visitate.contains(this.grafo.getEdgeSource(a)) && !daVisitare.contains(this.grafo.getEdgeSource(a)) && !c.equals(this.grafo.getEdgeSource(a)))){
				daVisitare.add(this.grafo.getEdgeSource(a));
			}
			if((!visitate.contains(this.grafo.getEdgeTarget(a)) && !daVisitare.contains(this.grafo.getEdgeTarget(a)) && !c.equals(this.grafo.getEdgeTarget(a)))){
				daVisitare.add(this.grafo.getEdgeTarget(a));
			}
		}
		
		if(!visitate.contains(c)) {
			visitate.add(c);
		}
		
		cerca(daVisitare,L+1);
	}

}
