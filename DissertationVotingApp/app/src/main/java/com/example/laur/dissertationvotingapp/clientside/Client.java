package com.example.laur.dissertationvotingapp.clientside;


import android.util.Base64;

import com.example.laur.dissertationvotingapp.activity.MainActivity;
import com.example.laur.dissertationvotingapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.net.ssl.HostnameVerifier;

public class Client {
    public static final int HTTP_TIMEOUT = 6000;
    private static HttpClient httpClient;
    private static String resp = "";

    private static HttpClient getHttpClient() {
        if(httpClient == null) {
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setContentCharset(params, "UTF-8");
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            params.setBooleanParameter("http.protocol.expect-continue", false);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            //registry.register(new Scheme("https", getSslSocketFactory(), 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            httpClient = new DefaultHttpClient(ccm, params);
        }
        return httpClient;
    }

    private static SSLSocketFactory getSslSocketFactory() {
        SSLSocketFactory socketFactory = null;
        try {
            InputStream in = MainActivity.getAppContext().getResources().openRawResource(R.raw.keystore);
            KeyStore keyStore = KeyStore.getInstance("BKS");
            socketFactory = new SSLSocketFactory(keyStore);
            try {
                keyStore.load(in, "123456".toCharArray());
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                in.close();
            }

            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return socketFactory;
    }

    public static String executeHttpPost(String url, ArrayList<NameValuePair> postParams) {
        BufferedReader in = null;
        String result = "default";
        try {
            HttpClient httpClient = getHttpClient();
            HttpPost req = new HttpPost(url);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParams);
            req.setEntity(entity);
            HttpResponse response = httpClient.execute(req);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
           // String separator = "###";
            while((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            result = sb.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public static String executeFakeRegisterHttpGet(String url, ArrayList<NameValuePair> params) {
        BufferedReader in = null;
        String result = "default";
        try {
            HttpClient httpClient = getHttpClient();
            String compositeUrl = addLocationToUrl(url, params);
            //String compositeUrl = url + params.get(0).getValue() + "/" + params.get(1).getValue() + "/" + params.get(2).getValue() + "/";
            HttpGet httpGet = new HttpGet(compositeUrl);
            HttpResponse response = httpClient.execute(httpGet);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            //String separator = "###";
            while((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            result = sb.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public static String executeRegisterHttpPost(String url, ArrayList<NameValuePair> params) {
        BufferedReader in = null;
        String result = "default";
        try {
            /*//HttpClient httpClient = getHttpClient();
            //String compositeUrl = addLocationToUrl(url, params);
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            //httpPost.setHeader("Content-Type", "multipart/form-data");
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            //multipartEntityBuilder.setContentType(ContentType.create("Content-Type","multipart/form-data"));
            multipartEntityBuilder.addTextBody("cnp", "123");
            //multipartEntityBuilder.addTextBody("img", new InputStreamBody(new ByteArrayInputStream(data), "img"));
            //multipartEntity.addPart("imei", new StringBody("123"));
            *//*UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);*//*
            HttpEntity entity = multipartEntityBuilder.build();
            httpPost.setEntity(entity);*/

            /*String encodedString = Base64.encodeToString(data, 0);
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params1 = new RequestParams();
            params1.put("img", encodedString);
            client.post(url, params1, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    resp = response;
                }
            });
*/
           /* String encodedString = Base64.encodeToString(data, 0);
            HttpClient client2 = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            HttpParams params2 = new BasicHttpParams();
            params2.setParameter("img", encodedString);
            params2.setParameter("cnp", "123");
            params2.setParameter("imei", "123");
            HttpEntity entity = new BasicHttpEntity();
            post.setParams(params2);
            HttpResponse response = httpClient.execute(post);*/


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost req = new HttpPost(url);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
            req.setEntity(entity);
            HttpResponse response = httpClient.execute(req);


            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            //String separator = "###";
            while((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            result = sb.toString();
            //result = resp;
        /*} catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();*/
        } catch(Exception e) {
          e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public static String executeHttpGet(String url) {
        BufferedReader in = null;
        String result = "default";
        try {
            HttpClient httpClient = getHttpClient();
            HttpGet req = new HttpGet(url);
            req.setURI(new URI(url));
            HttpResponse response = httpClient.execute(req);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String separator = "###";
            while((line = in.readLine()) != null) {
                sb.append(line + separator);
            }
            in.close();
            result = sb.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public static String addLocationToUrl(String url, ArrayList<NameValuePair> params){
        if(!url.endsWith("?"))
            url += "?";
        String paramString = URLEncodedUtils.format(params, "utf-8");
        url += paramString;
        return url;
    }
}
