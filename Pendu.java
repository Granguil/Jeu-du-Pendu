

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Pendu
 */
public class Pendu extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Pendu() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//Ouverture de la session
		HttpSession session=request.getSession();
		
		//Remise � z�ro des param�tres pour une nouvelle partie
		if(request.getParameter("rejouer")!=null) {
			//V�rification que le param�tre 'rejouer' est bien �gal � 1 
			int rejouer=Integer.parseInt(request.getParameter("rejouer"));
			if(rejouer==1) {
				//Enlever le mot de la session
				session.removeAttribute("mot");
				
				//Enregistrement du pseudo
				ScorePendu sp[]=ScorePendu.read();
				String pseudo=request.getParameter("pseudo");
				for(ScorePendu spendu:sp) {
					if(spendu.getPseudo().equals(" ")) {
						spendu.setPseudo(pseudo);
					}
				}
				ScorePendu.write(sp);
			}
		}
		
		//V�rifier si on continue une partie ou si c'est une nouvelle partie
		int continuer=0;
		if(request.getParameter("continuer")!=null) {
			continuer=Integer.parseInt(request.getParameter("continuer"));
		}
		
		//R�cup�ration du mot � trouver et de la lettre propos�e
		String mot=(String)session.getAttribute("mot");
		String l=request.getParameter("lettre");
		
		//Si on a un nouveau mot � deviner
		if((l==null) || mot==null) {
			
			//Choix du nouveau mot � deviner
			Lecteur lecteur=new Lecteur();
			mot=lecteur.choixmot();
			//Cr�ation du mot avec des �toiles pour afficher la longueur
			String mad="";
			for(int i=0;i<mot.length();i++) {
				mad+="* ";
			}
			
			//Pr�paration de l'image de la potence et du pendu
			String pendu[][];
			pendu=new String[6][6];
			for(int i=0;i<6;i++) {
				for(int j=0;j<6;j++) {
					pendu[i][j]=" ";
				}
			}
			//Cr�ation de la liste pour stocker les lettres fausses
			List<String> lf=new ArrayList<String>();
			
			//Si on recommence une partie, une remise � z�ro des param�tres(score, nombre de mots trouv�s, l'image du pendu et le nombre de tentatives
			if(continuer==0) {
				session.setAttribute("score", 0);
				session.setAttribute("nbmt", 0);
				session.setAttribute("pendu", pendu);
				session.setAttribute("nbtentatives", 0);
			}
			//Attribution des valeurs aussi bien pour une nouvelle partie qu'un mot nouveau d'une m�me partie(mots � d�couvrir, lettres fausses)
			session.setAttribute("mad", mad);
			session.setAttribute("mot",mot);
			session.setAttribute("lf", lf);
			}else {
				//Action � faire apr�s une proposition de lettre
				//R�cup�ration des donn�es de la jsp 
				int nbt=(int)session.getAttribute("nbtentatives");
				String lettre=request.getParameter("lettre").toLowerCase();
				String mad=(String)session.getAttribute("mad");
				//V�rification que l'utilisateur � bien proposer une lettre et pas un autre caract�re 
				if(Character.isLetter(lettre.charAt(0))) {
					lettre=Lecteur.sansAccent(lettre);
					char lettre2=lettre.charAt(0);
					String nvmot="";
					
					//V�rification si la lettre est comprise dans le mot et cr�ation de l'affichage du mot(nvmot)
					for(int i=0;i<mot.length();i++) {
						if(lettre2==mot.charAt(i)) {
							nvmot+=lettre2+" ";
						}else {
							nvmot+=mad.charAt(i*2)+" ";
						}
					}
					//R�cup�ration des lettres fausses
					List<Character> lf=(List<Character>)session.getAttribute("lf");
					//V�rification si la lettre est dans le mot
					if(nvmot.equals(mad)) {
						//Lettre non pr�sente (augmentation du nombre de tentatives, ajout d'une lettre fausse)
						nbt++;
						lf.add(lettre2);
					}else {
						//Mise � jour du mot � trouver
						mad=nvmot;
					}
				//Enregistrement des lettres fausses
				session.setAttribute("lf", lf);
				
				//Cr�ation du mot avec les lettres trouv�es
				String verif="";
				for(int i=0;i<mad.length();i=i+2) {
					verif+=mad.charAt(i);
				}
				
				//V�rification si le mot a �t� trouv�
				if(verif.equals(mot)) {
					//En cas de victoire, on pr�pare l'affichage de la page
					//
					String vic="Mot Trouv�";
					request.setAttribute("vic", vic);
					//Augmetnation du score
					int score=(int)session.getAttribute("score");
					score+=mot.length();
					session.setAttribute("score", score);
					//Augmentation du nombre de mots trouv�s
					int nbmt=(int)session.getAttribute("nbmt");
					nbmt++;
					session.setAttribute("nbmt", nbmt);
				}
				//Affichage du pendu selon le nombre de tentatives
				String pendu[][]=(String[][])session.getAttribute("pendu");
				switch(nbt) {
				case 1:
					pendu[5][0]="_";
					pendu[5][1]="_";
					pendu[5][2]="_";
					break;
				case 2:
					pendu[5][1]="|";
					pendu[4][1]="|";
					pendu[3][1]="|";
					pendu[2][1]="|";
					pendu[1][1]="|";
					break;
				case 3:
					pendu[0][1]="_";
					pendu[0][2]="_";
					pendu[0][3]="_";
					pendu[0][4]="_";
					break;
				case 4:
					pendu[1][4]="|";
					break;
				case 5:
					pendu[2][4]="o";
					break;
				case 6:
					pendu[3][4]="|";
					break;
				case 7:
					pendu[3][3]="/";
					break;
				case 8:
					pendu[3][5]="\\";
					break;
				case 9:
					pendu[4][3]="/";
					break;
				case 10:
					pendu[4][5]="\\";
				}
				//Enregistrement donn�es
				session.setAttribute("nbtentatives",nbt);
				session.setAttribute("pendu", pendu);
				session.setAttribute("mad", mad);
				
				//V�rification si d�faite
				if(nbt>=10) {
					String defaite="D�faite";
					request.setAttribute("def", defaite);
					
					//Enregistrement du score si dans les dix meilleurs scores
					int score=(int)session.getAttribute("score");
					ScorePendu sp =new ScorePendu();
					sp.setPseudo(" ");
					sp.setScore(score);
					ScorePendu listeSP[]=ScorePendu.read();
					
					//Adapation des scores si un joeur n'a pas laiss� de pseudo associ� � un score
					for(int i=0;i<listeSP.length;i++) {
						if(listeSP[i].getPseudo().equals(" ")) {
							for(int j=i+1;j<listeSP.length;j++) {
								listeSP[j-1].setPseudo(listeSP[j].getPseudo());
								listeSP[j-1].setScore(listeSP[j].getScore());
							}
						}
					}
					//Enregistrement du score
					for(int i=0;i<listeSP.length;i++) {
						if(listeSP[i].getScore()<score) {
							for(int j=listeSP.length-1;j>=i+1;j--) {
								listeSP[j].setScore(listeSP[j-1].getScore());
								listeSP[j].setPseudo(listeSP[j-1].getPseudo());
							}
							listeSP[i]=sp;
							i=11;
						}
					}
					//Enregistrement score
					ScorePendu.write(listeSP);
					//r�cup�ration des dix premiers scores
					listeSP=Arrays.copyOf(listeSP, 10);
					//Sauvegarde pour afficher le classement dans la jsp
					session.setAttribute("listeScore",listeSP);
					
				}
			
			}else {
				//Cas o� l'utilisateur n'aurait pas rentr� une lettre
				String message=lettre+" n'est pas une lettre";
				request.setAttribute("message",message);
			}
		}
		//Renvoi vers la jsp
		this.getServletContext().getRequestDispatcher("/pendu.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
