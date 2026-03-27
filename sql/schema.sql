-- ============================================
-- Script SQL pour la base de données Oracle
-- Pépinière "Plein de Foin"
-- ============================================

-- Suppression des objets existants (optionnel)
-- DROP TABLE DetailLivraison CASCADE CONSTRAINTS;
-- DROP TABLE Livraison CASCADE CONSTRAINTS;
-- DROP TABLE LigneCommande CASCADE CONSTRAINTS;
-- DROP TABLE Commande CASCADE CONSTRAINTS;
-- DROP TABLE Article CASCADE CONSTRAINTS;
-- DROP TABLE Client CASCADE CONSTRAINTS;
-- DROP SEQUENCE seq_article;
-- DROP VIEW STVALEUR;
-- DROP PROCEDURE augmenter_prix;
-- DROP FUNCTION montant_client;

-- ============================================
-- TABLES
-- ============================================

CREATE TABLE Client (
    noClient NUMBER PRIMARY KEY,
    nomClient VARCHAR2(100) NOT NULL,
    noTelephone VARCHAR2(20)
);

CREATE TABLE Article (
    noArticle NUMBER PRIMARY KEY,
    description VARCHAR2(200) NOT NULL,
    prixUnitaire NUMBER(10,2) NOT NULL,
    quantiteEnStock NUMBER DEFAULT 0
);

CREATE TABLE Commande (
    noCommande NUMBER PRIMARY KEY,
    dateCommande DATE DEFAULT SYSDATE,
    noClient NUMBER NOT NULL,
    CONSTRAINT fk_commande_client FOREIGN KEY (noClient) REFERENCES Client(noClient)
);

CREATE TABLE LigneCommande (
    noCommande NUMBER NOT NULL,
    noArticle NUMBER NOT NULL,
    quantite NUMBER NOT NULL,
    CONSTRAINT pk_ligne_commande PRIMARY KEY (noCommande, noArticle),
    CONSTRAINT fk_ligne_commande FOREIGN KEY (noCommande) REFERENCES Commande(noCommande),
    CONSTRAINT fk_ligne_article FOREIGN KEY (noArticle) REFERENCES Article(noArticle)
);

CREATE TABLE Livraison (
    noLivraison NUMBER PRIMARY KEY,
    dateLivraison DATE DEFAULT SYSDATE
);

CREATE TABLE DetailLivraison (
    noLivraison NUMBER NOT NULL,
    noCommande NUMBER NOT NULL,
    noArticle NUMBER NOT NULL,
    quantiteLivree NUMBER NOT NULL,
    CONSTRAINT pk_detail_livraison PRIMARY KEY (noLivraison, noCommande, noArticle),
    CONSTRAINT fk_detail_livraison FOREIGN KEY (noLivraison) REFERENCES Livraison(noLivraison),
    CONSTRAINT fk_detail_commande FOREIGN KEY (noCommande, noArticle) REFERENCES LigneCommande(noCommande, noArticle)
);

-- ============================================
-- SEQUENCE
-- ============================================

CREATE SEQUENCE seq_article
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================
-- VUE
-- ============================================

CREATE OR REPLACE VIEW STVALEUR AS
SELECT SUM(prixUnitaire * quantiteEnStock) AS valeur_totale 
FROM Article;

-- ============================================
-- PROCEDURE
-- ============================================

CREATE OR REPLACE PROCEDURE augmenter_prix(p_pourcentage IN NUMBER) AS
BEGIN
    UPDATE Article 
    SET prixUnitaire = prixUnitaire * (1 + p_pourcentage / 100);
    COMMIT;
END augmenter_prix;
/

-- ============================================
-- FONCTION
-- ============================================

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
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 0;
END montant_client;
/

-- ============================================
-- DONNEES DE TEST
-- ============================================

-- Clients
INSERT INTO Client (noClient, nomClient, noTelephone) VALUES (1, 'Dupont Jean', '0123456789');
INSERT INTO Client (noClient, nomClient, noTelephone) VALUES (2, 'Martin Marie', '0234567890');
INSERT INTO Client (noClient, nomClient, noTelephone) VALUES (3, 'Bernard Pierre', '0345678901');

-- Articles (utilisation de la séquence)
INSERT INTO Article (noArticle, description, prixUnitaire, quantiteEnStock) 
VALUES (seq_article.NEXTVAL, 'Rosier grimpant', 25.50, 50);
INSERT INTO Article (noArticle, description, prixUnitaire, quantiteEnStock) 
VALUES (seq_article.NEXTVAL, 'Lavande', 8.90, 100);
INSERT INTO Article (noArticle, description, prixUnitaire, quantiteEnStock) 
VALUES (seq_article.NEXTVAL, 'Olivier', 45.00, 20);
INSERT INTO Article (noArticle, description, prixUnitaire, quantiteEnStock) 
VALUES (seq_article.NEXTVAL, 'Hortensia', 15.00, 75);
INSERT INTO Article (noArticle, description, prixUnitaire, quantiteEnStock) 
VALUES (seq_article.NEXTVAL, 'Foin de prairie', 12.00, 200);

-- Commandes
INSERT INTO Commande (noCommande, dateCommande, noClient) VALUES (1, SYSDATE - 10, 1);
INSERT INTO Commande (noCommande, dateCommande, noClient) VALUES (2, SYSDATE - 5, 2);
INSERT INTO Commande (noCommande, dateCommande, noClient) VALUES (3, SYSDATE - 2, 1);

-- Lignes de commande
INSERT INTO LigneCommande (noCommande, noArticle, quantite) VALUES (1, 1, 5);
INSERT INTO LigneCommande (noCommande, noArticle, quantite) VALUES (1, 2, 10);
INSERT INTO LigneCommande (noCommande, noArticle, quantite) VALUES (2, 3, 2);
INSERT INTO LigneCommande (noCommande, noArticle, quantite) VALUES (2, 4, 8);
INSERT INTO LigneCommande (noCommande, noArticle, quantite) VALUES (3, 5, 15);

-- Livraisons
INSERT INTO Livraison (noLivraison, dateLivraison) VALUES (1, SYSDATE - 8);

-- Détails de livraison
INSERT INTO DetailLivraison (noLivraison, noCommande, noArticle, quantiteLivree) VALUES (1, 1, 1, 3);
INSERT INTO DetailLivraison (noLivraison, noCommande, noArticle, quantiteLivree) VALUES (1, 1, 2, 10);

COMMIT;

-- ============================================
-- VERIFICATION
-- ============================================

-- Test de la vue
SELECT * FROM STVALEUR;

-- Test de la fonction
SELECT montant_client(1) AS montant_client_1 FROM DUAL;
SELECT montant_client(2) AS montant_client_2 FROM DUAL;
