package com.email.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.util.Map;

/**
 * FreeMarker模板处理接口
 * <p>
 *  缺省服务接口不依赖任何配置，只在需要用到此接口时才需要在Spring中配置
 *  {@link org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer}。
 * </p>
 * @author chenbin at 2017/10/26 11:55
 */
public class FreeMarkerBean {
    /**
     * 依赖Web Application中模板的配置。
     *
     * 由于FreeMarker模板配置在不同的模块中可能会有不同的方式，因此
     * 将具体的注入留到具体模块中去配置，这里允许为空。
     */
    @Autowired(required = false)
    protected FreeMarkerConfig freeMarkerConfig;

    public String getText(String templateName,Map<String,Object> params) {
        return getText(templateName,"UTF-8",params);
    }

    /**
     * 制定编码通过模板名称获取实际内容
     * @param templateName 模板名称
     * @param encoding
     * @param params
     * @return
     */
    private String getText(String templateName, String encoding, Map<String, Object> params) {
        if (freeMarkerConfig == null) {
            throw new IllegalStateException("使用FreeMarker模板之前必须制定的配置策略：FreeMarkerConfig实现！");
        }
        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(
                    freeMarkerConfig.getConfiguration().getTemplate(templateName, encoding), params);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
