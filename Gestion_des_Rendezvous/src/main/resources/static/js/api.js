/**
 * Client API pour communiquer avec le backend
 */

let API = {
    /**
     * Fonction générique pour faire des requêtes
     */
    async request(endpoint, options = {}) {
        const url = `${CONFIG.API_BASE_URL}${endpoint}`;

        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json'
            }
        };

        const finalOptions = { ...defaultOptions, ...options };

        try {
            const response = await fetch(url, finalOptions);

            // Si la réponse n'est pas OK, lancer une erreur
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || 'Erreur lors de la requête');
            }

            // Si c'est un DELETE avec 204, pas de contenu à parser
            if (response.status === 204) {
                return null;
            }

            return await response.json();
        } catch (error) {
            console.error('Erreur API:', error);
            throw error;
        }
    },

    /**
     * API Patients
     */
    patients: {
        getAll() {
            return API.request(CONFIG.ENDPOINTS.PATIENTS);
        },

        getById(id) {
            return API.request(`${CONFIG.ENDPOINTS.PATIENTS}/${id}`);
        },

        create(data) {
            return API.request(CONFIG.ENDPOINTS.PATIENTS, {
                method: 'POST',
                body: JSON.stringify(data)
            });
        },

        update(id, data) {
            return API.request(`${CONFIG.ENDPOINTS.PATIENTS}/${id}`, {
                method: 'PUT',
                body: JSON.stringify(data)
            });
        },

        delete(id) {
            return API.request(`${CONFIG.ENDPOINTS.PATIENTS}/${id}`, {
                method: 'DELETE'
            });
        },

        search(term) {
            return API.request(`${CONFIG.ENDPOINTS.PATIENTS}/search?q=${term}`);
        },

        count() {
            return API.request(`${CONFIG.ENDPOINTS.PATIENTS}/count`);
        }
    },

    /**
     * API Médecins
     */
    medecins: {
        getAll() {
            return API.request(CONFIG.ENDPOINTS.MEDECINS);
        },

        getById(id) {
            return API.request(`${CONFIG.ENDPOINTS.MEDECINS}/${id}`);
        },

        create(data) {
            return API.request(CONFIG.ENDPOINTS.MEDECINS, {
                method: 'POST',
                body: JSON.stringify(data)
            });
        },

        update(id, data) {
            return API.request(`${CONFIG.ENDPOINTS.MEDECINS}/${id}`, {
                method: 'PUT',
                body: JSON.stringify(data)
            });
        },

        delete(id) {
            return API.request(`${CONFIG.ENDPOINTS.MEDECINS}/${id}`, {
                method: 'DELETE'
            });
        },

        getBySpecialite(specialite) {
            return API.request(`${CONFIG.ENDPOINTS.MEDECINS}/specialite/${specialite}`);
        },

        search(term) {
            return API.request(`${CONFIG.ENDPOINTS.MEDECINS}/search?q=${term}`);
        }
    },

    /**
     * API Rendez-vous
     */
    rendezvous: {
        getAll() {
            return API.request(CONFIG.ENDPOINTS.RENDEZVOUS);
        },

        getById(id) {
            return API.request(`${CONFIG.ENDPOINTS.RENDEZVOUS}/${id}`);
        },

        create(data) {
            return API.request(CONFIG.ENDPOINTS.RENDEZVOUS, {
                method: 'POST',
                body: JSON.stringify(data)
            });
        },

        update(id, data) {
            return API.request(`${CONFIG.ENDPOINTS.RENDEZVOUS}/${id}`, {
                method: 'PUT',
                body: JSON.stringify(data)
            });
        },

        delete(id) {
            return API.request(`${CONFIG.ENDPOINTS.RENDEZVOUS}/${id}`, {
                method: 'DELETE'
            });
        },

        cancel(id) {
            return API.request(`${CONFIG.ENDPOINTS.RENDEZVOUS}/${id}/cancel`, {
                method: 'PATCH'
            });
        },

        confirm(id) {
            return API.request(`${CONFIG.ENDPOINTS.RENDEZVOUS}/${id}/confirm`, {
                method: 'PATCH'
            });
        },

        complete(id) {
            return API.request(`${CONFIG.ENDPOINTS.RENDEZVOUS}/${id}/complete`, {
                method: 'PATCH'
            });
        },

        getByPatient(patientId) {
            return API.request(`${CONFIG.ENDPOINTS.RENDEZVOUS}/patient/${patientId}`);
        },

        getByMedecin(medecinId) {
            return API.request(`${CONFIG.ENDPOINTS.RENDEZVOUS}/medecin/${medecinId}`);
        },

        getByStatut(statut) {
            return API.request(`${CONFIG.ENDPOINTS.RENDEZVOUS}/statut/${statut}`);
        },

        getUpcomingByPatient(patientId) {
            return API.request(`${CONFIG.ENDPOINTS.RENDEZVOUS}/patient/${patientId}/upcoming`);
        },

        getUpcomingByMedecin(medecinId) {
            return API.request(`${CONFIG.ENDPOINTS.RENDEZVOUS}/medecin/${medecinId}/upcoming`);
        },

        countByStatut(statut) {
            return API.request(`${CONFIG.ENDPOINTS.RENDEZVOUS}/count/statut/${statut}`);
        }
    }
};

// Rendre API accessible globalement
window.API = API;