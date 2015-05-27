package db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Repr�sente les donn�es qui sont trait�es
 * Par exemple les noms et les journaux
 * @author Remi et Julien
 */
public class TreatedData {
	/**
	 * Map associant une donn�es � son nombre d'apparition dans la base de donn�es
	 */
	private Map<String, Integer> treatedData;
	/**
	 * Map associant une donn�e non trait�e � sa donn�e trait� qui apparaitra dans la base de donn�es
	 */
	private Map<String, String> toChange;

	/**
	 * Constructeur
	 */
	public TreatedData() {
		this.treatedData = new TreeMap<>();;
		this.toChange = new TreeMap<>();
	}

	/**
	 * Recup�re les donn�es et leur nombre d'apparition
	 * @return Les donn�es et leur nombre d'apparition
	 */
	public Map<String, Integer> getTreatedData() {
		return treatedData;
	}

	/**
	 * Charge les donn�es et leur nombre d'apparition
	 * @param treatedData Les donn�es et leur nombre d'apparition
	 */
	public void setTreatedData(Map<String, Integer> treatedData) {
		this.treatedData = treatedData;
	}

	/**
	 * R�cup�re les changements � r�aliser
	 * @return Les changement
	 */
	public Map<String, String> getToChange() {
		return toChange;
	}

	/** 
	 * Charge les changements r�alis�s
	 * @param toChange Les changements r�alis�s
	 */
	public void setToChange(TreeMap<String, String> toChange) {
		this.toChange = toChange;
	}

	/**
	 * Ajoute les changements contenus dans un fichier csv
	 * @param path Le chemin du fichier csv � charger
	 * @throws IOException
	 */
	public void loadToChange(String path) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(path));
		String tmp;
		String [] splitedLine;
		while ((tmp=br.readLine())!=null ) {
			if(tmp.contains(";") && tmp.charAt(0) != ';'){
				splitedLine = tmp.split(";");
				if(splitedLine.length == 2){
					if(splitedLine[0].contains(">")){
						String[] splitedWrong = splitedLine[0].split(">");
						for (int i = 0; i < splitedWrong.length; i++) {
							toChange.put(splitedWrong[i], splitedLine[1]);
						}
					}else{
						toChange.put(splitedLine[0], splitedLine[1]);
					}
				}
			}
		}
		this.uniformToChange();
	}

	public void saveToChange(String path) throws FileNotFoundException{
		Map <String, List<String>> toChangeByRight = this.getFinalTab();
		File csvFile = new File(path);
		PrintStream printstream = new PrintStream(new FileOutputStream(csvFile)); 

		for (String right : toChangeByRight.keySet()) {
			String buffer = "";
			for (String wrongs : toChangeByRight.get(right)) {
				buffer += wrongs + ">";
			}
			buffer = buffer.substring(0,buffer.length() - 1);
			buffer += ";"+right +"\n";
			printstream.print(buffer);
		}
	}

	/**
	 * Uniformise les changements
	 * Par exemple si on a S1->S2 et S2->S3 on fait S1->S3
	 * Attention peut g�n�rer des erreurs si on a S1->S2 et S2->S1
	 */
	public void uniformToChange (){
		for (String originalNames : toChange.keySet()) {
			while(toChange.containsKey(toChange.get(originalNames))){
				toChange.put(originalNames, toChange.get(toChange.get(originalNames)));
			}
		}
	}

	/**
	 * Recup�re une map associant une donn�e trait�e � toutes les donn�es non trait�es qui seront remplac�es
	 * @return Une map associant une donn�e trait�e � toutes les donn�es non trait�es qui seront remplac�es
	 */
	public Map<String,List<String>>  getFinalTab() {
		Map<String,List<String>> displayedData = new TreeMap<>();
		for (String originalData : this.toChange.keySet()){
			if(displayedData.containsKey(this.toChange.get(originalData))){
				displayedData.get(this.toChange.get(originalData)).add(originalData);
			}else{
				List<String> listNames = new ArrayList<>();
				listNames.add(originalData);
				displayedData.put(this.toChange.get(originalData), listNames);
			}
		}
		return displayedData;
	}

	/**
	 * R�cup�re une map associant une donn�e � son nombre d'apparition ordonn�e par ordre d'apparition
	 * @return Une map associant une donn�e � son nombre d'apparition ordonn�e par ordre d'apparition
	 */
	public Map<String, Integer> orderByValues(){
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(this.treatedData.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;			
	}
}
