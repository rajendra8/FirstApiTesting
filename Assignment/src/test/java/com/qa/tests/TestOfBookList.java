package com.qa.tests;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.qa.base.TestBase;
import com.qa.client.RestClient;
import com.qa.util.TestUtil;

public class TestOfBookList extends TestBase {
	TestBase testBase;
	String serviceUrl;
	String apiUrl;
	String url;
	String negativeBookListPara;
	String urlForMovie;
	RestClient restClient;
	CloseableHttpResponse closebaleHttpResponse;

	@BeforeMethod
	public void setUp() throws ClientProtocolException, IOException {
		testBase = new TestBase();
	}

	// to check the list of books
	@Test
	public void getThebookCount() throws ClientProtocolException, IOException {
		restClient = new RestClient();
		closebaleHttpResponse = restClient.get(prop.getProperty("LocusBookListPara"));
		// need to check the total list is 3 and status code
		// a. Status Code:
		int statusCode = closebaleHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status Code--->" + statusCode);
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");
		// b. Json String:
		String responseString = EntityUtils.toString(closebaleHttpResponse.getEntity(), "UTF-8");
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("Response JSON from API---> " + responseJson);
		// Booksvalue:
		String Booksvalue = TestUtil.getValueByJPath(responseJson, "/items/total");
		System.out.println("value of books is-->" + Booksvalue);
		Assert.assertEquals(Integer.parseInt(Booksvalue), 3);
	}

	// to check the status code without token bearer
	@Test
	public void getNegativeMovieList() throws ClientProtocolException, IOException {
		restClient = new RestClient();
		closebaleHttpResponse = restClient.get(prop.getProperty("UriToGetMovie"));
		// need to check the total list is 3 and status code
		// a. Status Code:
		int statusCode = closebaleHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status Code--->" + statusCode);
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_401, "Status code is not 401");
		// b. Json String:
		String responseString = EntityUtils.toString(closebaleHttpResponse.getEntity(), "UTF-8");
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("Response JSON from API---> " + responseJson);
	}

	// to check the movie list with token bearer
	@Test(priority = 2)
	public void getApiTestWithTokenBearer() throws ClientProtocolException, IOException {
		restClient = new RestClient();
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Authorization", "Bearer " + prop.getProperty("TokenKey"));
		closebaleHttpResponse = restClient.get(prop.getProperty("UriToGetMovie"), headerMap);
		int statusCode = closebaleHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status Code--->" + statusCode);
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");
		String responseString = EntityUtils.toString(closebaleHttpResponse.getEntity(), "UTF-8");
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("Response JSON from API---> " + responseJson);
	}

	@Test
	public void getMovieIdAndSpecificStatus() throws ClientProtocolException, IOException {
		String id;
		restClient = new RestClient();
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Authorization", "Bearer " + prop.getProperty("TokenKey"));
		closebaleHttpResponse = restClient.get(prop.getProperty("UriToGetMovie"), headerMap);
		int statusCode = closebaleHttpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");
		String responseString = EntityUtils.toString(closebaleHttpResponse.getEntity(), "UTF-8");
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("Response JSON from API---> " + responseJson);
		String movieId = TestUtil.getValueByJPath(responseJson, "/docs[0]/_id");
		System.out.println("get the id of 2nd movies-->" + movieId);
		closebaleHttpResponse = restClient.get(prop.getProperty("UriToGetMovie").concat("/" + movieId).concat("/quote"),
				headerMap);
		int statusCode1 = closebaleHttpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode1, RESPONSE_STATUS_CODE_200, "Status code is not 200");
	}
}
