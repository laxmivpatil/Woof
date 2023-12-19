package com.example.techverse;

 
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.verify.v2.Service;
import com.twilio.type.PhoneNumber;

public class SmsSender {
     public static final String ACCOUNT_SID ="AC5f95b80c20f80833cbdb2faae5debc59";
    public static final String AUTH_TOKEN = "94670d22736dd3a91a42d74d74b7dee9";
    public static final String TWILIO_NUMBER="+12139854075";	
   
    public static void  smsSent(String mobileNo,String Otp )
    {
    	Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
               new PhoneNumber(mobileNo),
               new PhoneNumber(TWILIO_NUMBER),
               "Your OTP is "+Otp)
               .create();
        
    }
     
}