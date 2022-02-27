package com.qu.lele.service.impl;

import com.qu.lele.dao.TblInventoryDao;
import com.qu.lele.dao.TblSeckillOrderDao;
import com.qu.lele.entity.TblInventory;
import com.qu.lele.entity.TblSeckillOrder;
import com.qu.lele.service.PlaceOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: 屈光乐
 * @create: 2022-02-25 14-29
 * 秒杀的业务
 */
@Service("placeOrderService")
@Slf4j
public class PlaceOrderServiceImpl implements PlaceOrderService {
    @Autowired
    private TblInventoryDao tblInventoryDao;
    @Autowired
    private TblSeckillOrderDao tblSeckillOrderDao;

    @Override
    public boolean placeOrder(String goodId, String userId) {
        TblInventory tblInventory = tblInventoryDao.selectByPrimaryKey(Integer.valueOf(goodId));
        //扣减库存
        if (tblInventory.getNum() > 0) {
            tblInventory.setNum(tblInventory.getNum() -1);
            tblInventoryDao.updateByPrimaryKeySelective(tblInventory);
            log.debug("库存还剩余:{}",tblInventory.getNum());
            //插入订单信息
            TblSeckillOrder tblSeckillOrder = new TblSeckillOrder();
            tblSeckillOrder.setOrderDescription("用户" + userId +"购买成功,此商品库存还剩" + tblInventory.getNum()+"件");
            tblSeckillOrder.setOrderStatus(0);
            tblSeckillOrder.setUserId(Integer.valueOf(userId));
            tblSeckillOrderDao.insertSelective(tblSeckillOrder);
            return true;
        } else {
            return false;
        }
    }
}
