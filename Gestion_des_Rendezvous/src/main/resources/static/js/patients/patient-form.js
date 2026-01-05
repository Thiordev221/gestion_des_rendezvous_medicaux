/**
 * Script pour le formulaire patient (création/modification)
 */

let isEditMode = false;
let patientId = null;

document.addEventListener('DOMContentLoaded', () => {
    checkEditMode();
    setupFormSubmit();
    setupDateValidation();
});

/**
 * Vérifier si on est en mode édition
 */
async function checkEditMode() {
    const urlParams = new URLSearchParams(window.location.search);
    patientId = urlParams.get('id');

    if (patientId) {
        isEditMode = true;
        document.getElementById('form-title').textContent = 'Modifier Patient';
        document.getElementById('submit-text').textContent = 'Mettre à jour';
        await loadPatientData(patientId);
    }
}

/**
 * Charger les données du patient à modifier
 */
async function loadPatientData(id) {
    try {
        const patient = await API.patients.getById(id);

        document.getElementById('patient-id').value = patient.id;
        document.getElementById('nom').value = patient.nom;
        document.getElementById('prenom').value = patient.prenom;
        document.getElementById('dateNaissance').value = patient.dateNaissance;
        document.getElementById('email').value = patient.email;
        document.getElementById('telephone').value = patient.telephone;
        document.getElementById('adresse').value = patient.adresse || '';
        document.getElementById('numeroSecuriteSociale').value = patient.numeroSecuriteSociale || '';

    } catch (error) {
        console.error('Erreur lors du chargement du patient:', error);
        Utils.showToast('Erreur lors du chargement des données du patient', 'danger');
        setTimeout(() => {
            window.location.href = 'patients.html';
        }, 2000);
    }
}

/**
 * Configurer la validation de la date
 */
function setupDateValidation() {
    const dateInput = document.getElementById('dateNaissance');

    // Définir la date maximale à aujourd'hui
    const today = new Date().toISOString().split('T')[0];
    dateInput.max = today;
}

/**
 * Configurer la soumission du formulaire
 */
function setupFormSubmit() {
    const form = document.getElementById('patient-form');

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
            const patientData = getFormData();

            if (isEditMode) {
                await API.patients.update(patientId, patientData);
                Utils.showToast('Patient mis à jour avec succès', 'success');
            } else {
                await API.patients.create(patientData);
                Utils.showToast('Patient créé avec succès', 'success');
            }

            // Rediriger vers la liste après 1 seconde
            setTimeout(() => {
                window.location.href = 'patients.html';
            }, 1000);

        } catch (error) {
            console.error('Erreur:', error);

            let errorMessage = 'Erreur lors de l\'enregistrement du patient';
            if (error.message.includes('email')) {
                errorMessage = 'Cet email est déjà utilisé par un autre patient';
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
    const dateNaissance = document.getElementById('dateNaissance').value;

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

    // Valider la date de naissance (pas dans le futur)
    const birthDate = new Date(dateNaissance);
    const today = new Date();
    if (birthDate > today) {
        Utils.showToast('La date de naissance ne peut pas être dans le futur', 'warning');
        document.getElementById('dateNaissance').focus();
        return false;
    }

    // Vérifier l'âge (au moins 0 ans, maximum 120 ans)
    const age = today.getFullYear() - birthDate.getFullYear();
    if (age > 120) {
        Utils.showToast('La date de naissance semble incorrecte', 'warning');
        document.getElementById('dateNaissance').focus();
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
        dateNaissance: document.getElementById('dateNaissance').value,
        email: document.getElementById('email').value.trim(),
        telephone: document.getElementById('telephone').value.trim(),
        adresse: document.getElementById('adresse').value.trim() || null,
        numeroSecuriteSociale: document.getElementById('numeroSecuriteSociale').value.trim() || null
    };
}