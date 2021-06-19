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
	Integer numRepTot=0;
	Integer numMatches=0;
	Integer numSottoSoglia=0;
	
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
				if(Math.random()*100<=50.0) {
				if(reporters.get(casa)>0) {
					List<TeamPesato> migliori=this.m.getMigliori(casa);
					if(migliori!=null) {
						//se c'è una squadra piu blasonata, cambia
						Team casuale=this.getRandom(migliori);
						reporters.put(casuale, reporters.get(casuale)+1);
						
						reporters.put(casa, reporters.get(casa)-1);
					}
				}
				}
				//la squadra ospite ha perso
				if(Math.random()*100<=20.0) {
					if(reporters.get(ospite)>0) {
					int numRep=(int)Math.random()*reporters.get(ospite);
					List<TeamPesato> peggiori=this.m.getPeggiori(ospite);
					if(peggiori!=null) {
						Team casuale=this.getRandom(peggiori);
						reporters.put(casuale, reporters.get(casuale)+numRep);
						
						reporters.put(ospite, reporters.get(ospite)-numRep);
					}
					}
				}
				
			}else if(ris==-1) {
				//ha vinto la squadra ospite
				//la squadra di casa ha perso
				if(Math.random()*100<=20.0) {
					if(reporters.get(casa)>0) {
					int numRep=(int)Math.random()*reporters.get(casa);
					List<TeamPesato> peggiori=this.m.getPeggiori(casa);
					if(peggiori!=null) {
						Team casuale=this.getRandom(peggiori);
						reporters.put(casuale, reporters.get(casuale)+numRep);
						
						reporters.put(casa, reporters.get(casa)-numRep);
					}
					}
				}
				
				if(Math.random()*100<=50.0) {
					if(reporters.get(ospite)>0) {
						List<TeamPesato> migliori=this.m.getMigliori(ospite);
						if(migliori!=null) {
							//se c'è una squadra piu blasonata, cambia
							Team casuale=this.getRandom(migliori);
							reporters.put(casuale, reporters.get(casuale)+1);
							
							reporters.put(ospite, reporters.get(ospite)-1);
						}
					}
					}
				
			}
		}
		double media=this.numRepTot/this.numMatches;
		System.out.println("Media: "+media+"\n");
		System.out.println("Num partite sotto soglia: "+this.numSottoSoglia);
	}
	
	
	
	private Team getRandom(List<TeamPesato> list) {
		Random randomizer = new Random();
		TeamPesato random = list.get(randomizer.nextInt(list.size()));
		return random.getT();
	}
}
