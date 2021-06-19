package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.Team;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void listAllTeams(Map<Integer, Team> idMap){
		String sql = "SELECT * FROM Teams";
//		List<Team> result = new ArrayList<Team>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Team team = new Team(res.getInt("TeamID"), res.getString("Name"));
				if(!idMap.containsKey(team.getTeamID())) {
					idMap.put(team.getTeamID(), team);
				}
			}
			conn.close();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match> listAllMatches(){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
				result.add(match);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Integer getPuntiCasa(Team t) {
		/*String sql="SELECT t1.cod, t1.risTrasf, t2.risCasa "
				+ "FROM ( "
				+ "SELECT m.ResultOfTeamHome AS cod, COUNT(*) risTrasf "
				+ "FROM matches m "
				+ "WHERE m.TeamAwayID=? "
				+ "GROUP BY m.ResultOfTeamHome) AS t1, "
				+ "( "
				+ "SELECT m1.ResultOfTeamHome AS cod, COUNT(*) AS risCasa "
				+ "FROM matches m1 "
				+ "WHERE m1.TeamHomeID=? "
				+ "GROUP BY m1.ResultOfTeamHome ) AS t2 "
				+ "WHERE t1.cod=t2.cod ";*/
		
		String sql="SELECT m.ResultOfTeamHome AS cod, COUNT(*) AS risCasa "
				+ "FROM matches m "
				+ "WHERE m.TeamHomeID=? AND m.ResultOfTeamHome<>-1 "
				+ "GROUP BY m.ResultOfTeamHome ";
		
		/*String sql2= "SELECT m.ResultOfTeamHome AS cod, COUNT(*) risTrasf "
				+ "FROM matches m "
				+ "WHERE m.TeamAwayID=?  "
				+ "GROUP BY m.ResultOfTeamHome ";*/
				
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, t.getTeamID());
			ResultSet res = st.executeQuery();
			Integer puntiCasa=0;
			while (res.next()) {
				
			Integer codice=	res.getInt("cod");
			
			if(codice==0) {
				puntiCasa+=res.getInt("risCasa");
			}
			else {
				puntiCasa+=res.getInt("risCasa")*3;
			}
				
			}
			conn.close();
			return puntiCasa;
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
				
	}
	
	public Integer getPuntiTrasf(Team t) {
		/*String sql="SELECT t1.cod, t1.risTrasf, t2.risCasa "
				+ "FROM ( "
				+ "SELECT m.ResultOfTeamHome AS cod, COUNT(*) risTrasf "
				+ "FROM matches m "
				+ "WHERE m.TeamAwayID=? "
				+ "GROUP BY m.ResultOfTeamHome) AS t1, "
				+ "( "
				+ "SELECT m1.ResultOfTeamHome AS cod, COUNT(*) AS risCasa "
				+ "FROM matches m1 "
				+ "WHERE m1.TeamHomeID=? "
				+ "GROUP BY m1.ResultOfTeamHome ) AS t2 "
				+ "WHERE t1.cod=t2.cod ";*/
		
		/*String sql1="SELECT m.ResultOfTeamHome, COUNT(*) AS risCasa "
				+ "FROM matches m "
				+ "WHERE m.TeamHomeID=? AND m.ResultOfTeamHome<>-1 "
				+ "GROUP BY m.ResultOfTeamHome ";*/
		
		String sql= "SELECT m.ResultOfTeamHome AS cod, COUNT(*) risTrasf "
				+ "FROM matches m "
				+ "WHERE m.TeamAwayID=?  AND m.ResultOfTeamHome<>1 "
				+ "GROUP BY m.ResultOfTeamHome ";
				
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, t.getTeamID());
			ResultSet res = st.executeQuery();
			Integer puntiT=0;
			while (res.next()) {
				
			Integer codice=	res.getInt("cod");
			
			if(codice==-1) {
				puntiT+=res.getInt("risTrasf")*3;
			}
			else {
				puntiT+=res.getInt("risTrasf");
			}
				
			}
			conn.close();
			return puntiT;
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
				
	}
	
}
