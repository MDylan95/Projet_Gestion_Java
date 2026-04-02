# Améliorations UI/UX — Pépinière Plein de Foin

## Ce qui a été ajouté

### 1. FlatLaf — Look & Feel moderne (`Main.java`)
FlatLaf remplace l'apparence Swing par défaut par un design plat et moderne.
Résultat : boutons nets, scrollbars fines, champs de saisie propres, fenêtres sans bordures datées.

**Comment l'installer dans NetBeans / IntelliJ :**

**Option A — Via Maven** (si le projet utilise Maven, dans `pom.xml`) :
```xml
<dependency>
    <groupId>com.formdev</groupId>
    <artifactId>flatlaf</artifactId>
    <version>3.4.1</version>
</dependency>
```

**Option B — JAR manuel** (recommandé pour ce projet) :
1. Télécharger `flatlaf-3.4.1.jar` sur https://github.com/JFormDesigner/FlatLaf/releases
2. Dans NetBeans : clic droit sur le projet → Properties → Libraries → Add JAR/Folder
3. Sélectionner le fichier `flatlaf-3.4.1.jar`
4. OK → Clean and Build

---

### 2. Notifications Toast (`ToastNotification.java`)
Remplace les `JOptionPane` intrusifs par des notifications discrètes qui :
- Apparaissent en bas à droite avec un effet fade-in
- Disparaissent automatiquement après 3 secondes
- Sont colorées selon le type (vert=succès, rouge=erreur, bleu=info, orange=warning)

**Usage dans le code :**
```java
ToastNotification.success(this, "Article créé avec succès.");
ToastNotification.error(this,   "Erreur de connexion à la base.");
ToastNotification.info(this,    "Valeur du stock : 125 000 FCFA");
ToastNotification.warning(this, "Stock faible pour cet article.");
```

---

### 3. Spinner de chargement (`LoadingSpinner.java`)
Affiche une roue de chargement animée pendant les opérations Oracle longues.
Évite que l'interface semble "gelée" lors d'une requête lente.

**Usage simple :**
```java
LoadingSpinner.run(this, "Chargement des articles...",
    () -> articleDAO.findAll(),           // tâche en arrière-plan
    articles -> refreshTable(articles),   // succès (sur l'EDT)
    e -> ToastNotification.error(this, e.getMessage())  // erreur
);
```

---

### 4. Transitions animées (`FadePanel.java`)
Chaque changement de vue (Articles → Commandes, etc.) s'accompagne d'un
fondu entrant fluide au lieu d'un remplacement brutal.

Intégré automatiquement dans `MainFrame.java` — aucune action requise.

---

### 5. Améliorations des tableaux (`UIStyles.java`)
- Highlight de ligne au survol de la souris
- `setFillsViewportHeight(true)` : le tableau occupe tout l'espace disponible
- `setReorderingAllowed(false)` : colonnes non réordonnables (plus stable)

---

## Fichiers modifiés

| Fichier | Modification |
|---|---|
| `Main.java` | Intégration FlatLaf + configuration globale |
| `MainFrame.java` | FadePanel + Toast + Spinner |
| `UIStyles.java` | Tableaux améliorés |

## Fichiers créés

| Fichier | Rôle |
|---|---|
| `ToastNotification.java` | Notifications non-bloquantes |
| `LoadingSpinner.java` | Indicateur de chargement animé |
| `FadePanel.java` | Transitions fluides entre panneaux |
