package com.thestore.microstore.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.thestore.microstore.vo.commom.BaseEntity;

public class ShoppingCartBaseOutput extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 8792913841912846656L;

    /** 服务器版本号 */
    private String version = "";

    /** 请求唯一标示 */
    private String uuid = "";

    /** 服务器IP */
    private String serverIp = "";

    /**请求的服务 */
    private String bizCode = "";

    /**返回的数据 */
    private Map<String, Object> resultData = new HashMap<String, Object>();

    /** 
     * 获取    服务器版本号 
     * @return version 服务器版本号 
     */
    public String getVersion() {
        return version;
    }

    /** 
     * 设置    服务器版本号 
     * @param version 服务器版本号 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /** 
     * 获取    请求唯一标示 
     * @return uuid 请求唯一标示 
     */
    public String getUuid() {
        return uuid;
    }

    /** 
     * 设置    请求唯一标示 
     * @param uuid 请求唯一标示 
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /** 
     * 获取    服务器IP 
     * @return serverIp 服务器IP 
     */
    public String getServerIp() {
        return serverIp;
    }

    /** 
     * 设置    服务器IP 
     * @param serverIp 服务器IP 
     */
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    /** 
     * 获取    请求的服务 
     * @return bizCode 请求的服务 
     */
    public String getBizCode() {
        return bizCode;
    }

    /** 
     * 设置    请求的服务 
     * @param bizCode 请求的服务 
     */
    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    /** 
     * 获取    返回的数据 
     * @return resultData 返回的数据 
     */
    public Map<String, Object> getResultData() {
        return resultData;
    }

    /** 
     * 设置    返回的数据 
     * @param resultData 返回的数据 
     */
    public void setResultData(Map<String, Object> resultData) {
        this.resultData = resultData;
    }

}

