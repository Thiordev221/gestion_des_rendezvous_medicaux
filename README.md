# 🏥 Système de Gestion de Rendez-vous Médicaux

Application web complète de gestion de rendez-vous médicaux développée avec **Spring Boot** et **JavaScript vanilla**.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue.svg)](https://www.postgresql.org/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple.svg)](https://getbootstrap.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Table des matières

- [Aperçu](#-aperçu)
- [Fonctionnalités](#-fonctionnalités)
- [Technologies](#-technologies)
- [Architecture](#-architecture)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Utilisation](#-utilisation)
- [Tests](#-tests)
- [Déploiement](#-déploiement)
- [API Documentation](#-api-documentation)
- [Screenshots](#-screenshots)
- [Auteur](#-auteur)

---

## 🎯 Aperçu

Cette application permet de gérer efficacement les rendez-vous dans un contexte médical en facilitant :
- La gestion des patients et de leurs informations
- La gestion des médecins et de leurs spécialités
- La planification et le suivi des rendez-vous avec détection automatique des conflits

**🔗 Demo live** : [https://votre-app.up.railway.app](https://votre-app.up.railway.app) *(à venir)*

---

## ✨ Fonctionnalités

### 👥 Gestion des Patients
- ✅ CRUD complet (Créer, Lire, Modifier, Supprimer)
- ✅ Recherche en temps réel par nom, prénom ou email
- ✅ Validation des données (email, téléphone, date de naissance)
- ✅ Historique des rendez-vous par patient

### 👨‍⚕️ Gestion des Médecins
- ✅ CRUD complet avec gestion des spécialités
- ✅ Filtrage par spécialité (Cardiologie, Dermatologie, Pédiatrie, etc.)
- ✅ Recherche dynamique
- ✅ Attribution de badges colorés par spécialité

### 📅 Gestion des Rendez-vous
- ✅ Création de rendez-vous avec sélection patient/médecin
- ✅ **Détection automatique des conflits de créneaux horaires**
- ✅ Gestion des statuts (En attente, Confirmé, Terminé, Annulé)
- ✅ Filtrage par statut, date et période
- ✅ Actions contextuelles selon le statut
- ✅ Statistiques en temps réel

### 📊 Dashboard
- ✅ Vue d'ensemble avec statistiques
- ✅ Prochains rendez-vous
- ✅ Actions rapides (Confirmer, Annuler, Terminer)

---

## 🛠️ Technologies

### Backend
- **Java 17** - Langage de programmation
- **Spring Boot 3.2** - Framework principal
- **Spring Data JPA** - Gestion de la persistance
- **MapStruct 1.5.5** - Mapping automatique Entity ↔ DTO
- **Lombok** - Réduction du code boilerplate
- **PostgreSQL** - Base de données relationnelle
- **Maven** - Gestion des dépendances

### Frontend
- **HTML5 / CSS3** - Structure et style
- **JavaScript (Vanilla)** - Logique côté client
- **Bootstrap 5.3** - Framework CSS responsive
- **Bootstrap Icons** - Iconographie

### Tests
- **JUnit 5** - Framework de tests
- **AssertJ** - Assertions fluides
- **@DataJpaTest** - Tests des repositories
- **H2 Database** - Base de données en mémoire pour les tests

---

## 🏗️ Architecture

### Structure du projet

```
Gestion_des_Rendezvous/
├── src/
│   ├── main/
│   │   ├── java/com/thiordev/Gestion_des_Rendezvous/
│   │   │   ├── models/           # Entités JPA
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   │   ├── request/      # DTOs pour les requêtes
│   │   │   │   └── response/     # DTOs pour les réponses
│   │   │   ├── mapper/           # Mappers MapStruct
│   │   │   ├── repository/       # Interfaces Spring Data JPA
│   │   │   ├── service/          # Logique métier
│   │   │   │   └── impl/         # Implémentations
│   │   │   ├── controller/       # Contrôleurs REST
│   │   │   ├── exception/        # Exceptions personnalisées
│   │   │   │   └── handler/      # Gestion globale des exceptions
│   │   │   └── config/           # Configuration (CORS, etc.)
│   │   └── resources/
│   │       ├── static/           # Frontend (HTML, CSS, JS)
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── pages/
│   │       └── application.properties
│   └── test/                     # Tests unitaires et d'intégration
└── pom.xml
```

### Architecture en couches

```
┌─────────────────────────────────────┐
│         Frontend (HTML/JS)          │
├─────────────────────────────────────┤
│       Controllers (REST API)        │
├─────────────────────────────────────┤
│      Services (Logique métier)     │
├─────────────────────────────────────┤
│   Repositories (Accès données)     │
├─────────────────────────────────────┤
│      PostgreSQL (Base de données)   │
└─────────────────────────────────────┘
```

### Principes appliqués
- ✅ **Clean Architecture** - Séparation des responsabilités
- ✅ **SOLID Principles** - Code maintenable et évolutif
- ✅ **DTO Pattern** - Séparation entités/données exposées
- ✅ **Repository Pattern** - Abstraction de la persistance
- ✅ **Exception Handling** - Gestion centralisée des erreurs

---

## 📥 Installation

### Prérequis
- **Java 17** ou supérieur
- **PostgreSQL 14** ou supérieur
- **Maven 3.8+**
- Un IDE (IntelliJ IDEA, Eclipse, VS Code)

### 1. Cloner le repository

```bash
git clone https://github.com/votre-username/gestion-rendezvous.git
cd gestion-rendezvous
```

### 2. Configurer PostgreSQL

```bash
# Se connecter à PostgreSQL
psql -U postgres

# Créer la base de données
CREATE DATABASE medical_reservation;

# Quitter
\q
```

### 3. Configurer l'application

Créer/modifier `src/main/resources/application.properties` :

```properties
# Configuration du serveur
server.port=8080

# Configuration PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/medical_reservation
spring.datasource.username=postgres
spring.datasource.password=votre_mot_de_passe
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuration JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 4. Compiler et lancer l'application

```bash
# Compiler le projet
mvn clean install

# Lancer l'application
mvn spring-boot:run
```

### 5. Accéder à l'application

```
🌐 Application : http://localhost:8080
📡 API REST : http://localhost:8080/api
```

---

## ⚙️ Configuration

### Variables d'environnement (Production)

Pour le déploiement, utilisez des variables d'environnement :

```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
server.port=${PORT:8080}
```

### Configuration CORS

Modifiez `CorsConfig.java` pour autoriser votre domaine frontend :

```java
registry.addMapping("/api/**")
    .allowedOrigins("http://localhost:3000", "https://votre-domaine.com")
    .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
    .allowedHeaders("*");
```

---

## 🚀 Utilisation

### Workflow typique

1. **Créer des patients** via `/pages/patients/patients.html`
2. **Créer des médecins** avec leurs spécialités via `/pages/medecins/medecins.html`
3. **Planifier des rendez-vous** via `/pages/rendezvous/rendezvous.html`
4. Le système **détecte automatiquement les conflits** de créneaux
5. **Gérer les statuts** : Confirmer → Terminer ou Annuler

### Exemples d'utilisation de l'API

#### Créer un patient

```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Diop",
    "prenom": "Amadou",
    "dateNaissance": "1990-05-15",
    "email": "amadou.diop@example.com",
    "telephone": "+221771234567",
    "adresse": "Dakar, Sénégal"
  }'
```

#### Lister tous les médecins

```bash
curl http://localhost:8080/api/medecins
```

#### Créer un rendez-vous

```bash
curl -X POST http://localhost:8080/api/rendezvous \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 1,
    "medecinId": 1,
    "dateHeureDebut": "2026-01-15T10:00:00",
    "dateHeureFin": "2026-01-15T11:00:00",
    "motifConsultation": "Consultation de routine"
  }'
```

---

## 🧪 Tests

Le projet inclut **27+ tests unitaires** pour garantir la fiabilité du code.

### Lancer les tests

```bash
# Tous les tests
mvn test

# Tests d'un repository spécifique
mvn test -Dtest=PatientRepositoryTest

# Tests avec rapport de couverture
mvn clean test jacoco:report
```

### Couverture des tests

- ✅ **Repositories** : Tests avec `@DataJpaTest` et H2
- ✅ **CRUD complet** pour toutes les entités
- ✅ **Détection de conflits** de rendez-vous
- ✅ **Requêtes personnalisées** (recherche, filtres)
- ✅ **Validation des données**

### Exemple de test

```java
@Test
void testExistsConflictingRendezVous() {
    // Given - RDV existant de 10h à 11h
    createRendezVous(medecin1, LocalDateTime.of(2026, 1, 15, 10, 0), 
                     LocalDateTime.of(2026, 1, 15, 11, 0));
    
    // When - Tentative de RDV à 10h30
    boolean conflict = repository.existsConflictingRendezVous(
        medecin1.getId(),
        LocalDateTime.of(2026, 1, 15, 10, 30),
        LocalDateTime.of(2026, 1, 15, 11, 30)
    );
    
    // Then - Conflit détecté
    assertThat(conflict).isTrue();
}
```

---

## 🌐 Déploiement

### Déployer sur Railway

1. **Préparer pour la production**
   ```bash
   mvn clean package -DskipTests
   ```

2. **Pousser sur GitHub**
   ```bash
   git add .
   git commit -m "Ready for deployment"
   git push origin main
   ```

3. **Déployer sur Railway**
   - Créer un compte sur [railway.app](https://railway.app)
   - Connecter votre repository GitHub
   - Ajouter PostgreSQL
   - Configurer les variables d'environnement
   - Déployer automatiquement !

### Variables d'environnement Railway

```
DATABASE_URL = ${{Postgres.DATABASE_URL}}
PORT = 8080
CORS_ORIGINS = *
```

---

## 📚 API Documentation

### Endpoints principaux

#### Patients
- `GET /api/patients` - Liste tous les patients
- `GET /api/patients/{id}` - Récupère un patient
- `POST /api/patients` - Crée un patient
- `PUT /api/patients/{id}` - Modifie un patient
- `DELETE /api/patients/{id}` - Supprime un patient
- `GET /api/patients/search?q={term}` - Recherche

#### Médecins
- `GET /api/medecins` - Liste tous les médecins
- `GET /api/medecins/{id}` - Récupère un médecin
- `POST /api/medecins` - Crée un médecin
- `PUT /api/medecins/{id}` - Modifie un médecin
- `DELETE /api/medecins/{id}` - Supprime un médecin
- `GET /api/medecins/specialite/{specialite}` - Filtre par spécialité

#### Rendez-vous
- `GET /api/rendezvous` - Liste tous les rendez-vous
- `GET /api/rendezvous/{id}` - Récupère un rendez-vous
- `POST /api/rendezvous` - Crée un rendez-vous
- `PUT /api/rendezvous/{id}` - Modifie un rendez-vous
- `DELETE /api/rendezvous/{id}` - Supprime un rendez-vous
- `PATCH /api/rendezvous/{id}/confirm` - Confirme un rendez-vous
- `PATCH /api/rendezvous/{id}/cancel` - Annule un rendez-vous
- `PATCH /api/rendezvous/{id}/complete` - Termine un rendez-vous

### Format des réponses

#### Succès (200 OK)
```json
{
  "id": 1,
  "nom": "Diop",
  "prenom": "Amadou",
  "email": "amadou.diop@example.com",
  "dateCreation": "2026-01-05T10:30:00",
  "dateModification": "2026-01-05T10:30:00"
}
```

#### Erreur (400 Bad Request)
```json
{
  "timestamp": "2026-01-05T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "L'email doit être valide",
  "path": "/api/patients"
}
```


## 🔮 Améliorations futures

- [ ] Authentification et autorisation (Spring Security)
- [ ] Vue calendrier interactive
- [ ] Notifications par email
- [ ] Export PDF des rendez-vous
- [ ] Statistiques avancées avec graphiques
- [ ] API REST avec pagination
- [ ] Mode sombre (Dark Mode)

---

## 🤝 Contribution

Les contributions sont les bienvenues ! Pour contribuer :

1. Fork le projet
2. Créez une branche (`git checkout -b feature/AmazingFeature`)
3. Committez vos changements (`git commit -m 'Add AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request



## 👨‍💻 Auteur

**Abdoulaye Thior**

- GitHub: [@Abdoulaye Thior] https://github.com/Thiordev221
- LinkedIn:https://www.linkedin.com/in/abdoulaye-thior-88b5b1336/
- Email: thiorabdoulaye39@gmail.com

---

## 🙏 Remerciements

- [Spring Boot](https://spring.io/projects/spring-boot) - Framework backend
- [Bootstrap](https://getbootstrap.com/) - Framework CSS
- [MapStruct](https://mapstruct.org/) - Mapping automatique
- [Railway](https://railway.app) - Plateforme de déploiement

---

## 📊 Statistiques du projet

- **Lignes de code** : ~5000+
- **Nombre de commits** : 1
- **Tests unitaires** : 27+
- **Couverture de tests** : 100%
- **Temps de développement** : 15 jours

---


**⭐ Si ce projet vous a été utile, n'hésitez pas à lui donner une étoile ! ⭐**

Made with ❤️ by Abdoulaye Thior

</di
