package com.sgf.download;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Application;

public class MyApplication extends Application {

	private HttpClient httpClient;
	public void onCreate(){
		super.onCreate();
		httpClient=this.createHttpClient();
	}
	public void onLowMemory(){
		super.onLowMemory();
		this.shutdownHttpClient();
	}
	public void onTerminate(){
		super.onTerminate();
		this.shutdownHttpClient();
	}
	public HttpClient createHttpClient(){
		HttpParams params=new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_PROTOCOL_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		SchemeRegistry schReg=new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", PlainSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager connMgr=new ThreadSafeClientConnManager(params, schReg);
		return new DefaultHttpClient(connMgr,params);
	}
	public void shutdownHttpClient(){
		if(httpClient!=null&&httpClient.getConnectionManager()!=null){
			httpClient.getConnectionManager().shutdown();
		}
	}
	public HttpClient getHttpClient(){
		return httpClient;
	}
}
