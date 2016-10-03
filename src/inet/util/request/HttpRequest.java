/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.util.request;

import inet.util.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

/**
 * Wrap http request
 *
 * @author iadd
 *
 */
public class HttpRequest implements IRequest {

    private String url;
    private String method;
    private List<SimpleEntry<String, Object>> headers;
    private List<SimpleEntry<String, Object>> params;
    private boolean usingCookie = false;

    private int timeout;

    private String toParams(List<SimpleEntry<String, Object>> p) {
        if (p.isEmpty()) {
            return "empty";
        }
        StringBuilder sb = new StringBuilder();
        for (SimpleEntry<String, Object> obj : p) {
            sb.append(obj.getKey());
            sb.append("=");
            sb.append(obj.getValue());
            sb.append("&");
        }
        if ((sb.charAt(sb.length() - 1)) == '&') {
            sb.deleteCharAt(sb.length() - 1);
        }
        String str = sb.toString();
        return str;
    }

    private Method getCurrentMethod() {
        if (getMethod().equalsIgnoreCase("GET")) {
            return Method.GET;
        } else if (getMethod().equalsIgnoreCase("POST")) {
            return Method.POST;
        }
        return null;
    }

    public HttpRequest() {
        if (getHeaders() == null) {
            this.headers = new java.util.ArrayList<SimpleEntry<String, Object>>();
        }
        if (getParams() == null) {
            this.params = new java.util.ArrayList<SimpleEntry<String, Object>>();
        }
        this.timeout = 5000;
        setMethod(Method.GET);
    }

    public HttpRequest(String url) {
        this();
        setUrl(url);
    }

    public boolean isUsingCookie() {
        return usingCookie;
    }

    public void setUsingCookie(boolean usingCookie) {
        this.usingCookie = usingCookie;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public final void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public void setMethod(String method) {
        this.method = method.toUpperCase();
    }

    @Override
    public final void setMethod(Method method) {
        if (method == Method.POST) {
            setMethod("POST");
        } else if (method == Method.GET) {
            setMethod("GET");
        }
    }

    @Override
    public final List<SimpleEntry<String, Object>> getHeaders() {
        return this.headers;
    }

    @Override
    public void addHeader(String key, Object value) {
        this.addHeader(new SimpleEntry<String, Object>(key, value));
    }

    @Override
    public void addHeader(SimpleEntry<String, Object> header) {
        this.headers.add(header);
    }

    @Override
    public void addHeader(List<SimpleEntry<String, Object>> headers) {
        this.headers = headers;
    }

    @Override
    public final List<SimpleEntry<String, Object>> getParams() {
        return this.params;
    }

    @Override
    public void addParam(String key, Object value) {
        this.params.add(new SimpleEntry<String, Object>(key, value));
    }

    @Override
    public CookieManager getCookies() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public void send(final Listener listener) {
        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result = send();
                if (listener != null) {
                    listener.onResponse(result);
                }
            }
        });
        sendThread.start();
    }

    @Override
    public String send() {
        URL url;
        URLConnection conn;
        String result = null;

        if (isUsingCookie()) {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        } else {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_NONE));
        }

        try {
            String strUrl = null;
            if (getCurrentMethod() == Method.POST) {
                strUrl = getUrl();
            } else if (getCurrentMethod() == Method.GET) {
                strUrl = getUrl() + "?" + toParams(getParams());
            }
            url = new URL(strUrl);
            conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(getTimeout());
            if (getHeaders() != null && !getHeaders().isEmpty()) {
                for (SimpleEntry<String, Object> p : getHeaders()) {
                    conn.setRequestProperty(p.getKey(), p.getValue() + "");
                }
            }
            if (url.getProtocol().equalsIgnoreCase("https")) {
                ((HttpsURLConnection) conn).setRequestMethod(getMethod());
            } else if (url.getProtocol().equalsIgnoreCase("http")) {
                ((HttpURLConnection) conn).setRequestMethod(getMethod());
            }
            if (getCurrentMethod() == Method.GET) {

            } else if (getCurrentMethod() == Method.POST) {
                conn.setDoInput(true);
                // boolean isContain = false;
                /*
				 * Do not set content-type because it is automatic for
				 * (SimpleEntry<String, Object> header : getHeaders()) { if
				 * (!header.getKey().equalsIgnoreCase("Content-type")) {
				 * continue; } isContain = true; break; }
				 * 
				 * if (!isContain) { //addHeader("Content-type",
				 * "application/x-www-form-urlencoded"); }
                 */
                String params = toParams(getParams());
                // addHeader("Content-Length",
                // params.getBytes().length);
                if (!getParams().isEmpty()) {
                    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                    writer.write(params);
                    writer.flush();
                }
            }

            int responseCode = 0;
            String line;
            if (url.getProtocol().equalsIgnoreCase("https")) {
                responseCode = ((HttpsURLConnection) conn).getResponseCode();
            } else if (url.getProtocol().equalsIgnoreCase("http")) {
                responseCode = ((HttpURLConnection) conn).getResponseCode();
            }
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                if (url.getProtocol().equalsIgnoreCase("https")) {
                    ((HttpsURLConnection) conn).disconnect();
                } else if (url.getProtocol().equalsIgnoreCase("http")) {
                    ((HttpURLConnection) conn).disconnect();
                }
                result = response.toString();
            }

        } catch (MalformedURLException ex) {
            result = ex.getMessage();
            Logger.log(ex);
        } catch (IOException ex) {
            result = ex.getMessage();
            Logger.log(ex);
        }
        return result;
    }

}
