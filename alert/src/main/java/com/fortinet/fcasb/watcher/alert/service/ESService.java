package com.fortinet.fcasb.watcher.alert.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fortinet.fcasb.watcher.alert.domain.Alert;
import com.fortinet.fcasb.watcher.alert.init.RestWrapper;
import com.fortinet.fcasb.watcher.alert.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zliu on 17/3/3.
 */
@Service
public class ESService {
    public static final Logger LOGGER = LoggerFactory.getLogger(ESService.class);

    private static final String SEARCH_DEFAULT_TYPE="log";
    private static final int SEARCH_SIZE=1000;
    private static final int SEARCH_FROM=0;


    @Autowired
    private RestWrapper restWrapper;


    private Map<String,Object> restSearch(Alert alert,Map<String,Object> searchFilter){
        String host = "";
        String port = "";
        if(StringUtils.isNotBlank(alert.getHost())&& StringUtils.isNumeric(alert.getPort())){
             host = alert.getHost();
             port = alert.getPort();
        }else{
            return null;
        }
        String url = "http://"+host+":"+port+"/"+alert.getIndex()+"/_search";
        ResponseEntity<Map<String,Object>> re = restWrapper.post(url,searchFilter,new TypeReference<Map<String,Object>>(){});
        return re.getBody();
    }


    public Map<String,Object> search(Alert alert,Date startTime,Date endTime) throws Exception {
        //根据alert查询条件
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        if(StringUtils.isNotBlank(alert.getSearchkey())) {
            boolQueryBuilder.must(QueryBuilders.queryStringQuery(alert.getSearchkey()));
        }

        if(alert.getFilter()!=null){
            Map<String,String> filter = JSON.parseObject(alert.getFilter(), HashMap.class);
            for(Map.Entry<String,String> value:filter.entrySet()){
                boolQueryBuilder.must(QueryBuilders.termQuery(value.getKey(), value.getValue()));
            }
        }

        boolQueryBuilder.must(QueryBuilders.rangeQuery("@timestamp").format("epoch_millis").gte(startTime.getTime()).lte(endTime.getTime()));

        LOGGER.info("current time :{}",StringUtil.getCurrentWholeMinTime());
        Map<String,Object> params = new HashMap<>();
        params.put("query", JSON.parse(boolQueryBuilder.toString()));
        params.put("size",SEARCH_SIZE);
        return restSearch(alert,params);


    }



}
