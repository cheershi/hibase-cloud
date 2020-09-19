package com.hibase.baseweb.core.exception.excel;


import com.hibase.baseweb.constant.exception.ExceptionCode;
import com.hibase.baseweb.core.exception.HibaseException;

/**
 * 表格业务异常
 *
 * @author chenfeng
 * @date 2019/03/28
 */
public class ExcelBusinessException extends HibaseException {

    public ExcelBusinessException() {
    }

    public ExcelBusinessException(int code, String message) {
        super(code, message);
    }

    public ExcelBusinessException(int code , String message, Throwable cause) {
        super(code, message, cause);
    }

    public ExcelBusinessException(String message) {
        super(ExceptionCode.EXCEL_BUSINESS_EXCEPTION.getCode(), message);
    }

    public ExcelBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
