package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void loadAllCountries(Map<Integer,Country>idMap,int anno) {
		
		
		//String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		//List<Country> result = new ArrayList<Country>();
		String sql="SELECT DISTINCT c.state1no, co.StateAbb, co.StateNme "
				+ "FROM contiguity AS c,country AS co "
				+ "WHERE (co.CCode=c.state1no) AND (YEAR<=? AND conttype=1)";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1,anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				//System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
				if(!idMap.containsKey(rs.getInt("state1no")))
				idMap.put(rs.getInt("state1no"), new Country(rs.getInt("state1no"),rs.getString("StateAbb"),rs.getString("StateNme")));
			}
			
			conn.close();
			//return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(Map<Integer,Country>idMap,int anno) {
		
		boolean controllo;
		
		String sql="SELECT state1no,state2no "
				+ "FROM contiguity "
				+ "WHERE year<=? AND conttype=1";
		//System.out.println("TODO -- BordersDAO -- getCountryPairs(int anno)");
		List<Border> result=new ArrayList<Border>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1,anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				controllo=true;
				//System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
				for(Border b: result) {
					if((b.getC1().getCcode()==rs.getInt("state1no") && b.getC2().getCcode()==rs.getInt("state2no")) || (b.getC1().getCcode()==rs.getInt("state2no") && b.getC2().getCcode()==rs.getInt("state1no"))) {
						controllo=false;
					}
				}
				if(controllo) {
					Country c1=idMap.get(rs.getInt("state1no"));
					Country c2=idMap.get(rs.getInt("state2no"));
					result.add(new Border(c1,c2));
				}
			}
			
			conn.close();
			//return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		return result;
		
	}
}
