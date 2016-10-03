/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.util.request;

import java.net.CookieManager;
import java.util.AbstractMap.SimpleEntry;

/**
 *
 * @author dainv
 */
public interface IRequest {

    /**
     * List of method
     *
     * @author iadd
     *
     */
    public static enum Method {
        GET, POST
    }

    public static interface Listener {

        void onResponse(String response);
    }

    /**
     * Url of request
     *
     * @return
     */
    public String getUrl();

    public void setUrl(String url);

    public String getMethod();

    public void setMethod(String method);

    public void setMethod(Method method);

    /**
     * Get list headers
     *
     * @return
     */
    public java.util.List<SimpleEntry<String, Object>> getHeaders();

    public void addHeader(String key, Object value);

    public void addHeader(SimpleEntry<String, Object> header);

    public void addHeader(java.util.List<SimpleEntry<String, Object>> headers);

    /**
     * Get list params
     *
     * @return
     */
    public java.util.List<SimpleEntry<String, Object>> getParams();

    public void addParam(String key, Object value);

    public CookieManager getCookies();

    /**
     * Send asynchronous
     *
     * @param listener
     */
    public void send(final Listener listener);

    /**
     * Send synchronous
     *
     * @return
     */
    public String send();
}
