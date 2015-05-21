package tools;

/**
 * Regroupe diff�rent outils sur des chaines de caract�res
 * @author Remi et Julien
 */
public class StringTool {

	/**
	 * Test si la chaine entr�e en param contient un chiffre
	 * @param str Une chaine de caract�re
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
	 * @param str Une chaine de caract�re
	 * @return La chaine de caract�re trait�e
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
	 * Uniforme la casse d'une chaine de caract�re 
	 * pour que la premi�re lettre de chaque mot soit en majuscule et les suivantes en minuscule
	 * @param name Une chaine de caract�re
	 * @return La chaine de caract�re trait�e
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
	 * Supprime les mois abr�g�s � la fin d'une chaine de caract�re
	 * @param str Une chaine de caract�re
	 * @return La chaine de caract�re trait�e
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
	 * Supprime les ":" et tout ce qui suit d'une chaine de caract�re
	 * @param str Une chaine de caract�re
	 * @return La chaine de caract�re trait�e
	 */
	public String removeColon(String str){
		if(str.contains(":")){
			return str.substring(0,str.indexOf(":"));
		}else{
			return str;

		}
	}
	
	/**
	 * Test si une chaine de caract�re ne contient que des espaces
	 * @param str Une chaine de caract�re
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
