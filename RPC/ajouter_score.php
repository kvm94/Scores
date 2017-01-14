<?php
	/* ETAPE 0 : TINCLUDE DE FONCTIONS ET PARAMETRAGE */
	$GLOBALS['json']=0;
	include('inc/erreurs.inc');



	/* ETAPE 1 : TEST DES PARAMETRES */
	if (!isset($_GET['score']) || empty($_GET['score'])){
		RetournerErreur(100);
	}
	if (!isset($_GET['jeu']) || empty($_GET['jeu'])){
		RetournerErreur(110);
	}
	$score = $_GET['score'];
	$jeu = $_GET['jeu'];
	$id = $_GET['id'];
	


	/* ETAPE 2 : TEST DE LA SESSION */									/*pourquoi??*/
	//include('inc/session.inc');

	

	/* ETAPE 3 : CONNEXION A LA BASE DE DONNEES */
	include('inc/db.inc');



	/* ETAPE 4 : SAUVEGARDE DANS LA DB */
	try{	
		$requete="INSERT INTO scores (jeu, score, id_utilisateur) VALUES (?, ?, ?)";
		$stm= $bdd->prepare($requete);
		$stm->execute(array($jeu, $score, $id));						/*changé par $_id*/
	}catch(Exception $e){
		RetournerErreur(2002);
	}



	/* ETAPE 5 : SI ON EST ARRIVE JUSQU'ICI, C'EST QUE TOUT EST CORRECT */
	echo "0";



	/* Valeurs de retour
	 * 00 : OK
	 * 100 : problème $_GET['score']
	 * 110 : problème $_GET['jeu']
	 * 500 : problème de SESSION
	 * 1000 : problème de connexion à la DB
	 * 20XX : autre problème
	 */
?>
