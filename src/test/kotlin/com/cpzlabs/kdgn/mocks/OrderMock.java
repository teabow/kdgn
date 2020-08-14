package com.cpzlabs.kdgn.mocks;

import com.cpzlabs.kdgn.annotations.AutoMap;
import com.cpzlabs.kdgn.annotations.AutoModel;
import com.cpzlabs.kdgn.annotations.AutoModelField;

import java.util.List;

@AutoMap
@AutoModel
public class OrderMock {

    @AutoModelField(required = true)
    private String orderId;

    @AutoModelField(required = true)
    private String customerId;

    @AutoModelField(required = true)
    private List<ProductMock> productsId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<ProductMock> getProductsId() {
        return productsId;
    }

    public void setProductsId(List<ProductMock> productsId) {
        this.productsId = productsId;
    }
}
