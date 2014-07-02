/*
 * Hero.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
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
package net.sarangnamu.diablo.api.json;

import net.sarangnamu.diablo.api.json.hero.Items;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Hero {
    @JsonProperty
    public int id;

    @JsonProperty
    public String name;

    @JsonProperty("class")
    public String classType;

    @JsonProperty
    public int gender;

    @JsonProperty
    public int level;

    @JsonProperty("skills")
    public int skills;

    @JsonProperty("items")
    public Items items;

    // followers

    @JsonProperty("stats")
    public Stats stats;
}
