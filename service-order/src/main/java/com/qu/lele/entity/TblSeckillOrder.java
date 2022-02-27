package com.qu.lele.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * tbl_seckill_order
 * @author 
 */
@Data
public class TblSeckillOrder implements Serializable {
    private Integer orderId;

    private Integer orderStatus;

    private String orderDescription;

    private Integer userId;

    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}