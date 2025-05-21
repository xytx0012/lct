package com.xytx.controller;


import com.xytx.enums.RCode;
import com.xytx.moder.BProductRecord;
import com.xytx.pojo.MultiProduct;
import com.xytx.service.ProductService;
import com.xytx.view.R;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class ProductController {
    @DubboReference(interfaceClass = ProductService.class,version="1.0")
    private ProductService productService;

    /**
     * 查询每个产品类型的前3个产品进行展示
     * @return
     */
    @GetMapping("/product/index")
    public R productIndex(){
        MultiProduct  multiProduct = productService.queryIndexPageProducts();
        return R.ok(multiProduct);
    }

    /**
     * 根据产品类型分页查询
     * @param pType
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/product/list")
    public R queryProductByType(@RequestParam("ptype") Integer pType,
                                @RequestParam(value = "pageNo",required = false,defaultValue = "1") Integer pageNo,
                                @RequestParam(value = "pageSize",required = false,defaultValue = "9") Integer pageSize
                                ){
        if(pType==null||(pType!=0&&pType!=1&&pType!=2)){
            return R.err(RCode.REQUEST_PRODUCT_TYPE_ERR);
        }
        if(pageNo==null||pageNo<1){
            pageNo=1;
        }
        if(pageSize==null||pageSize<1){
            pageSize=1;
        }

        return productService.queryProductByType(pType,pageNo,pageSize);
    }

    /**
     * 根据id查询产品详细信息和产品投资记录
     * @return
     */
    @GetMapping("/product/info")
    public R queryProductFullInfoById(@RequestParam("productId") Integer productId){
        if(productId==null||productId<1){
            return R.err(RCode.REQUEST_PARAM_ERR);
        }
        BProductRecord bProductRecord = productService.queryProductFullInfoById(productId);
        return R.ok(bProductRecord);
    }

    /**
     * 根据用户id查最近投资产品
     */
    @GetMapping("/product/records")
    public R queryProductRecords(@RequestParam("pageNum") Integer pageNum
            , @RequestParam("pageSize") Integer pageSize
            , @RequestHeader("Uid") Integer userId){

        if(userId==null||userId<1){
            return R.err(RCode.REQUEST_PARAM_ERR);
        }
        if(pageNum==null||pageNum<1){
            pageNum=1;
        }
        if(pageSize==null||pageSize<1){
            pageSize=6;
        }
        return productService.queryProductRecords(pageNum,pageSize,userId);
    }
}
