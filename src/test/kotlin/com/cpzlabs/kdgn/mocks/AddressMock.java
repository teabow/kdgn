package com.cpzlabs.kdgn.mocks;

import com.cpzlabs.kdgn.annotations.AutoMap;
import com.cpzlabs.kdgn.annotations.AutoModel;
import com.cpzlabs.kdgn.annotations.AutoModelField;

import java.io.Serializable;

@AutoMap
@AutoModel
public class AddressMock implements Serializable {

    @AutoModelField(required = true)
    private String street;

    @AutoModelField(required = true)
    private String city;

    @AutoModelField(required = true)
    private String zipCode;

    @AutoModelField(required = false)
    private String country;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
