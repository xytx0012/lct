package com.xytx.service;


import com.xytx.moder.BProductRecord;
import com.xytx.pojo.MultiProduct;
import com.xytx.view.R;

public interface ProductService {
    R queryProductByType(Integer pType, Integer pageNo, Integer pageSize);

    MultiProduct queryIndexPageProducts();


    BProductRecord queryProductFullInfoById(Integer productId);

    R queryProductRecords(Integer pageNum,Integer pageSize,Integer userId);
}
