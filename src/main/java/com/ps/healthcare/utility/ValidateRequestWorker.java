package com.ps.healthcare.utility;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;


@Slf4j
public class ValidateRequestWorker implements Runnable {


        private final JSONObject patientRecord;



    public ValidateRequestWorker(JSONObject patientRecord) {
        this.patientRecord = patientRecord;
    }

    @Override
        public void run() {
            try {
                // Implement the validation logic here
                log.info("Validating record: {}", patientRecord.toString());
                // e.g., validateJsonRequest(clientRecord);
            } catch (Exception e) {
                log.error("Error validating record: {}", patientRecord.toString(), e);
            }
        }
    }

