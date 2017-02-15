package ai.eve.http.okHttp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import ai.eve.volley.stack.HurlStack;
import okhttp3.OkHttpClient;
import okhttp3.OkUrlFactory;

/**
 * Created by wyong on 2016/6/28.
 */
public class OKHttpStack extends HurlStack {
    private OkHttpClient okHttpClient;
    public OKHttpStack(){
        this(new OkHttpClient());
    }
    public OKHttpStack(OkHttpClient okHttpClient){
        this.okHttpClient = okHttpClient;
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        OkUrlFactory okUrlFactory = new OkUrlFactory(okHttpClient);
        return okUrlFactory.open(url);
    }
}
