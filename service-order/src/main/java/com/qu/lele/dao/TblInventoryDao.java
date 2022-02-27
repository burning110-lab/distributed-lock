package com.qu.lele.dao;

import com.qu.lele.entity.TblInventory;
import com.qu.lele.entity.TblInventoryExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TblInventoryDao {
    long countByExample(TblInventoryExample example);

    int deleteByExample(TblInventoryExample example);

    int deleteByPrimaryKey(Integer goodId);

    int insert(TblInventory record);

    int insertSelective(TblInventory record);

    List<TblInventory> selectByExample(TblInventoryExample example);

    TblInventory selectByPrimaryKey(Integer goodId);

    int updateByExampleSelective(@Param("record") TblInventory record, @Param("example") TblInventoryExample example);

    int updateByExample(@Param("record") TblInventory record, @Param("example") TblInventoryExample example);

    int updateByPrimaryKeySelective(TblInventory record);

    int updateByPrimaryKey(TblInventory record);
}