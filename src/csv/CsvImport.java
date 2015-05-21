package csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 * Permet d'importer des données depuis un fichier CSV dans un tableau à deux dimensions 
 * @author Remi et Julien
 */
public class CsvImport {
	/**
	 * Chemin du fichier csv
	 */
	private String path;

	/**
	 * Constructeur
	 * @param path le chemin du fichier csv
	 */
	public CsvImport(String path) {
		this.path = path;
	}

	/**
	 * Importe les données depuis un fichier csv
	 * @return Un tableau de String à deux dimensions
	 * @throws IOException
	 */
	public String[][] importFile() throws IOException {
		int i = 0;
		String tmp;
		int[] tabSize = getTabSize();
		BufferedReader br = new BufferedReader(new FileReader(this.path));
		String dataTab[][] = new String[tabSize[0]][tabSize[1]];
		
		//On saute la premiere ligne de titre
		br.readLine();
		
		//On récupère les lignes suivantes
		while ((tmp=br.readLine())!=null ) {
			String[] splitted= tmp.split("\";");
			for (int j=0;j<dataTab[0].length;j++) {
				dataTab[i][j] = splitted[j].substring(1);
			}
			i++;
		}

		br.close();
		return dataTab;

	}
	/**
	 * Recupère la taille du tableau issue de l'importation
	 * @return la taille du tableau
	 * @throws IOException
	 */
	public int[] getTabSize() throws IOException {
		int count[] = {0,0};
		BufferedReader br = new BufferedReader(new FileReader(this.path));
		count[1] = br.readLine().split("\";").length;
		while ((br.readLine())!=null)
		{
			count[0]++;
		}
		br.close();
		return count;
	}

}
