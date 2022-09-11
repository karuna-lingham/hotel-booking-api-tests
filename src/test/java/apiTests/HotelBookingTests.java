package apiTests;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HotelBookingTests {

    private String baseUrl;
    private int bookingId;

    @BeforeEach
    public void setup() {
        baseUrl = "https://restful-booker.herokuapp.com";
        bookingId = 6509;
    }
     
    	@Test
    	@Order(1)
    	public void GetBookingById() {
    		// TODO Auto-generated method stub
            
            RestAssured.baseURI = baseUrl;
            String firstName = "K";
            String lastName = "Test1";
            int totalPrice = 90;
            boolean depositPaid = true;
             
            RequestSpecification request = RestAssured.given();

            Response response = request.get("/booking/"+bookingId);
            
            String jsonString = response.asString();
            System.out.println(jsonString);

            Assertions.assertEquals(response.getStatusCode(), 200);
            
            jsonString = response.asString();
            String fname = JsonPath.from(jsonString).get("firstname");
            String lname = JsonPath.from(jsonString).get("lastname");
            int tprice = JsonPath.from(jsonString).get("totalprice");
            boolean deposit = JsonPath.from(jsonString).get("depositpaid");
            

            Assertions.assertEquals(fname,firstName);
            Assertions.assertEquals(lname,lastName);
            Assertions.assertEquals(tprice,totalPrice);
            Assertions.assertEquals(deposit,depositPaid);
    	}

    	@Test
    	@Order(2)
    	public void GetBookingsWithNameFilter() {
    		// TODO Auto-generated method stub
            
            RestAssured.baseURI = baseUrl;
            String firstName = "K";
            String lastName = "Test1";
             
            RequestSpecification request = RestAssured.given();

            Response response = request.queryParam("firstname",firstName)
            		.and().queryParam("lastname",lastName)
            		.get("/booking");
            
            String jsonString = response.asString();
            System.out.println(jsonString);

            Assertions.assertEquals(response.getStatusCode(), 200);
            
            jsonString = response.asString();
            int id = JsonPath.from(jsonString).get("bookingid[0]");
            
            Assertions.assertEquals(bookingId,id);
    	}

    	@Test
    	@Order(3)
    	public void GetBookingsWithDateFilter() {
    		// TODO Auto-generated method stub
            
            RestAssured.baseURI = baseUrl;
            String checkin = "2022-09-09";
            String checkout = "2022-09-16";            
             
            RequestSpecification request = RestAssured.given();

            Response response = request.queryParam("checkin",checkin)
            		.and().queryParam("checkout",checkout)
            		.get("/booking");
            
            String jsonString = response.asString();
            System.out.println(jsonString);

            Assertions.assertEquals(response.getStatusCode(), 200);
            
            jsonString = response.asString();
            List<Map<String, String>> bookings = JsonPath.from(jsonString).get("bookings");
            
            System.out.println(bookings.size());
            Assertions.assertTrue(bookings.size() > 0);
            
    	}

    	@Test
    	@Order(4)
    	public void PartialUpdateBooking() {
    		// TODO Auto-generated method stub
            
            RestAssured.baseURI = baseUrl;
            String firstName = "K-auto";
            String lastName = "Test-auto1";
            int totalPrice = 90;
            boolean depositPaid = true;
             
            RequestSpecification request = RestAssured.given();

            Response response = request.auth()
            		.basic("admin", "password123")
            		.header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
            		.body("{\n"
            		+ "    \"firstname\": "+firstName+",\n"
            		+ "    \"lastname\": "+lastName+"\n"
            		+ "}")
            		.patch("/booking/"+bookingId);
            
            String jsonString = response.asString();
            System.out.println(jsonString);

            Assertions.assertEquals(response.getStatusCode(), 200);
            
            jsonString = response.asString();
            String fname = JsonPath.from(jsonString).get("firstname");
            String lname = JsonPath.from(jsonString).get("lastname");
            int tprice = JsonPath.from(jsonString).get("totalprice");
            boolean deposit = JsonPath.from(jsonString).get("depositpaid");
            

            Assertions.assertEquals(fname,firstName);
            Assertions.assertEquals(lname,lastName);
            Assertions.assertEquals(tprice,totalPrice);
            Assertions.assertEquals(deposit,depositPaid);
    	}

    	@Test
    	@Order(5)
    	public void DeleteBooking() {
    		// TODO Auto-generated method stub

    		RestAssured.baseURI = baseUrl;
            RequestSpecification request = RestAssured.given();

            Response response = request.auth()
            		.basic("admin", "password123")
            		.header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
            		.delete("/booking/"+bookingId);
            
            Assertions.assertEquals(response.getStatusCode(), 201);

            
            response = request.get("/booking/"+bookingId);
            
            Assertions.assertEquals(response.getStatusCode(), 200);

            String jsonString = response.asString();
            System.out.println(jsonString);
            List<Map<String, String>> bookings = JsonPath.from(jsonString).get("bookings");
            
            System.out.println(bookings.size());
            
            //Check size of bookings object should be 0 for that ID
            Assertions.assertTrue(bookings.size() == 0);            
            
    	}

}