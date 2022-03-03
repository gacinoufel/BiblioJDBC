package bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * objet qui fait la liaison avec la base de données pour accéder aux données de type Livre
 * nécessite une connexion pour s'initialiser
 */

@SuppressWarnings("unused")
public class ManagerLivre {
	private Connection connexion;
	public List<Livre> listLivre= new ArrayList<Livre>() ;
	private PreparedStatement  rechercherLesLivres;
	
	/**
	 * associer la connexion permet d'initialiser les preparedStaement
	 * 
	 */
	public void setConnection (Connection c) throws AppliException {
try{ 
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			this.connexion=c; 
			String query1="SELECT tp_livre.*, tp_personne.id, tp_personne.nom, tp_personne.prenom FROM tp_livre left JOIN tp_personne ON tp_personne.id = tp_livre.id_emprunte ORDER BY tp_livre.titre";

			this.rechercherLesLivres=this.connexion.prepareStatement(query1); 
			
			
			}catch(Exception e){ 
				e.printStackTrace(); 
			} 
				
	}
	
	/** 
	 * retourne la liste de tous les livres tries par titre
	 * a chaque livre sont associes son emprunteur et la personne qui a reserve
	 */
	
	public List <Livre> getLesLivres() throws AppliException { 
		try {
			ResultSet rs=this.rechercherLesLivres.executeQuery(); 
			while(rs.next()) { 
				//ORM Object Relationnel Mapping 
				Livre l= new Livre(rs.getInt(1),rs.getString(2)) ; 
				Personne pEmp=new Personne(rs.getInt(3),rs.getString(6),rs.getString(7)); 
				 //je separe les 2 types de personne 
				l.setEmprunte(pEmp) ;  
				Personne pResv=new Personne(rs.getInt(4),rs.getString(6),rs.getString(7));
				l.setReserve(pResv); 
				this.listLivre.add(l) ; 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listLivre;
		
	}
}
