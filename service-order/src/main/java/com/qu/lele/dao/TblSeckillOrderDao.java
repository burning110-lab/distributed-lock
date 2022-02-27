package com.qu.lele.dao;

import com.qu.lele.entity.TblSeckillOrder;
import com.qu.lele.entity.TblSeckillOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TblSeckillOrderDao {
    long countByExample(TblSeckillOrderExample example);

    int deleteByExample(TblSeckillOrderExample example);

    int deleteByPrimaryKey(Integer orderId);

    int insert(TblSeckillOrder record);

    int insertSelective(TblSeckillOrder record);

    List<TblSeckillOrder> selectByExample(TblSeckillOrderExample example);

    TblSeckillOrder selectByPrimaryKey(Integer orderId);

    int updateByExampleSelective(@Param("record") TblSeckillOrder record, @Param("example") TblSeckillOrderExample example);

    int updateByExample(@Param("record") TblSeckillOrder record, @Param("example") TblSeckillOrderExample example);

    int updateByPrimaryKeySelective(TblSeckillOrder record);

    int updateByPrimaryKey(TblSeckillOrder record);
}