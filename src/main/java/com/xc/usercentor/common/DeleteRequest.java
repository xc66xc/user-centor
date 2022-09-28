package com.xc.usercentor.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用删除请求
 */
@Data
public class DeleteRequest implements Serializable {
    private static final long serialVersionUID = 8429567016814008672L;
    private long id;
}
