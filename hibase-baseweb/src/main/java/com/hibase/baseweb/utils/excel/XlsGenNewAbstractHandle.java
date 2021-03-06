package com.hibase.baseweb.utils.excel;

/**
 * 对于一般的Java基本数据(包括Date) 都进行了一般处理
 * <p>
 * 但是仍然有一些复杂类型;需要把它转换到Excel,提供了一个抽象类,实现doHandle方法自定义处理即可;
 * <p>
 * 当自定义的Handle放在基本属性上时,优先使用自定义的Handle进行数据处理
 * <p>
 */
public abstract class XlsGenNewAbstractHandle<T,F> {

    public XlsGenNewAbstractHandle() {

    }

    /**
     * 自定义处理
     * @param data 当前实例的当前属性值
     * @param instanse 当前实例值
     * @return
     */
    public abstract String doHandle(T data, F instanse);
}
