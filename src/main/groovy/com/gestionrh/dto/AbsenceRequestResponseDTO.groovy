package com.gestionrh.dto

import com.gestionrh.AbsenceRequest
import com.gestionrh.AbsenceStatus

import java.text.SimpleDateFormat

class AbsenceRequestResponseDTO {
    Long id
    String reason
    AbsenceStatus status
    String beginDate
    String endDate

    AbsenceRequestResponseDTO(AbsenceRequest absenceRequest) {
        def formatter = new SimpleDateFormat("yyyy-MM-dd")

        id = absenceRequest.id
        reason = absenceRequest.reason
        status = absenceRequest.status
        beginDate = formatter.format(absenceRequest.beginDate)
        endDate = formatter.format(absenceRequest.endDate)
    }
}