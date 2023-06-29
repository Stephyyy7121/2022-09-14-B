package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private ItunesDAO dao;
	private Graph<Album, DefaultEdge> grafo;
	private List<Album> vertici;
	private List<Arco> archi;
	
	private Map<Integer, Album> idMapAlbum;
	private List<Album> componenteConnessa;
	
	//ricorsione
	private List<Album> soluzioneFinale;
	
	public Model() {
		
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		this.dao = new ItunesDAO();
		this.archi = new ArrayList<Arco>();
		this.idMapAlbum = new HashMap<Integer, Album>();
		this.componenteConnessa = new ArrayList<Album>();
	}
	
	public void loadNodes(double durata) {
		
		if (this.vertici.isEmpty())  {
			this.vertici = this.dao.getVertici(durata);
		}
	}
	
	public void clearGraph() {
		
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		this.vertici = new ArrayList<Album>();
	}
	
	public void creaGrafo(double durata) {
		
		clearGraph();
		loadNodes(durata);
		
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		if(this.idMapAlbum.isEmpty()) {
			for (Album a : this.vertici) {
				this.idMapAlbum.put(a.getAlbumId(), a);
			}
		}
		
		//archi 
		if(this.archi.isEmpty()) {
			this.archi = this.dao.getArchi();
		}
		
		for (Arco a : this.archi) {
			Album a1 = this.idMapAlbum.get(a.getA1());
			Album a2  =this.idMapAlbum.get(a.getA2());
			
			if (this.vertici.contains(a1) && this.vertici.contains(a2)) {
				
				this.grafo.addEdge(a1, a2);
			}
			
		}
		
		//aggiungere num di track all'oggetto Album
		List<Integer> numTrackAlbum = this.dao.getNumTrackAlbum();
		for (int i = 0; i < numTrackAlbum.size(); i++) {
			for (Album a : this.vertici) {
				if (a.getAlbumId() == i) {
					a.setNumTrack(numTrackAlbum.get(i));
				}
			}
		}
		
	}
	
	public Map<Integer,Album> getIdMapAlbum() {
		return this.idMapAlbum;
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Album> getVertici() {
		List<Album> ver = new ArrayList<Album>(this.vertici);
		Collections.sort(ver);
		return ver;
	}
	
	//metodo per ottenere tutti i nodi adiacenti --> restituisce l'insieme dei nodi adiacenti
	public List<Album> getComponente(Album a1) {
		/*ConnectivityInspector<Album, DefaultEdge> ci =
				new ConnectivityInspector<>(this.grafo) ;
		Set<Album> connessa = ci.connectedSetOf(a1);
		this.componenteConnessa.addAll(connessa) ;*/
		this.componenteConnessa = Graphs.neighborListOf(this.grafo, a1);
		this.componenteConnessa.add(a1);
		
		return this.componenteConnessa ;
	}
	
	//meotodo per calcolare il numero complessivo di brani di tutti i brani appartenenti alla componente
	public int numeroBrani(List<Album> connessa) {
		
		int num = 0;
		
		for (Album a : connessa) {
			num += a.getNumTrack();
		}
		
		return num;
	}
	
	
	
	//RICORSIONE 
	
	public List<Album> getAlbumFinale(double dTOT, Album a1) {
		
		//lista soluzione possibile
		List<Album> connessaList = new ArrayList<>();
		ConnectivityInspector<Album, DefaultEdge> ci =
				new ConnectivityInspector<>(this.grafo) ;
		Set<Album> connessa = ci.connectedSetOf(a1);
		connessaList.add(a1);
		connessa.remove(a1);
		connessaList.addAll(connessa);
		

		//inizializzare 
		this.soluzioneFinale = new ArrayList<Album>();
		
		//parziale
		List<Album> parziale = new ArrayList<Album>();
		parziale.add(a1);
		
		
		ricorsione(parziale, connessaList, dTOT, 1);
		
		return soluzioneFinale;
	}
	
	private void ricorsione(List<Album> parziale, List<Album> connessa, double dTOT, int livello) {
		
		//condizione di terminazione 
		if (sommaParziale(parziale) == dTOT) {
			this.soluzioneFinale = new ArrayList<Album>(parziale);
			return ;
		}
		
		if (parziale.size() > soluzioneFinale.size()) {
			this.soluzioneFinale = new ArrayList<Album>(parziale);
			return ;
		}
		if (livello == connessa.size()) {
			return;
		}
		
		/*for (Album a : connessa) {
			if (!parziale.contains(a)) {
				parziale.add(a);
				ricorsione(parziale, connessa, dTOT, livello +1);
				parziale.remove(parziale.size()-1);
				
			}
		}*/
		parziale.add(connessa.get(livello));
		ricorsione(parziale, connessa, dTOT, livello+1);
		parziale.remove(parziale.size()-1);
		livello--;
	}
	
	public int sommaParziale(List<Album> parziale) {
		
		int somma = 0;
		for (Album a : parziale) {
			somma += a.getDurata();
		}
		
		
		return somma;
	}
}
