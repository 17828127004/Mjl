package util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SaxAsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

public class KxhlRestClient {
	private static final String BASE_URL ="http://www.cdjiayibai.com/home";
//	private static final String BASE_URL = "http://hbox.honyork.com/api/";
	// private static final String BASE_URL =
	// "http://yujianhezhi-1.wx.jaeapp.com/api/";
	// private static final String BASE_URL = "http://hbox.wx.jaeapp.com/api/";

	private static AsyncHttpClient client = new AsyncHttpClient(
			Config.getSchemeRegistry());
	static {
		//11000
		client.setTimeout(30000);// 设置链接超时，如果不设置，默认为10s
	}

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}
	/**
	 * --common
	 * 
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void post(String url, RequestParams params,

	AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}
	public static void post(String url, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler){
		client.post(getAbsoluteUrl(url), params, jsonHttpResponseHandler);
	}

	/**
	 * --more
	 * 
	 * @param url
	 * @param params
	 * @param SessionCode
	 * @param responseHandler
	 */
	public static void post(String url, RequestParams params,
			String SessionCode, AsyncHttpResponseHandler responseHandler) {
		client.addHeader("Cookie", SessionCode);
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	/**
	 * --less
	 * 
	 * @param url
	 * @param params
	 * @param cookieStore
	 * @param responseHandler
	 */
	public static void post(String url, RequestParams params,
			PersistentCookieStore cookieStore,
			AsyncHttpResponseHandler responseHandler) {
		cookieStore.clear();
		client.setCookieStore(cookieStore);
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}
