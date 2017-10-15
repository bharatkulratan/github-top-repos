package info.techienotes.toprepos.networking;

//import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {
    private static final String API_BASE_URL = API.SERVER_URL;
    private static final int HTTP_REQUEST_TIMEOUT = 60;

    private static OkHttpClient buildHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.connectTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.SECONDS);
        httpClient.readTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.SECONDS);
        httpClient.writeTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.SECONDS);


        HttpLoggingInterceptor interceptor  = new HttpLoggingInterceptor();

        /**
         * OkHttp’s logging interceptor has four log levels: NONE, BASIC, HEADERS, BODY
         * NONE     : No logging.
         * BASIC    : Log request type, url, size of request body, response status and size of response body.
         * HEADERS  : Log request and response headers, request type, url, response status.
         * BODY     : Log request and response headers and body.
         */

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.networkInterceptors().add(interceptor);
        // below line could be uncommented for debug app
        // httpClient.networkInterceptors().add(new StethoInterceptor());
        return httpClient.build();
    }


    //--  https://sites.google.com/site/gson/gson-user-guide
    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .setPrettyPrinting()
            .create();

    private static final Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(buildHttpClient()).build();
        return retrofit.create(serviceClass);
    }
}