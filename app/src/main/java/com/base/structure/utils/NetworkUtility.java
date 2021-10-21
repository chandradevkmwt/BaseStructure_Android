package com.base.structure.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtility {
    public static final class Builder {

        private String BASE_URL = "";
        private long connectionTimeout;
        private long readTimeout;
        private long writeTimeout;
        private boolean shouldRetryOnConnectionFailure = false;
        private Retrofit mRetrofit;
        private String token = "";


        public Builder(String baseURL) {
            //BASE_URL = baseURL;
            BASE_URL = baseURL;
        }

        /**
         * Sets the default connect timeout in Seconds for new connections.
         */
        public Builder withConnectionTimeout(long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        /**
         * Sets the default read timeout in Minutes for new connections.
         */
        public Builder withReadTimeout(long readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        /**
         * Sets the default write timeout in Minutes for new connections.
         */
        public Builder withWriteTimeout(long writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public Builder withAccesToken(String accesToken) {
            this.token = accesToken;
            return this;
        }

        /**
         * Configures the Retrofit client to retry or not when a connectivity problem is encountered.
         */
        public Builder withShouldRetryOnConnectionFailure(boolean shouldRetryOnConnectionFailure) {
            this.shouldRetryOnConnectionFailure = shouldRetryOnConnectionFailure;
            return this;
        }

        /**
         * Create the Retrofit instance using the configured values.
         */
        public NetworkUtility build() {
            setupForAPI();
            return new NetworkUtility();
        }

        public Retrofit getRetrofit() {
            return mRetrofit;
        }

        private void setupForAPI() {

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            builder.connectTimeout(connectionTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout, TimeUnit.MINUTES)
                    .writeTimeout(writeTimeout, TimeUnit.MINUTES)
                    .retryOnConnectionFailure(shouldRetryOnConnectionFailure)
                    .addInterceptor(httpLoggingInterceptor);
//                    .addInterceptor(new CustomInterceptor(StaticData.HEADER_LANGUAGE, StaticData.HEADER_APP_VERSION));
                   /* .addInterceptor(new AliAfriqInterceptor(){
                        @Override
                        public Response intercept(Interceptor.Chain chain) throws IOException {

                            *//*String accessToken = PreferenceUtils.getToken(LoctagApp.getAppContext());
                            Request request = chain.request();
                            //alternative


                            request = request.newBuilder()
                                    //.headers(moreHeaders)
                                    .addHeader("Authorization", accessToken)
                                    .build();

                            Log.e("Authorization : ", accessToken);*//*
                          //  Response response = chain.proceed(request);
                            return null;
                        }
                    });
*/

            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

        }

    }
}
