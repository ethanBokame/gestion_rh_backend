package com.gestionrh

import com.gestionrh.dto.AbsenceRequestRequestDTO
import com.gestionrh.dto.AbsenceRequestResponseDTO
import com.gestionrh.dto.AbsenceRequestResponseRhDTO
import grails.gorm.transactions.Transactional

@Transactional
class AbsenceRequestService {

    Integer count(){
        return AbsenceRequest.count()
    }

    List<AbsenceRequestResponseRhDTO> getAll(Map params) {
        return AbsenceRequest
                .list(params)
                .collect(new AbsenceRequestResponseRhDTO(it))
    }

    AbsenceRequest getById(Long id) {
        return AbsenceRequest.get(id)
    }

    List<AbsenceRequest> getAllByEmployee(User employee, Map params = [:]) {
        return AbsenceRequest.findAllByEmployee(employee, params)
    }

    AbsenceRequestResponseDTO create(User employee, AbsenceRequestRequestDTO dto) {
        // User entry
        Map userEntry = dto.properties

        // Create absence request object
        AbsenceRequest absenceRequest = new AbsenceRequest(userEntry)

        // Add employee
        absenceRequest.employee = employee

        // Save in db
        absenceRequest.save(failOnError: true)

        return new AbsenceRequestResponseDTO(absenceRequest)
    }

    void update(User employee, Long id, AbsenceRequestRequestDTO dto) {
        // Get absence request
        AbsenceRequest absenceRequest = AbsenceRequest.get(id)

        // Exception
        if(!absenceRequest) throw new RuntimeException("Absence request not found")

        // Unauthorized
        if(absenceRequest.employee.id != employee.id) throw new RuntimeException("Absence request not found")

        // Already controlled
        if(absenceRequest.reviewer != null) throw new RuntimeException("Update no longer available")

        // User entry without null elements
        Map userEntry = dto.properties.findAll { it.value != null }

        // Update absence request
        absenceRequest.properties = userEntry

        // Save in db
        absenceRequest.save(failOnError: true)
    }

    void delete(User employee, Long id) {
        // Get absence request
        AbsenceRequest absenceRequest = AbsenceRequest.get(id)

        // Not found
        if(!absenceRequest) throw new RuntimeException("Absence request not found")

        // Unauthorized
        if(absenceRequest.employee.id != employee.id) throw new RuntimeException("Absence request not found")

        // Already controlled
        if(absenceRequest.reviewer != null) throw new RuntimeException("Deletion no longer available")

        // Delete in db
        absenceRequest.delete(failOnError: true)
    }

    void approved(User reviewer, long id) {
        // Get absence request
        AbsenceRequest absenceRequest = AbsenceRequest.get(id)

        // Exception
        if(!absenceRequest) throw new RuntimeException("Absence request not found")

        // Add reviewer
        absenceRequest.reviewer = reviewer

        // Approve absence request
        absenceRequest.status = "APPROVED"

        absenceRequest.save(failOnError: true)
    }

    void rejected(User reviewer, long id) {
        // Get absence request
        AbsenceRequest absenceRequest = AbsenceRequest.get(id)

        // Exception
        if(!absenceRequest) throw new RuntimeException("Absence request not found")

        // Add reviewer
        absenceRequest.reviewer = reviewer

        // Approve absence request
        absenceRequest.status = "REJECTED"

        absenceRequest.save(failOnError: true)
    }
}
