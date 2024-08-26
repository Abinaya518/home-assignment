package com.ps.healthcare;

import static org.junit.jupiter.api.Assertions.assertEquals;
        import static org.mockito.Mockito.mock;
        import static org.mockito.Mockito.when;

import com.ps.healthcare.model.PatientRecord;
import com.ps.healthcare.repository.PatientRecordRepository;
import com.ps.healthcare.service.PatientRecordService;
import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.springframework.boot.test.context.SpringBootTest;
        import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBootTest
@SpringJUnitConfig
public class PatientRecordServiceTests {

    @MockBean
    private PatientRecordRepository patientRecordRepository;

    @MockBean
    private KafkaTemplate kafkaTemplate;

    private PatientRecordService patientRecordService;

    @BeforeEach
    public void setUp() {
        patientRecordService = new PatientRecordService(patientRecordRepository, kafkaTemplate);
    }

    @Test
    public void testGetPatientRecord() throws ChangeSetPersister.NotFoundException {
        // Mock data
        String patientId = "123";
        PatientRecord mockRecord = new PatientRecord();
        mockRecord.setPatientId(patientId);
        mockRecord.setDiagnoses("Test Diagnosis");

        // Mock repository behavior
        when(patientRecordRepository.findPatientById(patientId)).thenReturn(mockRecord);

        // Call service method
        PatientRecord result = patientRecordService.getPatientRecord(patientId);

        // Verify the result
        assertEquals(patientId, result.getPatientId());
        assertEquals("Test Diagnosis", result.getDiagnoses());
    }
}
