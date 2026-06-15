package com.gestionrh

import com.gestionrh.dto.AbsenceRequestRequestDTO
import grails.plugin.springsecurity.annotation.Secured
import org.springframework.beans.factory.annotation.Value

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

    // Super ! mais ajoute une valeur par defaut sinon le système plantera si jamais la valeur n'existe pas
    // Pagination parameters
    @Value('${info.app.pagination.itemsReturnedMin}')
    Integer itemsReturnedMin

    // Super ! mais ajoute une valeur par defaut sinon le système plantera si jamais la valeur n'existe pas
    @Value('${info.app.pagination.itemsReturnedMin}')
    Integer itemsReturnedMax


    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured('ROLE_RH')
    def list(Integer max, Integer page) {
        // Pagination parameters
        // Cool mais je pense que définir juste le nombre d'élément sur la page est suffisant
        max = Math.min(max ?: itemsReturnedMin, itemsReturnedMax)
        page = page ?: 1

        params.max = max
        params.offset = (page - 1) * max
        // Ce que je propose
        // Long pageSize = max
        // Long pageIndex = page ?: 0
        // Long offset = pageIndex * pageSize
        
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
        // Evite les exceptions dans les controllers
        try {
            respond absenceRequestService.getById(id)
        }
        catch (Exception e) {
            respond([error: e.message], status: BAD_REQUEST)
        }
    }

    @Secured('ROLE_USER')
    def save(AbsenceRequestRequestDTO userInput) {
        // Evite les exceptions dans les controllers
        try {
            // Get employee
            User authenticatedUser = springSecurityService.getCurrentUser()

            respond absenceRequestService.create(authenticatedUser, userInput), status: CREATED
        }
        catch (Exception e) {
            respond([error: e.message], status: BAD_REQUEST)
        }
    }

    @Secured(['ROLE_USER'])
    @Transactional
    def update(Long id, AbsenceRequestRequestDTO userInput) {
        // Evite les exceptions dans les controllers
        try {
            // Get employee
            User authenticatedUser = springSecurityService.getCurrentUser()

            absenceRequestService.update(authenticatedUser, id, userInput)

            render status: NO_CONTENT
        }
        catch (Exception e) {
            respond ([error: e.message], status: BAD_REQUEST)
        }
    }

    @Secured('ROLE_USER')
    @Transactional
    def delete(Long id) {
        // Evite les exceptions dans les controllers
        try {
            // Get employee
            User authenticatedUser = springSecurityService.getCurrentUser()

            // Delete absence service
            absenceRequestService.deleteById(authenticatedUser, id)

            render status: NO_CONTENT
        }
        catch (Exception e) {
            respond ([error: e.message], status: NOT_FOUND)
        }

    }

    @Secured('ROLE_RH')
    @Transactional
    def approved(Long id) {
        // Evite les exceptions dans les controllers
        try {
            // Get reviewer
            User authenticatedUser = springSecurityService.getCurrentUser()

            // Approved absence request
            absenceRequestService.approved(authenticatedUser, id)

            render status: NO_CONTENT
        } catch (Exception e) {
            respond ([error: e.message], status: NOT_FOUND)
        }
    }

    @Secured('ROLE_RH')
    @Transactional
    def rejected(Long id) {
        // Evite les exceptions dans les controllers
        try {
            // Get reviewer
            User authenticatedUser = springSecurityService.getCurrentUser()

            // Approved absence request
            absenceRequestService.rejected(authenticatedUser, id)

            render status: NO_CONTENT
        } catch (Exception e) {
            respond ([error: e.message], status: NOT_FOUND)
        }
    }

    @Secured(['ROLE_USER'])
    @Transactional
    // Trop de code dupliqué dans cette methode(Methode 1)
    def myRequests(Integer max, Integer page) {
        // Evite les exceptions dans les controllers
        // Pagination parameters
        max = Math.min(max ?: itemsReturnedMin, itemsReturnedMax)
        page = page ?: 1

        params.max = max
        params.offset = (page - 1) * max

        // Get employee
        User authenticatedUser = springSecurityService.getCurrentUser()

        // Totals
        Integer totalItems = absenceRequestService.getAllByEmployee(authenticatedUser).size()
        Integer totalPages = (int) Math.ceil(totalItems / max)

        respond (
                data: absenceRequestService.getAllByEmployee(authenticatedUser, params),
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
