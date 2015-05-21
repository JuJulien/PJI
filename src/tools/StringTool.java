package tools;

/**
 * Regroupe différent outils sur des chaines de caractères
 * @author Remi et Julien
 */
public class StringTool {

	/**
	 * Test si la chaine entrée en param contient un chiffre
	 * @param str Une chaine de caractère
	 * @return Vrai si elle contient un chiffre, faux sinon
	 */
	public boolean containsNumber(String str){
		for(int i =0; i<str.length();i++){
			if(str.charAt(i)>=48 && str.charAt(i)<=57 ){
				return true;
			}
		}
		return false;
	}

	/**
	 * Supprime la ponctuation et les nombres
	 * @param str Une chaine de caractère
	 * @return La chaine de caractère traitée
	 */
	public String removeUnwantedChar(String str){
		String strClean = str.replace(".", "");
		strClean = strClean.replace(",", "");
		strClean = strClean.replace("[", "");
		strClean = strClean.replace("]", "");
		strClean = strClean.replace("(", "");
		strClean = strClean.replace(")", "");
		strClean = strClean.replace("\"", "");
		strClean = strClean.replace("/", "");
		strClean = strClean.replace("-", " ");
		if(containsNumber(strClean)){
			for (int i = 0; i < 10; i++) {
				strClean = strClean.replace("" + i, "");
			}
		}
		return strClean;
	}

	/**
	 * Uniforme la casse d'une chaine de caractère 
	 * pour que la première lettre de chaque mot soit en majuscule et les suivantes en minuscule
	 * @param name Une chaine de caractère
	 * @return La chaine de caractère traitée
	 */
	public String uniformCase(String name){
		String firstCleanName = removeUnwantedChar(name);
		String[] splitedName = firstCleanName.split(" ");

		firstCleanName = "";
		for(int k = 0; k<splitedName.length; k++){
			if(splitedName[k].length()>0){
				firstCleanName += splitedName[k].substring(0, 1).toUpperCase() + splitedName[k].substring(1).toLowerCase();
				if (k != splitedName.length-1){
					firstCleanName += " ";
				}
			}
		}
		return firstCleanName;
	}

	/**
	 * Supprime les mois abrégés à la fin d'une chaine de caractère
	 * @param str Une chaine de caractère
	 * @return La chaine de caractère traitée
	 */
	public String removeMonth(String str){
		String strClean = str;
		String[] month = {"jan","jeb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};
		for(int i = 0; i<month.length;i ++){
			if(str.toLowerCase().endsWith(" " + month[i])){
				strClean = str.substring(0, str.length() - 4);
			}
		}
		return strClean;
	}

	/**
	 * Supprime les ":" et tout ce qui suit d'une chaine de caractère
	 * @param str Une chaine de caractère
	 * @return La chaine de caractère traitée
	 */
	public String removeColon(String str){
		if(str.contains(":")){
			return str.substring(0,str.indexOf(":"));
		}else{
			return str;

		}
	}
	
	/**
	 * Test si une chaine de caractère ne contient que des espaces
	 * @param str Une chaine de caractère
	 * @return Vrai si elle ne contient que des espaces, faux sinon
	 */
	public Boolean containsOnlySpace(String str){
		for(int i = 0; i<str.length(); i++){
			if (str.charAt(i) != ' ')
				return false;
		}
		return true;
	}
}
