import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

class Lecteur 
{
	//Attribut file
	private static String adresse=file();
	
	//Valeur de l'attribut selon le support (dev ou prod)
	public static String file() {
		String adresse;
		adresse=System.getProperty("user.home") + File.separator + "xml" + File.separator + "liste_francais.txt";
		File adresse2=new File(adresse);
		if(!adresse2.isFile() ) {
			adresse=System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Html"
					+ File.separator + "liste_francais"+File.separator+"liste_francais.txt";
		}
		return adresse;
	}
	
		//Fonction de s�lection du mot � trouver
	  public String choixmot() throws IOException
	  {
		  //Cr�ation du lecteur
	    BufferedReader lecteurAvecBuffer = null;
	    
	    //Cr�ation des r�cepteurs
	    String ligne;
	    List<String> liste=new ArrayList<String>();
	    
	    //Ajout adresse au lecteur
		lecteurAvecBuffer = new BufferedReader(new FileReader(adresse));
	     String mot="";
	     
	     //Lecture ligne par ligne du fichier txt
	    while ((ligne = lecteurAvecBuffer.readLine()) != null) {
	    	//S�paration en tableau de caract�res du mot, mise en majuscule de la premi�re lettre
	    	char[] chaine=ligne.toCharArray();
	    	chaine[0]=Character.toUpperCase(chaine[0]);
	    	String ligne2=new String(chaine);
	    	boolean bool=true;
	    	
	    	//V�rification qu'il n'y a que des lettres
	    	for(int i=0;i<ligne2.length();i++) {
	    		if(!Character.isLetter(ligne2.charAt(i))) {
	    			bool=false;
	    		}
	    	}
	    	
	    	//Ajout � la liste si :
	    		//le mot ne commence pas par une majuscule
	    		//le mot compte entre 5 et 10 caract�res
	    		//le mot ne comprend que des lettres
	    	if(!ligne2.equals(ligne) && ligne.length()>=5 && ligne.length()<=10 && bool) {
	    	liste.add(ligne);
	    	}
	    }
	    //Fermeture du lecteur
	    lecteurAvecBuffer.close();
	    
	    //S�l�ction du mot et suppression des accents
	    int alea=(int)(Math.random()*liste.size());
	    mot=liste.get(alea);
	    mot=sansAccent(mot);
	    
		return mot;
	  }
	  
	  //Enlever les accents
	  public static String sansAccent(String s) 
	  {
		  	//Utilisation d'un design pattern pour enlever les accents
	        String strTemp = Normalizer.normalize(s, Normalizer.Form.NFD);
	        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	        return pattern.matcher(strTemp).replaceAll("");
	  }
}
