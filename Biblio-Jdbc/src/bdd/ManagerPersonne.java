package bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * objet qui fait la liaison avec la base de données pour accéder aux données 
 * de type Personne
 * nécessite une connexion pour s'initialiser
 */

@SuppressWarnings("unused")
public class ManagerPersonne {
	
	private Connection connexion;
	
	private PreparedStatement  rechercherLesPersonnes;
	public List<Personne> listPersonne= new ArrayList<Personne> () ; 
	/**
	 * associer la connexion permet d'initialiser les preparedStaement
	 * necesaires aux requetes
	 */
	public void setConnection (Connection c) throws AppliException {
try{ 
			Class.forName("oracle.jdbc.driver.OracleDriver");
			this.connexion=c; 
			String query1="select * from tp_personne order by nom";

			this.rechercherLesPersonnes=this.connexion.prepareStatement(query1) ; 

			}catch(Exception e){ 
				e.printStackTrace(); 
			} 
				
	}
	
	/**
	 *  retourne la liste des personnes ordonnee par nom
	 * declenche AppliException en cas de pb
	 */
	
	public List <Personne> getLesPersonnes () throws AppliException { 
		//ici je suppose que vous etes deja connecter a la base si je pe faire seConnecter 
		try {
			ResultSet rs=this.rechercherLesPersonnes.executeQuery(); 
			while(rs.next()) { 
				Personne p= new Personne(rs.getInt(1),rs.getString(2),rs.getString(3) ) ; 
				 //ajout de personne 
				this.listPersonne.add(p) ; 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return this.listPersonne;
		
	}
}
