package com.tenpines.utils;

import com.tenpines.model.Customer;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElasticCustomerSystem implements CustomerSystem{


    public static final String CUSTOMER = "customer";
    private static final int MAX_SIZE = 10000;
    private Client client;

    public static CustomerSystem initialize() {
        CustomerSystem customerSystem = new ElasticCustomerSystem();
        customerSystem.initDbConfiguration();
        return customerSystem;
    }

    public void closeSessionAndCommit() {
        client.close();
    }

    public void saveCustomer(Customer newCustomer) {
         String json = newCustomer.parseToJson();
        IndexResponse response = client.prepareIndex(CUSTOMER, "_doc")
                .setSource(json, XContentType.JSON).get();
    }

    public List<Customer> getAllCustomers() {
        SearchResponse response = client.prepareSearch(CUSTOMER)
                .setTypes("_doc")
                .setQuery(QueryBuilders.matchAllQuery())
                .setSize(MAX_SIZE)
                .setFrom(0 * MAX_SIZE)
                .execute()
                .actionGet();
        List<SearchHit> searchHits = Arrays.asList(response.getHits().getHits());
        List<Customer> results = new ArrayList<Customer>();
        searchHits.forEach(
                hit -> results.add(Customer.getCustomerFromJSon(hit)));
        return results;
    }

    public Customer findCustomerByName(String pepe) {
        return null;
    }

    public void initDbConfiguration() {
        try {
            client = new PreBuiltTransportClient(
            Settings.builder().put("client.transport.sniff", true)
                    .put("cluster.name","elasticsearch").build())
            .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAll() {
        getAllCustomers().forEach(customer ->{
            client.prepareDelete(CUSTOMER, "firstName", customer.getFirstName())
                    .execute()
                    .actionGet();


        });
    }
}
