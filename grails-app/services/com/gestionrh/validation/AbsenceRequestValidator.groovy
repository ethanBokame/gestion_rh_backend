package com.gestionrh.validation

import com.gestionrh.AbsenceRequest
import com.gestionrh.User

class AbsenceRequestValidator {

    static void checkCanUpdateAndDelete(AbsenceRequest absenceRequest, User authenticatedUser) {
        // Not found
        if(!absenceRequest) throw new RuntimeException("Absence request not found")

        // User unauthorized
        if(absenceRequest.employee.id != authenticatedUser.id) throw new RuntimeException("Absence request not found")

        // Already controlled by a reviewer
        if(absenceRequest.reviewer != null) throw new RuntimeException("Action no longer available")
    }

    static void checkCanTreatAndShow(AbsenceRequest absenceRequest) {
        // Not found
        if(!absenceRequest) throw new RuntimeException("Absence request not found")
    }
}
