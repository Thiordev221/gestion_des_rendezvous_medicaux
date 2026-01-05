/**
 * Fonctions utilitaires réutilisables
 */

let Utils = {
    /**
     * Formater une date au format français
     */
    formatDate(dateString) {
        if (!dateString) return '-';
        const date = new Date(dateString);
        return date.toLocaleDateString('fr-FR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    },

    /**
     * Formater une date et heure
     */
    formatDateTime(dateString) {
        if (!dateString) return '-';
        const date = new Date(dateString);
        return date.toLocaleDateString('fr-FR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    },

    /**
     * Formater un numéro de téléphone
     */
    formatPhone(phone) {
        if (!phone) return '-';
        // Format: +221 77 123 45 67
        return phone.replace(/(\+221)(\d{2})(\d{3})(\d{2})(\d{2})/, '$1 $2 $3 $4 $5');
    },

    /**
     * Afficher un toast (notification)
     */
    showToast(message, type = 'success') {
        const toastContainer = document.getElementById('toast-container');

        const toastHtml = `
            <div class="toast align-items-center text-white bg-${type} border-0" role="alert">
                <div class="d-flex">
                    <div class="toast-body">
                        ${message}
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" 
                            data-bs-dismiss="toast"></button>
                </div>
            </div>
        `;

        toastContainer.insertAdjacentHTML('beforeend', toastHtml);

        const toastElement = toastContainer.lastElementChild;
        const toast = new bootstrap.Toast(toastElement, { delay: 3000 });
        toast.show();

        // Supprimer l'élément après fermeture
        toastElement.addEventListener('hidden.bs.toast', () => {
            toastElement.remove();
        });
    },

    /**
     * Afficher une boîte de dialogue de confirmation
     */
    async showConfirmDialog(message) {
        return new Promise((resolve) => {
            const result = confirm(message);
            resolve(result);
        });
    },

    /**
     * Afficher un spinner de chargement
     */
    showLoading(containerId) {
        const container = document.getElementById(containerId);
        if (container) {
            container.innerHTML = `
                <div class="text-center my-5">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">Chargement...</span>
                    </div>
                    <p class="mt-2">Chargement en cours...</p>
                </div>
            `;
        }
    },

    /**
     * Afficher un message d'erreur
     */
    showError(containerId, message) {
        const container = document.getElementById(containerId);
        if (container) {
            container.innerHTML = `
                <div class="alert alert-danger" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    ${message}
                </div>
            `;
        }
    },

    /**
     * Obtenir le badge HTML pour un statut de rendez-vous
     */
    getStatutBadge(statut) {
        const badges = {
            'EN_ATTENTE': '<span class="badge bg-warning text-dark">En attente</span>',
            'CONFIRME': '<span class="badge bg-success">Confirmé</span>',
            'ANNULE': '<span class="badge bg-danger">Annulé</span>',
            'TERMINE': '<span class="badge bg-secondary">Terminé</span>'
        };
        return badges[statut] || '<span class="badge bg-secondary">Inconnu</span>';
    },

    /**
     * Valider un email
     */
    validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    },

    /**
     * Valider un téléphone sénégalais
     */
    validatePhone(phone) {
        const re = /^(\+221|00221)?[0-9]{9}$/;
        return re.test(phone.replace(/\s/g, ''));
    },

    /**
     * Calculer l'âge à partir d'une date de naissance
     */
    calculateAge(birthDate) {
        if (!birthDate) return '-';
        const today = new Date();
        const birth = new Date(birthDate);
        let age = today.getFullYear() - birth.getFullYear();
        const monthDiff = today.getMonth() - birth.getMonth();

        if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
            age--;
        }

        return age + ' ans';
    }
};

// Rendre Utils accessible globalement
window.Utils = Utils;