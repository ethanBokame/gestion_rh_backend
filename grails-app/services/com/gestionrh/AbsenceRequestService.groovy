package com.gestionrh

import com.gestionrh.dto.AbsenceRequestRequestDTO
import com.gestionrh.dto.AbsenceRequestResponseDTO
import com.gestionrh.dto.AbsenceRequestResponseRhDTO
import com.gestionrh.validation.AbsenceRequestValidator
import grails.gorm.transactions.Transactional

@Transactional
class AbsenceRequestService {
    // Utilise java doc pour les comments
    // Return number of rows in absence_request table
    Integer count(){
        // Ajouter du logging(check slf4j)
        return AbsenceRequest.count()
    }
    
    // Utilise de la java doc pour les comments
    // Return absence requests list
    List<AbsenceRequestResponseRhDTO> getAll(Map params) {
        // Ajouter du logging(check slf4j)
        return AbsenceRequest
                .list(params)
                .collect{ new AbsenceRequestResponseRhDTO(it) }
    }
    
    // Utilise java doc pour les comments
    // Return one absence request with his id
    AbsenceRequestResponseRhDTO getById(Long id) {
        // Get absence request with his id
        AbsenceRequest absenceRequest = AbsenceRequest.get(id)

        // Validation
        AbsenceRequestValidator.checkCanTreatAndShow(absenceRequest)

        return new AbsenceRequestResponseRhDTO(AbsenceRequest.get(id))
    }

    // Utilise java doc pour les comments
    // Return absence requests list of one employee
    // Définis les paramètres dans le service
    List<AbsenceRequestResponseDTO> getAllByEmployee(User authenticatedUser, Map params = [:]) {
        // Ajouter du logging(check slf4j)
        return AbsenceRequest
                .findAllByEmployee(authenticatedUser, params)
                .collect{ new AbsenceRequestResponseDTO(it) }
    }

    // Utilise java doc pour les comments
    // Create absence request
    AbsenceRequestResponseDTO create(User authenticatedUser, AbsenceRequestRequestDTO userInput) {
        // Ajouter du logging(check slf4j)
        // Instantiate absence request object
        AbsenceRequest absenceRequest = new AbsenceRequest(userInput.properties)

        // Add employee
        absenceRequest.employee = authenticatedUser

        // Save in db
        absenceRequest.save(failOnError: true)

        return new AbsenceRequestResponseDTO(absenceRequest)
    }

    // Update absence request
    void update(User authenticatedUser, Long id, AbsenceRequestRequestDTO userInput) {
        // Ajouter du logging(check slf4j)
        // Crée une methode get id qui contiendra aussi la validation
        // Get absence request with his id
        AbsenceRequest absenceRequest = AbsenceRequest.get(id)

        // Validation
        AbsenceRequestValidator.checkCanUpdateAndDelete(absenceRequest, authenticatedUser)

        // Update absence request properties
        absenceRequest.properties = userInput.properties.findAll { it.value != null }

        // Save in db
        absenceRequest.save(failOnError: true)
    }

    // Utilise java doc pour les comments
    // Delete absence request
    void deleteById(User authenticatedUser, Long id) {
        // Ajouter du logging(check slf4j)
         // Crée une methode get id qui contiendra aussi la validation
        // Get absence request with his id
        AbsenceRequest absenceRequest = AbsenceRequest.get(id)

        // Validation
        AbsenceRequestValidator.checkCanUpdateAndDelete(absenceRequest, authenticatedUser)

        // Delete in db
        absenceRequest.delete(failOnError: true)
    }

    // Utilise java doc pour les comments
    // Approve absence request
    void approved(User authenticatedUser, long id) {
        // Ajouter du logging(check slf4j)
         // Crée une methode get id qui contiendra aussi la validation
        // Get absence request with his id
        AbsenceRequest absenceRequest = AbsenceRequest.get(id)

        // Validation
        AbsenceRequestValidator.checkCanTreatAndShow(absenceRequest)

        // Add reviewer
        absenceRequest.reviewer = authenticatedUser

        // Approve absence request
        absenceRequest.status = "APPROVED"

        absenceRequest.save(failOnError: true)
    }

    // Utilise java doc pour les comments
    // Reject absence request
    void rejected(User authenticatedUser, long id) {
        // Ajouter du logging(check slf4j)
         // Crée une methode get id qui contiendra aussi la validation
        // Get absence request with his id
        AbsenceRequest absenceRequest = AbsenceRequest.get(id)

        // Validation
        AbsenceRequestValidator.checkCanTreatAndShow(absenceRequest)

        // Add reviewer
        absenceRequest.reviewer = authenticatedUser

        // Approve absence request
        absenceRequest.status = "REJECTED"

        absenceRequest.save(failOnError: true)
    }
}
