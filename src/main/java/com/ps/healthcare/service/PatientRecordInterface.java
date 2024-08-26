package com.ps.healthcare.service;

import org.apache.catalina.connector.Response;

public interface PatientRecordInterface {

    Response fetchAndValidateIncomingRequest(String requestKey);
}
