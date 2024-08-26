package com.ps.healthcare.model;

public class UpdatePatientRecordRequest {

    String patientId;
    String Diagnoses;
    String treatmentPlans;
    String prescriptions;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDiagnoses() {

        return Diagnoses;
    }

    public void setDiagonses(String diagonses) {
        Diagnoses = diagonses;
    }

    public String getTreatmentPlans() {
        return treatmentPlans;
    }

    public void setTreatmentPlans(String treatmentPlans) {
        this.treatmentPlans = treatmentPlans;
    }

    public String getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(String prescriptions) {
        this.prescriptions = prescriptions;
    }


}
