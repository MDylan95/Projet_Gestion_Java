# Pépinière "Plein de Foin" - Application de Gestion Commerciale

Application Java Swing moderne de gestion commerciale pour une pépinière, connectée à une base de données Oracle. Interface élégante avec FlatLaf, notifications non-bloquantes et animations fluides.

## 🎨 Améliorations UX/UI

### FlatLaf Look & Feel
- Interface moderne et cohérente
- Boutons nets avec coins arrondis
- Scrollbars fines et discrètes
- Palette de couleurs professionnelle

### Composants personnalisés
- **ToastNotification** : Notifications non-bloquantes (succès, erreur, warning, info)
- **LoadingSpinner** : Indicateur de chargement animé pour les opérations longues
- **FadePanel** : Transitions fluides entre les vues
- **AnimatedCard** : Cartes avec animations hover
- **SidebarButton** : Boutons de navigation avec icônes et animations
- **ValidatedTextField** : Champs avec validation en temps réel
- **IconUtils** : Icônes textuelles pour toute l'application

### Améliorations visuelles
- Tableaux avec alternances de couleurs
- Highlight au survol des lignes
- En-têtes stylisés avec meilleur contraste
- Dashboard avec cartes de statistiques
- Sidebar avec navigation claire

## Fonctionnalités

### Profil MAGASINIER
- Création d'articles (utilisation de la séquence Oracle `seq_article`)
- Consultation des commandes (lecture seule)
- Consultation des clients (lecture seule)

### Profil RESPONSABLE
- Accès complet à toutes les fonctionnalités
- Gestion des articles (CRUD complet)
- Gestion des clients (CRUD complet)
- Gestion des commandes et lignes de commande
- Gestion des livraisons
- Augmentation des prix via procédure Oracle `augmenter_prix`
- Consultation de la valeur du stock (calcul direct)
- Calcul du montant par client via fonction `montant_client`

## Prérequis

- **Java JDK 8** ou supérieur
- **Oracle Database** (XE ou autre)
- **Driver JDBC Oracle** (`ojdbc8.jar`)
- **FlatLaf** (`flatlaf-3.4.1.jar`)

## Structure du Projet

```
PleinDeFoin/
├── src/
│   └── com/
│       └── pepiniere/
│           ├── Main.java
│           ├── model/
│           │   ├── Article.java
│           │   ├── Client.java
│           │   ├── Commande.java
│           │   ├── LigneCommande.java
│           │   ├── Livraison.java
│           │   ├── DetailLivraison.java
│           │   └── UserRole.java
│           ├── dao/
│           │   ├── ArticleDAO.java
│           │   ├── ClientDAO.java
│           │   ├── CommandeDAO.java
│           │   └── LivraisonDAO.java
│           ├── ui/
│           │   ├── LoginFrame.java
│           │   ├── MainFrame.java
│           │   ├── ArticleFormPanel.java
│           │   ├── ArticleListPanel.java
│           │   ├── ClientFormPanel.java
│           │   ├── ClientListPanel.java
│           │   ├── CommandeFormPanel.java
│           │   ├── CommandeListPanel.java
│           │   ├── LivraisonFormPanel.java
│           │   ├── LivraisonListPanel.java
│           │   └── MontantClientPanel.java
│           └── util/
│               ├── DatabaseConnection.java
│               └── SessionManager.java
├── lib/
│   └── ojdbc8.jar (à ajouter)
└── README.md
```

## Configuration de la Base de Données

### Schéma Oracle (utilisateur Dev)

```sql
-- Tables
CREATE TABLE Client (
    noClient NUMBER PRIMARY KEY,
    nomClient VARCHAR2(100),
    noTelephone VARCHAR2(20)
);

CREATE TABLE Article (
    noArticle NUMBER PRIMARY KEY,
    description VARCHAR2(200),
    prixUnitaire NUMBER(10,2),
    quantiteEnStock NUMBER
);

CREATE TABLE Commande (
    noCommande NUMBER PRIMARY KEY,
    dateCommande DATE,
    noClient NUMBER REFERENCES Client(noClient)
);

CREATE TABLE LigneCommande (
    noCommande NUMBER REFERENCES Commande(noCommande),
    noArticle NUMBER REFERENCES Article(noArticle),
    quantite NUMBER,
    PRIMARY KEY (noCommande, noArticle)
);

CREATE TABLE Livraison (
    noLivraison NUMBER PRIMARY KEY,
    dateLivraison DATE
);

CREATE TABLE DetailLivraison (
    noLivraison NUMBER REFERENCES Livraison(noLivraison),
    noCommande NUMBER,
    noArticle NUMBER,
    quantiteLivree NUMBER,
    PRIMARY KEY (noLivraison, noCommande, noArticle)
);

-- Séquence pour les articles
CREATE SEQUENCE seq_article START WITH 1 INCREMENT BY 1;

-- Vue pour la valeur du stock
CREATE OR REPLACE VIEW STVALEUR AS
SELECT SUM(prixUnitaire * quantiteEnStock) AS valeur_totale FROM Article;

-- Procédure pour augmenter les prix
CREATE OR REPLACE PROCEDURE augmenter_prix(p_pourcentage IN NUMBER) AS
BEGIN
    UPDATE Article SET prixUnitaire = prixUnitaire * (1 + p_pourcentage/100);
    COMMIT;
END;
/

-- Fonction pour calculer le montant d'un client
CREATE OR REPLACE FUNCTION montant_client(p_noClient IN NUMBER) RETURN NUMBER AS
    v_montant NUMBER := 0;
BEGIN
    SELECT NVL(SUM(lc.quantite * a.prixUnitaire), 0)
    INTO v_montant
    FROM Commande c
    JOIN LigneCommande lc ON c.noCommande = lc.noCommande
    JOIN Article a ON lc.noArticle = a.noArticle
    WHERE c.noClient = p_noClient;
    
    RETURN v_montant;
END;
/
```

## Compilation et Exécution

### Compilation

**Sous Windows (CMD) :**
```cmd
javac -encoding UTF-8 -cp lib/ojdbc8.jar;lib/flatlaf-3.4.1.jar -d bin @sources.txt
```

**Sous Linux/Mac (Bash) :**
```bash
javac -encoding UTF-8 -cp lib/ojdbc8.jar:lib/flatlaf-3.4.1.jar -d bin src/com/pepiniere/**/*.java src/com/pepiniere/*.java
```

### Exécution

**Sous Windows :**
```cmd
java -cp bin;lib/ojdbc8.jar;lib/flatlaf-3.4.1.jar com.pepiniere.Main
```

**Sous Linux/Mac :**
```bash
java -cp bin:lib/ojdbc8.jar:lib/flatlaf-3.4.1.jar com.pepiniere.Main
```

## Configuration de la Connexion

Modifier les paramètres dans `DatabaseConnection.java` :
- `URL` : URL de connexion Oracle (par défaut: `jdbc:oracle:thin:@localhost:1521:XE`)
- `DEFAULT_USER` : Utilisateur par défaut (par défaut: `Dev`)
- `DEFAULT_PASSWORD` : Mot de passe par défaut (par défaut: `Dev`)

## Utilisation

1. Lancer l'application
2. Se connecter avec les identifiants Oracle
3. Sélectionner le profil (Magasinier ou Responsable)
4. Utiliser les menus selon les droits d'accès

## Architecture

- **Pattern DAO** : Séparation de la logique SQL de l'interface graphique
- **Singleton** : Pour la gestion de la connexion et de la session
- **Swing** : Interface graphique Java native avec FlatLaf
- **Composants personnalisés** : ToastNotification, LoadingSpinner, FadePanel, etc.

## Documentation

- **README.md** : Ce fichier, guide principal du projet
- **AMELIORATIONS_UXUI.md** : Documentation détaillée des améliorations UX/UI
- **sql/schema.sql** : Schéma complet de la base de données Oracle

## Fichiers clés

### Configuration
- `.gitignore` : Fichiers ignorés par Git
- `sources.txt` : Liste des fichiers source Java à compiler
- `download_flatlaf_jar.ps1` : Script pour télécharger FlatLaf

### Dépendances
- `lib/ojdbc8.jar` : Driver Oracle JDBC
- `lib/flatlaf-3.4.1.jar` : FlatLaf Look & Feel

## Dépannage

### Erreur : "ORA-00904: identifiant non valide"
- Vérifier que les noms de colonnes utilisent les majuscules (ex: `QUANTITEENSTOCK`)
- Consulter le schéma SQL pour les noms exacts des colonnes

### Erreur de connexion Oracle
- Vérifier que le service Oracle est actif
- Vérifier les paramètres de connexion dans `DatabaseConnection.java`
- Vérifier que le driver JDBC est présent dans `lib/`

### Icônes non visibles
- Les icônes utilisent des caractères Unicode simples et universels
- Vérifier que la police Segoe UI est disponible sur le système

## Auteur

Application développée pour la pépinière "Plein de Foin"

## Licence

Projet privé - Tous droits réservés
