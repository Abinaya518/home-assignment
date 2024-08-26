package com.ps.healthcare.service;

import com.ps.healthcare.model.PatientRecord;
import com.ps.healthcare.repository.PatientRecordRepository;
import com.ps.healthcare.utility.ValidateRequestWorker;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;


import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;


import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PatientRecordService {

    private  final PatientRecordRepository patientRecordRepository;
    private final ThreadPoolExecutor executor;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public PatientRecordService(PatientRecordRepository patientRecordRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.patientRecordRepository = patientRecordRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10); // Configure as needed
    }

    @Transactional
    @Cacheable(value = "patientRecords", key = "#patientId")
    public PatientRecord getPatientRecord(String patientId) {
        log.info("Get patient records");
        return patientRecordRepository.findPatientById(patientId);


}
   @Transactional
   @CachePut(value = "patientRecords", key = "#patientId")
    public void updatePatientRecord(String patientId,String diagnoses,String prescriptions,String treatmentPlans)  {
        PatientRecord record = patientRecordRepository.findPatientById(patientId);
        log.info("Updating the patient records");
        if(record!=null){
        record.setDiagnoses(diagnoses);
        record.setPrescriptions(prescriptions);
        record.setTreatmentPlans(treatmentPlans);

        patientRecordRepository.save(record);

            String message = String.format("Updated record for patient ID: %s", patientId);
            kafkaTemplate.send("patient-record-topic", message);
        } else {
            log.warn("Patient record not found for ID: {}", patientId);
        }
    }

    public Response validateIncomingRequest(String inputJson) {
        Response response = null;
        try {
            //how to get iterate json  and convert it to json object(multiple records)
            JSONObject obj =  new JSONObject(inputJson);

            JSONObject jsonObject = obj.getJSONObject("Diagnosis");
            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject patientRecord = jsonObject.getJSONObject(String.valueOf(i));
                Runnable validateRequestWorker = new ValidateRequestWorker(patientRecord);
                executor.execute(validateRequestWorker);
            }

            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                    if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                        log.error("Executor did not terminate");
                    }
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }

        } catch (Exception e) {
            log.error("Exception during request validation", e);
            // Handle exception or notify via email
        }
        return response;
    }



    }
