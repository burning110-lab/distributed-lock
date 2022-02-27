package com.qu.lele.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * tbl_inventory
 * @author 
 */
@Data
public class TblInventory implements Serializable {
    private Integer goodId;

    private Integer num;

    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}