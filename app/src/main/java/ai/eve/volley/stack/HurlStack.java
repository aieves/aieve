/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.eve.volley.stack;

import android.text.TextUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import ai.eve.EApplication;
import ai.eve.util.EDevice;
import ai.eve.util.ELog;
import ai.eve.util.ESecurity;
import ai.eve.volley.AuthFailureError;
import ai.eve.volley.Request;
import ai.eve.volley.Request.Method;

/**
 * An {@link HttpStack} based on {@link HttpURLConnection}.
 */
public class HurlStack implements HttpStack {

    private String mUserAgent;
    private final SSLSocketFactory mSslSocketFactory;
    private String mSignInfo;

    public HurlStack(){
    	mUserAgent = "Apache. Google webkit. volley/0/ai/eve";
    	mSslSocketFactory=null;
        mSignInfo = EDevice.GetSingInfo(EApplication.mContext);
    }
    
    public HurlStack(SSLSocketFactory sslSocketFactory){
    	mUserAgent = "Apache. Google webkit. volley/0/ai/eve";
    	mSslSocketFactory=sslSocketFactory;
        mSignInfo = EDevice.GetSingInfo(EApplication.mContext);
    }
    
    /**
     * @param sslSocketFactory SSL factory to use for HTTPS connections
     */
    public HurlStack(String userAgent, SSLSocketFactory sslSocketFactory) {
        mSslSocketFactory = sslSocketFactory;
		mUserAgent = userAgent;
        mSignInfo = EDevice.GetSingInfo(EApplication.mContext);
    }

	public HurlStack(String userAgent) {
		this(userAgent, null);
        mSignInfo = EDevice.GetSingInfo(EApplication.mContext);
	}

    @Override
    public HttpResponse performRequest(Request<?> request) throws IOException, AuthFailureError {
		HashMap<String, String> map = new HashMap<String, String>();
		if (!TextUtils.isEmpty(mUserAgent)) {
			map.put(HTTP.USER_AGENT, mUserAgent);
		}
        if (!TextUtils.isEmpty(mSignInfo)) {
            map.put("SIGN", ESecurity.Encrypt(mSignInfo));
        }
		map.putAll(request.getHeaders());

		URL parsedUrl = new URL(request.getUrl());
		HttpURLConnection connection = openConnection(parsedUrl, request);
		
		for (String headerName : map.keySet()) {
			connection.addRequestProperty(headerName, map.get(headerName));
		}
		if(EApplication.getCookies()!=null){
			ELog.I("请求cookie", EApplication.getCookies().toString());
			connection.addRequestProperty("Cookie", EApplication.getReqCookies());
		}else{
			ELog.I("请求cookie", "null");
		}
		setConnectionParametersForRequest(connection, request);
		int responseCode = connection.getResponseCode();
		if (responseCode == -1) {
			// -1 is returned by getResponseCode() if the response code could not be retrieved.
			// Signal to the caller that something was wrong with the connection.
			throw new IOException("Could not retrieve response code from HttpUrlConnection.");
		}

		StatusLine responseStatus = new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1),
                connection.getResponseCode(), connection.getResponseMessage());
        BasicHttpResponse response = new BasicHttpResponse(responseStatus);
        response.setEntity(entityFromConnection(connection));
        for (Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            if (header.getKey() != null ){
            	if(header.getKey().equalsIgnoreCase("Set-Cookie")) {
            		List<String> cookies = header.getValue();
            		HashMap<String, HttpCookie> cookieMap = new HashMap<String, HttpCookie>();
            		for (String string : cookies) {
            			List<HttpCookie> cookie = HttpCookie.parse(string);
            			for (HttpCookie httpCookie : cookie) {
            				if(httpCookie.getDomain()!=null&&httpCookie.getPath()!=null){
            					cookieMap.put(httpCookie.getName()+httpCookie.getDomain()+httpCookie.getPath(), httpCookie);           			          				
            				}else if(httpCookie.getDomain()==null&&httpCookie.getPath()!=null){
            					cookieMap.put(httpCookie.getName()+httpCookie.getPath(), httpCookie);             					       				
            				}else if(httpCookie.getDomain()!=null&&httpCookie.getPath()==null){
            					cookieMap.put(httpCookie.getName()+httpCookie.getDomain(), httpCookie);    
            				}else{
            					cookieMap.put(httpCookie.getName(), httpCookie);    
            				}
						}
					}
            	
                	EApplication.setCookies(cookieMap);
                	if(EApplication.getCookies()!=null){
                		ELog.I("响应cookie", EApplication.getCookies().toString());
        			}
                }else{
                	Header h = new BasicHeader(header.getKey(),header.getValue().get(0));
                	response.addHeader(h);
                }
            }
        }

        return response;
    }

    /**
     * Initializes an {@link HttpEntity} from the given {@link HttpURLConnection}.
     * @return an HttpEntity populated with data from <code>connection</code>.
     */
    private static HttpEntity entityFromConnection(HttpURLConnection connection) {
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream inputStream;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException ioe) {
            inputStream = connection.getErrorStream();
        }
        entity.setContent(inputStream);
        entity.setContentLength(connection.getContentLength());
        entity.setContentEncoding(connection.getContentEncoding());
        entity.setContentType(connection.getContentType());
        return entity;
    }

    /**
     * Create an {@link HttpURLConnection} for the specified {@code url}.
     */
    private HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    /**
     * Opens an {@link HttpURLConnection} with parameters.
     * @return an open connection
     */
    private HttpURLConnection openConnection(URL url, Request<?> request) throws IOException {
        HttpURLConnection connection = createConnection(url);

        int timeoutMs = request.getTimeoutMs();
        connection.setConnectTimeout(timeoutMs);
        connection.setReadTimeout(timeoutMs);
        connection.setUseCaches(false);
        connection.setDoInput(true);

        // use caller-provided custom SslSocketFactory, if any, for HTTPS
        if ("https".equals(url.getProtocol()) && mSslSocketFactory != null) {
            ((HttpsURLConnection)connection).setSSLSocketFactory(mSslSocketFactory);
        }

        return connection;
    }

    
	private static void setConnectionParametersForRequest(
			HttpURLConnection connection, Request<?> request) throws IOException, AuthFailureError {
        switch (request.getMethod()) {
            case Method.GET:
                // Not necessary to set the request method because connection defaults to GET but
                // being explicit here.
                connection.setRequestMethod("GET");
                break;
            case Method.DELETE:
                connection.setRequestMethod("DELETE");
                break;
            case Method.POST:
                connection.setRequestMethod("POST");
                addBodyIfExists(connection, request);
                break;
            case Method.PUT:
                connection.setRequestMethod("PUT");
                addBodyIfExists(connection, request);
                break;
            case Method.HEAD:
                connection.setRequestMethod("HEAD");
                break;
            case Method.OPTIONS:
                connection.setRequestMethod("OPTIONS");
                break;
            case Method.TRACE:
                connection.setRequestMethod("TRACE");
                break;
            case Method.PATCH:
                addBodyIfExists(connection, request);
                connection.setRequestMethod("PATCH");
                break;
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    private static void addBodyIfExists(
			HttpURLConnection connection, Request<?> request) throws IOException, AuthFailureError {
    	connection.setDoOutput(true);
    	connection.setRequestProperty("connection", "Keep-Alive");
    	connection.addRequestProperty(HTTP.CONTENT_TYPE, request.getBodyContentType());
        request.getBody(connection);
    }
	
}
