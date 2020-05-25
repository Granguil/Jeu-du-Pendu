import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class ScorePendu {
	//Attributs 
	private String pseudo;
	private int score;
	private static File file=file();
	
	//File selon environnement de dev ou de prod
	public static File file() {
		File file2 = new File(System.getProperty("user.home") + File.separator + "xml" + File.separator + "ScorePendu.xml");
		if (!file2.isFile()) {
			file2 = new File(System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "xml"
					+ File.separator + "ScorePendu.xml");
		} 
		return file2;
	}
	@Override
	public String toString() {
		return "ScorePendu [pseudo=" + pseudo + ", score=" + score + "]";
	}
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	public static ScorePendu[] read() {
		//Fonction pour lire le fichier xml
		ScorePendu liste[]= new ScorePendu[11];
		//Création de la factory
		XMLInputFactory factory = XMLInputFactory.newFactory();
		
		try {
			//Création du lecteur
			XMLStreamReader eye = factory.createXMLStreamReader(new InputStreamReader(new FileInputStream(file),Charset.forName("utf-8")));
			ScorePendu sp=null;
			//index de la ligne actuelle
			int a=0;
			while (eye.hasNext()) {
				int eventType = eye.next();
				//Switch selon l'événement du xml
				switch(eventType) {
				//On utilise l'élément de départ pour naviguer dans le document
				case XMLStreamReader.START_ELEMENT:
					switch (eye.getLocalName()) {
					//Chaque ligne correspond à un score et un pseudo
					case "Score":
						sp=new ScorePendu();
						liste[a]=sp;
						for (int i = 0; i < eye.getAttributeCount(); i++) {
							
							switch (eye.getAttributeName(i).toString().toUpperCase()) {
							case "PSEUDO":
								liste[a].setPseudo(eye.getAttributeValue(i));
								
								break;
							case "SCORE":
								liste[a].setScore(Integer.parseInt(eye.getAttributeValue(i)));
								
								break;
							}
						}
						//Passage à l'index de la ligne suivante
						a++;
					}
				}
			}
		}catch(Exception e) {
			e.getMessage();
		}
		
		return liste;
	}
	public static void write(ScorePendu sp[]) {
		//Fonction d'écriture d'un fichier xml
		//Créateur de la factory
		XMLOutputFactory factory = XMLOutputFactory.newFactory();
		try {
			//Créateur de l'outil d'écriture
			FileOutputStream channel = new FileOutputStream(file);
			XMLStreamWriter pen = factory.createXMLStreamWriter(channel, "UTF-8");
			
			//Ecriture du document
			pen.writeStartDocument("UTF-8", "1.0");
			pen.writeCharacters("\r\n");
			pen.writeStartElement("Classement");
			for(int i=0;i<11;i++) {
				pen.writeCharacters("\r\n\t");
				pen.writeEmptyElement("Score");
				pen.writeAttribute("Pseudo", sp[i].getPseudo());
				pen.writeAttribute("Score", String.valueOf(sp[i].getScore()));
			}
			pen.writeCharacters("\r\n");
			pen.writeEndElement();
		}catch(Exception e) {
			
		}
	}
}
