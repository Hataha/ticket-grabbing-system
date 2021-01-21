package com.kinoko.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kinoko.entity.Manager;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ManagerMapper extends BaseMapper<Manager> {

}
