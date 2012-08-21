package org.openthinks.generic.sql;

import java.util.List;

public interface PaginationQuery {
	int getTotality(String sql) throws Exception;    
    List<Object> selectPageDataByHQL(String sql,int pageSize,int page) throws Exception;
}
