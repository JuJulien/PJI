package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import csv.CsvImport;
import csv.CsvMaker;
import db.TreatData;
import db.TreatedData;

public class Main {

	public static void main(String[] args) throws IOException {

		String str = "0";

		while(Integer.parseInt(str) != 4) {

			System.out.println("1 - Nettoyer une base de donnees html\n");
			System.out.println("2 - Nettoyer la base de donnees csv manuellement\n");
			System.out.println("3 - Requêtes sur la base de donnees\n");
			System.out.println("4 - Quitter\n");

			System.out.print("Choix : \n");
			Scanner sc = new Scanner(System.in);
			str = sc.nextLine();

			if(Integer.parseInt(str) == 1) {
				System.out.print("Chemin d'acces a la base de donnees (html) : ");
				String path = sc.nextLine();
				try{
					CsvMaker cm = new CsvMaker(path);	
					//Importe les données depuis les fichiers html et crée un csv les contenants
					cm.build();
					CsvImport ci = new CsvImport("db.csv");
					TreatData treater = new TreatData(ci.importFile());

					//Effectue le traitement automatique des données
					List<TreatedData> namesAndJournals = treater.automaticalClean();
					//Parcours des données traitées
					for(int i = 0; i<namesAndJournals.size(); i++){
						TreatedData treatedData = namesAndJournals.get(i);

						//On ecrit ce qui va etre changé dans un fichier
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


						//On ecrit la liste des données traités dans un autre fichier
						BufferedWriter bw2 = new BufferedWriter(new FileWriter("treatedData_" + i + ".txt"));

						for (Iterator<String> iterator = treatedData.getTreatedData().keySet().iterator(); iterator.hasNext();) {
							String cur = iterator.next();
							bw2.write("Nom : " + cur + " : " +treatedData.getTreatedData().get(cur) + "\n");
						}
						bw2.close();
					}

					//On charge les changements à effectuer après le traitement des données
					treater.loadToChange(namesAndJournals);

					//On écrit les changement dans un le csv
					cm.makeCSV(treater.getDB().getData());	

					System.out.print("Fin du nettoyage");
				}
				catch(Exception e) {
					System.out.print("\n--> Base de donnees introuvable <--");
				}
			}
			else if(Integer.parseInt(str) == 2) {
				try{
					System.out.print("1 - Nettoyer les auteurs");
					System.out.print("2 - Nettoyer les journaux");
					String choix = sc.nextLine();
					System.out.print("Chemin d'acces a la base de donnees csv : ");
					String path = sc.nextLine();
					CsvImport ci = new CsvImport(path);
					TreatData treater = new TreatData(ci.importFile());

					System.out.print("Niveau de regroupement : ");
					String parameter = sc.nextLine();		
					TreatedData treatedData = new TreatedData();


					if(Integer.parseInt(choix) == 1) {
						treatedData.setTreatedData(treater.getDB().getNames());
						treater.manualClean(Integer.parseInt(parameter), treatedData);
					}
					else if(Integer.parseInt(choix) == 2) {
						treatedData.setTreatedData(treater.getDB().getJournals());
						treater.manualClean(Integer.parseInt(parameter), treatedData);
					}

					System.out.print("Appuyer sur Entree lorsque les modifications du fichier manualClean.csv sont terminees ");
					String entrer = sc.nextLine();
					treater.loadManualClean(treatedData);
				}
				catch(Exception e) {
					System.out.print("\n--> Base de donnees introuvable <--");
				}
			}
			else if(Integer.parseInt(str) == 3) {
				System.out.print("Chemin d'acces a la base de donnees (csv) : ");
				String path = sc.nextLine();
				try{
					System.out.println("1 - Recuperer les articles d'un auteur\n");
					System.out.println("2 - Recuperer les auteurs cites par un auteur\n");
					System.out.println("3 - Recuperer les journaux cites par un auteur\n");
					System.out.println("4 - Recuperer les citations d'un auteur\n");
					System.out.println("5 - Recuperer les journaux ou est cite un auteur\n");

					String choix = sc.nextLine();

					switch (Integer.parseInt(choix))
					{
					case 1:

						break;
					case 2:

						break;
					case 3:

						break;
					case 4:

						break;
					case 5:

						break;
					default:
						System.out.println("Ce choix n'existe pas");
					}
				}
				catch(Exception e) {
					System.out.print("\n--> Base de donnees introuvable <--");
				}
			}
		}

		System.out.println("\n\n");
	}
}
