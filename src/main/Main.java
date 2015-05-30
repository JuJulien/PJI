package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import request.Request;
import csv.CsvImport;
import csv.CsvMaker;
import db.DataBase;
import db.TreatData;
import db.TreatedData;

public class Main {

	public static void main(String[] args) throws IOException {

		String str = "0";
		String pathDb = "";

		while(Integer.parseInt(str) != 7) {

			System.out.println("\n1 - Nettoyer une base de donnees html automatiquement\n");
			System.out.println("2 - Generer la base de données sans le nettoyage\n");
			System.out.println("3 - Generer les fichiers csv des modifications du nettoyage automatique\n");
			System.out.println("4 - Generer les fichiers csv de nettoyage manuel\n");
			System.out.println("5 - Appliquer les modifications\n");
			System.out.println("6 - Requetes sur la base de donnees\n");
			System.out.println("7 - Quitter\n");

			System.out.print("Choix : ");
			Scanner sc = new Scanner(System.in);
			str = sc.nextLine();

			//Cas 1 Nettoyage total et automatique
			if(Integer.parseInt(str) == 1) {
				System.out.print("\nChemin d'acces a la base de donnees (html) : ");
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

						treatedData.saveToChange("automaticalClean_"+i+".csv");

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

					System.out.println("\n--> Termine <--\n");
				}
				catch(Exception e) {
					System.out.print("\n--> Base de donnees introuvable <--\n\n");
					pathDb = "";
				}
			}

			//Cas 2 générer la bdd sans le nettoyage
			else if(Integer.parseInt(str) == 2) {
				System.out.print("\nChemin d'acces a la base de donnees (html) : ");
				String path = sc.nextLine();
				try{
					CsvMaker cm = new CsvMaker(path);	
					//Importe les données depuis les fichiers html et crée un csv les contenants
					cm.build();
					System.out.println("\n--> Termine <--\n");
				}
				catch(Exception e) {
					System.out.print("\n--> Base de donnees introuvable <--\n\n");
					pathDb = "";
				}
			}

			//Cas 3 nettoyage automatique
			else if(Integer.parseInt(str) == 3) {
				if(pathDb == "") {
					System.out.print("\nChemin d'acces a la base de donnees csv : ");
					pathDb = sc.nextLine();
				}
				try{
					CsvImport ci = new CsvImport(pathDb);
					TreatData treater = new TreatData(ci.importFile());

					List<TreatedData> namesAndJournals = treater.automaticalClean();
					//Parcours des données traitées
					for(int i = 0; i<namesAndJournals.size(); i++){
						TreatedData treatedData = namesAndJournals.get(i);
						treatedData.saveToChange("automaticalClean_" + i + ".csv");
					}
					System.out.println("\n--> Termine <--\n");
				}
				catch(Exception e) {
					System.out.print("\n--> Base de donnees introuvable <--\n\n");
					pathDb = "";
				}

			}

			//Cas 4 nettoyage manuel
			else if(Integer.parseInt(str) == 4) {
				if(pathDb == "") {
					System.out.print("\nChemin d'acces a la base de donnees csv : ");
					pathDb = sc.nextLine();
				}
				try{
					CsvImport ci = new CsvImport(pathDb);
					TreatData treater = new TreatData(ci.importFile());

					System.out.print("\nNiveau de regroupement : ");
					String parameter = sc.nextLine();		
					TreatedData treatedData = new TreatedData();

					treatedData.setTreatedData(treater.getDB().getNames());
					treater.manualClean(Integer.parseInt(parameter), treatedData,"manualCleanNames.csv");

					treatedData.setTreatedData(treater.getDB().getJournals());
					treater.manualClean(Integer.parseInt(parameter), treatedData,"manualCleanJournals.csv");
					
					System.out.println("\n--> Termine <--\n");
				}
				catch(Exception e) {
					System.out.print("\n--> Base de donnees introuvable <--\n\n");
					pathDb = "";
				}
			}

			//Cas 5 appliquer modifications
			else if(Integer.parseInt(str) == 5) {
				if(pathDb == "") {
					System.out.print("\nChemin d'acces a la base de donnees csv : ");
					pathDb = sc.nextLine();
				}
				try{
					CsvImport ci = new CsvImport(pathDb);
					TreatData treater = new TreatData(ci.importFile());

					System.out.print("\nChemin d'acces au fichier csv de modifications : ");
					String path = sc.nextLine();

					try{
						TreatedData treatedData = new TreatedData();
						treatedData.loadToChange(path);

						System.out.print("\nVoulez vous modifier les noms (1) ou les journaux (2) : ");
						String choix = sc.nextLine();

						List<TreatedData> list = new ArrayList<TreatedData>();

						if(Integer.parseInt(choix) == 2){
							list.add(new TreatedData());
						}

						list.add(treatedData);

						if(Integer.parseInt(choix) == 1){
							list.add(new TreatedData());
						}

						treater.loadToChange(list);

						CsvMaker cm = new CsvMaker(pathDb);
						cm.makeCSV(treater.getDB().getData());
						
						System.out.println("\n--> Termine <--\n");

					}catch(Exception e) {
						e.printStackTrace();
						System.out.print("\n--> Fichier introuvable <--\n\n");
					}
				}
				catch(Exception e) {
					System.out.print("\n--> Base de donnees introuvable <--\n\n");
					pathDb = "";
				}
			}

			// Cas 6 requetes
			else if(Integer.parseInt(str) == 6) {
				if(pathDb == "") {
					System.out.print("\nChemin d'acces a la base de donnees csv : ");
					pathDb = sc.nextLine();
				}
				try{
					CsvImport ci = new CsvImport(pathDb);
					DataBase db = new DataBase(ci.importFile());
					String choix = "0";

					while(Integer.parseInt(choix) != 6) {
						System.out.println("\n1 - Recuperer les articles d'un auteur\n");
						System.out.println("2 - Recuperer les auteurs cites par un auteur\n");
						System.out.println("3 - Recuperer les journaux cites par un auteur\n");
						System.out.println("4 - Recuperer les citations d'un auteur\n");
						System.out.println("5 - Recuperer les journaux ou est cite un auteur\n");
						System.out.println("6 - Retour\n");

						System.out.print("Choix : ");
						choix = sc.nextLine();

						Request requete = new Request(db);
						List<String> answer;
						String param;
		
						switch (Integer.parseInt(choix))
						{
						case 1:		
							System.out.print("\nEntrez le nom d'un auteur : ");
							param = sc.nextLine();
							answer = requete.getArticle(param);
							System.out.println("\nArticles :");
							for (String string : answer) {
								System.out.println("\t"+string);
							}
							break;
						case 2:
							System.out.print("\nEntrez le nom d'un auteur : ");
							param = sc.nextLine();
							answer = requete.getReferences(param);
							System.out.println("\nAuteurs :");
							for (String string : answer) {
								System.out.println("\t"+string);
							}
							break;
						case 3:
							System.out.print("\nEntrez le nom d'un auteur : ");
							param = sc.nextLine();
							answer = requete.getJournalsReferences(param);
							System.out.println("\nJournaux cite :");
							for (String string : answer) {
								System.out.println("\t"+string);
							}
							break;
						case 4:
							System.out.print("\nEntrez le nom d'un auteur : ");
							param = sc.nextLine();
							answer = requete.getPeopleCiting(param);
							System.out.println("\nCite par :");
							for (String string : answer) {
								System.out.println("\t"+string);
							}
							break;
						case 5:
							System.out.print("\nEntrez le nom d'un auteur : ");
							param = sc.nextLine();
							System.out.println("\nJournaux:");
							answer = requete.getJournalsCiting(param);
							for (String string : answer) {
								System.out.println("\t"+string);
							}
							break;
						case 6:
							break;
						default:
							System.out.println("Ce choix n'existe pas");
						}
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					System.out.print("\n--> Base de donnees introuvable <--\n\n");
					pathDb = "";				
				}
			}
		}

		System.out.println("\n\n");
	}
}
