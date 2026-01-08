package edu.fdzc.malluser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.fdzc.malluser.entity.CsReply;
import edu.fdzc.malluser.entity.CustomerService;
import edu.fdzc.malluser.mapper.CsReplyMapper;
import edu.fdzc.malluser.mapper.CustomerServiceMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceService {
    private final CustomerServiceMapper customerServiceMapper;
    private final CsReplyMapper csReplyMapper;
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceService.class);

    public List<CustomerService> getUserConsultations(Long userId) {
        QueryWrapper<CustomerService> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        logger.info("获取用户 {} 的咨询列表", userId);
        return customerServiceMapper.selectList(queryWrapper);
    }

    public CustomerService getConsultationDetail(Long id) {
        logger.info("获取咨询详情，id: {}", id);
        return customerServiceMapper.selectById(id);
    }

    public List<CsReply> getConsultationReplies(Long consultationId) {
        QueryWrapper<CsReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cs_id", consultationId);
        queryWrapper.orderByAsc("create_time");
        logger.info("获取咨询 {} 的回复列表", consultationId);
        return csReplyMapper.selectList(queryWrapper);
    }

    public boolean addConsultation(CustomerService customerService) {
        logger.info("添加咨询：{}", customerService);
        try {
            customerService.setCreateTime(new Date());
            customerService.setUpdateTime(new Date());
            customerService.setStatus(0); // 未回复
            return customerServiceMapper.insert(customerService) > 0;
        } catch (Exception e) {
            logger.error("添加咨询失败", e);
            return false;
        }
    }

    public boolean addReply(CsReply csReply) {
        logger.info("添加回复：{}", csReply);
        try {
            csReply.setCreateTime(new Date());
            boolean success = csReplyMapper.insert(csReply) > 0;
            if (success) {
                CustomerService customerService = customerServiceMapper.selectById(csReply.getCsId());
                customerService.setStatus(1); // 已回复
                customerService.setUpdateTime(new Date());
                customerServiceMapper.updateById(customerService);
            }
            return success;
        } catch (Exception e) {
            logger.error("添加回复失败", e);
            return false;
        }
    }
}
