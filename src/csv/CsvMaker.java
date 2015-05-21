package csv;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Permet de créer un fichier csv à partir d'un tableau de données ou de fichier html
 * @author Remi et Julien
 *
 */
public class CsvMaker {
	/**
	 * Le chemin du fichier csv qui sera créé
	 */
	private String path;

	/**
	 * Constructeur
	 * @param path Le chemin du fichier csv qui sera créé
	 */
	public CsvMaker(String path){
		this.path = path;
	}

	/**
	 * Crée un fichier csv depuis des fichiers html
	 * @throws IOException
	 */
	public void build() throws IOException{
		List<String> allFiles = new ArrayList<>();
		getFilesRec(allFiles, path);
		processFiles(allFiles);

	}

	/**
	 * Récupère la liste des fichiers à traiter à partir d'une racine
	 * @param allFiles La liste des fichiers à traiter
	 * @param root La racine des fichiers à traiter
	 */
	private void getFilesRec(List<String> allFiles, String root) {
		File f = new File(root);
		File[] listFiles = f.listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			if (listFiles[i].isDirectory()){
				getFilesRec(allFiles, listFiles[i].toString());
			}else{ 
				allFiles.add(listFiles[i].toString());
			}
		}
	}
	
	/**
	 * Traite les fichiers html de allFiles pour créer un fichier Csv contenant les données
	 * @param allFiles La liste des fichiers à traiter
	 * @throws IOException
	 */
	private void processFiles(List<String> allFiles) throws IOException {
		List<String[]> csv = new ArrayList<>();
		for (int i = 0; i < allFiles.size(); i++) {
			BufferedReader in = new BufferedReader(new FileReader(allFiles.get(i)));
			String line;
			String[] category = {"AF","AU","TI","SO","CR","DT","ID","NR","TC","PY","WC","SC"};
			String state = "";
			String[] csvLine = new String[12];
			while((line=in.readLine())!=null){
				boolean find = false;
				int cpt = 0;
				//Fin d'un tableau ajout dans la list
				if(line.contains("<hr>")){
					csv.add(csvLine);
					csvLine = new String[12];
				}

				//Cas sur plusieurs lignes
				if(line.contains("<br")){
					while(!find && cpt<category.length){
						if(state.equals(category[cpt])){
							find = true;
							if(line.contains("</td>")){
								csvLine[cpt] += line.substring(line.indexOf(">"),line.lastIndexOf("</td>"));
							}else{
								csvLine[cpt] += line.substring(line.indexOf(">"));
							}
						}
						cpt++;
					}
				}


				//Cas ou ligne contient la categorie
				if(line.contains("<td valign=\"top\">")){

					find = false;
					cpt = 0;
					while(!find && cpt<category.length){
						if(line.contains("<td valign=\"top\">"+ category[cpt]+ " </td>")){
							state = category[cpt];
							find = true;
							if(!line.endsWith("</td>")){
								csvLine[cpt] = line.substring(line.indexOf("<td>") + "</td>".length() - 1);
							}else{
								csvLine[cpt] = line.substring(line.indexOf("<td>")+ "</td>".length() - 1,line.length()-"</td>".length());
							}
						}
						cpt++;
					}
					if(!find){
						state = "Not interested";
					}
				}
			}
			in.close();
		}
		fillCsv(csv);
	}
	
	/**
	 * Crée un fichier Csv à partir d'une liste de ligne
	 * @param csv La liste des lignes
	 * @throws FileNotFoundException
	 */
	public void fillCsv(List<String[]> csv) throws FileNotFoundException {
		File csvFile = new File("db.csv");
		PrintStream printstream = new PrintStream(new FileOutputStream(csvFile)); 
		printstream.print("\"AF\";\"AU\";\"TI\";\"SO\";\"CR\";\"DT\";\"ID\";\"NR\";\"TC\";\"PY\";\"WC\";\"SC\"\n");
		for (String[] string : csv) {
			for (int j = 0; j < string.length; j++) {
				if(string[j]!= null && string[j].contains("&amp;")){
					string[j] = string[j].replaceAll("&amp;", "&");
				}
				printstream.print("\"" + string[j] +"\";");
			}
			printstream.print("\n");

		}
		printstream.close();
	}
	
	/**
	 * Crée un fichier Csv à partir d'un tableau de données
	 * @param data Le tableau de données
	 * @throws FileNotFoundException
	 */
	public void makeCSV(String[][] data) throws FileNotFoundException{
		File csvFile = new File("db.csv");
		PrintStream printstream = new PrintStream(new FileOutputStream(csvFile)); 
		printstream.print("\"AF\";\"AU\";\"TI\";\"SO\";\"CR\";\"DT\";\"ID\";\"NR\";\"TC\";\"PY\";\"WC\";\"SC\"\n");
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				printstream.print("\"" + data[i][j] +"\";");
			}
			printstream.print("\n");
		}
		printstream.close();
	}

}
