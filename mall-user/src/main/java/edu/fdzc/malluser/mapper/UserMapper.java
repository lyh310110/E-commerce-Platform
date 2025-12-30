package edu.fdzc.malluser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.fdzc.malluser.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
