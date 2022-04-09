package com.rw.testapp.common;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class Problem {
    String detail;
    int statusCode;
}
