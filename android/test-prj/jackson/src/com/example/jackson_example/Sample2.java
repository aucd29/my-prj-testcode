package com.example.jackson_example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sample2 {
    @JsonProperty
    public String name;

    @JsonProperty
    public Sample data;
}
