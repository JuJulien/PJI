package request;

import java.util.ArrayList;
import java.util.List;

import db.DataBase;

public class Request {
	DataBase db;

	public Request(DataBase db){
		this.db = db;
	}

	/**
	 * Recup�re le noms des articles d'un auteur
	 * @param author Le nom de l'auteur
	 * @return La liste des articles �crit par l'auteur
	 */
	public List<String> getArticle(String author){
		List<String> articles = new ArrayList<>();
		for (int i = 0; i < db.getData().length; i++) {
			String [] authorNames = db.getData()[i][0].split(">");
			boolean find = false;
			int j = 0;
			while (!find && j<authorNames.length){
				if(authorNames[j].equals(author)){
					articles.add(db.getData()[i][2]);
					find = true;
				}
				j++;
			}
		}
		return articles;
	}

	/**
	 * R�cup�re tous les auteurs cit�s par un auteur
	 * @param author Le nom de l'auteur
	 * @return  La liste des auteurs cit�s par l'auteur
	 */
	public List<String> getReferences(String author){
		List<String> references = new ArrayList<>();
		for (int i = 0; i < db.getData().length; i++) {
			String [] authorNames = db.getData()[i][0].split(">");
			boolean find = false;
			int j = 0;
			while (!find && j<authorNames.length){
				if(authorNames[j].equals(author)){
					String[] credits = db.getData()[i][4].split(">");
					for(int k = 0; k<credits.length;j++){
						if(!references.contains(credits[k].substring(0, credits[k].indexOf(",")))){
							references.add(credits[k].substring(0, credits[k].indexOf(",")));
						}
					}
					find = true;
				}
				j++;
			}
		}
		return references;
	}

	/**
	 * R�cup�re les journaux cit�s par un auteur
	 * @param author Le nom de l'auteur
	 * @return La liste des journaux cit�s par l'auteur
	 */
	public List<String> getJournalsReferences(String author){
		List<String> journals = new ArrayList<>();
		for (int i = 0; i < db.getData().length; i++) {
			String [] authorNames = db.getData()[i][0].split(">");
			boolean find = false;
			int j = 0;
			while (!find && j<authorNames.length){
				if(authorNames[j].equals(author)){
					String[] credits = db.getData()[i][4].split(">");
					for(int k = 0; k<credits.length;j++){
						String[] citation = credits[k].split(",");
						if(!journals.contains(citation[2])){
							journals.add(citation[2]);
						}
					}
					find = true;
				}
				j++;
			}
		}
		return journals;
	}

	/**
	 * R�cup�re les personnes citant un auteur
	 * @param author Le nom de l'auteur
	 * @return La liste des personnes citant un auteur
	 */
	public List<String> getPeopleCiting(String author){
		List<String> peopleCiting = new ArrayList<>();
		for (int i = 0; i < db.getData().length; i++) {
			String [] credits = db.getData()[i][4].split(">");
			boolean find = false;
			int j = 0;
			while (!find && j<credits.length){
				if(credits[j].split(",")[0].equals(author)){
					String[] authors = db.getData()[i][0].split(">");
					for(int k = 0; k<authors.length;j++){
						if(!peopleCiting.contains(authors[k])){
							peopleCiting.add(authors[k]);
						}
					}
					find = true;
				}
				j++;
			}
		}
		return peopleCiting;
	}

	/**
	 * R�cup�re les journals o� est cit� un auteur
	 * @param author Le nom de l'auteur
	 * @return La liste des journaux o� est cit� un auteur
	 */
	public List<String> getJournalsCiting(String author){
		List<String> journalsCiting = new ArrayList<>();
		for (int i = 0; i < db.getData().length; i++) {
			String [] credits = db.getData()[i][4].split(">");
			boolean find = false;
			int j = 0;
			while (!find && j<credits.length){
				if(credits[j].split(",")[0].equals(author)){
					if(!journalsCiting.contains(db.getData()[i][3])){
						journalsCiting.add(db.getData()[i][3]);
					}
				}
				find = true;
				j++;
			}
		}
		return journalsCiting;
	}
}
