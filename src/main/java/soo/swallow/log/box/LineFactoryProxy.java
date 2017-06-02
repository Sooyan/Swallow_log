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

import java.util.Map;

import soo.swallow.log.LineFactory;

/**
 * Created by Joseph.yan
 */
public class LineFactoryProxy implements LineFactory<Object> {

    private static final String NULL_OBJECT = "<null>";

    private final Map<Class<?>, LineFactory> delegateMap;

    public LineFactoryProxy(Map<Class<?>, LineFactory> delegateMap) {
        this.delegateMap = delegateMap;
    }

    @Override
    public String[] getLines(Object content) {
        String[] lines;
        if (content == null) {
            lines = new String[]{NULL_OBJECT};
        } else {
            LineFactory lineFactory = delegateMap.get(content.getClass());
            if (lineFactory == null) {
                lines = new String[]{content.toString()};
            } else {
                lines = lineFactory.getLines(content);
                if (lines == null) {
                    throw new NullPointerException("Lines must not be null, return from "
                            + lineFactory.getClass().getSimpleName());
                }
            }
        }
        return lines;
    }
}
