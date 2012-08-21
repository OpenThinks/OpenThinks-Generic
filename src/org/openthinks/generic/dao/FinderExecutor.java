package org.openthinks.generic.dao;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public interface FinderExecutor <Type> {
    /**
     * Execute a finder method with the appropriate arguments
     */
    List<Type> executeFind(Method method, Object[] queryArgs);

    Iterator<Type> iterateFind(Method method, Object[] queryArgs);

    //ScrollableResults scrollFind(Method method, Object[] queryArgs);
}
