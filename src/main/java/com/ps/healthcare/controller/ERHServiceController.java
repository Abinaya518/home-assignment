package com.ps.healthcare.controller;


import com.ps.healthcare.model.PatientRecord;
import com.ps.healthcare.model.UpdatePatientRecordRequest;
import com.ps.healthcare.service.PatientRecordInterface;
import com.ps.healthcare.service.PatientRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ehr")
@Slf4j
public class ERHServiceController {

    private PatientRecordService patientRecordService;

    private PatientRecordInterface patientRecordInterface;


    @GetMapping("/getPatientRecord/{patientId}")
    public ResponseEntity<PatientRecord> getPatientRecord(@PathVariable String patientId) throws ChangeSetPersister.NotFoundException {

        PatientRecord record = patientRecordService.getPatientRecord(patientId);
        log.info("fetching the patient record from database");
        return ResponseEntity.ok(record);
    }

    public ResponseEntity<String> updatePatientRecord(@PathVariable String patientId, @RequestBody UpdatePatientRecordRequest request) throws ChangeSetPersister.NotFoundException {
        ScheduleJob scheduleJob = new ScheduleJob(patientId);
        scheduleJob.start();
        //processLogService.updateClientProcessLog(requestKey,rowKey,"Done",clientProcessLog, "", "");
        log.info("exit from controller");
        patientRecordService.updatePatientRecord(patientId, request.getDiagnoses(), request.getPrescriptions(), request.getTreatmentPlans());

        log.info("Updating the patient records successfuly");
        return ResponseEntity.ok("Patient Record updated sucessfully");


    }

    class ScheduleJob extends Thread {


        ScheduleJob(String patientId) {

        }

        @Override
        public void run() {
            log.info("Entered in ValidatorController");
            Response response = patientRecordInterface.fetchAndValidateIncomingRequest(patientRecordService.toString());

            // logger.info("RequestKey==>" + requestKey + ":: response==>" + response);
        }
    }
}

