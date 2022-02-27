package com.infrrd.service.impl;

import com.infrrd.dto.RegexMatchRespDTO;
import com.infrrd.exception.BadRegexException;
import com.infrrd.exception.RegexProcessingThreadException;
import com.infrrd.service.RegexMatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Service
public class RegexMatchServiceImpl implements RegexMatchService{

    private static final Logger logger = LoggerFactory.getLogger(RegexMatchServiceImpl.class);


    @Override
    public RegexMatchRespDTO matchInputTextAgainstRegexExp(String regexExp, String textBody){
        RegexMatchRespDTO regexMatchRespDTO = new RegexMatchRespDTO();
        Pattern pattern = null;
        try{
            pattern = Pattern.compile(regexExp);
        }catch(PatternSyntaxException exception) {
            throw new BadRegexException("Regex expression " + regexExp + " is invalid. Please provide valid regex expression");
        }

        Matcher matcher = pattern.matcher(textBody);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executorService.submit(() -> {
            return matcher.find();
        });
        Boolean matchFound = false;
        Boolean flag = false;
        try{
            matchFound = future.get(10,TimeUnit.SECONDS);
        }catch(TimeoutException timeOutException){
            regexExp = replacingRepetitionalQuantifiersWithPossesiveQuantifiers(regexExp);
            Pattern pattern2 = null;
            try{
                pattern2 = Pattern.compile(regexExp);
            }catch(PatternSyntaxException exception){
                throw new BadRegexException("Regex expression " + regexExp + " is invalid. Please provide valid regex expression");
            }
            Matcher matcher2 = pattern2.matcher(textBody);
            matchFound = matcher2.find();
            flag = true;
        }catch(Exception exception){
            logger.error("Exception occured :- " + exception.getMessage());
            throw new RegexProcessingThreadException(exception.getMessage());
        }
        if(matchFound)
        {
            regexMatchRespDTO.setMatch(matcher.group());
            regexMatchRespDTO.setError(false);
        }
        else
        {
            if(flag)
                regexMatchRespDTO.setError(true);
            else
                regexMatchRespDTO.setError(false);
            regexMatchRespDTO.setMatch(null);
        }
        return regexMatchRespDTO;
    }

    private String replacingRepetitionalQuantifiersWithPossesiveQuantifiers(String regexPattern){
        StringBuffer newRegexPattern = new StringBuffer("");
        char[] regexChars = regexPattern.toCharArray();
        for(char ch : regexChars){
            if(ch == '*' || ch == '+' || ch == '}' || ch == '?')
            {
                newRegexPattern.append(ch);
                newRegexPattern.append('+');
            }else
                newRegexPattern.append(ch);
        }
        String newRegex = newRegexPattern.toString();
        logger.info("Modified regex pattern :- {}",newRegex);
        return newRegex;
    }
}
