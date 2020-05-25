<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Pendu</title>
</head>
<body>
<!--Affichage Score  -->
Nombre de mots trouvés : <c:out value="${sessionScope.nbmt }"/>, score : <c:out value="${sessionScope.score }"/><br>

<!-- Affichage du mot si victoire ou défaite -->
<c:if test="${vic!=null || def!=null }">
<h3>Bonne réponse : <c:out value="${sessionScope.mot}"/></h3>
</c:if>

<!-- Affichage victoire ou défaite -->
<h2><c:out value="${def }"/></h2>
<h2><c:out value="${vic }"/></h2>

<!-- Affichage en cas de victoire -->
<c:if test="${vic!=null }">
<!-- Formualire pour passer au mot suivant -->
<form 	action="Pendu" method="post">
<input type="hidden" name="continuer" value="1">
<button>Continuer</button>
</form>
</c:if>

<!-- Données du jeu -->
<h3>Mot à découvrir : <c:out value="${sessionScope.mad}"/></h3>
<h3>Lettres Tentées Fausses :
<!-- Afficahge des lettres fausses --> 
<c:forEach items="${sessionScope.lf}" var="l">
<c:out value="${l }"/> 
</c:forEach>
</h3>

<!-- Affichage en cas de défaite -->
<c:if test="${def!=null}">
<form action="Pendu" method="post">
<!-- Affichage des dix meilleurs scores -->
<table>
<tr><th>Classement</th><th>Pseudo</th><th>Score</th>
<c:forEach items="${sessionScope.listeScore }" var="s" varStatus="loop">
<tr><th>
<!-- Afficahge de la place -->
<c:out value="${loop.index+1 }"/>.
</th> 
<!-- Score ancien -->
<c:if test="${s.pseudo!=' '}">
<td>
<c:out value="${s.pseudo}"/>
</td>
</c:if>
<!-- Score du joueur -->
<c:if test="${s.pseudo==' '}">
<td>
<!-- Enregistrement du pseudo du joueur -->
<input type="text" name="pseudo" placeholder="Rentrez votre pseudo" required/><br>
<button>Valider</button>
</td>
</c:if>
<td><c:out value="${s.score }"/></td>
</c:forEach>
</table>
<input type="hidden" name="rejouer" value="1">
<button>Rejouer</button>
</form>
</c:if>
<!-- Affichage pour que le joueur propose une lettre -->
<c:if test="${def==null  && vic==null}">
<form action="Pendu" method="post">
<label for="lettre">Proposer une lettre :</label><br>
<c:out value="${message }"/><br>
<input type="text" id="lettre" name="lettre" maxlength="1" required><br>
<button>Valider</button>
</form>
</c:if>
<table>
<!-- Affichage de la potence -->
<c:forEach items="${sessionScope.pendu }" var="p">
	<tr>
	<c:forEach items="${p}" var="e">
	<td><c:out value="${e }"/></td>
	</c:forEach>
	</tr>
</c:forEach>
</table>



<script>
//Selection de la zone de texte pour rentrer la lettre
let a=document.getElementById("lettre");
a.focus();
a.select();
</script>
</body>
</html>