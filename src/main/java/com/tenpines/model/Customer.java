package com.tenpines.model;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.search.SearchHit;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

@Entity
@Table( name = "CUSTOMERS" )

public class Customer {


    @Id
	@GeneratedValue
	private long id;
	@NotEmpty
	private String firstName;
	@NotEmpty
	private String lastName;
	@Pattern(regexp="D|C")
	private String identificationType;
	@NotEmpty
	private String identificationNumber;

	public Customer(){

    }

	public static Customer create(String firstName, String lastName, String type, String number) {
		Customer customer = new Customer();
		customer.firstName = firstName;
		customer.lastName = lastName;
		customer.identificationType = type;
		customer.identificationNumber = number;
		return customer;
	}

    public static Customer getCustomerFromJSon(SearchHit hit) {
        return JSON.parseObject(hit.getSourceAsString(), Customer.class) ;
    }

	public static Customer createForElastic(String firstName, String lastName, String type, String number, int id) {
		Customer customer = new Customer();
		customer.firstName = firstName;
		customer.lastName = lastName;
		customer.identificationType = type;
		customer.identificationNumber = number;
		customer.id = id;
		return customer;
	}


	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getIdentificationType() {
		return identificationType;
	}

	public void setIdentificationType(String identificationType) {
		this.identificationType = identificationType;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}


	public String parseToJson() {
		return JSON.toJSONString(this);
	}
}
