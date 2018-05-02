package com.jeeplus.common.utils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.security.Digests;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author Administrator
 */
public class ShortMsgUtils {
    public static void main(String[] args) {
        ShortMsgUtils.sendShotMsg("13430378456","验证码：110");
    } 
    
    public static String sendShotMsg(String mobile,String msg){
        String p = "0";
        String t = "0";
    	 String httpResponse =  send(mobile, msg+Global.getConfig("SHOP_MSG_SIGN"));
         try {
            JSONObject jsonObj = new JSONObject( httpResponse );
            int error_code = jsonObj.getInt("error");
            String error_msg = jsonObj.getString("msg");
            if(error_code==0){
                System.out.println("Send message success.");
                p = "1";
            }else{
                System.out.println("Send message failed,code is "+error_code+",msg is "+error_msg);
            }
        } catch (JSONException ex) {
            Logger.getLogger(ShortMsgUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        httpResponse =  testStatus();
        try {
            JSONObject jsonObj = new JSONObject( httpResponse );
            int error_code = jsonObj.getInt("error");
            if( error_code == 0 ){
                int deposit = jsonObj.getInt("deposit");
                System.out.println("Fetch deposit success :"+deposit);
                t = "1";
            }else{
                String error_msg = jsonObj.getString("msg");
                System.out.println("Fetch deposit failed,code is "+error_code+",msg is "+error_msg);
            }
        } catch (JSONException ex) {
            Logger.getLogger(ShortMsgUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p+t;

    }
    
    

    private static String send(String mobile,String msg){
        // just replace key here
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(
            "api",Global.getConfig("SHOP_MSG_API")));
        WebResource webResource = client.resource(
            "http://sms-api.luosimao.com/v1/send.json");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("mobile", mobile);//15986341004
        formData.add("message", msg);
        ClientResponse response =  webResource.type( MediaType.APPLICATION_FORM_URLENCODED ).
        post(ClientResponse.class, formData);
        String textEntity = response.getEntity(String.class);
        int status = response.getStatus();
        //System.out.print(textEntity);
        //System.out.print(status);
        return textEntity;
    }

    private static String testStatus(){
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(
            "api",Global.getConfig("SHOP_MSG_API")));
        WebResource webResource = client.resource( "http://sms-api.luosimao.com/v1/status.json" );
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        ClientResponse response =  webResource.get( ClientResponse.class );
        String textEntity = response.getEntity(String.class);
        int status = response.getStatus();
        //System.out.print(status);
        //System.out.print(textEntity);
        return textEntity;
    }
}