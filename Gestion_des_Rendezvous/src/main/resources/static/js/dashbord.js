/**
 * Script pour le Dashboard
 */

document.addEventListener('DOMContentLoaded', async () => {
    await loadDashboardStats();
    await loadUpcomingRendezVous();
});

/**
 * Charger les statistiques du dashboard
 */
async function loadDashboardStats() {
    try {
        // Charger le nombre de patients
        document.getElementById('total-patients').textContent = await API.patients.count();

        // Charger le nombre de médecins (compter manuellement car pas d'endpoint count)
        const medecins = await API.medecins.getAll();
        document.getElementById('total-medecins').textContent = medecins.length;

        // Charger les RDV en attente
        document.getElementById('rdv-en-attente').textContent = await API.rendezvous.countByStatut('EN_ATTENTE');

        // Charger les RDV confirmés
        document.getElementById('rdv-confirmes').textContent = await API.rendezvous.countByStatut('CONFIRME');

    } catch (error) {
        console.error('Erreur lors du chargement des statistiques:', error);
        Utils.showToast('Erreur lors du chargement des statistiques', 'danger');

        // Afficher 0 en cas d'erreur
        document.getElementById('total-patients').textContent = '0';
        document.getElementById('total-medecins').textContent = '0';
        document.getElementById('rdv-en-attente').textContent = '0';
        document.getElementById('rdv-confirmes').textContent = '0';
    }
}

/**
 * Charger les prochains rendez-vous
 */
async function loadUpcomingRendezVous() {
    const container = document.getElementById('upcoming-rendezvous');

    try {
        // Récupérer tous les RDV confirmés et en attente
        const [rdvEnAttente, rdvConfirmes] = await Promise.all([
            API.rendezvous.getByStatut('EN_ATTENTE'),
            API.rendezvous.getByStatut('CONFIRME')
        ]);

        // Combiner et filtrer les RDV futurs
        const allRdv = [...rdvEnAttente, ...rdvConfirmes];
        const now = new Date();
        const upcomingRdv = allRdv
            .filter(rdv => new Date(rdv.dateHeureDebut) > now)
            .sort((a, b) => new Date(a.dateHeureDebut) - new Date(b.dateHeureDebut))
            .slice(0, 5); // Prendre les 5 prochains

        if (upcomingRdv.length === 0) {
            container.innerHTML = `
                <div class="empty-state">
                    <i class="bi bi-calendar-x"></i>
                    <p>Aucun rendez-vous à venir</p>
                </div>
            `;
            return;
        }

        // Afficher les RDV
        let html = '<div class="table-responsive"><table class="table table-hover">';
        html += `
            <thead>
                <tr>
                    <th>Date & Heure</th>
                    <th>Patient</th>
                    <th>Médecin</th>
                    <th>Spécialité</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
        `;

        upcomingRdv.forEach(rdv => {
            html += `
                <tr>
                    <td>
                        <i class="bi bi-calendar3 me-2 text-primary"></i>
                        ${Utils.formatDateTime(rdv.dateHeureDebut)}
                    </td>
                    <td>
                        <i class="bi bi-person me-1"></i>
                        ${rdv.patientNom} ${rdv.patientPrenom}
                    </td>
                    <td>
                        <i class="bi bi-person-badge me-1"></i>
                        Dr. ${rdv.medecinNom} ${rdv.medecinPrenom}
                    </td>
                    <td>
                        <span class="badge bg-info">${rdv.medecinSpecialite}</span>
                    </td>
                    <td>${Utils.getStatutBadge(rdv.statut)}</td>
                    <td>
                        <div class="btn-group btn-group-sm">
                            ${rdv.statut === 'EN_ATTENTE' ? `
                                <button class="btn btn-success" onclick="confirmerRdv(${rdv.id})" 
                                        title="Confirmer">
                                    <i class="bi bi-check-lg"></i>
                                </button>
                            ` : ''}
                            <button class="btn btn-danger" onclick="annulerRdv(${rdv.id})" 
                                    title="Annuler">
                                <i class="bi bi-x-lg"></i>
                            </button>
                            <button onclick="viewRDVDetails(${rdv.id})" 
                               class="btn btn-primary" title="Voir détails">
                                <i class="bi bi-eye"></i>
                            </button>
                        </div>
                    </td>
                </tr>
            `;
        });

        html += '</tbody></table></div>';
        container.innerHTML = html;

    } catch (error) {
        console.error('Erreur lors du chargement des rendez-vous:', error);
        container.innerHTML = `
            <div class="alert alert-danger">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                Erreur lors du chargement des rendez-vous à venir
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
        await loadUpcomingRendezVous();
        await loadDashboardStats();
    } catch (error) {
        console.error('Erreur:', error);
        Utils.showToast('Erreur lors de la confirmation du rendez-vous', 'danger');
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
        await loadUpcomingRendezVous();
        await loadDashboardStats();
    } catch (error) {
        console.error('Erreur:', error);
        Utils.showToast('Erreur lors de l\'annulation du rendez-vous', 'danger');
    }
}

function viewRDVDetails(id){
    alert(`Détails du rendez-vous #${id} - Fontionnalité à venir !`)
}

// Rendre les fonctions accessibles globalement
window.confirmerRdv = confirmerRdv;
window.annulerRdv = annulerRdv;