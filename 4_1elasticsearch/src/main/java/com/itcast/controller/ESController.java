package com.itcast.controller;

import com.alibaba.fastjson.JSONObject;
import com.itcast.service.HotelService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

@Controller
public class ESController {

    @Autowired
    private HotelService hotelService;


    @GetMapping("/importData")
    @ResponseBody
    public Integer importData() throws IOException {

        return hotelService.addDocToES();
    }
}
