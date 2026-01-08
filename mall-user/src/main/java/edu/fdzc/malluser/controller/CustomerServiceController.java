package edu.fdzc.malluser.controller;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.malluser.entity.CsReply;
import edu.fdzc.malluser.entity.CustomerService;
import edu.fdzc.malluser.service.CustomerServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer-service")
@RequiredArgsConstructor
public class CustomerServiceController {
    private final CustomerServiceService customerServiceService;

    @GetMapping("/list")
    public Result<List<CustomerService>> getUserConsultations(@RequestParam Long userId) {
        List<CustomerService> consultations = customerServiceService.getUserConsultations(userId);
        return Result.success(consultations);
    }

    @GetMapping("/detail/{id}")
    public Result<CustomerService> getConsultationDetail(@PathVariable Long id) {
        CustomerService consultation = customerServiceService.getConsultationDetail(id);
        return Result.success(consultation);
    }

    @GetMapping("/replies/{consultationId}")
    public Result<List<CsReply>> getConsultationReplies(@PathVariable Long consultationId) {
        List<CsReply> replies = customerServiceService.getConsultationReplies(consultationId);
        return Result.success(replies);
    }

    @PostMapping("/add")
    public Result<Boolean> addConsultation(@RequestBody CustomerService customerService) {
        boolean success = customerServiceService.addConsultation(customerService);
        return success ? Result.success() : Result.error("添加咨询失败");
    }

    @PostMapping("/reply/add")
    public Result<Boolean> addReply(@RequestBody CsReply csReply) {
        boolean success = customerServiceService.addReply(csReply);
        return success ? Result.success() : Result.error("添加回复失败");
    }
}
