package db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import tools.StringTool;

/**
 * Permet de traiter et de nettoyer une base de donn�es
 * @author Remi et Julien
 */
public class TreatData {
	
	/**
	 * La base de donn�es
	 */
	private DataBase db;

	/**
	 * Constructeur
	 * @param data Les donn�es
	 */
	public TreatData(String[][] data){
		this.db = new DataBase(data);
	}
	
	/**
	 * R�cup�re la base de donn�es
	 * @return La base de donn�es
	 */
	public DataBase getDB(){
		return this.db;
	}
	
	/**
	 * Nettoie avec des m�thodes automatiques les donn�es
	 * @return Les donn�es n�ttoy�es
	 */
	public List<TreatedData> automaticalClean(){
		List<TreatedData> listTreated = new ArrayList<TreatedData>();
		
		listTreated.add(deleteSpace(uniformCase(getTreatedData(db.getNames()))));
		listTreated.add(deleteSpace(uniformCase(getTreatedData(db.getJournals()))));

		return listTreated;
	}
	
	/**
	 * Uniforme la casse des donn�es entr�es en param�tre
	 * On met la premiere lettre de chaques mot en Majuscule, les autres en minuscule
	 * @param treatedData Les donn�es � traiter
	 * @return Les donn�es trait�es
	 */
	public TreatedData uniformCase(TreatedData treatedData){
		TreeMap<String, Integer> tmpNames = new TreeMap<String, Integer>();
		StringTool tool = new StringTool();

		for (String name : treatedData.getTreatedData().keySet()) {
			String goodCase = tool.uniformCase(name);

			if(!name.equals(goodCase)){
				treatedData.getToChange().put(name, goodCase);
			}

			if(treatedData.getTreatedData().containsKey(goodCase)){
				tmpNames.put(goodCase, treatedData.getTreatedData().get(name) + treatedData.getTreatedData().get(goodCase));
			}else{
				tmpNames.put(goodCase, treatedData.getTreatedData().get(name));
			}
		}
		treatedData.setTreatedData(tmpNames);
		treatedData.uniformToChange();
		return treatedData;
	}
	
	/**
	 * Instancie un objet TreatedData � partir d'une map contenant les donn�es brutes
	 * Un premier nettoyage est r�alis�, on enl�ve la ponctuation.
	 * @param treatedDataMap Les donn�es brutes
	 * @return Les donn�es trait�es une premi�re fois
	 */
	public TreatedData getTreatedData(Map<String, Integer> treatedDataMap){
		TreatedData treatedData = new TreatedData();

		StringTool tool = new StringTool();
		String firstCleanData = "";
		for (String name : treatedDataMap.keySet()) {
			firstCleanData = tool.removeUnwantedChar(name);
			firstCleanData= tool.removeMonth(firstCleanData);
			firstCleanData = tool.removeColon(firstCleanData);

			if(!firstCleanData.isEmpty() && !tool.containsOnlySpace(firstCleanData)){
				if(!treatedData.getToChange().containsKey(name) && !name.equals(firstCleanData)){
					treatedData.getToChange().put(name, firstCleanData);
				}

				if(!treatedData.getTreatedData().containsKey(firstCleanData)){
					treatedData.getTreatedData().put(firstCleanData, treatedDataMap.get(name));
				}else{
					treatedData.getTreatedData().put(firstCleanData, treatedData.getTreatedData().get(firstCleanData) + treatedDataMap.get(name));
				}
			}
		}
		treatedData.uniformToChange();
		return treatedData;
	}


	/**
	 * Traite les donn�es en supprimant les espaces pour d�t�cter des donn�es similaire
	 * Par exemple Von neumann et VonNeumann seront r�unnis
	 * @param treatedData Les donn�es � traiter
	 * @return Les donn�es trait�s
	 */
	public TreatedData deleteSpace(TreatedData treatedData){
		Map<String, List<String>> unspaced = new HashMap<>();
		String noSpace = "";

		//On parcours les noms
		for (String data : treatedData.getTreatedData().keySet()) {
			//On enleve les espaces
			noSpace = data.replace(" ", "").toLowerCase();
			//Si le nom sans espace a d�j� �t� rencontr�
			if(unspaced.containsKey(noSpace)){
				if(!unspaced.get(noSpace).contains(data))
					unspaced.get(noSpace).add(data);
			}else{
				unspaced.put(noSpace, new ArrayList<String>());
				unspaced.get(noSpace).add(data);
			}
		}

		for (String dataWithoutSpace : unspaced.keySet()) {
			if(unspaced.get(dataWithoutSpace).size()>1){
				String mostSeen = "";
				int maxSeen = 0;
				for (String similarData : unspaced.get(dataWithoutSpace)) {
					if(treatedData.getTreatedData().get(similarData)>maxSeen){
						maxSeen = treatedData.getTreatedData().get(similarData);
						mostSeen = similarData;
					}
				}

				for (String similarData : unspaced.get(dataWithoutSpace)) {
					if(!similarData.equals(mostSeen)){
						treatedData.getToChange().put(similarData, mostSeen);

						treatedData.getTreatedData().put(mostSeen,treatedData.getTreatedData().get(similarData) + treatedData.getTreatedData().get(mostSeen));
						treatedData.getTreatedData().remove(similarData);
					}
				}
			}
		}
		treatedData.uniformToChange();
		return treatedData;
	}


	/**
	 * Cr�e un csv regroupant les noms dont le d�but est identique
	 * L'utilisateur peut ensuit modifier le nom comme il le souhaite 
	 * @param parameter Le niveau de regroupement
	 * @param treatedData Les donn�es � traiter
	 * @throws IOException
	 */
	public void manualClean(int parameter, TreatedData treatedData, String path) throws IOException{
		String curName = "";
		BufferedWriter bw = new BufferedWriter(new FileWriter(path));
		int cpt = 0;
		for (String name : treatedData.getTreatedData().keySet()) {
			if(name.length()>parameter){
				if(curName.equals("") || !name.substring(0, parameter).equals(curName.substring(0, parameter))){
					if(cpt>1){
						bw.write((curName+";\n\n"));
					}
					cpt = 1;
					curName = name;

				}else{
					bw.write(name + ";\n");
					cpt ++;
				}
			}
		}
		bw.close();
	}

	/**
	 * Charge le csv manualClean puis effectue les changements apport�s dans les donn�es
	 * @param treatedData Les donn�es o� seront fait le traitement manuel
	 * @throws IOException
	 */
	public void loadManualClean(TreatedData treatedData) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("manualClean.csv"));
		String tmp;
		String[] csvData;

		while ((tmp=br.readLine())!=null ) {
			if(!tmp.contains(";;") && tmp.contains(";")){
				csvData = tmp.split(";");
				//MAJ des modifications dans toChange
				treatedData.getToChange().put(csvData[0], csvData[1]);

				//MAJ des modifications dans names
				if(treatedData.getTreatedData().containsKey(csvData[1])){
					treatedData.getTreatedData().put(csvData[1],treatedData.getTreatedData().get(csvData[0]) + treatedData.getTreatedData().get(csvData[1]));
				}else{
					treatedData.getTreatedData().put(csvData[1],treatedData.getTreatedData().get(csvData[0]));
				}
				treatedData.getTreatedData().remove(csvData[0]);
			}
		}
		br.close();
	}

	/**
	 * Charge les modifications qui ont �t� faites dans les donn�es trait�es
	 * @param treatedList La liste des donn�es qui ont �t� trait�es
	 */
	public void loadToChange(List<TreatedData> treatedList){
		TreatedData names = treatedList.get(0);
		TreatedData journals = treatedList.get(1);
		
		//On parcours les donn�es
		for(int i = 0; i<db.getData().length;i++){
			
			//On g�re les Auteurs
			if(!db.getData()[i][0].equals("null")){
				String [] authorNames = db.getData()[i][0].split(">");
				String treatedNames = "";
				//On parcours chaque autheurs d'une publication
				for(int j = 0; j < authorNames.length;j++){
					//Si le noms doit etre remplac�
					if(names.getToChange().containsKey(authorNames[j])){
						treatedNames += names.getToChange().get(authorNames[j]);
					}else{
						treatedNames += authorNames[j] ;
					}
					//On ajoute le d�limitateur ">"
					if(j != authorNames.length - 1){
						treatedNames += ">";
					}
				}
				//On �crit le nom des auteurs trait�s dans la base de donn�es
				db.getData()[i][0] = treatedNames;
			}
			
			//On g�re les cr�dits
			if(!db.getData()[i][4].equals("null")){
				String [] creditsCases = db.getData()[i][4].split(">");
				String treatedCreditsCases = "";
				
				//On parcours chaque citation
				for(int j = 0; j<creditsCases.length;j++){
					String[] credits = creditsCases[j].split(",");
					String treatedCredits = "";
					
					//On parcours chaque champs de la citation
					for(int k = 0; k<credits.length;k++){
						//Si Premier champs (les noms) et qu'il faut modifier
						if(k == 0 &&  names.getToChange().containsKey(credits[k])){
								treatedCredits += names.getToChange().get(credits[k]);
						// Si Troisi�me champs (les journaux) et qu'il faut modifier
						}else if(k == 2 && journals.getToChange().containsKey(credits[k])){
								treatedCredits += journals.getToChange().get(credits[k]);
						//Sinon on r�ecrit tel quel
						}else{
							treatedCredits += credits[k];
						}
						
						//Ajout des d�limitateurs ","
						if(k!= creditsCases.length - 1){
							treatedCredits += ",";
						}
					}
					treatedCreditsCases += treatedCredits;
					//On ajoute le d�limitateur ">"
					if(j != creditsCases.length - 1){
						treatedCreditsCases += ">";
					}
				}
				//On �crit les cr�dits trait�s dans la base de donn�es
				db.getData()[i][4] = treatedCreditsCases;
			}
		}
	}
	
}
