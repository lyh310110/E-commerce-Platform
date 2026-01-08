package edu.fdzc.malluser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.fdzc.malluser.entity.PointRecord;
import edu.fdzc.malluser.entity.UserPoint;
import edu.fdzc.malluser.mapper.PointRecordMapper;
import edu.fdzc.malluser.mapper.UserPointMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPointService {
    private final UserPointMapper userPointMapper;
    private final PointRecordMapper pointRecordMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserPointService.class);

    public UserPoint getUserPoint(Long userId) {
        QueryWrapper<UserPoint> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        UserPoint userPoint = userPointMapper.selectOne(queryWrapper);
        if (userPoint == null) {
            userPoint = new UserPoint();
            userPoint.setUserId(userId);
            userPoint.setTotalPoints(0);
            userPoint.setAvailablePoints(0);
            userPoint.setUsedPoints(0);
            userPoint.setExpiredPoints(0);
            userPointMapper.insert(userPoint);
        }
        logger.info("获取用户 {} 的积分信息：{}", userId, userPoint);
        return userPoint;
    }

    @Transactional
    public boolean addPoints(Long userId, int points, String reason) {
        logger.info("为用户 {} 增加积分 {}，原因：{}", userId, points, reason);
        try {
            UserPoint userPoint = getUserPoint(userId);
            userPoint.setAvailablePoints(userPoint.getAvailablePoints() + points);
            userPoint.setTotalPoints(userPoint.getTotalPoints() + points);
            userPointMapper.updateById(userPoint);

            PointRecord pointRecord = new PointRecord();
            pointRecord.setUserId(userId);
            pointRecord.setAmount(points);
            pointRecord.setType(1); // 增加积分
            pointRecord.setDescription(reason);
            pointRecord.setCreateTime(new Date());
            pointRecord.setBalance(userPoint.getAvailablePoints());
            pointRecordMapper.insert(pointRecord);

            return true;
        } catch (Exception e) {
            logger.error("增加积分失败", e);
            return false;
        }
    }

    @Transactional
    public boolean deductPoints(Long userId, int points, String reason) {
        logger.info("为用户 {} 扣除积分 {}，原因：{}", userId, points, reason);
        try {
            UserPoint userPoint = getUserPoint(userId);
            if (userPoint.getAvailablePoints() < points) {
                return false;
            }
            userPoint.setAvailablePoints(userPoint.getAvailablePoints() - points);
            userPoint.setUsedPoints(userPoint.getUsedPoints() + points);
            userPointMapper.updateById(userPoint);

            PointRecord pointRecord = new PointRecord();
            pointRecord.setUserId(userId);
            pointRecord.setAmount(points);
            pointRecord.setType(2); // 扣除积分
            pointRecord.setDescription(reason);
            pointRecord.setCreateTime(new Date());
            pointRecord.setBalance(userPoint.getAvailablePoints());
            pointRecordMapper.insert(pointRecord);

            return true;
        } catch (Exception e) {
            logger.error("扣除积分失败", e);
            return false;
        }
    }

    public List<PointRecord> getPointRecords(Long userId) {
        QueryWrapper<PointRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        logger.info("获取用户 {} 的积分记录列表", userId);
        return pointRecordMapper.selectList(queryWrapper);
    }
}
