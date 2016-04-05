/**
 * Copyright 2013 Mani Selvaraj
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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ai.eve.volley.AuthFailureError;
import ai.eve.volley.Listener;
import ai.eve.volley.NetworkResponse;
import ai.eve.volley.Request;
import ai.eve.volley.Response;

/**
 * MultipartRequest - To handle the large file uploads. Extended from
 * JSONRequest. You might want to change to StringRequest based on your response
 * type.
 * 
 * @author Mani Selvaraj
 * 
 */
public class MultiPartStringRequest extends Request<String> implements
		MultiPartRequest {

	/* To hold the parameter name and the File to upload */
	private Map<String, File> fileUploads = new HashMap<String, File>();

	/**
	 * Creates a new request with the given method.
	 * 
	 * @param method
	 *            the request {@link Method} to use
	 * @param url
	 *            URL to fetch the string at
	 * @param listener
	 *            Listener to receive the String response
	 * @param errorListener
	 *            Error listener, or null to ignore errors
	 */
	public MultiPartStringRequest(int method, String url,
			Listener<String> listener) {
		super(method, url, listener);
	}

	/**
	 * 要上传的文件
	 */
	public Map<String, File> getFiles() {
		return fileUploads;
	}

	String BOUNDARY = UUID.randomUUID().toString();
	@Override
	public void getBody(HttpURLConnection connection) throws AuthFailureError {
		try {
			String CHARSET = "utf-8"; // 设置编码
			String PREFIX = "--", LINE_END = "\r\n";
			//String CONTENT_TYPE = "multipart/form-data";
			// 边界标识 随机生成
			
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.writeUTF(writeStringParams(BOUNDARY));
			out.flush();
			Map<String, File> map = getFiles();
			Set<String> keySet = map.keySet();
			for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
				String name = it.next();
				File value = map.get(name);
				StringBuilder sb = new StringBuilder();
				/** * 当文件不为空，把文件包装并且上传 */
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				 * filename是文件的名字，包含后缀名的 比如:abc.png
				 */
				sb.append("Content-Disposition: form-data; name=\"" + name
						+ "\"; filename=\"" + value.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				
				out.writeBytes(sb.toString());
				out.flush();
				
				InputStream is = new FileInputStream(value);
				byte[] bytes = new byte[2048];
				int len = 0 ;
	            while ( ( len = is.read ( bytes ) ) != - 1 ){
	            	out.write(bytes,0,len) ;
	            }
	            out.flush();
				is.close();

				out.writeBytes(LINE_END);
			}

			out.writeBytes(PREFIX + BOUNDARY + PREFIX + LINE_END);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 普通字符串数据
	private String writeStringParams(String boundary) throws Exception {
		String PREFIX = "--", LINE_END = "\r\n";
		String CHARSET = "utf-8"; // 设置编码
		Map<String, String> map = getParams();
		Set<String> keySet = map.keySet();
		StringBuilder sb = new StringBuilder();
		StringBuilder sbTmp = new StringBuilder();
		
		
		sb.append(PREFIX);
		sb.append(boundary);
		sb.append(LINE_END);
		sb.append("Content-Disposition: form-data; name=\"volley\"\r\n");
		sb.append("Content-Type: " + "application/octet-stream; charset="
				+ CHARSET + LINE_END);
		sb.append("\r\n");
		sb.append("volley");
		sb.append("\r\n");
		
		
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			String value = map.get(name);
			sbTmp.append(name+"="+value+"&");
			sb.append(PREFIX);
			sb.append(boundary);
			sb.append(LINE_END);
			sb.append("Content-Disposition: form-data; name=\"" + name
					+ "\"\r\n");
			sb.append("Content-Type: " + "application/octet-stream; charset="
					+ CHARSET + LINE_END);
			sb.append("\r\n");
			sb.append(value + "\r\n");
		}
		//TODO ELog.D("入参：", sbTmp.toString());
		return sb.toString();
	}

	// 文件数据
	private String writeFileParams(String boundary) throws Exception {
		Map<String, File> map = getFiles();
		Set<String> keySet = map.keySet();
		StringBuilder sb = new StringBuilder();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			File value = map.get(name);
			sb.append("--" + boundary + "\r\n");
			sb.append("Content-Disposition: form-data; name=\"" + name
					+ "\"; filename=\"" + value.getName() + "\"\r\n");
			sb.append("Content-Type: " + "application/octet-stream" + "\r\n");
			sb.append("\r\n");
			sb.append(getBytes(value));
			sb.append("\r\n");
		}
		return sb.toString();
	}

	// 把文件转换成字节数组
	private String getBytes(File f) throws Exception {
		StringBuilder sb = new StringBuilder();
		FileInputStream in = new FileInputStream(f);
		byte[] b = new byte[8192];
		int n;
		while ((n = in.read(b)) != -1) {
			sb.append(new String(b));
		}
		in.close();

		return sb.toString();
	}

	// 添加结尾数据
	private String paramsEnd(String boundary) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("--" + boundary + "--" + "\r\n");
		sb.append("\r\n");
		return sb.toString();
	}

	public String getBodyContentType() {
		return "multipart/form-data;boundary=" + BOUNDARY;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			parsed = new String(response.data, response.charset);
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		return Response.success(parsed, response);
	}

}