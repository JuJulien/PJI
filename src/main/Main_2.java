package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import csv.CsvImport;
import csv.CsvMaker;
import db.TreatedData;
import db.TreatData;

/**
 * Effectue le nettoyage automatique d'un jeu de donn�es
 * @author Remi et Julien
 */
public class Main_2 {

	public static void main(String[] args) throws IOException {
		CsvMaker cm = new CsvMaker("data");

		//Importe les donn�es depuis les fichiers html et cr�e un csv les contenants
		cm.build();

		CsvImport ci = new CsvImport("db.csv");
		TreatData treater = new TreatData(ci.importFile());

		//Effectue le traitement automatique des donn�es
		List<TreatedData> namesAndJournals = treater.automaticalClean();
		//Parcours des donn�es trait�es
		for(int i = 0; i<namesAndJournals.size(); i++){
			TreatedData treatedData = namesAndJournals.get(i);

			//On ecrit ce qui va etre chang� dans un fichier
			Map<String, List<String>> displayToChange = treatedData.getFinalTab();
			BufferedWriter bw = new BufferedWriter(new FileWriter("displayChanges_" + i + ".txt"));

			for (String iterable : displayToChange.keySet()) {
				List<String> list =  displayToChange.get(iterable);
				bw.write("Name : " + iterable + "\n");
				for (String string : list) {
					bw.write(" ---> " + string + "\n");
				}
			}
			bw.close();


			//On ecrit la liste des donn�es trait�s dans un autre fichier
			BufferedWriter bw2 = new BufferedWriter(new FileWriter("treatedData_" + i + ".txt"));

			for (Iterator<String> iterator = treatedData.getTreatedData().keySet().iterator(); iterator.hasNext();) {
				String cur = iterator.next();
				bw2.write("Nom : " + cur + " : " +treatedData.getTreatedData().get(cur) + "\n");
			}
			bw2.close();

		}

		//On charge les changements � effectuer apr�s le traitement des donn�es
		treater.loadToChange(namesAndJournals);

		//On �crit les changement dans un le csv
		cm.makeCSV(treater.getDB().getData());
	}

}
