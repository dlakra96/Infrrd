package com.infrrd.service;

import com.infrrd.dto.RegexMatchRespDTO;

public interface RegexMatchService {

    RegexMatchRespDTO matchInputTextAgainstRegexExp(String regexExp,String textBody);

}
