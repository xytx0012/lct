package com.xytx.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xytx.constants.JCBConstant;
import com.xytx.mapper.BBidInfoMapper;
import com.xytx.mapper.BProductRecordMapper;
import com.xytx.moder.BProductRecord;
import com.xytx.pojo.BidInfoProduct;
import com.xytx.pojo.MultiProduct;
import com.xytx.util.CommonUtil;
import com.xytx.view.R;
import com.xytx.vo.ProductRecordVo;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

@DubboService(interfaceClass = ProductService.class, version = "1.0")
public class ProductServiceImpl implements ProductService {
    @Resource
    private BProductRecordMapper productRecordMapper;
    @Resource
    private BBidInfoMapper bidInfoMapper;


    @Override
    public R queryProductByType(Integer pType,Integer pageNo,Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<BProductRecord> productList = productRecordMapper.selectByTypeLimit(pType);
        PageInfo<BProductRecord> bProductRecordList = new PageInfo<>(productList);
        return  R.ok(bProductRecordList);
    }

    @Override
    public MultiProduct queryIndexPageProducts() {
        MultiProduct multiProduct = new MultiProduct();
        List<BProductRecord> xinShouBao = queryLimit(JCBConstant.PRODUCT_TYPE_XINSHOUBAO,1);
        List<BProductRecord> youXuan = queryLimit(JCBConstant.PRODUCT_TYPE_YOUXUAN,3);
        List<BProductRecord> sanBiao = queryLimit(JCBConstant.PRODUCT_TYPE_SANBIAO,3);
        multiProduct.setXinShouBao(xinShouBao);
        multiProduct.setYouXuan(youXuan);
        multiProduct.setSanBiao(sanBiao);
        return multiProduct;
    }
    private List<BProductRecord> queryLimit(Integer pType, Integer pageSize){
        PageHelper.startPage(0,pageSize);
        return productRecordMapper.selectByTypeLimit(pType);
    }

    @Override
    public BProductRecord queryProductFullInfoById(Integer productId) {
        BProductRecord bProductRecord = productRecordMapper.selectProductDetilById(productId);
        List<BidInfoProduct> bidInfoProductList = bidInfoMapper.selectByProductId(productId);
        bidInfoProductList.forEach((bidInfoProduct) -> {
            bidInfoProduct.setPhone(CommonUtil.phoneTuoMing(bidInfoProduct.getPhone()));
        });
        bProductRecord.setBidInfoProducts(bidInfoProductList);
        return bProductRecord;
    }

    @Override
    public R queryProductRecords(Integer pageNum,Integer pageSize,Integer userId) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProductRecordVo> productRecordVoList = bidInfoMapper.selectByUserId(userId);
        PageInfo<ProductRecordVo> pageInfo = new PageInfo<>(productRecordVoList);
        return R.ok(pageInfo);
    }

}
