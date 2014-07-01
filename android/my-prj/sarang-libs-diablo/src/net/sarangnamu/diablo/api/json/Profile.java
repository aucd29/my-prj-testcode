/*
 * Profile.java
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

import java.util.ArrayList;

import net.sarangnamu.diablo.api.json.profile.Heroes;
import net.sarangnamu.diablo.api.json.profile.Kills;
import net.sarangnamu.diablo.api.json.profile.TimePlayed;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <pre>
 * {@code
 *
 * }
 * </pre>
 *
 * @see http://kr.battle.net/api/d3/profile/burke-1935/
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class Profile {
    @JsonProperty
    public ArrayList<Heroes> horoes;

    @JsonProperty
    public long lastHeroPlayed;

    @JsonProperty
    public long lastUpdated;

    @JsonProperty
    public Kills kills;

    @JsonProperty
    public TimePlayed timePlayed;

    @JsonProperty
    public ArrayList<String> fallenHeroes; // temp

    @JsonProperty
    public int paragonLevel;

    @JsonProperty
    public int paragonLevelHardcore;

    @JsonProperty
    public String battleTag;

    @JsonProperty
    public ArrayList<String> progression;
}
