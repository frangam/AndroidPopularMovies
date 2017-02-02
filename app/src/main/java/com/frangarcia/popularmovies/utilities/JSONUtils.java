/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frangarcia.popularmovies.utilities;


import org.json.JSONArray;
import org.json.JSONException;


/**
 * A useful class for working with JSON
 */
public class JSONUtils {
    /* *****************************************
     * Public Static Methods
     ******************************************/
    /**
     * Constructs an Integer array from a JSONArray parameter
     * @param jsonArray the JSONArray
     * @return the integer array
     */
    public static Integer[] getIntegerArray(JSONArray jsonArray){
        Integer[] res = new Integer[jsonArray.length()];

        try {
            for(int i=0; i<jsonArray.length(); i++){
                res[i] = jsonArray.getInt(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }
}
