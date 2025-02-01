package com.yunchuan.medical.controller.doctor;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.ConsultationDTO;
import com.yunchuan.medical.dto.PrescriptionDTO;
import com.yunchuan.medical.dto.UserDTO;
import com.yunchuan.medical.dto.MedicalRecordDTO;
import com.yunchuan.medical.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 医生-患者管理
 */
@Tag(name = "医生-患者管理")
@RestController
@RequestMapping("/doctor/patients")
public class DoctorPatientController {

    private final MedicalRecordService medicalRecordService;

    public DoctorPatientController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @Operation(summary = "获取患者列表")
    @GetMapping
    public Result<Object> getPatientList() {
        // 模拟患者数据
        UserDTO patient1 = UserDTO.builder()
                .id("1")
                .username("zhang")
                .name("张三")
                .phone("13800138001")
                .email("zhang@example.com")
                .status("normal")
                .createTime(LocalDateTime.now().minusDays(30))
                .build();
                
        UserDTO patient2 = UserDTO.builder()
                .id("2")
                .username("li")
                .name("李四")
                .phone("13800138002")
                .email("li@example.com")
                .status("normal")
                .createTime(LocalDateTime.now().minusDays(20))
                .build();
                
        return Result.ok(new Object() {
            public final List<UserDTO> list = Arrays.asList(patient1, patient2);
            public final int total = 2;
        });
    }

    @Operation(summary = "获取患者详情")
    @GetMapping("/{id}")
    public Result<UserDTO> getPatientDetail(@PathVariable String id) {
        // 模拟患者详情数据
        UserDTO patient = UserDTO.builder()
                .id(id)
                .username("zhang")
                .name("张三")
                .phone("13800138001")
                .email("zhang@example.com")
                .status("normal")
                .createTime(LocalDateTime.now().minusDays(30))
                .build();
                
        return Result.ok(patient);
    }

    @Operation(summary = "获取患者问诊记录")
    @GetMapping("/{id}/consultations")
    public Result<Object> getPatientConsultations(@PathVariable String id) {
        // 模拟问诊记录数据
        ConsultationDTO consultation1 = ConsultationDTO.builder()
                .id("1")
                .userId(id)
                .userName("张三")
                .doctorId("1")
                .doctorName("张医生")
                .departmentId("1")
                .departmentName("内科")
                .symptoms("头痛，发烧38度")
                .diagnosis("上呼吸道感染")
                .treatment("建议服用感冒药，多休息")
                .status("COMPLETED")
                .startTime(LocalDateTime.now().minusDays(5))
                .endTime(LocalDateTime.now().minusDays(5).plusHours(1))
                .createTime(LocalDateTime.now().minusDays(5))
                .build();
                
        ConsultationDTO consultation2 = ConsultationDTO.builder()
                .id("2")
                .userId(id)
                .userName("张三")
                .doctorId("1")
                .doctorName("张医生")
                .departmentId("1")
                .departmentName("内科")
                .symptoms("咳嗽，胸闷")
                .diagnosis("支气管炎")
                .treatment("建议服用止咳药，注意保暖")
                .status("COMPLETED")
                .startTime(LocalDateTime.now().minusDays(2))
                .endTime(LocalDateTime.now().minusDays(2).plusHours(1))
                .createTime(LocalDateTime.now().minusDays(2))
                .build();
                
        return Result.ok(new Object() {
            public final List<ConsultationDTO> list = Arrays.asList(consultation1, consultation2);
            public final int total = 2;
        });
    }

    @Operation(summary = "获取患者处方记录")
    @GetMapping("/{id}/prescriptions")
    public Result<Object> getPatientPrescriptions(@PathVariable String id) {
        // 模拟处方记录数据
        PrescriptionDTO prescription1 = PrescriptionDTO.builder()
                .id("1")
                .consultationId("1")
                .patientId(id)
                .doctorId("1")
                .diagnosis("上呼吸道感染")
                .medications(Arrays.asList("布洛芬缓释胶囊 0.3g", "感冒灵颗粒 1袋"))
                .dosage("1. 每日2次，共3天\n2. 每日3次，共3天")
                .instructions("餐后服用，如有不适请立即停药")
                .status("COMPLETED")
                .createTime(LocalDateTime.now().minusDays(5))
                .build();
                
        PrescriptionDTO prescription2 = PrescriptionDTO.builder()
                .id("2")
                .consultationId("2")
                .patientId(id)
                .doctorId("1")
                .diagnosis("支气管炎")
                .medications(Arrays.asList("氨溴索片 30mg", "头孢克肟胶囊 0.1g"))
                .dosage("1. 每日3次，共5天\n2. 每日2次，共3天")
                .instructions("饭后服用，整片吞服")
                .status("COMPLETED")
                .createTime(LocalDateTime.now().minusDays(2))
                .build();
                
        return Result.ok(new Object() {
            public final List<PrescriptionDTO> list = Arrays.asList(prescription1, prescription2);
            public final int total = 2;
        });
    }

    @Operation(summary = "获取处方列表")
    @GetMapping("/{patientId}/prescriptions")
    public Result<Object> getPrescriptionList(@PathVariable String patientId) {
        // 模拟处方数据
        PrescriptionDTO prescription = PrescriptionDTO.builder()
                .id("1")
                .patientId(patientId)
                .doctorId("1")
                .diagnosis("感冒")
                .medications(Arrays.asList("布洛芬"))
                .dosage("每次一片，一日三次")
                .instructions("饭后服用")
                .status("PENDING")
                .createTime(LocalDateTime.now())
                .build();
                
        return Result.ok(new Object() {
            public final List<PrescriptionDTO> list = Arrays.asList(prescription);
            public final int total = 1;
        });
    }

    @Operation(summary = "获取处方详情")
    @GetMapping("/{patientId}/prescriptions/{id}")
    public Result<PrescriptionDTO> getPrescription(
            @PathVariable String patientId,
            @PathVariable String id) {
        // 模拟处方数据
        PrescriptionDTO prescription = PrescriptionDTO.builder()
                .id(id)
                .patientId(patientId)
                .doctorId("1")
                .diagnosis("感冒")
                .medications(Arrays.asList("布洛芬"))
                .dosage("每次一片，一日三次")
                .instructions("饭后服用")
                .status("PENDING")
                .createTime(LocalDateTime.now())
                .build();
                
        return Result.ok(prescription);
    }

    @Operation(summary = "获取患者病历记录")
    @GetMapping("/{id}/records")
    public Result<List<MedicalRecordDTO>> getPatientRecords(@PathVariable String id) {
        return Result.ok(medicalRecordService.getMedicalRecordList(id));
    }
} 