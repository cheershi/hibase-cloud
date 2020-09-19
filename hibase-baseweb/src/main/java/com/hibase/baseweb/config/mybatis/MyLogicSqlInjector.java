package com.hibase.baseweb.config.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.methods.Insert;
import com.baomidou.mybatisplus.extension.injector.methods.*;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 自定义逻辑删除注入器
 *
 * @author chenfeng
 * @date 2019/03/26
 */
public class MyLogicSqlInjector extends AbstractSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList() {
        return Stream.of(
                new Insert(),
                new LogicDelete(),
                new LogicDeleteByMap(),
                new LogicDeleteById(),
                new LogicDeleteBatchByIds(),
                new LogicUpdate(),
                new MyLogicUpdateById(),
                new MyLogicUpdateNullById(),
                new LogicSelectById(),
                new LogicSelectBatchByIds(),
                new LogicSelectByMap(),
                new LogicSelectOne(),
                new LogicSelectCount(),
                new LogicSelectMaps(),
                new LogicSelectMapsPage(),
                new LogicSelectObjs(),
                new LogicSelectList(),
                new LogicSelectPage()
        ).collect(toList());
    }
}
