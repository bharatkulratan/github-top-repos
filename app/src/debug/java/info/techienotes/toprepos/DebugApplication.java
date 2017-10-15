package info.techienotes.toprepos;

import android.content.Context;

import com.facebook.stetho.Stetho;



public class DebugApplication extends GithubApp {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    /*
    @NonNull
    public OkHttpClient jsonRequestClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .addNetworkInterceptor(new HttpLoggingInterceptor())
                .readTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    @Override
    @NonNull
    public OkHttpClient multipartRequestClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .addNetworkInterceptor(new HttpLoggingInterceptor())
                .readTimeout(Constants.MULTIPART_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }
    */
}
