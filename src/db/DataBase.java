package db;

import java.util.Map;
import java.util.TreeMap;

import tools.StringTool;

/**
 * Repr�sente une base de donn�es o� les donn�es sont stock�es dans un tableau de chaine de caract�re
 * @author Remi et Julien
 */
public class DataBase {
	/**
	 * Les donn�es
	 */
	private String[][] data;

	/**
	 * Constructeur
	 * @param data Les donn�es
	 */
	public DataBase(String[][] data){
		this.data = data;
	}
	/**
	 * R�cup�re les donn�es
	 * @return les donn�es
	 */
	public String[][] getData() {
		return this.data;
	}
	
	/**
	 * R�cup�re les noms des personnes pr�sentes dans les colonnes Author et Credit et le nombre de fois qu'elles apparaissent
	 * @return Une map associant les noms avec le nombre de fois qu'ils apparaissent
	 */
	public Map<String, Integer> getNames(){
		Map<String, Integer> names = new TreeMap<>();
		
		StringTool tool = new StringTool();

		for(int i = 1; i<data.length; i++){
			//Traitement des AF
			if(!data[i][0].equals("null")){
				String [] authorNames = data[i][0].split(">");
				for(int j = 0; j < authorNames.length;j++){

					if(!authorNames[j].startsWith("&") && !authorNames[j].startsWith("*") && !tool.containsNumber(authorNames[j])){
						
						if(!names.containsKey(authorNames[j])){
							names.put(authorNames[j],1);
						}else{
							names.put(authorNames[j], names.get(authorNames[j]) + 1);
						}
					}
				}
			}

			//Traitement de CR
			if(!data[i][4].equals("null")){
				String [] creditsCase = data[i][4].split(">");
				for(int j = 0; j<creditsCase.length;j++){
					String[] credit = creditsCase[j].split(",");


					if(!credit[0].startsWith("&") && !credit[0].startsWith("*") && !credit[0].startsWith("(") && !tool.containsNumber(credit[0])){
						if(!names.containsKey(credit[0])){
							names.put(credit[0], 1);
						}else{
							names.put(credit[0], names.get(credit[0]) + 1);
						}
					}



				}

			}
		}
		return names;
	}
	
	/**
	 * R�cup�re les journaux pr�sents dans la colonne Credit et le nombre de fois qu'elles apparaissent
	 * @return Une map associant les journaux avec le nombre de fois qu'ils apparaissent
	 */
	public Map<String, Integer> getJournals(){
		Map<String, Integer> journals = new TreeMap<>();
		
		for(int i = 1; i<data.length; i++){
			//Traitement des Credits
			if(!data[i][4].equals("null")){
				String [] creditsCase = data[i][4].split(">");
				for(int j = 0; j<creditsCase.length;j++){
					String[] credit = creditsCase[j].split(",");

					int col = 2;
					if(credit.length< 3){
						col = credit.length - 1;
					}
					if(!journals.containsKey(credit[col])){
						journals.put(credit[col], 1);
					}else{
						journals.put(credit[col], journals.get(credit[col]) + 1);
					}
				}
			}

		}
		return journals;
	}
}
