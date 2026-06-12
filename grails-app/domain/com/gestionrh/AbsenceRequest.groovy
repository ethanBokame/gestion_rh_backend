package com.gestionrh

class AbsenceRequest {

    User employee
    User reviewer

    String reason

    AbsenceStatus status = AbsenceStatus.PENDING

    Date beginDate
    Date endDate

    Date dateCreated
    Date lastUpdated

    static constraints = {
        employee nullable: true
        reviewer nullable: true

        reason nullable: false, blank: false

        beginDate nullable: false
        endDate nullable: false

        endDate validator: { val, obj ->
            val >= obj.beginDate
        }
    }
}
