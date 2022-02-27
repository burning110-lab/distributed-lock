package com.qu.lele.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * tbl_order_lock
 * @author 
 */
@Data
public class TblOrderLock implements Serializable {
    private Integer orderId;

    private Integer userId;

    private static final long serialVersionUID = 1L;
}