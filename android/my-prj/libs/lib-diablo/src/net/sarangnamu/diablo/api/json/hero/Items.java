/*
 * Item.java
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
package net.sarangnamu.diablo.api.json.hero;

import net.sarangnamu.diablo.api.json.hero.items.Item;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Items {
    @JsonProperty("head")
    public Item head;

    @JsonProperty("torso")
    public Item torso;

    @JsonProperty("feet")
    public Item feet;

    @JsonProperty("hands")
    public Item hands;

    @JsonProperty("shoulders")
    public Item shoulders;

    @JsonProperty("legs")
    public Item legs;

    @JsonProperty("bracers")
    public Item bracers;

    @JsonProperty("mainHand")
    public Item mainHand;

    @JsonProperty("offHand")
    public Item offHand;

    @JsonProperty("waist")
    public Item waist;

    @JsonProperty("rightFinger")
    public Item rightFinger;

    @JsonProperty("leftFinger")
    public Item leftFinger;

    @JsonProperty("neck")
    public Item neck;
}
