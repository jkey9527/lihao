package com.cattle.lihao.controller;

import com.cattle.lihao.bean.ProductBean;
import com.cattle.lihao.bean.ProductDetailBean;
import com.cattle.lihao.response.Result;
import com.cattle.lihao.service.ProductService;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 货品
 *
 * @author niujie
 * @date 2023/4/21 22:43
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "cattle/lihao/product", method = RequestMethod.POST)
@CrossOrigin(origins = "*")
public class ProductController {
    private static final Logger LOGGER = Logger.getLogger(ProductController.class);

    private ProductService productService;

    /**
     * 新建货品
     * @param product product
     * @return java.lang.String
     * @author niujie
     * @date 2023/8/5
     */
    @RequestMapping("/saveProduct")
    public String saveProduct(@RequestBody ProductBean product) {
        try {
            productService.saveProduct(product);
            return Result.success("操作成功！");
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 增加库存
     * @param productDetails productDetails
     * @return java.lang.String
     * @author niujie
     * @date 2023/8/5
     */
    @RequestMapping("/addProductDetail")
    public String addProductDetail(@RequestBody List<ProductDetailBean> productDetails) {
        try {
            productService.addProductDetail(productDetails);
            return Result.success("操作成功！");
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 减少库存
     * @param productDetails productDetails
     * @return java.lang.String
     * @author niujie
     * @date 2023/8/5
     */
    @RequestMapping("/subProductDetail")
    public String subProductDetail(@RequestBody List<ProductDetailBean> productDetails) {
        try {
            productService.subProductDetail(productDetails);
            return Result.success("操作成功！");
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 查询货品信息
     * @param product product
     * @return java.lang.String
     * @author niujie
     * @date 2023/8/5
     */
    @RequestMapping("/getProductList")
    public String getProductList(@RequestBody ProductBean product) {
        try {
            List<ProductBean> productList = productService.getProductList(product);
            return Result.success("操作成功！", productList);
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 查询货品信息
     * @param product product
     * @return java.lang.String
     * @author niujie
     * @date 2023/8/5
     */
    @RequestMapping("/getProduct")
    public String getProduct(@RequestBody ProductBean product) {
        try {
            ProductBean productBean = productService.getProduct(product);
            return Result.success("操作成功！", productBean);
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 库存
     * @param product product
     * @return java.lang.String
     * @author niujie
     * @date 2023/8/6
     */
    @RequestMapping("/getProducts")
    public String getProducts(@RequestBody ProductBean product) {
        try {
            List<ProductBean> productBeans = productService.getProducts(product);
            return Result.success("操作成功！", productBeans);
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

}
