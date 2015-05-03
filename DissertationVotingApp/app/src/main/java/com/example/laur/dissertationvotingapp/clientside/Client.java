package com.example.laur.dissertationvotingapp.clientside;


import com.example.laur.dissertationvotingapp.activity.MainActivity;
import com.example.laur.dissertationvotingapp.R;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
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

import javax.net.ssl.HostnameVerifier;

public class Client {
    public static final int HTTP_TIMEOUT = 6000;
    private static HttpClient httpClient;

    private static HttpClient getHttpClient() {
        if(httpClient == null) {
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setContentCharset(params, "UTF-8");
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            params.setBooleanParameter("http.protocol.expect-continue", false);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", getSslSocketFactory(), 443));
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
}
