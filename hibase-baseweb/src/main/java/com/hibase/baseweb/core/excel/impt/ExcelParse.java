package com.hibase.baseweb.core.excel.impt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.hibase.baseweb.core.exception.excel.ExcelBusinessException;
import com.hibase.baseweb.utils.ValidationUtils;
import com.hibase.baseweb.valid.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * excel转化类
 *
 * @author chenfeng
 * @create 2019-04-25 21:11
 */
@Slf4j
public class ExcelParse<T> {

    public List<T> parseExcel(Class parstType, InputStream inputStream){

        return this.parseExcel(parstType, inputStream, 1, 1);
    }

    /**
     * 解析表格
     *
     * @param parstType   解析泛型
     * @param inputStream 传入流
     * @param sheetNo 读取指定的工作表格，为0读取所有的
     * @param headLineMun 标题从第几行开始
     * @return
     */
    public List<T> parseExcel(Class parstType, InputStream inputStream, int sheetNo, int headLineMun) {

        InputStream inputStream1 = new BufferedInputStream(inputStream);

        List<T> result = (List<T>) EasyExcelFactory.read(inputStream1, new Sheet(sheetNo, headLineMun, parstType));

        if (CollUtil.isNotEmpty(result)) {

            int i = 1 + headLineMun;

            // 替换el表达式
            ExpressionParser ep = new SpelExpressionParser();

            EvaluationContext ctx = new StandardEvaluationContext();

            for (T temp : result) {

                ValidationResult validationResult = ValidationUtils.validateEntity(temp);

                if (validationResult.hasErrors()) {

                    String msg = validationResult.getDefaultMessage();

                    ctx.setVariable("count", i);

                    if (StrUtil.isNotEmpty(msg)) {

                        msg = ep.parseExpression(msg, new TemplateParserContext()).getValue(ctx).toString();

                    }

                    throw new ExcelBusinessException(msg);
                }

                i++;
            }
        }

        return result;
    }
}
