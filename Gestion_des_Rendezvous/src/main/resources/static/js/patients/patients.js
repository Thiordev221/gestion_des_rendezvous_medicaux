/**
 * Script pour la gestion de la liste des patients
 */

let allPatients = [];

document.addEventListener('DOMContentLoaded', async () => {
    await loadPatients();
    setupSearchListener();
});

/**
 * Charger tous les patients
 */
async function loadPatients() {
    const container = document.getElementById('patients-container');

    try {
        allPatients = await API.patients.getAll();

        if (allPatients.length === 0) {
            displayEmptyState();
            return;
        }

        displayPatients(allPatients);
        updatePatientCount(allPatients.length);

    } catch (error) {
        console.error('Erreur lors du chargement des patients:', error);
        container.innerHTML = `
            <div class="alert alert-danger">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                Erreur lors du chargement des patients. Veuillez réessayer.
            </div>
        `;
    }
}

/**
 * Afficher les patients dans un tableau
 */
function displayPatients(patients) {
    const container = document.getElementById('patients-container');

    let html = '<div class="table-responsive"><table class="table table-hover">';
    html += `
        <thead>
            <tr>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Date de naissance</th>
                <th>Email</th>
                <th>Téléphone</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
    `;

    patients.forEach(patient => {
        html += `
            <tr>
                <td>
                    <i class="bi bi-person-circle me-2 text-primary"></i>
                    <strong>${patient.nom}</strong>
                </td>
                <td>${patient.prenom}</td>
                <td>
                    ${Utils.formatDate(patient.dateNaissance)}
                    <br>
                    <small class="text-muted">${Utils.calculateAge(patient.dateNaissance)}</small>
                </td>
                <td>
                    <i class="bi bi-envelope me-1"></i>
                    ${patient.email}
                </td>
                <td>
                    <i class="bi bi-telephone me-1"></i>
                    ${Utils.formatPhone(patient.telephone)}
                </td>
                <td>
                    <div class="btn-group btn-group-sm">
                        <a href="patient-form.html?id=${patient.id}" 
                           class="btn btn-warning" 
                           title="Modifier">
                            <i class="bi bi-pencil"></i>
                        </a>
                        <button class="btn btn-danger" 
                                onclick="deletePatient(${patient.id}, '${patient.nom} ${patient.prenom}')" 
                                title="Supprimer">
                            <i class="bi bi-trash"></i>
                        </button>
                        <button class="btn btn-info" 
                                onclick="viewPatientDetails(${patient.id})" 
                                title="Voir détails">
                            <i class="bi bi-eye"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `;
    });

    html += '</tbody></table></div>';
    container.innerHTML = html;
}

/**
 * Afficher l'état vide
 */
function displayEmptyState() {
    const container = document.getElementById('patients-container');
    container.innerHTML = `
        <div class="empty-state">
            <i class="bi bi-person-x"></i>
            <p>Aucun patient enregistré</p>
            <a href="patient-form.html" class="btn btn-primary">
                <i class="bi bi-plus-circle me-2"></i>
                Ajouter le premier patient
            </a>
        </div>
    `;
    updatePatientCount(0);
}

/**
 * Mettre à jour le compteur de patients
 */
function updatePatientCount(count) {
    document.getElementById('patient-count').textContent = count;
}

/**
 * Configurer l'écouteur de recherche
 */
function setupSearchListener() {
    const searchInput = document.getElementById('search-input');

    searchInput.addEventListener('input', (e) => {
        const searchTerm = e.target.value.toLowerCase().trim();

        if (searchTerm === '') {
            displayPatients(allPatients);
            updatePatientCount(allPatients.length);
            return;
        }

        const filteredPatients = allPatients.filter(patient => {
            return patient.nom.toLowerCase().includes(searchTerm) ||
                patient.prenom.toLowerCase().includes(searchTerm) ||
                patient.email.toLowerCase().includes(searchTerm) ||
                patient.telephone.includes(searchTerm);
        });

        displayPatients(filteredPatients);
        updatePatientCount(filteredPatients.length);

        if (filteredPatients.length === 0) {
            document.getElementById('patients-container').innerHTML = `
                <div class="empty-state">
                    <i class="bi bi-search"></i>
                    <p>Aucun patient trouvé pour "${searchTerm}"</p>
                </div>
            `;
        }
    });
}

/**
 * Supprimer un patient
 */
async function deletePatient(id, name) {
    if (!await Utils.showConfirmDialog(`Voulez-vous vraiment supprimer le patient ${name} ?`)) {
        return;
    }

    try {
        await API.patients.delete(id);
        Utils.showToast(`Patient ${name} supprimé avec succès`, 'success');
        await loadPatients();
    } catch (error) {
        console.error('Erreur lors de la suppression:', error);
        Utils.showToast('Erreur lors de la suppression du patient. Attention: un patient qui un rendez-vous ne peut être supprimé !', 'danger');
    }
}

/**
 * Voir les détails d'un patient
 */
function viewPatientDetails(id) {
    // Pour l'instant, on affiche une alerte
    // Plus tard, on créera une page patient-detail.html
    alert(`Détails du patient #${id} - Fonctionnalité à venir`);
}

// Rendre les fonctions accessibles globalement
window.deletePatient = deletePatient;
window.viewPatientDetails = viewPatientDetails;