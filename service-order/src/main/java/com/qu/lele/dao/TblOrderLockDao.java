package com.qu.lele.dao;

import com.qu.lele.entity.TblOrderLock;
import com.qu.lele.entity.TblOrderLockExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TblOrderLockDao {
    long countByExample(TblOrderLockExample example);

    int deleteByExample(TblOrderLockExample example);

    int deleteByPrimaryKey(Integer orderId);

    int insert(TblOrderLock record);

    int insertSelective(TblOrderLock record);

    List<TblOrderLock> selectByExample(TblOrderLockExample example);

    TblOrderLock selectByPrimaryKey(Integer orderId);

    int updateByExampleSelective(@Param("record") TblOrderLock record, @Param("example") TblOrderLockExample example);

    int updateByExample(@Param("record") TblOrderLock record, @Param("example") TblOrderLockExample example);

    int updateByPrimaryKeySelective(TblOrderLock record);

    int updateByPrimaryKey(TblOrderLock record);
}