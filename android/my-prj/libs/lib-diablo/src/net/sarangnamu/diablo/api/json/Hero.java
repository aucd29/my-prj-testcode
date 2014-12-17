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
import net.sarangnamu.diablo.api.json.hero.Skills;
import net.sarangnamu.diablo.api.json.hero.Stats;
import net.sarangnamu.diablo.api.json.hero.progression.Progression;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @see http://blizzard.github.io/d3-api-docs/#
 * @see http://kr.battle.net/api/d3/profile/burke-1935/hero/12541198
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
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

    @JsonProperty
    public int paragonLevel;

    @JsonProperty
    public boolean hardcore;

    @JsonProperty("skills")
    public Skills skills;

    @JsonProperty("items")
    public Items items;

    // followers

    @JsonProperty("stats")
    public Stats stats;

    // kills

    @JsonProperty("progression")
    public Progression progression;

    @JsonProperty
    public boolean dead;

    @JsonProperty("last-updated")
    public long lastUpdated;
}
