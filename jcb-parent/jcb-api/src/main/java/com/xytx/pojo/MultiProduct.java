package com.xytx.pojo;

import com.xytx.moder.BProductRecord;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class MultiProduct implements Serializable {
    private List<BProductRecord> xinShouBao;
    private List<BProductRecord> youXuan;
    private List<BProductRecord> sanBiao;
}

