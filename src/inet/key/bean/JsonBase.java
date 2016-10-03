/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.key.bean;

import com.google.gson.reflect.TypeToken;
import inet.util.JsonUtil;
import java.lang.reflect.Type;

/**
 *
 * @author dainv
 */
public abstract class JsonBase<T> {

    public T parseJson(String json) {
        Type type = new TypeToken<T>() {
        }.getType();
        T obj;
        obj = JsonUtil.fromJson(json, type);
        return obj;
    }

    public String toJson() {
        String str;
        str = JsonUtil.toJson(this);
        return str;
    }
}
