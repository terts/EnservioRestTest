package com.enservioTest.com.rest;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;

import org.json.JSONArray;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class RestTest {

    @BeforeClass
    public void setBaseUri () {
        RestAssured.baseURI = "https://qriusity.com";
    }

    private boolean testStatusCode (int expectedStatus, Response response)  {
    	boolean validation = false;
    	int status = response.getStatusCode();
        if (status == expectedStatus) {
        	validation = true;
        	Assert.assertEquals(status, expectedStatus, "Correct status code returned.");
        } else {
        	Assert.fail("The page did not load properly.");
        }
        return validation;
    }
    	
    @Test
    public void checkCorrectNumberOfQuestions() {
        //get the response based on the baseURI plus params
    	//https://qriusity.com/v1/questions?page=1&limit=25
        Response response = given().param ("page", "1").param ("limit", "25")
        		.when().get("/v1/questions")
        		.then().contentType(ContentType.JSON)
        		.extract().response();
        if (testStatusCode(200, response)) {
        	int numQues = response.body().path("name.size()");
        	Assert.assertEquals(numQues, 20, "Number of questions was correctly truncated down to 20.");
        }
    }
      
    @Test
    public void checkCorrectNumberOfCategories() {
        //get the response based on the baseURI plus params
    	//https://qriusity.com/v1/categories?page=1&limit=25
        Response response = given().param ("page", "1").param ("limit", "25")
        		.when().get("/v1/categories")
        		.then().contentType(ContentType.JSON)
        		.extract().response();
        if (testStatusCode(200, response)) {
        	int numQues = response.body().path("name.size()");
        	Assert.assertEquals(numQues, 20, "Number of categories was correctly truncated down to 20.");
        }
    }
      
    @Test
    public void verifyCapital() throws JSONException {
        //get capital of Albania
    	//I would normally have the country and correct capital passed as a variable to the method
        Response response = get("http://restcountries.eu/rest/v1/name/albania");
        JSONArray jsonResponse = new JSONArray(response.asString());
        String capital = jsonResponse.getJSONObject(0).getString("capital");
        Assert.assertEquals(capital, "Tirana", "The country Albania's captial is " + capital);
    }
}
