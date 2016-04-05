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

package ai.eve.volley.request;

import org.apache.http.protocol.HTTP;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import ai.eve.volley.Listener;
import ai.eve.volley.NetroidLog;
import ai.eve.volley.NetworkResponse;
import ai.eve.volley.Request;
import ai.eve.volley.Response;

/**
 * A request for retrieving a T type response body at a given URL that also
 * optionally sends along a JSON body in the request specified.
 * 
 * @param <T>
 *            JSON type of response expected
 */
public abstract class JsonRequest<T> extends Request<T> {
	/** Charset for request. */
	private static final String PROTOCOL_CHARSET = "utf-8";

	/** Content type for request. */
	private static final String PROTOCOL_CONTENT_TYPE = String.format(
			"application/json; charset=%s", PROTOCOL_CHARSET);

	private final String mRequestBody;

	public JsonRequest(int method, String url, String requestBody,
			Listener<T> listener) {
		super(method, url, listener);
		mRequestBody = requestBody;
	}

	@Override
	abstract protected Response<T> parseNetworkResponse(NetworkResponse response);

	@Override
	public String getBodyContentType() {
		return PROTOCOL_CONTENT_TYPE;
	}

	@Override
	public void getBody(HttpURLConnection connection) {
		try {
			if (mRequestBody != null) {
				connection.setDoOutput(true);
				connection.addRequestProperty(HTTP.CONTENT_TYPE,
						getBodyContentType());
				DataOutputStream out = new DataOutputStream(
						connection.getOutputStream());
				out.write(mRequestBody.getBytes(PROTOCOL_CHARSET));
				out.close();
			}
		} catch (UnsupportedEncodingException uee) {
			NetroidLog
					.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
							mRequestBody, PROTOCOL_CHARSET);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
