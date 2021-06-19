package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Simulator {

	
	Model m;
	int N;
	int X;
	Map<Team, Integer> reporters;
	Map<Integer, Team> teams;
	List<Match> matches;
	Integer numRepTot;
	Integer numMatches;
	Integer numSottoSoglia;
	
	public void setParam(Model m, int N, int X) {
		this.m=m;
		this.N=N;
		this.X=X;
	}
	
	public void init() {
		reporters=new HashMap<>();
	List<Team> squadre=	m.getVertici();
	for(Team s: squadre) {
		reporters.put(s, this.N);
	}
	teams=this.m.getIdMap();
	this.matches=m.getMatches();
	this.numRepTot=0;
	this.numMatches=0;
	this.numSottoSoglia=0;
	
	}
	
	public void sim() {
		for(Match m: matches) {
			
			Team casa=this.teams.get(m.getTeamHomeID());
			Team ospite=this.teams.get(m.getTeamAwayID());
			int repC=reporters.get(casa);
			int repO=reporters.get(ospite);
			this.numRepTot+=repC+repO;
			this.numMatches++;
			if((repC+repO) <this.X) {
				this.numSottoSoglia++;
			}
			Integer ris=m.getResultOfTeamHome();
			if(ris==1) {
				//ha vinto la squadra di casa
			
				this.gestisciVincente(casa);
				
				//la squadra ospite ha perso
			
				this.gestisciPerdente(ospite);
				
			}else if(ris==-1) {
				//ha vinto la squadra ospite
				
			
				this.gestisciVincente(ospite);
				
				//la squadra di casa ha perso
			
				this.gestisciPerdente(casa);
				
			}
		}
		double media=(this.numRepTot*1.0)/this.numMatches;
		System.out.println("Media: "+media+"\n");
		System.out.println("Num partite sotto soglia: "+this.numSottoSoglia);
	}
	
	
	
	private Team getRandom(List<TeamPesato> list) {
		Random randomizer = new Random();
		TeamPesato random = list.get(randomizer.nextInt(list.size()));
		return random.getT();
	}
	
	private void gestisciVincente(Team t) {
		if(Math.random()*100<=50.0) {
			if(reporters.get(t)>0) {
				List<TeamPesato> migliori=this.m.getMigliori(t);
				if(migliori!=null) {
					//se c'è una squadra piu blasonata, cambia
					Team casuale=this.getRandom(migliori);
					reporters.put(casuale, reporters.get(casuale)+1);
					
					reporters.put(t, reporters.get(t)-1);
				}
			}
			}
	}
	
	private void gestisciPerdente(Team t) {
		if(Math.random()*100<=20.0) {
			if(reporters.get(t)>0) {
			int numRep=(int)Math.random()*reporters.get(t);
			List<TeamPesato> peggiori=this.m.getPeggiori(t);
			if(peggiori!=null) {
				Team casuale=this.getRandom(peggiori);
				reporters.put(casuale, reporters.get(casuale)+numRep);
				
				reporters.put(t, reporters.get(t)-numRep);
			}
			}
		}
	}
}
