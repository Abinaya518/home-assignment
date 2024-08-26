package com.ps.healthcare.repository;

import com.ps.healthcare.model.PatientRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRecordRepository  extends JpaRepository<PatientRecord,Long> {
    public PatientRecord findPatientById(String patientId);
}
