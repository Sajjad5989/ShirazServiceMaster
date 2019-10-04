package ir.shirazservice.expert.webservice.shirazserviceapi;

public interface ShirazServiceApi {

    //در ذخیره ی در شرپریفرنس هم از این اسامی استفاده کردم
    String USER_ID = "User-Id";
    String USER_NAME = "User-Name";
    String USER_PASS = "User-Pass";
    String ACCESS_TOKEN = "Access-Token";
    String PAYMENT_MERCHANT = "e6846e64-c7e8-11e7-863c-000c295eb8fc";
    String PAYMENT_URL =  "return://zarrinpalpayment";

    //***************************************


    // آدرس یو آرال بیس شیراز سرویس
    //  String BASE_URL = "https://shiraz-service.ir/api/general/8.1.3/";
    String BASE_URL = "http://shz-service.ir/api/workman/6.1.5/";

    // نوع ارسال و دریافت
    String CONTENT_TYPE = "Content-Type: application/json";

    // نوع دستگاه
    //1 => Android Global, 2 => IOS Global, 3 => Site, 4 => Android ServiceMan, 5 => IOS ServiceMan
    String DEVICE_TYPE = "Device-Type:4";  long CONNECTION_TIMEOUT = 45;
    long WRITE_TIMEOUT = 45;
    long READ_TIMEOUT = 45;



}



// زمان های تایم آوت
