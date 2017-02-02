/*
 * Copyright (C) 2016 Francisco Manuel Garcia Moreno
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


/**
 * A useful approach for making the code reusable and structural
 * @param <T> The class of the result
 */
public interface AsyncTaskCompleteListener<T> {
    /**
     * Invoked before the execution of the task
     */
    public void OnTaskPreRun();

    /**
     * Invoked after AsyncTask execution has been completed
     * @param result the resulting object
     */
    public void onTaskComplete(T result);
}
