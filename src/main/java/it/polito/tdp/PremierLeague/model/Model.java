package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private Graph<Team, DefaultWeightedEdge> graph;
	private PremierLeagueDAO dao;
	private Map<Integer, Team> idMap;
	private List<Team> teams;
	
	public Model() {
		dao=new PremierLeagueDAO();
		
	}
	
	public String creaGrafo() {
		graph=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		idMap=new HashMap<>();
		dao.listAllTeams(idMap);
		Graphs.addAllVertices(graph, idMap.values());
		System.out.println(graph.vertexSet().size());
		teams=new ArrayList<>(idMap.values());
		for(Team t: teams) {
			Integer id=t.getTeamID();
			Integer puntiC= dao.getPuntiCasa(t);
			Integer puntiT=dao.getPuntiTrasf(t);
			t.setPunti(puntiC+puntiT);
		}
		System.out.println(teams);
		for(Team t: teams) {
			for(Team t2: teams) {
				Integer peso=t.getPunti()-t2.getPunti();
				if(peso>0) {
					Graphs.addEdge(graph, t, t2, peso);
				}
			}
		}
		System.out.println(graph.edgeSet().size());
		return "num vertici: "+this.graph.vertexSet().size()+" numArchi: "+graph.edgeSet().size();
	}
	
	public List<Team> getVertici(){
		List<Team> result=new ArrayList<>(this.teams);
		Collections.sort(result);
		return result;
	}
	
	public List<TeamPesato> getMigliori(Team t){
		List<TeamPesato> result=new ArrayList<>();
		List<Team> migliori=Graphs.predecessorListOf(graph, t);
		if(migliori.size()>0) {
		for(Team s: migliori) {
			result.add(new TeamPesato(s, graph.getEdgeWeight(graph.getEdge(s, t))));
		}
		Collections.sort(result);
		return result;
		}
		return null;
	}
	
	
	public List<TeamPesato> getPeggiori(Team t){
		List<TeamPesato> result=new ArrayList<>();
		List<Team> peggiori=Graphs.successorListOf(graph, t);
		if(peggiori.size()>0) {
		for(Team s: peggiori) {
			result.add(new TeamPesato(s, graph.getEdgeWeight(graph.getEdge(t, s))));
		}
		Collections.sort(result);
		return result;
		}
		return null;
	}
	
	public Map<Integer, Team> getIdMap(){
		return this.idMap;
	}
	
	public List<Match> getMatches(){
		return this.dao.listAllMatches();
	}
	public void avviaSim(int N, int X) {
		Simulator s=new Simulator();
		s.setParam(this, N, X);
		s.init();
		s.sim();
	}
}
