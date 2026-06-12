package com.gestionrh.dto

import com.gestionrh.AbsenceRequest
import com.gestionrh.AbsenceStatus

import java.text.SimpleDateFormat

class AbsenceRequestResponseRhDTO {
    Long id
    String employee
    String reason
    AbsenceStatus status
    String beginDate
    String endDate

    AbsenceRequestResponseRhDTO(AbsenceRequest absenceRequest) {
        def formatter = new SimpleDateFormat("yyyy-MM-dd")

        id = absenceRequest.id
        employee = absenceRequest.employee.email
        reason = absenceRequest.reason
        status = absenceRequest.status
        beginDate = formatter.format(absenceRequest.beginDate)
        endDate = formatter.format(absenceRequest.endDate)
    }
}
