package net.AzureWare.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HwidTools {
	public static boolean AddHwid(List<Cookie> cookies, String Hwid) throws IOException {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		try {
			CookieStore cookieStore = new BasicCookieStore();
			
			httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			
			String url = "https://alphaantileak.cn/debug/bbs/?post-create-1-1.htm";
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
			
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("doctype", "1"));
			formparams.add(new BasicNameValuePair("return_html", "1"));
			formparams.add(new BasicNameValuePair("quotepid", "0"));
			formparams.add(new BasicNameValuePair("message", Hwid));
			
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			
			for (int i = 0; i < cookies.size(); i++) {
				cookieStore.addCookie(cookies.get(i));
			}
			
			response = httpclient.execute(httppost);

			return cookieStore.toString().contains("bbs_token");
		}catch (Exception e) {
			throw e;
		}finally {
			httpclient.close();
			response.close();
		}
	}
	
	public static List<Cookie> Login(String username, String password) throws Throwable {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		try {
			CookieStore cookieStore = new BasicCookieStore();
			
			httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			
			String url = "https://alphaantileak.cn/debug/bbs/?user-login.htm";
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
			
			
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("email", username));
			formparams.add(new BasicNameValuePair("password", password));
		
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			
			List<Cookie> localCookies = null;
			if(entity != null) {
				localCookies = cookieStore.getCookies();
			}
			return localCookies;
		}catch (Exception e) {
			throw e;
		}finally {
			httpclient.close();
			response.close();
		}
	}
	
	public static String sendGet(String url) {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		try {
			CookieStore cookieStore = new BasicCookieStore();
			
			httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
			
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			
			response = httpclient.execute(httppost);
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			return result;
		}catch (Exception e) {
			try {
				throw e;
			} catch (Exception e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}finally {
			try {
				httpclient.close();
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			try {
				response.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String Register(String username, String email, String password, String code) throws IOException {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		try {
			CookieStore cookieStore = new BasicCookieStore();
			
			httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			
			String url = "https://alphaantileak.cn/debug/bbs/?user-create.htm";
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
			
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("email", email));
			formparams.add(new BasicNameValuePair("username", username));
			formparams.add(new BasicNameValuePair("password", password));
			formparams.add(new BasicNameValuePair("invitecode", code));
			
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			
			response = httpclient.execute(httppost);
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			if(result.contains("邮箱格式不正确")) {
				return "邮箱格式不正确";
			}else if(result.contains("邀请码无效")) {
				return "邀请码无效";
			}else if(result.contains("用户注册成功")) {
				return "用户注册成功";
			}else if(result.contains("邮箱已经被注册")) {
				return "邮箱已经被注册";
			}else if(result.contains("用户名已经被注册")) {
				return "用户名已经被注册";
			}else {
				return "原因不明注册失败";
			}
		}catch (Exception e) {
			throw e;
		}finally {
			httpclient.close();
			response.close();
		}
	}
	
}
