/**
 * Configuration globale de l'application
 */

const CONFIG = {
    // URL de base de l'API
    API_BASE_URL: 'http://localhost:8080/api',

    // Endpoints
    ENDPOINTS: {
        PATIENTS: '/patients',
        MEDECINS: '/medecins',
        RENDEZVOUS: '/rendezvous'
    },

    // Configuration de l'application
    APP: {
        NAME: 'Gestion Rendez-vous Médicaux',
        VERSION: '1.0.0'
    },

    // Messages
    MESSAGES: {
        SUCCESS: {
            CREATE: 'Élément créé avec succès',
            UPDATE: 'Élément mis à jour avec succès',
            DELETE: 'Élément supprimé avec succès'
        },
        ERROR: {
            GENERIC: 'Une erreur est survenue',
            NETWORK: 'Erreur de connexion au serveur',
            NOT_FOUND: 'Élément non trouvé'
        }
    }
};

// Rendre CONFIG accessible globalement
window.CONFIG = CONFIG;