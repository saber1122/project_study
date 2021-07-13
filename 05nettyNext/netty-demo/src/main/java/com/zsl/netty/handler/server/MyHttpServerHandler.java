package com.zsl.netty.handler.server;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.ParameterMetaData;
import java.util.List;
import java.util.Map;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/7/1 21:26
 */
@Slf4j
public class MyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final HttpDataFactory HTTP_DATA_FACTORY = new DefaultHttpDataFactory(DefaultHttpDataFactory.MAXSIZE);

    static {
        DiskFileUpload.baseDirectory = "/opt/netty/fileupload";
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {

        //    根据不同的请求方式做处理
        HttpMethod method = fullHttpRequest.method();
        if (HttpMethod.GET.equals(method)) {
            parseGet(fullHttpRequest);
        } else if (HttpMethod.POST.equals(method)) {
            parsePost(fullHttpRequest);
        } else {
            log.error("{} method is not supported ,please change http method for get or post!", method);
        }
        //给客户端做出响应
        //response client
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<h3>success</h3>");
        sb.append("</body>");
        sb.append("</html>");
        writeResponse(ctx,fullHttpRequest,HttpResponseStatus.OK,sb.toString());

    }

    /**
     * 处理 POST 请求
     *
     * @param fullHttpRequest
     */
    private void parsePost(FullHttpRequest fullHttpRequest) {
        String contentType = getContentType(fullHttpRequest);
        switch (contentType) {
            case "application/json":
                parseJson(fullHttpRequest);
                break;
            case "application/x-www-form-urlencoded":
                parseFormData(fullHttpRequest);
                break;
            case "multipart/form-data":
                parseMultipart(fullHttpRequest);
                break;
            default:
        }
    }

    /**
     * 给客户端做出相应
     * @param ctx
     * @param fullHttpRequest
     * @param status
     * @param msg
     */
    private void writeResponse(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest, HttpResponseStatus status, String msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,status);
        //具体响应体的数据，即sb中的数据
        response.content().writeBytes(msg.getBytes(StandardCharsets.UTF_8));
        //设置响应头
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html;charset=utf-8");
        //设置响应体的数据大小
        HttpUtil.setContentLength(response,response.content().readableBytes());
        //保持连接不断，下次可能还用得到
        boolean keepAlive = HttpUtil.isKeepAlive(fullHttpRequest);
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION,"keep-alive");
            ctx.writeAndFlush(response);
        }else {
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }
    /**
     * 解析 文件上传，同样也适用于 解析普通的表单提交请求
     * 通用（普通的post，文件上传 ）
     *
     * @param fullHttpRequest
     */
    private void parseMultipart(FullHttpRequest fullHttpRequest) {
        //有什么用？
        HttpPostRequestDecoder postRequestDecoder = new HttpPostRequestDecoder(HTTP_DATA_FACTORY, fullHttpRequest);
        //判断是否是multipart
        if (postRequestDecoder.isMultipart()) {
            //    获取body中的数据
            List<InterfaceHttpData> bodyHttpDatas = postRequestDecoder.getBodyHttpDatas();
            bodyHttpDatas.stream().forEach(dataItem -> {
                //    判断表单类型
                InterfaceHttpData.HttpDataType dataType = dataItem.getHttpDataType();
                if (dataType.equals(InterfaceHttpData.HttpDataType.Attribute)) {
                    //    普通表单项 如：  <label for="username">用户名:</label><input id="username" type="text" name="username">
                    Attribute attribute = (Attribute) dataItem;
                    try {
                        log.info("表单项名称:{},表单项值:{}", attribute.getName(), attribute.getValue());
                    } catch (IOException e) {
                        log.error("获取表单项数据错误,msg={}", e.getMessage());
                    }
                } else if (dataItem.equals(InterfaceHttpData.HttpDataType.FileUpload)) {
                    //    文件上传
                    //    将文件保存到磁盘
                    FileUpload fileUpload = (FileUpload) dataItem;
                    //    获取原始文件名称
                    String filename = fileUpload.getFilename();
                    //    获取表单名称
                    String name = fileUpload.getName();
                    log.info("文件名称:{},表单项名称:{}",filename,name);

                    //    将文件保存到磁盘
                    //    文件是否准备好
                    if (fileUpload.isCompleted()) {
                        try {
                            String path = DiskFileUpload.baseDirectory + File.separator + filename;
                            //File file = fileUpload.getFile();
                            fileUpload.renameTo(new File(path));
                        } catch (IOException e) {
                            log.error("文件转存失败,msg={}",e.getMessage());
                        }

                    }
                }
            });
        }

    }

    /**
     * 解析 form 表单
     *
     * @param fullHttpRequest
     */
    private void parseFormData(FullHttpRequest fullHttpRequest) {
        //    请求体和url中可能同时都有参数
        parseKVstr(fullHttpRequest.uri(), true);
        //解析请求体中的参数
        parseKVstr(fullHttpRequest.content().toString(StandardCharsets.UTF_8), false);
    }

    /**
     * 解析json格式
     *
     * @param fullHttpRequest
     */
    private void parseJson(FullHttpRequest fullHttpRequest) {
        String jsonstr = fullHttpRequest.content().toString(StandardCharsets.UTF_8);
        //使用json工具反序列化
        JSONObject jsonObject = JSONObject.parseObject(jsonstr);
        //打印 json数据
        jsonObject.entrySet().stream().forEach(entry -> {
            log.info("json key={},json value={}", entry.getKey(), entry.getValue());
        });
    }

    /**
     * 获取contentType
     *
     * @param fullHttpRequest
     * @return
     */
    private String getContentType(FullHttpRequest fullHttpRequest) {
        HttpHeaders headers = fullHttpRequest.headers();
        //content-type: text/plain;charset=utf-8
        String contentType = headers.get(HttpHeaderNames.CONTENT_TYPE);
        return contentType.split(";")[0];
    }

    /**
     * 处理 Get 请求
     *
     * @param fullHttpRequest
     */
    private void parseGet(FullHttpRequest fullHttpRequest) {
        //    根据 url 获取参数
        parseKVstr(fullHttpRequest.uri(), true);
    }

    /**
     * @param str  url地址
     * @param path 是否包含路径
     */
    private void parseKVstr(String str, boolean path) {
        // 通过    QueryStringDecoder 解析 kv 字符串
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(str, path);
        //以key-value的格式获取参数
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        //封装参数，执行业务，此处打印即可
        if (parameters != null && parameters.size() > 0) {
            parameters.entrySet().stream().forEach(entry -> {
                log.info("参数名:{},参数值:{}", entry.getKey(), entry.getValue());
            });
        }

    }
}
