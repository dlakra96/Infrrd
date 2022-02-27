package com.infrrd.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegexMatchReqDTO {

    @NotBlank(message = "provide some value for regular expression")
    String regex;
    @NotBlank(message = "provide some value for input text body")
    String textBody;


}
