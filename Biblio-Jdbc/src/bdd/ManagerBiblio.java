package bdd;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * objet qui propose tous les services necessaires  pour la gestion de la bibliotheque:
 * operations (emprunter, reserver, restituer)
 */

@SuppressWarnings("unused")
public class ManagerBiblio {
	
	private java.sql.Connection laConnexion ;

	private Statement stmt ; // zone de requete
	private CallableStatement csEmprunter, csRestituer ; // pour les procedures stockees
	

	/**
	 * Constructeur vide 
	 */
	public ManagerBiblio() {
		super();
	}


	/**
	 * la connection passee en parametre permet d'initialiser les objects necessaires 
	 * a la réalisation des opérations : callableStatement,...
	 * @throws AppliException si l'initialisation pose probleme
	 */
	public void setConnection( Connection connection) throws AppliException{
try{ 
			//changement de connexion 
			Class.forName("oracle.jdbc.driver.OracleDriver");
			this.laConnexion=connection; 
			this.stmt = this.laConnexion.createStatement();
			}catch(Exception e){ 
				e.printStackTrace(); 
			} 
		
	};


	/**
	 * @param livre un livre dans la base
	 * @param personne une personne dans la base
	 * @throws AppliException si l'emprunt n'est pas possible :  le livre est déjà  emprunté ou réservé par une autre personne, 
	 * ou la personne a déjà  emprunté 3 livres
	 * Réalise l'emprunt du livre par la personne 
	 */
	public void emprunter(Livre livre, Personne personne) throws AppliException{ 
		int idl=livre.getId() ; 
		int idp=personne.getId(); 
		/*if(isNull(idl) || isNull(idp)) { 
			throw new AppliException (20111) ;
		} */
		try {
			CallableStatement cs=this.laConnexion.prepareCall("{ call GACI.emprunter(?,?) }"); 
			cs.setInt(1, idl);
			cs.setInt(2, idp);
			
		} catch (SQLException e) { 
		throw new  AppliException(e.getErrorCode()); } 
			

		}


	
	
	/**
	 * @param livre un livre dans la base
	 * @throws AppliException si la restitution n'est pas possible : il n'est pas emprunté
	 * Réalise la restitution du livre (en sortie il n'est plus emprunté)
	 */
	public void restituer(Livre livre) throws AppliException{
			int idl=livre.getId() ; 
			/*if(isNull(idl) || isNull(idp)) { 
				throw new AppliException (20111) ;
			} */
			try {
				CallableStatement cs=this.laConnexion.prepareCall("{ call GACI.restituer(?) }"); 
				cs.setInt(1, idl);
				
			} catch (SQLException e) { 
				throw new  AppliException(e.getErrorCode()); } 
				
	}

	
	
	/**
	 * @param livre, un livre dans la base
	 * @param personne, une personne dans la base
	 * @throws AppliException si un des paramètres n'est pas défini
	 * @throws AppliException si la réservation n'est pas possible : le livre est déjà réservé par une autre personne, 
	 * ou il n'est pas emprunté, ou il est déjà  emprunté par cette personne, 
	 * ou la personne a déjà  3 réservations
	 */
	
	 
	public void reserver (Livre livre, Personne personne) throws AppliException {
		int idl=livre.getId() ; 
		int idp=personne.getId() ; 
		if (isNull(idl) || isNull(idp)) { 
			throw new  AppliException(20111);
		} 
		ResultSet resCount;
		try {
			resCount = stmt.executeQuery("select count(*) from TP_livre l  where l.id_reserve="+idp ); 
			int nbEmprunt=resCount.getInt(1) ; 
			if (nbEmprunt>=3) { 
				throw new AppliException(20114) ; 
			} 
			
			ResultSet res2 = stmt.executeQuery("select id_reserve,id_emprunte from TP_livre l  where l.id="+idl );  
			int idResrv=res2.getInt(1) ; 
			int idEmp=res2.getInt(2) ;  
			if (!isNull(idResrv) || idEmp==idp) {  
				throw new AppliException(20115) ; 
			} 
			ResultSet res3 = stmt.executeQuery("select id_emprunte from TP_livre l  where l.id="+idl );  
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block  
		
			throw new AppliException(e.getErrorCode()) ; 
		} 
		
		
		
	}

	public static boolean isNull(Object obj) {
	     return obj == null;
	 }
}
