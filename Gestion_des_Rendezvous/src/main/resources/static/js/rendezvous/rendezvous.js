/**
 * Script pour la gestion de la liste des rendez-vous
 */

let allRendezVous = [];

document.addEventListener('DOMContentLoaded', async () => {
    await loadRendezVous();
    setupSearchListener();
    setupStatutFilter();
    setupPeriodeFilter();
});

/**
 * Charger tous les rendez-vous
 */
async function loadRendezVous() {
    const container = document.getElementById('rendezvous-container');

    try {
        allRendezVous = await API.rendezvous.getAll();

        if (allRendezVous.length === 0) {
            displayEmptyState();
            return;
        }

        displayRendezVous(allRendezVous);
        updateRdvCount(allRendezVous.length);
        updateStatistics(allRendezVous);

    } catch (error) {
        console.error('Erreur lors du chargement des rendez-vous:', error);
        container.innerHTML = `
            <div class="alert alert-danger">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                Erreur lors du chargement des rendez-vous. Veuillez réessayer.
            </div>
        `;
    }
}

/**
 * Afficher les rendez-vous dans un tableau
 */
function displayRendezVous(rendezvous) {
    const container = document.getElementById('rendezvous-container');

    // Trier par date décroissante
    rendezvous.sort((a, b) => new Date(b.dateHeureDebut) - new Date(a.dateHeureDebut));

    let html = '<div class="table-responsive"><table class="table table-hover">';
    html += `
        <thead>
            <tr>
                <th>Date & Heure</th>
                <th>Patient</th>
                <th>Médecin</th>
                <th>Spécialité</th>
                <th>Motif</th>
                <th>Statut</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
    `;

    rendezvous.forEach(rdv => {
        const isPast = new Date(rdv.dateHeureDebut) < new Date();
        const rowClass = isPast ? 'table-secondary' : '';

        html += `
            <tr class="${rowClass}">
                <td>
                    <i class="bi bi-calendar3 me-2 text-primary"></i>
                    <strong>${Utils.formatDateTime(rdv.dateHeureDebut)}</strong>
                    ${isPast ? '<br><small class="text-muted">Passé</small>' : ''}
                </td>
                <td>
                    <i class="bi bi-person-circle me-1"></i>
                    ${rdv.patientNom} ${rdv.patientPrenom}
                </td>
                <td>
                    <i class="bi bi-person-badge me-1"></i>
                    Dr. ${rdv.medecinNom} ${rdv.medecinPrenom}
                </td>
                <td>
                    <span class="badge bg-info">${rdv.medecinSpecialite}</span>
                </td>
                <td>
                    <small>${rdv.motifConsultation || 'Non précisé'}</small>
                </td>
                <td>${Utils.getStatutBadge(rdv.statut)}</td>
                <td>
                    ${getActionButtons(rdv)}
                </td>
            </tr>
        `;
    });

    html += '</tbody></table></div>';
    container.innerHTML = html;
}

/**
 * Obtenir les boutons d'action selon le statut
 */
function getActionButtons(rdv) {
    let buttons = '<div class="btn-group btn-group-sm">';

    // Bouton Confirmer (si EN_ATTENTE)
    if (rdv.statut === 'EN_ATTENTE') {
        buttons += `
            <button class="btn btn-success" 
                    onclick="confirmerRdv(${rdv.id})" 
                    title="Confirmer">
                <i class="bi bi-check-lg"></i>
            </button>
        `;
    }

    // Bouton Terminer (si CONFIRME)
    if (rdv.statut === 'CONFIRME') {
        buttons += `
            <button class="btn btn-secondary" 
                    onclick="terminerRdv(${rdv.id})" 
                    title="Terminer">
                <i class="bi bi-check-circle"></i>
            </button>
        `;
    }

    // Bouton Annuler (si EN_ATTENTE ou CONFIRME)
    if (rdv.statut === 'EN_ATTENTE' || rdv.statut === 'CONFIRME') {
        buttons += `
            <button class="btn btn-danger" 
                    onclick="annulerRdv(${rdv.id})" 
                    title="Annuler">
                <i class="bi bi-x-lg"></i>
            </button>
        `;
    }

    // Bouton Modifier
    buttons += `
        <a href="rdv-form.html?id=${rdv.id}" 
           class="btn btn-warning" 
           title="Modifier">
            <i class="bi bi-pencil"></i>
        </a>
    `;

    // Bouton Supprimer
    buttons += `
        <button class="btn btn-dark" 
                onclick="deleteRdv(${rdv.id})" 
                title="Supprimer">
            <i class="bi bi-trash"></i>
        </button>
    `;

    buttons += '</div>';
    return buttons;
}

/**
 * Afficher l'état vide
 */
function displayEmptyState() {
    const container = document.getElementById('rendezvous-container');
    container.innerHTML = `
        <div class="empty-state">
            <i class="bi bi-calendar-x"></i>
            <p>Aucun rendez-vous enregistré</p>
            <a href="rdv-form.html" class="btn btn-primary">
                <i class="bi bi-plus-circle me-2"></i>
                Créer le premier rendez-vous
            </a>
        </div>
    `;
    updateRdvCount(0);
    updateStatistics([]);
}

/**
 * Mettre à jour les statistiques
 */
function updateStatistics(rendezvous) {
    const stats = {
        EN_ATTENTE: 0,
        CONFIRME: 0,
        TERMINE: 0,
        ANNULE: 0
    };

    rendezvous.forEach(rdv => {
        stats[rdv.statut]++;
    });

    document.getElementById('stat-en-attente').textContent = stats.EN_ATTENTE;
    document.getElementById('stat-confirme').textContent = stats.CONFIRME;
    document.getElementById('stat-termine').textContent = stats.TERMINE;
    document.getElementById('stat-annule').textContent = stats.ANNULE;
}

/**
 * Mettre à jour le compteur de rendez-vous
 */
function updateRdvCount(count) {
    document.getElementById('rdv-count').textContent = count;
}

/**
 * Configurer l'écouteur de recherche
 */
function setupSearchListener() {
    const searchInput = document.getElementById('search-input');
    searchInput.addEventListener('input', applyFilters);
}

/**
 * Configurer le filtre de statut
 */
function setupStatutFilter() {
    const statutFilter = document.getElementById('statut-filter');
    statutFilter.addEventListener('change', applyFilters);
}

/**
 * Configurer le filtre de période
 */
function setupPeriodeFilter() {
    const periodeFilter = document.getElementById('periode-filter');
    periodeFilter.addEventListener('change', applyFilters);
}

/**
 * Appliquer tous les filtres
 */
function applyFilters() {
    const searchTerm = document.getElementById('search-input').value.toLowerCase().trim();
    const selectedStatut = document.getElementById('statut-filter').value;
    const selectedPeriode = document.getElementById('periode-filter').value;

    let filtered = allRendezVous;

    // Filtre par recherche
    if (searchTerm !== '') {
        filtered = filtered.filter(rdv => {
            return rdv.patientNom.toLowerCase().includes(searchTerm) ||
                rdv.patientPrenom.toLowerCase().includes(searchTerm) ||
                rdv.medecinNom.toLowerCase().includes(searchTerm) ||
                rdv.medecinPrenom.toLowerCase().includes(searchTerm) ||
                rdv.medecinSpecialite.toLowerCase().includes(searchTerm);
        });
    }

    // Filtre par statut
    if (selectedStatut !== '') {
        filtered = filtered.filter(rdv => rdv.statut === selectedStatut);
    }

    // Filtre par période
    if (selectedPeriode !== '') {
        const now = new Date();

        filtered = filtered.filter(rdv => {
            const rdvDate = new Date(rdv.dateHeureDebut);

            switch (selectedPeriode) {
                case 'today':
                    return rdvDate.toDateString() === now.toDateString();

                case 'week':
                    const weekStart = new Date(now);
                    weekStart.setDate(now.getDate() - now.getDay());
                    const weekEnd = new Date(weekStart);
                    weekEnd.setDate(weekStart.getDate() + 7);
                    return rdvDate >= weekStart && rdvDate < weekEnd;

                case 'month':
                    return rdvDate.getMonth() === now.getMonth() &&
                        rdvDate.getFullYear() === now.getFullYear();

                case 'upcoming':
                    return rdvDate > now;

                case 'past':
                    return rdvDate < now;

                default:
                    return true;
            }
        });
    }

    displayRendezVous(filtered);
    updateRdvCount(filtered.length);
    updateStatistics(filtered);

    if (filtered.length === 0) {
        document.getElementById('rendezvous-container').innerHTML = `
            <div class="empty-state">
                <i class="bi bi-search"></i>
                <p>Aucun rendez-vous trouvé avec ces critères</p>
            </div>
        `;
    }
}

/**
 * Confirmer un rendez-vous
 */
async function confirmerRdv(id) {
    if (!await Utils.showConfirmDialog('Voulez-vous confirmer ce rendez-vous ?')) {
        return;
    }

    try {
        await API.rendezvous.confirm(id);
        Utils.showToast('Rendez-vous confirmé avec succès', 'success');
        await loadRendezVous();
    } catch (error) {
        console.error('Erreur:', error);
        Utils.showToast('Erreur lors de la confirmation', 'danger');
    }
}

/**
 * Terminer un rendez-vous
 */
async function terminerRdv(id) {
    if (!await Utils.showConfirmDialog('Marquer ce rendez-vous comme terminé ?')) {
        return;
    }

    try {
        await API.rendezvous.complete(id);
        Utils.showToast('Rendez-vous marqué comme terminé', 'success');
        await loadRendezVous();
    } catch (error) {
        console.error('Erreur:', error);
        Utils.showToast('Erreur lors de la mise à jour', 'danger');
    }
}

/**
 * Annuler un rendez-vous
 */
async function annulerRdv(id) {
    if (!await Utils.showConfirmDialog('Voulez-vous vraiment annuler ce rendez-vous ?')) {
        return;
    }

    try {
        await API.rendezvous.cancel(id);
        Utils.showToast('Rendez-vous annulé avec succès', 'warning');
        await loadRendezVous();
    } catch (error) {
        console.error('Erreur:', error);
        Utils.showToast('Erreur lors de l\'annulation', 'danger');
    }
}

/**
 * Supprimer un rendez-vous
 */
async function deleteRdv(id) {
    if (!await Utils.showConfirmDialog('Voulez-vous vraiment supprimer ce rendez-vous ? Cette action est irréversible.')) {
        return;
    }

    try {
        await API.rendezvous.delete(id);
        Utils.showToast('Rendez-vous supprimé avec succès', 'success');
        await loadRendezVous();
    } catch (error) {
        console.error('Erreur:', error);
        Utils.showToast('Erreur lors de la suppression', 'danger');
    }
}

// Rendre les fonctions accessibles globalement
window.confirmerRdv = confirmerRdv;
window.terminerRdv = terminerRdv;
window.annulerRdv = annulerRdv;
window.deleteRdv = deleteRdv;