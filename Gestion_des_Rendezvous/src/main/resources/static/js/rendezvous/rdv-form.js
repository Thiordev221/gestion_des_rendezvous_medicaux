/**
 * Script pour le formulaire rendez-vous (création/modification)
 */

let isEditMode = false;
let rdvId = null;
let patients = [];
let medecins = [];

document.addEventListener('DOMContentLoaded', async () => {
    await loadPatientsAndMedecins();
    checkEditMode();
    setupFormSubmit();
    setupDateTimeValidation();
    setupAvailabilityCheck();
});

/**
 * Charger les patients et médecins
 */
async function loadPatientsAndMedecins() {
    try {
        [patients, medecins] = await Promise.all([
            API.patients.getAll(),
            API.medecins.getAll()
        ]);

        populatePatientSelect();
        populateMedecinSelect();

    } catch (error) {
        console.error('Erreur lors du chargement:', error);
        Utils.showToast('Erreur lors du chargement des données', 'danger');
    }
}

/**
 * Remplir le select des patients
 */
function populatePatientSelect() {
    const select = document.getElementById('patientId');

    let html = '<option value="">Sélectionnez un patient</option>';
    patients.forEach(patient => {
        html += `<option value="${patient.id}">${patient.nom} ${patient.prenom} - ${patient.email}</option>`;
    });

    select.innerHTML = html;
}

/**
 * Remplir le select des médecins
 */
function populateMedecinSelect() {
    const select = document.getElementById('medecinId');

    let html = '<option value="">Sélectionnez un médecin</option>';

    // Grouper par spécialité
    const specialites = {};
    medecins.forEach(medecin => {
        if (!specialites[medecin.specialite]) {
            specialites[medecin.specialite] = [];
        }
        specialites[medecin.specialite].push(medecin);
    });

    // Créer les optgroups
    Object.keys(specialites).sort().forEach(specialite => {
        // html += `<optgroup label="${specialite}">`;
        specialites[specialite].forEach(medecin => {
            html += `<option value="${medecin.id}">Dr. ${medecin.nom} - ${specialite} - ${medecin.prenom}</option>`;
        });
        // html += '</optgroup>';
    });

    select.innerHTML = html;
}

/**
 * Vérifier si on est en mode édition
 */
async function checkEditMode() {
    const urlParams = new URLSearchParams(window.location.search);
    rdvId = urlParams.get('id');

    if (rdvId) {
        isEditMode = true;
        document.getElementById('form-title').textContent = 'Modifier Rendez-vous';
        document.getElementById('submit-text').textContent = 'Mettre à jour';
        document.getElementById('statut-container').style.display = 'block';
        await loadRendezVousData(rdvId);
    }
}

/**
 * Charger les données du rendez-vous à modifier
 */
async function loadRendezVousData(id) {
    try {
        const rdv = await API.rendezvous.getById(id);

        document.getElementById('rdv-id').value = rdv.id;
        document.getElementById('patientId').value = rdv.patientId;
        document.getElementById('medecinId').value = rdv.medecinId;

        // Convertir les dates au format datetime-local
        const debut = new Date(rdv.dateHeureDebut);
        const fin = new Date(rdv.dateHeureFin);

        document.getElementById('dateHeureDebut').value = formatDateTimeLocal(debut);

        // Calculer la durée en minutes
        const dureeMinutes = (fin - debut) / (1000 * 60);
        document.getElementById('duree').value = dureeMinutes;

        document.getElementById('motifConsultation').value = rdv.motifConsultation || '';
        document.getElementById('statut').value = rdv.statut;

    } catch (error) {
        console.error('Erreur lors du chargement du rendez-vous:', error);
        Utils.showToast('Erreur lors du chargement des données', 'danger');
        setTimeout(() => {
            window.location.href = 'rendezvous.html';
        }, 2000);
    }
}

/**
 * Formater une date au format datetime-local
 */
function formatDateTimeLocal(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day}T${hours}:${minutes}`;
}

/**
 * Configurer la validation de la date
 */
function setupDateTimeValidation() {
    const dateInput = document.getElementById('dateHeureDebut');

    // Définir la date minimale à maintenant
    const now = new Date();
    dateInput.min = formatDateTimeLocal(now);
}

/**
 * Configurer la vérification de disponibilité
 */
function setupAvailabilityCheck() {
    const medecinSelect = document.getElementById('medecinId');
    const dateInput = document.getElementById('dateHeureDebut');
    const dureeSelect = document.getElementById('duree');

    const checkAvailability = () => {
        if (medecinSelect.value && dateInput.value && dureeSelect.value) {
            verifyAvailability();
        }
    };

    medecinSelect.addEventListener('change', checkAvailability);
    dateInput.addEventListener('change', checkAvailability);
    dureeSelect.addEventListener('change', checkAvailability);
}

/**
 * Vérifier la disponibilité du créneau
 */
async function verifyAvailability() {
    const alertDiv = document.getElementById('availability-alert');
    const medecinId = document.getElementById('medecinId').value;
    const dateHeureDebut = document.getElementById('dateHeureDebut').value;
    const duree = parseInt(document.getElementById('duree').value);

    if (!medecinId || !dateHeureDebut || !duree) {
        alertDiv.style.display = 'none';
        return;
    }

    try {
        // Calculer la date de fin
        const debut = new Date(dateHeureDebut);
        const fin = new Date(debut.getTime() + duree * 60000);

        // Récupérer les rendez-vous du médecin pour cette période
        const rdvMedecin = await API.rendezvous.getByMedecin(medecinId);

        // Vérifier les conflits
        let hasConflict = false;

        for (const rdv of rdvMedecin) {
            // Ignorer le rendez-vous actuel en mode édition
            if (isEditMode && rdv.id === parseInt(rdvId)) {
                continue;
            }

            // Ignorer les rendez-vous annulés
            if (rdv.statut === 'ANNULE') {
                continue;
            }

            const rdvDebut = new Date(rdv.dateHeureDebut);
            const rdvFin = new Date(rdv.dateHeureFin);

            // Vérifier le chevauchement
            if ((debut < rdvFin && fin > rdvDebut)) {
                hasConflict = true;
                break;
            }
        }

        if (hasConflict) {
            alertDiv.innerHTML = `
                <div class="alert alert-warning">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    <strong>Attention !</strong> Ce créneau horaire est déjà occupé pour ce médecin.
                </div>
            `;
        } else {
            alertDiv.innerHTML = `
                <div class="alert alert-success">
                    <i class="bi bi-check-circle-fill me-2"></i>
                    <strong>Disponible !</strong> Ce créneau est libre.
                </div>
            `;
        }

        alertDiv.style.display = 'block';

    } catch (error) {
        console.error('Erreur lors de la vérification:', error);
    }
}

/**
 * Configurer la soumission du formulaire
 */
function setupFormSubmit() {
    const form = document.getElementById('rdv-form');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        // Validation HTML5
        if (!form.checkValidity()) {
            e.stopPropagation();
            form.classList.add('was-validated');
            return;
        }

        // Validation personnalisée
        if (!await validateForm()) {
            return;
        }

        // Désactiver le bouton pendant l'envoi
        const submitBtn = document.getElementById('submit-btn');
        const originalText = document.getElementById('submit-text').textContent;
        submitBtn.disabled = true;
        document.getElementById('submit-text').innerHTML =
            '<span class="spinner-border spinner-border-sm me-2"></span>Enregistrement...';

        try {
            const rdvData = getFormData();

            if (isEditMode) {
                await API.rendezvous.update(rdvId, rdvData);
                Utils.showToast('Rendez-vous mis à jour avec succès', 'success');
            } else {
                await API.rendezvous.create(rdvData);
                Utils.showToast('Rendez-vous créé avec succès', 'success');
            }

            // Rediriger vers la liste après 1 seconde
            setTimeout(() => {
                window.location.href = 'rendezvous.html';
            }, 1000);

        } catch (error) {
            console.error('Erreur:', error);

            let errorMessage = 'Erreur lors de l\'enregistrement du rendez-vous';
            if (error.message.includes('créneau')) {
                errorMessage = 'Ce créneau horaire est déjà réservé pour ce médecin';
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
async function validateForm() {
    const dateHeureDebut = document.getElementById('dateHeureDebut').value;
    const duree = parseInt(document.getElementById('duree').value);

    // Vérifier que la date est dans le futur (sauf en mode édition)
    if (!isEditMode) {
        const debut = new Date(dateHeureDebut);
        const now = new Date();

        if (debut <= now) {
            Utils.showToast('La date du rendez-vous doit être dans le futur', 'warning');
            return false;
        }
    }

    // Vérifier la durée
    if (duree <= 0) {
        Utils.showToast('La durée doit être positive', 'warning');
        return false;
    }

    return true;
}

/**
 * Récupérer les données du formulaire
 */
function getFormData() {
    const dateHeureDebut = new Date(document.getElementById('dateHeureDebut').value);
    const duree = parseInt(document.getElementById('duree').value);
    const dateHeureFin = new Date(dateHeureDebut.getTime() + duree * 60000);

    const data = {
        patientId: parseInt(document.getElementById('patientId').value),
        medecinId: parseInt(document.getElementById('medecinId').value),
        dateHeureDebut: dateHeureDebut.toISOString(),
        dateHeureFin: dateHeureFin.toISOString(),
        motifConsultation: document.getElementById('motifConsultation').value.trim() || null
    };

    // Ajouter le statut si en mode édition
    if (isEditMode) {
        data.statut = document.getElementById('statut').value;
    }

    return data;
}