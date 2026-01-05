package com.thiordev.Gestion_des_Rendezvous.service;

import com.thiordev.Gestion_des_Rendezvous.dto.request.MedecinRequestDto;
import com.thiordev.Gestion_des_Rendezvous.dto.response.MedecinResponseDto;
import com.thiordev.Gestion_des_Rendezvous.exception.ConflictException;
import com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException;
import com.thiordev.Gestion_des_Rendezvous.mapper.MedecinMapper;
import com.thiordev.Gestion_des_Rendezvous.models.Medecin;
import com.thiordev.Gestion_des_Rendezvous.models.Specialite;
import com.thiordev.Gestion_des_Rendezvous.repositories.MedecinRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MedecinServiceImpl implements MedecinService {

    private final MedecinMapper medecinMapper;

    private final MedecinRepository medecinRepository;

    @Override
    public MedecinResponseDto createMedecin(MedecinRequestDto requestDto) {
        log.info("Creating medecin ");
        if(medecinRepository.existsByEmail(requestDto.getEmail())) {
            log.error("Un Medecin avec cet email existe : {}", requestDto.getEmail());
            throw new ConflictException("Un medecin avec cet email existe dèja");
        }
        Medecin medecin = medecinMapper.toEntity(requestDto);
        medecinRepository.save(medecin);
        log.info("Medecin created : {}", medecin.getEmail());

        return medecinMapper.toResponseDto(medecin);
    }

    @Override
    @Transactional(readOnly = true)
    public MedecinResponseDto getMedecinById(Long id) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(()-> new RessourceNotFoundException("Medecin", "id", id));
        return medecinMapper.toResponseDto(medecin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedecinResponseDto> getAllMedecins() {
        List<Medecin> medecins = medecinRepository.findAll();
        return medecinMapper.toResponseDtoList(medecins);
    }

    @Override
    public MedecinResponseDto updateMedecin(Long id, MedecinRequestDto requestDto) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(()-> new RessourceNotFoundException("Medecin", "id", id));

        if(medecinRepository.existsByEmail(requestDto.getEmail()) && requestDto.getEmail().equals(medecin.getEmail())) {
            log.error("L'email est dèja utilisé par un autre medecin");
            throw new ConflictException("Cet email est dèja utilisé par un autre medecin !");
        }

        medecinMapper.updateEntityFromDto(requestDto, medecin);
        log.info("Le Medecin est mis à jour : {}", medecin.getEmail());
        medecinRepository.save(medecin);
        return medecinMapper.toResponseDto(medecin);
    }

    @Override
    public void deleteMedecin(Long id) {
        log.info("Suppresion du medecin avec l'Id {}", id);
        if(!medecinRepository.existsById(id)) {
            log.error("Medecin introuvable");
            throw new RessourceNotFoundException("Medecin", "id", id);
        }
        medecinRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedecinResponseDto> getMedecinsBySpecialite(Specialite specialite) {
        log.info("Recherche de medecins par la specialité : {}", specialite);
        List<Medecin> medecins = medecinRepository.findBySpecialite(specialite);
        return medecinMapper.toResponseDtoList(medecins);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedecinResponseDto> searchMedecins(String searchTerm) {
        log.info("Recherche des medecins selon les termes : {}", searchTerm);
        List<Medecin> medecins = medecinRepository.searchByNomOrPrenom(searchTerm);
        return medecinMapper.toResponseDtoList(medecins);
    }

    @Override
    public MedecinResponseDto getMedecinByEmail(String email) {
        log.info("Recherche de medecin par email : {}", email);
        Medecin medecin = medecinRepository.findByEmail(email)
                .orElseThrow(()-> new RessourceNotFoundException("Medecin", "email", email));
        return medecinMapper.toResponseDto(medecin);
    }

    @Override
    @Transactional(readOnly = true)
    public long countMedecinsBySpecialite(Specialite specialite) {
        log.info("COmptage de medecins par specialité : {}", specialite);

        return medecinRepository.countBySpecialite(specialite);
    }
}
