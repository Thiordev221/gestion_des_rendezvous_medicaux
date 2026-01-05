/**
 * Script pour le formulaire médecin (création/modification)
 */

let isEditMode = false;
let medecinId = null;

document.addEventListener('DOMContentLoaded', () => {
    checkEditMode();
    setupFormSubmit();
});

/**
 * Vérifier si on est en mode édition
 */
async function checkEditMode() {
    const urlParams = new URLSearchParams(window.location.search);
    medecinId = urlParams.get('id');

    if (medecinId) {
        isEditMode = true;
        document.getElementById('form-title').textContent = 'Modifier Médecin';
        document.getElementById('submit-text').textContent = 'Mettre à jour';
        await loadMedecinData(medecinId);
    }
}

/**
 * Charger les données du médecin à modifier
 */
async function loadMedecinData(id) {
    try {
        const medecin = await API.medecins.getById(id);

        document.getElementById('medecin-id').value = medecin.id;
        document.getElementById('nom').value = medecin.nom;
        document.getElementById('prenom').value = medecin.prenom;
        document.getElementById('specialite').value = medecin.specialite;
        document.getElementById('email').value = medecin.email;
        document.getElementById('telephone').value = medecin.telephone;
        document.getElementById('adresse').value = medecin.adresse || '';

    } catch (error) {
        console.error('Erreur lors du chargement du médecin:', error);
        Utils.showToast('Erreur lors du chargement des données du médecin', 'danger');
        setTimeout(() => {
            window.location.href = 'medecins.html';
        }, 2000);
    }
}

/**
 * Configurer la soumission du formulaire
 */
function setupFormSubmit() {
    const form = document.getElementById('medecin-form');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        // Validation HTML5
        if (!form.checkValidity()) {
            e.stopPropagation();
            form.classList.add('was-validated');
            return;
        }

        // Validation personnalisée
        if (!validateForm()) {
            return;
        }

        // Désactiver le bouton pendant l'envoi
        const submitBtn = document.getElementById('submit-btn');
        const originalText = document.getElementById('submit-text').textContent;
        submitBtn.disabled = true;
        document.getElementById('submit-text').innerHTML =
            '<span class="spinner-border spinner-border-sm me-2"></span>Enregistrement...';

        try {
            const medecinData = getFormData();

            if (isEditMode) {
                await API.medecins.update(medecinId, medecinData);
                Utils.showToast('Médecin mis à jour avec succès', 'success');
            } else {
                await API.medecins.create(medecinData);
                Utils.showToast('Médecin créé avec succès', 'success');
            }

            // Rediriger vers la liste après 1 seconde
            setTimeout(() => {
                window.location.href = 'medecins.html';
            }, 1000);

        } catch (error) {
            console.error('Erreur:', error);

            let errorMessage = 'Erreur lors de l\'enregistrement du médecin';
            if (error.message.includes('email')) {
                errorMessage = 'Cet email est déjà utilisé par un autre médecin';
            }

            Utils.showToast(errorMessage, 'danger');

            // Réactiver le bouton
            submitBtn.disabled = false;
            document.getElementById('submit-text').textContent = originalText;
        }
    });
}

/**
 * Valider le formulaire
 */
function validateForm() {
    const email = document.getElementById('email').value;
    const telephone = document.getElementById('telephone').value;
    const specialite = document.getElementById('specialite').value;

    // Valider l'email
    if (!Utils.validateEmail(email)) {
        Utils.showToast('L\'email n\'est pas valide', 'warning');
        document.getElementById('email').focus();
        return false;
    }

    // Valider le téléphone
    if (!Utils.validatePhone(telephone)) {
        Utils.showToast('Le numéro de téléphone n\'est pas valide', 'warning');
        document.getElementById('telephone').focus();
        return false;
    }

    // Vérifier la spécialité
    if (specialite === '') {
        Utils.showToast('Veuillez sélectionner une spécialité', 'warning');
        document.getElementById('specialite').focus();
        return false;
    }

    return true;
}

/**
 * Récupérer les données du formulaire
 */
function getFormData() {
    return {
        nom: document.getElementById('nom').value.trim(),
        prenom: document.getElementById('prenom').value.trim(),
        specialite: document.getElementById('specialite').value,
        email: document.getElementById('email').value.trim(),
        telephone: document.getElementById('telephone').value.trim(),
        adresse: document.getElementById('adresse').value.trim() || null
    };
}