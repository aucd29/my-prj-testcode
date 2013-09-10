package com.example.jackson_example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sample {
    @JsonProperty
    public String url;

    @JsonProperty
    protected String user;
}
