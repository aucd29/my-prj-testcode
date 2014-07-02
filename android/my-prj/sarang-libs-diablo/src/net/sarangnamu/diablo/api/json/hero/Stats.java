/*
 * Stats.java
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

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stats {
    @JsonProperty
    public int life;

    @JsonProperty
    public int damage;

    @JsonProperty
    public double attackSpeed;

    @JsonProperty
    public int armor;

    @JsonProperty
    public int strength;

    @JsonProperty
    public int dexterity;

    @JsonProperty
    public int vitality;

    @JsonProperty
    public int intelligence;

    @JsonProperty
    public int physicalResist;

    @JsonProperty
    public int fireResist;

    @JsonProperty
    public int coldResist;

    @JsonProperty
    public int lightningResist;

    @JsonProperty
    public int poisonResist;

    @JsonProperty
    public int arcaneResist;

    @JsonProperty
    public float critDamage;

    @JsonProperty
    public int blockChance;

    @JsonProperty
    public int blockAmountMin;

    @JsonProperty
    public int blockAmountMax;

    @JsonProperty
    public int damageIncrease;

    @JsonProperty
    public int critChance;

    @JsonProperty
    public int damageReduction;

    @JsonProperty
    public int thorns;

    @JsonProperty
    public int lifeSteal;

    @JsonProperty
    public int lifePerKill;

    @JsonProperty
    public float goldFind;

    @JsonProperty
    public int magicFind;

    @JsonProperty
    public int lifeOnHit;

    @JsonProperty
    public int primaryResource;

    @JsonProperty
    public int secondaryResource;
}
