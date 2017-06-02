/*
 *
 *  * Copyright 2015 Soo [154014022@qq.com | sootracker@gmail.com]
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package soo.swallow.log.box;

import soo.swallow.log.Level;
import soo.swallow.log.Loggable;

/**
 * Created by Joseph.yan
 */
public class DefaultLoggable implements Loggable {

    @Override
    public boolean forClass(Class<?> cls) {
        return true;
    }

    @Override
    public boolean forTag(String tag) {
        return true;
    }

    @Override
    public boolean forLevel(Level level) {
        return true;
    }

    @Override
    public boolean forThread(Thread thread) {
        return true;
    }
}
