package com.howtoprogram.okhttp.cache;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CacheOkHttp {

	private final OkHttpClient client;

	public CacheOkHttp(File cacheDirectory) throws Exception {
		int cacheSize = 10 * 1024 * 1024; // 10 MiB
		Cache cache = new Cache(cacheDirectory, cacheSize);
		client = new OkHttpClient.Builder().cache(cache).build();
	}

	public static void main(String args[]) throws Exception {

		File cacheLoc = new File("D:\\tmp\\okhttp-cache");
		Assert.assertTrue(cacheLoc.exists());
		CacheOkHttp cacheOkHttp = new CacheOkHttp(cacheLoc);
		cacheOkHttp.run();
	}

	public void run() throws Exception {
		Request request = new Request.Builder().url("http://httpbin.org/cache/10").build();

		Response response1 = client.newCall(request).execute();
		if (!response1.isSuccessful()) {
			throw new IOException("Unexpected code " + response1);
		}

		String response1Body = response1.body().string();
		System.out.println("Response 1 response:          " + response1);
		System.out.println("Response 1 cache response:    " + response1.cacheResponse());
		System.out.println("Response 1 network response:  " + response1.networkResponse());

		Response response2 = client.newCall(request).execute();
		if (!response2.isSuccessful()) {
			throw new IOException("Unexpected code " + response2);
		}

		String response2Body = response2.body().string();
		System.out.println("Response 2 response:          " + response2);
		System.out.println("Response 2 cache response:    " + response2.cacheResponse());
		System.out.println("Response 2 network response:  " + response2.networkResponse());
		System.out.println("Response 2 equals Response 1? " + response1Body.equals(response2Body));
		
		Thread.sleep(11000);

		Response response3 = client.newCall(request).execute();
		if (!response3.isSuccessful()) {
			throw new IOException("Unexpected code " + response3);
		}
		System.out.println(response3.cacheControl().maxStaleSeconds());

		System.out.println("Response 3 response:          " + response3);
		System.out.println("response 3 cache response:    " + response3.cacheResponse());
		System.out.println("Response 3 network response:  " + response3.networkResponse());

	}
}
