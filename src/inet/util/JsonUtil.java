/*
 * Copyright (C) 2015 dainv@inet.vn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package inet.util;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author dainv@inet.vn
 */
public final class JsonUtil {

    private static boolean isSerializeNulls = false;

    private static final GsonBuilder gb = new GsonBuilder();
    private static Gson gson;

    public static void setIsSerializeNulls(boolean value) {
        isSerializeNulls = value;
    }

    public static Gson getGson() {
        if (isSerializeNulls) {
            gb.serializeNulls();
        }
        gson = gb.create();
        return gson;
    }

    /**
     * Convert list object to json
     *
     * @param <T>
     * @param lst
     * @return
     */
    public static final <T> String toJson(List<T> lst) {
        String str = null;
        try {
            str = getGson().toJson(lst);
        } catch (Exception ex) {
            Logger.log(ex);
        }
        return str;
    }

    /**
     * Convert object to json
     *
     * @param obj
     * @return
     */
    public static final <T> String toJson(T obj) {
        String str = null;
        try {
            str = getGson().toJson(obj);
        } catch (Exception ex) {
            Logger.log(ex);
        }
        return str;
    }

    /**
     * Parse json string to object Type type = new TypeToken<MatchObject
     * <MatchInfo>>() {}.getType();
     *
     * @param json
     * @param type
     * @return
     */
    public static final <T> T fromJson(String json, Type type) {
        T obj = null;
        try {
            obj = getGson().fromJson(json, type);
        } catch (Exception ex) {
            Logger.log(ex);
        }
        return obj;
    }

}
