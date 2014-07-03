/*
 * ItemInfo.java
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

import net.sarangnamu.diablo.api.json.iteminfo.gems.Gem;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @see http://blizzard.github.io/d3-api-docs/#item-information/item-information-example
 * @see http://bit.ly/1z9ANVs
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class ItemInfo {
    @JsonProperty
    public String id;

    @JsonProperty
    public String name;

    @JsonProperty
    public String icon;

    @JsonProperty
    public String displayColor;

    @JsonProperty
    public String tooltipParams;

    @JsonProperty
    public String requiredLevel;

    @JsonProperty
    public String itemLevel;

    @JsonProperty
    public int bonusAffixes;

    @JsonProperty
    public String bonusAffixesMax;

    @JsonProperty
    public boolean accountBound;

    @JsonProperty
    public String flavorText;

    @JsonProperty
    public String typeName;

    // type

    @JsonProperty
    public String armor;

    // attributes
    @JsonProperty("attributes")
    public String attributes;

    // attributesRaw

    // randomAffixes

    // gems
    @JsonProperty("gems")
    public ArrayList<Gem> gems;

    // socketEffects

    // set

    // slug

    // ranks

    // craftedBy
}
