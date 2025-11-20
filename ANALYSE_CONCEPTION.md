# Analyse de Conception - TP Patterns

## 1. Diagramme de Classes

```
┌─────────────────────────────────────────────────────────────────────┐
│                           <<interface>>                             │
│                              ICours                                 │
├─────────────────────────────────────────────────────────────────────┤
│ + getDescription(): String                                          │
│ + getDuree(): double                                                │
└─────────────────────────────────────────────────────────────────────┘
                                    △
                                    │ implements
                    ┌───────────────┴───────────────┐
                    │                               │
┌───────────────────────────────┐   ┌──────────────────────────────────┐
│          Cours                │   │    <<abstract>>                  │
├───────────────────────────────┤   │    CoursDecorator                │
│ - matiere: String             │   ├──────────────────────────────────┤
│ - enseignant: String          │   │ # coursDecorated: ICours         │
│ - salle: String               │   ├──────────────────────────────────┤
│ - date: String                │   │ + CoursDecorator(cours: ICours)  │
│ - heureDebut: String          │   │ + getDescription(): String       │
│ - estOptionnel: boolean       │   │ + getDuree(): double             │
│ - niveau: String              │   └──────────────────────────────────┘
│ - necessiteProjecteur: boolean│                  △
├───────────────────────────────┤                  │ extends
│ + Cours(...)                  │                  │
│ + getDescription(): String    │   ┌──────────────────────────────────┐
│ + getDuree(): double          │   │       CoursEnLigne               │
│ + getMatiere(): String        │   ├──────────────────────────────────┤
│ + getEnseignant(): String     │   │ + CoursEnLigne(cours: ICours)    │
└───────────────────────────────┘   │ + getDescription(): String       │
            △                       │ + getDuree(): double             |
            │ builds                └──────────────────────────────────┘
            
┌───────────────────────────────┐
│      CoursBuilder             │
├───────────────────────────────┤
│ - matiere: String             │
│ - enseignant: String          │
│ - salle: String               │
│ - date: String                │
│ - heureDebut: String          │
│ - estOptionnel: boolean       │
│ - niveau: String              │
│ - necessiteProjecteur: boolean│
├───────────────────────────────┤
│ + setMatiere(...): CoursBuilder       │
│ + setEnseignant(...): CoursBuilder    │
│ + setSalle(...): CoursBuilder         │
│ + setDate(...): CoursBuilder          │
│ + setHeureDebut(...): CoursBuilder    │
│ + setEstOptionnel(...): CoursBuilder  │
│ + setNiveau(...): CoursBuilder        │
│ + setNecessiteProjecteur(...): CoursBuilder │
│ + build(): Cours              │
└───────────────────────────────┘


┌──────────────────────────────┐        ┌──────────────────────────────┐
│     <<interface>>            │        │     <<interface>>            │
│        Observer              │        │         Subject              │
├──────────────────────────────┤        ├──────────────────────────────┤
│ + update(message: String)    │        │ + attach(o: Observer)        │
└──────────────────────────────┘        │ + detach(o: Observer)        │
            △                            │ + notifyObservers(msg: String) │
            │ implements                 └──────────────────────────────┘
    ┌───────┴────────┐                              △
    │                │                              │ implements
┌───────────────┐  ┌──────────────┐  ┌────────────────────────────────┐
│   Etudiant    │  │ Responsable  │  │  GestionnaireEmploiDuTemps     │
├───────────────┤  ├──────────────┤  ├────────────────────────────────┤
│ - nom: String │  │- nom: String │  │ - listeCours: List<ICours>     │
├───────────────┤  ├──────────────┤  │ - observers: List<Observer>    │
│+ Etudiant(..) │  │+Responsable()│  ├────────────────────────────────┤
│+ update(...)  │  │+ update(...) │  │ + attach(o: Observer)          │
└───────────────┘  └──────────────┘  │ + detach(o: Observer)          │
                                     │ + notifyObservers(msg: String) │
                                     │ + ajouterCours(cours: ICours)  │
                                     │ + modifierCours(...)           │
                                     │ + setChangement(msg: String)   │
                                     └────────────────────────────────┘
```

## 2. Patterns Implémentés

### 2.1 **Builder Pattern** (Création)

- **Classe**: `CoursBuilder`
- **But**: Faciliter la construction d'objets `Cours` complexes avec de nombreux paramètres
- **Avantages**:
  - Code plus lisible
  - Construction progressive
  - Évite les constructeurs avec trop de paramètres

### 2.2 **Decorator Pattern** (Structure)

- **Classes**: `CoursDecorator` (abstract) et `CoursEnLigne`
- **But**: Ajouter dynamiquement des fonctionnalités à un cours sans modifier sa classe
- **Avantages**:
  - Extension flexible des fonctionnalités
  - Respect du principe Ouvert/Fermé
  - Alternative à l'héritage multiple

### 2.3 **Observer Pattern** (Comportement)

- **Interfaces**: `Observer` et `Subject`
- **Classes**: `Etudiant`, `Responsable` (observateurs) et `GestionnaireEmploiDuTemps` (sujet)
- **But**: Notifier automatiquement les observateurs des changements dans l'emploi du temps
- **Avantages**:
  - Couplage faible entre sujet et observateurs
  - Communication one-to-many
  - Facile d'ajouter de nouveaux observateurs

## 3. Analyse des Principes SOLID

### ✅ **S - Single Responsibility Principle (SRP)** - **RESPECTÉ**

- Chaque classe a une responsabilité unique et bien définie :
  - `Cours`: Représente un cours
  - `CoursBuilder`: Construction de cours
  - `GestionnaireEmploiDuTemps`: Gestion de la liste des cours et notification
  - `Etudiant`/`Responsable`: Recevoir des notifications

### ✅ **O - Open/Closed Principle (OCP)** - **RESPECTÉ**

- Le code est ouvert à l'extension mais fermé à la modification :
  - Le pattern **Decorator** permet d'ajouter de nouvelles fonctionnalités (ex: `CoursEnLigne`) sans modifier les classes existantes
  - On peut créer de nouveaux décorateurs sans toucher à `CoursDecorator` ou `Cours`
  - On peut ajouter de nouveaux types d'observateurs sans modifier `GestionnaireEmploiDuTemps`

### ✅ **L - Liskov Substitution Principle (LSP)** - **RESPECTÉ**

- Les sous-classes peuvent remplacer leurs classes parentes sans problème :
  - `CoursEnLigne` peut être utilisé partout où `ICours` est attendu
  - `Etudiant` et `Responsable` peuvent être utilisés interchangeablement comme `Observer`

### ✅ **I - Interface Segregation Principle (ISP)** - **RESPECTÉ**

- Les interfaces sont petites et spécifiques :
  - `ICours`: 2 méthodes seulement (getDescription, getDuree)
  - `Observer`: 1 méthode (update)
  - `Subject`: 3 méthodes liées à la gestion des observateurs
  - Aucune classe n'est forcée d'implémenter des méthodes inutiles

### ⚠️ **D - Dependency Inversion Principle (DIP)** - **PARTIELLEMENT RESPECTÉ**

**Points positifs:**

- `GestionnaireEmploiDuTemps` dépend de l'interface `Observer` et non des classes concrètes
- La liste de cours utilise `ICours` (interface) et non `Cours` directement

**Point d'amélioration:**

- La classe `CoursBuilder` crée directement une instance de `Cours` (dépendance concrète)
- **Suggestion**: On pourrait faire en sorte que le Builder retourne `ICours` au lieu de `Cours`

```java
// Amélioration possible:
public ICours build() {  // Au lieu de: public Cours build()
    return new Cours(...);
}
```

## 4. Autres Principes de Conception

### ✅ **DRY (Don't Repeat Yourself)** - **RESPECTÉ**

- Le pattern Decorator évite la duplication de code en réutilisant les méthodes du cours décoré
- Le pattern Builder évite la duplication de constructeurs multiples

### ✅ **Composition over Inheritance** - **RESPECTÉ**

- Le pattern Decorator utilise la composition (`coursDecorated`) plutôt que l'héritage
- Plus flexible et évite les hiérarchies de classes complexes

### ⚠️ **Law of Demeter (LoD)** - **PARTIELLEMENT RESPECTÉ**

- Les classes parlent principalement aux objets avec lesquels elles collaborent directement
- Pas de chaînage excessif de méthodes visible dans le code

## 5. Points d'Amélioration Suggérés

### 5.1 Validation dans CoursBuilder

```java
public Cours build() {
    if (matiere == null || enseignant == null) {
        throw new IllegalStateException("Matière et enseignant obligatoires");
    }
    return new Cours(...);
}
```

### 5.2 Immutabilité de la classe Cours

- Rendre les attributs `final`
- Supprimer les setters si présents
- Cela garantit la sécurité des threads et évite les modifications accidentelles

### 5.3 Gestion des exceptions

- Ajouter de la gestion d'erreurs dans les méthodes critiques
- Par exemple, vérifier que l'observateur n'est pas null avant de l'ajouter

### 5.4 Documentation

- Ajouter des JavaDoc pour expliquer l'usage des patterns
- Documenter les contrats des interfaces

## 6. Conclusion

**Score global: 9/10** 🎯

Votre code respecte **très bien** les principes de conception logicielle :

- ✅ Les 3 patterns de conception sont correctement implémentés
- ✅ 4.5/5 des principes SOLID sont respectés
- ✅ Le code est maintenable, extensible et testable
- ⚠️ Quelques améliorations mineures possibles (validation, DIP complet)

Le projet démontre une **excellente compréhension** des design patterns et des bonnes pratiques de conception orientée objet ! 👏
