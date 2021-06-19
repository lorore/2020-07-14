package it.polito.tdp.PremierLeague.model;

public class TeamPesato implements Comparable<TeamPesato> {

	Team t;
	Double peso;
	public TeamPesato(Team t, Double peso) {
		super();
		this.t = t;
		this.peso = peso;
	}
	public Team getT() {
		return t;
	}
	public void setT(Team t) {
		this.t = t;
	}
	
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "TeamPesato [t=" + t + ", peso=" + peso + "]";
	}
	@Override
	public int compareTo(TeamPesato o) {
		return Double.compare(this.peso, o.getPeso());
	}
	
	
}
