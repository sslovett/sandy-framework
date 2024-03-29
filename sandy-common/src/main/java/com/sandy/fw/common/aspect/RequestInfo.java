package com.sandy.fw.common.aspect;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class RequestInfo {
    private String ip;
    private String url;
    private String httpMethod;
    private String classMethod;
    private String requestParams;
    private String result;
    private Long timeCost;

    public void print() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===============================Start===============================");
        sb.append("\nURL                : ").append(this.url);
        sb.append("\nRequest IP         : ").append(this.ip);
        sb.append("\nHTTP Method        : ").append(this.httpMethod);
        sb.append("\nClass Method       : ").append(this.classMethod);
        sb.append("\nRequest Args       : ").append(this.requestParams);
        sb.append("\nResponse Args      : ").append(this.result);
        sb.append("\nTime-Consuming     : ").append(timeCost).append(" ms");
        sb.append("\n===============================End==================================");
        log.info(sb.toString());
    }
}
