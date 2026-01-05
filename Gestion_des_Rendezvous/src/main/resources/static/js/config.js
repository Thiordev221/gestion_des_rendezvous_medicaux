
const CONFIG = {
    // Détection automatique de l'environnement
    API_BASE_URL: window.location.hostname === 'localhost'
        ? 'http://localhost:8080/api'
        : `${window.location.origin}/api`,

    ENDPOINTS: {
        PATIENTS: '/patients',
        MEDECINS: '/medecins',
        RENDEZVOUS: '/rendezvous'
    },

    APP: {
        NAME: 'Gestion Rendez-vous Médicaux',
        VERSION: '1.0.0'
    },

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

window.CONFIG = CONFIG;