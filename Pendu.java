

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
		
		//Remise à zéro des paramètres pour une nouvelle partie
		if(request.getParameter("rejouer")!=null) {
			//Vérification que le paramètre 'rejouer' est bien égal à 1 
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
		
		//Vérifier si on continue une partie ou si c'est une nouvelle partie
		int continuer=0;
		if(request.getParameter("continuer")!=null) {
			continuer=Integer.parseInt(request.getParameter("continuer"));
		}
		
		//Récupération du mot à trouver et de la lettre proposée
		String mot=(String)session.getAttribute("mot");
		String l=request.getParameter("lettre");
		
		//Si on a un nouveau mot à deviner
		if((l==null) || mot==null) {
			
			//Choix du nouveau mot à deviner
			Lecteur lecteur=new Lecteur();
			mot=lecteur.choixmot();
			//Création du mot avec des étoiles pour afficher la longueur
			String mad="";
			for(int i=0;i<mot.length();i++) {
				mad+="* ";
			}
			
			//Préparation de l'image de la potence et du pendu
			String pendu[][];
			pendu=new String[6][6];
			for(int i=0;i<6;i++) {
				for(int j=0;j<6;j++) {
					pendu[i][j]=" ";
				}
			}
			//Création de la liste pour stocker les lettres fausses
			List<String> lf=new ArrayList<String>();
			
			//Si on recommence une partie, une remise à zéro des paramètres(score, nombre de mots trouvés, l'image du pendu et le nombre de tentatives
			if(continuer==0) {
				session.setAttribute("score", 0);
				session.setAttribute("nbmt", 0);
				session.setAttribute("pendu", pendu);
				session.setAttribute("nbtentatives", 0);
			}
			//Attribution des valeurs aussi bien pour une nouvelle partie qu'un mot nouveau d'une même partie(mots à découvrir, lettres fausses)
			session.setAttribute("mad", mad);
			session.setAttribute("mot",mot);
			session.setAttribute("lf", lf);
			}else {
				//Action à faire après une proposition de lettre
				//Récupération des données de la jsp 
				int nbt=(int)session.getAttribute("nbtentatives");
				String lettre=request.getParameter("lettre").toLowerCase();
				String mad=(String)session.getAttribute("mad");
				//Vérification que l'utilisateur à bien proposer une lettre et pas un autre caractère 
				if(Character.isLetter(lettre.charAt(0))) {
					lettre=Lecteur.sansAccent(lettre);
					char lettre2=lettre.charAt(0);
					String nvmot="";
					
					//Vérification si la lettre est comprise dans le mot et création de l'affichage du mot(nvmot)
					for(int i=0;i<mot.length();i++) {
						if(lettre2==mot.charAt(i)) {
							nvmot+=lettre2+" ";
						}else {
							nvmot+=mad.charAt(i*2)+" ";
						}
					}
					//Récupération des lettres fausses
					List<Character> lf=(List<Character>)session.getAttribute("lf");
					//Vérification si la lettre est dans le mot
					if(nvmot.equals(mad)) {
						//Lettre non présente (augmentation du nombre de tentatives, ajout d'une lettre fausse)
						nbt++;
						lf.add(lettre2);
					}else {
						//Mise à jour du mot à trouver
						mad=nvmot;
					}
				//Enregistrement des lettres fausses
				session.setAttribute("lf", lf);
				
				//Création du mot avec les lettres trouvées
				String verif="";
				for(int i=0;i<mad.length();i=i+2) {
					verif+=mad.charAt(i);
				}
				
				//Vérification si le mot a été trouvé
				if(verif.equals(mot)) {
					//En cas de victoire, on prépare l'affichage de la page
					//
					String vic="Mot Trouvé";
					request.setAttribute("vic", vic);
					//Augmetnation du score
					int score=(int)session.getAttribute("score");
					score+=mot.length();
					session.setAttribute("score", score);
					//Augmentation du nombre de mots trouvés
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
				//Enregistrement données
				session.setAttribute("nbtentatives",nbt);
				session.setAttribute("pendu", pendu);
				session.setAttribute("mad", mad);
				
				//Vérification si défaite
				if(nbt>=10) {
					String defaite="Défaite";
					request.setAttribute("def", defaite);
					
					//Enregistrement du score si dans les dix meilleurs scores
					int score=(int)session.getAttribute("score");
					ScorePendu sp =new ScorePendu();
					sp.setPseudo(" ");
					sp.setScore(score);
					ScorePendu listeSP[]=ScorePendu.read();
					
					//Adapation des scores si un joeur n'a pas laissé de pseudo associé à un score
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
					//récupération des dix premiers scores
					listeSP=Arrays.copyOf(listeSP, 10);
					//Sauvegarde pour afficher le classement dans la jsp
					session.setAttribute("listeScore",listeSP);
					
				}
			
			}else {
				//Cas où l'utilisateur n'aurait pas rentré une lettre
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
