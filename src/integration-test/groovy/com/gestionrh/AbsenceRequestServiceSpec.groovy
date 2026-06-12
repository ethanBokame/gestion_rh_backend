package com.gestionrh

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import org.grails.datastore.mapping.core.Datastore
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Integration
@Rollback
class AbsenceRequestServiceSpec extends Specification {

    AbsenceRequestService absenceRequestService
    @Autowired Datastore datastore

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new AbsenceRequest(...).save(flush: true, failOnError: true)
        //new AbsenceRequest(...).save(flush: true, failOnError: true)
        //AbsenceRequest absenceRequest = new AbsenceRequest(...).save(flush: true, failOnError: true)
        //new AbsenceRequest(...).save(flush: true, failOnError: true)
        //new AbsenceRequest(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //absenceRequest.id
    }

    void cleanup() {
        assert false, "TODO: Provide a cleanup implementation if using MongoDB"
    }

    void "test get"() {
        setupData()

        expect:
        absenceRequestService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<AbsenceRequest> absenceRequestList = absenceRequestService.list(max: 2, offset: 2)

        then:
        absenceRequestList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        absenceRequestService.count() == 5
    }

    void "test delete"() {
        Long absenceRequestId = setupData()

        expect:
        absenceRequestService.count() == 5

        when:
        absenceRequestService.delete(absenceRequestId)
        datastore.currentSession.flush()

        then:
        absenceRequestService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        AbsenceRequest absenceRequest = new AbsenceRequest()
        absenceRequestService.save(absenceRequest)

        then:
        absenceRequest.id != null
    }
}
