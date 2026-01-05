/**
 * Script pour la gestion de la liste des médecins
 */

let allMedecins = [];

document.addEventListener('DOMContentLoaded', async () => {
    await loadMedecins();
    setupSearchListener();
    setupSpecialiteFilter();
});

/**
 * Charger tous les médecins
 */
async function loadMedecins() {
    const container = document.getElementById('medecins-container');

    try {
        allMedecins = await API.medecins.getAll();

        if (allMedecins.length === 0) {
            displayEmptyState();
            return;
        }

        displayMedecins(allMedecins);
        updateMedecinCount(allMedecins.length);

    } catch (error) {
        console.error('Erreur lors du chargement des médecins:', error);
        container.innerHTML = `
            <div class="alert alert-danger">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                Erreur lors du chargement des médecins. Veuillez réessayer.
            </div>
        `;
    }
}

/**
 * Afficher les médecins dans un tableau
 */
function displayMedecins(medecins) {
    const container = document.getElementById('medecins-container');

    let html = '<div class="table-responsive"><table class="table table-hover">';
    html += `
        <thead>
            <tr>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Spécialité</th>
                <th>Email</th>
                <th>Téléphone</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
    `;

    medecins.forEach(medecin => {
        const specialiteBadgeColor = getSpecialiteBadgeColor(medecin.specialite);

        html += `
            <tr>
                <td>
                    <i class="bi bi-person-badge-fill me-2 text-primary"></i>
                    <strong>Dr. ${medecin.nom}</strong>
                </td>
                <td>${medecin.prenom}</td>
                <td>
                    <span class="badge ${specialiteBadgeColor}">${medecin.specialite}</span>
                </td>
                <td>
                    <i class="bi bi-envelope me-1"></i>
                    ${medecin.email}
                </td>
                <td>
                    <i class="bi bi-telephone me-1"></i>
                    ${Utils.formatPhone(medecin.telephone)}
                </td>
                <td>
                    <div class="btn-group btn-group-sm">
                        <a href="medecin-form.html?id=${medecin.id}" 
                           class="btn btn-warning" 
                           title="Modifier">
                            <i class="bi bi-pencil"></i>
                        </a>
                        <button class="btn btn-danger" 
                                onclick="deleteMedecin(${medecin.id}, 'Dr. ${medecin.nom} ${medecin.prenom}')" 
                                title="Supprimer">
                            <i class="bi bi-trash"></i>
                        </button>
                        <button class="btn btn-info" 
                                onclick="viewMedecinDetails(${medecin.id})" 
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
 * Obtenir la couleur du badge selon la spécialité
 */
function getSpecialiteBadgeColor(specialite) {
    const colors = {
        'CARDIOLOGIE': 'bg-danger',
        'DERMATOLOGIE': 'bg-warning',
        'PEDIATRIE': 'bg-info',
        'NEUROLOGIE': 'bg-primary',
        'OPHTALMOLOGIE': 'bg-success',
        'MEDECINE_GENERALE': 'bg-secondary',
        'GYNECOLOGIE': 'bg-pink text-dark²',
        'PSYCHIATRIE': 'bg-purple',
        'ORTHOPEDIE': 'bg-dark',
        'ORL': 'bg-teal'
    };
    return colors[specialite] || 'bg-secondary';
}

/**
 * Afficher l'état vide
 */
function displayEmptyState() {
    const container = document.getElementById('medecins-container');
    container.innerHTML = `
        <div class="empty-state">
            <i class="bi bi-person-badge"></i>
            <p>Aucun médecin enregistré</p>
            <a href="medecin-form.html" class="btn btn-primary">
                <i class="bi bi-plus-circle me-2"></i>
                Ajouter le premier médecin
            </a>
        </div>
    `;
    updateMedecinCount(0);
}

/**
 * Mettre à jour le compteur de médecins
 */
function updateMedecinCount(count) {
    document.getElementById('medecin-count').textContent = count;
}

/**
 * Configurer l'écouteur de recherche
 */
function setupSearchListener() {
    const searchInput = document.getElementById('search-input');

    searchInput.addEventListener('input', () => {
        applyFilters();
    });
}

/**
 * Configurer le filtre de spécialité
 */
function setupSpecialiteFilter() {
    const specialiteFilter = document.getElementById('specialite-filter');

    specialiteFilter.addEventListener('change', () => {
        applyFilters();
    });
}

/**
 * Appliquer les filtres (recherche + spécialité)
 */
function applyFilters() {
    const searchTerm = document.getElementById('search-input').value.toLowerCase().trim();
    const selectedSpecialite = document.getElementById('specialite-filter').value;

    let filteredMedecins = allMedecins;

    // Filtre par recherche
    if (searchTerm !== '') {
        filteredMedecins = filteredMedecins.filter(medecin => {
            return medecin.nom.toLowerCase().includes(searchTerm) ||
                medecin.prenom.toLowerCase().includes(searchTerm) ||
                medecin.email.toLowerCase().includes(searchTerm) ||
                medecin.telephone.includes(searchTerm) ||
                medecin.specialite.toLowerCase().includes(searchTerm);
        });
    }

    // Filtre par spécialité
    if (selectedSpecialite !== '') {
        filteredMedecins = filteredMedecins.filter(medecin => {
            return medecin.specialite === selectedSpecialite;
        });
    }

    displayMedecins(filteredMedecins);
    updateMedecinCount(filteredMedecins.length);

    if (filteredMedecins.length === 0) {
        document.getElementById('medecins-container').innerHTML = `
            <div class="empty-state">
                <i class="bi bi-search"></i>
                <p>Aucun médecin trouvé avec ces critères</p>
            </div>
        `;
    }
}

/**
 * Supprimer un médecin
 */
async function deleteMedecin(id, name) {
    if (!await Utils.showConfirmDialog(`Voulez-vous vraiment supprimer ${name} ?`)) {
        return;
    }

    try {
        await API.medecins.delete(id);
        Utils.showToast(`${name} supprimé avec succès`, 'success');
        await loadMedecins();
    } catch (error) {
        console.error('Erreur lors de la suppression:', error);
        Utils.showToast('Erreur lors de la suppression du médecin. Attention: un medecin qui un rendez-vous ne peut être supprimé !', 'danger');
    }
}

/**
 * Voir les détails d'un médecin
 */
function viewMedecinDetails(id) {
    alert(`Détails du médecin #${id} - Fonctionnalité à venir`);
}

// Rendre les fonctions accessibles globalement
window.deleteMedecin = deleteMedecin;
window.viewMedecinDetails = viewMedecinDetails;