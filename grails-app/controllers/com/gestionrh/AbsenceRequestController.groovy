package com.gestionrh

import com.gestionrh.dto.AbsenceRequestRequestDTO
import grails.plugin.springsecurity.annotation.Secured
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.BAD_REQUEST

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional

@ReadOnly
class AbsenceRequestController {

    AbsenceRequestService absenceRequestService

    def springSecurityService

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured('ROLE_RH')
    def index(Integer max, Integer page) {
        // Pagination parameters
        max = Math.min(max ?: 10, 100)
        page = page ?: 1

        params.max = max
        params.offset = (page - 1) * max

        // Totals
        Integer totalItems = absenceRequestService.count()
        Integer totalPages = (int) Math.ceil(totalItems / max)

        respond (
                data: absenceRequestService.getAll(params),
                total: totalItems,
                pagination: [
                    currentPage: page,
                    prevPage: page > 1 ? page - 1 : null,
                    nextPage: page < totalPages ? page + 1 : null,
                    totalPages: totalPages
                ]
        )
    }

    @Secured(['ROLE_USER', 'ROLE_RH'])
    def show(Long id) {
        respond absenceRequestService.getById(id)
    }

    @Secured('ROLE_USER')
    def save(AbsenceRequestRequestDTO dto) {
        try {
            // Get employee
            User employee = springSecurityService.getCurrentUser()

            respond absenceRequestService.create(employee, dto), status: CREATED
        }
        catch (Exception e) {
            respond([error: e.message], status: BAD_REQUEST)
        }
    }

    @Secured(['ROLE_USER'])
    @Transactional
    def update(Long id, AbsenceRequestRequestDTO dto) {
        try {
            // Get employee
            User employee = springSecurityService.getCurrentUser()

            absenceRequestService.update(employee, id, dto)

            render status: NO_CONTENT
        }
        catch (Exception e) {
            respond ([error: e.message], status: BAD_REQUEST)
        }
    }

    @Secured('ROLE_USER')
    @Transactional
    def delete(Long id) {
        try {
            // Get employee
            User employee = springSecurityService.getCurrentUser()

            // Delete absence service
            absenceRequestService.delete(employee, id)

            render status: NO_CONTENT
        }
        catch (Exception e) {
            respond ([error: e.message], status: NOT_FOUND)
        }

    }

    @Secured('ROLE_RH')
    @Transactional
    def approved(Long id) {
        try {
            // Get the authentify user
            User reviewer = springSecurityService.getCurrentUser()

            // Approved absence request
            absenceRequestService.approved(reviewer, id)
            render status: NO_CONTENT
        } catch (Exception e) {
            respond ([error: e.message], status: NOT_FOUND)
        }
    }

    @Secured('ROLE_RH')
    @Transactional
    def rejected(Long id) {
        try {
            // Get the authentify user
            User reviewer = springSecurityService.getCurrentUser()

            // Approved absence request
            absenceRequestService.rejected(reviewer, id)
            render status: NO_CONTENT
        } catch (Exception e) {
            respond ([error: e.message], status: NOT_FOUND)
        }
    }

    @Secured(['ROLE_USER'])
    @Transactional
    def myRequests(Integer max, Integer page) {
        // Pagination parameters
        max = Math.min(max ?: 10, 100)
        page = page ?: 1

        params.max = max
        params.offset = (page - 1) * max

        // Get the authentify user
        User employee = springSecurityService.getCurrentUser()

        // Totals
        Integer totalItems = absenceRequestService.getAllByEmployee(employee).size()
        Integer totalPages = (int) Math.ceil(totalItems / max)

        respond (
                data: absenceRequestService.getAllByEmployee(employee, params),
                total: totalItems,
                pagination: [
                        currentPage: page,
                        prevPage: page > 1 ? page - 1 : null,
                        nextPage: page < totalPages ? page + 1 : null,
                        totalPages: totalPages
                ]
        )
    }
}
